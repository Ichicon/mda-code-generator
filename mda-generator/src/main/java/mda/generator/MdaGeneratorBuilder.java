package mda.generator;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

import mda.generator.converters.DomainToTypeConverter;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.readers.ModelFileReader;
import mda.generator.readers.XmiReader;

public class MdaGeneratorBuilder {
	private Class<? extends ModelFileReader> readerClass = XmiReader.class;
	private String pathToModel = null;	
	private Class<? extends DomainToTypeConverter> typeConverter;

	
	/**
	 * [MANDATORY] Path do model file to use for mda generation
	 * @param modelPath /path/to/the/file
	 * @return builder to reuse
	 */
	public MdaGeneratorBuilder withModelPath(String modelPath) {
		this.pathToModel = modelPath;		
		return this;
	}
	
	/**
	 * [MANDATORY] Class to use for 'Domain -> java type' and 'Domain -> db type' conversion
	 * @param modelPath /path/to/the/file
	 * @return builder to reuse
	 */
	public MdaGeneratorBuilder withTypeConverter(Class<? extends DomainToTypeConverter> typeConverter) {
		this.typeConverter = typeConverter;		
		return this;
	}
	
	/**
	 * Class used to read the model file, default is mda.generator.readers.XmiReader
	 * @param readerClass Class to use instead of XmiReader
	 * @return builder to reuse
	 */
	public MdaGeneratorBuilder withReaderClass(Class<? extends ModelFileReader> readerClass){
		this.readerClass = readerClass;
		
		return this;
	}
	
	public MdaGenerator build() {
		MdaGenerator generator = new MdaGenerator();
		
		// Paramètres pour la lecture
		if(StringUtils.isEmpty(pathToModel)){
			throw new MdaGeneratorException("MdaGenerator needs an input file, use new MdaGeneratorBuilder().withModelPath(\"/path/to/model\")");
		}
		generator.setPathToModelFile(pathToModel);
		generator.setReaderClass(readerClass);
		
		// TODO autres parametres pour la genration
		
		return generator;
	}
}
