package mda.basics.persistence.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Id;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.query.Query;

import mda.basics.Constantes;
import mda.basics.exceptions.DataAccessException;
import mda.basics.persistence.Entity;

import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;



/**
 * Description de l'interface : Description des fonctions générique Hibernate
 * <hr/>
 * @param <T> ClassePersistante
 * @param <ID> Type de la clé primaire
 * @author BULL - $Author: cvs_cr $
 * @version $Revision: 1.10 $ - $Date: 2005/04/26 12:21:45 $
 */
public class GenericDAOImpl<T extends Entity<ID>, ID extends Serializable> implements GenericDAO<T, ID> {

    /**
     * Log des messages applicatifs.
     */
    private static final Logger LOG = LogManager.getLogger(GenericDAOImpl.class);

    /**
     * Interface de la classe persistante
     */
    private final Class<T> interfaceDeLaClassePersistante;

    /**
     * Classe persistante
     */
    private final Class<?> mPersistentClass;

    /**
     * Classe du paramètre générique ID
     */
    private final Class<ID> classeID;

    /**
     * Nom court de la Classe persistante
     */
    private final String mPersistentClassName;

    /**
     * Session Hibernate
     */
    private Session mSession;

    /**
     * L'entity class mapper qui permet de faire le lien entre implémentation hibernate et interface
     */
    private final EntityClassMapper entityClassMapper = HibernateUtil.getEntityClassMapper(getHibernateKey());

    /**
     * AND pour la génération HQL
     */
    static final String AND = " AND ";

    /**
     * OR pour la génération de HQL
     */
    static final String OR = " OR ";

    /**
     * Constructeur
     */
    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        interfaceDeLaClassePersistante = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        classeID = (Class<ID>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

        if (entityClassMapper == null) {
            throw new NotInitializedException();
        }
        this.mPersistentClass = entityClassMapper.getEntityClassFromInterface(interfaceDeLaClassePersistante);
        // Nom court de la classe Bean
        this.mPersistentClassName = this.mPersistentClass.getName().substring(
                        this.mPersistentClass.getName().lastIndexOf('.') + 1);
    }

    /**
     * Constructeur
     * @param interfaceEntite l'interface du bean qui va être manipulé dans la DAO
     * @param classeId la classe qui correspondent au type de la FK
     */
    public GenericDAOImpl(final Class<T> interfaceEntite, final Class<ID> classeId) {
        interfaceDeLaClassePersistante = interfaceEntite;
        this.classeID = classeId;

        if (entityClassMapper == null) {
            throw new NotInitializedException();
        }
        this.mPersistentClass = entityClassMapper.getEntityClassFromInterface(interfaceDeLaClassePersistante);
        // Nom court de la classe Bean
        this.mPersistentClassName = this.mPersistentClass.getName().substring(
                        this.mPersistentClass.getName().lastIndexOf('.') + 1);
    }

    /**
     * Log
     * @return Instance de la classe de log
     */
    protected static Logger getLog() {
        return LOG;
    }

    /**
     * Session
     * @param s Session hibernate
     */
    public void setSession(final Session s) {
        this.mSession = s;
    }

    /**
     * Session
     * @return La Session hibernate
     * @throws DataAccessException Exception DAO
     */
    protected Session getSession() throws DataAccessException {
        if (mSession == null) {
            mSession = HibernateUtil.currentSession(getHibernateKey());
        }
        if (!mSession.isOpen()) {
            // Déclenchement d'une exception : Session fermée ou null
            throw new DataAccessException(Constantes.MSG_SESSION_CLOSE_OR_NULL);
        }
        // Retourne l'objet session
        return mSession;
    }

    /**
     * @return la clé de la configuration hibernate ("" par défaut)
     */
    protected String getHibernateKey() {
        return "";
    }

    @SuppressWarnings("unchecked")
	public ID getId(final T objet) throws DataAccessException {
        return (ID) getSession().getIdentifier(objet);
    }

    /**
     * Classe de l'objet persistant
     * @return Classe de l'objet persistant
     */
    public Class<?> getPersistentClass() {
        return mPersistentClass;
    }

    /**
     * Classe de l'objet persistant
     * @return Classe de l'objet persistant
     */
    public String getPersistentClassName() {
        return mPersistentClassName;
    }


    public T findById(final ID id) throws DataAccessException {
        final T bo = (T) getSession().get(getPersistentClass(), id);
        return bo;
    }


    public T selectForUpdate(final ID id) throws DataAccessException {
        final T bo = (T) getSession().get(getPersistentClass(), id, LockOptions.UPGRADE);
        return bo;
    }


    public List<T> findAll() throws DataAccessException {
        final StringBuilder hqlQuery = new StringBuilder();
        hqlQuery.append("FROM ").append(mPersistentClassName);
        final Query query = getSession().createQuery(hqlQuery.toString());
        query.setCacheable(isEntityCacheable());
        return query.list();
    }

    /**
     * @return true si l'entité provient d'une table de référence et est donc mise en cache (voir annotation Hibernate Cache)
     */
    private boolean isEntityCacheable() {
        final Cache cacheAnnotation = getPersistentClass().getAnnotation(Cache.class);
        return cacheAnnotation != null && cacheAnnotation.usage() != CacheConcurrencyStrategy.NONE;
    }

    /**
     * Méthode qui renvoie la liste des objets triés par le champ passé en paramètre
     * @param nomChamp le nom du champ
     * @param sensTri le sens du tri (Constantes.TRI_ASC ou Constantes.TRI_DESC) - si null ASC utilisé
     * @return la liste d'objets appropriés
     * @throws DataAccessException erreur d'accès aux données
     */
    @SuppressWarnings("unchecked")
    protected List<T> findAll(final String nomChamp, final String sensTri) throws DataAccessException {
        final StringBuilder hqlQuery = new StringBuilder();
        hqlQuery.append("FROM ").append(mPersistentClassName);
        if (nomChamp != null && !nomChamp.isEmpty()) {
            hqlQuery.append(" ORDER BY ").append(nomChamp);
        }
        if (sensTri != null) {
            hqlQuery.append(' ').append(sensTri);
        }
        final Query query = getSession().createQuery(hqlQuery.toString());
        return query.list();
    }


    public long countAll() throws DataAccessException {
        // Requete pour compter le nombre d'enregistrement
        final String sqlQuery = "select count(*) from " + mPersistentClassName;
        final Query query = getSession().createQuery(sqlQuery);
        return (Long) query.uniqueResult();
    }

    public T merge(final T bo) throws DataAccessException {
        return (T) getSession().merge(bo);
    }

    public T save(final T bo) throws DataAccessException {
        getSession().save(bo);
        return bo;
    }
    public void delete(final T bo) throws DataAccessException {
        getSession().delete(bo);
        getSession().flush();
    }

    public void deleteAll() throws DataAccessException {
        // Requete pour compter le nombre d'enregistrement
        final String sqlQuery = "delete from " + mPersistentClassName;
        final Query query = getSession().createQuery(sqlQuery);
        query.executeUpdate();
    }

    /**
     * Flush de la session hibernate
     * @throws DataAccessException Exception DAO
     */
    public void flush() throws DataAccessException {
        getSession().flush();
    }

    /**
     * Clear de la session hibernate
     * @throws DataAccessException Exception DAO
     */
    public void clear() throws DataAccessException {
        getSession().clear();
    }

    /**
     * {@inheritDoc}
     */
    public void initialise(final Object bo) throws DataAccessException {
        Hibernate.initialize(bo);
    }

    /**
     * {@inheritDoc}
     */
    public void detache(final T bo) throws DataAccessException {
        getSession().evict(bo);
    }

    /**
     * {@inheritDoc}
     */
    public void detacheObject(final Object obj) throws DataAccessException {
        getSession().evict(obj);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDetache(final T bo) throws DataAccessException {
        return !getSession().contains(bo);
    }

    /**
     * {@inheritDoc}
     */
    public void rechargeDepuisSourceDonnees(final T bo) throws DataAccessException {
        getSession().refresh(bo);
    }

    /**
     * {@inheritDoc}
     */
    public void attache(final T bo) throws DataAccessException {
        getSession().update(bo);
    }

    
    public ID getIdentifiantDepuisInstanceNonPersistente(final T objet) throws DataAccessException {
        ID identifiant = null;
        try {
            for (final Method method : getPersistentClass().getDeclaredMethods()) {
                final Id annotation = method.getAnnotation(javax.persistence.Id.class);
                if (annotation != null) {
                    identifiant = (ID) method.invoke(objet);
                    break;
                }
            }
            for (final Field field : getPersistentClass().getDeclaredFields()) {
                // si le champ a le @Id positionné sur lui (possible en
                // hibernate)
                final Id annotation = field.getAnnotation(javax.persistence.Id.class);
                if (annotation != null) {
                    identifiant = (ID) field.get(objet);
                    break;
                }
            }
        } catch (final Exception e) {
            throw new DataAccessException("Problème durant la récupération de la valeur de la PK par réflection", e);
        }
        return identifiant;
    }

    public Set<T> getListeInstancesByListeIds(final Set<ID> listeIds) throws DataAccessException {
        Set<T> listeObjets = new HashSet<T>();

        if (listeIds == null || listeIds.isEmpty()) {
            return listeObjets;
        }

        // si la PK n'est pas de type number (objet composé) il faut utiliser n
        // requetes getById
        // optimisation : si il y a un seul id, on utilise getById
        if (listeIds.size() == 1 || !Number.class.isAssignableFrom(classeID)) {
            for (final ID id : listeIds) {
                listeObjets.add(findById(id));
            }
        } else {
            // sinon on peut le faire avec une seule requête in
            String nomAttribut = null;
            String nomMethodeSansGet = null;
            // on regarde d'abord si on trouve une méthode qui a l'annotation ID
            final Method[] declaredMethods = getPersistentClass().getDeclaredMethods();
            for (final Method method : declaredMethods) {
                final Id annotation = method.getAnnotation(javax.persistence.Id.class);
                if (annotation != null) {
                    nomMethodeSansGet = method.getName().replace("get", "");
                    break;
                }
            }
            // pour chaque champ on regarde si on trouve un champ qui a
            // l'annotation ID ou si on trouve un
            // champ qui correspond au getter trouvé dans la première boucle
            final Field[] declaredFields = getPersistentClass().getDeclaredFields();
            for (final Field field : declaredFields) {
                final String nomChamp = field.getName();
                // si une méthode getter est utilisée par ce champ
                if (nomMethodeSansGet != null && nomMethodeSansGet.equalsIgnoreCase(nomChamp)) {
                    nomAttribut = nomChamp;
                    break;
                }
                // si le champ a le @Id positionné sur lui (possible en
                // hibernate)
                final Id annotation = field.getAnnotation(javax.persistence.Id.class);
                if (annotation != null) {
                    nomAttribut = nomChamp;
                    break;
                }
            }
            if (nomAttribut == null) {
                throw new DataAccessException("Aucun ID trouvé sur le bean : " + getPersistentClassName());
            }
            final Criteria criteria = getSession().createCriteria(getPersistentClass());
            criteria.add(Restrictions.in(nomAttribut, listeIds));
            listeObjets = new HashSet<T>(criteria.list());
        }
        return listeObjets;
    }

    /**
     * Lance q.list() et retourne la liste 'castÃ©e'
     * @param <W> le type de la liste
     * @param q query hibernate
     * @return la liste 'castÃ©e'
     */
    @SuppressWarnings("unchecked")
    public <W> List<W> listAndCast(final Query q) {
        return q.list();
    }  
}
