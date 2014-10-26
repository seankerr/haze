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
public class BinaryMessageWriter extends MessageWriter {

    /**
     * Create a new {@link BinaryMessageWriter} instance.
     */
    public BinaryMessageWriter () {
    }

    /**
     * Create a new {@link BinaryMessageWriter} instance.
     *
     * @param buffer The byte buffer.
     */
    public BinaryMessageWriter (ByteBuf buffer) {
        setBuffer(buffer);
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
        getBuffer().writeChar(value);

        return this;
    }

    /**
     * Write a double value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeDouble (double value) {
        getBuffer().writeDouble(value);

        return this;
    }

    /**
     * Write a float value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeFloat (float value) {
        getBuffer().writeFloat(value);

        return this;
    }

    /**
     * Write a int value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeInt (int value) {
        getBuffer().writeInt(value);

        return this;
    }

    /**
     * Write a long value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeLong (long value) {
        getBuffer().writeLong(value);

        return this;
    }

    /**
     * Write a short value.
     *
     * @param value The value.
     */
    @Override
    public MessageWriter writeShort (short value) {
        getBuffer().writeShort(value);

        return this;
    }

}