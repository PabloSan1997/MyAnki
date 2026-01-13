# MyAnki: Tu Compañero de Repaso con Tarjetas de Memoria

¡Bienvenido a **MyAnki**! Esta es una aplicación full-stack diseñada para ayudarte a dominar cualquier tema mediante el uso eficiente de tarjetas de memoria (flashcards) y el principio de la Repetición Espaciada (SRS - Spaced Repetition System). Ya sea que estés estudiando para exámenes, aprendiendo un nuevo idioma o memorizando conceptos complejos, MyAnki está aquí para optimizar tu proceso de aprendizaje.

## ¿Qué es MyAnki?

MyAnki es una plataforma robusta y moderna que te permite:

*   **Crear y Gestionar Tarjetas:** Diseña tus propias tarjetas de memoria con preguntas y respuestas, organizándolas a tu medida.
*   **Repetición Espaciada Inteligente:** Un algoritmo avanzado calcula automáticamente cuándo debes revisar cada tarjeta, asegurando que repases la información justo antes de olvidarla. Esto maximiza la retención a largo plazo y minimiza el tiempo de estudio.
*   **Experiencia de Usuario Fluida:** Una interfaz intuitiva y reactiva te permite concentrarte en lo que realmente importa: aprender.

## Arquitectura del Proyecto

MyAnki está construido con una arquitectura moderna de microservicios, dividida en dos componentes principales:

1.  **`clientpart` (Frontend):** La interfaz de usuario que interactúas en tu navegador.
    *   Desarrollado con **React** y **TypeScript**.
    *   Construido con **Vite** para un desarrollo rápido y eficiente.
    *   Gestiona la presentación de las tarjetas, la navegación y la interacción del usuario.

2.  **`servicepart` (Backend):** El cerebro de la aplicación que gestiona la lógica de negocio, los datos y la autenticación.
    *   Construido con **Java 21** y el framework **Spring Boot**.
    *   Ofrece una **API RESTful** para que el frontend se comunique y recupere/envíe datos.
    *   Utiliza una estrategia avanzada de **autenticación basada en JWT** para mantener tus datos seguros.
    *   Almacena la información de usuarios y tarjetas en una base de datos **PostgreSQL**.

## Funcionalidades Clave

*   **Autenticación Segura:** Sistema de login y registro con una gestión de tokens JWT (Access y Refresh tokens) para proteger tu sesión.
*   **Gestión de Notas Personalizadas:** Crea, edita y organiza tus tarjetas de memoria.
*   **Algoritmo de Repetición Espaciada:** El corazón de MyAnki, que adapta dinámicamente los intervalos de repaso a tu rendimiento.
*   **Panel de Repaso:** Un espacio dedicado donde puedes revisar tus tarjetas programadas para el día.

## Empezando

Para obtener instrucciones detalladas sobre cómo configurar y ejecutar tanto el frontend como el backend, por favor, consulta los archivos `README.md` específicos en los directorios `clientpart` y `servicepart` respectivamente.


