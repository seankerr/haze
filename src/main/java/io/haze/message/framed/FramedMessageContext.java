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

package io.haze.message.framed;

import io.haze.message.Message;
import io.haze.message.MessageContext;
import io.haze.transport.TransportClient;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class FramedMessageContext extends MessageContext {

    /** The node id. */
    private int nodeId;

    /**
     * Create a new {@link FramedMessageContext} instance.
     *
     * @param client  The transport client.
     * @param message The message.
     * @param nodeId  The node id.
     */
    public FramedMessageContext (TransportClient client, Message message, int nodeId) {
        super(client, message);

        this.nodeId = nodeId;
    }

    /**
     * Clone this {@link FramedMessageContext}.
     */
    @Override
    public FramedMessageContext clone () {
        return new FramedMessageContext(getTransportClient(), getMessage(), getNodeId());
    }

    /**
     * Retrieve the node id.
     */
    public int getNodeId () {
        return nodeId;
    }

    /**
     * Retrieve the string representation of this {@link FramedMessageContext}.
     */
    @Override
    public String toString () {
        return String.format("<%s:%d:'%s'>", getClass().getSimpleName(), getNodeId(), getMessage());
    }

}