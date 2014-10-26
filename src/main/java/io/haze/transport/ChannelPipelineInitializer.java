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

import io.haze.message.MessageDecoder;
import io.haze.message.MessageDispatcher;
import io.haze.message.MessageEncoder;
import io.haze.transport.ChannelEventHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public abstract class ChannelPipelineInitializer<T_CHANNEL extends Channel,
                                                 T_SERVER extends TransportServer>
                                                 extends ChannelInitializer<T_CHANNEL> {

    /** The channel event handler. */
    private ChannelEventHandler eventHandler;

    /** The message decoder. */
    private MessageDecoder decoder;

    /** The message dispatcher. */
    private MessageDispatcher dispatcher;

    /** The message encoder. */
    private MessageEncoder encoder;

    /** The transport server. */
    private T_SERVER server;

    /**
     * Retrieve the {@link ChannelEventHandler}.
     */
    public ChannelEventHandler getChannelEventHandler () {
        return eventHandler;
    }

    /**
     * Retrieve the {@link MessageDecoder}.
     */
    public MessageDecoder getMessageDecoder () {
        return decoder;
    }

    /**
     * Retrieve the {@link MessageDispatcher}.
     */
    public MessageDispatcher getMessageDispatcher () {
        return dispatcher;
    }

    /**
     * Retrieve the {@link MessageEncoder}.
     */
    public MessageEncoder getMessageEncoder () {
        return encoder;
    }

    /**
     * Retrieve the {@link TransportServer}.
     */
    public T_SERVER getTransportServer () {
        return server;
    }

    /**
     * Initialize the {@link Channel} pipeline.
     *
     * @param channel The channel.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void initChannel (T_CHANNEL channel)
    throws Exception {
        channel.pipeline()
               .addLast("message-decoder", decoder)
               .addLast("message-encoder", encoder)
               .addLast("dispatcher", dispatcher)
               .addLast("event", eventHandler);
    }

    /**
     * Initialize this {@link ChannelPipelineInitializer}.
     *
     * @throws Exception If an error has occurred.
     */
    public void initialize ()
    throws Exception {
        decoder      = (MessageDecoder) getTransportServer().getMessageDecoderFactory().newInstance();
        dispatcher   = (MessageDispatcher) getTransportServer().getMessageDispatcherFactory().newInstance();
        encoder      = (MessageEncoder) getTransportServer().getMessageEncoderFactory().newInstance();
        eventHandler = (ChannelEventHandler) getTransportServer().getChannelEventHandlerFactory().newInstance();

        decoder.initialize();
        dispatcher.initialize();
        encoder.initialize();
        eventHandler.initialize();
    }

    /**
     * Set the {@link TransportServer}.
     *
     * @param server The transport server.
     */
    void setTransportServer (T_SERVER server) {
        this.server = server;
    }

}