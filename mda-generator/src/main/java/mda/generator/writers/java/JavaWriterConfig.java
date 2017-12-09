package mda.generator.writers.java;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collection;

import mda.generator.beans.UmlPackage;
import mda.generator.converters.ConverterInterface;

public class JavaWriterConfig {
	private Path javaOutputDirectory; 
	private Collection<UmlPackage> umlPackages; 
	private ConverterInterface converter;
	private String entities;
	private String daos;
	private Charset charset = StandardCharsets.UTF_8;
	
	/**
	 * @return the javaOutputDirectory
	 */
	public Path getJavaOutputDirectory() {
		return javaOutputDirectory;
	}
	/**
	 * @param javaOutputDirectory the javaOutputDirectory to set
	 */
	public void setJavaOutputDirectory(Path javaOutputDirectory) {
		this.javaOutputDirectory = javaOutputDirectory;
	}
	/**
	 * @return the umlPackages
	 */
	public Collection<UmlPackage> getUmlPackages() {
		return umlPackages;
	}
	/**
	 * @param umlPackages the umlPackages to set
	 */
	public void setUmlPackages(Collection<UmlPackage> umlPackages) {
		this.umlPackages = umlPackages;
	}
	/**
	 * @return the converter
	 */
	public ConverterInterface getConverter() {
		return converter;
	}
	/**
	 * @param converter the converter to set
	 */
	public void setConverter(ConverterInterface converter) {
		this.converter = converter;
	}
	/**
	 * @return the entities
	 */
	public String getEntities() {
		return entities;
	}
	/**
	 * @param entities the entities to set
	 */
	public void setEntities(String entities) {
		this.entities = entities;
	}
	/**
	 * @return the daos
	 */
	public String getDaos() {
		return daos;
	}
	/**
	 * @param daos the daos to set
	 */
	public void setDaos(String daos) {
		this.daos = daos;
	}
	/**
	 * @return the charset
	 */
	public Charset getCharset() {
		return charset;
	}
	/**
	 * @param charset the charset to set
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}
