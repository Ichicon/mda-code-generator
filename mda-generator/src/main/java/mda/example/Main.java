package mda.example;
import mda.generator.MdaGenerator;
import mda.generator.MdaGeneratorBuilder;
import mda.generator.readers.xmi.XmiReader;

public class Main {
	public static void main(String[] args) {
		
		
		
		MdaGenerator generator = new MdaGeneratorBuilder() //
				.withModelPath("C:\\Users\\Fabien\\git\\mda-code-generator\\mda-generator\\src\\main\\resources\\example.xmi")
				.withReaderClass(XmiReader.class)
				.withTypeConverter(ExampleDomainConverter.class)
				.build();
		
		generator.generate();
	}
}
