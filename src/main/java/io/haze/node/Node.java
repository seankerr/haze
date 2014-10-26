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

import io.haze.core.Service;
import io.haze.message.Message;
import io.haze.message.MessageContext;
import io.haze.message.reader.MessageReader;
import io.haze.transport.TransportClient;
import io.haze.transport.TransportServer;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public abstract class Node<T> extends Service {

    /** The map of message classes. */
    private Map<T,Class<? extends Message>> messageClasses = new HashMap();

    /** The node id. */
    private int nodeId;

    /**
     * Build a {@link Message} from a {@link MessageReader}.
     *
     * @param reader The message reader.
     *
     * @throws Exception If an error has occurred.
     */
    public abstract Message buildMessage (MessageReader reader)
    throws Exception;

    /**
     * Deserialize the {@link MessageReader} into a {@link Message} whose type is stored internally.
     *
     * @param reader The message reader.
     * @param key    The key.
     *
     * @throws IllegalAccessException If the constructor cannot be called.
     * @throws InstantiationException If the class instance could not be created.
     */
    protected Message deserializeMessage (MessageReader reader, T key)
    throws IllegalAccessException, InstantiationException {
        Class<? extends Message> clazz = messageClasses.get(key);

        if (clazz != null) {
            return clazz.newInstance().deserialize(reader);
        }

        return null;
    }

    /**
     * Retrieve the node id.
     */
    public int getNodeId () {
        return nodeId;
    }

    /**
     * Handle a {@link io.haze.message.MessageFormat} change.
     *
     * @param client The transport client.
     */
    public void handleMessageFormat (TransportClient client) {
    }

    /**
     * Handle a {@link TransportClient} connection.
     *
     * @param client The transport client.
     */
    public void handleConnect (TransportClient client) {
    }

    /**
     * Handle a {@link TransportClient} disconnection.
     *
     * @param client The transport client.
     */
    public void handleDisconnect (TransportClient client) {
    }

    /**
     * Handle a {@link TransportServer} shutdown.
     *
     * @param server The transport server.
     */
    public void handleTransportShutdown (TransportServer server) {
    }

    /**
     * Receive a {@link MessageContext}.
     *
     * @param context The message context.
     *
     * @throws Exception If an error has occurred.
     */
    public abstract void receiveMessage (MessageContext context)
    throws Exception;

    /**
     * Register a {@link Message} class.
     *
     * @param key   The key.
     * @param clazz The message class.
     */
    public void registerMessageClass (T key, Class<? extends Message> clazz) {
        messageClasses.put(key, clazz);
    }

    /**
     * Run this {@link Node}.
     */
    @Override
    public void run () {
        block(1000);
    }

    /**
     * Set the node id.
     *
     * @param nodeId The node id.
     */
    void setNodeId (int nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * Retrieve the string representation of this {@link Node}.
     */
    @Override
    public String toString () {
        return String.format("<%s:%s:%s>", getClass().getSimpleName(), getServiceFactoryContext().getName(),
                             getNodeId());
    }

}