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

package io.haze.transport.udp;

import io.haze.transport.udp.UDPEncoder;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
@Sharable
public abstract class UDPDecoder extends MessageToMessageDecoder<DatagramPacket> {

    /** The ACK bit. */
    private static final byte ACK_BIT = 0x01;

    /** The udp encoder. */
    private UDPEncoder encoder;

    /** The transport server. */
    private UDPTransportServer server;

    /**
     * Decode a {@link DatagramPacket} into a {@link MessageContext}.
     *
     * @param context  The channel handler context.
     * @param packet   The datagram packet.
     * @param messages The output messages.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void decode (ChannelHandlerContext context, DatagramPacket packet, List<Object> messages)
    throws Exception {
        UDPTransportClient client = null;

        if (!server.isAddressRegistered(packet.sender())) {
            client = (UDPTransportClient) server.registerAddress(packet.sender())
                                                .setAccessTime(System.currentTimeMillis())
                                                .setChannel((DatagramChannel) context.channel())
                                                .setMessageFormat(server.getDefaultMessageFormat());
        } else {
            client = (UDPTransportClient) server.getTransportClient(packet.sender())
                                                .setAccessTime(System.currentTimeMillis());
        }

        byte flags = packet.content().readByte();

        if ((flags & ACK_BIT) == ACK_BIT) {
            // ack packet
            encoder.handleAckPacket(client, packet);
        } else {
            // standard data packet
            handlePacket(client, packet, messages);
        }
    }

    /**
     * Retrieve the {@link UDPTransportServer}.
     */
    public UDPTransportServer getTransportServer () {
        return server;
    }

    /**
     * Handle an incoming {@link DatagramPacket}.
     *
     * @param client   The transport client.
     * @param packet   The datagram packet.
     * @param messages The output messages.
     *
     * @throws Exception If an error has occurred.
     */
    public abstract void handlePacket (UDPTransportClient client, DatagramPacket packet, List<Object> messages)
    throws Exception;

    /**
     * Set the {@link UDPTransportServer}.
     *
     * @param server The transport server.
     */
    public void setTransportServer (UDPTransportServer server) {
        this.encoder = ((UDPChannelPipelineInitializer) server.getChannelPipelineInitializer()).getEncoder();
        this.server  = server;
    }

}