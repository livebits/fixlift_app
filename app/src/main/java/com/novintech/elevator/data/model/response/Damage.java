package com.novintech.elevator.data.model.response;

import java.util.List;

public class Damage {
    public String id;
    public String appUserId;
    public String visitDate;
    public String description;
    public String status;
    public String serviceId;
    public String createdAt;

    public User serviceUser;
    public User appUser;

    public Factor factors;
    public Report reports;
}
