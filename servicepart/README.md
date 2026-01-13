# Backend de MyAnki: `servicepart`

Este directorio contiene el código fuente del backend de la aplicación **MyAnki**. Es una API RESTful construida con Java y Spring Boot, encargada de la lógica de negocio, la autenticación, la gestión de datos y la implementación del algoritmo de Repetición Espaciada (SRS).

## Tecnologías Utilizadas

*   **Lenguaje**: [Java](https://www.java.com/) 21
*   **Framework**: [Spring Boot](https://spring.io/projects/spring-boot)
*   **Gestión de Dependencias**: [Apache Maven](https://maven.apache.org/)
*   **Base de Datos**: [PostgreSQL](https://www.postgresql.org/)
*   **ORM/Persistencia**: [Spring Data JPA](https://spring.io/projects/spring-data-jpa) e [Hibernate](https://hibernate.org/)
*   **Seguridad**: [Spring Security](https://spring.io/projects/spring-security)
*   **Autenticación**: JSON Web Tokens (JWT) con una estrategia de doble token (Access y Refresh).

## Estructura del Proyecto

El código fuente principal se encuentra en `src/main/java/com/mianki/servicio/servicepart/`. Aquí se organizan las diferentes capas de la arquitectura:

*   **`controllers/`**: Contiene los controladores REST (`@RestController`) que exponen los endpoints de la API, como `AppController` (para la lógica principal de notas) y `UserController` (para la autenticación).
*   **`models/dtos/`**: Objetos de transferencia de datos (DTOs) utilizados para la comunicación entre el frontend y el backend, así como entre capas internas.
*   **`models/entities/`**: Entidades JPA (`@Entity`) que mapean las tablas de la base de datos (e.g., `UserEntity`, `MyNotes`, `LoginEntity`).
*   **`models/enums/`**: Enumeraciones para estados de notas (`NoteLevel`) y opciones de repaso (`OptionNote`).
*   **`repositories/`**: Interfaces de repositorios (`@Repository`) que extienden `JpaRepository` para el acceso a datos.
*   **`security/`**: Configuración de Spring Security, incluyendo el `JwtValidationFilter` para validar los tokens de acceso.
*   **`service/`**: Contiene la lógica de negocio principal y la implementación del algoritmo SRS.
    *   **`imp/`**: Implementaciones de las interfaces de servicio.
    *   **`utils/`**: Servicios de utilidad, notablemente `DaysCalculatorService` que implementa la lógica central del algoritmo de Repetición Espaciada.
*   **`exceptions/`**: Clases de excepciones personalizadas (`MyBadRequestException`, `RefreshAccessException`, `RestartSeccionException`).

## Autenticación

El backend implementa una estrategia de autenticación avanzada basada en dos JWT:

*   **Access Token**: De corta duración, utilizado para autorizar las solicitudes a la API. Se valida en cada petición por `JwtValidationFilter`.
*   **Refresh Token**: De larga duración, enviado al cliente como una cookie `HttpOnly` y segura. Es "stateful" (se registra en la base de datos) y permite obtener nuevos Access Tokens sin requerir las credenciales del usuario nuevamente.

## Algoritmo de Repetición Espaciada (SRS)

El corazón inteligente de MyAnki reside en el `DaysCalculatorService`. Este servicio es responsable de:

*   Determinar la próxima fecha de repaso (`nextreview`) para cada nota.
*   Ajustar los intervalos de repaso basándose en el `NoteLevel` de la nota (`INITIAL`, `NEXT`, `PREGRADUATED`, `GRADUATED`) y la evaluación del usuario (ej., "Good", "Hard").
*   Para notas `GRADUATED`, los intervalos de repaso se expanden multiplicativamente para maximizar la retención a largo plazo.

## Configuración y Ejecución

Para poner en marcha el backend de MyAnki:

1.  **Requisitos**:
    *   [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) 21 o superior.
    *   [Apache Maven](https://maven.apache.org/download.cgi).
    *   Una instancia de base de datos [PostgreSQL](https://www.postgresql.org/download/) funcionando.

2.  **Configuración de la Base de Datos**:
    *   Crea una base de datos PostgreSQL.
    *   Modifica el archivo `src/main/resources/application.properties` para configurar la conexión a tu base de datos:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
        spring.datasource.username=your_username
        spring.datasource.password=your_password
        spring.jpa.hibernate.ddl-auto=update # O 'create-drop' para desarrollo
        ```

3.  **Navega al directorio `servicepart`**:
    ```bash
    cd servicepart
    ```

4.  **Compila el proyecto**:
    ```bash
    ./mvnw clean install
    ```
    Este comando compilará el código y descargará todas las dependencias.

5.  **Ejecuta la aplicación**:
    ```bash
    ./mvnw spring-boot:run
    ```
    Esto iniciará el servidor Spring Boot, que por defecto escuchará en el puerto `8080`.

    Alternativamente, puedes ejecutar el JAR compilado:
    ```bash
    java -jar target/servicepart-0.0.1-SNAPSHOT.jar
    ```
    (El nombre exacto del JAR puede variar ligeramente dependiendo de la versión en `pom.xml`).

## Endpoints Principales de la API

*   `/api/user/login`: Autenticación de usuarios.
*   `/api/user/register`: Registro de nuevos usuarios.
*   `/api/user/refresh`: Refrescar el token de acceso.
*   `/api/user/logout`: Cerrar la sesión del usuario.
*   `/api/user/countnotes`: Obtener el conteo de notas.
*   `/api/app/new-note`: Crear una nueva nota.
*   `/api/app/notes-review`: Obtener notas para repasar hoy.
*   `/api/app/update-note`: Actualizar una nota después del repaso.

---

¡Disfruta desarrollando y usando MyAnki!
