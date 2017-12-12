package mda.generator.writers.sql;

import java.nio.file.Path;

import mda.generator.beans.UmlPackage;
import mda.generator.converters.ConverterInterface;

public class SQLWriterConfig {
	private Path sqlTemplatePath;
	private Path sqlOutputDirectory;
	private Iterable<UmlPackage> packagesList;
	private Iterable<String> excludesClassesPrefixes;
	private ConverterInterface converter;
	
	/**
	 * @return the sqlTemplatePath
	 */
	public Path getSqlTemplatePath() {
		return sqlTemplatePath;
	}
	/**
	 * @param sqlTemplatePath the sqlTemplatePath to set
	 */
	public void setSqlTemplatePath(Path sqlTemplatePath) {
		this.sqlTemplatePath = sqlTemplatePath;
	}
	/**
	 * @return the sqlOutputDirectory
	 */
	public Path getSqlOutputDirectory() {
		return sqlOutputDirectory;
	}
	/**
	 * @param sqlOutputDirectory the sqlOutputDirectory to set
	 */
	public void setSqlOutputDirectory(Path sqlOutputDirectory) {
		this.sqlOutputDirectory = sqlOutputDirectory;
	}
	/**
	 * @return the packagesList
	 */
	public Iterable<UmlPackage> getPackagesList() {
		return packagesList;
	}
	/**
	 * @param packagesList the packagesList to set
	 */
	public void setPackagesList(Iterable<UmlPackage> packagesList) {
		this.packagesList = packagesList;
	}
	/**
	 * @return the excludesClassesPrefixes
	 */
	public Iterable<String> getExcludesClassesPrefixes() {
		return excludesClassesPrefixes;
	}
	/**
	 * @param excludesClassesPrefixes the excludesClassesPrefixes to set
	 */
	public void setExcludesClassesPrefixes(Iterable<String> excludesClassesPrefixes) {
		this.excludesClassesPrefixes = excludesClassesPrefixes;
	}
	/**
	 * @return the convert
	 */
	public ConverterInterface getConverter() {
		return converter;
	}
	/**
	 * @param convert the convert to set
	 */
	public void setConverter(ConverterInterface convert) {
		this.converter = convert;
	}
	
	
	
}
