package us.mn.state.health.lims.samplesource.valueholder;

public class SampleSource {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private boolean active;

    public SampleSource(){
        super();
        this.setActive(true);
    }

    public SampleSource(String name, String description) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SampleSource that = (SampleSource) o;

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

}
