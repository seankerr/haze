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

package io.haze.transport.tcp.websocket;

import io.haze.transport.TransportServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class WebSocketFrameEncoder extends MessageToMessageEncoder<ByteBuf> {

    /** The transport server. */
    private TransportServer server;

    /**
     * Create a new {@link WebSocketFrameEncoder} instance.
     *
     * @param server The transport server.
     */
    public WebSocketFrameEncoder (TransportServer server) {
        this.server = server;
    }

    /**
     * Encode a {@link ByteBuf} into a {@link io.netty.handler.codec.http.websocketx.WebSocketFrame}.
     *
     * @param context  The channel handler context.
     * @param message  The input message.
     * @param messages The output messages.
     */
    @Override
    public void encode (ChannelHandlerContext context, ByteBuf message, List<Object> messages)
    throws Exception {
        messages.add(new TextWebSocketFrame(message.retain()));
    }

}