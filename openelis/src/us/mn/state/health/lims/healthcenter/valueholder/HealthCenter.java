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

package us.mn.state.health.lims.healthcenter.valueholder;

public class HealthCenter{
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private boolean active;
    private boolean allowPatientCreation;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public HealthCenter(){
        super();
        this.setActive(true);
        this.setAllowPatientCreation(true);
    }

    public HealthCenter(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAllowPatientCreation() {
        return allowPatientCreation;
    }

    public void setAllowPatientCreation(boolean allowPatientCreation) {
        this.allowPatientCreation = allowPatientCreation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HealthCenter that = (HealthCenter) o;

        if (active != that.active) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    public boolean matches(String healthCenter) {
        if (healthCenter == null || healthCenter.trim().length() == 0)
            return false;
        return name.equals(healthCenter.trim());
    }
}
