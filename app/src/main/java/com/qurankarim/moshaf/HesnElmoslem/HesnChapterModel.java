package com.qurankarim.moshaf.HesnElmoslem;

public class HesnChapterModel {
    private String chapterText;
    private String chapterSource;

    public HesnChapterModel(String chapterText, String chapterSource) {
        this.chapterText = chapterText;
        this.chapterSource = chapterSource;
    }

    public String getChapterText() {
        return chapterText;
    }

    public void setChapterText(String chapterText) {
        this.chapterText = chapterText;
    }

    public String getChapterSource() {
        return chapterSource;
    }

    public void setChapterSource(String chapterSource) {
        this.chapterSource = chapterSource;
    }
}
