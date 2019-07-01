package mda.generator.converters;

import mda.generator.beans.UmlDomain;

/**
 * Convert JAVA types to ORACLE DB types
 * @author Fabien Crapart
 */
public class DomainToOracleConverter extends AbstractDomainToJavaConverter {

	@Override
	public String getDataBaseType(UmlDomain domain) {
		String javaType = getJavaType(domain);
		String dbType;
		
		switch(javaType) {
		case "String":
			// Max length defined, we stay in varchar2 if size <= 4000
			if(domain.getMaxLength()!=null && !"0".equals(domain.getMaxLength())&& Integer.parseInt(domain.getMaxLength()) <= 4000) {
				dbType="VARCHAR2("+domain.getMaxLength()+")";
			} else { // CLOB for other cases
				dbType="CLOB";
			}
			break;
		case "Boolean" :
			dbType= "NUMBER(1)";
			break;
		case "Short":
			dbType= "NUMBER(2)";
			break;
		case "Char":
			dbType= "CHAR(1 CHAR)";
			break;
		case "Integer" :
			dbType= "NUMBER(4)";
			break;
		case "Long":
			dbType= "NUMBER(12)";
			break;
		case "java.time.LocalDate":
			dbType= "DATE";
			break;
		case "java.time.LocalDateTime":
			dbType= "DATE";
			break;
		case "Float":
			dbType= "NUMBER(4,2)";
			break;
		case "Double":
			dbType= "NUMBER(12,2)";
			break;
		case "java.math.BigDecimal":
			dbType= "NUMBER(12,2)"; 
			break;
		case "Byte[]": 
			dbType= "RAW(" + (domain.getMaxLength()==null || "0".equals(domain.getMaxLength())?"1":domain.getMaxLength())+ ")";
			break;
		case "Byte": 
			dbType= "RAW(1)"; 
			break;
		case "java.sql.Blob":
			dbType= "BLOB";
			break;
		default :
			dbType = "Unknown";
			break;
		}
		
		return dbType;
	}

}
