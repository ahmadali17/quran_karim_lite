package com.qurankarim.moshaf.QuranAudio;

public class QariModel {
    private int qariId;
    private String qariName;
    private String qariRelativePath;
    private String qariFavStatus;

    public QariModel(int qariId, String qariName, String qariRelativePath, String qariFavStatus) {
        this.qariId = qariId;
        this.qariName = qariName;
        this.qariRelativePath = qariRelativePath;
        this.qariFavStatus = qariFavStatus;
    }

    public String getQariFavStatus() {
        return qariFavStatus;
    }

    public void setQariFavStatus(String qariFavStatus) {
        this.qariFavStatus = qariFavStatus;
    }

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
}
