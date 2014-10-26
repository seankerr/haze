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
public class TextMessageWriter extends MessageWriter {

    /** The default delimiter for primitive values. */
    public static final byte DEFAULT_DELIMITER = (byte) ':';

    /** The delimiter for primitive values. */
    private byte delimiter = DEFAULT_DELIMITER;

    /**
     * Create a new {@link TextMessageWriter} instance.
     */
    public TextMessageWriter () {
    }

    /**
     * Create a new {@link TextMessageWriter} instance.
     *
     * @param buffer The byte buffer.
     */
    public TextMessageWriter (ByteBuf buffer) {
        setBuffer(buffer);
    }

    /**
     * Set the delimiter.
     *
     * @param delimiter The delimiter.
     */
    public void setDelimiter (byte delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Write a boolean value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeBoolean (boolean value) {
        getBuffer().writeByte(value ? '1' : '0');

        return this;
    }

    /**
     * Write a byte value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeByte (byte value) {
        getBuffer().writeByte(value);

        return this;
    }

    /**
     * Write a byte array value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeBytes (byte[] value) {
        getBuffer().writeBytes(value);

        return this;
    }

    /**
     * Write a char value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeChar (int value) {
        writeInt(value);

        return this;
    }

    /**
     * Write a double value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeDouble (double value) {
        getBuffer().writeBytes(String.valueOf(value).getBytes());
        getBuffer().writeByte(delimiter);

        return this;
    }

    /**
     * Write a float value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeFloat (float value) {
        getBuffer().writeBytes(String.valueOf(value).getBytes());
        getBuffer().writeByte(delimiter);

        return this;
    }

    /**
     * Write a int value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeInt (int value) {
        getBuffer().writeBytes(String.valueOf(value).getBytes());
        getBuffer().writeByte(delimiter);

        return this;
    }

    /**
     * Write a long value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeLong (long value) {
        getBuffer().writeBytes(String.valueOf(value).getBytes());
        getBuffer().writeByte(delimiter);

        return this;
    }

    /**
     * Write a short value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeShort (short value) {
        getBuffer().writeBytes(String.valueOf(value).getBytes());
        getBuffer().writeByte(delimiter);

        return this;
    }

}