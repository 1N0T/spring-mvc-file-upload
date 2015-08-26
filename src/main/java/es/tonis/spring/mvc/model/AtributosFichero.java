//======================================================================
// Java Bean que será devuelto en forma de lista en formato JSON
//
// Ignoraremos la propiedad bytes que podría contener el fichero en caso
// de necesitarse para el procesamiento (no es el caso de este ejmplo).
//======================================================================
package es.tonis.spring.mvc.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({"bytes"})
public class AtributosFichero {

	private String nombreFichero;
	private String tamañoFichero;
	private String tipoFichero;
	
	private byte[] bytes;
	
	public String getNombreFichero() {
		return nombreFichero;
	}
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	public String getTamañoFichero() {
		return tamañoFichero;
	}
	public void setTamañoFichero(String tamañoFichero) {
		this.tamañoFichero = tamañoFichero;
	}
	public String getTipoFichero() {
		return tipoFichero;
	}
	public void setTipoFichero(String tipoFichero) {
		this.tipoFichero = tipoFichero;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
