package mda.example;

import java.util.HashMap;
import java.util.Map;

import mda.generator.beans.UmlDomain;
import mda.generator.converters.ConverterInterface;

public class ExampleDomainConverter implements ConverterInterface {
	private static final Map<String, String> mapJavaTypes = new HashMap<>(); // domain => type
	private static final Map<String, String> mapBddTypes = new HashMap<>(); // domain => type
	
	static {
		// JAVA TYPES
		mapJavaTypes.put("DO_ID","Long");
		mapJavaTypes.put("DO_NOMBRE_LONG","Long");
		mapJavaTypes.put("DO_NOMBRE_COURT","Integer");
		mapJavaTypes.put("DO_OUI_NON","Boolean");
		mapJavaTypes.put("DO_LIBELLE_COURT","String");
		mapJavaTypes.put("DO_PERIODE","String");
		mapJavaTypes.put("DO_CODE","String");
		mapJavaTypes.put("DO_NOM","String");
		mapJavaTypes.put("DO_PRENOM","String");
		mapJavaTypes.put("DO_PASSWORD","String");
		mapJavaTypes.put("DO_VARCHAR_50","String");
		mapJavaTypes.put("DO_VARCHAR_255","String");
		mapJavaTypes.put("DO_COMMENTAIRE","String");
		mapJavaTypes.put("DO_LIBELLE_LONG","String");
		mapJavaTypes.put("DO_MOT_PASSE","String");
		mapJavaTypes.put("DO_ORDRE_REPARTITION","String");
		mapJavaTypes.put("DO_DATE","java.time.LocalDate");
		mapJavaTypes.put("DO_DATE_HEURE","java.time.LocalDateTime");
		// Blob
		// set  Hibernate.getLobCreator(session).createBlob(inputStream, file.length());
		// get  blob.getBytes(1, (int) blob.length()); -> write to file
		mapJavaTypes.put("DO_FICHIER","java.sql.Blob"); 
		
		//BDD TYPES
		mapBddTypes.put("DO_ID","number(12)");
		mapBddTypes.put("DO_NOMBRE_LONG","number(12)");
		mapBddTypes.put("DO_NOMBRE_COURT","number(4)");
		mapBddTypes.put("DO_OUI_NON","number(1)");
		mapBddTypes.put("DO_LIBELLE_COURT","varchar2(50)");
		mapBddTypes.put("DO_PERIODE","varchar2(4)");
		mapBddTypes.put("DO_CODE","varchar2(3)");
		mapBddTypes.put("DO_NOM","varchar2(50)");
		mapBddTypes.put("DO_PRENOM","varchar2(50)");
		mapBddTypes.put("DO_PASSWORD","varchar2(70)");
		mapBddTypes.put("DO_VARCHAR_50","varchar2(50)");
		mapBddTypes.put("DO_VARCHAR_255","varchar2(255)");
		mapBddTypes.put("DO_COMMENTAIRE","CLOB");
		mapBddTypes.put("DO_LIBELLE_LONG","varchar2(100)");
		mapBddTypes.put("DO_MOT_PASSE","varchar2(70)");
		mapBddTypes.put("DO_ORDRE_REPARTITION","varchar2(10)");
		mapBddTypes.put("DO_DATE","DATE");
		mapBddTypes.put("DO_DATE_HEURE","DATE");
		mapBddTypes.put("DO_FICHIER","BLOB"); 
	}
	
	
	@Override
	public String getJavaType(UmlDomain domain) {
		String javaType = mapJavaTypes.get(domain.getName());
		if(javaType == null) {
			javaType = "Unknown";
		}	
		return javaType;
	}

	@Override
	public String getDataBaseType(UmlDomain domain) {
		String bddType = mapBddTypes.get(domain.getName());
		if(bddType == null) {
			bddType = "Unknown";
		}	
		return bddType;
	}

}
