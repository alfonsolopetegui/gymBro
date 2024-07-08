# GymBro - Gym Management System

## Descripción
GymBro es un sistema de gestión de gimnasio que permite a los usuarios registrarse en horarios específicos para diferentes clases.
Este proyecto utiliza Java 17 y Spring Boot para el backend y se conecta a una base de datos MySQL.

## Requisitos
- IntelliJ IDEA (o cualquier otro IDE compatible con Java)
- JDK 17
- MySQL Server
- MySQL Workbench (opcional, para gestión de base de datos)

## Configuración del Proyecto

### Paso 1: Clonar el Repositorio
Clona el repositorio del proyecto en tu máquina local:

git clone https://github.com/alfonsolopetegui/gymBro

### Paso 2: Importar el Proyecto en IntelliJ IDEA
1. Abre IntelliJ IDEA.
2. Selecciona `File > Open` y navega hasta el directorio donde clonaste el repositorio.
3. Selecciona el directorio del proyecto y haz clic en `Open`.

### Paso 3: Configurar la Base de Datos MySQL
1. Asegurate de tener MySQL Server instalado y en ejecución.
2. La primera vez que instalas MySQL Server, te pide crear una contraseña para el usuario "root".
Esta es la contraseña que se usa para crear conexiones y acceder a la base de datos desde la aplicación.
3. Abrí MySQL Workbench, y crea una nueva conexión local. Para asegurarte de que funciona,
podes hacer click en "test connection".
4. La primera vez que ejecutes la aplicación gymBro, la base de datos "gym_bro" debería crearse automáticamente,
debido a la configuración del archivo `application.properties` de Spring.


- Así se ve el archivo `application.properties`, en la carpeta "resources"

spring.application.name=gymBro

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/gym_bro?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=tu_contraseña

spring.jpa.hibernate.ddl-auto=update
logging.level.org.springframework.security.web.*=debug

- Asegurate de colocar tu propia contraseña de MySQL Server, donde dice "tu_contraseña".

- Vuando ejecutes la App, podes mirar los logs en la consola para asegurarte de que la aplicación se conectó
correctamente a la base de datos:

2024-07-04 10:30:00.000  INFO 1234 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2024-07-04 10:30:01.000  INFO 1234 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2024-07-04 10:30:01.000  INFO 1234 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.MySQL5Dialect

- Otra verificación podria ser utilizar el endpoint para TEST:

http://localhost:8080/api/gymBro

## Notas Adicionales
- Asegúrate de que tu servidor MySQL esté en funcionamiento antes de ejecutar la aplicación.
- Si tienes alguna pregunta o encuentras algún problema, por favor revisa la documentación o contacta al administrador del proyecto.

¡Gracias por utilizar GymBro!