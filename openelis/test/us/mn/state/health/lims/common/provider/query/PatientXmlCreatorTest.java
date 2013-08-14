package us.mn.state.health.lims.common.provider.query;

import com.thoughtworks.xstream.XStream;
import org.junit.Test;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.common.provider.query.converter.PersonAddressConverter;
import us.mn.state.health.lims.common.util.XMLUtil;

import java.util.ArrayList;
import java.util.List;

public class PatientXmlCreatorTest {

    public static final String TYPE = "T";

    @Test
    public void shouldCreateAddressXml() {
        List addressParts = createAddressPartsList();

        String xml = getXml("addresslines", addressParts);

        System.out.println(xml);
    }

    @Test
    public void shouldNotFailWhenAddressHasNullValues() {
        List<PersonAddress> addressParts = new ArrayList<PersonAddress>();
        addressParts.add(createPersonAddress("100", TYPE, "1", null));

        String xml = getXml("addresslines", addressParts);

        System.out.println(xml);
    }

    private String getXml(String rootNodeName, List addressParts) {
        StringBuilder xml = new StringBuilder();
        XStream xstream = new XStream();
        xstream.registerConverter(new PersonAddressConverter());
        xstream.alias(rootNodeName, List.class);

        String addressesXml = xstream.toXML(addressParts);

        XMLUtil.appendKeyValue("address", addressesXml, xml);
        return xml.toString();
    }

    private List<PersonAddress> createAddressPartsList() {
        List<PersonAddress> addressParts = new ArrayList<PersonAddress>();
        addressParts.add(createPersonAddress("100", TYPE, "1", "street"));
        addressParts.add(createPersonAddress("100", TYPE, "2", "village"));
        addressParts.add(createPersonAddress("100", TYPE, "3", "gran panchayat"));
        addressParts.add(createPersonAddress("100", TYPE, "4", "tehsil"));
        addressParts.add(createPersonAddress("100", TYPE, "5", "district"));
        addressParts.add(createPersonAddress("100", TYPE, "6", "state"));
        return addressParts;
    }

    private PersonAddress createPersonAddress(String personId, String type, String addressPartId, String value) {
        PersonAddress pa = new PersonAddress();
        pa.setPersonId(personId);
        pa.setType(type);
        pa.setAddressPartId(addressPartId);
        pa.setValue(value);
        return pa;
    }


}
