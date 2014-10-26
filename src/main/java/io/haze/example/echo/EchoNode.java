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

package io.haze.example.echo;

import io.haze.message.Message;
import io.haze.message.MessageContext;
import io.haze.message.reader.MessageReader;
import io.haze.node.Node;
import io.haze.transport.TransportClient;
import io.haze.transport.TransportServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class EchoNode extends Node {

    /** The list of clients */
    private List<TransportClient> clients = Collections.synchronizedList(new ArrayList());

    /**
     * Build a {@link Message} from a {@link MessageReader}.
     *
     * @param reader The message reader.
     */
    @Override
    public Message buildMessage (MessageReader reader) {
        byte[] bytes = new byte[reader.readInt()];

        reader.readBytes(bytes);

        return new EchoMessage(new String(bytes));
    }

    /**
     * Handle a {@link TransportClient} connection.
     *
     * @param client The transport client.
     */
    @Override
    public void handleConnect (TransportClient client) {
        clients.add(client);
    }

    /**
     * Handle a {@link TransportClient} disconnection.
     *
     * @param client The transport client.
     */
    @Override
    public void handleDisconnect (TransportClient client) {
        clients.remove(client);
    }

    /**
     * Handle a {@link TransportServer} shutdown.
     *
     * @param server The transport server.
     */
    @Override
    public void handleTransportShutdown (TransportServer server) {
        synchronized (clients) {
            List<TransportClient> remove = new ArrayList();

            for (TransportClient client : clients) {
                if (client.getTransportServer() == server) {
                    remove.add(client);
                }
            }

            for (TransportClient client : remove) {
                clients.remove(client);
            }
        }
    }

    /**
     * Receive a {@link MessageContext}.
     *
     * @param context The message context.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void receiveMessage (MessageContext context)
    throws Exception {
        // repeat the message to all connected clients
        for (TransportClient _client : clients) {
            _client.write(context.clone().setTransportClient(_client));
        }
    }

}