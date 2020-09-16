package com.qurankarim.moshaf;

import java.util.Date;

public class UserData {

    private String userName;
    private String userAndroidId;
    private boolean isPurchased;
    private int userPoints;
    private String loginDate;

    public UserData() {
    }

    public UserData(String userName, String userAndroidId, boolean isPurchased, int userPoints, String loginDate) {
        this.userName = userName;
        this.userAndroidId = userAndroidId;
        this.isPurchased = isPurchased;
        this.userPoints = userPoints;
        this.loginDate = loginDate;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAndroidId() {
        return userAndroidId;
    }

    public void setUserAndroidId(String userAndroidId) {
        this.userAndroidId = userAndroidId;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(int userPoints) {
        this.userPoints = userPoints;
    }
}
