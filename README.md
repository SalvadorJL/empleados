API REST para gestión de empleados con Spring Boot y Docker

## 📋 Requirenments
- Java 17
- Maven 3.8+
- MySQL 8.x
- Docker (optional)

## 🚀 Instalation

### 1. Clonar repositorio
```bash
git clone https://github.com/SalvadorJL/empleados-api.git
cd empleados-api
```

### 2. Create database
```sql
CREATE DATABASE empleados;
```
Configure aplicacion.properties

```properties
server.port=8081

spring.datasource.url=jdbc:mysql://localhost:3306/employee?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

spring.h2.console.enabled=true

springdoc.api-docs.version=OPENAPI_3_0

logging.level.root=INFO
logging.level.org.springframework.web=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %logger{50} - %msg%n

management.endpoints.web.exposure.include=health,info
```

### 3. Build and run the project
```bash
mvn clean install
mvn spring-boot:run
```

### 4. Documentation

```bash
http://localhost:8081/swagger-ui/index.html

Endpoints:

GET /api/employees - List all employees

POST /api/employees - Create new employee

PUT /api/employees/{id} - Update employee

DELETE /api/employees/{id} - Delete employee
```
