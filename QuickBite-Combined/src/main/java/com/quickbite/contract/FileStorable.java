package com.quickbite.contract;

/**
 * Contract for entities that can be persisted as a line in a text file.
 * Demonstrates OOP Concept #5: Interfaces.
 */
public interface FileStorable {
    int getId();
    String toFileLine();
}
