package org.cocome.tradingsystem.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.log4j.Logger;

@ApplicationScoped
@Startup
public class Configuration {

	private Properties configData;

	private static final Logger log = Logger.getLogger(Configuration.class);

	@PostConstruct
	public void fetchConfiguration() {
		log.debug("Initialising properties...");
		final String fileName = "Configuration.properties";
		this.configData = Configuration.loadPropertiesFromClasspath(fileName);
	}

	/**
	 * Load properties file from classpath with Java 7 :-)
	 *
	 * @param fileName
	 * @return properties
	 */
	public static Properties loadPropertiesFromClasspath(final String fileName) {
		final Properties props = new Properties();
		try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
			if (in != null) {
				log.debug("Loading properties from file...");
				props.load(in);
				log.debug("Got properties: " + props.toString());
			}
		} catch (final IOException ioe) {
			log.error("Can't load properties from file.", ioe);
		}
		return props;
	}

	@Produces
	public String getString(final InjectionPoint point) {
		final String propertyPath = point.getMember().getDeclaringClass().getPackage().getName();
		final String propertyName = point.getMember().getName();

		final String configPropertyPath = propertyPath + "." + propertyName;

		String propertyValue = this.configData.getProperty(configPropertyPath);
		log.debug("Retrieved property " + configPropertyPath + " with value " + propertyValue);

		// Fall back to only the property name in case there is no property with
		// the given package
		if ((propertyValue == null) || propertyValue.isEmpty()) {
			propertyValue = this.configData.getProperty(propertyName);
			log.debug("Retrieved property " + propertyName + " with value " + propertyValue);
		}

		return (propertyValue == null) ? "" : propertyValue;
	}

	@Produces
	public long getLong(final InjectionPoint point) {
		final String propertyPath = point.getMember().getDeclaringClass().getPackage().getName();
		final String propertyName = point.getMember().getName();

		final String configPropertyPath = propertyPath + "." + propertyName;

		String propertyValue = this.configData.getProperty(configPropertyPath);
		log.debug("Retrieved property " + configPropertyPath + " with value " + propertyValue);

		// Fall back to only the property name in case there is no property with
		// the given package
		if ((propertyValue == null) || propertyValue.isEmpty()) {
			propertyValue = this.configData.getProperty(propertyName);
			log.debug("Retrieved property " + propertyName + " with value " + propertyValue);
		}

		return ((propertyValue == null) || propertyValue.isEmpty()) ? Long.MIN_VALUE : Long.parseLong(propertyValue);
	}
}
