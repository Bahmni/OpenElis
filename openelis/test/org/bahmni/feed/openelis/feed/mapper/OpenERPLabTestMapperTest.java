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

package org.bahmni.feed.openelis.feed.mapper;

import junit.framework.Assert;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Test;

public class OpenERPLabTestMapperTest {
    static final String TEST_EVENT_CONTENT = " {\"category\": \"Test\",\"uuid\": \"07a5f352-ad6e-4638-9c99-2d5af364a920\", \"list_price\": \"0.0\", \"name\": \"ECHO\",\"status\": \"active\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";

    @Test
    public void shouldMapToTestObject() throws Exception {
        OpenERPLabTestMapper labTestMapper = new OpenERPLabTestMapper();
        LabObject labObject = labTestMapper.getLabObject(new Event("tag:atomfeed.ict4h.org:05a68ba2-6764-4a31-b28e-21ddfc445e8a", TEST_EVENT_CONTENT), "1");
        Assert.assertEquals("Test",labObject.getCategory());
        Assert.assertEquals("ECHO",labObject.getName());
        Assert.assertEquals("07a5f352-ad6e-4638-9c99-2d5af364a920",labObject.getExternalId());
        Assert.assertEquals("active", labObject.getStatus());

    }

    @Test
    public void asd() {
        String sqlForAllTestsToday = "SELECT \n" +
                "sample.accession_number AS accession_number, \n" +
                "sample.collection_date AS collection_date, \n" +
                "person.first_name AS first_name, \n" +
                "person.last_name AS last_name, \n" +
                "patient_identity.identity_data AS st_number, \n" +
                "sample_source.name AS sample_source, \n" +
                "SUM(CASE WHEN  analysis.status_id IN (4, 7) THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN (16) THEN 1 ELSE 0 END) as pending_validation_count,\n" +
                "COUNT(test.id) AS total_test_count,\n" +
                "CASE WHEN document_track.report_generation_time is null THEN false ELSE true END as is_printed\n" +
                "FROM Sample AS sample\n" +
                "LEFT OUTER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id \n" +
                "LEFT  JOIN sample_source ON sample_source.id = sample.sample_source_id \n" +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id \n" +
                "INNER JOIN Person AS person ON patient.person_id = person.id \n" +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id \n" +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' \n" +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id \n" +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id \n" +
                "INNER JOIN test ON test.id = analysis.test_id\n" +
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' and document_track.parent_id is null\n" +
                "WHERE analysis.status_id IN (4, 6, 7, 16, 17) \n" +
                "GROUP BY sample.accession_number, sample.collection_date, person.first_name, person.last_name, sample_source.name, patient_identity.identity_data, document_track.report_generation_time\n" +
                "HAVING COUNT(analysis.id) > SUM(CASE WHEN  analysis.status_id IN (6) THEN 1 ELSE 0 END) and max(analysis.lastupdated) < ?\n" +
                "ORDER BY sample.accession_number DESC\n" +
                "LIMIT 1000;";
        System.out.println(sqlForAllTestsToday);

    }
}
