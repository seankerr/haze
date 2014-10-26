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

package io.haze.node;

import io.haze.message.MessageContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public abstract class QueuedMessageNode<T> extends Node<T> {

    /** The message queue. */
    private BlockingQueue<MessageContext> queue = new LinkedBlockingQueue();

    /**
     * Retrieve the message queue.
     */
    protected BlockingQueue<MessageContext> getQueue () {
        return queue;
    }

    /**
     * Receive a {@link MessageContext}.
     *
     * @param context The message context.
     */
    @Override
    public void receiveMessage (MessageContext context)
    throws Exception {
        if (!queue.add(context)) {
            throw new Exception("Failed to add message to queue");
        }
    }

    /**
     * Run this {@link QueuedMessageNode}.
     */
    @Override
    public abstract void run ();

}