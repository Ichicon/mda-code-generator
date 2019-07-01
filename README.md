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
         * [Source Role → Role] : Name for get"ObjectName" (useful in case of double association to the same class). Default is PK name of pointed class.
         * [Source Role → Alias] Nom pour la colonne FK dans la table (utile en cas de double liaison vers la même table). Par défaut nom de la PK de la classe pointée.
         * [Target Role → Role] : Nom pour le getNOMOBJET (utile en cas de double liaison vers la même table)
         * [Target Role → Alias] Nom pour la colonne FK dans la table (utile en cas de double liaison vers la même table). Par défaut nom de la PK de la classe pointée.
         * [Target Role ou Source Role -> Owned] /!\ Pour une OneToOne permet d'indiquer que ce côté de la relation sera pointée par l'autre (la FK sera dans la table "owner")
   * Pour ajouter des commentaires :
       * Sur le package, il faut ajouter un objet Note sur le schéma et écrire dedans.
       * Sur une classe il faut saisir le commentaire dans le champ “Notes”
       * Sur un attribut il faut saisir le commentaire dans le champs “Alias”
