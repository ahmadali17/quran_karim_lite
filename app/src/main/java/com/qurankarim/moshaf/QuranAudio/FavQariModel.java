package com.qurankarim.moshaf.QuranAudio;

public class FavQariModel {
    private int qariId;
    private String qariName;
    private String qariRelativePath;
    private String qariFavStatus;

    public int getQariId() {
        return qariId;
    }

    public void setQariId(int qariId) {
        this.qariId = qariId;
    }

    public String getQariName() {
        return qariName;
    }

    public void setQariName(String qariName) {
        this.qariName = qariName;
    }

    public String getQariRelativePath() {
        return qariRelativePath;
    }

    public void setQariRelativePath(String qariRelativePath) {
        this.qariRelativePath = qariRelativePath;
    }

    public String getQariFavStatus() {
        return qariFavStatus;
    }

    public void setQariFavStatus(String qariFavStatus) {
        this.qariFavStatus = qariFavStatus;
    }

    public FavQariModel(int qariId, String qariName, String qariRelativePath, String qariFavStatus) {
        this.qariId = qariId;
        this.qariName = qariName;
        this.qariRelativePath = qariRelativePath;
        this.qariFavStatus = qariFavStatus;
    }
}