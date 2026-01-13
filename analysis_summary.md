# Análisis Arquitectónico Full-Stack: MyAnki

Este documento detalla la arquitectura de la aplicación **MyAnki**, un sistema de tarjetas de memoria (flashcards) basado en el principio de Repetición Espaciada (SRS). La aplicación sigue un moderno enfoque full-stack, con un backend de API REST construido en Java Spring Boot y un frontend de tipo Single-Page Application (SPA) en React.

---

## 1. Arquitectura General

- **Frontend (`clientpart`)**: Una aplicación React con TypeScript, construida con Vite. Se encarga de toda la interfaz de usuario y la interacción con el cliente.
- **Backend (`servicepart`)**: Una API RESTful robusta construida con Java 21 y Spring Boot. Gestiona la lógica de negocio, la autenticación de usuarios y la persistencia de datos.
- **Base de Datos**: PostgreSQL, con la que se interactúa a través de Spring Data JPA.
- **Comunicación**: La comunicación se realiza a través de peticiones HTTP. El frontend consume los endpoints REST proveídos por el backend.

*(Nota: La funcionalidad de WebSockets que pudo haber existido en el pasado ha sido eliminada por completo del proyecto).*

---

## 2. Backend (`servicepart`)

El backend está diseñado siguiendo una arquitectura en capas clásica, priorizando la seguridad y la lógica de negocio desacoplada.

### a. Estrategia de Autenticación Avanzada (2-JWT)

El sistema utiliza una estrategia de dos Tokens Web JSON (JWT) para proporcionar una sesión segura y una experiencia de usuario fluida:

1.  **Token de Acceso (Access Token)**:
    - **Propósito**: Autorizar cada petición a los endpoints protegidos de la API.
    - **Vida útil**: Muy corta (7 minutos).
    - **Mecanismo**: Es un token JWT estándar (stateless) que contiene la información del usuario (ID, roles). Se envía en la cabecera `Authorization` como `Bearer <token>`.
    - **Validación**: El filtro de seguridad `JwtValidationFilter` lo valida en cada petición. Si ha expirado, lanza una excepción `RefreshAccessException` que resulta en una respuesta `401 Unauthorized`.

2.  **Token de Refresco (Refresh Token)**:
    - **Propósito**: Obtener un nuevo Token de Acceso cuando el actual expira, sin que el usuario tenga que volver a introducir sus credenciales.
    - **Vida útil**: Larga (1 día).
    - **Mecanismo**: Se genera al hacer login y se envía al cliente en una **cookie `HttpOnly` y segura**. Esto lo hace inaccesible para el código JavaScript del navegador, protegiéndolo de ataques XSS.
    - **Seguridad (Stateful)**: A diferencia del token de acceso, este token es "stateful". Cada token de refresco está asociado a una entidad `LoginEntity` en la base de datos. Al hacer logout, la entidad se marca como inactiva, **invalidando el token de refresco en el servidor de forma inmediata**, incluso si su fecha de expiración aún no ha llegado.

### b. Lógica de Negocio: Sistema de Repetición Espaciada (SRS)

El corazón de la aplicación reside en su algoritmo de SRS, implementado en el `DaysCalculatorService`.

- **Gestión de Notas**: El `AppServiceImp` maneja la lógica de negocio principal: crear notas, obtener las notas que el usuario debe repasar hoy (`nextreview <= now`), y actualizar las notas después de un repaso.
- **Algoritmo SRS (`DaysCalculatorService`)**:
    - Utiliza una máquina de estados para cada nota, basada en su `NoteLevel`: `INITIAL`, `NEXT`, `PREGRUADATED`, `GRADUATED`.
    - Cuando un usuario evalúa su respuesta a una nota (ej. "Fácil", "Difícil"), este servicio calcula la próxima fecha de repaso (`nextreview`).
    - Para las notas en estado `GRADUATED`, el cálculo del siguiente intervalo es multiplicativo. Por ejemplo:
        - **Respuesta "Good"**: `nuevo_intervalo = intervalo_anterior * 2.5`
        - **Respuesta "Hard"**: `nuevo_intervalo = intervalo_anterior * 1.5`
    - Esto asegura que las tarjetas fáciles se muestren con menos frecuencia, mientras que las difíciles aparecen más a menudo.

---

## 3. Frontend (`clientpart`)

El frontend es una aplicación moderna y reactiva construida para ofrecer una experiencia de usuario rápida y fluida.

- **Stack Tecnológico**:
    - **Framework**: React 18.
    - **Lenguaje**: TypeScript.
    - **Build Tool**: Vite.
    - **Routing**: **TanStack Router**, que ofrece enrutamiento 100% "typesafe".
    - **Gestión de Estado Asíncrono**: **TanStack Query**, para el fetching, cacheo, y actualización de datos del servidor.
    - **Estilos**: SCSS.

### a. Flujo de Autenticación en el Cliente

El frontend implementa un flujo de autenticación automático y silencioso que se integra perfectamente con el backend.

1.  **Almacenamiento de Tokens**:
    - El **Token de Acceso** se almacena **en memoria**, dentro de un React Context (`ContextProvider.tsx`). Esto lo aísla y evita su exposición en `localStorage`.
    - El **Token de Refresco** no es manejado por JavaScript; el navegador lo gestiona automáticamente a través de la cookie `HttpOnly`.

2.  **Autenticación Inicial**: Al cargar la aplicación, el `ContextProvider` intenta llamar inmediatamente al endpoint `/api/user/refresh`. Si la cookie del token de refresco es válida, el backend devuelve un nuevo token de acceso, que se carga en el estado de la aplicación, autenticando al usuario de forma transparente.

3.  **Renovación Silenciosa de Tokens (Manejo de 401)**:
    - La función `theRefresh` en `userApi.ts` actúa como un "wrapper" o interceptor para las llamadas a la API.
    - Las funciones en `noteApi.ts` (como `getNotesToReview`, `updateNote`, etc.) utilizan este wrapper.
    - **Flujo**:
        1. Se realiza una llamada a la API (ej. para obtener las notas del día).
        2. Si la llamada falla con un error `401 Unauthorized` (porque el token de acceso expiró), el wrapper `theRefresh` lo intercepta.
        3. El wrapper llama automáticamente al endpoint `/api/user/refresh`.
        4. Si tiene éxito, actualiza el token de acceso en el `ContextProvider`.
        5. Finalmente, **reintenta la llamada original fallida**, ahora con el nuevo token.
    - Este proceso es completamente transparente para el usuario, que no experimenta ninguna interrupción.

### b. Componentes Principales de la UI

- **`MyHeader.tsx`**: La barra de navegación principal, muestra información del usuario y opciones de logout.
- **`ShowNotSumary.tsx`**: Muestra un resumen de las notas pendientes de repaso.
- **`AddForm.tsx`**: Un formulario para crear nuevas tarjetas (notas).
- **`ReviewNotes.tsx`**: El componente principal de la aplicación, donde el usuario repasa sus tarjetas. Utiliza **TanStack Query** para obtener las notas, gestionando los estados de carga y mostrando las tarjetas una por una.