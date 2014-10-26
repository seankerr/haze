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
import io.haze.message.MessageContext;
import io.haze.node.Node;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class DiscoveryRequest extends Message {

    /**
     * Handle this {@link DiscoveryRequest}.
     *
     * @param context The message context.
     * @param node    The node.
     */
    @Override
    public void handle (MessageContext context, Node node) {
        context.setMessage(new DiscoveryResponse(node.getServiceFactoryContext().getApplication()));
        context.getTransportClient().write(context);
    }

}