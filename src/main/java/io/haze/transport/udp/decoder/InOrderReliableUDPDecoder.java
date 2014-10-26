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

package io.haze.transport.udp.decoder;

import io.haze.message.BufferMessage;
import io.haze.message.MessageContext;
import io.haze.transport.udp.UDPTransportClient;

import io.netty.channel.socket.DatagramPacket;

import java.util.List;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class InOrderReliableUDPDecoder extends ReliableUDPDecoder {

    /**
     * Handle an incoming {@link DatagramPacket}.
     *
     * @param client   The transport client.
     * @param packet   The datagram packet.
     * @param messages The output messages.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void handlePacket (UDPTransportClient client, DatagramPacket packet, List<Object> messages)
    throws Exception {
        messages.add(new MessageContext(client, new BufferMessage(packet.content().retain())));
    }

}