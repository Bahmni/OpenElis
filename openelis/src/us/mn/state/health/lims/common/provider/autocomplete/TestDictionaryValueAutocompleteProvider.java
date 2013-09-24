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

package us.mn.state.health.lims.common.provider.autocomplete;

import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TestDictionaryValueAutocompleteProvider extends BaseAutocompleteProvider {

    public List processRequest(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        String dictionaryName = request.getParameter("resultValue");
        DictionaryDAOImpl dictionaryDAO = new DictionaryDAOImpl();
        return dictionaryDAO.getAllDictionarys(dictionaryName);
    }
}
