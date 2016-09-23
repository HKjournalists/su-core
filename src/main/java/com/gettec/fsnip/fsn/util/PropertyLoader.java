package com.gettec.fsnip.fsn.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * <p>
 * There are several ways to get some/pkg/resource.properties programmatically from your Java code:
 * </p>
 * <q>
 * ClassLoader.getResourceAsStream ("some/pkg/resource.properties");
 * Class.getResourceAsStream ("/some/pkg/resource.properties");
 * ResourceBundle.getBundle ("some.pkg.resource");
 * </q>
 * <p>
 * The small behavioral difference among the built-in methods above can be a nuisance, especially if
 * some resource names were hardcoded. It makes sense to abstract away little things like whether
 * slashes or dots are used as name separators, etc.
 * </p>
 * 
 * @author Cyrus Cao
 */
public class PropertyLoader {

	private static final Logger LOG = Logger.getLogger(PropertyLoader.class);

	/**
	 * Whether loadProperties() throws an exception or merely returns null when it can't find the
	 * resource.
	 */
	private static final boolean THROW_ON_LOAD_FAILURE = true;

	/**
	 * Whether the resource is searched as a resource bundle or as a generic classpath resource.
	 */
	private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;

	/**
	 * Properties file suffix. Set to ".properties" by default.
	 */
	private static final String SUFFIX = ".properties";

	/**
	 * A convenience file loader that uses the current thread's context classloader.
	 */
	public static InputStream loadFile(final String clsPathName) {
		if (PropertyLoader.LOG.isDebugEnabled()) {
			PropertyLoader.LOG.debug("Reading file into inputStream: " + clsPathName);
		}

		InputStream result = null;
		ClassLoader clsLoader = Thread.currentThread().getContextClassLoader();
		if (clsLoader == null) {
			clsLoader = ClassLoader.getSystemClassLoader();
		}

		result = clsLoader.getResourceAsStream(clsPathName);

		return result;
	}

	/**
	 * A convenience overload of {@link #loadProperties(String, ClassLoader)} that uses the current
	 * thread's context classloader.
	 */
	public static Properties loadProperties(final String name) {
		if (PropertyLoader.LOG.isDebugEnabled()) {
			PropertyLoader.LOG.debug("Loading properties from file: " + name);
		}
		return PropertyLoader.loadProperties(name, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * Looks up a resource named 'resName' in the classpath. The resource must map to a file with
	 * .properties extention. The name is assumed to be absolute and can use either "/" or "." for
	 * package segment separation with an optional leading "/" and optional ".properties" suffix.
	 * Thus, the following names refer to the same resource:
	 * <q>
	 * some.pkg.Resource
	 * some.pkg.Resource.properties
	 * some/pkg/Resource
	 * some/pkg/Resource.properties
	 * /some/pkg/Resource
	 * /some/pkg/Resource.properties
	 * </q>
	 * 
	 * @param resName
	 *            classpath resource name [may not be null]
	 * @param clsLoader
	 *            classloader through which to load the resource [null is equivalent to the
	 *            application loader]
	 * @return resource converted to java.util.Properties [may be null if the resource was not found
	 *         and THROW_ON_LOAD_FAILURE is false]
	 */
	@SuppressWarnings("rawtypes")
	public static Properties loadProperties(final String resName, final ClassLoader clsLoader) {
		if (resName == null) {
			throw new IllegalArgumentException("null input: name");
		}

		String name = resName;
		if (name.startsWith("/")) {
			name = name.substring(1);
		}

		if (name.endsWith(PropertyLoader.SUFFIX)) {
			name = name.substring(0, name.length() - PropertyLoader.SUFFIX.length());
		}

		Properties result = null;

		InputStream in = null;
		ClassLoader loader = null;
		try {
			if (clsLoader == null) {
				loader = ClassLoader.getSystemClassLoader();
			} else {
				loader = clsLoader;
			}

			if (PropertyLoader.LOAD_AS_RESOURCE_BUNDLE) {
				name = name.replace('/', '.');
				// Throws MissingResourceException on lookup failures:
				final ResourceBundle rb = ResourceBundle.getBundle(name, Locale.getDefault(),
						loader);

				result = new Properties();
				for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
					final String key = (String) keys.nextElement();
					final String value = rb.getString(key);

					result.put(key, value);
				}
			} else {
				name = name.replace('.', '/');

				if (!name.endsWith(PropertyLoader.SUFFIX)) {
					name = name.concat(PropertyLoader.SUFFIX);
				}

				// Returns null on lookup failures:
				in = loader.getResourceAsStream(name);
				if (in != null) {
					result = new Properties();
					result.load(in);
				}
			}
		} catch (Exception e) {
			result = null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable ignore) {
					// Not much we could do
				}
			}
		}

		if (PropertyLoader.THROW_ON_LOAD_FAILURE && (result == null)) {
			throw new IllegalArgumentException("could not load ["
					+ name
					+ "]"
					+ " as "
					+ (PropertyLoader.LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle"
							: "a classloader resource"));
		}

		return result;
	}
} // End of class
