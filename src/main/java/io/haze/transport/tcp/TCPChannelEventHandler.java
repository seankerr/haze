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

import io.haze.message.MessageDispatcher;
import io.haze.transport.ChannelEventHandler;
import io.haze.transport.TransportClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class TCPChannelEventHandler extends ChannelEventHandler {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(TCPChannelEventHandler.class);

    /**
     * Handle a channel unregistered event.
     *
     * @param context The channel handler context.
     */
    @Override
    public void channelUnregistered (ChannelHandlerContext context) {
        getFactoryContext().getTransportServer()
                           .unregisterAddress(((SocketChannel) context.channel()).remoteAddress());
    }

    /**
     * Handle an uncaught exception.
     *
     * @param context   The channel handler context.
     * @param exception The thrown exception.
     */
    @Override
    public void exceptionCaught (ChannelHandlerContext context, Throwable exception) {
        try {
            TransportClient client = getFactoryContext().getTransportServer()
                                                        .getTransportClient(((SocketChannel) context.channel())
                                                                            .remoteAddress());

            ((MessageDispatcher) context.channel()
                                        .pipeline()
                                        .get("message-dispatcher"))
                                        .writeException(client,
                                                        exception);
        } catch (Exception e) {
            logger.error(String.format("Could not handle exception: %s", e.getMessage()));
        }
    }

}