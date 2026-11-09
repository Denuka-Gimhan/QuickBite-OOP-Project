package com.quickbite.util;

import com.quickbite.dao.*;
import com.quickbite.model.*;

import jakarta.servlet.ServletContext;

import java.io.IOException;

/**
 * The ONE place that knows what must happen to the other parts of the system when an order is
 * placed, changes status, or is deleted. (Owner: Member 4. Members 3, 5 and 6 only CALL these methods.)
 *
 *   placed          -> goes into the FIFO queue + a "Pending" payment record is created
 *   status changed  -> queue / payment / delivery are brought in line
 *   deleted         -> queue, payment, delivery and rating records of that order are removed
 */
public final class OrderFlow {
    private OrderFlow() {}

    public static void onPlaced(ServletContext ctx, Order o) throws IOException {
        Web.queue(ctx).enqueue(o.getId());
        Web.payments(ctx).addForOrder(o);
    }

    /** Call after the order's new status has been saved with OrderDAO.update(). */
    public static void onStatusChanged(ServletContext ctx, Order o) throws IOException {
        String s = o.getStatus();

        // queue: only Pending orders wait in the queue
        OrderQueue q = Web.queue(ctx);
        if ("Pending".equals(s)) q.enqueue(o.getId()); else q.remove(o.getId());

        // payment: Delivered -> Paid, Cancelled -> Cancelled / Refunded
        Web.payments(ctx).onOrderStatus(o.getId(), s);

        // delivery: close the trip and free the rider
        DeliveryDAO deliveries = Web.deliveries(ctx);
        Delivery d = deliveries.getActiveByOrder(o.getId());
        if (d != null && ("Delivered".equals(s) || "Cancelled".equals(s))) {
            if ("Delivered".equals(s)) { d.setStatus("Delivered"); d.setDeliveredAt(Web.now()); deliveries.update(d); }
            else deliveries.delete(d.getId());
            DeliveryPersonDAO riders = Web.drivers(ctx);
            DeliveryPerson p = riders.getById(d.getPersonId());
            if (p != null) { p.setAvailable(true); riders.update(p); }
        }
    }

    public static void onDeleted(ServletContext ctx, int orderId) throws IOException {
        Web.queue(ctx).remove(orderId);
        Web.payments(ctx).deleteByOrder(orderId);
        Web.ratings(ctx).deleteByOrder(orderId);
        Delivery d = Web.deliveries(ctx).getActiveByOrder(orderId);
        if (d != null) {
            DeliveryPersonDAO riders = Web.drivers(ctx);
            DeliveryPerson p = riders.getById(d.getPersonId());
            if (p != null) { p.setAvailable(true); riders.update(p); }
        }
        Web.deliveries(ctx).deleteByOrder(orderId);
    }

    /**
     * FIFO: takes the oldest waiting order off the queue and starts preparing it.
     * Returns that order, or null when nobody is waiting.
     */
    public static Order processNext(ServletContext ctx) throws IOException {
        OrderQueue q = Web.queue(ctx);
        OrderDAO orders = Web.orders(ctx);
        synchronized (q) {
            while (!q.isEmpty()) {
                Order o = orders.getById(q.dequeue());
                if (o == null || !"Pending".equals(o.getStatus())) continue;   // stale entry, skip it
                o.setStatus("Preparing");
                orders.update(o);
                onStatusChanged(ctx, o);
                return o;
            }
        }
        return null;
    }
}
