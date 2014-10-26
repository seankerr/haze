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

package io.haze.message.framed;

import io.haze.message.BufferMessage;
import io.haze.message.MessageContext;
import io.haze.message.MessageDecoder;
import io.haze.message.MessageDispatcher;
import io.haze.message.reader.MessageReader;
import io.haze.node.Node;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class FramedMessageDecoder extends MessageDecoder {

    /** The message dispatcher. */
    private MessageDispatcher dispatcher;

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(FramedMessageDecoder.class);

    /** The array of nodes. */
    private Node[] nodes;

    /**
     * Decode a {@link MessageContext} to get the node id and message length, then build a
     * {@link io.haze.message.Message} and send it to the {@link Node}.
     *
     * @param context  The channel handler context.
     * @param message  The input message.
     * @param messages The output messages.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void decode (ChannelHandlerContext context, MessageContext message, List<Object> messages)
    throws Exception {
        ByteBuf       buffer = ((BufferMessage) message.getMessage()).getBuffer();
        MessageReader reader = message.getTransportClient().getMessageReader();
        State         state  = (State) message.getTransportClient().getState(State.class);

        if (state == null) {
            state = new State();
        }

        try {
            while (true) {
                // set this each time, since we use a temporary buffer for each message
                reader.setBuffer(buffer);

                if (state.nodeId < 0 && reader.hasShort()) {
                    state.nodeId = reader.readShort();
                }

                if (state.length < 0 && reader.hasInt()) {
                    state.length = reader.readInt();
                }

                if (state.length > -1 && state.length <= buffer.readableBytes()) {
                    if (state.nodeId > -1 && state.nodeId < nodes.length) {
                        try {
                            // place the entire message into a temp buffer
                            ByteBuf tempBuffer = Unpooled.buffer();

                            buffer.readBytes(tempBuffer, state.length);
                            reader.setBuffer(tempBuffer);

                            messages.add(new FramedMessageContext(message.getTransportClient(),
                                                                  nodes[state.nodeId].buildMessage(reader),
                                                                  state.nodeId));
                        } catch (Exception e) {
                            // invalid message
                            dispatcher.writeException(message.getTransportClient(), e);

                            logger.error(e.getMessage(), e);
                        }
                    } else {
                        // invalid node
                        dispatcher.writeError(message.getTransportClient(), "Invalid node: " + state.nodeId);
                    }

                    // reset values for the next message
                    state.length = -1;
                    state.nodeId = -1;
                } else {
                    // no more messages
                    if (logger.isDebugEnabled()) {
                        for (Object object : messages) {
                            logger.debug(((MessageContext) object).getMessage().toString());
                        }
                    }

                    return;
                }
            }
        } catch (Exception e) {
            // the message  couldn't be read, which means the rest of the stream is foobarred
            dispatcher.writeError(message.getTransportClient(), "Invalid message");

            // throw the exception to make sure the global handler catches it and disconnects the client
            throw e;
        } finally {
            message.getTransportClient().setState(state.getClass(), state);
        }
    }

    /**
     * Initialize this {@link FramedMessageEncoder}.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void initialize ()
    throws Exception {
        dispatcher = getFactoryContext().getTransportServer()
                                        .getChannelPipelineInitializer()
                                        .getMessageDispatcher();

        nodes = getFactoryContext().getApplication().getNodes().toArray(new Node[0]);
    }

    /**
     * TODO: Documentation.
     *
     * @author Sean Kerr [sean@code-box.org]
     */
    private static class State {

        /** The message length. */
        private int length = -1;

        /** The node id. */
        private int nodeId = -1;

    }

}