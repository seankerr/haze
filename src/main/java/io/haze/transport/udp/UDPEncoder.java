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

import io.haze.message.BufferMessage;
import io.haze.message.MessageContext;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToByteEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
@Sharable
public abstract class UDPEncoder extends MessageToByteEncoder<MessageContext> {

    /** The flags byte size. */
    public static final int FLAGS_BYTE_SIZE = 1;

    /** The packet length byte size. */
    public static final int LENGTH_BYTE_SIZE = 2;

    /** The packet id byte size. */
    public static final int PACKET_ID_BYTE_SIZE = 2;

    /** The total headers byte size. */
    public static final int HEADERS_BYTE_SIZE = FLAGS_BYTE_SIZE +     // flags
                                                PACKET_ID_BYTE_SIZE + // current packet id
                                                PACKET_ID_BYTE_SIZE + // start packet id
                                                PACKET_ID_BYTE_SIZE + // end packet id
                                                LENGTH_BYTE_SIZE;     // packet length

    /** The safe UDP packet size. */
    public static final int SAFE_PACKET_SIZE = 512;

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(UDPEncoder.class);

    /** The transport server. */
    private UDPTransportServer server;

    /**
     * Encode a {@link MessageContext} into a {@link ByteBuf}.
     *
     * @param context The channel handler context.
     * @param message The input message.
     * @param buffer  The output buffer.
     */
    @Override
    public void encode (ChannelHandlerContext context, MessageContext message, ByteBuf buffer)
    throws Exception {
        ByteBuf messageBuffer = ((BufferMessage) message.getMessage()).getBuffer();
        int     count         = messageBuffer.readableBytes() / SAFE_PACKET_SIZE +
                                ((messageBuffer.readableBytes() % SAFE_PACKET_SIZE == 0) ? 0 : 1);
        State   state         = (State) message.getTransportClient().getState(State.class);

        if (state == null) {
            state = new State();
        }

        try {
            int endPacketId   = state.packetId - 1 + count;
            int startPacketId = state.packetId;

            while (messageBuffer.readableBytes() > 0) {
                int     capacity     = Math.min(SAFE_PACKET_SIZE,
                                                messageBuffer.readableBytes() + HEADERS_BYTE_SIZE);
                ByteBuf packetBuffer = Unpooled.buffer(capacity, capacity);

                // write flags, current packet id, start packet id, end packet id, payload length
                packetBuffer.writeByte(0);
                packetBuffer.writeShort(state.packetId++);
                packetBuffer.writeShort(startPacketId);
                packetBuffer.writeShort(endPacketId);
                packetBuffer.writeShort((short) (capacity - HEADERS_BYTE_SIZE));

                // read one packet worth of data from buffer and handle the packet
                messageBuffer.readBytes(packetBuffer, capacity - HEADERS_BYTE_SIZE);

                handlePacket((UDPTransportClient) message.getTransportClient(),
                             state.packetId,
                             new DatagramPacket(packetBuffer, message.getTransportClient().getAddress()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            message.getTransportClient().setState(state.getClass(), state);
        }
    }

    /**
     * Retrieve the {@link UDPTransportServer}.
     */
    public UDPTransportServer getTransportServer () {
        return server;
    }

    /**
     * Handle an ACK {@link DatagramPacket}.
     *
     * @param client The transport client.
     * @param packet The ack datagram packet.
     *
     * @throws Exception If an error has occurred.
     */
    public abstract void handleAckPacket (UDPTransportClient client, DatagramPacket packet)
    throws Exception;

    /**
     * Handle an outgoing {@link DatagramPacket}.
     *
     * @param client   The transport client.
     * @param apcketId The packet id.
     * @param packet   The datagram packet.
     *
     * @throws Exception If an error has occurred.
     */
    public abstract void handlePacket (UDPTransportClient client, short packetId, DatagramPacket packet)
    throws Exception;

    /**
     * Set the {@link UDPTransportServer}.
     *
     * @param server The transport server.
     */
    public void setTransportServer (UDPTransportServer server) {
        this.server = server;
    }

    /**
     * TODO: Documentation.
     *
     * @author Sean Kerr [sean@code-box.org]
     */
    private static class State {

        /** The packet id. */
        private short packetId;

    }

}