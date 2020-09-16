package com.qurankarim.moshaf.Adiah;

public class DoaaModel {
    private String doaaId;
    private String doaaContent;
    private String doaaRefrance;

    public DoaaModel(String doaaId, String doaaContent, String doaaRefrance) {
        this.doaaId = doaaId;
        this.doaaContent = doaaContent;
        this.doaaRefrance = doaaRefrance;
    }

    public String getDoaaId() {
        return doaaId;
    }

    public void setDoaaId(String doaaId) {
        this.doaaId = doaaId;
    }

    public String getDoaaContent() {
        return doaaContent;
    }

    public void setDoaaContent(String doaaContent) {
        this.doaaContent = doaaContent;
    }

    public String getDoaaRefrance() {
        return doaaRefrance;
    }

    public void setDoaaRefrance(String doaaRefrance) {
        this.doaaRefrance = doaaRefrance;
    }
}
