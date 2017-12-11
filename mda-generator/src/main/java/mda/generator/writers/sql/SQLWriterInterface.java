package mda.generator.writers.sql;

import java.nio.file.Path;

public interface SQLWriterInterface {
	void writeSql(Path outputDirectory);
}
