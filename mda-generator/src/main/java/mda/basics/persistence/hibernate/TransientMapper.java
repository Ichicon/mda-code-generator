package mda.basics.persistence.hibernate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;

import mda.basics.exceptions.DataAccessException;
import mda.basics.persistence.Entity;



/**
 * Classe de conversion entre un objet entité persistence et une entité transient.
 * @author DCSID
 */
public class TransientMapper {

    /** Les entités déjà transformées */
    private final Map<String, Entity<?>> transientEntities = new HashMap<String, Entity<?>>();

    /**
     * Déconnecte l'entité.
     * @param entity l'entité
     * @return l'entité déconnectée
     * @throws DataAccessException erreur DataAccess
     */
    public Entity<?> disconnectEntity(final Entity<?> entity) throws DataAccessException {
        final String keyForEntity = keyForEntity(entity);
        if (transientEntities.containsKey(keyForEntity)) {
            return transientEntities.get(keyForEntity);
        }

        // instanciation de la nouvelle entité
        final Entity<?> retour = getNewInstanceOfEntity(entity);
        transientEntities.put(keyForEntity, retour);
        // copie des champs
        copyFields(entity, retour);
        return retour;
    }

    /**
     * Construit une clé unique pour l'entité
     * @param entity l'entité
     * @return la clé de l'entité
     */
    private static String keyForEntity(final Entity<?> entity) {
        if (entity == null) {
            return "null";
        }
        if (entity.getId() == null) {
            return entity.getClass().getCanonicalName() + entity.hashCode();
        }
        return entity.getClass().getCanonicalName() + entity.getId();
    }

    /**
     * Déconnecte une collection
     * @param persistentCollection la collection
     * @return la collection déconnectée
     * @throws DataAccessException erreur DataAccess
     */
    public Collection<?> disconnectCollection(final PersistentCollection persistentCollection)
                    throws DataAccessException {

        // ******************
        // NOTE : Si type non géré, consulter http://java.developpez.com/faq/hibernate/?page=Objets#recupererPojo
        // ******************

        // si le champ contient une map de DTOs ou XTOs, il faut les convertir également
        // else if (persistentCollection instanceof Map<?, ?>) {
        // c'est une map
        // final Map<?, ?> v_map = (Map<?, ?>) fieldValue;
        // setMapFieldValue(to, fieldName, v_map);
        // }

        final Collection<?> retour;
        // si le champ contient un Set, il faut le convertir
        if (persistentCollection instanceof PersistentSet) {
            // c'est un set
            final PersistentSet set = (PersistentSet) persistentCollection;
            retour = convertSet(set);
        } else if (persistentCollection instanceof PersistentBag) {
            // si le champ contient un Bag, il faut le convertir
            // c'est un set
            final PersistentBag set = (PersistentBag) persistentCollection;
            retour = convertBag(set);
        } else {
            retour = null;
        }
        return retour;
    }

    /**
     * Créé une nouvelle instance de l'entité
     * @param entity l'entité
     * @return une nouvelle instance de l'entité
     * @throws DataAccessException erreur DataAccess
     */
    @SuppressWarnings("unchecked")
    private static Entity<?> getNewInstanceOfEntity(final Entity<?> entity) throws DataAccessException {
        try {
            final Constructor<? extends Entity<?>> cons = (Constructor<? extends Entity<?>>) entity.getClass()
                            .getDeclaredConstructor();
            cons.setAccessible(true);
            return cons.newInstance();
        } catch (final Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    /**
     * Copie champ par champ d'un objet source vers un objet destination.
     * @param from Objet source
     * @param to Objet destination
     * @throws DataAccessException erreur DataAccess
     */
    private void copyFields(final Entity<?> from, final Entity<?> to) throws DataAccessException {
        // récupération des attributs de l'objet source, et vérification avec ceux de l'objet destination
        final List<String> fields = listFields(from);

        // parcours de chaque attribut (v_fields ne peut pas être null)
        for (final String fieldName : fields) {
            try {
                copyField(from, to, fieldName);
            } catch (final Exception e) {
                throw new DataAccessException(e.getMessage(), e);
            }
        }
    }

    /**
     * Copie d'un champ d'un objet source vers un objet destination.
     * @param from Objet source
     * @param to Objet destination
     * @param fieldName Nom du champ
     * @throws DataAccessException erreur DataAccess
     */
    private void copyField(final Object from, final Object to, final String fieldName) throws DataAccessException {
        // récupération de la valeur de l'objet source
        final Object fieldValue = getFieldValue(from, fieldName);

        // si le champ contient une Entity, il faut la convertir également
        if (fieldValue instanceof Entity<?>) {
            // conversion de l'entité
            final Entity<?> disconnectedEntity = disconnectEntity((Entity<?>) fieldValue);
            setFieldValue(to, fieldName, disconnectedEntity);
        } else if (fieldValue instanceof PersistentCollection) {
            final Collection<?> disconnectedCollection = disconnectCollection((PersistentCollection) fieldValue);
            setFieldValue(to, fieldName, disconnectedCollection);
        } else {
            // mise à jour de la valeur dans l'objet destination
            setFieldValue(to, fieldName, fieldValue);
        }
    }

    /**
     * Modifie la valeur d'un champ de type Collection dans un objet.
     * @param persistentSet la collection
     * @return la collection déconnectée
     * @throws DataAccessException erreur DataAccess
     */
    private Set<?> convertSet(final PersistentSet persistentSet) throws DataAccessException {
        if (persistentSet != null && persistentSet.wasInitialized()) {
            final HashSet<Object> newSet = new HashSet<Object>(persistentSet.size());
            for (final Object setElement : persistentSet) {
                if (setElement == null) {
                    newSet.add(null);
                } else {
                    if (setElement instanceof Entity<?>) {
                        newSet.add(disconnectEntity((Entity<?>) setElement));
                    } else {
                        newSet.add(setElement);
                    }
                }
            }
            return newSet;
        } else {
            return null;
        }
    }

    /**
     * Modifie la valeur d'un champ de type Collection dans un objet.
     * @param persistentBag la collection
     * @return la collection déconnectée
     * @throws DataAccessException erreur DataAccess
     */
    private List<Object> convertBag(final PersistentBag persistentBag) throws DataAccessException {
        if (persistentBag != null && persistentBag.wasInitialized()) {
            final List<Object> newList = new ArrayList<Object>(persistentBag.size());
            for (final Object setElement : persistentBag) {
                if (setElement == null) {
                    newList.add(null);
                } else {
                    if (setElement instanceof Entity<?>) {
                        newList.add(disconnectEntity((Entity<?>) setElement));
                    } else {
                        newList.add(setElement);
                    }
                }
            }
            return newList;
        } else {
            return null;
        }
    }

    /**
     * Modifie la valeur d'un champ de type Map dans un objet.
     * @param p_instance l'instance de l'objet
     * @param p_fieldName le nom du champ
     * @param p_map la nouvelle valeur du champ
     */
    // @SuppressWarnings("unchecked")
    // private void setMapFieldValue(final Object p_instance, final String p_fieldName, final Map<?, ?> p_map) {
    // // création d'une map qui pourra être transformée
    // Map<?, ?> v_mapTransformee = p_map;
    // if (!p_map.isEmpty()) {
    // // récupération de la première clé et de la première valeur non nulls pour tester le type
    // final Object v_keyValueTest = getFirstNonNull(p_map.keySet());
    // final Object v_valueValueTest = getFirstNonNull(p_map.values());
    // // conversion des clés de la map
    // if (v_keyValueTest instanceof Dto_Itf<?>) {
    // v_mapTransformee = convertMapDtoAsKey((Map<Dto_Itf<?>, Object>) v_mapTransformee, p_fieldName);
    // } else if (v_keyValueTest instanceof Xto_Itf<?>) {
    // v_mapTransformee = convertMapXtoAsKey((Map<Xto_Itf<?>, Object>) v_mapTransformee, p_fieldName);
    // }
    // // conversion des valeurs de la map
    // if (v_valueValueTest instanceof Dto_Itf<?>) {
    // v_mapTransformee = convertMapDtoAsValue((Map<Object, Dto_Itf<?>>) v_mapTransformee, p_fieldName);
    // } else if (v_valueValueTest instanceof Xto_Itf<?>) {
    // v_mapTransformee = convertMapXtoAsValue((Map<Object, Xto_Itf<?>>) v_mapTransformee, p_fieldName);
    // }
    // }
    //
    // // mise à jour de la map convertie dans l'objet destination
    // setFieldValue(p_instance, p_fieldName, v_mapTransformee);
    // }

    /**
     * Convertit une map dans laquelle les clés sont des DTO.
     * @param p_map la map de DTO initiale qui sera convertie
     * @param p_fieldName Le champ de l'instance qui recevra la map de XTO convertie
     * @return la map convertie
     */
    // @SuppressWarnings("unchecked")
    // private Map<?, ?> convertMapDtoAsKey(final Map<Dto_Itf<?>, Object> p_map, final String p_fieldName) {
    // // instanciation d'une nouvelle map
    // final Map<Xto_Itf<?>, Object> v_newMap = (Map<Xto_Itf<?>, Object>) createMapOfSameType(p_map);
    // final Mapper_Itf<? extends Dto_Itf<?>, ? extends Xto_Itf<?>> v_specificMapper = getSpecificMapper(p_fieldName);
    // if (v_specificMapper != null) {
    // for (final Entry<Dto_Itf<?>, ?> v_mapEntry : p_map.entrySet()) {
    // if (v_mapEntry.getKey() == null) {
    // v_newMap.put(null, v_mapEntry.getValue());
    // } else {
    // final Dto_Itf<?> v_dtoElement = v_mapEntry.getKey();
    // final Xto_Itf<?> v_xtoElement = v_specificMapper.convertDtoItfToXto(v_dtoElement);
    // v_newMap.put(v_xtoElement, v_mapEntry.getValue());
    // }
    // }
    // }
    // return v_newMap;
    // }
    //
    // /**
    // * Convertit une map dans laquelle les clés sont des XTO.
    // * @param p_map la map de XTO initiale qui sera convertie
    // * @param p_fieldName Le champ de l'instance qui recevra la map de DTO convertie
    // * @return la map convertie
    // */
    // @SuppressWarnings("unchecked")
    // private Map<?, ?> convertMapXtoAsKey(final Map<Xto_Itf<?>, Object> p_map, final String p_fieldName) {
    // // instanciation d'une nouvelle map
    // final Map<Dto_Itf<?>, Object> v_newMap = (Map<Dto_Itf<?>, Object>) createMapOfSameType(p_map);
    // final Mapper_Itf<? extends Dto_Itf<?>, ? extends Xto_Itf<?>> v_specificMapper = getSpecificMapper(p_fieldName);
    // if (v_specificMapper != null) {
    // for (final Entry<Xto_Itf<?>, ?> v_mapEntry : p_map.entrySet()) {
    // if (v_mapEntry.getKey() == null) {
    // v_newMap.put(null, v_mapEntry.getValue());
    // } else {
    // final Xto_Itf<?> v_xtoElement = v_mapEntry.getKey();
    // final Dto_Itf<?> v_dtoElement = v_specificMapper.convertXtoItfToDto(v_xtoElement);
    // v_newMap.put(v_dtoElement, v_mapEntry.getValue());
    // }
    // }
    // }
    // return v_newMap;
    // }
    //
    // /**
    // * Convertit la map de DTO p_map en map de XTO.
    // * @param p_map la map de DTO initiale qui sera convertie
    // * @param p_fieldName Le champ de l'instance qui recevra la map de XTO convertie
    // * @return la map convertie
    // */
    // @SuppressWarnings("unchecked")
    // private Map<?, ?> convertMapDtoAsValue(final Map<Object, Dto_Itf<?>> p_map, final String p_fieldName) {
    // // instanciation d'une nouvelle map
    // final Map<Object, Xto_Itf<?>> v_newMap = (Map<Object, Xto_Itf<?>>) createMapOfSameType(p_map);
    // final Mapper_Itf<? extends Dto_Itf<?>, ? extends Xto_Itf<?>> v_specificMapper = getSpecificMapper(p_fieldName);
    // if (v_specificMapper != null) {
    // for (final Entry<?, Dto_Itf<?>> v_mapEntry : p_map.entrySet()) {
    // if (v_mapEntry.getValue() == null) {
    // v_newMap.put(v_mapEntry.getKey(), null);
    // } else {
    // final Dto_Itf<?> v_dtoElement = v_mapEntry.getValue();
    // final Xto_Itf<?> v_xtoElement = v_specificMapper.convertDtoItfToXto(v_dtoElement);
    // v_newMap.put(v_mapEntry.getKey(), v_xtoElement);
    // }
    // }
    // }
    // return v_newMap;
    // }
    //
    // /**
    // * Convertit la map de XTO p_map en map de DTO et l'insère dans le champ p_fieldName de p_to.
    // * @param p_map la map de XTO initiale qui sera convertie
    // * @param p_fieldName Le champ de l'instance qui recevra la map de DTO convertie
    // * @return la map convertie
    // */
    // @SuppressWarnings("unchecked")
    // private Map<?, ?> convertMapXtoAsValue(final Map<Object, Xto_Itf<?>> p_map, final String p_fieldName) {
    // // instanciation d'une nouvelle map
    // final Map<Object, Dto_Itf<?>> v_newMap = (Map<Object, Dto_Itf<?>>) createMapOfSameType(p_map);
    // final Mapper_Itf<? extends Dto_Itf<?>, ? extends Xto_Itf<?>> v_specificMapper = getSpecificMapper(p_fieldName);
    // if (v_specificMapper != null) {
    // for (final Entry<?, Xto_Itf<?>> v_mapEntry : p_map.entrySet()) {
    // if (v_mapEntry.getValue() == null) {
    // v_newMap.put(v_mapEntry.getKey(), null);
    // } else {
    // final Xto_Itf<?> v_xtoElement = v_mapEntry.getValue();
    // final Dto_Itf<?> v_dtoElement = v_specificMapper.convertXtoItfToDto(v_xtoElement);
    // v_newMap.put(v_mapEntry.getKey(), v_dtoElement);
    // }
    // }
    // }
    // return v_newMap;
    // }

    /**
     * Retourne le premier élément non null de la collection en paramètre, ou null sinon.
     * @param p_collection Collection
     * @return Object
     */
    // private static Object getFirstNonNull(final Collection<?> p_collection) {
    // Object v_retour = null;
    // for (final Object v_value : p_collection) {
    // if (v_value != null) {
    // v_retour = v_value;
    // break;
    // }
    // }
    // return v_retour;
    // }

    /**
     * Récupère la valeur d'un champ pour un objet.
     * @param instance l'instance de l'objet
     * @param fieldName le nom du champ
     * @return la valeur de ce champ pour cette instance d'objet
     */
    private static Object getFieldValue(final Object instance, final String fieldName) {
        try {
            // On n'appelle pas le getter pour ne pas faire appel au lazy loading.
            // On ne veut certainement pas charger toutes les références de chacun des objets qu'on convertit alors qu'on n'utilisera pas ces données dans l'entité déconnectée.
            // Si ces données sont réellement nécessaires côté client, alors elles auront dû être chargées spécifiquement par les services.
            final Class<? extends Object> clazz = instance.getClass();
            final Field field = findField(clazz, fieldName);
            return field.get(instance);
        } catch (final Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Modifie la valeur d'un champ dans un objet.
     * @param instance l'instance de l'objet
     * @param fieldName le nom du champ
     * @param value la nouvelle valeur du champ
     */
    private static void setFieldValue(final Object instance, final String fieldName, final Object value) {
        if (value != null) {
            try {
                final Class<? extends Object> clazz = instance.getClass();
                // try {
                // en passant par le setter
                // final Method setter = clazz.getDeclaredMethod("set" + fieldName.substring(0, 1).toUpperCase()
                // + fieldName.substring(1), value.getClass());
                // setter.invoke(instance, value);
                // } catch (final NoSuchMethodException e) {
                // le setter n'a pas été trouvé dans la classe, utilisation de l'attribut directement
                final Field field = findField(clazz, fieldName);
                field.set(instance, value);
                // }
            } catch (final Throwable e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * Recherche un champ récursivement dans une entité et ses ancêtres
     * @param clazz la classe de l'entité
     * @param fieldName le nom du champ
     * @return le champ trouvé
     * @throws NoSuchFieldException si le champ n'a pas été trouvée
     */
    private static Field findField(final Class<?> clazz, final String fieldName) throws NoSuchFieldException {
        // liste les champs du dto
        for (final Field field : clazz.getDeclaredFields()) {
            // on exclut les champs static final comme serialVersionUID car ils ne nous intéressent pas (et en plus on ne peut pas changer la valeur)
            // on exclut également le champ qui contient la valeur de la version (@JdbcVersion ou @Version)
            if (isFieldCopiable(field) && field.getName().equals(fieldName)) {
                field.setAccessible(true);
                return field;
            }
        }
        if (clazz.getSuperclass() != null) {
            return findField(clazz.getSuperclass(), fieldName);
        }
        throw new NoSuchFieldException(fieldName);

    }

    /**
     * Retourne la liste des champs pour cet objet
     * @param entity (In)(*) L'objet métier de ce mapper.
     * @return la liste des champs pour cet objet
     */
    private static List<String> listFields(final Entity<?> entity) {
        return listFields(entity.getClass());
    }

    /**
     * Retourne la liste des champs pour cette classe.
     * @param clazz la classe
     * @return la liste des champs
     */
    private static List<String> listFields(final Class<?> clazz) {
        final List<String> retour = new ArrayList<String>();
        // liste les champs du dto
        for (final Field field : clazz.getDeclaredFields()) {
            // on exclut les champs static final comme serialVersionUID car ils ne nous intéressent pas (et en plus on ne peut pas changer la valeur)
            // on exclut également le champ qui contient la valeur de la version (@JdbcVersion ou @Version)
            if (isFieldCopiable(field)) {
                retour.add(field.getName());
            }
        }
        if (clazz.getSuperclass() != null) {
            retour.addAll(listFields(clazz.getSuperclass()));
        }
        return retour;
    }

    /**
     * Est-ce que cet attribut est copiable
     * @param field Field
     * @return boolean
     */
    private static boolean isFieldCopiable(final Field field) {
        // si le champ est static et final on ne le copie pas
        final int modifiers = field.getModifiers();
        return !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers);
    }

}
