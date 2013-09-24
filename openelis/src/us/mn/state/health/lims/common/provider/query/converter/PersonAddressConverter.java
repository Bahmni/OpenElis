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

package us.mn.state.health.lims.common.provider.query.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import us.mn.state.health.lims.address.valueholder.PersonAddress;

import java.util.List;


public class PersonAddressConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {

        List<PersonAddress> personAddresses = (List<PersonAddress>) o;
        for (int i = 0 ; i < personAddresses.size() ; i++){
            PersonAddress personAddress = personAddresses.get(i);
            writer.startNode("addressline");
            createLeafNode(writer, "index", String.valueOf(i));
            createLeafNode(writer, "value", personAddress.getValue());
            writer.endNode();
        }
    }

    private void createLeafNode(HierarchicalStreamWriter writer, String name, String value) {
        if(value != null) {
            writer.startNode(name);
            writer.setValue(value);
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return null;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return List.class.isAssignableFrom(aClass);
    }
}
