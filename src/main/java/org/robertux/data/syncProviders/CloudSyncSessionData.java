package org.robertux.data.syncProviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by robertux on 11/15/17.
 */
public abstract class CloudSyncSessionData {
    protected Logger logger = LogManager.getLogger(this.getClass());

    protected boolean initialized = false;
    protected boolean inSync = false;

    public boolean isInitialized() {
        return initialized;
    }

    public boolean isInSync() {
        return inSync;
    }

    public void setInSync(boolean inSync) {
        this.inSync = inSync;
    }
}
