package com.example.adminloginactivity.Classes;

public class RegisterUsers {
    String firstname,lastname,username,cnic,phoneno, email, approve, id;

    public RegisterUsers() {
    }

    public RegisterUsers(String firstname, String lastname, String username, String cnic, String phoneno, String email,String approve, String id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.cnic = cnic;
        this.phoneno = phoneno;
        this.email = email;
        this.approve =approve;
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}