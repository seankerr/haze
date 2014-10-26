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
import io.haze.message.writer.MessageWriter;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class DisconnectResponse extends Message {

    /** The message. */
    private String message;

    /**
     * Create a new {@link DisconnectResponse} instance.
     *
     * @param message The message.
     */
    public DisconnectResponse (String message) {
        this.message = message;
    }

    /**
     * Retrieve the error message.
     */
    public String getMessage () {
        return message;
    }

    /**
     * Write a serialized version of this {@link DisconnectResponse} onto the {@link MessageWriter}.
     *
     * @param writer The message writer.
     */
    @Override
    public void serialize (MessageWriter writer) {
        writer.writeShort((short) "disconnect".length())
              .writeBytes("disconnect".getBytes())
              .writeInt(message.length())
              .writeBytes(message.getBytes());
    }

    /**
     * Retrieve the string representation of this {@link DisconnectResponse}.
     */
    @Override
    public String toString () {
        return String.format("<%s:'%s'>", getClass().getSimpleName(), message);
    }

}