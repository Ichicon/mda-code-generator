package mda.generator.writers.java;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mda.generator.beans.UmlClass;
import mda.generator.beans.UmlPackage;
import mda.generator.converters.ConverterInterface;

/**
 * Java package to generate to filesystem
 * 
 * @author Fabien
 *
 */
public class JavaPackage {
	private final Path packagePath;
	private final String packageName;
	private final String packageComment;
	
	private List<JavaClass> classes = new ArrayList<>();
	
	/**
	 * Package initialization
	 * @param parentPath Root of source code
	 * @param packageName Package name
	 * @param packageComment Facultative : comment to include in package-info.java
	 * @param converter Type converter for classes
	 */
	public JavaPackage(Path parentPath, UmlPackage umlPackage, ConverterInterface converter) {
		this.packagePath = parentPath.resolve(Paths.get(StringUtils.replaceChars(umlPackage.getName(), '.','/')));
		this.packageName = umlPackage.getName();
		this.packageComment = umlPackage.getComment();
		
		for(UmlClass umlClass : umlPackage.getClasses()) {
			classes.add(new JavaClass(this, umlClass, converter));
		}
	}
	
	
	/**
	 * @return the packagePath
	 */
	public Path getPackagePath() {
		return packagePath;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @return the packageComment
	 */
	public String getPackageComment() {
		return packageComment;
	}

	/**
	 * @return the classes (copy)
	 */
	public List<JavaClass> getClasses() {
		return new ArrayList<>(classes);
	}

}
