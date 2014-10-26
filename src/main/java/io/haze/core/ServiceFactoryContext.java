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

package io.haze.core;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class ServiceFactoryContext extends FactoryContext {

    /** The service name. */
    private String name;

    /**
     * Create a new {@link ServiceFactoryContext} instance.
     *
     * @param application The application.
     * @param namespace   The configuration namespace.
     * @param name        The service name.
     */
    public ServiceFactoryContext (Application application, String namespace, String name) {
        super(application, namespace);

        this.name = name;
    }

    /**
     * Retrieve the name of the {@link Service} to be created from this {@link ServiceFactoryContext}.
     */
    public String getName () {
        return name;
    }

    /**
     * Retrieve the string representation of this {@link ServiceFactoryContext}.
     */
    public String toString () {
        return String.format("<%s:%s>", getClass().getSimpleName(), getName());
    }

}