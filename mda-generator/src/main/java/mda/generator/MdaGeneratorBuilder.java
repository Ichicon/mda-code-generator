package mda.generator;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mda.example.Main;
import mda.generator.converters.ConverterInterface;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.readers.ModelFileReaderInterface;
import mda.generator.readers.xmi.XmiReader;
import mda.generator.writers.java.JavaWriter;
import mda.generator.writers.java.JavaWriterInterface;
import mda.generator.writers.sql.SQLWriter;
import mda.generator.writers.sql.SQLWriterInterface;


/**
 * Builder to initialize the MdaGenerator.
 * 
 * @author Fabien
 *
 */
public class MdaGeneratorBuilder {
	/** logger */ 
	private static final Logger LOG = LogManager.getLogger(MdaGeneratorBuilder.class);
	
	/** Classe pour lire le model */
	private Class<? extends ModelFileReaderInterface> readerClass = XmiReader.class;
	/** Empalcement du fichier de model */
	private Path pathToModel = null;	
	
	/** Classe pour convertir les domains en types java et bdd */
	private Class<? extends ConverterInterface> typeConverter;
	
	/** Classe pour écrire les classes java*/
	private Class<? extends JavaWriterInterface> javaWriter = JavaWriter.class;
	
	/** Classe pour écrire le sql */
	private Class<? extends SQLWriterInterface> sqlWriter = SQLWriter.class;
	
	/** Emplacement de sortie des packages et fichiers générés */
	private Path javaOutputDirectory = null;
	/** Emplacement du fichier SQL généré en sortie */
	private Path sqlOutputDirectory = null;
	
	/** Nom dans les packages qui correspond à l'emplacement des entities, permet de faire le path équivalent pour les daos */
	private String entitiesPackagePartName = null;
	private String daosPackagePartName = null;

	
	/**
	 * [MANDATORY] Path to model file to use for mda generation
	 * @param modelPath /path/to/the/file Use MdaGeneratorBuilder.getApplicationPath() to use relative path easily, ex: MdaGeneratorBuilder.getApplicationPath().resolve("example.xmi")
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withModelPath(Path modelPath) {
		this.pathToModel = modelPath;		
		return this;
	}
	
	/**
	 * [MANDATORY] Class to use for 'Domain -> java type' and 'Domain -> db type' conversion
	 * @param typeConverter Class implementing DomainToTypeConverter interface
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withTypeConverter(Class<? extends ConverterInterface> typeConverter) {
		this.typeConverter = typeConverter;		
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
		this.javaWriter = javaWriterClass;
		
		return this;
	}

	/**
	 * Class used to write the sql files
	 * @param slqWriterClass Class implementing SqlWriterInterface interface (to use instead of default SqlWriter)
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withSqlWriter(Class<? extends SQLWriterInterface> slqWriterClass){
		this.sqlWriter = slqWriterClass;
		
		return this;
	}
	
	/**
	 * [MANDATORY] Path to output directory to write java packages, entities and daos
	 * @param javaOutputDirPath /path/to/directory
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withJavaOutputDirectory(Path javaOutputDirPath) {
		this.javaOutputDirectory = javaOutputDirPath;		
		return this;
	}
	
	
	/**
	 * [MANDATORY] Path to output directory to write sql creation script(s)
	 * @param sqlOutputDirPath /path/to/directory
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withSqlOutputDirectory(Path sqlOutputDirPath) {
		this.sqlOutputDirectory = sqlOutputDirPath;		
		return this;
	}
	
	/**
	 * [MANDATORY] Name of the package part which is root for entities. Ex : for the package "com.mycompany.myproject.entities.users" it's "entities"
	 * @param entitiesPackagePartName name (ex: "entities")
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withEntitiesPackagePartName(String entitiesPackagePartName) {
		this.entitiesPackagePartName = entitiesPackagePartName;		
		return this;
	}
	
	/**
	 * [MANDATORY] Name of the package part which is root for daos. Ex (see withEntitiesPackagePartName example too) : the package "com.mycompany.myproject.entities.users" will become "com.mycompany.myproject.daos.users" 
	 * @param daosPackagePartName name (ex: "daos"), can be the equals to entities
	 * @return builder to re-use
	 */
	public MdaGeneratorBuilder withDaosPackagePartName(String daosPackagePartName) {
		this.daosPackagePartName = daosPackagePartName;		
		return this;
	}
	
	
	/**
	 * Build the MdaGenerator from parameters
	 * @return MdaGenerator object built
	 * @throws MdaGeneratorException if parameters are not set correctly
	 */
	public MdaGenerator build() {
		MdaGenerator generator = new MdaGenerator();
		
		// Paramètres pour la lecture
		if(!Files.exists(pathToModel)){
			throw new MdaGeneratorException("MdaGenerator needs an input file, use mdaGeneratorBuilder.withModelPath(\"/path/to/model\")");
		}
		if(typeConverter == null){
			throw new MdaGeneratorException("MdaGenerator needs a Domain -> Types converter, define a class implementing ConverterInterface interface and use mdaGeneratorBuilder.withTypeConverter(myTypeConverter.class)");
		}
		if(javaWriter == null){
			throw new MdaGeneratorException("MdaGenerator needs a java writer, define a class implementing JavaWriterInterface interface and use mdaGeneratorBuilder.withJavaWriter(myJavaWriter.class)");
		}
		if(sqlWriter == null){
			throw new MdaGeneratorException("MdaGenerator needs a sql writer, define a class implementing SqlWriterInterface interface and use mdaGeneratorBuilder.withSqlWriter(mySqlWriter.class)");
		}
		if(javaOutputDirectory == null) {
			throw new MdaGeneratorException("MdaGenerator needs a java root directory to write files, use mdaGeneratorBuilder.withJavaOutputDirectory(\"/path/to/directory\")");
		}
		if(sqlOutputDirectory == null) {
			throw new MdaGeneratorException("MdaGenerator needs a sql root directory to write files, use mdaGeneratorBuilder.withSqlOutputDirectory(\"/path/to/directory\")");
		}
		if(entitiesPackagePartName == null) {
			throw new MdaGeneratorException("MdaGenerator needs a name for entities package part name (ex: x.x.x.entities.y.y.y), use mdaGeneratorBuilder.withEntitiesPackagePartName(\"entities\")");
		}
		if(daosPackagePartName == null) {
			throw new MdaGeneratorException("MdaGenerator needs a name for daos package part name which will be put instead of entities names (ex: x.x.x.entities.y.y.y  becomes x.x.x.daos.y.y.y) , use mdaGeneratorBuilder.withDaosPackagePartName(\"daos\")");
		}
		
		generator.setPathToModelFile(pathToModel);
		generator.setReaderClass(readerClass);
		generator.setConverterClass(typeConverter);
		generator.setJavaWriterClass(javaWriter);
		generator.setSqlWriterClass(sqlWriter);
		generator.setJavaOutputDirectory(javaOutputDirectory);
		generator.setConverterClass(typeConverter);
		generator.setSqlOutputDirectory(sqlOutputDirectory);
		generator.setDaosPackagePartName(daosPackagePartName);
		generator.setEntitiesPackagePartName(entitiesPackagePartName);

	
		// TODO facultatif extends/implements pour les DAOs
		// TODO facultatif extends/implements pour les entities ?
		
		
		return generator;
	}
	
	/**
	 * @return Renvoie l'emplacement sur le fs de l'application
	 */
	public static Path getApplicationPath() {

		URI currentUri;
		try {
			currentUri = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI();
		} catch (URISyntaxException e) {
			throw new MdaGeneratorException("Erreur dans la récupération du path",e);
		}
		return Paths.get(currentUri);
	}
}
