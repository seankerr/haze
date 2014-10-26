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
public abstract class Service implements Runnable {

    /** The context. */
    private ServiceFactoryContext context;

    /** Indicates that this service is running. */
    private Boolean isRunning = false;

    /** The service id. */
    private int serviceId;

    /** The thread within which this service is running. */
    private Thread thread;

    /**
     * Create a new {@link Service} instance.
     */
    protected Service () {
        this.thread = new Thread(this);
    }

    /**
     * Block while this {@link Service} is running.
     *
     * @param sleep The sleep interval.
     */
    public void block (long sleep) {
        while (isRunning()) {
            try {
                Thread.sleep(sleep);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * Block while this {@link Service} is running.
     *
     * @param sleep The sleep interval.
     * @param loop  The service loop to execute after each loop.
     */
    public void block (long sleep, ServiceLoop loop) {
        while (isRunning()) {
            loop.loop();

            try {
                Thread.sleep(sleep);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * Retrieve the {@link ServiceFactoryContext} that was used to create this {@link Service}.
     */
    public ServiceFactoryContext getServiceFactoryContext () {
        return context;
    }

    /**
     * Retrieve the name of this {@link Service}.
     */
    public String getName () {
        return getServiceFactoryContext().getName();
    }

    /**
     * Retrieve the service id.
     */
    public int getServiceId () {
        return serviceId;
    }

    /**
     * Retrieve the {@link Thread} within which this {@link Service} is running.
     */
    protected Thread getThread () {
        return thread;
    }

    /**
     * Initialize this {@link Service}.
     */
    public void initialize () {
    }

    /**
     * Indicates that this {@link Service} is running.
     */
    public boolean isRunning () {
        synchronized (isRunning) {
            return isRunning;
        }
    }

    /**
     * Set the {@link ServiceFactoryContext}.
     *
     * @param context The context.
     */
    public void setServiceFactoryContext (ServiceFactoryContext context) {
        this.context = context;
    }

    /**
     * Set the service id.
     *
     * @param serviceId The service id.
     */
    public void setServiceId (int serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * Stop this {@link Service}.
     */
    public void stop () {
        synchronized (isRunning) {
            isRunning = false;
        }
    }

    /**
     * Start this {@link Service}.
     */
    void start () {
        if (!thread.isAlive()) {
            thread.start();

            isRunning = true;
        }
    }

    /**
     * Retrieve the string representation of this {@link Service}.
     */
    @Override
    public String toString () {
        return String.format("<%s:%s>", getClass().getSimpleName(), getServiceFactoryContext().getName());
    }

}