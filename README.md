![logo](https://raw.github.com/1N0T/images/master/global/1N0T.png)
# spring-mvc-file-upload

Seguramente existen miles de ejemplos similares. Éste, pretende ser el código mínimo necesario para realizar el upload de múltiples ficheros en una aplicación **Spring MVC**.

Para compilar y ejecutar el ejemplo, ejecutar en una terminal el siguiente comando
```
mvn tomcat:run
```
Naturalmente, se presupone que ya tienes instalado **maven** en el equipo.

Para probar, después de ejecutar el comando anterior, abrir la URL siguiente en un naveador:
```
http://localhost:8080/spring-mvc-file-upload/
```
Nos aparecerá una formulario muy elemental, que nos permitirá realizar una selección múltiple de ficheros, que se subirán al directorio **./ficheros/** que se encuentra en la raiz de la aplicación. El destino puede ser modificado cambiando el contenido del fichero **configuracion.properties**.

Para dealizar el download de un fichero abriremos la siguiente URL:
```
http://localhost:8080/spring-mvc-file-upload/rest/controller/download/[nombre del fichero]
```
Naturalmente, debemos sustituir **[nombre del fichero]** por el valor que corresponda (p.ej. miFichero.jpg).
