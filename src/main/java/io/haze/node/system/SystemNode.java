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

package io.haze.node.system;

import io.haze.message.Message;
import io.haze.message.MessageContext;
import io.haze.message.reader.MessageReader;
import io.haze.node.Node;
import io.haze.node.system.message.DisconnectRequest;
import io.haze.node.system.message.DiscoveryRequest;
import io.haze.node.system.message.FormatRequest;
import io.haze.node.system.message.ShutdownRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class SystemNode extends Node<String> {

    /** The hard-wired node id. */
    public static final int NODE_ID = 0;

    /** The logger. */
    private Logger logger = LoggerFactory.getLogger(SystemNode.class);

    /**
     * Build a {@link Message} from a {@link MessageReader}.
     *
     * @param reader The message reader.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public Message buildMessage (MessageReader reader)
    throws Exception {
        // get the command
        short  length = reader.readShort();
        byte[] bytes  = new byte[length];

        reader.readBytes(bytes);

        // lookup the proper message, deserialize it and return it
        return deserializeMessage(reader, new String(bytes));
    }

    /**
     * Initialize this {@link SystemNode}.
     */
    @Override
    public void initialize () {
        registerMessageClass("disconnect", DisconnectRequest.class);
        registerMessageClass("discovery", DiscoveryRequest.class);
        registerMessageClass("format", FormatRequest.class);
        registerMessageClass("shutdown", ShutdownRequest.class);
    }

    /**
     * Receive a {@link MessageContext}.
     *
     * @param context The message context.
     *
     * @throws Exception If an error has occurred.
     */
    @Override
    public void receiveMessage (MessageContext context)
    throws Exception {
        context.getMessage().handle(context, this);
    }

}