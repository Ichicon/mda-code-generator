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
         * [Source Role → Role] : Name for get"ObjectName" (useful in case of multiple associations to the same class). Default is  is the name of the primary key of the pointed class.
         * [Source Role → Alias] Name for the FK column in the SQL table (useful when multiple association to the same table). Default is the name of the primary key in the other table.
         * [Target Role → Role] : Same as "Source Role -> Role", name for get"ObjectName" (useful in case of multiple associations to the same class). Default is  is the name of the primary key of the pointed class.
         * [Target Role → Alias] Same as "Source Role -> Alias", name for the FK column in the SQL table (useful when multiple association to the same table). Default is the name of the primary key in the other table.
         * [Target Role ou Source Role -> Owned] /!\ For a OneToOne, define which side hold the field (the is in the "owner" table => so not the one with "owned" to true).
   * To add comments :
       * For the package level (package-info.java), you must add a "Note" object into the diagram and write into.
       * For a class, you have to add a comment in "Notes".
       * For an attribute, you must put the comment in the "Alias" field.
