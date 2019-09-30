package mda.generator.writers.java;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import mda.generator.beans.UmlPackage;
import mda.generator.converters.java.JavaNameConverterInterface;
import mda.generator.converters.type.TypeConverterInterface;

/**
 * Configuration for JavaWriter, alimented by MdaGenerator.
 *
 * @author Fabien Crapart
 *
 */
public class JavaWriterConfig {
	private Path javaOutputDirectory;
	private Collection<UmlPackage> umlPackages;
	private TypeConverterInterface converter;
	private Path pathToPackageInfoTemplate;
	private Path pathToEntitiesTemplate;
	private Path pathToDaosTemplate;
	private String entities;
	private String daos;
	private JavaNameConverterInterface javaNameConverter;



	/** Added annotations for classes Map<ClassName, List<AnnotationString>>*/
	private Map<String,List<String>> annotationsForClasses;
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
	 * Uml to java converter.
	 * @return the converter
	 */
	public TypeConverterInterface getConverter() {
		return converter;
	}
	/**
	 * @param converter the converter to set
	 */
	public void setConverter(TypeConverterInterface converter) {
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
	/**
	 * @return the pathToEntitiesTemplate
	 */
	public Path getPathToEntitiesTemplate() {
		return pathToEntitiesTemplate;
	}
	/**
	 * @param pathToEntitiesTemplate the pathToEntitiesTemplate to set
	 */
	public void setPathToEntitiesTemplate(Path pathToEntitiesTemplate) {
		this.pathToEntitiesTemplate = pathToEntitiesTemplate;
	}
	/**
	 * @return the pathToDaosTemplate
	 */
	public Path getPathToDaosTemplate() {
		return pathToDaosTemplate;
	}
	/**
	 * @param pathToDaosTemplate the pathToDaosTemplate to set
	 */
	public void setPathToDaosTemplate(Path pathToDaosTemplate) {
		this.pathToDaosTemplate = pathToDaosTemplate;
	}
	/**
	 * @return the pathToPackageInfoTemplate
	 */
	public Path getPathToPackageInfoTemplate() {
		return pathToPackageInfoTemplate;
	}
	/**
	 * @param pathToPackageInfoTemplate the pathToPackageInfoTemplate to set
	 */
	public void setPathToPackageInfoTemplate(Path pathToPackageInfoTemplate) {
		this.pathToPackageInfoTemplate = pathToPackageInfoTemplate;
	}
	/**
	 * @return the annotationsForClasses
	 */
	public Map<String, List<String>> getAnnotationsForClasses() {
		return annotationsForClasses;
	}
	/**
	 * @param annotationsForClasses the annotationsForClasses to set
	 */
	public void setAnnotationsForClasses(Map<String, List<String>> annotationsForClasses) {
		this.annotationsForClasses = annotationsForClasses;
	}
	/**
	 * @return the javaNameConverter
	 */
	public JavaNameConverterInterface getJavaNameConverter() {
		return javaNameConverter;
	}
	/**
	 * @param javaNameConverter the javaNameConverter to set
	 */
	public void setJavaNameConverter(JavaNameConverterInterface javaNameConverter) {
		this.javaNameConverter = javaNameConverter;
	}
}
