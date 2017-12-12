package mda.generator.writers.sql;

import java.util.ArrayList;
import java.util.List;

import mda.generator.beans.UmlClass;

/**
 * SQL table to print in sql file
 * @author Fabien Crapart
 *
 */
public class SQLTable {
	private final String name;
	private final String comment;
	private final List<SQLColumn> columnsList = new ArrayList<>();
	private String pkValue;
	
	
	public SQLTable(UmlClass umlClass) {
		this.name = umlClass.getName();
		this.comment = umlClass.getComment();
	}
	
	public SQLTable(String name, String comment) {
		this.name = name;
		this.comment = comment;
	}
	
	
	public void addColumn(SQLColumn column) {
		columnsList.add(column);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the columnsList (copy)
	 */
	public List<SQLColumn> getColumnsList() {
		return new ArrayList<>(columnsList);
	}

	/**
	 * @return the pkValue
	 */
	public String getPkValue() {
		return pkValue;
	}

	/**
	 * @param pkValue the pkValue to set
	 */
	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}		
}
