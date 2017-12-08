// --------------------------------------------------------------
// Projet : BULL-Commons
// Client : BULL
// Auteur : BULL CSP
// Copyright © BULL - 2008
// --------------------------------------------------------------
// $Id: $File $Revision $DateZ $Author $
// --------------------------------------------------------------

package mda.basics.persistence.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import mda.basics.exceptions.DataAccessException;
import mda.basics.persistence.Entity;


/**
 * Description de l'interface : Description des fonctions générique Hibernate
 * <hr/>
 * @param <T> ClassePersistante
 * @param <ID> Type de la clé primaire
 * @author BULL - $Author: cvs_cr $
 * @version $Revision: 1.10 $ - $Date: 2005/04/26 12:21:45 $
 */
public interface GenericDAO<T extends Entity<ID>, ID extends Serializable> {

    /**
     * Recherche par ID
     * @param id Id de l'objet
     * @return Object
     * @throws DataAccessException Exception DAO
     */
    T findById(ID id) throws DataAccessException;

    /**
     * Recherche par ID en bloquant la ligne (Select for update)
     * @param id Id de l'objet
     * @return Object
     * @throws DataAccessException Exception DAO
     */
    T selectForUpdate(ID id) throws DataAccessException;

    /**
     * Renvoie tous les objets existants pour la base associée au generic
     * @return la liste des objets
     * @throws DataAccessException s'il y a une erreur d'accès aux données
     */
    List<T> findAll() throws DataAccessException;

    /**
     * Compte le nombre d'objet
     * @return le nombre d'objet
     * @throws DataAccessException Exception DAO
     */
    long countAll() throws DataAccessException;

    /**
     * Creation d'un objet
     * @param bo l'objet à créer
     * @return l'objet crée
     * @throws DataAccessException Exception DAO
     */
    T save(T bo) throws DataAccessException;

    /**
     * Modification d'un objet
     * @param bo l'objet à modifier
     * @return l'objet persisté
     * @throws DataAccessException Exception DAO
     */
    T merge(T bo) throws DataAccessException;

    /**
     * Supression d'un objet
     * @param bo objet à supprimer
     * @throws DataAccessException Exception DAO
     */
    void delete(T bo) throws DataAccessException;

    /**
     * Vide la table de toutes ses entrées.
     * @throws DataAccessException Exception DAO
     */
    void deleteAll() throws DataAccessException;

    /**
     * Initialisation d'un objet
     * @param bo objet à initialiser
     * @throws DataAccessException Exception DAO
     */
    void initialise(Object bo) throws DataAccessException;

    /**
     * Permet de détacher un objet
     * @param bo objet à initialiser
     * @throws DataAccessException Exception DAO
     */
    void detache(T bo) throws DataAccessException;

    /**
     * Permet de détacher un objet
     * @param obj objet à détacher
     * @throws DataAccessException Exception DAO
     */
    void detacheObject(Object obj) throws DataAccessException;

    /**
     * Vérifie si un object est détacher
     * @param bo objet à vérifier
     * @return true si l'objet est détaché
     * @throws DataAccessException Exception DAO
     */
    boolean isDetache(T bo) throws DataAccessException;

    /**
     * Méthode qui renvoie une liste d'objets depuis une liste d'ID
     * @param listeIds la liste d'IDs
     * @return la liste d'objets
     * @throws TechniqueException exception technique
     */
    Set<T> getListeInstancesByListeIds(Set<ID> listeIds) throws DataAccessException;

    /**
     * Méthode qui permet de raffraichir un objet depuis la base de données (utilise quand on modifie un objet via une requete pour le récupérer dans son état après la requete)
     * @param bo l'objet à rafraichir
     * @throws DataAccessException erreur d'accès aux données
     */
    void rechargeDepuisSourceDonnees(T bo) throws DataAccessException;

    /**
     * Renvoie l'identifiant d'un objet qui est dans le contexte DAO
     * @param objet l'objet duquel renvoyer l'identifiant
     * @return l'identifiant
     * @throws DataAccessException erreur d'accès aux données
     */
    ID getId(final T objet) throws DataAccessException;

    /**
     * Renvoie l'identifiant d'un objet sans passé qui n'est pas dans le contexte DAO
     * @param objet l'objet duquel renvoyer l'identifiant
     * @return l'identifiant
     * @throws TechniqueException exception technique
     */
    ID getIdentifiantDepuisInstanceNonPersistente(final T objet) throws DataAccessException;

    /**
     * Flush le flux SQL
     * @throws DataAccessException exception d'accès aux données
     */
    void flush() throws DataAccessException;

    /**
     * Méthode qui attache l'objet au contexte
     * @param bo l'objet à attacher
     * @throws DataAccessException erreur d'accès aux données
     */
    void attache(T bo) throws DataAccessException;
}
