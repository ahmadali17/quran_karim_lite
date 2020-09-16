package com.qurankarim.moshaf.Quran;

public class SurahModel {
    private String surahName;
    private int surahNumber;
    private int surahVersesNumber;
    private String surahRevelationType;
    public String isFav;

    public SurahModel(String surahName, int surahNumber, int surahVersesNumber, String surahRevelationType, String isFav) {
        this.surahName = surahName;
        this.surahNumber = surahNumber;
        this.surahVersesNumber = surahVersesNumber;
        this.surahRevelationType = surahRevelationType;
        this.isFav = isFav;
    }

    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }

    public String getSurahName() {
        return surahName;
    }

    public void setSurahName(String surahName) {
        this.surahName = surahName;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public void setSurahNumber(int surahNumber) {
        this.surahNumber = surahNumber;
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
}
