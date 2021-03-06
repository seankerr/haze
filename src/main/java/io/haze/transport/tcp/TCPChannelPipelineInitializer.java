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

import io.haze.transport.ChannelPipelineInitializer;

import io.netty.channel.socket.SocketChannel;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class TCPChannelPipelineInitializer extends ChannelPipelineInitializer<SocketChannel,
                                                                              TCPTransportServer> {

    /** The tcp decoder. */
    private TCPDecoder decoder;

    /** The tcp encoder. */
    private TCPEncoder encoder;

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
               .addFirst("tcp-decoder", decoder)
               .addFirst("tcp-encoder", encoder);

        getTransportServer().registerAddress(channel.remoteAddress())
                            .setChannel(channel)
                            .setMessageFormat(getTransportServer().getDefaultMessageFormat());
    }

    /**
     * Initialize this {@link TCPChannelPipelineInitializer}.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void initialize ()
    throws Exception {
        super.initialize();

        decoder = new TCPDecoder(getTransportServer());
        encoder = new TCPEncoder();
    }

}