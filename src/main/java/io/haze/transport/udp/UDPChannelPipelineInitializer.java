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

import io.haze.transport.ChannelPipelineInitializer;
import io.haze.transport.udp.decoder.InOrderReliableUDPDecoder;
import io.haze.transport.udp.decoder.InOrderUnreliableUDPDecoder;
import io.haze.transport.udp.decoder.LatestReliableUDPDecoder;
import io.haze.transport.udp.decoder.LatestUnreliableUDPDecoder;
import io.haze.transport.udp.decoder.OutOfOrderReliableUDPDecoder;
import io.haze.transport.udp.decoder.OutOfOrderUnreliableUDPDecoder;
import io.haze.transport.udp.encoder.ReliableUDPEncoder;
import io.haze.transport.udp.encoder.UnreliableUDPEncoder;

import io.netty.channel.socket.DatagramChannel;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class UDPChannelPipelineInitializer extends ChannelPipelineInitializer<DatagramChannel,
                                                                              UDPTransportServer> {

    /** The udp decoder. */
    private UDPDecoder decoder;

    /** The udp encoder. */
    private UDPEncoder encoder;

    /**
     * Retrieve the {@link UDPdecoder}.
     */
    public UDPDecoder getDecoder () {
        return decoder;
    }

    /**
     * Retrieve the {@link UDPEncoder}.
     */
    public UDPEncoder getEncoder () {
        return encoder;
    }

    /**
     * Initialize the {@link DatagramChannel} pipeline.
     *
     * @param channel The datagram channel.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void initChannel (DatagramChannel channel)
    throws Exception {
        super.initChannel(channel);

        decoder.setTransportServer(getTransportServer());
        encoder.setTransportServer(getTransportServer());

        channel.pipeline()
               .addFirst("udp-decoder", decoder)
               .addFirst("udp-encoder", encoder);
    }

    /**
     * Initialize this {@link UDPChannelPipelineInitializer}.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void initialize ()
    throws Exception {
        super.initialize();

        boolean latest = getTransportServer().getServiceFactoryContext()
                                             .getConfiguration()
                                             .getBoolean(getTransportServer().getServiceFactoryContext()
                                                                             .getNamespace() +
                                                                             ".server.packet.latest",
                                                                             false);

        boolean ordered = getTransportServer().getServiceFactoryContext()
                                              .getConfiguration()
                                              .getBoolean(getTransportServer().getServiceFactoryContext()
                                                                              .getNamespace() +
                                                                              ".server.packet.ordered",
                                                                              false);

        boolean reliable = getTransportServer().getServiceFactoryContext()
                                               .getConfiguration()
                                               .getBoolean(getTransportServer().getServiceFactoryContext()
                                                                               .getNamespace() +
                                                                               ".server.packet.reliable",
                                                                               false);

        if (latest && reliable) {
            decoder = new LatestReliableUDPDecoder();
        } else if (latest && !reliable) {
            decoder = new LatestUnreliableUDPDecoder();
        } else if (ordered && reliable) {
            decoder = new InOrderReliableUDPDecoder();
        } else if (ordered && !reliable) {
            decoder = new InOrderUnreliableUDPDecoder();
        } else if (reliable) {
            decoder = new OutOfOrderReliableUDPDecoder();
        } else {
            decoder = new OutOfOrderUnreliableUDPDecoder();
        }

        if (reliable) {
            encoder = new ReliableUDPEncoder();
        } else {
            encoder = new UnreliableUDPEncoder();
        }
    }

}