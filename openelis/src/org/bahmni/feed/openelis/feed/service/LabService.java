package org.bahmni.feed.openelis.feed.service;

import org.bahmni.feed.openelis.feed.domain.LabObject;

import java.io.IOException;

public abstract class LabService {

    public void process(LabObject labObject) throws Exception {
        if (labObject.getStatus().equals("deleted"))
            delete(labObject);
        else
            save(labObject);
    }

    protected abstract void delete(LabObject labObject) throws IOException;

    protected abstract void save(LabObject labObject) throws Exception;
}
