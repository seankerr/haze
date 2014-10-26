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
import io.haze.message.MessageEncoder;
import io.haze.message.writer.MessageWriter;

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
public class FramedMessageEncoder extends MessageEncoder {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(FramedMessageEncoder.class);

    /**
     * Encode the encapsulated {@link io.haze.message.Message} into a {@link BufferMessage} and then overwrite the old
     * {@link io.haze.message.Message}.
     *
     * @param context  The channel handler context.
     * @param message  The input message.
     * @param messages The output messages.
     */
    @Override
    public void encode (ChannelHandlerContext context, MessageContext message, List<Object> messages)
    throws Exception {
        BufferMessage bufferMessage = new BufferMessage(Unpooled.buffer());
        ByteBuf       payloadBuffer = Unpooled.buffer();
        MessageWriter writer        = message.getTransportClient().getMessageWriter();

        if (logger.isDebugEnabled()) {
            logger.debug(message.getMessage().toString());
        }

        // write node id
        writer.setBuffer(bufferMessage.getBuffer());
        writer.writeShort((short) ((FramedMessageContext) message).getNodeId());

        // serialize message into a separate buffer so we can exact the payload length
        writer.setBuffer(payloadBuffer);
        ((FramedMessageContext) message).getMessage().serialize(writer);

        // write payload length and serialized message
        writer.setBuffer(bufferMessage.getBuffer());
        writer.writeInt(payloadBuffer.readableBytes());

        // increase message buffer and write payload buffer into it
        bufferMessage.getBuffer().capacity(bufferMessage.getBuffer().readableBytes() + payloadBuffer.readableBytes());
        payloadBuffer.readBytes(bufferMessage.getBuffer(), payloadBuffer.readableBytes());
        message.setMessage(bufferMessage);
        messages.add(message);
    }

}