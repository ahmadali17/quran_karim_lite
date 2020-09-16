package com.qurankarim.moshaf.Quran;

public class FavSurahModel {

    String surahTitle;
    String surahNum;
    private int surahVersesNumber;
    private String surahRevelationType;


    public FavSurahModel() {
    }

    public FavSurahModel(String surahTitle, String surahNum, int surahVersesNumber, String surahRevelationType) {
        this.surahTitle = surahTitle;
        this.surahNum = surahNum;
        this.surahVersesNumber = surahVersesNumber;
        this.surahRevelationType = surahRevelationType;
    }

    public int getSurahVersesNumber() {
        return surahVersesNumber;
    }

    public void setSurahVersesNumber(int surahVersesNumber) {
        this.surahVersesNumber = surahVersesNumber;
    }

    public String getSurahRevelationType() {
        return surahRevelationType;
    }

    public void setSurahRevelationType(String surahRevelationType) {
        this.surahRevelationType = surahRevelationType;
    }

    public String getSurahTitle() {
        return surahTitle;
    }

    public void setSurahTitle(String surahTitle) {
        this.surahTitle = surahTitle;
    }

    public String getSurahNum() {
        return surahNum;
    }

    public void setSurahNum(String surahNum) {
        this.surahNum = surahNum;
    }
}
