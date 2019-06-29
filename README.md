# mda-code-generator
Génération de code java et sql à partir d'une modélisation xml

## Modifications du modèle de données (sous ea 11)

Modifier le schéma dans xxxxx.eap avec Entreprise Architect.

  * Pour ajouter un nouveau domaine :
       * Aller dans Settings → Code Engineering Datatypes. Choisir Java puis saisir ou modifier les DO_xxx.
       * Il faut exporter les metadata au format XML avec Project -> Model Import/Export -> Export Reference Data et cocher Model Data Types - Codes an DDL
       * Remplacer le fichier metadata_xxxx.xml par ce nouveau fichier (ou le créer).
  * Pour définir la clé primaire, il faut aller dans le détail de l'attribut et cocher isId
  * Pour définir un attribut nullable, il faut aller dans le détail de l'attribut et mettre 0 dans Lower bound de Multiplicity.
  * Pour ajouter une relation entre deux classes :
     * Utiliser le 1er trait (Associate) dans Class Relationships.
     * Sur l'association ([xxxx] ⇔ facultatif) :
         * General → Name : nom1(x caractères, sans underscore)_nom2(x caractères, sans underscore) → ne pas dépasser 26 caractères à cause des noms des index générés (IDX_xxxx)
         * [Source Role → Role] : Nom pour le getNOMOBJET (utile en cas de double liaison vers la même table). Par défaut nom de la classe pointée.
         * [Source Role → Alias] Nom pour la colonne FK dans la table (utile en cas de double liaison vers la même table). Par défaut nom de la PK de la classe pointée.
         * [Target Role → Role] : Nom pour le getNOMOBJET (utile en cas de double liaison vers la même table)
         * [Target Role → Alias] Nom pour la colonne FK dans la table (utile en cas de double liaison vers la même table). Par défaut nom de la PK de la classe pointée.
         * [Target Role ou Source Role -> Owned] /!\ Pour une OneToOne permet d'indiquer que ce côté de la relation sera pointée par l'autre (la FK sera dans la table "owner")
   * Pour ajouter des commentaires :
       * Sur le package, il faut ajouter un objet Note sur le schéma et écrire dedans.
       * Sur une classe il faut saisir le commentaire dans le champ “Notes”
       * Sur un attribut il faut saisir le commentaire dans le champs “Alias”
