# Sistema de citas medicas

Este proyecto es un sistema de administración de citas médicas desarrollado en Java. Permite gestionar doctores, pacientes y citas, con control de acceso para administradores.

Instalación y Configuración:

Clonar el repositorio:
git clone https://github.com/TU-USUARIO/citas-medicas.git  
cd citas-medicas  

Compilar el código:
javac -d out src/*.java 

Generar el JAR:
jar cvfm SistemaCitas.jar manifest.txt -C out . 

Ejecutar el programa:
java -jar SistemaCitas.jar  

Uso del Programa :
Al ejecutar el JAR, se mostrará un menú con opciones:
Registrar doctores
Registrar pacientes
Crear citas médicas
Salir del programa
Se debe ingresar los datos requeridos en cada opción.
Los datos se almacenan en archivos CSV dentro de la carpeta db/.
Licencia
Este proyecto está bajo la licencia MIT.

Autor
Rodrigo Joel Medina Martínez
