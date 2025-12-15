# TPDise-o2025
tp diseño de sistemas 2025 utn frsf

Pasos para correr el progama:
1. Abrir la carpeta TPDiseño2025, dentro de ella ejecutar el comando docker compose up -d
2. Abrir la carpeta frontend_next, dentro de TPDiseno2025 y ejecutar el comando npm run dev
3. Luego en Spring Boot DashBoard ejecutar el archico TpDiseno2025Application.java o en la carpeta TPDiseño2025 ejecutar el comando mvn springboot:run

Luego de eso ya tendriamos corriendo la aplicacion, el front y la base de datos.
Para acceer a la base de datos, ingresar al puerto http://localhost:8080, registrarse con las credenciales del docker-compose (lineas 24 y 25). Dentro del pgAdmin local registrar la BD con las credenciales (lineas 6 y 7, en host name/address: postgres y en port: 5432).
Para acceder al front de la aplicacion ingresar al puerto http://localhost:3000. Acceder al sistema con las credenciales (usuario: conserje, contraseña: admin123 o  pasar directamente a http://localhost:3000/dashboard).

La aplicacion springboot tiene asignado el puerto localhost:8081
