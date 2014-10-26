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

package io.haze.transport.tcp;

import io.haze.node.system.message.DisconnectResponse;
import io.haze.transport.TransportClient;

import io.netty.channel.socket.SocketChannel;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class TCPTransportClient extends TransportClient<SocketChannel,TCPTransportServer> {

    /**
     * Disconnect this {@link TCPTransportClient}.
     *
     * @param message The disconnect message.
     */
    @Override
    public TCPTransportClient disconnect (String message) {
        getTransportServer().getChannelPipelineInitializer()
                            .getMessageDispatcher()
                            .writeMessage(this, new DisconnectResponse(message));

        // close the channel so the unregister event will be executed and unregister the client
        getChannel().close();

        super.disconnect(message);

        return this;
    }

}