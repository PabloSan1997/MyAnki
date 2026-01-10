# Análisis Arquitectónico: Backend `servicepart`

Este documento detalla la arquitectura y el funcionamiento interno del backend `servicepart`, una aplicación Java Spring Boot diseñada para una plataforma de tarjetas de memoria (flashcards) con un enfoque en alta seguridad y una experiencia de usuario en tiempo real.

---

## 1. Arquitectura General

- **Framework**: Java con Spring Boot.
- **Acceso a Datos**: Spring Data JPA.
- **Base de Datos**: PostgreSQL.
- **Seguridad**: Spring Security.
- **Estructura**: Arquitectura en capas bien definida:
    - `controllers`: Exponen la API REST y manejan las peticiones HTTP.
    - `services`: Contienen la lógica de negocio principal.
    - `repositories`: Definen la capa de acceso a datos con interfaces de Spring Data.
    - `models`: Incluyen las entidades JPA, DTOs y enumeraciones.

---

## 2. Gestión de Sesiones y Autenticación: Estrategia de 3 Tokens

El sistema de autenticación es el componente más sofisticado, utilizando tres tipos de JSON Web Tokens (JWT) para maximizar la seguridad y el control.

### Flujo de Autenticación
1.  **Registro/Login**: El usuario se registra o inicia sesión a través de un endpoint REST (`/api/user/register` o `/api/user/login`).
2.  **Recepción de Tokens**: Tras una autenticación exitosa, el servidor devuelve un `DoubleTokenDto` que contiene:
    - Un **Token de Acceso** en el cuerpo de la respuesta.
    - Un **Token de Login/Refresco** también en el cuerpo, que el cliente debe almacenar de forma segura (por ejemplo, en una cookie `HttpOnly`).

### Los 3 Tokens

#### a. Token de Acceso
- **Propósito**: Autorizar las peticiones a la API (ej. obtener notas, actualizar perfil).
- **Vida útil**: Muy corta (ej. 5-15 minutos).
- **Contenido**: ID de usuario, nombre de usuario y roles (`authorities`).
- **Manejo de Expiración**: Si el token expira, la API devuelve un error (`RefreshAccessException`) que instruye al cliente para que use el Token de Refresco.

#### b. Token de Login/Refresco
- **Propósito**: Obtener un nuevo Token de Acceso cuando el actual expira.
- **Vida útil**: Larga (ej. días o semanas).
- **Característica Clave**: Este token está **vinculado a una sesión (`LoginEntity`) en la base de datos**.
- **Seguridad Avanzada (Revocación en el Servidor)**:
    - Al hacer login, se crea una nueva fila `LoginEntity` con estado `activo`.
    - Al validar el token, el servidor comprueba no solo la firma JWT, sino también que la `LoginEntity` asociada siga `activa`.
    - Al hacer logout, el estado de la `LoginEntity` se cambia a `inactivo`, **invalidando el token de refresco de forma inmediata en el servidor**, incluso si no ha expirado.

#### c. Token de WebSocket
- **Propósito**: Autentizar *únicamente* la conexión WebSocket inicial.
- **Vida útil**: Ultra corta (ej. 30-60 segundos).
- **Flujo**:
    1. El cliente solicita este token a un endpoint REST (`/api/user/socket`) justo antes de conectarse.
    2. El cliente usa este token como parámetro en la URL de conexión del WebSocket (ej. `ws://.../getinfo?jwt=...`).
    3. El token es de un solo uso y expira rápidamente para minimizar su exposición.

---

## 3. Lógica de Negocio: Sistema de Repetición Espaciada (SRS)

La funcionalidad principal gira en torno a la gestión de notas o flashcards para estudiar.

- **Seguridad**: Todas las operaciones con notas están securizadas a nivel de usuario. Los métodos de servicio siempre recuperan al usuario del `SecurityContextHolder` para asegurar que solo puede acceder a sus propios datos.
- **Algoritmo SRS**: La lógica para calcular la próxima fecha de repaso de una nota (`nextreview`) se delega al `DaysCalculatorService`. Este servicio toma una nota y la respuesta del usuario (ej. "fácil", "difícil") para determinar el siguiente intervalo de estudio.
- **Obtención de Notas para Repaso**: El endpoint principal (`GET /api/notes`) devuelve solo las notas cuya fecha `nextreview` es anterior o igual a la fecha actual, formando la base del sistema SRS.

---

## 4. WebSockets para Notificaciones en Tiempo Real

El sistema utiliza WebSockets con STOMP para una experiencia de usuario dinámica.

- **Endpoint**: La conexión se establece en `/getinfo`.
- **Autenticación**:
    1. Un `JwtInterceptor` intercepta la petición de handshake del WebSocket.
    2. Valida el **Token de WebSocket** de un solo uso pasado como parámetro en la URL.
    3. Si es válido, extrae el nombre de usuario y lo almacena en los atributos de la sesión.
    4. Un `UserHandshakeHandler` toma este nombre de usuario y lo establece como el `Principal` de la sesión del WebSocket, permitiendo el envío de mensajes dirigidos.
- **Uso Práctico**:
    - Después de que un usuario crea, elimina o reinicia una nota, el `AppServiceImp` usa `SimpMessagingTemplate` para enviar la lista actualizada de notas repasables.
    - El mensaje se envía al destino privado del usuario (`/user/{username}/notes/all`), lo que provoca que su interfaz de usuario se actualice en tiempo real sin necesidad de recargar la página.

---

## 5. Archivos y Componentes Clave

- **`UserServiceImp`**: Orquesta el registro, login y la generación inicial de tokens.
- **`JwtServicesImp`**: Implementa la lógica de creación y validación para los tres tipos de JWT y gestiona la capa de persistencia de la sesión (`LoginEntity`).
- **`AppServiceImp`**: Contiene la lógica de negocio para la gestión de notas y la interacción con el `DaysCalculatorService` y los WebSockets.
- **`WebSocketConfig`**: Configura los endpoints de STOMP, el broker de mensajes y los interceptores de seguridad.
- **`JwtInterceptor` / `UserHandshakeHandler`**: Trabajan en conjunto para asegurar las conexiones WebSocket.
- **`pom.xml`**: Define todas las dependencias del proyecto (Spring Boot, Security, JPA, JWT, etc.).
