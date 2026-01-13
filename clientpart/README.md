# Frontend de MyAnki: `clientpart`

Este directorio contiene el código fuente del frontend de la aplicación **MyAnki**. Es una Single-Page Application (SPA) construida con tecnologías modernas para proporcionar una interfaz de usuario rápida, reactiva y fluida.

## Tecnologías Utilizadas

*   **Framework**: [React](https://react.dev/) v18
*   **Lenguaje**: [TypeScript](https://www.typescriptlang.org/)
*   **Build Tool**: [Vite](https://vitejs.dev/)
*   **Gestión de Rutas**: [TanStack Router](https://tanstack.com/router/) (enrutamiento 100% "typesafe")
*   **Gestión de Estado Asíncrono**: [TanStack Query](https://tanstack.com/query/) (para fetching, cacheo y actualización de datos del servidor)
*   **Estilos**: [SCSS](https://sass-lang.com/) (Sass)
*   **Manejo de API**: `fetch` API estándar y un sistema de interceptores para la renovación silenciosa de tokens.

## Estructura del Proyecto

El corazón de la aplicación se encuentra en el directorio `src/`. Aquí se organizan los componentes, rutas, lógica de API y estilos:

*   **`src/api/`**: Contiene la lógica para interactuar con la API del backend, incluyendo los servicios `noteApi.ts` y `userApi.ts`, y el mecanismo `theRefresh` para la renovación automática de tokens.
*   **`src/components/`**: Componentes UI reutilizables como `AddForm.tsx`, `MyHeader.tsx`, `ReviewNotes.tsx`, `ShowNotSumary.tsx`.
*   **`src/integrations/tanstack-query/`**: Configuración e integración de TanStack Query.
*   **`src/routes/`**: Definición de las rutas de la aplicación, utilizando TanStack Router.
*   **`src/styles/`**: Archivos SCSS para los estilos de la aplicación.
*   **`src/ContextProvider.tsx`**: Contexto global de React para manejar el estado de autenticación y el Token de Acceso.
*   **`src/main.tsx`**: Punto de entrada principal de la aplicación React.
*   **`src/routeTree.gen.ts`**: Archivo generado automáticamente por TanStack Router para el enrutamiento typesafe.

## Funcionalidades Clave del Frontend

*   **Autenticación Transparente:** Manejo automático de la autenticación del usuario. Al iniciar la aplicación, intenta refrescar el token de acceso. Si el token de acceso expira durante una sesión, el sistema lo renueva de forma silenciosa e imperceptible para el usuario, reintentando la petición original.
*   **Gestión de Sesión:** El token de acceso se guarda en memoria (React Context), no en `localStorage`, aumentando la seguridad. El token de refresco es gestionado por el navegador a través de una cookie `HttpOnly`.
*   **Repaso de Notas:** Interfaz para mostrar las tarjetas a repasar, permitiendo al usuario evaluar su conocimiento (fácil, difícil, etc.) y avanzar en el algoritmo SRS.
*   **Creación de Notas:** Formularios para añadir nuevas tarjetas al sistema.
*   **Visualización de Progreso:** Resúmenes y estadísticas básicas del estado de las notas.

## Configuración y Ejecución

Para poner en marcha el frontend de MyAnki:

1.  **Asegúrate de tener el backend (`servicepart`) en ejecución.** El frontend necesita comunicarse con la API del backend.
2.  **Navega al directorio `clientpart`**:
    ```bash
    cd clientpart
    ```
3.  **Instala las dependencias**:
    ```bash
    npm install
    # o
    yarn install
    ```
4.  **Inicia el servidor de desarrollo**:
    ```bash
    npm run dev
    # o
    yarn dev
    ```
    Esto iniciará la aplicación en modo de desarrollo, generalmente en `http://localhost:5173` (o un puerto similar). El navegador se abrirá automáticamente.

5.  **Compilación para producción**:
    ```bash
    npm run build
    # o
    yarn build
    ```
    Este comando generará los archivos estáticos de la aplicación optimizados para producción en el directorio `dist/`.

## Scripts Disponibles

En el `package.json`, encontrarás los siguientes scripts útiles:

*   `dev`: Inicia el servidor de desarrollo de Vite.
*   `build`: Compila la aplicación para producción.
*   `lint`: Ejecuta ESLint para revisar el código.
*   `preview`: Sirve la build de producción localmente.
*   `generate-route-types`: Genera los tipos de rutas para TanStack Router.

---

¡Disfruta desarrollando y usando MyAnki!
