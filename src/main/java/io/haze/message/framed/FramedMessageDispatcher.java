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

import io.haze.message.Message;
import io.haze.message.MessageContext;
import io.haze.message.MessageDispatcher;
import io.haze.node.Node;
import io.haze.node.system.SystemNode;
import io.haze.node.system.message.ErrorResponse;
import io.haze.transport.TransportClient;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class FramedMessageDispatcher extends MessageDispatcher {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(FramedMessageDispatcher.class);

    /** The array of nodes. */
    private Node[] nodes;

    /**
     * Dispatch a {@link MessageContext}.
     *
     * @param context The channel handler context.
     * @param message The input message.
     * @param output  The output messages.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void decode (ChannelHandlerContext context, MessageContext message, List<Object> output)
    throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(message.getMessage() + " to node " + ((FramedMessageContext) message).getNodeId());
        }

        nodes[((FramedMessageContext) message).getNodeId()].receiveMessage(message);
    }

    /**
     * Initialize this {@link FramedMessageDispatcher}.
     */
    @Override
    public void initialize () {
        nodes = getFactoryContext().getApplication().getNodes().toArray(new Node[0]);
    }

    /**
     * Write an error to a {@link TransportClient}.
     *
     * @param client  The transport client.
     * @param message The error message.
     */
    @Override
    public FramedMessageDispatcher writeError (TransportClient client, String message) {
        client.write(new FramedMessageContext(client, new ErrorResponse(message), SystemNode.NODE_ID));

        return this;
    }

    /**
     * Write an error to a {@link TransportClient}.
     *
     * @param client  The transport client.
     * @param code    The error code.
     * @param message The error message.
     */
    @Override
    public FramedMessageDispatcher writeError (TransportClient client, int code, String message) {
        client.write(new FramedMessageContext(client, new ErrorResponse(code, message), SystemNode.NODE_ID));

        return this;
    }

    /**
     * Write an exception to a {@link TransportClient}.
     *
     * @param client    The transport client.
     * @param exception The exception.
     */
    @Override
    public FramedMessageDispatcher writeException (TransportClient client, Throwable exception) {
        client.write(new FramedMessageContext(client, new ErrorResponse(exception), SystemNode.NODE_ID));

        return this;
    }

    /**
     * Write a message to a {@link TransportClient}.
     *
     * @param client  The transport client.
     * @param message The message.
     */
    @Override
    public FramedMessageDispatcher writeMessage (TransportClient client, Message message) {
        client.write(new FramedMessageContext(client, message, SystemNode.NODE_ID));

        return this;
    }

}