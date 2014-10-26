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

import io.netty.buffer.ByteBuf;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public abstract class MessageReader {

    /** The byte buffer. */
    private ByteBuf buffer;

    /**
     * Retrieve the {@link ByteBuf}.
     */
    public ByteBuf getBuffer () {
        return buffer;
    }

    /**
     * Retrieve the number of bytes left in the buffer.
     */
    public int getBytesLeft () {
        return getBuffer().readableBytes();
    }

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a boolean value.
     */
    public abstract boolean hasBoolean ();

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a byte value.
     */
    public abstract boolean hasByte ();

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a byte array value.
     *
     * @param length The length of bytes.
     */
    public abstract boolean hasBytes (int length);

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a char value.
     */
    public abstract boolean hasChar ();

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a double value.
     */
    public abstract boolean hasDouble ();

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a float value.
     */
    public abstract boolean hasFloat ();

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read an int value.
     */
    public abstract boolean hasInt ();

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a long value.
     */
    public abstract boolean hasLong ();

    /**
     * Indicates whether or not the {@link ByteBuf} has enough readable bytes to read a short value.
     */
    public abstract boolean hasShort ();

    /**
     * Search for a byte value within the {@link ByteBuf} and return the position. If the byte value is not found,
     * -1 is returned.
     *
     * @param value The byte to search for.
     */
    public int indexOf (byte value) {
        return getBuffer().bytesBefore(value) + 1;
    }

    /**
     * Read a boolean value.
     */
    public abstract boolean readBoolean ();

    /**
     * Read a byte value.
     */
    public abstract byte readByte ();

    /**
     * Read a byte array value.
     *
     * @param destination The destination byte array.
     */
    public abstract void readBytes (byte[] destination);

    /**
     * Read a char value.
     */
    public abstract char readChar ();

    /**
     * Read a double value.
     */
    public abstract double readDouble ();

    /**
     * Read a float value.
     */
    public abstract float readFloat ();

    /**
     * Read a int value.
     */
    public abstract int readInt ();

    /**
     * Read a long value.
     */
    public abstract long readLong ();

    /**
     * Read a short value.
     */
    public abstract short readShort ();

    /**
     * Set the {@link ByteBuf}.
     *
     * @param buffer The byte buffer.
     */
    public void setBuffer (ByteBuf buffer) {
        this.buffer = buffer;
    }

}