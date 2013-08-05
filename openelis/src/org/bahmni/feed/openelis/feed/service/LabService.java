package org.bahmni.feed.openelis.feed.service;

import org.bahmni.feed.openelis.feed.domain.LabObject;

public abstract class LabService {

    public void process(LabObject labObject) throws Exception {
    if (labObject.getStatus().equals("active"))
        save(labObject);
    else if (labObject.getStatus().equals("inactive"))
        inactivate(labObject);
    else if (labObject.getStatus().equals("deleted"))
        delete(labObject);
    }

    protected abstract void inactivate(LabObject labObject);

    protected abstract void delete(LabObject labObject);

    protected abstract void save(LabObject labObject) throws Exception;


}
