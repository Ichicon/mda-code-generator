package mda.basics;

/**
 * Constantes génériques
 */
public final class Constantes {

    /**
     * Contructeur privé afin d'éviter l'instanciation de la classe utilitaire. : Constantes()
     */
    private Constantes() {
    }

    /**
     * Message d'erreur : Session hibernate null ou fermée.
     */
    public static final String MSG_SESSION_CLOSE_OR_NULL = "Session fermée";

    /**
     * Message d'erreur : Impossible de créer la session hibernate.
     */
    public static final String MSG_ERR_SESSION_FACTORY = "Erreur d'initialisation de la session";

    /**
     * Message d'erreur : Une erreur inattendue s'est produite
     */
    public static final String ERREUR_INATTENDUE = "Erreur technique inattendue";

    /**
     * Message d'erreur : Une erreur de type accès aux données s'est produite
     */
    public static final String ERREUR_ACCESS = "Erreur technique d'accès aux données";

    /** Id incorrect */
    public static final String ERREUR_ID_INCORRECT = "Id incorrect";

    /**
     * Problème fonctionnel - message par defaut
     */
    public static final String MSG_ERREUR_FONCTIONNELLE_DEFAULT = "Erreur fonctionnelle";

    /**
     * Erreur d'export sur une liste
     */
    public static final String EXPORT_ERREUR_TECHNIQUE = "Impossible d'exporter la liste, une exception technique s'est produite.";

}
