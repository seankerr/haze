// +-------------------------------------------------------------------------------------------------------------------+
// | Copyright 2013 Sean Kerr                                                                                          |
// |                                                                                                                   |
// | Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance    |
// | with the License. You may obtain a copy of the License at                                                         |
// |                                                                                                                   |
// |   http://www.apache.org/licenses/LICENSE-2.0                                                                      |
// |                                                                                                                   |
// | Unless required by applicable law or agreed to in writing, software distributed under the License is distributed  |
// | on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for |
// | the specific language governing permissions and limitations under the License.                                    |
// +-------------------------------------------------------------------------------------------------------------------+

package io.haze.core;

import org.apache.commons.configuration.XMLConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Element;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public abstract class ServiceFactory {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(ServiceFactory.class);

    /** The current service id. */
    private static int serviceId;

    /**
     * Create all {@link Service} instances for this {@link ServiceFactory}.
     *
     * @param application The application.
     */
    protected void createServices (Application application) {
        XMLConfiguration configuration = application.getConfiguration();
        int              count         = 0;
        String           element       = getConfigurationElement();
        String           subElement    = getConfigurationSubElement();

        try {
            count = ((Element) configuration.getDocument()
                                            .getElementsByTagName(element).item(0))
                                            .getElementsByTagName(subElement)
                                            .getLength();
        } catch (Exception e) {
            logger.error(String.format("You must specify at least one %s", subElement));

            return;
        }

        for (int i = 0; i < count; i++) {
            String namespace = String.format("%s.%s(%d)", element, subElement, i);
            String name      = configuration.getString(String.format("%s[@name]", namespace), "").trim();

            if (name.length() == 0) {
                logger.error(String.format("<%s> is missing name attribute", subElement));

                continue;
            }

            try {
                ServiceFactoryContext context = new ServiceFactoryContext(application, namespace, name);
                Service               service = newInstance(context);

                service.setServiceId(getNextServiceId());
                application.registerService(service);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

    }

    /**
     * Retrieve a class from the configuration file.
     *
     * @param configuration The configuration.
     * @param namespace     The configuration namespace.
     * @param clazz         The expected class type.
     *
     * @throws FactoryException If the class is invalid, the class cannot be found, or the class cannot be cast to the
     *                          expected type.
     */
    protected <T> Class<T> getClassElement (XMLConfiguration configuration, String namespace, Class<T> clazz)
    throws FactoryException {
        return getClassElement(configuration, namespace, clazz, "");
    }

    /**
     * Retrieve a class from the configuration file.
     *
     * @param configuration The configuration.
     * @param namespace     The configuration namespace.
     * @param clazz         The expected class type.
     * @param defaultValue  The default value.
     *
     * @throws FactoryException If the class is invalid, the class cannot be found, or the class cannot be cast to the
     *                          expected type.
     */
    protected <T> Class<T> getClassElement (XMLConfiguration configuration, String namespace, Class<T> clazz,
                                            String defaultValue)
    throws FactoryException {
        String value = configuration.getString(String.format("%s[@class]", namespace), defaultValue).trim();

        if (value.length() == 0) {
            throw new FactoryException(String.format("%s is missing class attribute", namespace));
        }

        try {
            return (Class<T>) Class.forName(value);
        } catch (ClassCastException e) {
            throw new FactoryException(String.format("%s class '%s' cannot be cast to %s", namespace, value,
                                                     clazz.getName()));
        } catch (ClassNotFoundException e) {
            throw new FactoryException(String.format("%s class '%s' cannot be found", namespace, value));
        }
    }

    /**
     * Retrieve a class instance from the configuration file.
     *
     * @param configuration The configuration.
     * @param namespace     The configuration namespace.
     * @param clazz         The expected class type.
     *
     * @throws FactoryException If the class is invalid, the class cannot be found, the class cannot be cast to the
     *                          expected type, the constructor could not be called, or the constructor threw an
     *                          exception.
     */
    protected <T> T getClassElementInstance (XMLConfiguration configuration, String namespace, Class<T> clazz)
    throws FactoryException {
        return getClassElementInstance(configuration, namespace, clazz, "");
    }

    /**
     * Retrieve a class instance from the configuration file.
     *
     * @param configuration The configuration.
     * @param namespace     The configuration namespace.
     * @param clazz         The expected class type.
     * @param defaultValue  The default value.
     *
     * @throws FactoryException If the class is invalid, the class cannot be found, the class cannot be cast to the
     *                          expected type, the constructor could not be called, or the constructor threw an
     *                          exception.
     */
    protected <T> T getClassElementInstance (XMLConfiguration configuration, String namespace, Class<T> clazz,
                                             String defaultValue)
    throws FactoryException {
        Class<T> result = null;

        try {
            result = getClassElement(configuration, namespace, clazz, defaultValue);

            return result.newInstance();
        } catch (IllegalAccessException e) {
            throw new FactoryException(String.format("%s class '%s' constructor is inaccessible",
                                                     namespace, result.getName()));
        } catch (InstantiationException e) {
            if (e.getMessage() == null) {
                throw new FactoryException(String.format("%s class '%s' has no suitable constructor, " +
                                                         "or is an abstract class", namespace, result.getName()));
            }

            throw new FactoryException(String.format("%s class '%s' constructor threw an exception: %s",
                                                     namespace, result.getName(), e.getMessage()));
        }
    }

    /**
     * Retrieve the configuration element.
     */
    public abstract String getConfigurationElement ();

    /**
     * Retrieve the configuration sub-element.
     */
    public abstract String getConfigurationSubElement ();

    /**
     * Increase the server id and retrieve it.
     */
    protected int getNextServiceId () {
        return ++serviceId;
    }

    /**
     * Retrieve the {@link Service} that will be created.
     */
    public abstract Class<? extends Service> getServiceClass ();

    /**
     * Create a new {@link Service} instance.
     *
     * @param context The service factory context.
     *
     * @throws FactoryException If the service cannot be created.
     */
    public abstract Service newInstance (ServiceFactoryContext context)
    throws FactoryException;

    /**
     * Register a {@link Service}.
     *
     * @param application The application.
     * @param service     The service.
     *
     * @throws ServiceException If the name is already registered.
     */
    protected void registerService (Application application, Service service)
    throws ServiceException {
        application.registerService(service);
    }

}