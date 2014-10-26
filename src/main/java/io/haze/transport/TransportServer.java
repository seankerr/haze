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

import io.haze.core.ClassFactory;
import io.haze.core.FactoryContext;
import io.haze.core.Service;
import io.haze.core.ServiceLoop;
import io.haze.message.MessageFormat;
import io.haze.message.MessageDecoder;
import io.haze.message.MessageDispatcher;
import io.haze.message.MessageEncoder;
import io.haze.node.Node;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public abstract class TransportServer<T extends TransportClient> extends Service implements ServiceLoop {

    /** The default host. */
    public static final String DEFAULT_HOST = "0.0.0.0";

    /** The default sleep interval (in milliseconds) between loop execution. */
    public static final long DEFAULT_SLEEP = 1000;

    /** The default client timeout. */
    public static final int DEFAULT_TIMEOUT = 1000 * 60;

    /** The channel event handler factory. */
    private ClassFactory<ChannelEventHandler> channelEventHandlerFactory;

    /** The channel pipeline initializer. */
    private ChannelPipelineInitializer channelPipelineInitializer;

    /** The map of transport clients. */
    private Map<InetSocketAddress,T> clients = Collections.synchronizedMap(new HashMap());

    /** The default client message format. */
    private MessageFormat defaultMessageFormat;

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(TransportServer.class);

    /** The message decoder factory. */
    private ClassFactory<MessageDecoder> messageDecoderFactory;

    /** The message dispatcher factory. */
    private ClassFactory<MessageDispatcher> messageDispatcherFactory;

    /** The message encoder factory. */
    private ClassFactory<MessageEncoder> messageEncoderFactory;

    /** The transport client factory. */
    private ClassFactory<T> transportClientFactory;

    /** The transport server id. */
    private int serverId;

    /** The sleep interval (in milliseconds) between loop execution. */
    private long sleep;

    /** The client timeout. */
    private int timeout;

    /**
     * Retrieve the {@link ChannelEventHandler} factory.
     */
    public ClassFactory<ChannelEventHandler> getChannelEventHandlerFactory () {
        return channelEventHandlerFactory;
    }

    /**
     * Retrieve the {@link ChannelPipelineInitializer}.
     */
    public ChannelPipelineInitializer getChannelPipelineInitializer () {
        return channelPipelineInitializer;
    }

    /**
     * Retrieve the default {@link MessageFormat}.
     */
    public MessageFormat getDefaultMessageFormat () {
        return defaultMessageFormat;
    }

    /**
     * Retrieve the {@link MessageDecoder} factory.
     */
    public ClassFactory<MessageDecoder> getMessageDecoderFactory () {
        return messageDecoderFactory;
    }

    /**
     * Retrieve the {@link MessageDispatcher} factory.
     */
    public ClassFactory<MessageDispatcher> getMessageDispatcherFactory () {
        return messageDispatcherFactory;
    }

    /**
     * Retrieve the {@link MessageEncoder} factory.
     */
    public ClassFactory<MessageEncoder> getMessageEncoderFactory () {
        return messageEncoderFactory;
    }

    /**
     * Retrieve the {@link TransportClient} associated with a given {@link InetSocketAddress}.
     *
     * @param address The socket address.
     *
     * @throws TransportException If the channel is not registered.
     */
    public T getTransportClient (InetSocketAddress address)
    throws TransportException {
        T client = clients.get(address);

        if (client != null) {
            return client;
        }

        throw new TransportException("Address is not registered");
    }

    /**
     * Retrieve a connected {@link TransportClient} collection.
     */
    public Collection<T> getTransportClients () {
        return Collections.unmodifiableCollection(clients.values());
    }

    /**
     * Retrieve the transport server id.
     */
    public int getTransportServerId () {
        return serverId;
    }

    /**
     * Initialize this {@link TransportServer}.
     */
    @Override
    public void initialize () {
        super.initialize();

        sleep   = getServiceFactoryContext().getConfiguration().getLong(getServiceFactoryContext().getNamespace() +
                                                                        ".server.sleep", DEFAULT_SLEEP);
        timeout = getServiceFactoryContext().getConfiguration().getInt(getServiceFactoryContext().getNamespace() +
                                                                       ".server.timeout", DEFAULT_TIMEOUT);
    }

    /**
     * Indicates whether or not a {@link InetSocketAddress} has been registered.
     *
     * @param address The inet socket address.
     */
    public boolean isAddressRegistered (InetSocketAddress address) {
        return clients.containsKey(address);
    }

    /**
     * Handle a loop iteration.
     */
    @Override
    public void loop () {
        // check timeouts
        List<TransportClient> disconnects = new ArrayList();

        long now = System.currentTimeMillis();

        for (TransportClient client : clients.values()) {
            if (client.getAccessTime() < now - timeout) {
                disconnects.add(client);
            }
        }

        for (TransportClient client : disconnects) {
            client.disconnect("Timeout");
        }
    }

    /**
     * Register a {@link InetSocketAddress}.
     *
     * @param address The inet socket address.
     *
     * @throws TransportException If the channel could not be registered.
     */
    public T registerAddress (InetSocketAddress address)
    throws TransportException {
        if (clients.containsKey(address)) {
            throw new TransportException("Address is already registered");
        }

        try {
            T client = transportClientFactory.newInstance();

            client.setAddress(address)
                  .setTransportServer(this)
                  .initialize();

            clients.put(address, client);

            if (logger.isDebugEnabled()) {
                logger.debug("Connected: " + client);
            }

            try {
                for (Node node : getServiceFactoryContext().getApplication().getNodes()) {
                    node.handleConnect(client);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            return client;
        } catch (Exception e) {
            throw new TransportException(String.format("Could not create TransportClient instance: %s",
                                                       e.getMessage()));
        }
    }

    /**
     * Run this {@link TransportServer}.
     */
    @Override
    public void run () {
        block(sleep, this);
    }

    /**
     * Set the {@link ChannelEventHandler} class.
     *
     * @param clazz The channel event handler class.
     */
    void setChannelEventHandlerClass (Class<ChannelEventHandler> clazz) {
        channelEventHandlerFactory = new ClassFactory<ChannelEventHandler>(
                                         clazz,
                                         new FactoryContext(getServiceFactoryContext().getApplication(),
                                                            getServiceFactoryContext().getNamespace() + ".event",
                                                            this)
                                     );
    }

    /**
     * Set the {@link ChannelPipelineInitializer}.
     *
     * @param channelPipelineInitializer The channel pipeline initializer.
     */
    void setChannelPipelineInitializer (ChannelPipelineInitializer channelPipelineInitializer) {
        this.channelPipelineInitializer = channelPipelineInitializer;

        channelPipelineInitializer.setTransportServer(this);
    }

    /**
     * Set the default {@link MessageFormat}.
     *
     * @param messageFormat The default message format.
     */
    void setDefaultMessageFormat (MessageFormat defaultMessageFormat) {
        defaultMessageFormat = defaultMessageFormat;
    }

    /**
     * Set the {@link MessageDecoder} class.
     *
     * @param clazz The message decoder class.
     */
    void setMessageDecoderClass (Class<MessageDecoder> clazz) {
        messageDecoderFactory = new ClassFactory<MessageDecoder>(
                                    clazz,
                                    new FactoryContext(getServiceFactoryContext().getApplication(),
                                                       getServiceFactoryContext().getNamespace() + ".decoder",
                                                       this)
                                );
    }

    /**
     * Set the {@link MessageDispatcher} class.
     *
     * @param clazz The message decoder class.
     */
    void setMessageDispatcherClass (Class<MessageDispatcher> clazz) {
        messageDispatcherFactory = new ClassFactory<MessageDispatcher>(
                                       clazz,
                                       new FactoryContext(getServiceFactoryContext().getApplication(),
                                                          getServiceFactoryContext().getNamespace() + ".dispatcher",
                                                          this)
                                   );
    }

    /**
     * Set the {@link MessageEncoder} class.
     *
     * @param clazz The message encoder class.
     */
    void setMessageEncoderClass (Class<MessageEncoder> clazz) {
        messageEncoderFactory = new ClassFactory<MessageEncoder>(
                                    clazz,
                                    new FactoryContext(getServiceFactoryContext().getApplication(),
                                                       getServiceFactoryContext().getNamespace() + ".encoder",
                                                       this)
                                );
    }

    /**
     * Set the {@link TransportClient} class.
     *
     * @param clazz The transport client class.
     */
    void setTransportClientClass (Class<T> clazz) {
        transportClientFactory = new ClassFactory<T>(
                                     clazz,
                                     new FactoryContext(getServiceFactoryContext().getApplication(),
                                                        getServiceFactoryContext().getNamespace() + ".client",
                                                        this)
                                 );
    }

    /**
     * Set the transport server id.
     *
     * @param serverId The transport server id.
     */
    void setTransportServerId (int serverId) {
        this.serverId = serverId;
    }

    /**
     * Stop this {@link TransportServer}.
     */
    @Override
    public synchronized void stop () {
        // note: can't use an iterator over the client map and call unregisterAddress() which directly edits
        //       the map -- we must copy the addresses and loop those
        List<InetSocketAddress> addresses = new ArrayList();

        for (InetSocketAddress address : clients.keySet()) {
            addresses.add(address);
        }

        for (InetSocketAddress address : addresses) {
            try {
                getTransportClient(address).disconnect("Shutting down");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        // notify all nodes that we're shutting down
        for (Node node : getServiceFactoryContext().getApplication().getNodes()) {
            node.handleTransportShutdown(this);
        }

        super.stop();
    }

    /**
     * Retrieve the string representation of this {@link TransportServer}.
     */
    @Override
    public String toString () {
        return String.format("<%s:%s:%s>", getClass().getSimpleName(), getServiceFactoryContext().getName(),
                             getTransportServerId());
    }

    /**
     * Unregister a {@link InetSocketAddress}.
     *
     * @param address The inet socket address.
     */
    public T unregisterAddress (InetSocketAddress address) {
        T client = clients.remove(address);

        if (client != null) {
            client.handleUnregister();

            for (Node node : getServiceFactoryContext().getApplication().getNodes()) {
                node.handleDisconnect(client);
            }

            return client;
        }

        return null;
    }

}