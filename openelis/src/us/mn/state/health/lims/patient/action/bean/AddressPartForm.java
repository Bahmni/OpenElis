package us.mn.state.health.lims.patient.action.bean;


public class AddressPartForm {

    private String name;
    private String type;
    private String value="";
    private String nameKey;
    private String id;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
