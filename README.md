# taller2trips

## Instalación

### Servidor

* Instalación de MongoDB

  Para la instalación de MongoDB se puede consultar la siguiente página del sitio oficial:
[MongoDB Download Center](https://www.mongodb.com/download-center?jmp=nav)
  
  Para la instalación en sistemas Debian
[Install MongoDB Community Edition on Debian](https://docs.mongodb.com/master/tutorial/install-mongodb-on-debian/?_ga=1.248303311.990775504.1480219421)

  Se debe asegurar que el service de mongo esté corriendo, para ello:

    ``` sudo service mongod start ```
    

 * Instalación de NodeJS v6
    
    [Página oficial](https://nodejs.org/es/)
       
    Para sistemas Debian:
 
    ```
      curl -sL https://deb.nodesource.com/setup_6.x | sudo -E bash -
      sudo apt-get install -y nodejs
    ```
 
 * Ejecución del servidor
 
    ``` 
      cd webserver
      npm install
      npm start
    ``` 
  * Ingresar al sistema web por medio de la URL:
  
    ```
      http://localhost:3000/
    ```
 
### Cliente Android

* Alternativa 1

  1. [Descargar Android Studio](https://developer.android.com/studio/index.html?hl=es-419)
  2. Levantar el proyecto alojado en cliente/MyApplication
  
* Alternativa 2

  Descargar en el celular la apk ```app-debug.apk``` ubicada en client/ en el celular, instalarla y ejecutarla.

Para poder conectar la aplicación móvil con el servidor, ingresar a Configuración por medio del panel lateral.

En el textbox, se debe ingresar la ip del servidor, por ejemplo: ```http://localhost:3000/api```.
