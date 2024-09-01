package com.example.recipegenius.PresentationClasses;

public class UserRegistrationModel {


    private String UserId; // Add this field
    private String Name;
    private String Email;
    private String UserType;
    private String profileImage; // URL of the profile image
    private String dob; // Date of Birth
    private String age;
    private String gender;
    private String address;
    private String phone;

    public UserRegistrationModel() {
    }

    public UserRegistrationModel(String userId, String name, String email, String userType, String profileImage, String dob, String age, String gender, String address, String phone) {
        UserId = userId;
        Name = name;
        Email = email;
        UserType = userType;
        this.profileImage = profileImage;
        this.dob = dob;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
