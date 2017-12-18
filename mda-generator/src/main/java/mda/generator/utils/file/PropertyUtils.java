package mda.generator.utils.file;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import mda.generator.exceptions.MdaGeneratorException;

public final class PropertyUtils {
	private PropertyUtils () {
		
	}

	/**
	 * 
	 * @param key
	 * @param prop
	 */
	public static void loadPathFromProperty(String key , Properties prop, Object target) {
		if(prop != null) {
			String val = prop.getProperty(key);
			if(val != null) {
				setAttributeValue(target, key, PathUtils.getPathForClassPathAndFs(val));
			}
		}		
	}

	/**
	 * 
	 * @param key
	 * @param prop
	 */
	public static void loadClassFromProperty(String key, Properties prop, Object target)  {
		if(prop != null) {
			String val = prop.getProperty(key);
			if(val != null) {
				try {
					setAttributeValue(target, key, Class.forName(val));
				} catch (ClassNotFoundException e) {
					throw new MdaGeneratorException("La classe " + val + " n'a pas pu être chargée ",e );
				}
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @param prop
	 */
	public static void loadStringList(String key, Properties prop, Object target)  {
		if(prop != null) {
			String val = prop.getProperty(key);
			if(val != null) {
				ArrayList<String> list = new ArrayList<>();
				for(String sVal : val.split(",")) {
					list.add(sVal.trim());
				}
				setAttributeValue(target, key, list);			
			}
		}

	}

	/**
	 * 
	 * @param key
	 * @param prop
	 */
	public static void loadCharset(String key, Properties prop, Object target)  {
		if(prop != null) {
			String val = prop.getProperty(key);
			if(val != null) {
				setAttributeValue(target, key, Charset.forName(val));			
			}
		}

	}

	/**
	 * 
	 * @param key
	 * @param prop
	 */
	public static void loadString(String key, Properties prop, Object target)  {
		if(prop != null) {
			String val = prop.getProperty(key);
			if(val != null) {
				setAttributeValue(target, key, val);			
			}
		}

	}

	private static boolean setAttributeValue(Object object, String fieldName, Object fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(object, fieldValue);
				return true;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return false;
	}
}
