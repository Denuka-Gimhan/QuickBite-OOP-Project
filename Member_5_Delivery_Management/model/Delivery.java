package com.quickbite.model;

import java.util.List;

/** Links one order to one delivery person and tracks the trip:- Assigned -> Picked up -> Delivered. */

/**
 * MODEL class for a Delivery.
 * This is just a data holder (POJO) - it stores the values of ONE delivery.
 * It does NOT talk to the file directly. The DeliveryDAO does that.
 *
 * A delivery goes through 3 stages:
 *   Assigned  ->  Picked up  ->  Delivered
 */

public class Delivery {
    // Allowed status values. List.of() creates an unchangeable (immutable) list.
    // Using a constant here means the whole app uses the SAME set of status strings.

    public static final List<String> STATUSES = List.of("Assigned", "Picked up", "Delivered");

    private int id;    // delivery id (set by DAO)
    private final int orderId;     // which order this delivery belongs to
    private final int personId;        // rider's id (link to DeliveryPerson)
    private final String personName;    // copied at assign time so old records stay readable
    private final String personPhone;   // copied at assign time (same reason)
    private String status;             // Assigned / Picked up / Delivered (can change)
    private final String assignedAt;    // time when rider was assigned (fixed)
    private String deliveredAt;          // time when delivered (empty until Delivered)

    // Constructor: we pass all 8 fields. Object created only once.
    public Delivery(int id, int orderId, int personId, String personName, String personPhone,
                    String status, String assignedAt, String deliveredAt) {
        this.id = id; this.orderId = orderId; this.personId = personId; this.personName = personName;
        this.personPhone = personPhone; this.status = status; this.assignedAt = assignedAt; this.deliveredAt = deliveredAt;
    }

    // A delivery is "active" if it is NOT yet Delivered.
    // Used by DAO (getActiveByOrder, riderIsBusy) to check if the rider is still busy.
    public boolean isActive() {
        return !"Delivered".equals(status); } // safer than status.equals("Delivered") - no NPE


    /**
     * Convert this object into one line for the text file.
     * Format: id|orderId|personId|personName|personPhone|status|assignedAt|deliveredAt
     * DAO uses this method to save, and reads it back in getAll().
     */

    public String toFileLine() {
        return String.join("|", String.valueOf(id), String.valueOf(orderId), String.valueOf(personId),
                personName, personPhone, status, assignedAt, deliveredAt);
    }


    // ---------- GETTERS / SETTERS ----------
    // Only fields that can change have setters:
    //   id           -> set once by DAO in add()
    //   status       -> changes as the trip moves forward
    //   deliveredAt  -> set only when the trip is finished
    // orderId, personId, personName, personPhone, assignedAt -> never change (final)

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
