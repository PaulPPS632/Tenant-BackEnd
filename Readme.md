# Como ejecutar

## Crea la base de datos

el comando esta en el archivo querys "create database superfact3;"

## Configura properties del Backend

entra en properties y asegurate de colocar el usuario y password correcto de tu mysql

spring.datasource.username=springuser

spring.datasource.password=ThePassword

luego de esto ejecuta el spring boot para que se creen las tablas correspondientes

## Ejecuta las querys para insertar datos globales

ejecuta las query que estan en el archivo querys.sql

## Ejecuta frontend

ng serve para ejecutar el frontend
