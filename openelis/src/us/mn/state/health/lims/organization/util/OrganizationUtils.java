package us.mn.state.health.lims.organization.util;

import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.organization.dao.OrganizationDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.organization.valueholder.Organization;

import java.util.ArrayList;
import java.util.List;

public class OrganizationUtils {
    private static final String REFERRAL_LAB = "referralLab";

    public static List<IdValuePair> getReferralOrganizations() {
        List<IdValuePair> pairs = new ArrayList<>();

        OrganizationDAO orgDAO = new OrganizationDAOImpl();
        List<Organization> orgs = orgDAO.getOrganizationsByTypeName("organizationName", REFERRAL_LAB);
        pairs.add(new IdValuePair("0", ""));

        for (Organization org : orgs) {
            pairs.add(new IdValuePair(org.getId(), org.getOrganizationName()));
        }

        return pairs;
    }
}
