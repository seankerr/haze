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

package io.haze.message;

import io.haze.message.reader.MessageReader;
import io.haze.message.writer.MessageWriter;
import io.haze.node.Node;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public abstract class Message {

    /**
     * Read a serialized version of {@link Message} from the {@link MessageReader}.
     *
     * @param reader The message reader.
     */
    public Message deserialize (MessageReader reader) {
        return this;
    }

    /**
     * Handle this {@link Message}.
     *
     * @param context The message context.
     * @param node    The node.
     */
    public void handle (MessageContext context, Node node) {
    }

    /**
     * Write a serialized version of this {@link Message} onto the {@link MessageWriter}.
     *
     * @param writer The message writer.
     */
    public void serialize (MessageWriter writer) {
    }

    /**
     * Retrieve the string representation of this {@link Message}.
     */
    @Override
    public String toString () {
        return String.format("<%s>", getClass().getSimpleName());
    }

}