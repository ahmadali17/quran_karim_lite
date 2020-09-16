package com.qurankarim.moshaf.Azkar;

public class ZekrModel {

    private String zekrContent;
    private int zekrRepeat;
    private String zekrbless;

    public ZekrModel() {
    }

    public ZekrModel(String zekrContent, int zekrRepeat, String zekrbless) {
        this.zekrContent = zekrContent;
        this.zekrRepeat = zekrRepeat;
        this.zekrbless = zekrbless;
    }

    public String getZekrContent() {
        return zekrContent;
    }

    public void setZekrContent(String zekrContent) {
        this.zekrContent = zekrContent;
    }

    public int getZekrRepeat() {
        return zekrRepeat;
    }

    public void setZekrRepeat(int zekrRepeat) {
        this.zekrRepeat = zekrRepeat;
    }

    public String getZekrbless() {
        return zekrbless;
    }

    public void setZekrbless(String zekrbless) {
        this.zekrbless = zekrbless;
    }
}
