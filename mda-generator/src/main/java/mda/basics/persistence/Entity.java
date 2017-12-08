package mda.basics.persistence;

import java.io.Serializable;

/**
 * Interface générique des beans.
 * @param <ID> le type de clé primaire du bean
 */
public interface Entity<ID> extends Serializable {

    /**
     * Permet de récupérer l'identifiant du Bean.
     * @return l'identifiant du Bean
     */
    ID getId();

    /**
     * Permet de setter l'identifiant du Bean.
     * @param id l'identifiant du Bean
     */
    void setId(ID id);

}
