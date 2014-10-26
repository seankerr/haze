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

package io.haze.message.reader;

import io.haze.message.writer.TextMessageWriter;

import io.netty.buffer.ByteBuf;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class TextMessageReader extends MessageReader {

    /** The delimiter for primitive values. */
    private byte delimiter = TextMessageWriter.DEFAULT_DELIMITER;

    /**
     * Create a new {@link TextMessageReader} instance.
     */
    public TextMessageReader () {
    }

    /**
     * Create a new {@link TextMessageReader} instance.
     *
     * @param buffer The byte buffer.
     */
    public TextMessageReader (ByteBuf buffer) {
        setBuffer(buffer);
    }

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a boolean value.
     */
    @Override
    public boolean hasBoolean () {
        return getBuffer().readableBytes() > 0;
    }

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a byte value.
     */
    @Override
    public boolean hasByte () {
        return getBuffer().readableBytes() > 0;
    }

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a byte array value.
     *
     * @param length The length of bytes.
     */
    @Override
    public boolean hasBytes (int length) {
        return length <= getBuffer().readableBytes();
    }

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a char value.
     */
    @Override
    public boolean hasChar () {
        return getBuffer().bytesBefore(delimiter) > -1;
    }

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a double value.
     */
    @Override
    public boolean hasDouble () {
        return getBuffer().bytesBefore(delimiter) > -1;
    }

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a float value.
     */
    @Override
    public boolean hasFloat () {
        return getBuffer().bytesBefore(delimiter) > -1;
    }

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read an int value.
     */
    @Override
    public boolean hasInt () {
        return getBuffer().bytesBefore(delimiter) > -1;
    }

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a long value.
     */
    @Override
    public boolean hasLong () {
        return getBuffer().bytesBefore(delimiter) > -1;
    }

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a short value.
     */
    @Override
    public boolean hasShort () {
        return getBuffer().bytesBefore(delimiter) > -1;
    }

    /**
     * Read a boolean value.
     */
    @Override
    public boolean readBoolean () {
        return getBuffer().readByte() == '1';
    }

    /**
     * Read a byte value.
     */
    @Override
    public byte readByte () {
        return getBuffer().readByte();
    }

    /**
     * Read a byte array value.
     *
     * @param destination The destination byte array.
     */
    @Override
    public void readBytes (byte[] destination) {
        getBuffer().readBytes(destination);
    }

    /**
     * Read a char value.
     */
    @Override
    public char readChar () {
        return Character.toChars(readInt())[0];
    }

    /**
     * Read a double value.
     */
    @Override
    public double readDouble () {
        try {
            byte[] bytes = new byte[getBuffer().bytesBefore((byte) ':')];

            getBuffer().readBytes(bytes);

            return Double.parseDouble(new String(bytes));
        } finally {
            // discard delimiter
            getBuffer().readByte();
        }
    }

    /**
     * Read a float value.
     */
    @Override
    public float readFloat () {
        try {
            byte[] bytes = new byte[getBuffer().bytesBefore((byte) ':')];

            getBuffer().readBytes(bytes);

            return Float.parseFloat(new String(bytes));
        } finally {
            // discard delimiter
            getBuffer().readByte();
        }
    }

    /**
     * Read a int value.
     */
    @Override
    public int readInt () {
        try {
            byte[] bytes = new byte[getBuffer().bytesBefore((byte) ':')];

            getBuffer().readBytes(bytes);

            return Integer.parseInt(new String(bytes));
        } finally {
            // discard delimiter
            getBuffer().readByte();
        }
    }

    /**
     * Read a long value.
     */
    @Override
    public long readLong () {
        try {
            byte[] bytes = new byte[getBuffer().bytesBefore((byte) ':')];

            getBuffer().readBytes(bytes);

            return Long.parseLong(new String(bytes));
        } finally {
            // discard delimiter
            getBuffer().readByte();
        }
    }

    /**
     * Read a short value.
     */
    @Override
    public short readShort () {
        try {
            byte[] bytes = new byte[getBuffer().bytesBefore((byte) ':')];

            getBuffer().readBytes(bytes);

            return Short.parseShort(new String(bytes));
        } finally {
            // discard delimiter
            getBuffer().readByte();
        }
    }

    /**
     * Set the delimiter.
     *
     * @param delimiter The delimiter.
     */
    public void setDelimiter (byte delimiter) {
        this.delimiter = delimiter;
    }

}