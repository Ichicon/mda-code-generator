package mda.generator.converters;


import mda.generator.beans.UmlDomain;
import mda.generator.converters.AbstractDomainToJavaConverter;


/**
 * Convert JAVA types to POSTGRESQL types
 * @author Damien Guérard
 */
public class DomainToPostgresConverter extends AbstractDomainToJavaConverter {

	@Override
	public String getDataBaseType(UmlDomain domain) {
		String dbType;
		if (domain.getTypeName() != null && domain.getTypeName().startsWith("varchar")) {
			dbType = "VARCHAR(" + domain.getMaxLength() + ')';
		} else {

			String javaType = getJavaType(domain);

			switch (javaType) {
			case "String":
				dbType = "TEXT";
				break;
			case "Boolean":
				dbType = "BOOLEAN";
				break;
			case "Short":
				dbType = "SMALLINT";
				break;
			case "Char":
				dbType = "CHARACTER(1)";
				break;
			case "Integer":
				dbType = "INTEGER";
				break;
			case "Long":
				dbType = "BIGINT";
				break;
			case "java.time.LocalDate":
				dbType = "DATE";
				break;
			case "java.time.LocalDateTime":
				dbType = "TIMESTAMP";
				break;
			case "Float":
				dbType = "REAL";
				break;
			case "Double":
				dbType = "DOUBLE PRECISION";
				break;
			case "Byte[]":
			case "java.sql.Blob":
				dbType = "BYTEA";
				break;
			case "Byte":
				dbType = "BIT(1)";
				break;

			default:
				dbType = "Unknown";
				break;
			}
		}

		return dbType;
	}

}
