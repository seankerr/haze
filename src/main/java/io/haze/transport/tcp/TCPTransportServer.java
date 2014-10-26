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
import io.haze.transport.TransportServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class TCPTransportServer extends TransportServer<TCPTransportClient> {

    /** The default port. */
    public static final int DEFAULT_PORT = 10010;

    /** The listening host. */
    private String host;

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(TCPTransportServer.class);

    /** The listening port. */
    private int port;

    /**
     * Initialize this {@link TCPTransportServer}.
     */
    @Override
    public void initialize () {
        super.initialize();

        host = getServiceFactoryContext().getConfiguration().getString(getServiceFactoryContext().getNamespace() +
                                                                       ".server.host", DEFAULT_HOST);
        port = getServiceFactoryContext().getConfiguration().getInt(getServiceFactoryContext().getNamespace() +
                                                                    ".server.port", DEFAULT_PORT);
    }

    /**
     * Run this {@link TCPTransportServer}.
     */
    @Override
    public void run () {
        EventLoopGroup bossGroup   = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap            bootstrap           = new ServerBootstrap();
            ChannelPipelineInitializer pipelineInitializer = getChannelPipelineInitializer();

            pipelineInitializer.initialize();

            bootstrap.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(pipelineInitializer);

            Channel channel = bootstrap.bind(host, port).sync().channel();

            logger.info(String.format("%s running on %s:%d", getClass().getSimpleName(), host, port));

            super.run();

            channel.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}