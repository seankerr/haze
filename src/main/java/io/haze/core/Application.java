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

import io.haze.node.Node;
import io.haze.node.NodeFactory;
import io.haze.transport.TransportServerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class Application {

    /** The configuration. */
    private XMLConfiguration configuration;

    /** The list of service factories. */
    private static List<ServiceFactory> factories = new ArrayList();

    /** Indicates whether or not the application is running. */
    private Boolean isRunning = false;

    /** The logger. */
    private Logger logger = LoggerFactory.getLogger(Application.class);

    /** The list of nodes. */
    private List<Node> nodes = new ArrayList();

    /** The list of services. */
    private List<Service> services = new ArrayList();

    /**
     * Create a new {@link Application} instance.
     *
     * @param configuration The configuration.
     */
    private Application (XMLConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Retrieve the {@link XMLConfiguration}.
     */
    public XMLConfiguration getConfiguration () {
        return configuration;
    }

    /**
     * Retrieve a {@link Node}.
     *
     * @param nodeId The node id.
     */
    public Node getNode (int nodeId) {
        return nodes.get(nodeId);
    }

    /**
     * Retrieve the {@link Node} collection.
     */
    public Collection<Node> getNodes () {
        return Collections.unmodifiableList(nodes);
    }

    /**
     * Retrieve the {@link Node} list count.
     */
    public int getNodeCount () {
        return nodes.size();
    }

    /**
     * Retrieve a {@link Service}.
     *
     * @param name The service name.
     */
    public Service getService (String name) {
        for (Service service : services) {
            if (service.getName().equals(name)) {
                return service;
            }
        }

        return null;
    }

    /**
     * Retrieve an array of {@link Service} names.
     */
    public Collection<String> getServiceNames () {
        List<String> names = new ArrayList();

        for (Service service : services) {
            names.add(service.getName());
        }

        return names;
    }

    /**
     * Retrieve the {@link Service} collection.
     */
    public Collection<Service> getServices () {
        return Collections.unmodifiableList(services);
    }

    /**
     * Retrieve a collection of services whose class matches <code>T</code>.
     *
     * @param clazz The class type.
     */
    public <T> Collection<T> getServices (Class<T> clazz) {
        List<T> matches = new ArrayList();

        for (Service service : services) {
            if (clazz.isInstance(service)) {
                matches.add((T) service);
            }
        }

        return matches;
    }

    /**
     * Indicates whether or not a {@link Service} has been registered.
     *
     * @param name The service name.
     */
    public boolean hasService (String name) {
        for (Service service : services) {
            if (service.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Indicates whether or not this {@link Application} is running.
     */
    public boolean isRunning () {
        synchronized (isRunning) {
            return isRunning;
        }
    }

    /**
     * Load a configuration file.
     *
     * @param path The configuration file path relative to the application directory.
     *
     * @throws ConfigurationException If the configuration file could not be loaded.
     * @throws MalformedURLException  If the configuration file URL is invalid.
     */
    public static XMLConfiguration loadConfiguration (String path)
    throws ConfigurationException, MalformedURLException {
        return new XMLConfiguration(new File(path).toURL());
    }

    /**
     * The application entry point. A configuration file path may be specified, otherwise "conf/haze.xml" will be used.
     *
     * @param args The command-line arguments.
     *
     * @throws Exception If the configuration could not be loaded.
     */
    public static void main (String[] args)
    throws Exception {
        new Application(loadConfiguration(args.length > 0 ? args[0] : "conf/haze.xml")).start();
    }

    /**
     * Register a {@link Service}.
     *
     * @param service The service.
     *
     * @throws ServiceException If the name is already registered.
     */
    void registerService (Service service)
    throws ServiceException {
        if (!hasService(service.getName())) {
            services.add(service);

            return;
        }

        throw new ServiceException(String.format("Service %s is already registered", service.getName()));
    }

    /**
     * Register a {@link ServiceFactory}. Once this {@link Application} starts, factories can no longer be registered.
     *
     * @param factory The service factory.
     */
    public static void registerServiceFactory (ServiceFactory factory) {
        factories.add(factory);
    }

    /**
     * Start this {@link Application}.
     */
    private void start () {
        isRunning = true;

        for (ServiceFactory factory : factories) {
            factory.createServices(this);
        }

        if (services.size() > 0) {
            // build list of nodes
            for (Node node : getServices(Node.class)) {
                nodes.add(node);
            }

            // reverse the service order, so they can be started descendingly
            Collections.reverse(services);

            // start services
            logger.info("Starting services:");

            for (Service service : services) {
                service.initialize();
                service.start();

                logger.info(String.format("  * %s", service.getName()));
            }

            // put the service order back
            Collections.reverse(services);
        }

        // register shutdown hook
        final Application app = this;

        Runtime.getRuntime().addShutdownHook(new Thread () {
            public void run () {
                if (app.isRunning()) {
                    app.stop();

                    return;
                }

                // forcefully exit
                System.exit(1);
            }
        });
    }

    /**
     * Stop this {@link Application}.
     */
    public void stop () {
        if (!isRunning()) {
            return;
        }

        synchronized (isRunning) {
            isRunning = false;
        }

        // stop services
        logger.info("Stopping services:");

        for (Service service : services) {
            try {
                service.stop();
                service.getThread().interrupt();
                service.getThread().join();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                logger.info(String.format("  * %s", service.getName()));
            }
        }
    }

    /**
     * Retrieve the string representation of this {@link Application}.
     */
    public String toString () {
        return String.format("<%s>", getClass().getSimpleName());
    }

    static {
        registerServiceFactory(new TransportServerFactory());
        registerServiceFactory(new NodeFactory());
    }

}