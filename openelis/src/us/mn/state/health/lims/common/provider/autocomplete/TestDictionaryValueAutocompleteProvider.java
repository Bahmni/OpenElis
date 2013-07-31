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
