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

import io.haze.message.BufferMessage;
import io.haze.message.MessageContext;
import io.haze.transport.TransportServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
@Sharable
public class TCPDecoder extends MessageToMessageDecoder<ByteBuf> {

    /** The transport server. */
    private TransportServer server;

    /**
     * Create a new {@link TCPDecoder} instance.
     *
     * @param server The transport server.
     */
    public TCPDecoder (TransportServer server) {
        this.server = server;
    }

    /**
     * Decode a {@link ByteBuf} into a {@link MessageContext}.
     *
     * @param context  The channel handler context.
     * @param message  The message.
     * @param messages The output messages.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void decode (ChannelHandlerContext context, ByteBuf message, List<Object> messages)
    throws Exception {
        messages.add(new MessageContext(server.getTransportClient(((SocketChannel) context.channel()).remoteAddress())
                                              .setAccessTime(System.currentTimeMillis()),
                                        new BufferMessage(message.retain())));
    }

}