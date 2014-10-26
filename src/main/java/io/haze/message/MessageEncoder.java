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

import io.haze.core.FactoryContext;
import io.haze.core.FactoryInitializer;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
@Sharable
public abstract class MessageEncoder extends MessageToMessageEncoder<MessageContext> implements FactoryInitializer {

    /** The factory context. */
    private FactoryContext context;

    /**
     * Retrieve the {@link FactoryContext}.
     */
    @Override
    public FactoryContext getFactoryContext () {
        return context;
    }

    /**
     * Initialize this {@link MessageEncoder}.
     */
    @Override
    public void initialize () {
    }

    /**
     * Set the {@link FactoryContext}.
     *
     * @param context The factory context.
     */
    @Override
    public void setFactoryContext (FactoryContext context) {
        this.context = context;
    }

    /**
     * Retrieve the string representation of this {@link MessageEncoder}.
     */
    @Override
    public String toString () {
        return String.format("<%s>", getClass().getSimpleName());
    }

}