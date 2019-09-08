# mda-code-generator
JAVA and SQL code generator from an XMI model and a XML metamodel (for types definition).

Build package structure, JPA entities, DAOs squeletons (SPRING crude repository), SQL create and drop scripts.

## Editing DATA model (with Enteprise architect 11)

Edit xxxxx.eap schematic with Entreprise Architect.

  * To add a new domain (<=> new type with length and precision constraints) :
       * Go to Settings → Code Engineering Datatypes. Choose Java and add or modify DO_xxx.
       * You have to export metadata as XML with Project -> Model Import/Export -> Export Reference Data and check Model Data Types - Codes an DDL
       * Replace metadata_xxxx.xml file (or generate it the first time) with this new file.
  * To define a primary key, go to the attribut detail panel and check isId.
  * To define a nullable attribute, go to the attribut detail panel and put 0 into Lower bound of Multiplicity.
  * To add a relationship between to classes :
     * Use the first line (Associate) in Class Relationships.
     * On the association you can set these ([xxxx] ⇔ not mandatory) :
         * General → Name : name1(x characters, without underscore)_name2(x characters, without underscore) → do not input more than 26 characters because of index names limitations in DB (oracle => 30 characters)
         * [Source Role → Role] : Name for get"ObjectName", set"ObjetName" and attribute behind the two (useful in case of multiple associations to the same class). Default is  is the name of the primary key of the pointed class.
         * [Source Role → Alias] Name for the FK column in the SQL table (useful when multiple association to the same table). Default is the name of the primary key in the other table.
         * [Target Role → Role] : Same as "Source Role -> Role", name for get"ObjectName", set"ObjetName" and attribute behind these two (useful in case of multiple associations to the same class). Default is  is the name of the primary key of the pointed class.
         * [Target Role → Alias] Same as "Source Role -> Alias", name for the FK column in the SQL table (useful when multiple association to the same table). Default is the name of the primary key in the other table.
         * [Target Role ou Source Role -> Owned] /!\ For a OneToOne, define which side hold the field (the is in the "owner" table => so not the one with "owned" to true).
   * To add comments :
       * For the package level (package-info.java), you must add a "Note" object into the diagram and write into.
       * For a class, you have to add a comment in "Notes".
       * For an attribute, you must put the comment in the "Alias" field.

## Using generator

### Install
Get both `mda-generator` and `example-generator` java projetcs from the repository.
Open `mda.example.Main` in `example-generator` and modify it to configure as you want.

### Configuration
You can use pure Java configuration, or use a property file.
There are detailed comments on each parameter in the example properties file, take a look at it.

#### Pure Java 
Chain calls of `with"ParameterName"` on new `MdaGeneratorBuilder()`, end with `build()`.
Call `generate()` on the `MdaGenerator` instance.

#### Properties file 
Call `fromProperties(String path)` (null use default mda-generator.properties in src/main/resources) on new MdaGeneratorBuilder(), end with `build()`.

You can add `with"ParameterName"` after `fromProperties(String path)` and before `build()` to override parameters from the property file.

Call `generate()` on the `MdaGenerator` instance.

### Run
Run the Main class as Java application.


## Modifying templates
Template are written using Velocity engine, look at official docs for syntax and usage.
You can modify default templates or create new ones on configure the generator to use it instead of default templates.

### JAVA templates 

#### Entities
Generate ORM beans, default template is JPA bean (entity.vm).

It uses the "end of generated code" comment to permit you to add code at the end of class.

These values are mapped to the template :
TODO

#### DAOs
Generate DAOs, default extends Spring crude repositories (dao_spring.vm).

These values are mapped to the template :
TODO

#### Package-info
Generato package-info in each dao and entities packages extracted from the class diagrams names.

These values are mapped to the template :
TODO

#### Special comments :
There are specials comments managed in java files. 
It can be used in any java file and the default templates are using it (look at each template for details).

1) Stop generation : Comment used to prevent generator from generating the file again.
Put only one per file !

`// STOP GENERATION`

2) End of generated code : generator keep the content after this line
Put only one per file !

`// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE`

 
 
### SQL templates

#### Create tables
Generate SQL sequences (for primary keys), tables with comments from the model file, foreign keys and their indexes.

These values are mapped to the template :
TODO

#### Drop tables
Generate SQL drops for sequences, foreign keys, foerign keys indexes, tables.

These values are mapped to the template :
TODO


