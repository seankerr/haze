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

import io.haze.message.BufferMessage;
import io.haze.message.MessageContext;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
@Sharable
public class TCPEncoder extends MessageToMessageEncoder<MessageContext> {

    /**
     * Encode a {@link MessageContext} into a {@link io.netty.buffer.ByteBuf}.
     *
     * @param context  The channel handler context.
     * @param message  The input message.
     * @param messages The output messages.
     */
    @Override
    public void encode (ChannelHandlerContext context, MessageContext message, List<Object> messages)
    throws Exception {
        context.write(((BufferMessage) message.getMessage()).getBuffer());
    }

}