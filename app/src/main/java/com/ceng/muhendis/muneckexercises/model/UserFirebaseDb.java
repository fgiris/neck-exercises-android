package com.ceng.muhendis.muneckexercises.model;

/**
 * Created by muhendis on 28.03.2018.
 */

public class UserFirebaseDb {
    String email;
    String password;
    String name;
    String gender;
    String height;
    String weight;
    String age;
    String token;
    String email_password;

    public String getProgramStartDate() {
        return programStartDate;
    }

    public void setProgramStartDate(String programStartDate) {
        this.programStartDate = programStartDate;
    }

    String programStartDate;
    boolean isLoggedIn;
    boolean isFirstSurveyCompleted;

    public boolean isLastSurveyCompleted() {
        return isLastSurveyCompleted;
    }

    public void setLastSurveyCompleted(boolean lastSurveyCompleted) {
        isLastSurveyCompleted = lastSurveyCompleted;
    }

    boolean isLastSurveyCompleted;

    public UserFirebaseDb() {
    }

    public UserFirebaseDb(String email, String password, String name, String gender, String height, String weight, String age, boolean isLoggedIn, boolean isFirstSurveyCompleted, String token, String programStartDate, boolean isLastSurveyCompleted) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.isLoggedIn = isLoggedIn;
        this.isFirstSurveyCompleted = isFirstSurveyCompleted;
        this.token = token;
        this.email_password = email+"_"+password;
        this.programStartDate = programStartDate;
        this.isLastSurveyCompleted = isLastSurveyCompleted;
    }


    public String getEmail() {
        return email;
    }

    public String getEmail_password() {
        return email_password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getAge() {
        return age;
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public boolean getIsFirstSurveyCompleted() {
        return isFirstSurveyCompleted;
    }

    public String getToken() {
        return token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail_password(String email_password) {
        this.email_password = email_password;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setFirstSurveyCompleted(boolean firstSurveyCompleted) {
        isFirstSurveyCompleted = firstSurveyCompleted;
    }
}
