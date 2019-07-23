package com.example.firebase_basics;

public class User
{
    private String NAME;
    private String CONTACT_NUMBER;
    private String DOB;
    private String ADDRESS;
    private String AADHAAR_NUMBER;

    public User()
    {  }
    public User(String name, String contactNumber, String DOB, String address, String aadhaarNumber)
    {
        this.NAME = name;
        this.CONTACT_NUMBER = contactNumber;
        this.DOB = DOB;
        this.ADDRESS = address;
        this.AADHAAR_NUMBER = aadhaarNumber;
    }
    public String getUserName()
    { return this.NAME; }
    public String getUserContactNumber()
    { return this.CONTACT_NUMBER; }
    public String getUserDOB()
    { return this.DOB; }
    public String getUserAddress()
    { return this.ADDRESS; }
    public String getUserAadhaarNumber()
    { return this.AADHAAR_NUMBER; }

    public void setUserName(String name)
    { this.NAME = name; }
    public void setUserAddress(String address)
    { this.ADDRESS = address ; }
    public void setUserDOB(String DOB)
    { this.DOB = DOB ; }
    public void setUserContactNumber(String contactNumber)
    { this.CONTACT_NUMBER = contactNumber; }
    public void setUserAadhaarNumber(String aadhaarNumber)
    { this.AADHAAR_NUMBER = aadhaarNumber; }
}
