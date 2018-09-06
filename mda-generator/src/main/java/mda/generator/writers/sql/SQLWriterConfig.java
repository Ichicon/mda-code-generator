package mda.generator.writers.sql;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import mda.generator.beans.UmlPackage;
import mda.generator.converters.ConverterInterface;

/**
 * Configuration for SQLWriter, alimented by MdaGenerator
 * 
 * @author Fabien Crapart
 */
public class SQLWriterConfig {
	private Path createSqlTemplatePath;
	private Path dropSqlTemplatePath;
	private Path sqlOutputDirectory;
	private Iterable<UmlPackage> packagesList;
	private Iterable<String> excludesClassesPrefixes;
	private ConverterInterface converter;
	private Charset charset = StandardCharsets.UTF_8;
	
	/**
	 * @return the sqlTemplatePath
	 */
	public Path getDropSqlTemplatePath() {
		return dropSqlTemplatePath;
	}
	/**
	 * @param sqlTemplatePath the sqlTemplatePath to set
	 */
	public void setDropSqlTemplatePath(Path sqlTemplatePath) {
		this.dropSqlTemplatePath = sqlTemplatePath;
	}
	
	/**
	 * @return the sqlTemplatePath
	 */
	public Path getCreateSqlTemplatePath() {
		return createSqlTemplatePath;
	}
	/**
	 * @param sqlTemplatePath the sqlTemplatePath to set
	 */
	public void setCreateSqlTemplatePath(Path sqlTemplatePath) {
		this.createSqlTemplatePath = sqlTemplatePath;
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
