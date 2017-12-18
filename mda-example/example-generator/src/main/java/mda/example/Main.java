package mda.example;
import mda.generator.MdaGenerator;
import mda.generator.MdaGeneratorBuilder;

public class Main {
	
	
	public static void main(String[] args) {
		MdaGenerator generator = new MdaGeneratorBuilder() //
				.fromPropertiesFile(null) // default file : mda-generator.properties
				.build();

		generator.generate();
	}


}
