package com.quickbite.model;

import java.util.List;

/** Links one order to one delivery person and tracks the trip:- Assigned -> Picked up -> Delivered. */
public class Delivery {
    public static final List<String> STATUSES = List.of("Assigned", "Picked up", "Delivered");

    private int id;
    private final int orderId;
    private final int personId;
    private final String personName;    // copied at assign time so old records stay readable
    private final String personPhone;
    private String status;
    private final String assignedAt;
    private String deliveredAt;

    public Delivery(int id, int orderId, int personId, String personName, String personPhone,
                    String status, String assignedAt, String deliveredAt) {
        this.id = id; this.orderId = orderId; this.personId = personId; this.personName = personName;
        this.personPhone = personPhone; this.status = status; this.assignedAt = assignedAt; this.deliveredAt = deliveredAt;
    }

    public boolean isActive() { return !"Delivered".equals(status); }

    /** id||orderId||personId||personName||personPhone||status||assignedAt||deliveredAt */
    public String toFileLine() {
        return String.join("|", String.valueOf(id), String.valueOf(orderId), String.valueOf(personId),
                personName, personPhone, status, assignedAt, deliveredAt);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrderId() { return orderId; }
    public int getPersonId() { return personId; }
    public String getPersonName() { return personName; }
    public String getPersonPhone() { return personPhone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAssignedAt() { return assignedAt; }
    public String getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(String deliveredAt) { this.deliveredAt = deliveredAt; }
}
