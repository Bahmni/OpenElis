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

package us.mn.state.health.lims.address.valueholder;

import java.util.ArrayList;
import java.util.Collection;

public class AddressParts extends ArrayList<AddressPart> {
    public AddressParts(Collection<? extends AddressPart> c) {
        super(c);
    }

    public AddressPart findByName(String partName) {
        for (AddressPart addressPart : this) {
            if (addressPart.getPartName().equals(partName))
                return addressPart;
        }
        return null;
    }

    public AddressPart findById(String id) {
        for (AddressPart addressPart : this) {
            if (addressPart.getId().equals(id))
                return addressPart;
        }
        return null;
    }
}
