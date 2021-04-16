package com.qurankarim.moshaf.Quran;

import java.io.Serializable;

public class JuzModel implements Serializable {
    String juzNumber;
    boolean isChecked = false;

    public JuzModel(String juzNumber) {
        this.juzNumber = juzNumber;
    }

    public String getJuzNumber() {
        return juzNumber;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setJuzNumber(String juzNumber) {
        this.juzNumber = juzNumber;
    }
}
