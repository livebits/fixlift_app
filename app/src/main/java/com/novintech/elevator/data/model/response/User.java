package com.novintech.elevator.data.model.response;

import java.util.List;

public class User {

    public int id;
    public String email;
    public String mobile;
    public String firstname;
    public String lastname;
    public String companyId;
    public String buildingName;
    public String address;
    public String latitude = "";
    public String longitude = "";
    public String role;

    public String fcmToken = "";
    public String code;
    public Token token;


    public int balance;
    public List<FactorPayment> factorPayments;
    public List<Damage> damages;

    public String getFullName() {
        return firstname + " " + lastname;
    }
}
