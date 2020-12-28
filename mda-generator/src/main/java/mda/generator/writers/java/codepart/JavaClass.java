package mda.generator.writers.java.codepart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.beans.UmlClass;
import mda.generator.converters.java.JavaNameConverterInterface;
import mda.generator.converters.type.TypeConverterInterface;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.writers.NamesComputingUtil;
import mda.generator.writers.java.JavaWriter;
import mda.generator.writers.java.utils.ImportManager;

/**
 * Java class to store data to print in class file
 * @author Fabien Crapart
 */
public class JavaClass {
	private final String packageName;

	protected final ImportManager importManager;
	protected final JavaNameConverterInterface javaNameConverter;
	protected final TypeConverterInterface typeConverter;

	private final List<String> commentsList  = new ArrayList<>();
	private final List<JavaAnnotation> annotationsList = new ArrayList<>();
	private List<String> userDefinedAnnotations = new ArrayList<>();

	private final Visibility visibilite = Visibility.PUBLIC;
	private final String name;

	private final List<JavaAttribute> attributesList = new ArrayList<>();
	private final List<JavaMethod> methodsList = new ArrayList<>();

	private JavaClass pkClass;
	private JavaAttribute pkField;
	/**
	 *
	 * @param name
	 * @param packageName
	 */
	public JavaClass(String name, String packageName, String comments) {
		this.name = name;
		this.packageName = packageName;
		commentsList.add(comments);
		importManager = new ImportManager(packageName);

		// Not used in this case
		javaNameConverter = null;
		typeConverter = null;
	}

	/**-
	 *
	 * @param javaPackage
	 * @param umlClass
	 * @param typeConverter
	 * @param javaNameConverter
	 */
	public JavaClass(JavaPackage javaPackage, UmlClass umlClass, TypeConverterInterface typeConverter, JavaNameConverterInterface javaNameConverter) {
		this.javaNameConverter = javaNameConverter;
		this.typeConverter = typeConverter;

		name = javaNameConverter.convertClassName(umlClass.getName());
		if(umlClass.getComment() != null) {
			commentsList.addAll(Arrays.asList(umlClass.getComment().split("\n")));
		} else {
			commentsList.add(JavaWriter.NO_COMMENT_FOUND);
		}

		packageName = javaPackage.getPackageName();
		importManager = new ImportManager(packageName);

		// Annotation entity
		annotationsList.add(new JavaAnnotation(importManager.getFinalName("javax.persistence.Entity")));
		// Annotation table
		annotationsList.add(
				new JavaAnnotation(
						importManager.getFinalName("javax.persistence.Table"),
						new JavaAnnotationProperty("name","\"" + umlClass.getName() + "\"")
						)
				);

		// Composite PK
		if(umlClass.getPKs().size() > 1) {
			createCompositePK(javaPackage, umlClass);
		} else if(umlClass.getPKs().size() > 0){ // Standard single PK field
			createPKField(javaPackage, umlClass);
		}

		// Attributes, getter, setter
		manageAttributes(umlClass);

		// Associations vers d'autres classes
		manageAssociations(umlClass);
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @return the importsList
	 */
	public Collection<String> getImportsList() {
		return importManager.getAllImports();
	}

	/**
	 * @return the comment
	 */
	public List<String> getCommentsList() {
		return new ArrayList<>(commentsList);
	}

	/**
	 * @return the annotationsList
	 */
	public List<JavaAnnotation> getAnnotationsList() {
		return annotationsList;
	}

	/**
	 * @return the visibilite
	 */
	public Visibility getVisibilite() {
		return visibilite;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the attributesList
	 */
	public List<JavaAttribute> getAttributesList() {
		return attributesList;
	}

	/**
	 * @return the methodsList
	 */
	public List<JavaMethod> getMethodsList() {
		return methodsList;
	}

	/**
	 * @return the pkClass
	 */
	public JavaClass getPkClass() {
		return pkClass;
	}

	/**
	 * @return the pkField
	 */
	public JavaAttribute getPkField() {
		return pkField;
	}

	/**
	 * @param userDefinedAnnotations the userDefinedAnnotations to set
	 */
	public void setUserDefinedAnnotations(List<String> userDefinedAnnotations) {
		this.userDefinedAnnotations = userDefinedAnnotations;
	}

	/**
	 * @return the userDefinedAnnotations
	 */
	public List<String> getUserDefinedAnnotations() {
		return userDefinedAnnotations;
	}

	/**
	 *
	 * @param javaPackage
	 * @param umlClass
	 * @param converter
	 * @param nameConverter
	 */
	protected void createPKField(JavaPackage javaPackage, UmlClass umlClass) {
		UmlAttribute umlPK = umlClass.getPKs().get(0);
		pkField = new JavaAttribute(umlPK, typeConverter,javaNameConverter, importManager);
		attributesList.add(pkField);

		// Generate getter/setter
		JavaMethod getterPK = generateGetter(pkField);
		String seqName = "\"" + NamesComputingUtil.computeSequenceName(umlClass) + "\"";

		// Id annotation on PK field
		getterPK.addAnnotations(new JavaAnnotation(importManager.getFinalName("javax.persistence.Id")));
		// Annotation Sequence generator
		getterPK.addAnnotations(new JavaAnnotation(
				importManager.getFinalName("javax.persistence.SequenceGenerator"),
				new JavaAnnotationProperty("name",seqName),
				new JavaAnnotationProperty("sequenceName",seqName),
				new JavaAnnotationProperty("allocationSize","1") // sequence always 1 because sql increment must have the same value
				));
		// Annotation on PK field to use generator
		getterPK.addAnnotations(new JavaAnnotation(
				importManager.getFinalName("javax.persistence.GeneratedValue"),
				new JavaAnnotationProperty("strategy",importManager.getFinalName("javax.persistence.GenerationType")+".SEQUENCE"),
				new JavaAnnotationProperty("generator",seqName)
				));
		// Annotation pour le nom de la colonne
		getterPK.addAnnotations(new JavaAnnotation(
				importManager.getFinalName("javax.persistence.Column"),
				new JavaAnnotationProperty("name","\""+pkField.getColumnName() +"\""),
				new JavaAnnotationProperty("nullable",pkField.isNotNull()?"false":"true")
				));
		methodsList.add(getterPK);
		methodsList.add(generateSetter(pkField));
	}

	/**
	 * Create composite PK
	 * @param javaPackage
	 * @param umlClass
	 * @param converter
	 */
	protected void createCompositePK(JavaPackage javaPackage, UmlClass umlClass) {
		// Use an embedded class as attribute
		pkClass = new JavaClass(getName()+"Id", javaPackage.getPackageName(),"Composite Key for " + getName());
		// Add embedded annotation in imports
		importManager.getFinalName("javax.persistence.EmbeddedId");

		// Add embedabble annotation into pkClass
		pkClass.annotationsList.add(new JavaAnnotation(pkClass.importManager.getFinalName("javax.persistence.Embeddable")));

		// Add real pks in embeddable class
		for(UmlAttribute pk : umlClass.getPKs()) {
			// Attribute for pk
			JavaAttribute compositeAttr = new JavaAttribute(pk, typeConverter, javaNameConverter, importManager);
			pkClass.attributesList.add(compositeAttr);

			// Getter with @Column
			JavaMethod compositeAttrGetter = generateGetter(compositeAttr);
			pkClass.methodsList.add(compositeAttrGetter);

			compositeAttrGetter.addAnnotations(new JavaAnnotation(
					pkClass.importManager.getFinalName("javax.persistence.Column"),
					new JavaAnnotationProperty("name","\""+compositeAttr.getColumnName() +"\""),
					new JavaAnnotationProperty("nullable",compositeAttr.isNotNull()?"false":"true")
					));

			// Setter
			JavaMethod compositeAttrSetter = generateSetter(compositeAttr);
			pkClass.methodsList.add(compositeAttrSetter);
		}

		// Create fake pkField for generation name
		pkField = new JavaAttribute(StringUtils.uncapitalize(pkClass.getName()), pkClass.getName(), null);
		// Create getter and setter in main class with fake attribute
		methodsList.add(generateGetter(pkField));
		methodsList.add(generateSetter(pkField));



		// Add composite Key as package class
		javaPackage.addClass(pkClass);
	}

	/**
	 * Attributs (no PLS) managements to create java attributes and getter/setters
	 * @param umlClass
	 * @param converter
	 */
	protected void manageAttributes(UmlClass umlClass ) {
		for(UmlAttribute umlAttribute : umlClass.getAttributes()) {
			// PKs managed before
			if(!umlAttribute.isPK()) {
				JavaAttribute javaAttribute = new JavaAttribute(umlAttribute,typeConverter,javaNameConverter, importManager);
				attributesList.add(javaAttribute);

				// Generate getter/setter
				JavaMethod getter = generateGetter(javaAttribute);

				// Liste property pour @Column
				List<JavaAnnotationProperty> columnProperties = new ArrayList<>();
				columnProperties.add(new JavaAnnotationProperty("name","\""+javaAttribute.getColumnName() +"\""));
				columnProperties.add(new JavaAnnotationProperty("nullable",javaAttribute.isNotNull()?"false":"true"));
				if(!javaAttribute.isUpdatable()) {
					columnProperties.add(new JavaAnnotationProperty("updatable","false"));
				}

				// Annotation pour le nom de la colonne
				getter.addAnnotations(new JavaAnnotation(
						importManager.getFinalName("javax.persistence.Column"),
						columnProperties.toArray(new JavaAnnotationProperty[0])
						));

				methodsList.add(getter);
				methodsList.add(generateSetter(javaAttribute));
			}
		}
	}

	/**
	 * Management of umlAssociation to create attributes and getter/setters
	 * @param umlClass
	 * @param converter
	 */
	protected void manageAssociations(UmlClass umlClass) {
		for(UmlAssociation association : umlClass.getAssociations()) {
			if(association.isTargetNavigable()) {
				JavaAttribute javaAttribute = new JavaAttribute(association, typeConverter,javaNameConverter, importManager);
				attributesList.add(javaAttribute);

				// Generate getter/setter
				JavaMethod assocGetter = generateGetter(javaAttribute);

				// xToMany
				if(association.isTargetMultiple() ) {
					// ManyToMany
					if(association.getOpposite().isTargetMultiple()) {
						buildManyToMany(association, assocGetter);
					} else { // OneToMany
						buildOneToMany(association, assocGetter);
					}
				} else { // xToOne
					// ManyToOne
					if(association.getOpposite().isTargetMultiple()) {
						buildManyToOne(association, assocGetter);
					} else { // OneToOne
						buildOneToOne(association, assocGetter);
					}
				}

				methodsList.add(assocGetter);
				methodsList.add(generateSetter(javaAttribute));
			}
		}
	}

	/**
	 * Build One to many annotations
	 * @param association association with data
	 * @param assocGetter Getter for the many to one
	 */
	protected void buildManyToOne(UmlAssociation association, JavaMethod assocGetter) {
		assocGetter.addAnnotations(new JavaAnnotation(
				importManager.getFinalName("javax.persistence.ManyToOne"),
				// ManyToOne is eager by default, which is bad :/
				new JavaAnnotationProperty("fetch",importManager.getFinalName("javax.persistence.FetchType")+".LAZY")
				)
				);
		assocGetter.addAnnotations(new JavaAnnotation(
				importManager.getFinalName("javax.persistence.JoinColumn"),
				new JavaAnnotationProperty("name","\"" + NamesComputingUtil.computeColumnFkName(association) + "\""),
				new JavaAnnotationProperty("referencedColumnName","\"" + NamesComputingUtil.computeColumnPkName(association.getTarget()) + "\"")
				));
	}

	/**
	 * Build One to many annotations
	 * @param association association with data
	 * @param assocGetter Getter for the one to many
	 */
	protected void buildOneToMany(UmlAssociation association, JavaMethod assocGetter) {
		List<JavaAnnotationProperty> propertiesOneToMany = new ArrayList<>();

		//  Bidirectionnal relation, mappedBy is enough
		if(association.getOpposite().isTargetNavigable()) {

			propertiesOneToMany.add(new JavaAnnotationProperty("mappedBy","\""+ javaNameConverter.computeFkAttributeName(association.getOpposite()) + "\""));

		} else {// Unidirectional, needs join column name and reference column name
			JavaAnnotation joinColumn = new JavaAnnotation(
					importManager.getFinalName("javax.persistence.JoinColumn"),
					new JavaAnnotationProperty("name","\"" + NamesComputingUtil.computeColumnFkName(association.getOpposite())+ "\""),
					new JavaAnnotationProperty("referencedColumnName","\"" + NamesComputingUtil.computeColumnPkName(association.getSource()) + "\""),
					new JavaAnnotationProperty("nullable", association.getOpposite().isTargetNullable() ? "true" : "false")
					);
			assocGetter.addAnnotations(joinColumn);
		}
		// Cascading ALL
		propertiesOneToMany.add(new JavaAnnotationProperty("cascade",importManager.getFinalName("javax.persistence.CascadeType")+".ALL"));
		// Add orphan removal
		propertiesOneToMany.add(new JavaAnnotationProperty("orphanRemoval","true"));

		// OneToMany with properties
		JavaAnnotation oneToMany = new JavaAnnotation(
				importManager.getFinalName("javax.persistence.OneToMany"),
				propertiesOneToMany.toArray(new JavaAnnotationProperty[0])
				);
		assocGetter.addAnnotations(oneToMany);
	}

	/**
	 * Build many to many annotations
	 * @param association association with data
	 * @param assocGetter Getter for the many to many
	 */
	protected void buildManyToMany(UmlAssociation association, JavaMethod assocGetter) {
		// The "owner" have the annotation with join columns and intermediate table name
		// Or owner has no navigability
		if(!association.isTargetOwned() || !association.getOpposite().isTargetNavigable()) {
			assocGetter.addAnnotations(new JavaAnnotation(
					importManager.getFinalName("javax.persistence.ManyToMany"),
					new JavaAnnotationProperty("cascade","{"
							+ importManager.getFinalName("javax.persistence.CascadeType")+".PERSIST,"
							+ importManager.getFinalName("javax.persistence.CascadeType")+".MERGE}")
					));
			assocGetter.addAnnotations(new JavaAnnotation(
					importManager.getFinalName("javax.persistence.JoinTable"),
					new JavaAnnotationProperty("name","\"" +  association.getName() + "\""),
					new JavaAnnotationProperty("joinColumns","@"+importManager.getFinalName("javax.persistence.JoinColumn")+"(name = \""+ NamesComputingUtil.computeColumnFkName(association.getOpposite())+"\")"),
					new JavaAnnotationProperty("inverseJoinColumns","@"+importManager.getFinalName("javax.persistence.JoinColumn")+"(name = \""+  NamesComputingUtil.computeColumnFkName(association)+ "\")")
					));
		} else { // Not "owner" of the manyToMany, mappedBy with opposite getter is enough
			assocGetter.addAnnotations(new JavaAnnotation(
					importManager.getFinalName("javax.persistence.ManyToMany"),
					new JavaAnnotationProperty("mappedBy","\""+ javaNameConverter.computeFkAttributeName(association.getOpposite()) + "\""),
					new JavaAnnotationProperty("cascade","{"
							+ importManager.getFinalName("javax.persistence.CascadeType")+".PERSIST,"
							+ importManager.getFinalName("javax.persistence.CascadeType")+".MERGE}")
					));
		}
	}

	/**
	 * Build One to One annotations
	 * @param association association with data
	 * @param assocGetter Getter for the One to One
	 */
	protected void buildOneToOne(UmlAssociation association, JavaMethod assocGetter) {
		// Association side is owner of OneToOne
		if(association.isTargetOwned()) {
			// One to one with fetch = lazy
			assocGetter.addAnnotations(
					new JavaAnnotation(
							importManager.getFinalName("javax.persistence.OneToOne"),
							// OneToOne is eager by default, which is bad :/
							new JavaAnnotationProperty("fetch",importManager.getFinalName("javax.persistence.FetchType")+".LAZY")
							)
					);
			// Reference to owner PK
			assocGetter.addAnnotations(new JavaAnnotation(
					importManager.getFinalName("javax.persistence.JoinColumn"),
					new JavaAnnotationProperty("name","\"" + NamesComputingUtil.computeColumnFkName(association) + "\""),
					new JavaAnnotationProperty("referencedColumnName","\"" + NamesComputingUtil.computeColumnPkName(association.getTarget()) + "\"")
					));
		}
		// Not owner, must have the owning side define (or association can't work)
		else {
			// Can't use mapped by if the owning side has no annotation defined
			if(!association.getOpposite().isTargetNavigable()) {
				throw new MdaGeneratorException("OneToOne association " + association.getName() + " must have navigability from the owner side.");
			} else {
				assocGetter.addAnnotations(
						new JavaAnnotation(
								importManager.getFinalName("javax.persistence.OneToOne"),
								// OneToOne is eager by default, which is bad :/
								new JavaAnnotationProperty("fetch",importManager.getFinalName("javax.persistence.FetchType")+".LAZY"),
								// mapped by
								new JavaAnnotationProperty("mappedBy","\""+ javaNameConverter.computeFkAttributeName(association.getOpposite())  + "\""),
								// cascading all
								new JavaAnnotationProperty("cascade","{"+ importManager.getFinalName("javax.persistence.CascadeType")+".ALL}")
								)
						);
			}
		}

	}


	private static JavaMethod generateGetter(JavaAttribute attribute) {
		JavaMethod getter = new JavaMethod(Visibility.PUBLIC, attribute.getJavaType(), "get" + StringUtils.capitalize(attribute.getName()));

		// Comment from model
		for(String commentLine : attribute.getComments()) {
			getter.addCommentLine(commentLine);
		}
		// Comment for return
		getter.addCommentLine("@return value of " + attribute.getName());

		getter.addContentLine("return " + attribute.getName());

		return getter;
	}

	private static JavaMethod generateSetter(JavaAttribute attribute) {
		List<String> args = new ArrayList<>();
		args.add("final " + attribute.getJavaType() + " " + attribute.getName());

		JavaMethod setter = new JavaMethod(Visibility.PUBLIC, "void", "set" + StringUtils.capitalize(attribute.getName()), args);

		// Comment from model
		for(String commentLine : attribute.getComments()) {
			setter.addCommentLine(commentLine);
		}
		// Comment for param
		setter.addCommentLine("@param " + attribute.getName() + " new value to give to "  + attribute.getName());

		// Content for setter
		setter.addContentLine("this." + attribute.getName() + " = " + attribute.getName());

		return setter;
	}




}
