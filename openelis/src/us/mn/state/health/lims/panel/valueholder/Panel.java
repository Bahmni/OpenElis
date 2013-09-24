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
package us.mn.state.health.lims.panel.valueholder;

import us.mn.state.health.lims.common.valueholder.EnumValueItemImpl;


public class Panel extends EnumValueItemImpl {

	private static final long serialVersionUID = 1L;

	private String id;
	private String panelName;
	private String description;
	private int sortOrderInt;
	
	public Panel() {
		super();
	}
	
	public String getId() {
		return this.id;
	}

	public String getPanelName() {
		return this.panelName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	protected String getDefaultLocalizedName() {
		return panelName;
	}

	public int getSortOrderInt() {
		return sortOrderInt;
	}

	public void setSortOrderInt(int sortOrderInt) {
		this.sortOrderInt = sortOrderInt;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Panel panel = (Panel) o;

        if (description != null ? !description.equals(panel.description) : panel.description != null) return false;
        if (id != null ? !id.equals(panel.id) : panel.id != null) return false;
        if (!panelName.equals(panel.panelName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + panelName.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
