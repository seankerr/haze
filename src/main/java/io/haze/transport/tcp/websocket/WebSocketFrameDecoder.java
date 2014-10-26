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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class WebSocketFrameDecoder extends MessageToMessageDecoder<Object> {

    /** The map of handshakers. */
    private Map<InetSocketAddress,WebSocketServerHandshaker> handshakers = Collections.synchronizedMap(new HashMap());

    /** The transport server. */
    private TransportServer server;

    /**
     * Create a new {@link WebSocketFrameDecoder} instance.
     *
     * @param server The transport server.
     */
    public WebSocketFrameDecoder (TransportServer server) {
        this.server = server;
    }

    /**
     * Decode a {@link FullHttpRequest} or {@link WebSocketFrame} into a {@link io.haze.message.Message}.
     *
     * @param context  The channel handler context.
     * @param message  The input message.
     * @param messages The output messages.
     */
    @Override
    public void decode (ChannelHandlerContext context, Object message, List<Object> messages)
    throws Exception {
        if (message instanceof WebSocketFrame) {
            handleWebSocket(context, (WebSocketFrame) message, messages);
        } else if (message instanceof FullHttpRequest) {
            handleHTTP(context, (FullHttpRequest) message, messages);
        }
    }

    /**
     * Handle the initial HTTP request and execute the web socket handshake.
     *
     * @param context  The channel handler context.
     * @param request  The HTTP request.
     * @param messages The output messages.
     *
     * @throws Exception If an error has occurred.
     */
    private void handleHTTP (ChannelHandlerContext context, FullHttpRequest request, List<Object> messages)
    throws Exception {
        WebSocketServerHandshakerFactory handshakerFactory = new WebSocketServerHandshakerFactory(
                                    String.format("ws://%s/", request.headers().get(Names.HOST)), null, false
                                );

        WebSocketServerHandshaker handshaker = handshakerFactory.newHandshaker(request);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(context.channel());
        } else {
            InetSocketAddress address = ((SocketChannel) context.channel()).remoteAddress();

            handshakers.put(address, handshaker);

            handshaker.handshake(context.channel(), request);
        }
    }

    /**
     * Handle a web socket frame.
     *
     * @param context  The channel handler context.
     * @param frame    The web socket frame.
     * @param messages The output messages.
     *
     * @throws Exception If an error has occurred.
     */
    private void handleWebSocket (ChannelHandlerContext context, WebSocketFrame frame, List<Object> messages)
    throws Exception {
        if (frame instanceof TextWebSocketFrame || frame instanceof BinaryWebSocketFrame) {
            messages.add(frame.content().retain());
        } else if (frame instanceof CloseWebSocketFrame) {
            InetSocketAddress         address    = ((SocketChannel) context.channel()).remoteAddress();
            WebSocketServerHandshaker handshaker = handshakers.remove(address);

            handshaker.close(context.channel(), (CloseWebSocketFrame) frame.retain());
        } else if (frame instanceof PingWebSocketFrame) {
            context.writeAndFlush(new PongWebSocketFrame());
        }
    }

}