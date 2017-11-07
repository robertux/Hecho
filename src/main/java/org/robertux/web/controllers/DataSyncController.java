package org.robertux.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.model.JsonResponse;

/**
 * Created by robertux on 11/6/17.
 */
public abstract class DataSyncController {
    protected Logger logger = LogManager.getLogger(this.getClass());

    public abstract String getLogoUrl();

    public abstract String getSyncUrl();

    public abstract JsonResponse sync(String sessionId, String token);
}
