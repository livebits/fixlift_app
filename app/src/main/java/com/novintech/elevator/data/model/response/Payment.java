package com.novintech.elevator.data.model.response;

import java.util.List;

public class Payment {
    public int id;
    public int appUserId;
    public int price;
    public String refCode;
    public String date;
    public String status;

    public List<Factor> factors;
}
