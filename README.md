# TaskWave Backend (Java Spring Boot)

## Descripción

Backend del proyecto **TaskWave**, encargado de gestionar usuarios y tareas.  
Implementado en **Java con Spring Boot**, utiliza **JWT** para autenticación y soporta funcionalidades como:  

- Registro de usuarios (contraseña encriptada con BCrypt)  
- Login y generación de **Access Token** y **Refresh Token**  
- Actualización y eliminación de usuarios  
- CRUD de tareas
- Paginacion y filtros
- Escalable para agregar futuras funcionalidades  

---

## ⚙️ Tecnologías

- Java 21+  
- Spring Boot 4.0.1  
- Spring Security (BCrypt + JWT)  
- JPA / Hibernate  
- MySQL
- Maven

---

## 📂 Estructura del proyecto
```bash
taskwave-backend/
│
├─ src/main/java/com/taskwave/...
│   ├─ controller/       <- Endpoints REST
│   ├─ service/          <- Lógica de negocio
│   ├─ repository/       <- Acceso a base de datos (JPA)
|   ├─ DTO/ 
│   ├─ entity/            <- Entidades de la base de datos
│   └─ util/             <- Helpers como JwtUtil
│
├─ src/main/resources/
│   ├─ application.properties  <- Configuración de la app
│
├─ .env                         <- Variable de entorno
├─ pom.xml / build.gradle       <- Dependencias y configuración del build
└─ .gitignore
```
