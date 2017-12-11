package mda.example;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import mda.generator.MdaGenerator;
import mda.generator.MdaGeneratorBuilder;
import mda.generator.converters.DomainToOracleConverter;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.readers.xmi.XmiReader;

public class Main {
	public static void main(String[] args) {
		MdaGenerator generator = new MdaGeneratorBuilder() //
				.withModelPath(MdaGeneratorBuilder.getApplicationPath().resolve("example.xmi"))
				.withMetadataPath(MdaGeneratorBuilder.getApplicationPath().resolve("example_metadata.xml"))
//				.withReaderClass(XmiReader.class)
				.withTypeConverter(DomainToOracleConverter.class)
		//		.withJavaOutputDirectory(MdaGeneratorBuilder.getApplicationPath().getParent().getParent().resolve("src/main/javagen"))
//				.withSqlOutputDirectory(MdaGeneratorBuilder.getApplicationPath().getParent().getParent().resolve("src/dev/sql/create"))
//				.withEntitiesPackagePartName("domain")
//				.withDaosPackagePartName("daos")
				.withEntityTemplate(MdaGeneratorBuilder.getApplicationPath().resolve("entity.vm"))
				.build();

		generator.generate();
	}


}
