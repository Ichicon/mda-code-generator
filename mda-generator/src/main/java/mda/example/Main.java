package mda.example;
import java.nio.file.Path;
import java.nio.file.Paths;

import mda.generator.MdaGenerator;
import mda.generator.MdaGeneratorBuilder;
import mda.generator.readers.XmiReader;

public class Main {
	public static void main(String[] args) {
		
		
		
		MdaGenerator generator = new MdaGeneratorBuilder() //
				.withModelPath("C:\\Users\\Fabien\\git\\mda-code-generator\\mda-generator\\src\\main\\resources\\example.xmi")
				.withReaderClass(XmiReader.class)
				.build();
		
		generator.generate();
	}
}
