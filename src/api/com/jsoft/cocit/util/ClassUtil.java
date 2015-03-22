// $codepro.audit.disable unnecessaryCast
package com.jsoft.cocit.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.Entity;

import org.nutz.aop.ClassAgent;
import org.nutz.lang.Mirror;

import com.jsoft.cocimpl.ioc.aop.LazyClassAgent;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.exception.CocException;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public abstract class ClassUtil {

	/**
	 * 判断指定的类是否是基本类型？基本类型包括String、Boolean、Number。
	 * 
	 * @param cls
	 * @return
	 */
	public static boolean isPrimitive(Object obj) {
		return isString(obj) || isBoolean(obj) || isInteger(obj) || isLong(obj) || isByte(obj) || isShort(obj) || isDouble(obj) || isFloat(obj);
	}

	public static Class getTypeOfObject(Class primitiveType) {
		if (boolean.class.equals(primitiveType)) {
			return Boolean.class;
		}
		if (int.class.equals(primitiveType)) {
			return Integer.class;
		}
		if (byte.class.equals(primitiveType)) {
			return Byte.class;
		}
		if (long.class.equals(primitiveType)) {
			return Long.class;
		}
		if (char.class.equals(primitiveType)) {
			return Character.class;
		}
		if (double.class.equals(primitiveType)) {
			return Double.class;
		}
		if (float.class.equals(primitiveType)) {
			return Float.class;
		}
		return primitiveType;
	}

	public static boolean isDate(Object obj) {
		if (obj == null)
			return false;

		Class cls;
		if (obj instanceof Class)
			cls = (Class) obj;
		else
			cls = obj.getClass();

		return Date.class.isAssignableFrom(cls);
	}

	public static boolean isString(Object obj) {
		if (obj == null)
			return false;

		Class cls;
		if (obj instanceof Class)
			cls = (Class) obj;
		else
			cls = obj.getClass();

		return cls.equals(String.class);
	}

	public static boolean isBoolean(Object obj) {
		if (obj == null)
			return false;

		Class cls;
		if (obj instanceof Class)
			cls = (Class) obj;
		else
			cls = obj.getClass();

		return cls.equals(Boolean.class);
	}

	public static boolean isNumber(Object obj) {
		if (isInteger(obj) || isLong(obj) || isByte(obj) || isShort(obj) || isDouble(obj) || isFloat(obj)) {
			return true;
		}

		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return Number.class.isAssignableFrom(kls);
	}

	public static boolean isFloat(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Float.class);
	}

	public static boolean isDouble(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Double.class);
	}

	public static boolean isByte(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Byte.class);
	}

	public static boolean isShort(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Short.class);
	}

	public static boolean isInteger(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Integer.class);
	}

	public static boolean isLong(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Long.class);
	}

	public static <T> T newInstance(String name, Object... args) {
		if (name == null || name.trim().length() == 0)
			return null;

		Class<T> cls = forName(name);

		return newInstance(cls, args);
	}

	public static <T> T newInstance(Class<T> cls, Object... args) {

		Object object = null;
		if (cls.isArray()) {
			object = Array.newInstance(cls.getComponentType(), 0);
		} else {
			Constructor constructor = null;
			Constructor[] constructors = cls.getDeclaredConstructors();
			for (Constructor c : constructors) {
				Class[] types = c.getParameterTypes();
				if (types.length != args.length)
					continue;

				boolean isMatched = true;
				for (int i = 0; i < args.length; i++) {
					Object arg = args[i];
					if (arg != null && !types[i].isAssignableFrom(arg.getClass())) {
						isMatched = false;
					}
				}
				if (isMatched) {
					constructor = c;
					break;
				}
			}
			if (constructor != null) {
				constructor.setAccessible(true);
				try {
					object = constructor.newInstance(args);
				} catch (Throwable e) {
					throw new CocException(e);
				}
			}
		}

		return (T) object;
	}

	public static Class forName(String name) {
		return forName(name, getDefaultClassLoader());
	}

	public static Class forName(String name, ClassLoader classLoader) {
		// name = name.trim();

		// "java.lang.String[]" style arrays
		if (name.endsWith("[]")) {
			String elementClassName = name.substring(0, name.length() - 2);
			Class elementClass = forName(elementClassName, classLoader);

			return Array.newInstance(elementClass, 0).getClass();
		}

		// "[Ljava.lang.String;" style arrays
		if (name.startsWith("[L") && name.endsWith(";")) {
			String elementClassName = name.substring(2, name.length() - 1);
			Class elementClass = forName(elementClassName, classLoader);

			return Array.newInstance(elementClass, 0).getClass();
		}

		try {
			return classLoader.loadClass(name);
		} catch (ClassNotFoundException e) {
			throw new CocException(e);
		}
	}

	private static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		if (cl == null) {
			cl = ClassUtil.class.getClassLoader();
		}

		return cl;
	}

	public static boolean hasField(Class klass, String propName) {
		try {
			return Mirror.me(klass).getField(propName) != null;
		} catch (Throwable e) {
			LogUtil.warn("", e);
			return false;
		}
	}

	public static boolean isLazy(Object type) {
		if (type == null) {
			return false;
		}
		if (type instanceof Class) {
			return isLazy((Class) type);
		}
		return isLazy(type.getClass());
	}

	public static boolean isLazy(Class type) {
		if (type == null)
			return false;

		return type.getSimpleName().endsWith(LazyClassAgent.LAZY_CLASSNAME_SUFFIX);
	}

	public static boolean isAgent(Class type) {
		if (type == null)
			return false;

		return type.getSimpleName().endsWith(ClassAgent.CLASSNAME_SUFFIX);
	}

	public static Class getType(Class proxyClass) {
		if (isAgent(proxyClass) || isLazy(proxyClass))
			return proxyClass.getSuperclass();

		return proxyClass;
	}

	public static String getDisplayName(Field field) {
		if (field == null) {
			return "";
		}
		return "(" + field.getName() + ")";
	}

	public static String getDisplayName(Class classOfEntity) {
		if (classOfEntity == null) {
			return "";
		}
		return classOfEntity.getSimpleName();
	}

	public static boolean isEntityType(Class type) {
		if (type == null)
			return false;

		if (isLazy(type) || isAgent(type)) {
			return true;
		}
		return type.getAnnotation(Entity.class) != null;
	}

	public static Class getType(Class type, String prop) throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		if (type == null || StringUtil.isBlank(prop))
			return null;

		int dot = prop.indexOf(".");
		if (dot > 0) {
			Class t = Mirror.me(type).getField(prop.substring(0, dot)).getType();
			return getType(t, prop.substring(dot + 1));
		} else {
			Mirror me = Mirror.me(type);
			try {
				return me.getField(prop).getType();
			} catch (Throwable e) {
				return type.getMethod("get" + prop.substring(0, 1).toUpperCase() + prop.substring(1)).getReturnType();
			}
		}
	}

	public static Class reloadClass(String clsname) throws ClassNotFoundException {
		return getCocitClassLoader().reloadClass(clsname);
	}

	private static CocitClassLoader getCocitClassLoader() {
		// if (demsyClassLoader == null) {
		// demsyClassLoader = new DemsyClassLoader(getDefaultClassLoader());
		// }
		// return demsyClassLoader;
		return new CocitClassLoader(getDefaultClassLoader());
	}

	private static class CocitClassLoader extends ClassLoader {

		private CocitClassLoader(ClassLoader parent) {
			super(parent);
		}

		protected Class defineClass(String className, File file) throws IOException {
			FileInputStream fis = null;
			ByteArrayOutputStream baos = null;

			byte[] bytecodes = new byte[1024];
			int ln = 0;

			try {
				fis = new FileInputStream(file);
				baos = new ByteArrayOutputStream();

				while ((ln = fis.read(bytecodes)) > 0) {
					baos.write(bytecodes, 0, ln);
				}

				baos.flush();

				return defineClass(className, baos.toByteArray());
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (IOException e) {
					}
				}

				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}

		}

		protected Class defineClass(String className, byte[] bytecodes) {
			Class cls = defineClass(className, bytecodes, 0, bytecodes.length);
			this.resolveClass(cls);

			return cls;
		}

		protected Class reloadClass(String clsname) throws ClassNotFoundException {
			try {
				return defineClass(clsname, new File(Cocit.me().getConfig().getClassDir() + File.separator + clsname.replace(".", File.separator) + ".class"));
			} catch (IOException e) {
				throw new ClassNotFoundException(e.getMessage());
			}
			// return super.loadClass(clsname);
		}

	}

}
