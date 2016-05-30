/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/
package us.mn.state.health.lims.common.util.resources;

import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;
import us.mn.state.health.lims.common.log.LogEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Diane Benz Oct 29, 2005
 * 
 */
public class ResourceLocator {
	private static ResourceLocator me; // Holder for Singleton

	// Holds the ApplicationResources.properties information
    private MessageResources messageResources = null;

    private MessageResources languageResources = null;

	// Name of file that contains resource mappings. This class loads this into
	// the propertyFilePairs object
	private final String RESOURCE_PROPERTIES = "Resources.properties";

	public static final String AJAX_PROPERTIES = "AjaxResources.properties";
	
	public static final String REPORTS_PROPERTIES = "Reports.properties";
	
	//bugzilla 1550
	public static final String XMIT_PROPERTIES = "Transmission.properties";


	
	// RESOURCES_PROPERTIES is read into this Properties object
	private Properties propertyFilePairs;

	// Keep class from being instantiated.
	private ResourceLocator() throws RuntimeException {
		/*
		 * Finding the property file which holds the references to property file
		 * definitions. This file contains a=b value pairs that
		 * getResourceAsInputStream() needs to work correctly.
		 */
		InputStream propertyStream = this.getClass().getResourceAsStream(
				RESOURCE_PROPERTIES);
        if (propertyStream == null) {
			// Property file not found, throw exception
			throw new RuntimeException("Resources Property file "
					+ RESOURCE_PROPERTIES + " was not found.");
		}
		// Now load a java.util.Properties object with the properties
		propertyFilePairs = new Properties();
		try {
			propertyFilePairs.load(propertyStream);
		} catch (IOException e) {
            //bugzilla 2154
			LogEvent.logError("ResourceLocator","ResourceLocator()",e.toString());   		
			throw new RuntimeException(e);
		} finally {
			if (null != propertyStream) {
				try {
					propertyStream.close();
					propertyStream = null;
				} catch (Exception e) {
                    //bugzilla 2154
			        LogEvent.logError("ResourceLocator","ResourceLocator()",e.toString());
				}
			}
		}

		// Initialize the message resources object
		initializeMessageResources();
        // Initialize the Language resources object
        initializeLanguageResources();
	}

	/**
	 * Return the instance of this singleton
	 */
	public static ResourceLocator getInstance() throws RuntimeException {
		if (me == null) {
			synchronized (ResourceLocator.class) {
				if (me == null) {
					me = new ResourceLocator();
				}
			}
		}
		return me;
	}

	/**
	 * Get passed in a resource name, and return an InputStream to the property
	 * file corresponding to the resource name.
	 * 
	 * @input String Resource Name
	 * @return InputStream Stream to resource
	 * @exception Exception
	 *                Cannot find property file for resource
	 */
	public InputStream getResourceAsInputStream(String pResourceName)
			throws RuntimeException {
		String resourceFileName = getResourceFileName(pResourceName);
		InputStream resourceStream = getNamedResourceAsInputStream(resourceFileName);
		return resourceStream;
	}

	public InputStream getNamedResourceAsInputStream(String resourceFileName)
			throws RuntimeException {
		InputStream resourceStream = this.getClass().getResourceAsStream(
				resourceFileName);
		if (resourceStream == null) {
			throw new RuntimeException("Could not find resource "
					+ resourceFileName);
		}
		return resourceStream;
	}

	public String getResourceFileName(String pResourceName)
			throws RuntimeException {
		String resourceFileName = propertyFilePairs.getProperty(pResourceName);
		if (resourceFileName == null) {
			throw new RuntimeException("Resource mapping for the file name "
					+ pResourceName + " could not be found.");
		}
		return resourceFileName;
	}

	/*
	 */
	private void initializeMessageResources() {
		MessageResourcesConfig config = new MessageResourcesConfig();
		config.setParameter(propertyFilePairs
				.getProperty("ApplicationResources.classpath"));
		String factory = config.getFactory();
		MessageResourcesFactory.setFactoryClass(factory);
		MessageResourcesFactory factoryObject = MessageResourcesFactory
				.createFactory();

		messageResources = factoryObject.createResources(config.getParameter());
		messageResources.setReturnNull(config.getNull());
	}

	private void initializeLanguageResources(){
        MessageResourcesConfig config = new MessageResourcesConfig();

        config.setParameter(propertyFilePairs
                .getProperty("LanguageResources.classpath"));
        String factory = config.getFactory();
        MessageResourcesFactory.setFactoryClass(factory);
        MessageResourcesFactory factoryObject = MessageResourcesFactory
                .createFactory();

        languageResources = factoryObject.createResources(config.getParameter());
        languageResources.setReturnNull(config.getNull());
	}

	/**
	 * Returns the messageResources.
	 * 
	 * @return MessageResources
	 */
	public MessageResources getMessageResources() {
		return messageResources;
	}

    /**
     * Returns the languageResources.
     *
     * @return LanguageResources
     */

	/**
	 * Returns the path for a resource filename.
	 * 
	 * @return FilePath string
	 */
	public String getFilePath(String fileName) {
		return this.getClass().getResource(fileName).getPath();
	}
}
