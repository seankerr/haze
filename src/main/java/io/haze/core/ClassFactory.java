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

import java.lang.reflect.InvocationTargetException;

/**
 * TODO: Documentation.
 *
 * @author Sean Kerr [sean@code-box.org]
 */
public class ClassFactory<T> {

    /** The class. */
    private Class<T> clazz;

    /** The factory context. */
    private FactoryContext context;

    /**
     * Create a new {@link ClassFactory} instance.
     *
     * @param clazz The clazz.
     */
    public ClassFactory (Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Create a new {@link ClassFactory} instance.
     *
     * @param clazz   The clazz.
     * @param context The factory context.
     */
    public ClassFactory (Class<T> clazz, FactoryContext context) {
        this.clazz   = clazz;
        this.context = context;
    }

    /**
     * Retrieve a new class instance.
     *
     * @throws IllegalAccessException If the constructor cannot be called.
     * @throws InstantiationException If the message decoder instance could not be created.
     */
    public T newInstance ()
    throws IllegalAccessException, InstantiationException {
        T instance = clazz.newInstance();

        if (context != null && instance instanceof FactoryInitializer) {
            ((FactoryInitializer) instance).setFactoryContext(context);
        }

        return instance;
    }

    /**
     * Retrieve a new class instance.
     *
     * @param parameters The constructor parameters.
     *
     * @throws IllegalAccessException    If the constructor cannot be called.
     * @throws InstantiationException    If the message decoder instance could not be created.
     * @throws InvocationTargetException If an exception was thrown from the constructor.
     * @throws NoSuchMethodException     If a constructor with matching parameters cannot be found.
     */
    public T newInstance (Object... parameters)
    throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        Class[] types = new Class[parameters.length];

        for (int i = 0; i < types.length; i++) {
            types[i] = parameters[i].getClass();
        }

        return clazz.getConstructor(types).newInstance(parameters);
    }

}