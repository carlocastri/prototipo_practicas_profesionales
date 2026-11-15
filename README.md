# Plataforma de Prácticas Profesionales — Prototipo funcional

Prototipo del Taller integrador de la Unidad 3 del curso Ingeniería de software
(Fundación Universitaria Católica del Norte). Implementa dos módulos de la plataforma
diseñada en el Taller 2:

1. **Registro de estudiantes y empresas** (RF03, RF04): la empresa se registra con NIT único,
   queda *pendiente de validación* y el coordinador la aprueba.
2. **Publicación de vacantes y postulación** (RF05, RF06, RF08, RF09): solo las empresas aprobadas
   publican vacantes; el estudiante se postula con autorización de tratamiento de datos y no puede
   postularse dos veces a la misma vacante.

## Tecnologías

| Capa | Tecnología |
| --- | --- |
| Backend | Java 21+ · Spring Boot 4.1 (Web MVC, Data JPA, Bean Validation) |
| Frontend | Angular 22 · TypeScript |
| Base de datos | H2 en memoria (entorno de prueba) · perfil `postgres` para PostgreSQL |
| Pruebas | JUnit 5 · Mockito · Spring Boot Test (MockMvc) |
| Integración continua | GitHub Actions |

## Estructura

```
backend/    API REST Spring Boot (paquetes presentacion, negocio, persistencia, transversal)
frontend/   Aplicación Angular (código fuente)
.github/    Flujo de integración continua
```

La versión compilada del frontend se incluye en `backend/src/main/resources/static`, de modo que
el prototipo completo se ejecuta con un solo comando.

## Requisitos

- JDK 21 o superior
- Maven 3.9 o superior
- Node.js 22 o superior (solo para modificar el frontend)

## Ejecutar el prototipo

```bash
cd backend
mvn spring-boot:run
```

Abrir <http://localhost:8080>.

## Ejecutar las pruebas

```bash
cd backend
mvn test
```

Se ejecutan 9 pruebas: 7 unitarias (CP01 a CP07) y 2 de integración (PI01 y PI02).

## Desarrollo del frontend

```bash
cd frontend
npm install
npm start          # http://localhost:4200, con proxy de /api hacia el backend en :8080
```

Para actualizar la versión que sirve el backend:

```bash
npm run build
cp -r dist/frontend/browser/* ../backend/src/main/resources/static/
```

## API REST

| Método | Ruta | Descripción |
| --- | --- | --- |
| POST | `/api/empresas` | Registrar empresa (queda pendiente de validación) |
| GET | `/api/empresas` | Listar empresas |
| PUT | `/api/empresas/{id}/aprobar` | Aprobar empresa |
| POST | `/api/estudiantes` | Registrar estudiante |
| GET | `/api/estudiantes` | Listar estudiantes |
| POST | `/api/vacantes` | Publicar vacante |
| GET | `/api/vacantes` | Listar vacantes vigentes |
| POST | `/api/postulaciones` | Postularse a una vacante |
| GET | `/api/postulaciones` | Listar postulaciones |

## Limitaciones del prototipo

No incluye autenticación ni control de acceso por rol, validación de vacantes por la coordinación,
verificación de elegibilidad con el sistema académico ni carga de documentos. Estas funciones
corresponden a los siguientes sprints del plan del Taller 2.
