package com.quickbite.model;

/** A rider who delivers orders. */
public class DeliveryPerson {
    private int id;
    private String name;
    private String phone;
    private String vehicle;      // Bike, Scooter, Three-wheeler ...
    private boolean available;   // false while he/she is out on a delivery (or on leave)

    public DeliveryPerson(int id, String name, String phone, String vehicle, boolean available) {
        this.id = id; this.name = name; this.phone = phone; this.vehicle = vehicle; this.available = available;
    }

    /** id|name|phone|vehicle|available */
    public String toFileLine() { return id + "|" + name + "|" + phone + "|" + vehicle + "|" + available; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getVehicle() { return vehicle; }
    public void setVehicle(String vehicle) { this.vehicle = vehicle; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
