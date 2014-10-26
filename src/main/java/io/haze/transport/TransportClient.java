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

import io.haze.core.FactoryContext;
import io.haze.core.FactoryInitializer;
import io.haze.message.MessageContext;
import io.haze.message.MessageFormat;
import io.haze.message.reader.BinaryMessageReader;
import io.haze.message.reader.MessageReader;
import io.haze.message.reader.TextMessageReader;
import io.haze.message.writer.BinaryMessageWriter;
import io.haze.message.writer.MessageWriter;
import io.haze.message.writer.TextMessageWriter;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public abstract class TransportClient<T_CHANNEL extends Channel,
                                      T_SERVER extends TransportServer> implements FactoryInitializer {

    /** The most recent time this client sent a message. */
    private long accessTime = System.currentTimeMillis();

    /** The internet socket address. */
    private InetSocketAddress address;

    /** The underlying channel. */
    private T_CHANNEL channel;

    /** The factory context. */
    private FactoryContext context;

    /** The message format. */
    private MessageFormat format;

    /** Indicates whether or not this client is still connected. */
    private Boolean isConnected = true;

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(TransportClient.class);

    /** The message reader. */
    private MessageReader reader;

    /** The message writer. */
    private MessageWriter writer;

    /** The transport server. */
    private T_SERVER server;

    /** The map of state data. */
    private Map<Class,Object> state = Collections.synchronizedMap(new HashMap());

    /**
     * Disconnect this {@link TransportClient}.
     *
     * @param message The disconnect message.
     */
    public TransportClient<T_CHANNEL,T_SERVER> disconnect (String message) {
        synchronized (isConnected) {
            isConnected = false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Disconnected '" + message + "': " + this);
        }

        return this;
    }

    /**
     * Flush the underlying {@link Channel}.
     */
    public void flush () {
        getChannel().flush();
    }

    /**
     * Retrieve the most recent time {@link TransportClient} sent a message.
     */
    public long getAccessTime () {
        return accessTime;
    }

    /**
     * Retrieve the {@link InetSocketAddress}.
     */
    public InetSocketAddress getAddress () {
        return address;
    }

    /**
     * Retrieve the underlying {@link Channel}.
     */
    public T_CHANNEL getChannel () {
        return channel;
    }

    /**
     * Retrieve the {@link FactoryContext}.
     */
    @Override
    public FactoryContext getFactoryContext () {
        return context;
    }

    /**
     * Retrieve the {@link MessageFormat}.
     */
    public MessageFormat getMessageFormat () {
        return format;
    }

    /**
     * Retrieve the {@link MessageReader}.
     */
    public MessageReader getMessageReader () {
        return reader;
    }

    /**
     * Retrieve the {@link MessageWriter}.
     */
    public MessageWriter getMessageWriter () {
        return writer;
    }

    /**
     * Retrieve state for a particular class accessing this {@link TransportClient}.
     *
     * @param clazz The class.
     */
    public Object getState (Class clazz) {
        return state.get(clazz);
    }

    /**
     * Retrieve the {@link TransportServer}.
     */
    public T_SERVER getTransportServer () {
        return server;
    }

    /**
     * Handle the unregistration of this {@link TransportClient}.
     */
    public void handleUnregister () {
    }

    /**
     * Indicates whether or not state data for a particular class exists for this {@link TransportClient}.
     *
     * @param clazz The class.
     */
    public boolean hasStateData (Class clazz) {
        return state.containsKey(clazz);
    }

    /**
     * Initialize this {@link TransportClient}.
     */
    @Override
    public void initialize () {
    }

    /**
     * Indicates whether or not this {@link TransportClient} is connected.
     */
    public boolean isConnected () {
        synchronized (isConnected) {
            return isConnected;
        }
    }

    /**
     * Set the most recent time this {@link TransportClient} sent a message.
     *
     * @param time The time.
     */
    public TransportClient<T_CHANNEL,T_SERVER> setAccessTime (long time) {
        this.accessTime = time;

        return this;
    }

    /**
     * Set the {@link InetSocketAddress}.
     *
     * @param address The internet socket address.
     */
    public TransportClient<T_CHANNEL,T_SERVER> setAddress (InetSocketAddress address) {
        this.address = address;

        return this;
    }

    /**
     * Set the underlying {@link Channel}.
     *
     * @param channel The channel.
     */
    public TransportClient<T_CHANNEL,T_SERVER> setChannel (T_CHANNEL channel) {
        this.channel = channel;

        return this;
    }

    /**
     * Set the {@link FactoryContext}.
     *
     * @param context The factory context.
     */
    @Override
    public void setFactoryContext (FactoryContext context) {
        this.context = context;
    }

    /**
     * Set the {@link MessageFormat}.
     *
     * @param format The message format.
     */
    public TransportClient<T_CHANNEL,T_SERVER> setMessageFormat (MessageFormat format) {
        this.format = format;

        if (format == MessageFormat.BINARY) {
            reader = new BinaryMessageReader();
            writer = new BinaryMessageWriter();
        } else {
            reader = new TextMessageReader();
            writer = new TextMessageWriter();
        }

        return this;
    }

    /**
     * Set the state data for a particular class accessing this {@link TransportClient}.
     *
     * @param clazz The class.
     * @param data  The data.
     */
    public TransportClient<T_CHANNEL,T_SERVER> setState (Class clazz, Object data) {
        state.put(clazz, data);

        return this;
    }

    /**
     * Set the {@link TransportServer}.
     *
     * @param server The transport server.
     */
    TransportClient<T_CHANNEL,T_SERVER> setTransportServer (T_SERVER server) {
        this.server = server;

        return this;
    }

    /**
     * Retrieve the string representation of this {@link TransportClient}.
     */
    @Override
    public String toString () {
        return String.format("<%s:%s>", getClass().getSimpleName(), getAddress());
    }

    /**
     * Write and flush a {@link MessageContext}.
     *
     * @param context The message context.
     */
    public TransportClient<T_CHANNEL,T_SERVER> write (MessageContext context) {
        getChannel().writeAndFlush(context);

        return this;
    }

    /**
     * Write and flush an object.
     *
     * @param object The object.
     */
    public TransportClient<T_CHANNEL,T_SERVER> write (Object object) {
        getChannel().writeAndFlush(object);

        return this;
    }

    /**
     * Write a {@link MessageContext}.
     *
     * @param context The message context.
     * @param flush   Indicates whether or not the message context should be flushed.
     */
    public TransportClient<T_CHANNEL,T_SERVER> write (MessageContext context, boolean flush) {
        if (flush) {
            getChannel().writeAndFlush(context);
        } else {
            getChannel().write(context);
        }

        return this;
    }

    /**
     * Write an object.
     *
     * @param object The object.
     * @param flush  Indicates whether or not the object should be flushed.
     */
    public TransportClient<T_CHANNEL,T_SERVER> write (Object object, boolean flush) {
        if (flush) {
            getChannel().writeAndFlush(object);
        } else {
            getChannel().write(object);
        }

        return this;
    }

}