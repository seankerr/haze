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

package io.haze.transport.udp.encoder;

import io.haze.transport.udp.UDPEncoder;
import io.haze.transport.udp.UDPTransportClient;

import io.netty.channel.socket.DatagramPacket;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class UnreliableUDPEncoder extends UDPEncoder {

    /**
     * Handle an ACK {@link DatagramPacket}.
     *
     * @param client The transport client.
     * @param packet The ack datagram packet.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void handleAckPacket (UDPTransportClient client, DatagramPacket packet)
    throws Exception {
        // unreliable encoding doesn't expect ack packets
    }

    /**
     * Handle an outgoing {@link DatagramPacket}.
     *
     * @param client   The transport client.
     * @param packetId The packet id.
     * @param packet   The datagram packet.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void handlePacket (UDPTransportClient client, short packetId, DatagramPacket packet)
    throws Exception {
        client.write(packet);
    }

}