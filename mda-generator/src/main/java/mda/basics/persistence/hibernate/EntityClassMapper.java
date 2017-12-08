package mda.basics.persistence.hibernate;

import java.io.Serializable;

/**
 * Classe qui permet de récupérer le Bean d'implémentation Hibernate depuis une interface
 */
public interface EntityClassMapper {

    /**
     * Renvoie le Bean hibernate approprié par rapport à l'interface passée en argument
     * @param interfaceDeLaClasse l'interface à partir de laquelle chercher
     * @return le bean hibernate approprié
     */
    Class<? extends Serializable> getEntityClassFromInterface(final Class<?> interfaceDeLaClasse);
}
