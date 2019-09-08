package mda.generator.writers.sql;

/**
 * Class to write sql create tables and drop tables files.
 * 
 * @author Fabien Crapart
 */
public interface SQLWriterInterface {
	/**
	 * Write create tables and drop tables sql files with provided configuration
	 * @param config
	 */
	void writeSql(SQLWriterConfig config);
}
