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

import io.haze.transport.tcp.TCPChannelPipelineInitializer;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class WebSocketChannelPipelineInitializer extends TCPChannelPipelineInitializer {

    /**
     * Initialize the {@link SocketChannel} pipeline.
     *
     * @param channel The socket channel.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void initChannel (SocketChannel channel)
    throws Exception {
        super.initChannel(channel);

        channel.pipeline()
               .addFirst("http-codec", new HttpServerCodec())
               .addAfter("http-codec", "http-aggregator", new HttpObjectAggregator(65536))
               .addAfter("http-aggregator", "web-socket-decoder", new WebSocketFrameDecoder(getTransportServer()))
               .addAfter("web-socket-decoder", "web-socket-encoder", new WebSocketFrameEncoder(getTransportServer()));
    }

}