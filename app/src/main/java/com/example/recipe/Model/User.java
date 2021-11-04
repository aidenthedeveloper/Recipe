package com.example.recipe.Model;

public class User {

   private String id,fullName, email, username, phoneNo, password, imageurl,bio,gender,month,year;
   int age;

    public User(String id,String fullName, String username, String phoneNo, String password,String imageurl,String bio,String gender,int age,String month,String year) {
    }

    public User(String id, String fullName, String email, String username, String phoneNo, String password, String imageurl,String bio,String gender,int age,String month,String year) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.phoneNo = phoneNo;
        this.password = password;
        this.imageurl = imageurl;
        this.bio = bio;
        this.gender = gender;
        this.age = age;
        this.month = month;
        this.year = year;
    }

    public User() {

    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}

