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

package us.mn.state.health.lims.common.provider.query;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.validator.GenericValidator;
import us.mn.state.health.lims.address.dao.AddressPartDAO;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.AddressParts;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.common.provider.query.converter.PersonAddressConverter;
import us.mn.state.health.lims.common.util.XMLUtil;
import us.mn.state.health.lims.patient.util.PatientUtil;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.patienttype.dao.PatientPatientTypeDAO;
import us.mn.state.health.lims.patienttype.daoimpl.PatientPatientTypeDAOImpl;
import us.mn.state.health.lims.patienttype.valueholder.PatientType;
import us.mn.state.health.lims.person.valueholder.Person;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PatientXmlCreator {

    private String ADDRESS_PART_DEPT_ID;
    private String ADDRESS_PART_COMMUNE_ID;
    private String ADDRESS_PART_VILLAGE_ID;
    private PatientPatientTypeDAO patientPatientTypeDAO;
    private PersonAddressDAO personAddressDAO;
    private AddressPartDAO addressPartDAO;

    public PatientXmlCreator() {
        addressPartDAO = new AddressPartDAOImpl();
        patientPatientTypeDAO = new PatientPatientTypeDAOImpl();
        personAddressDAO = new PersonAddressDAOImpl();
    }

    public void createXml(Patient patient, StringBuilder xml) {
        List<AddressPart> addressPartList = addressPartDAO.getAll();

        for( AddressPart addressPart : addressPartList){
            if( "department".equals(addressPart.getPartName())){
                ADDRESS_PART_DEPT_ID = addressPart.getId();
            }else if( "commune".equals(addressPart.getPartName())){
                ADDRESS_PART_COMMUNE_ID = addressPart.getId();
            }else if( "village".equals(addressPart.getPartName())){
                ADDRESS_PART_VILLAGE_ID = addressPart.getId();
            }
        }

        Person person = patient.getPerson();

        PatientIdentityTypeMap identityMap = PatientIdentityTypeMap.getInstance();

        List<PatientIdentity> identityList = PatientUtil.getIdentityListForPatient(patient.getId());

        XMLUtil.appendKeyValue("ID", patient.getId(), xml);
        XMLUtil.appendKeyValue("nationalID", patient.getNationalId(), xml);
        XMLUtil.appendKeyValue("ST_ID", identityMap.getIdentityValue(identityList, "ST"), xml);
        XMLUtil.appendKeyValue("subjectNumber", identityMap.getIdentityValue(identityList, "SUBJECT"), xml);
        XMLUtil.appendKeyValue("lastName", getLastNameForResponse(person), xml);
        XMLUtil.appendKeyValue("firstName", person.getFirstName(), xml);
        XMLUtil.appendKeyValue("middleName", person.getMiddleName(), xml);
        XMLUtil.appendKeyValue("mother", identityMap.getIdentityValue(identityList, "MOTHER"), xml);
        XMLUtil.appendKeyValue("aka", identityMap.getIdentityValue(identityList, "AKA"), xml);
        XMLUtil.appendKeyValue("street", person.getStreetAddress(), xml);
        XMLUtil.appendKeyValue("city", getAddress(person, ADDRESS_PART_VILLAGE_ID), xml);
        XMLUtil.appendKeyValue("birthplace", patient.getBirthPlace(), xml);
        XMLUtil.appendKeyValue("faxNumber", person.getFax(), xml);
        XMLUtil.appendKeyValue("phoneNumber", person.getHomePhone(), xml);
        XMLUtil.appendKeyValue("email", person.getEmail(), xml);
        XMLUtil.appendKeyValue("gender", patient.getGender(), xml);
        XMLUtil.appendKeyValue("patientType", getPatientType(patient), xml);
        XMLUtil.appendKeyValue("insurance", identityMap.getIdentityValue(identityList, "INSURANCE"),xml);
        XMLUtil.appendKeyValue("occupation", identityMap.getIdentityValue(identityList, "OCCUPATION"), xml);
        XMLUtil.appendKeyValue("dob", patient.getBirthDateForDisplay(), xml);
        XMLUtil.appendKeyValue("commune", getAddress(person, ADDRESS_PART_COMMUNE_ID), xml);
        XMLUtil.appendKeyValue("addressDept", getAddress(person, ADDRESS_PART_DEPT_ID), xml);
        XMLUtil.appendKeyValue("motherInitial", identityMap.getIdentityValue(identityList, "MOTHERS_INITIAL"), xml);
        XMLUtil.appendKeyValue("externalID", patient.getExternalId(), xml);
        XMLUtil.appendKeyValue("education", identityMap.getIdentityValue(identityList, "EDUCATION"), xml);
        XMLUtil.appendKeyValue("maritialStatus", identityMap.getIdentityValue(identityList, "MARITIAL"), xml);
        XMLUtil.appendKeyValue("nationality", identityMap.getIdentityValue(identityList, "NATIONALITY"), xml);
        XMLUtil.appendKeyValue("otherNationality", identityMap.getIdentityValue(identityList, "OTHER NATIONALITY"), xml);
        XMLUtil.appendKeyValue("healthDistrict", identityMap.getIdentityValue(identityList, "HEALTH DISTRICT"), xml);
        XMLUtil.appendKeyValue("healthRegion", identityMap.getIdentityValue(identityList, "HEALTH REGION"), xml);
        XMLUtil.appendKeyValue("primaryRelative", identityMap.getIdentityValue(identityList, "PRIMARYRELATIVE"), xml);

        List<PersonAddress> addressLines = personAddressDAO.getAddressPartsByPersonId(person.getId());
        String addressPartsXML = createAddressPartsXML(addressLines, addressPartList);
        XMLUtil.appendKeyValue("address", addressPartsXML, xml);

        if (patient.getLastupdated() != null) {
            String updateAsString = patient.getLastupdated().toString();
            XMLUtil.appendKeyValue("patientUpdated", updateAsString, xml);
        }

        if (person.getLastupdated() != null) {
            String updateAsString = person.getLastupdated().toString();
            XMLUtil.appendKeyValue("personUpdated", updateAsString, xml);
        }

    }


    private String createAddressPartsXML(List<PersonAddress> addressLines, List<AddressPart> addressPartList){
        SortAddressByDisplayOrder(addressLines, addressPartList);
        XStream xstream = new XStream();
        xstream.registerConverter(new PersonAddressConverter());
        xstream.alias("addresslines", List.class);
        return xstream.toXML(addressLines);
    }

    private void SortAddressByDisplayOrder(List<PersonAddress> addressLines, List<AddressPart> addressPartList) {
        final AddressParts addressParts = new AddressParts(addressPartList);
        Collections.sort(addressLines, new Comparator<PersonAddress>() {
            @Override
            public int compare(PersonAddress address1, PersonAddress address2) {
                AddressPart address1Part = addressParts.findById(address1.getAddressPartId());
                AddressPart address2Part = addressParts.findById(address2.getAddressPartId());
                return address1Part.getDisplayOrder().compareTo(address2Part.getDisplayOrder());
            }
        });
    }

    /**
     * Fake the unknown patient by never return whatever happens to be in last name field.
     * @param person
     * @return
     */
    private String getLastNameForResponse(Person person) {
        if (PatientUtil.getUnknownPerson().getId().equals(person.getId())) {
            return null;
        } else {
            return person.getLastName();
        }
    }

    private String getPatientType(Patient patient) {

        PatientType patientType = patientPatientTypeDAO.getPatientTypeForPatient(patient.getId());

        return patientType != null ? patientType.getType() : null;
    }

    private String getAddress(Person person, String addressPartId) {
        if (GenericValidator.isBlankOrNull(addressPartId)) {
            return "";
        }
        PersonAddress address = personAddressDAO.getByPersonIdAndPartId( person.getId(), addressPartId);

        return address != null ? address.getValue() : "";
    }
}
