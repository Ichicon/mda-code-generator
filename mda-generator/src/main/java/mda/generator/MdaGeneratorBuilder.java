package mda.generator;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import mda.generator.converters.java.JavaNameConverterInterface;
import mda.generator.converters.java.JavaSnakeToCamelNameConverter;
import mda.generator.converters.type.TypeConverterInterface;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.readers.ModelFileReaderInterface;
import mda.generator.readers.xmi.XmiReader;
import mda.generator.utils.file.PathUtils;
import mda.generator.utils.file.PropertyUtils;
import mda.generator.writers.java.JavaWriter;
import mda.generator.writers.java.JavaWriterInterface;
import mda.generator.writers.sql.SQLWriterInterface;
import mda.generator.writers.sql.StandardSQLWriter;


/**
 * Builder to initialize the MdaGenerator.
 *
 * @author Fabien
 */
public class MdaGeneratorBuilder {
	private static String CREATE_TABLES_DEFAULT_FILE_NAME = "create_tables.sql";
	private static String DROP_TABLES_DEFAULT_FILE_NAME = "drop_tables.sql";

	/** Classe pour lire le model */
	private Class<? extends ModelFileReaderInterface> readerClass = XmiReader.class;
	/** Emplacement du fichier de métadonnées */
	private Path pathToMetadata= null;
	/** Empalcement du fichier de model */
	private Path pathToModel = null;

	/** Classe pour convertir les domains en types java et bdd */
	private Class<? extends TypeConverterInterface> typeConverter;

	/** Classe pour convertir les noms du diagramme en nom java */
	private Class<? extends JavaNameConverterInterface> javaNameConverter = JavaSnakeToCamelNameConverter.class;

	/** Classe pour écrire les classes java*/
	private Class<? extends JavaWriterInterface> javaWriter = JavaWriter.class;

	/** Classe pour écrire le sql */
	private Class<? extends SQLWriterInterface> sqlWriter = StandardSQLWriter.class;

	/** Annotations à ajouter aux classes */
	private final Map<String, List<String>> annotationsForClasses = new HashMap<>();

	/** Emplacement de sortie des packages et fichiers générés */
	private Path javaOutputDirectory;
	/** Emplacement du répertoire par défaut des SQL générés en sortie */
	private Path sqlOutputDirectory;
	/** Emplacement du fichier SQL de création de tables généré en sortie */
	private Path sqlCreateTablesPath;
	/** Emplacement du fichier SQL de suppression de table généré en sortie */
	private Path sqlDropTablesPath;

	/** Nom dans les packages qui correspond à l'emplacement des entities, permet de faire le path équivalent pour les daos */
	private String entitiesPackagePartName = "entities";
	private String daosPackagePartName = "daos";

	/** Package-info Velocity template */
	private Path pathToPackageInfoTemplate = PathUtils.getPathForClassPathAndFs("/templates/package-info.vm");
	/** JPA Entities Velocity template */
	private Path pathToEntitiesTemplate =PathUtils.getPathForClassPathAndFs("/templates/entity.vm");
	/** DAOs Velocity template */
	private Path pathToDaosTemplate = PathUtils.getPathForClassPathAndFs("/templates/dao_spring.vm");
	/** Create SQL Velocity template */
	private Path pathToCreateSQLTemplate =PathUtils.getPathForClassPathAndFs("/templates/createSQL_oracle.vm");
	/** DRop SQL Velocity template */
	private Path pathToDropSQLTemplate =PathUtils.getPathForClassPathAndFs("/templates/dropSQL_oracle.vm");
	/** Charsets for writing files */
	private Charset charset = StandardCharsets.UTF_8;
	/** Class prefixes for which sql tables will not be generated */
	private List<String> excludedPrefixes;
	/** Prefix to add before sql sequence name */
	private String sqlSequencePrefixName = "SEQ_";
	/** Schéma */
	private String sqlSchemaName;


	/**
	 * [RECOMMENDED] Load the generator with a properties file. You should call build() just after this call, yet you can still call withXxxx before building to overload parameters.
	 * /!\ Annotations cannot be added with property files, you have to call withAnnotation(...)
	 * @param pathToProperties Path to property file
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder fromPropertiesFile(final String pathToProperties) {
		String finalPath;
		if(pathToProperties == null) {
			finalPath = "/mda-generator.properties";
		} else {
			finalPath = pathToProperties;
		}

		loadProperties(PathUtils.getPathForClassPathAndFs(finalPath));

		return this;
	}

	/**
	 * [MANDATORY] Path to model file to use for mda generation
	 * @param modelPath /path/to/the/file Use MdaGeneratorBuilder.getApplicationPath() to use relative path easily, ex: MdaGeneratorBuilder.getApplicationPath().resolve("example.xmi")
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withModelPath(Path modelPath) {
		if(!Files.exists(modelPath)){
			throw new MdaGeneratorException("Le fichier de modèle n'a pas été trouvé à l'emplacemen fourni : "  + modelPath);
		}

		pathToModel = modelPath;
		return this;
	}
	/**
	 * [MANDATORY] Path to metadata file to use for mda generation, can be the same as model file depending on uml editor
	 * @param modelPath /path/to/the/file Use MdaGeneratorBuilder.getApplicationPath() to use relative path easily, ex: MdaGeneratorBuilder.getApplicationPath().resolve("example_metadata.xmi")
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withMetadataPath(Path metadataPath) {
		pathToMetadata = metadataPath;
		return this;
	}

	/**
	 * [MANDATORY] Class to use for 'Domain -> java type' and 'Domain -> db type' conversion
	 * @param typeConverter Class implementing DomainToTypeConverter interface
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withTypeConverter(Class<? extends TypeConverterInterface> typeConverter) {
		this.typeConverter = typeConverter;
		return this;
	}

	/**
	 * Class to use for generating java names from diagram names. Default is JavaSnakeToCamelNameConverter
	 * @param typeConverter Class implementing JavaNameConverterInterface interface
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withJavaNameConverter(Class<? extends JavaNameConverterInterface> javaNameConverterClass) {
		javaNameConverter = javaNameConverterClass;
		return this;
	}

	/**
	 * Path to output directory to write java packages, entities and daos, default "PROGRAM_ROOT"/../../src/main/javagen
	 * @param javaOutputDirPath /path/to/directory
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withJavaOutputDirectory(Path javaOutputDirPath) {
		javaOutputDirectory = javaOutputDirPath;
		return this;
	}

	/**
	 * Path to output directory to write sql creation script(s), default "PROGRAM_ROOT"/../../src/db/sqlgen
	 * @param sqlOutputDirPath /path/to/directory
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withSqlOutputDirectory(Path sqlOutputDirPath) {
		sqlOutputDirectory = sqlOutputDirPath;
		return this;
	}

	/**
	 * Name of the package part which is root for entities, default "entities". Ex : for the package "com.mycompany.myproject.entities.users" it's "entities"
	 * @param entitiesPackagePartName name (ex: "entities")
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withEntitiesPackagePartName(String entitiesPackagePartName) {
		this.entitiesPackagePartName = entitiesPackagePartName;
		return this;
	}


	/**
	 * Package-info template file,  default "PROGRAM_ROOT"/package-info.vm
	 * @param template path to template, ex: D:/myproject/templates/package-info.vm
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withPackageInfoTemplate(Path template) {
		pathToPackageInfoTemplate = template;
		return this;
	}

	/**
	 * Entities template file,  default "PROGRAM_ROOT"/entity.vm
	 * @param template path to template ex: D:/myproject/templates/entities.vm
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withEntityTemplate(Path template) {
		pathToEntitiesTemplate = template;
		return this;
	}

	/**
	 * DAOs template file,  default "PROGRAM_ROOT"/dao_spring.vm
	 * @param template path to template ex: D:/myproject/templates/daos.vm
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withDaoTemplate(Path template) {
		pathToDaosTemplate = template;
		return this;
	}

	/**
	 * create_tables.sql template file,  default "PROGRAM_ROOT"/createSQL_oracle.vm
	 * @param template path to template ex: D:/myproject/templates/createSQL.vm
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withCreateSQLTemplate(Path template) {
		pathToCreateSQLTemplate = template;
		return this;
	}

	/**
	 * drop_tables.sql template file,  default "PROGRAM_ROOT"/dropSQL_oracle.vm
	 * @param template path to template ex: D:/myproject/templates/dropSQL.vm
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withDropSQLTemplate(Path template) {
		pathToDropSQLTemplate = template;
		return this;
	}

	/**
	 * Name of the package part which is root for daos, default "daos". Ex (see withEntitiesPackagePartName example too) : the package "com.mycompany.myproject.entities.users" will become "com.mycompany.myproject.daos.users"
	 * @param daosPackagePartName name (ex: "daos"), can be the equals to entities
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withDaosPackagePartName(String daosPackagePartName) {
		this.daosPackagePartName = daosPackagePartName;
		return this;
	}

	/**
	 * Class used to read the model file, default is mda.generator.readers.XmiReader
	 * @param readerClass Class implementing ModelFileReader interface (to use instead of default XmiReader)
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withReaderClass(Class<? extends ModelFileReaderInterface> readerClass){
		this.readerClass = readerClass;

		return this;
	}

	/**
	 * Class used to write the java files
	 * @param javaWriterClass Class implementing JavaWriterInterface interface (to use instead of default JavaWriter)
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withJavaWriter(Class<? extends JavaWriterInterface> javaWriterClass){
		javaWriter = javaWriterClass;

		return this;
	}

	/**
	 * Class used to write the sql files
	 * @param slqWriterClass Class implementing SqlWriterInterface interface (to use instead of default SqlWriter)
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withSqlWriter(Class<? extends SQLWriterInterface> slqWriterClass){
		sqlWriter = slqWriterClass;

		return this;
	}

	/**
	 * Charset used to write the files
	 * @param charset Charset to use to write files
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withCharset(Charset charset){
		this.charset = charset;

		return this;
	}

	/**
	 * List of class name prefixes for which sql will no be generated (shared db with other application)
	 * @param excludedPrefixes List of prefix to exclude, can be a list of tables too. Ex: "otherapp_" or "tableToExclude"
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withExcludedPrefixes(String... excludedPrefixes) {
		this.excludedPrefixes = Arrays.asList(excludedPrefixes);
		return this;
	}

	/**
	 * Charset used to write the files
	 * @param sqlSequencePrefixName SQL Sequence prefix name to use (can be empty)
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withSqlSequencePrefixName(String sqlSequencePrefixName){
		this.sqlSequencePrefixName = sqlSequencePrefixName;
		return this;
	}

	/**
	 * Add an annotation to list of class (simple name). Call multiple times to add multiple annotations.
	 * @param annotation Annotation text. Ex: "@Cacheable(true)"
	 * @param classNames Simple names of class which will have this annotation. Ex : "Organisme"
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withAnnotation(String annotation, String ... classNames) {
		for(String className : classNames) {
			List<String> listeAnnots = annotationsForClasses.get(className);
			if(listeAnnots == null) {
				listeAnnots = new ArrayList<>();
				annotationsForClasses.put(className, listeAnnots);
			}

			listeAnnots.add(annotation);
		}


		return this;
	}

	/**
	 * Add a schema name to use into SQL templates
	 * @param sqlSchemaName name of sql schema
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withSqlSchemaName(String sqlSchemaName) {
		this.sqlSchemaName = sqlSchemaName;
		return this;
	}

	/**
	 *
	 * @param sqlCreateTablesPath
	 * @return
	 */
	public MdaGeneratorBuilder withSqlCreateTablesPath(final Path sqlCreateTablesPath) {
		this.sqlCreateTablesPath = sqlCreateTablesPath;
		return this;
	}

	/**
	 *
	 * @param sqlDropTablesPath
	 * @return
	 */
	public MdaGeneratorBuilder withSqlDropTablesPath(final Path sqlDropTablesPath) {
		this.sqlDropTablesPath = sqlDropTablesPath;
		return this;
	}

	/**
	 * Build the MdaGenerator from parameters
	 * @return MdaGenerator object built
	 * @throws MdaGeneratorException if parameters are not set correctly
	 */
	public MdaGenerator build() {
		MdaGenerator generator = new MdaGenerator();

		checkParameters();

		generator.setPathToModelFile(pathToModel);
		generator.setPathToMetadataFile(pathToMetadata);
		generator.setReaderClass(readerClass);
		generator.setTypeConverterClass(typeConverter);
		generator.setJavaWriterClass(javaWriter);
		generator.setSqlWriterClass(sqlWriter);
		generator.setJavaOutputDirectory(javaOutputDirectory);
		generator.setAnnotationsForClasses(annotationsForClasses);
		generator.setJavaNameConverterClass(javaNameConverter);

		if(sqlCreateTablesPath == null) {
			generator.setSqlCreateTablesPath(sqlOutputDirectory.resolve(CREATE_TABLES_DEFAULT_FILE_NAME));
		} else {
			generator.setSqlCreateTablesPath(sqlCreateTablesPath);
		}

		if(sqlDropTablesPath == null) {
			generator.setSqlDropTablesPath(sqlOutputDirectory.resolve(DROP_TABLES_DEFAULT_FILE_NAME));
		} else {
			generator.setSqlDropTablesPath(sqlDropTablesPath);
		}


		generator.setDaosPackagePartName(daosPackagePartName);
		generator.setEntitiesPackagePartName(entitiesPackagePartName);
		generator.setPathToCreateSQLTemplate(pathToCreateSQLTemplate);
		generator.setPathToDropSQLTemplate(pathToDropSQLTemplate);
		generator.setPathToEntitiesTemplate(pathToEntitiesTemplate);
		generator.setPathToDaosTemplate(pathToDaosTemplate);
		generator.setPathToPackageInfoTemplate(pathToPackageInfoTemplate);
		generator.setCharset(charset);
		generator.setExcludedPrefixes(excludedPrefixes);
		generator.setSqlSequencePrefixName(sqlSequencePrefixName);
		generator.setSqlSchemaName(sqlSchemaName);

		return generator;
	}

	/**
	 * Vérifie la validité des paramètres fournis
	 */
	protected void checkParameters() {
		if(pathToModel==null || !Files.exists(pathToModel)){
			throw new MdaGeneratorException("MdaGenerator needs an input file, use mdaGeneratorBuilder.withModelPath(\"/path/to/model\")");
		}
		if(pathToMetadata==null || !Files.exists(pathToMetadata)){
			throw new MdaGeneratorException("MdaGenerator needs a metadata input file, use mdaGeneratorBuilder.withMetadataPath(\"/path/to/metadata\")");
		}


		if(typeConverter == null){
			throw new MdaGeneratorException("MdaGenerator needs a Domain -> Types converter, define a class implementing ConverterInterface interface and use mdaGeneratorBuilder.withTypeConverter(myTypeConverter.class)");
		}
		if(javaNameConverter == null){
			throw new MdaGeneratorException("MdaGenerator needs a Java names converter, define a class implementing JavaNameConverterInterface interface and use mdaGeneratorBuilder.withJavaNameConverter(myJavaNameConverter.class)");
		}
		if(javaWriter == null){
			throw new MdaGeneratorException("MdaGenerator needs a java writer, define a class implementing JavaWriterInterface interface and use mdaGeneratorBuilder.withJavaWriter(myJavaWriter.class)");
		}
		if(javaOutputDirectory == null) {
			throw new MdaGeneratorException("MdaGenerator needs a java root directory to write files, use mdaGeneratorBuilder.withJavaOutputDirectory(\"/path/to/directory\")");
		}
		if(entitiesPackagePartName == null) {
			throw new MdaGeneratorException("MdaGenerator needs a name for entities package part name (ex: x.x.x.entities.y.y.y), use mdaGeneratorBuilder.withEntitiesPackagePartName(\"entities\")");
		}
		if(daosPackagePartName == null) {
			throw new MdaGeneratorException("MdaGenerator needs a name for daos package part name which will be put instead of entities names (ex: x.x.x.entities.y.y.y  becomes x.x.x.daos.y.y.y) , use mdaGeneratorBuilder.withDaosPackagePartName(\"daos\")");
		}
		if(pathToPackageInfoTemplate == null) {
			throw new MdaGeneratorException("MdaGenerator needs a package-info.java template. Create a velocity template and bind it with mdaGeneratorBuilder.withPackageInfoTemplate(\"path/to/template.vm\")");
		}
		if(pathToEntitiesTemplate == null) {
			throw new MdaGeneratorException("MdaGenerator needs a java entities template. Create a velocity template and bind it with mdaGeneratorBuilder.withEntityTemplate(\"path/to/template.vm\")");
		}

		if(pathToDaosTemplate == null) {
			throw new MdaGeneratorException("MdaGenerator needs a java daos template. Create a velocity template and bind it with mdaGeneratorBuilder.withDaoTemplate(\"path/to/template.vm\")");
		}

		if(sqlWriter == null){
			throw new MdaGeneratorException("MdaGenerator needs a sql writer, define a class implementing SqlWriterInterface interface and use mdaGeneratorBuilder.withSqlWriter(mySqlWriter.class)");
		}


		if(sqlCreateTablesPath == null && sqlOutputDirectory == null) {
			throw new MdaGeneratorException("MdaGenerator needs a path to generate SQL create tables file. "
					+ "Define either a path with mdaGeneratorBuilder.withSqlCreateTablesPath(\\\"/path/to/file\\\") "
					+ "or a SQL root directory to write files with default names using mdaGeneratorBuilder.withSqlOutputDirectory(\"/path/to/directory\")");
		}

		if(sqlDropTablesPath == null && sqlOutputDirectory == null) {
			throw new MdaGeneratorException("MdaGenerator needs a path to generate SQL drop tables file. "
					+ "Define either a path with mdaGeneratorBuilder.withSqlDropTablesPath(\\\"/path/to/file\\\") "
					+ "or a SQL root directory to write files with default names using mdaGeneratorBuilder.withSqlOutputDirectory(\"/path/to/directory\")");
		}

		if(pathToCreateSQLTemplate == null) {
			throw new MdaGeneratorException("MdaGenerator needs a create sql template. Create a velocity template and bind it with mdaGeneratorBuilder.withCreateSQLTemplate(\"path/to/template.vm\")");
		}
		if(pathToDropSQLTemplate == null) {
			throw new MdaGeneratorException("MdaGenerator needs a drop sql template. Create a velocity template and bind it with mdaGeneratorBuilder.withDropSQLTemplate(\"path/to/template.vm\")");
		}
		if(charset == null) {
			throw new MdaGeneratorException("MdaGenerator needs a charset defined to create files. Use mdaGeneratorBuilder.withCharset(StandardCharsets.UTF_8)");
		}
	}

	/**
	 *
	 * @param pathToProperties
	 */
	private void loadProperties(Path pathToProperties) {
		Properties prop = new Properties();

		try (InputStream input = Files.newInputStream(pathToProperties)){
			prop.load(input);

			PropertyUtils.loadClassFromProperty("readerClass", prop, this);
			PropertyUtils.loadPathFromProperty("pathToModel", prop, this);
			PropertyUtils.loadPathFromProperty("pathToMetadata", prop, this);
			PropertyUtils.loadClassFromProperty("typeConverter", prop, this);
			PropertyUtils.loadClassFromProperty("javaNameConverter", prop, this);

			PropertyUtils.loadCharset("charset", prop, this);

			PropertyUtils.loadClassFromProperty("javaWriter", prop, this);
			PropertyUtils.loadPathFromProperty("javaOutputDirectory", prop, this);
			PropertyUtils.loadString("entitiesPackagePartName", prop, this);
			PropertyUtils.loadString("daosPackagePartName", prop, this);
			PropertyUtils.loadPathFromProperty("pathToPackageInfoTemplate", prop, this);
			PropertyUtils.loadPathFromProperty("pathToEntitiesTemplate", prop, this);
			PropertyUtils.loadPathFromProperty("pathToDaosTemplate", prop, this);

			PropertyUtils.loadClassFromProperty("sqlWriter", prop, this);
			PropertyUtils.loadPathFromProperty("sqlOutputDirectory", prop, this);
			PropertyUtils.loadPathFromProperty("sqlCreateTablesPath", prop, this);
			PropertyUtils.loadPathFromProperty("sqlDropTablesPath", prop, this);
			PropertyUtils.loadPathFromProperty("pathToCreateSQLTemplate", prop, this);
			PropertyUtils.loadPathFromProperty("pathToDropSQLTemplate", prop, this);
			PropertyUtils.loadStringList("excludedPrefixes", prop, this);
			PropertyUtils.loadString("sqlSequencePrefixName", prop, this);
			PropertyUtils.loadString("sqlSchemaName", prop, this);
		} catch(Exception e) {
			throw new MdaGeneratorException("Cannot load property file " + pathToProperties.toString(),e);
		}
	}


}
