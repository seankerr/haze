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

package io.haze.node;

import io.haze.core.Application;
import io.haze.core.FactoryException;
import io.haze.core.Service;
import io.haze.core.ServiceFactory;
import io.haze.core.ServiceFactoryContext;
import io.haze.node.system.SystemNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class NodeFactory extends ServiceFactory {

    /** The logger. */
    private Logger logger = LoggerFactory.getLogger(NodeFactory.class);

    /** The current node id. */
    private int nodeId = -1;

    /**
     * Create all {@link Node} instances for this {@link NodeFactory}.
     *
     * @param application The application.
     */
    protected void createServices (Application application) {
        // register system node before any other nodes, and without requiring it in the configuration
        try {
            // system node has no configuration namespace, signified by the null parameter
            ServiceFactoryContext context = new ServiceFactoryContext(application, null, "system-node");
            Node                  service = new SystemNode();

            service.setServiceFactoryContext(context);
            service.setNodeId(getNextNodeId());
            service.setServiceId(getNextServiceId());
            registerService(application, service);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        super.createServices(application);
    }

    /**
     * Retrieve the configuration element.
     */
    @Override
    public String getConfigurationElement () {
        return "nodes";
    }

    /**
     * Retrieve the configuration sub-element.
     */
    @Override
    public String getConfigurationSubElement () {
        return "node";
    }

    /**
     * Increase the node id and retrieve it.
     */
    int getNextNodeId () {
        return ++nodeId;
    }

    /**
     * Retrieve the {@link Service} that will be created.
     */
    @Override
    public Class<? extends Service> getServiceClass () {
        return Node.class;
    }

    /**
     * Create a new {@link Node} instance.
     *
     * @param context The service factorycontext.
     *
     * @throws FactoryException If the node cannot be created.
     */
    @Override
    public Service newInstance (ServiceFactoryContext context)
    throws FactoryException {
        Node node = getClassElementInstance(context.getApplication().getConfiguration(), context.getNamespace(),
                                            Node.class);

        node.setServiceFactoryContext(context);
        node.setNodeId(getNextNodeId());

        return node;
    }

}