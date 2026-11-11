package com.quickbite.model;

/** A rider who delivers orders. */

/**
 * MODEL class for a Delivery Person (rider).
 * This is a simple data holder (POJO) – it stores the values of ONE rider.
 * It does NOT talk to the file. The DeliveryPersonDAO does that.
 *
 * A rider's details:
 *   id | name | phone | vehicle | available
 */


public class DeliveryPerson {
    private int id;       // unique id (set by DAO in add())
    private String name;   // rider's full name
    private String phone;   // contact number
    private String vehicle;      // Bike, Scooter, Three-wheeler ...
    private boolean available;   // true  = free, can be assigned
                                 // false = busy on a delivery (or on leave)


    // Constructor: creates one rider object with all fields.
    public DeliveryPerson(int id, String name, String phone, String vehicle, boolean available) {
        this.id = id; this.name = name; this.phone = phone; this.vehicle = vehicle; this.available = available;
    }

    /**
     * Convert this object into one line for the text file.
     * Format: id|name|phone|vehicle|available
     * The DAO uses this method to save, and reads it back in getAll().
     */

    public String toFileLine() { return id + "|" + name + "|" + phone + "|" + vehicle + "|" + available; }


    // ---------- GETTERS / SETTERS ----------
    // All fields have getters and setters because all of them
    // can change over time (name change, phone change, availability toggles, etc.)


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }    // set once by DAO in add()

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getVehicle() { return vehicle; }
    public void setVehicle(String vehicle) { this.vehicle = vehicle; }

    // boolean getter is named isAvailable() (Java convention for boolean)
    public boolean isAvailable() { return available; }


    // Called by service layer when rider is assigned (false) or delivered (true)
    public void setAvailable(boolean available) { this.available = available; }
}
