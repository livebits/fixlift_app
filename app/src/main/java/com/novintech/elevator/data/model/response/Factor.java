package com.novintech.elevator.data.model.response;

import java.util.List;

public class Factor {
    public int id;
    public int damageId;
    public String status;
    public String createdAt;
    public String paymentStatus;
    public int sumPrice;

    public Damage damage;
    public List<FactorItem> factorItems;
    public List<Payment> payments;
}
