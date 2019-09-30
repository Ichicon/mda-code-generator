package mda.generator.writers.sql;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import mda.generator.beans.UmlPackage;
import mda.generator.converters.type.TypeConverterInterface;

/**
 * Configuration for SQLWriter, alimented by MdaGenerator
 *
 * @author Fabien Crapart
 */
public class SQLWriterConfig {

	private Path createSqlTemplatePath;
	private Path dropSqlTemplatePath;
	private Path sqlOutputDirectory;
	private Path createTablesPath;
	private Path dropTablesPath;
	private Iterable<UmlPackage> packagesList;
	private Iterable<String> excludesClassesPrefixes;
	private TypeConverterInterface typeConverter;
	private Charset charset = StandardCharsets.UTF_8;
	private String sqlSchemaName;

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
		dropSqlTemplatePath = sqlTemplatePath;
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
		createSqlTemplatePath = sqlTemplatePath;
	}

	/**
	 * @return the createTablesPath
	 */
	public Path getCreateTablesPath() {
		return createTablesPath;
	}
	/**
	 * @param createTablesPath the createTablesPath to set
	 */
	public void setCreateTablesPath(Path createTablesPath) {
		this.createTablesPath = createTablesPath;
	}

	/**
	 * @return the dropTablesPath
	 */
	public Path getDropTablesPath() {
		return dropTablesPath;
	}
	/**
	 * @param dropTablesPath the dropTablesPath to set
	 */
	public void setDropTablesPath(Path dropTablesPath) {
		this.dropTablesPath = dropTablesPath;
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
	 * @param typeConverter the convert to set
	 */
	public void setTypeConverter(TypeConverterInterface typeConverter) {
		this.typeConverter = typeConverter;
	}

	/**
	 *
	 * @return typeConverter
	 */
	public TypeConverterInterface getTypeConverter() {
		return typeConverter;
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
	 * @return the schema
	 */
	public String getSqlSchemaName() {
		return sqlSchemaName;
	}
	/**
	 * @param schema the schema to set
	 */
	public void setSqlSchemaName(String schema) {
		sqlSchemaName = schema;
	}

}
