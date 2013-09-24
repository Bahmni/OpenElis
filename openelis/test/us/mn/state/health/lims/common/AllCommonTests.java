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

package us.mn.state.health.lims.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import us.mn.state.health.lims.common.provider.validation.AccessionNumberProgramValidatorTest;
import us.mn.state.health.lims.common.provider.validation.AccessionNumberSiteYearValidatorTest;
import us.mn.state.health.lims.common.provider.validation.AccessionNumberYearValidatorTest;
import us.mn.state.health.lims.common.provider.validation.PasswordValidationTest;
import us.mn.state.health.lims.common.util.DateUtilTest;

@RunWith(Suite.class)
@SuiteClasses({DateUtilTest.class,
	AccessionNumberProgramValidatorTest.class,
	AccessionNumberSiteYearValidatorTest.class,
	AccessionNumberYearValidatorTest.class,
	PasswordValidationTest.class})
public class AllCommonTests {

}
