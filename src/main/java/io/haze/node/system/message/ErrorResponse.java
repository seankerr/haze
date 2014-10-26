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
public class ErrorResponse extends Message {

    /** The error code. */
    private int code = -1;

    /** The thrown exception. */
    private Throwable exception;

    /** The error message. */
    private String message;

    /**
     * Create a new {@link ErrorResponse} instance.
     *
     * @param message The error message.
     */
    public ErrorResponse (String message) {
        this.message = message;
    }

    /**
     * Create a new {@link ErrorResponse} instance.
     *
     * @param exception The thrown exception.
     */
    public ErrorResponse (Throwable exception) {
        this.exception = exception;
    }

    /**
     * Create a new {@link ErrorResponse} instance.
     *
     * @param code    The error code.
     * @param message The error message.
     */
    public ErrorResponse (int code, String message) {
        this.code    = code;
        this.message = message;
    }

    /**
     * Retrieve the error code.
     */
    public int getCode () {
        return code;
    }

    /**
     * Retrieve the exception.
     */
    public Throwable getException () {
        return exception;
    }

    /**
     * Retrieve the error message.
     */
    public String getMessage () {
        return message;
    }

    /**
     * Write a serialized version of this {@link ErrorResponse} onto the {@link MessageWriter}.
     *
     * @param writer The message writer.
     */
    @Override
    public void serialize (MessageWriter writer) {
        if (exception != null) {
            StackTraceElement[] stackTrace = exception.getStackTrace();

            writer.writeShort((short) "exception".length())
                  .writeBytes("exception".getBytes())
                  .writeInt(exception.getMessage().length())
                  .writeBytes(exception.getMessage().getBytes())
                  .writeInt(stackTrace.length);

            for (StackTraceElement element : stackTrace) {
                writer.writeShort((short) element.getFileName().length())
                      .writeBytes(element.getFileName().getBytes())

                      .writeInt(element.getLineNumber())

                      .writeShort((short) element.getClassName().length())
                      .writeBytes(element.getClassName().getBytes())

                      .writeShort((short) element.getMethodName().length())
                      .writeBytes(element.getMethodName().getBytes());
            }
        } else if (code > -1) {
            writer.writeShort((short) "errorcode".length())
                  .writeBytes("errorcode".getBytes())
                  .writeInt(code)
                  .writeInt(message.length())
                  .writeBytes(message.getBytes());
        } else {
            writer.writeShort((short) "error".length())
                  .writeBytes("error".getBytes())
                  .writeInt(message.length())
                  .writeBytes(message.getBytes());
        }
    }

    /**
     * Retrieve the string representation of this {@link ErrorResponse}.
     */
    @Override
    public String toString () {
        if (exception != null) {
            return String.format("<%s:'%s'>", getClass().getSimpleName(), exception.getMessage());
        }

        return String.format("<%s:'%s'>", getClass().getSimpleName(), message);
    }

}