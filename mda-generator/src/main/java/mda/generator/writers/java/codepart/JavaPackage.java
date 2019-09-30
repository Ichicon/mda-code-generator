package mda.generator.writers.java.codepart;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mda.generator.beans.UmlClass;
import mda.generator.beans.UmlPackage;
import mda.generator.converters.java.JavaNameConverterInterface;
import mda.generator.converters.type.TypeConverterInterface;
import mda.generator.writers.java.JavaWriter;

/**
 * Java package to generate to filesystem.
 *
 * @author Fabien
 *
 */
public class JavaPackage {
	private final Path packagePath;
	private final String packageName;
	private final List<String> comments = new ArrayList<>();
	private final List<JavaClass> classes = new ArrayList<>();

	/**
	 * Package initialization.
	 *
	 * @param parentPath Root of source code
	 * @param packageName Package name
	 * @param comments Facultative : comment to include in package-info.java
	 * @param converter Type converter for classes
	 */
	public JavaPackage(Path parentPath, UmlPackage umlPackage, TypeConverterInterface converter, JavaNameConverterInterface nameConverter) {
		packagePath = parentPath.resolve(Paths.get(StringUtils.replaceChars(umlPackage.getName(), '.','/')));
		packageName = umlPackage.getName();

		if(umlPackage.getComment() != null) {
			comments.addAll(Arrays.asList(StringUtils.split(umlPackage.getComment(),"\n")));
		} else {
			comments.add(JavaWriter.NO_COMMENT_FOUND);
		}

		for(UmlClass umlClass : umlPackage.getClasses()) {
			classes.add(new JavaClass(this, umlClass, converter, nameConverter));
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
	 * @return the package comments
	 */
	public List<String> getCommentsList() {
		return comments;
	}

	/**
	 * @return the classes (copy)
	 */
	public List<JavaClass> getClasses() {
		return new ArrayList<>(classes);
	}

	/**
	 * Adding a new class
	 * @param javaClass new class to add
	 */
	public void addClass(JavaClass javaClass) {
		classes.add(javaClass);
	}
}
