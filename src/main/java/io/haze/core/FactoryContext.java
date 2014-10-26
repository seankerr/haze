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

import io.haze.transport.TransportServer;

import org.apache.commons.configuration.XMLConfiguration;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class FactoryContext {

    /** The application. */
    private Application application;

    /** The configuration namespace. */
    private String namespace;

    /** The transport server. */
    private TransportServer server;

    /**
     * Create a new {@link FactoryContext} instance.
     *
     * @param application The application.
     * @param namespace   The configuration namespace.
     */
    public FactoryContext (Application application, String namespace) {
        this(application, namespace, null);
    }

    /**
     * Create a new {@link FactoryContext} instance.
     *
     * @param application The application.
     * @param namespace   The configuration namespace.
     * @param server      The transport server.
     */
    public FactoryContext (Application application, String namespace, TransportServer server) {
        this.application = application;
        this.namespace   = namespace;
        this.server      = server;
    }

    /**
     * Retrieve the {@link Application}.
     */
    public Application getApplication () {
        return application;
    }

    /**
     * Retrieve the {@link XMLConfiguration}.
     */
    public XMLConfiguration getConfiguration () {
        return application.getConfiguration();
    }

    /**
     * Retrieve the configuration namespace.
     */
    public String getNamespace () {
        return namespace;
    }

    /**
     * Retrieve the {@link TransportServer}.
     */
    public TransportServer getTransportServer () {
        return server;
    }

    /**
     * Retrieve the string representation of this {@link FactoryContext}.
     */
    public String toString () {
        return String.format("<%s:%s>", getClass().getSimpleName(), getNamespace());
    }

}