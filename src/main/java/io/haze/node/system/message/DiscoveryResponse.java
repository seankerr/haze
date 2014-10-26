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

import io.haze.core.Application;
import io.haze.message.Message;
import io.haze.message.writer.MessageWriter;
import io.haze.node.Node;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class DiscoveryResponse extends Message {

    /** The application. */
    private Application application;

    /**
     * Create a new {@link DiscoveryResponse} instance.
     *
     * @param application The application.
     */
    public DiscoveryResponse (Application application) {
        this.application = application;
    }

    /**
     * Write a serialized version of this {@link DiscoveryResponse} onto the {@link MessageWriter}.
     *
     * @param writer The message writer.
     */
    @Override
    public void serialize (MessageWriter writer) {
        writer.writeShort((short) "discovery".length())
              .writeBytes("discovery".getBytes())
              .writeShort((short) application.getNodeCount());

        for (Node node : application.getNodes()) {
            writer.writeShort((short) node.getNodeId())
                  .writeShort((short) node.getName().length())
                  .writeBytes(node.getName().getBytes());
        }
    }

}