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

package io.haze.message;

import io.haze.transport.TransportClient;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class MessageContext {

    /** The message. */
    private Message message;

    /** The transport client. */
    private TransportClient client;

    /**
     * Create a new {@link MessageContext} instance.
     *
     * @param client  The transport client.
     * @param message The message.
     */
    public MessageContext (TransportClient client, Message message) {
        this.client  = client;
        this.message = message;
    }

    /**
     * Clone this {@link MessageContext}.
     */
    public MessageContext clone () {
        return new MessageContext(getTransportClient(), getMessage());
    }

    /**
     * Retrieve the {@link Message}.
     */
    public Message getMessage () {
        return message;
    }

    /**
     * Retrieve the {@link TransportClient}.
     */
    public TransportClient getTransportClient () {
        return client;
    }

    /**
     * Set the {@link Message}.
     *
     * @param message The message.
     */
    public MessageContext setMessage (Message message) {
        this.message = message;

        return this;
    }

    /**
     * Set the {@link TransportClient}.
     *
     * @param client The transport client.
     */
    public MessageContext setTransportClient (TransportClient client) {
        this.client = client;

        return this;
    }

}