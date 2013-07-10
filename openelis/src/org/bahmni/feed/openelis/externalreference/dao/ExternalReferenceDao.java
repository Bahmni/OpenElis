package org.bahmni.feed.openelis.externalreference.dao;

import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import us.mn.state.health.lims.common.dao.BaseDAO;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;

public interface ExternalReferenceDao extends BaseDAO {

    ExternalReference getData(String externalReferenceId) throws LIMSRuntimeException;

    boolean insertData(ExternalReference externalReference) throws LIMSRuntimeException;
}
