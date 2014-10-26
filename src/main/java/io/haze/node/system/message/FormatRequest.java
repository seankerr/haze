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

package io.haze.node.system.message;

import io.haze.message.Message;
import io.haze.message.MessageContext;
import io.haze.message.MessageFormat;
import io.haze.message.reader.MessageReader;
import io.haze.node.Node;
import io.haze.transport.TransportClient;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class FormatRequest extends Message {

    /** The format. */
    public String format;

    /**
     * Read a serialized version of {@link FormatRequest} from the {@link MessageReader}.
     *
     * @param reader The message reader.
     */
    @Override
    public Message deserialize (MessageReader reader) {
        int    length = reader.readShort();
        byte[] bytes  = new byte[length];

        reader.readBytes(bytes);

        format = new String(bytes);

        return this;
    }

    /**
     * Handle this {@link FormatRequest}.
     *
     * @param context The message context.
     * @param node    The node.
     */
    @Override
    public void handle (MessageContext context, Node node) {
        TransportClient client = context.getTransportClient();
        MessageFormat   format = client.getMessageFormat();

        if (this.format.equals("binary") && format != MessageFormat.BINARY) {
            // switch to binary
            client.setMessageFormat(MessageFormat.BINARY);

            context.setMessage(new FormatResponse("binary"));
            client.write(context);
        } else if (this.format.equals("text") && format != MessageFormat.TEXT) {
            // switch to text
            client.setMessageFormat(MessageFormat.TEXT);

            context.setMessage(new FormatResponse("text"));
            client.write(context);
        }

        if (format != client.getMessageFormat()) {
            // send a notification to all nodes
            for (Node _node : client.getFactoryContext().getApplication().getNodes()) {
                _node.handleMessageFormat(client);
            }
        }
    }

    /**
     * Retrieve the string representation of this {@link FormatRequest}.
     */
    @Override
    public String toString () {
        return String.format("<%s:'%s'>", getClass().getSimpleName(), format);
    }

}