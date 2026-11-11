package com.quickbite.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Group-allocated DATA STRUCTURE: Queue (FIFO) - orders are prepared in the order they were placed.
 * Hand-written singly linked queue (not java.util.Queue) so it can be explained in the viva.
 * It stores order IDs; the order details always come from orders.txt (via OrderDAO).
 *
 *   enqueue(id)  -> add at the back      O(1)
 *   dequeue()    -> take from the front  O(1)
 *   peek()       -> look at the front    O(1)
 */
public class OrderQueue {
    private static class Node {
        final int orderId;
        Node next;
        Node(int orderId) { this.orderId = orderId; }
    }

    private Node head;   // front of the queue
    private Node tail;   // back of the queue
    private int size;

    public synchronized void enqueue(int orderId) {
        if (contains(orderId)) return;               // an order can be in the queue only once
        Node n = new Node(orderId);
        if (tail == null) head = tail = n;
        else { tail.next = n; tail = n; }
        size++;
    }

    /** Removes and returns the order ID at the front, or -1 when the queue is empty. */
    public synchronized int dequeue() {
        if (head == null) return -1;
        int id = head.orderId;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return id;
    }

    /** Front order ID without removing it, or -1 when empty. */
    public synchronized int peek() { return head == null ? -1 : head.orderId; }

    /** Removes an order from anywhere in the queue (cancelled / status changed by admin). */
    public synchronized boolean remove(int orderId) {
        Node prev = null;
        for (Node cur = head; cur != null; prev = cur, cur = cur.next) {
            if (cur.orderId == orderId) {
                if (prev == null) head = cur.next; else prev.next = cur.next;
                if (cur == tail) tail = prev;
                size--;
                return true;
            }
        }
        return false;
    }

    public synchronized boolean contains(int orderId) { return position(orderId) > 0; }

    /** 1 = next to be prepared, 0 = not in the queue. */
    public synchronized int position(int orderId) {
        int pos = 1;
        for (Node cur = head; cur != null; cur = cur.next, pos++) if (cur.orderId == orderId) return pos;
        return 0;
    }

    public synchronized boolean isEmpty() { return size == 0; }
    public synchronized int size() { return size; }

    public synchronized List<Integer> toList() {
        List<Integer> ids = new ArrayList<>();
        for (Node cur = head; cur != null; cur = cur.next) ids.add(cur.orderId);
        return ids;
    }
}
