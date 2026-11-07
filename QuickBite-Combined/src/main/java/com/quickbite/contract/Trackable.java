package com.quickbite.contract;

/**
 * Contract for entities providing progress status and ETA.
 * Demonstrates OOP Concept #5: Interfaces.
 */
public interface Trackable {
    String getStatus();
    String getEta();
}
