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
 *
 * Contributor(s): CIRG, University of Washington, Seattle WA.
 */
package us.mn.state.health.lims.patient.action;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.hibernate.StaleObjectStateException;
import us.mn.state.health.lims.address.dao.AddressPartDAO;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.common.exception.LIMSInvalidSTNumberException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.exception.LIMSValidationException;
import us.mn.state.health.lims.common.formfields.FormFields;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.patient.action.bean.AddressPartForm;
import us.mn.state.health.lims.patient.action.bean.AddressParts;
import us.mn.state.health.lims.patient.action.bean.PatientManagmentInfo;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.patienttype.dao.PatientPatientTypeDAO;
import us.mn.state.health.lims.patienttype.daoimpl.PatientPatientTypeDAOImpl;
import us.mn.state.health.lims.patienttype.util.PatientTypeMap;
import us.mn.state.health.lims.patienttype.valueholder.PatientPatientType;
import us.mn.state.health.lims.person.dao.PersonDAO;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PatientManagementUpdateAction extends BaseAction implements IPatientUpdate {

    protected Patient patient;
    protected Person person;
    private List<PatientIdentity> patientIdentities;
    private String patientID = "";
    private Boolean initializeFormOnSave = true;
    private static PatientIdentityDAO identityDAO = new PatientIdentityDAOImpl();
    private static PatientDAO patientDAO = new PatientDAOImpl();
    private static PersonAddressDAO personAddressDAO = new PersonAddressDAOImpl();

    protected PatientUpdateStatus patientUpdateStatus = PatientUpdateStatus.NO_ACTION;

    private static String ADDRESS_PART_VILLAGE_ID;
    private static String ADDRESS_PART_COMMUNE_ID;
    private static String ADDRESS_PART_DEPT_ID;

    public static enum PatientUpdateStatus {
        NO_ACTION, UPDATE, ADD
    }

    ;

    static {
        AddressPartDAO addressPartDAO = new AddressPartDAOImpl();
        List<AddressPart> partList = addressPartDAO.getAll();

        for (AddressPart addressPart : partList) {
            if ("department".equals(addressPart.getPartName())) {
                ADDRESS_PART_DEPT_ID = addressPart.getId();
            } else if ("commune".equals(addressPart.getPartName())) {
                ADDRESS_PART_COMMUNE_ID = addressPart.getId();
            } else if ("village".equals(addressPart.getPartName())) {
                ADDRESS_PART_VILLAGE_ID = addressPart.getId();
            }
        }
    }

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String forward = FWD_SUCCESS;

        BaseActionForm dynaForm = (BaseActionForm) form;
        PatientManagmentInfo patientInfo = (PatientManagmentInfo) dynaForm.get("patientProperties");
        setPatientUpdateStatus(patientInfo);

        if (patientUpdateStatus != PatientUpdateStatus.NO_ACTION) {

            ActionMessages errors;

            errors = preparePatientData(mapping, request, patientInfo);

            if (errors.size() > 0) {
                return mapping.findForward(FWD_FAIL);
            }


            try {
                persistPatientData(patientInfo, request.getContextPath());
            } catch (LIMSInvalidSTNumberException e) {
                request.setAttribute(IActionConstants.REQUEST_FAILED, true);
                return addErrorMessageAndForward(mapping, request, new ActionError("errors.InvalidStNumber", null, null));
            } catch (LIMSValidationException e) {
                request.setAttribute(IActionConstants.REQUEST_FAILED, true);
                return addErrorMessageAndForward(mapping, request, new ActionError(e.getMessage(), null, null));
            } catch (LIMSDuplicateRecordException e) {
                request.setAttribute(IActionConstants.REQUEST_FAILED, true);
                return addErrorMessageAndForward(mapping, request, new ActionError("errors.duplicate.STNumber", null, null));
            } catch (LIMSRuntimeException lre) {
                request.setAttribute(IActionConstants.REQUEST_FAILED, true);
                if (lre.getException() instanceof StaleObjectStateException) {
                    return addErrorMessageAndForward(mapping, request, new ActionError("errors.OptimisticLockException", null, null));
                } else {
                    return addErrorMessageAndForward(mapping, request, new ActionError("errors.UpdateException", null, null));
                }
            }

            if (initializeFormOnSave) {
                dynaForm.initialize(mapping);
            }
        }

        setSuccessFlag(request, forward);

        return mapping.findForward(forward);
    }

    private ActionForward addErrorMessageAndForward(ActionMapping mapping, HttpServletRequest request, ActionError error) {
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        saveErrors(request, errors);
        request.setAttribute(Globals.ERROR_KEY, errors);
        request.setAttribute(ALLOW_EDITS_KEY, "false");
        return mapping.findForward(FWD_FAIL);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * us.mn.state.health.lims.patient.action.IPatientUpdate#preparePatientData
     * (org.apache.struts.action.ActionMapping,
     * javax.servlet.http.HttpServletRequest,
     * us.mn.state.health.lims.common.action.BaseActionForm)
     */
    public ActionMessages preparePatientData(ActionMapping mapping, HttpServletRequest request, PatientManagmentInfo patientInfo)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        if (currentUserId == null) {
            currentUserId = getSysUserId(request);
        }

        ActionMessages errors = new ActionMessages();// dynaForm.validate(mapping,
        // request);

        initMembers();

        if (patientUpdateStatus == PatientUpdateStatus.UPDATE) {
            loadForUpdate(patientInfo);
        }

        copyFormBeanToValueHolders(patientInfo);

        setSystemUserID();

        setLastUpdatedTimeStamps(patientInfo);

        return errors;
    }

    private void setLastUpdatedTimeStamps(PatientManagmentInfo patientInfo) {
        String patientUpdate = patientInfo.getPatientLastUpdated();
        if (!GenericValidator.isBlankOrNull(patientUpdate)) {
            Timestamp timeStamp = Timestamp.valueOf(patientUpdate);
            patient.setLastupdated(timeStamp);
        }

        String personUpdate = patientInfo.getPersonLastUpdated();
        if (!GenericValidator.isBlankOrNull(personUpdate)) {
            Timestamp timeStamp = Timestamp.valueOf(personUpdate);
            person.setLastupdated(timeStamp);
        }
    }

    private void initMembers() {
        patient = new Patient();
        person = new Person();
        patientIdentities = new ArrayList<PatientIdentity>();
    }

    private void loadForUpdate(PatientManagmentInfo patientInfo) {

        patientID = patientInfo.getPatientPK();
        patient = patientDAO.readPatient(patientID);
        person = patient.getPerson();

        patientIdentities = identityDAO.getPatientIdentitiesForPatient(patient.getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * us.mn.state.health.lims.patient.action.IPatientUpdate#setPatientUpdateStatus
     * (us.mn.state.health.lims.common.action.BaseActionForm)
     */
    public void setPatientUpdateStatus(PatientManagmentInfo patientInfo) {

        String status = patientInfo.getPatientProcessingStatus();

        if (status.equals("noAction")) {
            patientUpdateStatus = PatientUpdateStatus.NO_ACTION;
        } else if (status.equals("update")) {
            patientUpdateStatus = PatientUpdateStatus.UPDATE;
        } else {
            patientUpdateStatus = PatientUpdateStatus.ADD;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * us.mn.state.health.lims.patient.action.IPatientUpdate#getPatientUpdateStatus
     * ()
     */
    public PatientUpdateStatus getPatientUpdateStatus() {
        return patientUpdateStatus;
    }

    private void copyFormBeanToValueHolders(PatientManagmentInfo patientInfo) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        PropertyUtils.copyProperties(patient, patientInfo);
        PropertyUtils.copyProperties(person, patientInfo);
    }

    private void setSystemUserID() {
        patient.setSysUserId(currentUserId);
        person.setSysUserId(currentUserId);

        for (PatientIdentity identity : patientIdentities) {
            identity.setSysUserId(currentUserId);
        }
    }

    public void persistPatientData(PatientManagmentInfo patientInfo, String contextPath) throws LIMSRuntimeException {
        PersonDAO personDAO = new PersonDAOImpl();

        if (patientUpdateStatus == PatientUpdateStatus.ADD) {
            personDAO.insertData(person);
        } else if (patientUpdateStatus == PatientUpdateStatus.UPDATE) {
            personDAO.updateData(person);
        }

        patient.setPerson(person);
        String uuid = UUID.randomUUID().toString();
        if (patientUpdateStatus == PatientUpdateStatus.ADD) {
            patient.setUuid(uuid);
            patientDAO.insertData(patient);
        } else if (patientUpdateStatus == PatientUpdateStatus.UPDATE) {
            patientDAO.updateData(patient);
        }

        persistPatientRelatedInformation(patientInfo, patient);
        patientID = patient.getId();
    }

    protected void persistPatientRelatedInformation(PatientManagmentInfo patientInfo, Patient patient) {
        persistIndentityTypes(patientInfo);
        if (FormFields.getInstance().useField(FormFields.Field.DynamicAddress)) {
            persistPatientAddressInfo(patientInfo, person);

        } else {
            persistExtraPatientAddressInfo(patientInfo);
        }
        persistPatientType(patientInfo);
    }

    protected void persistIndentityTypes(PatientManagmentInfo patientInfo) {

        persistIdentityType(patientInfo.getSTnumber(), "ST");
        persistIdentityType(patientInfo.getMothersName(), "MOTHER");
        persistIdentityType(patientInfo.getAka(), "AKA");
        persistIdentityType(patientInfo.getInsuranceNumber(), "INSURANCE");
        persistIdentityType(patientInfo.getOccupation(), "OCCUPATION");
        persistIdentityType(patientInfo.getSubjectNumber(), "SUBJECT");
        persistIdentityType(patientInfo.getMothersInitial(), "MOTHERS_INITIAL");
        persistIdentityType(patientInfo.getEducation(), "EDUCATION");
        persistIdentityType(patientInfo.getMaritialStatus(), "MARITIAL");
        persistIdentityType(patientInfo.getNationality(), "NATIONALITY");
        persistIdentityType(patientInfo.getHealthDistrict(), "HEALTH DISTRICT");
        persistIdentityType(patientInfo.getHealthRegion(), "HEALTH REGION");
        persistIdentityType(patientInfo.getOtherNationality(), "OTHER NATIONALITY");
        persistIdentityType(patientInfo.getPrimaryRelative(), "primaryRelative");
    }

    private void persistPatientAddressInfo(PatientManagmentInfo patientInfo, Person person) {
        deleteExistingAddress(person);
        addNewAddress(patientInfo, person);
    }

    private void addNewAddress(PatientManagmentInfo patientInfo, Person person) {
        AddressParts addressParts = patientInfo.getAddressParts();
        List<AddressPartForm> addressPartForms = addressParts.getAddressPartForms();

        for (AddressPartForm addressPartForm : addressPartForms) {
            PersonAddress personAddress = createPersonAddress(person, addressPartForm);
            personAddressDAO.insert(personAddress);
        }
    }

    private PersonAddress createPersonAddress(Person person, AddressPartForm addressPartForm) {
        PersonAddress personAddress = new PersonAddress();
        personAddress.setAddressPartId(addressPartForm.getId());
        personAddress.setType(addressPartForm.getType());
        personAddress.setPersonId(person.getId());
        personAddress.setValue(addressPartForm.getValue());
        personAddress.setSysUserId(currentUserId);
        return personAddress;
    }

    private void deleteExistingAddress(Person person) {
        personAddressDAO.deleteAddressOfPerson(person);
    }


    private void persistExtraPatientAddressInfo(PatientManagmentInfo patientInfo) {
        PersonAddress village = null;
        PersonAddress commune = null;
        PersonAddress dept = null;
        List<PersonAddress> personAddressList = personAddressDAO.getAddressPartsByPersonId(person.getId());

        for (PersonAddress address : personAddressList) {
            if (address.getAddressPartId().equals(ADDRESS_PART_COMMUNE_ID)) {
                commune = address;
                commune.setValue(patientInfo.getCommune());
                commune.setSysUserId(currentUserId);
                personAddressDAO.update(commune);
            } else if (address.getAddressPartId().equals(ADDRESS_PART_VILLAGE_ID)) {
                village = address;
                village.setValue(patientInfo.getCity());
                village.setSysUserId(currentUserId);
                personAddressDAO.update(village);
            } else if (address.getAddressPartId().equals(ADDRESS_PART_DEPT_ID)) {
                dept = address;
                if (!GenericValidator.isBlankOrNull(patientInfo.getAddressDepartment()) && !patientInfo.getAddressDepartment().equals("0")) {
                    dept.setValue(patientInfo.getAddressDepartment());
                    dept.setType("D");
                    dept.setSysUserId(currentUserId);
                    personAddressDAO.update(dept);
                }
            }
        }

        if (commune == null) {
            insertNewPatientInfo(ADDRESS_PART_COMMUNE_ID, patientInfo.getCommune(), "T");
        }

        if (village == null) {
            insertNewPatientInfo(ADDRESS_PART_VILLAGE_ID, patientInfo.getCity(), "T");
        }

        if (dept == null && patientInfo.getAddressDepartment() != null && !patientInfo.getAddressDepartment().equals("0")) {
            insertNewPatientInfo(ADDRESS_PART_DEPT_ID, patientInfo.getAddressDepartment(), "D");
        }
    }

    private void insertNewPatientInfo(String partId, String value, String type) {
        PersonAddress address;
        address = new PersonAddress();
        address.setPersonId(person.getId());
        address.setAddressPartId(partId);
        address.setType(type);
        address.setValue(value);
        address.setSysUserId(currentUserId);
        personAddressDAO.insert(address);
    }

    public void persistIdentityType(String paramValue, String type) throws LIMSRuntimeException {

        Boolean newIdentityNeeded = true;
        String typeID = PatientIdentityTypeMap.getInstance().getIDForType(type);

        if (patientUpdateStatus == PatientUpdateStatus.UPDATE) {

            for (PatientIdentity listIdentity : patientIdentities) {
                if (listIdentity.getIdentityTypeId().equals(typeID)) {

                    newIdentityNeeded = false;

                    if ((listIdentity.getIdentityData() == null && !GenericValidator.isBlankOrNull(paramValue))
                            || (listIdentity.getIdentityData() != null && !listIdentity.getIdentityData().equals(paramValue))) {
                        listIdentity.setIdentityData(paramValue);
                        identityDAO.updateData(listIdentity);
                    }

                    break;
                }
            }
        }

        if (newIdentityNeeded && !GenericValidator.isBlankOrNull(paramValue)) {
            // either a new patient or a new identity item
            PatientIdentity identity = new PatientIdentity();
            identity.setPatientId(patient.getId());
            identity.setIdentityTypeId(typeID);
            identity.setSysUserId(currentUserId);
            identity.setIdentityData(paramValue);
            identity.setLastupdatedFields();
            identityDAO.insertData(identity);
        }
    }

    protected void persistPatientType(PatientManagmentInfo patientInfo) {

        PatientPatientTypeDAO patientPatientTypeDAO = new PatientPatientTypeDAOImpl();

        String typeName = null;

        try {
            typeName = patientInfo.getPatientType();
        } catch (Exception ignored) {
        }

        if (!GenericValidator.isBlankOrNull(typeName) && !typeName.equals("0")) {
            String typeID = PatientTypeMap.getInstance().getIDForType(typeName);

            PatientPatientType patientPatientType = patientPatientTypeDAO.getPatientPatientTypeForPatient(patient.getId());

            if (patientPatientType == null) {
                patientPatientType = new PatientPatientType();
                patientPatientType.setSysUserId(currentUserId);
                patientPatientType.setPatientId(patient.getId());
                patientPatientType.setPatientTypeId(typeID);
                patientPatientTypeDAO.insertData(patientPatientType);
            } else {
                patientPatientType.setSysUserId(currentUserId);
                patientPatientType.setPatientTypeId(typeID);
                patientPatientTypeDAO.updateData(patientPatientType);
            }
        }
    }


    @Override
    protected String getPageTitleKey() {
        return "patient.management.title";
    }

    @Override
    protected String getPageSubtitleKey() {
        return "patient.management.title";
    }

    public String getPatientId(BaseActionForm dynaForm) {
        return GenericValidator.isBlankOrNull(patientID) ? (String) dynaForm.getString("patientPK") : patientID;
    }
}
