package com.bikroy.framework.globals;

import java.util.Hashtable;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.xml.DOMConfigurator;

import com.bikroy.framework.utilities.textutilities.TextUtilities;

/**
 * This Class is wrapper class around Java LOG4J logging framework. All logging
 * in Automation framework would be done through this Class.
 * 
 * @author Aishwarya Dwivedi
 * @since 1.0
 */
public final class Logger {
	/**
	 * Load LOG4j configuration file.
	 * 
	 * @param File
	 */
	public static synchronized final void LoadConfiguration(final String File) {
		try {
			DOMConfigurator.configure(File);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * logger to hold logger class instance.
	 */
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("AUTOMATION");

	/**
	 * Default logging configuration file name
	 */
	private static String DefaultLog4JConfiguration = "./src/main/resources/log4j-agent.xml";

	/**
	 * Collection of Logger
	 */
	private static Hashtable<String, org.apache.log4j.Logger> objHashtable = new Hashtable<String, org.apache.log4j.Logger>();

	/**
	 * Log exception to debug message
	 * 
	 * @param e
	 */
	public final void debug(final Exception e) {
		this.debug("Exception Occured: (" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + e.getMessage());
		for (Object msg : e.getStackTrace()) {
			this.debug(msg.toString());
		}
	}

	/**
	 * Log debug message
	 * 
	 * @param Message
	 */
	public final void debug(final Object Message) {
		if (GlobalConsts.IsVerboseLogging()) {
			this.logger.debug("(" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + Message);
		}
	}

	/**
	 * Prints Entry of method as an information
	 */
	public final void enterMethod() {
		this.logger.info("ENTER --> (" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ")");
	}

	/**
	 * Prints Entry of method as an information
	 */
	public final void enterMethod(final String Message) {
		this.logger.info("ENTER --> (" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") --> " + Message);
	}

	/**
	 * Log exception to error message
	 * 
	 * @param e
	 */
	public final void error(final Exception e) {
		this.error("Exception Occured: (" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + e.getMessage());
		for (Object msg : e.getStackTrace()) {
			this.error(msg.toString());
		}
	}

	/**
	 * Log error message
	 * 
	 * @param Message
	 */
	public final void error(final Object Message) {
		this.logger.error("(" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + Message);
	}

	/**
	 * Prints Exit of method as an information
	 */
	public final void exitMethod() {
		this.logger.info("EXIT --> (" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ")");
	}

	/**
	 * Prints Exit of method as an information
	 */
	public final void exitMethod(final String Message) {
		this.logger.info("EXIT --> (" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") --> " + Message);
	}

	/**
	 * Log exception to fatal message
	 * 
	 * @param e
	 */
	public final void fatal(final Exception e) {
		this.fatal("Exception Occured: (" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + e.getMessage());
		for (Object msg : e.getStackTrace()) {
			this.fatal(msg.toString());
		}
	}

	/**
	 * Log fatal message
	 * 
	 * @param Message
	 */
	public final void fatal(final Object Message) {
		this.logger.fatal("(" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + Message);
	}

	/**
	 * Log exception to fullDebug message
	 * 
	 * @param e
	 */
	public final void fullDebug(final Exception e) {
		this.fullDebug("Exception Occured: (" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + e.getMessage());
		for (Object msg : e.getStackTrace()) {
			this.fullDebug(msg.toString());
		}
	}

	/**
	 * Log fullDebug message
	 * 
	 * @param Message
	 */
	public final void fullDebug(final Object Message) {
		if (GlobalConsts.IsVerboseLogging()) {
			this.logger.debug("(" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + Message);
		}
	}

	/**
	 * @return the defaultLog4JConfiguration
	 */
	public final String getDefaultLog4JConfiguration() {
		return Logger.DefaultLog4JConfiguration;
	}

	/**
	 * Return debug level.
	 * 
	 * @return ALL,DEBUG,ERROR,FATAL,INFO,OFF,TRACE,TRACE_INTand WARN
	 */
	public final Level getLevel() {
		return this.logger.getLevel();
	}

	/**
	 * Method to get Logger instance.
	 * 
	 * @param clazz
	 *            Class name
	 * @return Return Logger instance
	 */
	public Logger getLogger(final Class<?> clazz) {
		return this.getLogger(clazz.getName().toString());
	}

	/**
	 * Method to get Logger instance
	 * 
	 * @param strName
	 *            name in string format
	 * @return Return Logger instance
	 */
	public Logger getLogger(final String strName) {
		if (Logger.objHashtable.containsKey(strName)) {
			this.logger = Logger.objHashtable.get(strName);
		} else {
			Logger.LoadConfiguration(Logger.DefaultLog4JConfiguration);
			this.logger = org.apache.log4j.Logger.getLogger(strName);
			Logger.objHashtable.put(strName, this.logger);
		}
		return this;
	}

	/**
	 * Method to get Logger name
	 * 
	 * @return Logger Name
	 */
	public final String getName() {
		return this.logger.getName();
	}

	/**
	 * Method to get Logger parent
	 * 
	 */
	public final Category getParent() {
		return this.logger.getParent();
	}

	/**
	 * Log info message
	 * 
	 * @param Message
	 */
	public final void info(final Object Message) {
		this.logger.info("(" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + Message);
	}

	/**
	 * 
	 * @param objAppender
	 * 
	 * @return <b>true :-> </b>Logger is attached<br>
	 *         <b>false:-> </b>Not Attached
	 */
	public final boolean isAttached(final Appender objAppender) {
		return this.logger.isAttached(objAppender);
	}

	/**
	 * @return Return Debug ON/OFF status
	 */
	public final boolean isDebugEnabled() {
		return this.logger.isDebugEnabled();
	}

	/**
	 * Log notification message as info message - Surrounds message with special
	 * characters headings
	 * 
	 * @param Message
	 */
	public final void note(final Object Message, final int level) {
		switch (level) {
		case 1:
			this.logger.info(TextUtilities.repeat("-", 50));
			break;
		case 2:
			this.logger.info(TextUtilities.repeat("#", 50));
			this.logger.info(TextUtilities.repeat("#", 50));
			break;
		}

		this.logger.info("(" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + Message);

		switch (level) {
		case 1:
			this.logger.info(TextUtilities.repeat("-", 50));
			break;
		case 2:
			this.logger.info(TextUtilities.repeat("#", 50));
			this.logger.info(TextUtilities.repeat("#", 50));
			break;
		}
	}

	/**
	 * Remove all logging destination.
	 */
	public final void removeAllAppenders() {
		this.logger.removeAllAppenders();
	}

	/**
	 * Remove objAppender destination
	 * 
	 * @param objAppender
	 */
	public final void removeAppender(final Appender objAppender) {
		this.logger.removeAppender(objAppender);
	}

	/**
	 * Remove destination by name
	 * 
	 * @param Name
	 */
	public final void removeAppender(final String Name) {
		this.logger.removeAppender(Name);
	}

	/**
	 * @param defaultLog4JConfiguration
	 *            the defaultLog4JConfiguration to set
	 */
	public void setDefaultLog4JConfiguration(final String defaultLog4JConfiguration) {
		Logger.DefaultLog4JConfiguration = defaultLog4JConfiguration;
	}

	/**
	 * Set log level
	 * 
	 * @param objLevel
	 *            Possible values
	 *            ALL,DEBUG,ERROR,FATAL,INFO,OFF,TRACE,TRACE_INTand WARN
	 */
	public final void setLevel(final Level objLevel) {
		this.logger.setLevel(objLevel);
	}

	/**
	 * Log exception to warn message
	 * 
	 * @param e
	 */
	public final void warn(final Exception e) {
		this.warn("Exception Occured: (" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + e.getMessage());
		for (Object msg : e.getStackTrace()) {
			this.warn(msg.toString());
		}
	}

	/**
	 * Log warn message
	 * 
	 * @param Message
	 */
	public final void warn(final Object Message) {
		this.logger.warn("(" + Thread.currentThread().getStackTrace()[2].getMethodName().trim() + ") -> " + Message);
	}
}
