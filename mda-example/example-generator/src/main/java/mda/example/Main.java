package mda.example;
import mda.generator.MdaGenerator;
import mda.generator.MdaGeneratorBuilder;

public class Main {
	
	
	public static void main(String[] args) {
		MdaGenerator generator = new MdaGeneratorBuilder() //
				.fromPropertiesFile(null) // default file : mda-generator.properties
				// Add specific cache annotation to UserType and Service classes
				.withAnnotation("@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY)", "UserType", "Service")
				.build();

		generator.generate();
	}


}
