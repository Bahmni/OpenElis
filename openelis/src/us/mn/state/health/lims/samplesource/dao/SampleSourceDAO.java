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

package us.mn.state.health.lims.samplesource.dao;

import us.mn.state.health.lims.samplesource.valueholder.SampleSource;

import java.util.List;

public interface SampleSourceDAO {

    public List<SampleSource> getAll();
    public List<SampleSource> getAllActive();
    public SampleSource getByName(String name, boolean caseInsensitiveComparision);
    public SampleSource get(String id);
    public void add(SampleSource sampleSource);
    public void update(SampleSource sampleSource);

    void deactivate(String sampleSourceName);

}
