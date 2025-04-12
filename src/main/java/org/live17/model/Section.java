package org.live17.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Section {
    private final String sectionID;
    private List<Stream> sectionData;
    private String lokalisedKey;
    private Boolean mlDynamicLabel;
    private String labelID;

    @JsonCreator
    public Section(
            @JsonProperty("sectionID") String sectionID,
            @JsonProperty("sectionData") List<Stream> sectionData) {
        if (sectionID == null || sectionID.trim().isEmpty()) {
            throw new IllegalArgumentException("SectionID cannot be null or empty");
        }
        if (sectionData == null) {
            throw new IllegalArgumentException("SectionData cannot be null");
        }
        this.sectionID = sectionID;
        this.sectionData = new ArrayList<>(sectionData);
    }

    public String getSectionID() {
        return sectionID;
    }

    public List<Stream> getSectionData() {
        return new ArrayList<>(sectionData);
    }

    public void setSectionData(List<Stream> sectionData) {
        if (sectionData == null) {
            throw new IllegalArgumentException("SectionData cannot be null");
        }
        this.sectionData = new ArrayList<>(sectionData);
    }

    public String getLokalisedKey() {
        return lokalisedKey;
    }

    @JsonProperty("lokalisedKey")
    public void setLokalisedKey(String lokalisedKey) {
        this.lokalisedKey = lokalisedKey;
    }

    public Boolean isMlDynamicLabel() {
        return mlDynamicLabel != null ? mlDynamicLabel : false;
    }

    @JsonProperty("mlDynamicLabel")
    public void setMlDynamicLabel(Boolean mlDynamicLabel) {
        this.mlDynamicLabel = mlDynamicLabel;
    }

    public String getLabelID() {
        return labelID;
    }

    @JsonProperty("labelID")
    public void setLabelID(String labelID) {
        this.labelID = labelID;
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionID='" + sectionID + '\'' +
                ", sectionData=" + sectionData +
                ", lokalisedKey='" + lokalisedKey + '\'' +
                ", mlDynamicLabel=" + mlDynamicLabel +
                ", labelID='" + labelID + '\'' +
                '}';
    }
}
