package us.mn.state.health.lims.sample.util;

import us.mn.state.health.lims.sample.bean.SampleEditItem;

import java.util.ArrayList;
import java.util.List;

public class PossibleSample {
    private String sampleType;
    private String accessionNumber;
    private List<SampleEditItem> possibleTestOrPanel = new ArrayList<>();



    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public List<SampleEditItem> getPossibleTestOrPanel() {
        return possibleTestOrPanel;
    }

    public void setPossibleTestOrPanel(List<SampleEditItem> possibleTestOrPanel) {
        this.possibleTestOrPanel = possibleTestOrPanel;
    }

    public void addPossibleTestOrPanel(SampleEditItem testOrPanel){
        this.possibleTestOrPanel.add(testOrPanel);
    }

}
