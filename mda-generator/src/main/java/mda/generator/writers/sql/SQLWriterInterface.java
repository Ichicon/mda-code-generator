package mda.generator.writers.sql;

import java.nio.file.Path;

import mda.generator.beans.UmlPackage;

public interface SQLWriterInterface {
	/**
	 * Write sql with config
	 * @param config
	 */
	void writeSql(SQLWriterConfig config);
}
