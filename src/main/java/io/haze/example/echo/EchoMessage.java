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

package io.haze.example.echo;

import io.haze.message.Message;
import io.haze.message.writer.MessageWriter;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class EchoMessage extends Message {

    /** The data. */
    public String data;

    /**
     * Create a new {@link EchoMessage} instance.
     *
     * @param data The data.
     */
    public EchoMessage (String data) {
        this.data = data;
    }

    /**
     * Write a serialized version of this {@link EchoMessage} onto the {@link MessageWriter}.
     *
     * @param writer The message writer.
     */
    @Override
    public void serialize (MessageWriter writer) {
        writer.writeInt(data.length())
              .writeBytes(data.getBytes());
    }

    /**
     * Retrieve the string representation of this {@link EchoMessage}.
     */
    @Override
    public String toString () {
        return String.format("<%s:'%s'>", getClass().getSimpleName(), data);
    }

}