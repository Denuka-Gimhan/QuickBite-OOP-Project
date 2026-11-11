package com.quickbite.model;

/**
 * Top-level node class for the hand-written FIFO OrderQueue.
 * Replaces nested Node class (forbidden construct).
 * Demonstrates OOP Concept #1: Constructors (chaining) and Concept #2: Encapsulation.
 */
public class QueueNode {
    private final int orderId;
    private QueueNode next;

    public QueueNode(int orderId) {
        this(orderId, null);
    }

    public QueueNode(int orderId, QueueNode next) {
        this.orderId = orderId;
        this.next = next;
    }

    public int getOrderId() {
        return orderId;
    }

    public QueueNode getNext() {
        return next;
    }

    public void setNext(QueueNode next) {
        this.next = next;
    }
}
