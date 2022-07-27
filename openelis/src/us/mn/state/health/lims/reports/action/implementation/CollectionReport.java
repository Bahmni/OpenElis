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
 * Copyright (C) ITECH, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package us.mn.state.health.lims.reports.action.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.patient.util.PatientUtil;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.reports.action.implementation.reportBeans.ErrorMessages;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;

public abstract class CollectionReport implements IReportCreator {

	@Override
	public String getContentType() {
		return "application/pdf; charset=UTF-8";
	}

	protected String reportPath;
	protected BaseActionForm dynaForm;
	protected Set<String> handledOrders;

	@Override
	public String getResponseHeaderName() {
		return null;
	}

	@Override
	public String getResponseHeaderContent() {
		return null;
	}

	@Override
	public void initializeReport(BaseActionForm dynaForm) {
		handledOrders = new HashSet<String>();
		this.dynaForm = dynaForm;
	}

	@Override
	public Map<String, Object> getReportParameters() throws IllegalStateException {
		return Collections.emptyMap();
	}

	@Override
	public byte[] runReport() throws Exception {
		List<byte[]> byteList = generateReports();
		if (byteList.isEmpty()) {
			HashMap<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("SUBREPORT_DIR", reportPath);
			parameterMap.put("directorName", ConfigurationProperties.getInstance().getPropertyValue(Property.labDirectorName));
			List<ErrorMessages> errorMsgs = new ArrayList<ErrorMessages>();
			ErrorMessages msgs = new ErrorMessages();
			msgs.setMsgLine1(StringUtil.getMessageForKey("report.error.message.noPrintableItems"));
			errorMsgs.add(msgs);
			return JasperRunManager.runReportToPdf(reportPath + "NoticeOfReportError.jasper", parameterMap, new JRBeanCollectionDataSource(
					errorMsgs));
		} else {
			return merge(byteList);
		}
	}

	@Override
	public void setReportPath(String path) {
		reportPath = path;
	}

	protected byte[] merge(List<byte[]> byteList) throws DocumentException {
		byte[] outputBytes;
		OutputStream outputStream = new ByteArrayOutputStream();

		try {

			PdfCopyFields pcf = new PdfCopyFields(outputStream);
			for (byte[] bytes : byteList) {
				ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
				PdfReader pdfReader = new PdfReader(inputStream);

				// place holder in case we do have to rotate
				// if (false) {
				// int n = pdfReader.getNumberOfPages();
				// int rot;
				// PdfDictionary pageDict;
				// for (int i = 1; i <= n; i++) {
				// rot = pdfReader.getPageRotation(i);
				// pageDict = pdfReader.getPageN(i);
				// pageDict.put(PdfName.ROTATE, new PdfNumber(rot + 90));
				// }
				// }
				pcf.addDocument(pdfReader);
			}

			if (!byteList.isEmpty()) {
				pcf.close();
			}

			outputBytes = ((ByteArrayOutputStream) outputStream).toByteArray();

			return outputBytes;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return null;
	}

	protected byte[] createReport(String reportName) {
		IReportCreator reportCreator = ReportImplementationFactory.getReportCreator(reportName);

		if (reportCreator != null) {
			reportCreator.initializeReport(dynaForm);
			reportCreator.setReportPath(reportPath);

			@SuppressWarnings("unchecked")
			Map<String, Object> parameterMap = reportCreator.getReportParameters();
			parameterMap.put("SUBREPORT_DIR", reportPath);
			handledOrders.addAll(reportCreator.getReportedOrders());
			try {
				return reportCreator.runReport();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	protected Patient getPatient() {
		String patientId = dynaForm.getString("patientNumberDirect");
		return PatientUtil.getPatientByIdentificationNumber(patientId);
	}

	public List<String> getReportedOrders() {
		List<String> orderList = new ArrayList<String>();
		orderList.addAll(handledOrders);
		return orderList;
	}

	abstract protected List<byte[]> generateReports();
}
