/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package us.mn.state.health.lims.healthcenter.dao;

import java.util.List;

import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;

public interface HealthCenterDAO {
    HealthCenter get(String id);
    public List<HealthCenter> getAll() throws LIMSRuntimeException;

    public List<String> getAllHealthCenterNames() throws LIMSRuntimeException;

    public List<HealthCenter> getAllActive() throws LIMSRuntimeException;
    public HealthCenter getByName(String name) throws LIMSRuntimeException;
    public void add(HealthCenter healthCenter) throws LIMSRuntimeException;
    public void update(HealthCenter healthCenter) throws LIMSRuntimeException;

    void deactivate(String centerName);
}
