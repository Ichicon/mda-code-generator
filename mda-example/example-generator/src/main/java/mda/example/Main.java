package mda.example;
import java.nio.file.Paths;
import java.util.Objects;

import mda.generator.MdaGenerator;
import mda.generator.MdaGeneratorBuilder;
import mda.generator.converters.DomainToOracleConverter;

public class Main {
	public static void main(String[] args) {
		MdaGenerator generator = new MdaGeneratorBuilder() //
				.fromPropertiesFile(null) // default file : mda-generator.properties
//				.withModelPath(MdaGeneratorBuilder.getApplicationPath().resolve("model").resolve("example.xmi"))
//				.withMetadataPath(MdaGeneratorBuilder.getApplicationPath().resolve("model").resolve("example_metadata.xml"))
//				.withReaderClass(XmiReader.class)
//				.withTypeConverter(DomainToOracleConverter.class)
//				.withJavaOutputDirectory(MdaGeneratorBuilder.getApplicationPath().getParent().getParent().resolve("src/main/javagen"))
//				.withSqlOutputDirectory(MdaGeneratorBuilder.getApplicationPath().getParent().getParent().resolve("src/dev/sql/create"))
//				.withEntitiesPackagePartName("domain")
//				.withDaosPackagePartName("daos")
//				.withEntityTemplate(MdaGeneratorBuilder.getApplicationPath().resolve("templates").resolve("entity.vm"))
//				.withPackageInfoTemplate(MdaGeneratorBuilder.getApplicationPath().resolve("templates").resolve("package-info.vm"))
//				.withExcludedPrefixes("test_","double_key")
				.build();

		generator.generate();
	}


}
