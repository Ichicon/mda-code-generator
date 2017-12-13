package mda.generator.writers.sql;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.beans.UmlClass;
import mda.generator.beans.UmlPackage;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.writers.VelocityUtils;
import mda.generator.writers.java.NamesComputingUtil;

/**
 * Create SQL files for Oracle
 * @author Fabien Crapart
 */
public class OracleSQLWriter implements SQLWriterInterface {
	/** Logger */
	private static final Logger LOG = LogManager.getLogger(OracleSQLWriter.class);

	private static final String STOP_GENERATION = "-- STOP GENERATION";
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
			writeSQLFile(config.getSqlOutputDirectory().resolve("create_tables.sql"), config.getCreateSqlTemplatePath());
			writeSQLFile(config.getSqlOutputDirectory().resolve("drop_tables.sql"), config.getDropSqlTemplatePath());
		}catch(Exception e) {
			throw new MdaGeneratorException("Error while writing SQL files", e);
		}
	}
	
	/**
	 * 
	 * @param filePath
	 * @param templateToUse
	 * @throws IOException
	 */
	protected void writeSQLFile(Path filePath, Path templateToUse) throws IOException {				
		VelocityContext context = new VelocityContext();
		if(VelocityUtils.analyseFileAndCompleteContext(filePath, STOP_GENERATION, END_OF_GENERATED, context)) {
			context.put("sequencesList", sequencesList);
			context.put("tablesList", tablesList);
			context.put("fksList", fksList);
			context.put("end_of_generated", END_OF_GENERATED);

			// FIXME add charset to config
			VelocityUtils.writeFileFromTemplate(filePath, templateToUse, context, config.getCharset());
		}else {
			LOG.debug(filePath + " will not be overwritten because '" + STOP_GENERATION + "' is present");
		}
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
						// ManyToMany, need intermediate table
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
						// oneToMany, no FK
					} else { // ManyToOne, create FK
                        // Columns for FK
                        for(UmlAttribute pkX : umlAssociation.getTarget().getPKs()){
                            SQLColumn fk;                       
                            // Use fk column name defined in association
                            if(umlAssociation.getTarget().getPKs().size() == 1) {
                                fk = new SQLColumn(NamesComputingUtil.computeFKName(umlAssociation),pkX.getDomain(),true,"ManyToOne FK " + umlAssociation.getTarget().getName(), config.getConverter());
                            } else { // Generate name from pk because it's a composite key
                                fk = new SQLColumn(pkX.getName(),pkX.getDomain(),true,"ManyToOne FK " + umlAssociation.getTarget().getName(), config.getConverter());       
                            }
                            table.addColumn(fk);                       
                        }
                        
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
	

}
