package com.example.Login.activities;

/*
  사용자 정보
 */
public class UserAccount {

    private String userId;
    private String userPw;
    private String userPhoneNumber;
    private String userName;
    private String userBirth;
    private String idToken;     //firebase 고유 토큰 정보


    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }



    public String getIdToken() {
        return idToken;
    }


    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }



    public UserAccount() {

    }
}
