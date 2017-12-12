package mda.generator.writers.sql;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.beans.UmlClass;
import mda.generator.beans.UmlPackage;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.writers.java.NamesComputingUtil;

public class OracleSQLWriter implements SQLWriterInterface {
	/** Logger */
	private static final Logger LOG = LogManager.getLogger();
	
	private static final String END_OF_GENERATED = "-- END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE";
	
	private List<SQLForeignKey> fksList = new ArrayList<>();
	private List<SQLTable> tablesList = new ArrayList<>();
	private List<SQLSequence> sequencesList = new ArrayList<>();
	
	private SQLWriterConfig config;
	
	
	
	@Override
	public void writeSql(SQLWriterConfig config) {
		if(config == null) {
			throw new MdaGeneratorException("Null config given for sql writer");
		}		
		this.config = config;
		
		// Create the root directory for sql if doesn't exists
		try {
			Files.createDirectories(config.getSqlOutputDirectory());
		} catch (IOException e) {
			throw new MdaGeneratorException("Error while creating source root path", e);
		}

		// Iterate to extract datas from all classes	
		for(UmlPackage umlPackage : config.getPackagesList()) {
			extractDataFromPackage(umlPackage);
		}
		
		// Write SQL file from template and extracted data
		try {
			writeFile(config.getSqlOutputDirectory().resolve("crebas.sql"));
		}catch(Exception e) {
			throw new MdaGeneratorException("Error while writing SQL file", e);
		}
	}
	
	/**
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	protected void writeFile(Path filePath) throws IOException {		
		StringBuilder contentToKeep = new StringBuilder();
		boolean keepContent = false;
		boolean lineAdded = false;
		if(Files.exists(filePath)) {
			for(String line : Files.readAllLines(filePath)) {
				// We keep user edited content
				if(keepContent) {
					if(lineAdded) {
						contentToKeep.append("\n");
					} else {
						lineAdded = true;
					}
					contentToKeep.append(line);
				}	
				
				// Comment to detect user content (after this line)
				if(line.contains(END_OF_GENERATED)) {
					keepContent = true;
				}		
			}
		}
		
		VelocityContext context = new VelocityContext();

		context.put("sequencesList", sequencesList);
		context.put("tablesList", tablesList);
		context.put("fksList", fksList);
		context.put("end_of_generated", END_OF_GENERATED);
		context.put("keep_content", keepContent);
		context.put("content_to_keep", contentToKeep.toString());

		writeFileFromTemplate(filePath, config.getSqlTemplatePath(), context);
	}
	
	/**
	 * 
	 * @param umlPackage
	 */
	protected void extractDataFromPackage(UmlPackage umlPackage) {
		for(UmlClass umlClass : umlPackage.getClasses()) {
			if(!isExcludedClassName(umlClass)) {
				SQLTable table = new SQLTable(umlClass);
				tablesList.add(table);
				
				// Compute sequence name, no sequence for multiple pks
				if(umlClass.getPKs().size() == 1) {
					sequencesList.add(new SQLSequence(umlClass));
				}
				
				// Compute pk value			
				table.setPkValue(NamesComputingUtil.computePKValue(umlClass));
				
				// Add columns and compute pk name list			
				for(UmlAttribute umlAttribute : umlClass.getAttributes()) {
					SQLColumn column  = new SQLColumn(umlAttribute, config.getConverter());
					
					table.addColumn(column);
				}
				
				// Compute association columns and FKs
				for(UmlAssociation umlAssociation : umlClass.getAssociations()) {
					if(umlAssociation.isTargetMultiple()) {
						// ManyToMany, on doit créer une table intermédiaire avec la pks de chaque table
						if(umlAssociation.getOpposite().isTargetMultiple() && umlAssociation.isOwner()) {
							if(umlAssociation.getSource().getPKs().size() > 1 || umlAssociation.getTarget().getPKs().size() > 1) {
								throw new MdaGeneratorException("Cannot create table for " + umlAssociation.getName() + " association, composite pk forbidden in many to many");
							}
							
							// Table
							SQLTable manyToManyTable = new SQLTable(umlAssociation.getName(), "ManyToMany " 
									+ umlAssociation.getSource().getName() + " / " 
									+ umlAssociation.getTarget().getName());
							tablesList.add(manyToManyTable);
							
							// PK 1
							UmlAttribute attrPk1 = umlAssociation.getSource().getPKs().get(0);
							SQLColumn pk1 = new SQLColumn(NamesComputingUtil.computeFKName(umlAssociation.getOpposite()),attrPk1.getDomain(),true,"ManyToMany FK " + umlAssociation.getSource().getName(), config.getConverter());
							manyToManyTable.addColumn(pk1);
							
							// PK 2
							UmlAttribute attrPk2 = umlAssociation.getTarget().getPKs().get(0);
							SQLColumn pk2 = new SQLColumn(NamesComputingUtil.computeFKName(umlAssociation),attrPk2.getDomain(),true,"ManyToMany FK " + umlAssociation.getTarget().getName(), config.getConverter());
							manyToManyTable.addColumn(pk2);
							
							// PK VALUE
							manyToManyTable.setPkValue(pk1.getName() + "," + pk2.getName());
							
							// FK1
							SQLForeignKey fk1 = new SQLForeignKey(umlAssociation.getName()+"_1", manyToManyTable.getName(), umlAssociation.getSource().getName(), pk1.getName(), pk1.getName());
							fksList.add(fk1);
							
							// FK2		
							SQLForeignKey fk2 = new SQLForeignKey(umlAssociation.getName()+"_2", manyToManyTable.getName(), umlAssociation.getTarget().getName(), pk2.getName(), pk2.getName());
							fksList.add(fk2);
						}
						// Sinon oneToMany, pas de FK
					} else { // ManyToOne, on créé la FK
						fksList.add(new SQLForeignKey(umlAssociation));
					}
				}
			}
		}
	}

	/**
	 * Indicate if the class sql should be generated
	 * @param umlClass Class
	 * @return true if the class should not be generated, false otherwise
	 */
	protected boolean isExcludedClassName(UmlClass umlClass) {
		if(config.getExcludesClassesPrefixes() != null) {
			String className = umlClass.getName().toLowerCase();
			for(String excludedPrefix : config.getExcludesClassesPrefixes()) {
				if(className.startsWith(excludedPrefix.toLowerCase())){
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * Write context to a file with a template
	 * @param filePath Path to write file
	 * @param templatePath Path to Velocity template
	 * @param context File content in velocity context
	 * @throws IOException Error while writing file
	 */
	protected void writeFileFromTemplate(Path filePath, Path templatePath, VelocityContext context ) throws IOException {
		Properties prop = new Properties();
		prop.setProperty("file.resource.loader.path", templatePath.getParent().toString());
		Velocity.init(prop);
		
		Template template = null;
		try{
			template = Velocity.getTemplate(templatePath.getFileName().toString());
		}catch( Exception e ){ 
			throw new MdaGeneratorException("Error while writing from template " + templatePath,e);
		}
		StringWriter sw = new StringWriter();
		template.merge(context,sw);		
		LOG.debug("Creating SQL " + filePath);				
		Files.write(filePath, sw.toString().getBytes(StandardCharsets.UTF_8));	
	}
}
