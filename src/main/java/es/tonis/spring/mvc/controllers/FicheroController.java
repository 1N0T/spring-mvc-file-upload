package es.tonis.spring.mvc.controllers;

import java.io.IOException;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import org.springframework.http.HttpStatus;

import es.tonis.spring.mvc.model.AtributosFichero;

@Controller
@RequestMapping("/controller")
public class FicheroController {

	// Recuperamos el valor almacenado en la variable configuracion.destino.ficheros
	// definida en alguno de los ficheros properties definidos en la configuración
	// de sipring.
	 
	@Value("${configuracion.destino.ficheros}")
	private String destinoFicheros;
	
	Logger logger = Logger.getLogger(FicheroController.class);
	

	// =================================================================
	// Esta función nos permite subir uno vo varios ficheros a un 
	// directorio especificado en como una variable de un fichero 
	// properties. 
	//
	// URL:    /rest/controller/upload  
	// Método: upload()
	//
	// Parámetros: 
	// 		request:  MultipartHttpServletRequest
	//		response: HttpServletResponse
	//
	// Resultado (en formato JSON):
	// 		ficheros: LinkedList<AtributosFichero>
	// =================================================================

	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public @ResponseBody LinkedList<AtributosFichero> upload( MultipartHttpServletRequest request, HttpServletResponse response ) {

		LinkedList<AtributosFichero> ficheros         = new LinkedList<AtributosFichero>();
		AtributosFichero             atributosFichero = null;
		List<MultipartFile>          ficherosMP       = request.getFiles("ficheros[]");

        ficheros.clear();
        	    
		// Procesamos cada uno de los ficheros seleccionados en el formulario 
		for (MultipartFile ficheroMP : ficherosMP) {

			// Guardamos nombre, tamaño y tipo del fichero en la lista
			// que se devolverá como una matriz de objetos JSON
			atributosFichero = new AtributosFichero();
			atributosFichero.setNombreFichero(ficheroMP.getOriginalFilename());
			atributosFichero.setTamañoFichero(ficheroMP.getSize()/1024 + " Kb");
			atributosFichero.setTipoFichero(ficheroMP.getContentType());
			 
			try {
				
				// Aquí guardamos el contenido del fichero en la matriz de ficheros. Esto podría
				// ser de utilidad para facilitar un tratamiento posterior, pero carece de utilidad
				// para el ejemplo actual que sólo pretendemos realizar un upload sin más, pero
				// la dejamos la sentencia a modo de ejemplo
				atributosFichero.setBytes(ficheroMP.getBytes());
				
				logger.info("Subiento fichero " + ficheroMP.getOriginalFilename() + " en " + destinoFicheros);
				
				// Copiamos el fichero al directorio definido en el fichero properties
				FileCopyUtils.copy(ficheroMP.getBytes(), new FileOutputStream(destinoFicheros + ficheroMP.getOriginalFilename()));
				
			} catch (IOException e) {
				// Mostramos excepciones si se produce un error.
				e.printStackTrace();
			}
			
			// Añadimos atributos del fichero a la matriz de retorno.
			ficheros.add(atributosFichero);
			 
		 }
		 
		// Devolvemos matriz con las propiedades de los ficheros subidos
		// [{"nombreFichero":"ficheroX.JPG","tamañoFichero":"1025 Kb","tipoFichero":"image/jpeg"}, ...]
		return ficheros;
 
	}


	// =================================================================
	// Este métodonos permite descargar el fichero cuyo nombre se ha 
	// pasado como parámetro y que se encuentre en el directorio de 
	// descargas. Si no existe, devuelve un 404.
	//
	// URL:    /rest/controller/download/<nombre de fichero>  
	// Método: download()
	//
	// Parámetros: 
	//		response: HttpServletResponse
	//		fichero:  String
	//
	// Resultado:
	// 		Devuelve el fichero como adjunto o 404 si no existe
	//
	// Hemos indicado {fichero:.+} como parámetro, en lugar de 
	// {fichero}. Esto es debido a que si el último parámetro contiene
	// un punto en su valor (cosa probable en nuestro ejemplo por la
	// extensión de los ficheros), se trunca a partir del punto.
	// =================================================================

	@RequestMapping(value = "/download/{fichero:.+}", method = RequestMethod.GET)
	public void download( HttpServletResponse response, @PathVariable String fichero){
		try {		
			File miFichero = new File( destinoFicheros + fichero );
			
			// Si el fichero existe y no es un directorio, lo devolvemos como adjunto para su
			// descarga. En caso contrario, devolvemos un error 404.
			if (miFichero.exists() && !miFichero.isDirectory()) {
				response.setHeader("Content-disposition", "attachment; filename=\"" + fichero + "\"");
				FileCopyUtils.copy(new FileInputStream(miFichero), response.getOutputStream());
			} else {
				response.setStatus( HttpServletResponse.SC_NOT_FOUND );
			}
		} catch (IOException e) {
			// Mostramos excepciones si se produce un error no controlado.
			e.printStackTrace();
		}
	}
 
}
