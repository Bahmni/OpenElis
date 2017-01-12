

<%@page import="us.mn.state.health.lims.common.formfields.FormFields.Field"%>
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page import="us.mn.state.health.lims.common.action.IActionConstants,
	             us.mn.state.health.lims.common.util.ConfigurationProperties,
			     us.mn.state.health.lims.common.util.ConfigurationProperties.Property,
			     us.mn.state.health.lims.common.provider.validation.AccessionNumberValidatorFactory,
			     us.mn.state.health.lims.common.provider.validation.IAccessionNumberValidator,
			     us.mn.state.health.lims.common.formfields.FormFields,
			     us.mn.state.health.lims.common.util.StringUtil,
			      us.mn.state.health.lims.common.util.IdValuePair" %>


<%@ taglib uri="/tags/struts-bean"		prefix="bean" %>
<%@ taglib uri="/tags/struts-html"		prefix="html" %>
<%@ taglib uri="/tags/struts-logic"		prefix="logic" %>
<%@ taglib uri="/tags/labdev-view"		prefix="app" %>
<%@ taglib uri="/tags/struts-tiles"     prefix="tiles" %>
<%@ taglib uri="/tags/sourceforge-ajax" prefix="ajax"%>

<bean:define id="formName"		value='<%=(String) request.getAttribute(IActionConstants.FORM_NAME)%>' />


<%!

    boolean useReferralSiteList = false;
    boolean useProviderInfo = false;

    boolean trackPayment = false;
    boolean requesterLastNameRequired = false;
    IAccessionNumberValidator accessionNumberValidator;
    boolean supportfirstNameFirst;


%>
<%
    String path = request.getContextPath();
    useReferralSiteList = FormFields.getInstance().useField(FormFields.Field.RequesterSiteList);
    useProviderInfo = FormFields.getInstance().useField(FormFields.Field.ProviderInfo);
    trackPayment = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.trackPatientPayment, "true");
    accessionNumberValidator = new AccessionNumberValidatorFactory().getValidator();
    requesterLastNameRequired = FormFields.getInstance().useField(Field.SampleEntryRequesterLastNameRequired);
    supportfirstNameFirst = FormFields.getInstance().useField(Field.FirstNameFirst);


%>

<% if( FormFields.getInstance().useField(Field.SampleEntryLabOrderTypes)) {%>
<logic:iterate indexId="index" id="orderTypes"  type="IdValuePair" name='<%=formName%>' property="orderTypes">
    <input id='<%="orderType_" + index %>'
           type="radio"
           name="orderType"
           onchange='orderTypeSelected(this);'
           value='<%=orderTypes.getId() %>' />
    <label for='<%="orderType_" + index %>' ><%=orderTypes.getValue() %></label>
</logic:iterate>
<hr/>
<% } %>

<!-- <html:button property="showHide" value="-" onclick="showHideSection(this, 'orderDisplay');" styleId="orderDisplayButton"/>
 -->
 <h2 id="orderDisplay-title"><%= StringUtil.getContextualMessageForKey("sample.entry.order.label") %>
<span class="requiredlabel">*</span></h2>

<div id="orderDisplay" style="display:block;" >
<table  style="width:90%" >

<tr>
<td>
<table width="auto">
<tr>
    <td width="20%">
        <%=StringUtil.getContextualMessageForKey("quick.entry.accession.number")%>
        :
        <span class="requiredlabel">*</span>
    </td>
    <td width="80%">
        <app:text name="<%=formName%>" property="labNo"
                  maxlength='<%= Integer.toString(accessionNumberValidator.getMaxAccessionLength())%>'
                  onchange="checkAccessionNumber(this);makeDirty();"
                  styleClass="text"
                  styleId="labNo" />

        <span class="text-gutter">
        <bean:message key="sample.entry.scanner.instructions"/>
       </span>
        <html:button property="generate"
                     styleClass="textButton"
                     onclick="getNextAccessionNumber(); setSampleFieldValid('labNo');makeDirty();" >
            <bean:message key="sample.entry.scanner.generate"/>
        </html:button>
    </td>
</tr>
<% if( FormFields.getInstance().useField(Field.UseSampleSource)){ %>
<tr>
    <td><bean:message key="sample.entry.sampleSource" />:
        <span class="requiredlabel">*</span>
    </td>
    <td>
        <html:select styleId="sampleSourceID" name="<%=formName%>" property="sampleSourceId" onchange=" makeDirty();setSave();">
            <html:option value=""></html:option>
            <html:optionsCollection name="<%=formName%>" property="sampleSourceList" label="name" value="id" />
        </html:select>
    </td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField(Field.SampleEntryUseRequestDate)){ %>
<tr>
    <td><bean:message key="sample.entry.requestDate" />:
        <span class="requiredlabel">*</span><font size="1"><bean:message key="sample.date.format" /></font></td>
    <td><html:text name='<%=formName %>'
                   property="requestDate"
                   styleId="requestDate"
                   onchange="makeDirty();checkValidEntryDate(this, 'past')"
                   onkeyup="addDateSlashes(this, event);"
                   maxlength="10"/>
</tr>
<% } %>
<tr>
    <td >
        <%= StringUtil.getContextualMessageForKey("quick.entry.received.date") %>
        :
        <span class="requiredlabel">*</span>
        <font size="1"><bean:message key="sample.date.format" />
        </font>
    </td>
    <td colspan="2">
        <app:text name="<%=formName%>"
                  property="receivedDateForDisplay"
                  onchange="checkValidEntryDate(this, 'past');makeDirty();"
                  onkeyup="addDateSlashes(this, event);"
                  maxlength="10"
                  styleClass="text"
                  styleId="receivedDateForDisplay" />

        <% if( FormFields.getInstance().useField(Field.SampleEntryUseReceptionHour)){ %>
        <bean:message key="sample.receptionTime" />:
        <html:text name="<%=formName %>" property="recievedTime" onchange="makeDirty(); checkValidTime(this)"/>

        <% } %>
    </td>
</tr>
<% if( FormFields.getInstance().useField(Field.SampleEntryNextVisitDate)){ %>
<tr>
    <td><bean:message key="sample.entry.nextVisit.date" />&nbsp;<font size="1"><bean:message key="sample.date.format" /></font>:</td>
    <td>
        <html:text name='<%= formName %>'
                   property="nextVisitDate"
                   onchange="makeDirty();checkValidEntryDate(this, 'future')"
                   onkeyup="addDateSlashes(this, event);"
                   styleId="nextVisitDate"
                   maxlength="10"/>
    </td>
</tr>
<% } %>

<% if( FormFields.getInstance().useField(Field.SampleEntryRequestingSiteSampleId)) {%>
<tr class="requestingFacilityID">
    <td >
        <bean:message key="sample.clientReference" />
        :
    </td>
    <td >
        <app:text name="<%=formName%>"
                  property="requesterSampleID"
                  styleId="requestingFacilityID"
                  size="50"
                  maxlength="50"
                  onchange="makeDirty();"/>
    </td>
    <td width="10%" >&nbsp;</td>
    <td width="45%" >&nbsp;</td>
</tr>
<% } %>
<% if( useReferralSiteList){ %>
<tr>
    <td >
        <%= StringUtil.getContextualMessageForKey("sample.entry.project.siteName") %>:
        <% if( FormFields.getInstance().useField(Field.SampleEntryReferralSiteNameRequired)) {%>
        <span class="requiredlabel">*</span>
        <% } %>
    </td>
    <td colspan="3">
        <html:select styleId="requesterId"
                     name="<%=formName%>"
                     property="referringSiteId"
                     onchange="makeDirty();siteListChanged(this);setSave();"
                     onkeyup="capitalizeValue( this.value );"
                >
            <option value=""></option>
            <html:optionsCollection name="<%=formName%>" property="referringSiteList" label="value" value="id" />
        </html:select>
    </td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField(Field.SampleEntryReferralSiteCode)){ %>
<tr>
    <td >
        <bean:message key="sample.entry.referringSite.code" />
    </td>
    <td>
        <html:text styleId="requesterCodeId"
                   name="<%=formName%>"
                   property="referringSiteCode"
                   onchange="makeDirty();setSave();">
        </html:text>
    </td>
</tr>
<% } %>
<%  if (useProviderInfo) { %>
<% if(supportfirstNameFirst) { %>
<tr>
    <td class="firstNameLabel">
    <bean:message key="sample.entry.provider.fullName"/>:
    </td>
    <td>
        <html:select name="<%=formName%>" property="providerId" styleId="providerId">
            <app:optionsCollection
                    name="<%=formName%>"
                    property="providerList"
                    label="fullName"
                    value="id"
                    allowEdits="true"
                    />
        </html:select>
    </td>
    <%--<td class="firstNameLabel">--%>
        <%--<bean:message key="sample.entry.provider.firstName"/>:--%>
    <%--</td>--%>
    <%--<td class="firstName">--%>
        <%--<html:text name="<%=formName%>"--%>
                   <%--property="providerFirstName"--%>
                   <%--styleId="providerFirstNameID"--%>
                   <%--onchange="makeDirty();"--%>
                   <%--size="30" />--%>
        <%--<%= StringUtil.getContextualMessageForKey("sample.entry.provider.name") %>:--%>
        <%--<% if(requesterLastNameRequired ){ %>--%>
        <%--<span class="requiredlabel">*</span>--%>
        <%--<% } %>--%>
        <%--<html:text name="<%=formName%>"--%>
                   <%--property="providerLastName"--%>
                   <%--styleId="providerLastNameID"--%>
                   <%--onchange="makeDirty();setSave()"--%>
                   <%--size="30" />--%>
    <%--</td>--%>
</tr>
<% } else { %>
<tr>

    <td class="lastNameLabel">
        <%= StringUtil.getContextualMessageForKey("sample.entry.provider.fullName") %>:
        <% if(requesterLastNameRequired ){ %>
        <span class="requiredlabel">*</span>
        <% } %>
    </td>
    <td class="lastName">
        <html:text name="<%=formName%>"
                   property="providerLastName"
                   styleId="providerLastNameID"
                   onchange="makeDirty();setSave()"
                   size="30" />
        <bean:message key="humansampleone.provider.firstName.short"/>:
        <html:text name="<%=formName%>"
                   property="providerFirstName"
                   styleId="providerFirstNameID"
                   onchange="makeDirty();"
                   size="30" />
    </td>
</tr>

<% } %>
<tr class="providerWorkPhoneID">
    <td >
        <%= StringUtil.getContextualMessageForKey("humansampleone.provider.workPhone") + ": " +  StringUtil.getContextualMessageForKey("humansampleone.phone.additionalFormat")%>
    </td>
    <td>
        <app:text name="<%=formName%>"
                  property="providerWorkPhone"
                  styleId="providerWorkPhoneID"
                  size="20"
                  styleClass="text"
                  onchange="makeDirty()" />
    </td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField(Field.SampleEntryProviderFax)){ %>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey("sample.entry.project.faxNumber")%>:
    </td>
    <td>
        <app:text name="<%=formName%>"
                  property="providerFax"
                  styleId="providerFaxID"
                  size="20"
                  styleClass="text"
                  onchange="makeDirty()" />
    </td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField(Field.SampleEntryProviderEmail)){ %>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey("sample.entry.project.email")%>:
    </td>
    <td>
        <app:text name="<%=formName%>"
                  property="providerEmail"
                  styleId="providerEmailID"
                  size="20"
                  styleClass="text"
                  onchange="makeDirty()" />
    </td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField(Field.SampleEntryHealthFacilityAddress)) {%>
<tr>
    <td><bean:message key="sample.entry.facility.address"/>:</td>
</tr>
<tr>
    <td>&nbsp;&nbsp;<bean:message key="sample.entry.facility.street"/>
    <td>
        <html:text name='<%=formName %>'
                   property="facilityAddressStreet"
                   styleClass="text"
                   onchange="makeDirty()"/>
    </td>
</tr>
<tr>
    <td>&nbsp;&nbsp;<bean:message key="sample.entry.facility.commune"/>:<td>
    <html:text name='<%=formName %>'
               property="facilityAddressCommune"
               styleClass="text"
               onchange="makeDirty()"/>
</td>
</tr>
<tr>
    <td><bean:message key="sample.entry.facility.phone"/>:<td>
    <html:text name='<%=formName %>'
               property="facilityPhone"
               styleClass="text"
               onchange="makeDirty()"/>
</td>
</tr>
<tr>
    <td><bean:message key="sample.entry.facility.fax"/>:<td>
    <html:text name='<%=formName %>'
               property="facilityFax"
               styleClass="text"
               onchange="makeDirty()"/>
</td>
</tr>
<% } %>
<% if( trackPayment){ %>
<tr>
    <td><bean:message key="sample.entry.patientPayment"/>: </td>
    <td>

        <html:select name="<%=formName %>" property="paymentOptionSelection" >
            <option value='' ></option>
            <logic:iterate id="optionValue" name='<%=formName%>' property="paymentOptions" type="IdValuePair" >
                <option value='<%=optionValue.getId()%>' >
                    <bean:write name="optionValue" property="value"/>
                </option>
            </logic:iterate>
        </html:select>
    </td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField(Field.SampleEntryLabOrderTypes)) {%>
<tr >
    <td><bean:message key="sample.entry.sample.period"/>:</td>
    <td>
        <html:select name="<%=formName %>"
                     property="followupPeriodOrderType"
                     onchange="makeDirty(); labPeriodChanged( this, '8' )"
                     styleId="followupLabOrderPeriodId"
                     style="display:none">
            <option value='' ></option>
            <logic:iterate id="optionValue" name='<%=formName%>' property="followupPeriodOrderTypes" type="IdValuePair" >
                <option value='<%=optionValue.getId()%>' >
                    <bean:write name="optionValue" property="value"/>
                </option>
            </logic:iterate>
        </html:select>
        <html:select name="<%=formName %>"
                     property="initialPeriodOrderType"
                     onchange="makeDirty(); labPeriodChanged( this, '2' )"
                     styleId="initialLabOrderPeriodId"
                     style="display:none">
            <option value='' ></option>
            <logic:iterate id="optionValue" name='<%=formName%>' property="initialPeriodOrderTypes" type="IdValuePair" >
                <option value='<%=optionValue.getId()%>' >
                    <bean:write name="optionValue" property="value"/>
                </option>
            </logic:iterate>
        </html:select>
        &nbsp;
        <html:text name='<%= formName %>'
                   property="otherPeriodOrder"
                   styleId="labOrderPeriodOtherId"
                   style="display:none" />
    </td>
</tr>
<% } %>
</table>
</td>
</tr>
</table>
</div>