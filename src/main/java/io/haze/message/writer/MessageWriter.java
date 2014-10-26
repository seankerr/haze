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

package io.haze.message.writer;

import io.netty.buffer.ByteBuf;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public abstract class MessageWriter {

    /** The byte buffer. */
    private ByteBuf buffer;

    /**
     * Retrieve the {@link ByteBuf}.
     */
    public ByteBuf getBuffer () {
        return buffer;
    }

    /**
     * Set the {@link ByteBuf}.
     *
     * @param buffer The byte buffer.
     */
    public void setBuffer (ByteBuf buffer) {
        this.buffer = buffer;
    }

    /**
     * Write a boolean value.
     *
     * @param value The value.
     */
    public abstract MessageWriter writeBoolean (boolean value);

    /**
     * Write a byte value.
     *
     * @param value The value.
     */
    public abstract MessageWriter writeByte (byte value);

    /**
     * Write a byte array value.
     *
     * @param value The value.
     */
    public abstract MessageWriter writeBytes (byte[] value);

    /**
     * Write a char value.
     *
     * @param value The value.
     */
    public abstract MessageWriter writeChar (int value);

    /**
     * Write a double value.
     *
     * @param value The value.
     */
    public abstract MessageWriter writeDouble (double value);

    /**
     * Write a float value.
     *
     * @param value The value.
     */
    public abstract MessageWriter writeFloat (float value);

    /**
     * Write a int value.
     *
     * @param value The value.
     */
    public abstract MessageWriter writeInt (int value);

    /**
     * Write a long value.
     *
     * @param value The value.
     */
    public abstract MessageWriter writeLong (long value);

    /**
     * Write a short value.
     *
     * @param value The value.
     */
    public abstract MessageWriter writeShort (short value);

}