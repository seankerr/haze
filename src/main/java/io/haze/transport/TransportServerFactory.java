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

package io.haze.transport;

import io.haze.core.FactoryException;
import io.haze.core.Service;
import io.haze.core.ServiceFactory;
import io.haze.core.ServiceFactoryContext;
import io.haze.message.MessageFormat;
import io.haze.message.MessageDecoder;
import io.haze.message.MessageDispatcher;
import io.haze.message.MessageEncoder;
import io.haze.transport.tcp.TCPTransportServer;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class TransportServerFactory extends ServiceFactory {

    /** The current transport server id. */
    private int serverId;

    /**
     * Retrieve the configuration element.
     */
    @Override
    public String getConfigurationElement () {
        return "transports";
    }

    /**
     * Retrieve the configuration sub-element.
     */
    @Override
    public String getConfigurationSubElement () {
        return "transport";
    }

    /**
     * Retrieve the {@link Service} that will be created.
     */
    @Override
    public Class<? extends Service> getServiceClass () {
        return TransportServer.class;
    }

    /**
     * Create a new {@link TransportServer} instance.
     *
     * @param context The context.
     *
     * @throws FactoryException If the transport server cannot be created.
     */
    public Service newInstance (ServiceFactoryContext context)
    throws FactoryException {
        TransportServer server = getClassElementInstance(context.getApplication().getConfiguration(),
                                                         context.getNamespace() + ".server", TransportServer.class);

        server.setServiceFactoryContext(context);

        if (context.getConfiguration().getBoolean(context.getNamespace() + ".server[@binary]", false)) {
            server.setDefaultMessageFormat(MessageFormat.BINARY);
        } else {
            server.setDefaultMessageFormat(MessageFormat.TEXT);
        }

        server.setChannelEventHandlerClass(
            getClassElement(context.getApplication().getConfiguration(), context.getNamespace() + ".event",
                            ChannelEventHandler.class,
                            (server instanceof TCPTransportServer)
                            ? "io.haze.transport.tcp.TCPChannelEventHandler"
                            : "io.haze.transport.udp.UDPChannelEventHandler")
        );

        server.setChannelPipelineInitializer(
            getClassElementInstance(context.getApplication().getConfiguration(), context.getNamespace() + ".pipeline",
                                    ChannelPipelineInitializer.class,
                                    (server instanceof TCPTransportServer)
                                    ? "io.haze.transport.tcp.TCPChannelPipelineInitializer"
                                    : "io.haze.transport.udp.UDPChannelPipelineInitializer")
        );

        server.setMessageDecoderClass(
            getClassElement(context.getApplication().getConfiguration(), context.getNamespace() + ".decoder",
                            MessageDecoder.class, "io.haze.message.framed.FramedMessageDecoder")
        );

        server.setMessageDispatcherClass(
            getClassElement(context.getApplication().getConfiguration(), context.getNamespace() + ".dispatcher",
                            MessageDispatcher.class, "io.haze.message.framed.FramedMessageDispatcher")
        );

        server.setMessageEncoderClass(
            getClassElement(context.getApplication().getConfiguration(), context.getNamespace() + ".encoder",
                            MessageEncoder.class, "io.haze.message.framed.FramedMessageEncoder")
        );

        server.setTransportClientClass(
            getClassElement(context.getApplication().getConfiguration(), context.getNamespace() + ".client",
                            TransportClient.class,
                            (server instanceof TCPTransportServer)
                            ? "io.haze.transport.tcp.TCPTransportClient"
                            : "io.haze.transport.udp.UDPTransportClient")
        );

        server.setTransportServerId(++serverId);

        return server;
    }

}