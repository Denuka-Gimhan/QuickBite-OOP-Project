package com.quickbite.servlet;

import com.quickbite.dao.OrderDAO;
import com.quickbite.model.*;
import com.quickbite.util.OrderFlow;
import com.quickbite.util.Web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    private OrderDAO dao;

    @Override
    public void init() throws ServletException {
        try { dao = Web.orders(getServletContext()); }
        catch (IOException e) { throw new ServletException("Cannot open orders file", e); }
    }

    /** GET: order list (default) or checkout page (action=checkout). */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        User me = Web.user(req);
        if (me == null) { Web.redirect(resp, "users?action=login", "Please log in to continue."); return; }

        if ("checkout".equals(req.getParameter("action"))) {
            Cart cart = Web.cart(req);
            String changes = Web.syncCart(ctx, cart);            // latest prices / stock before the customer pays
            if (cart.isEmpty()) { Web.redirect(resp, "cart", changes.isEmpty() ? "Your cart is empty." : changes); return; }
            if (!changes.isEmpty()) { Web.redirect(resp, "cart", changes); return; }
            showCheckout(req, resp, cart);
            return;
        }

        List<Order> orders = me.isAdmin() ? dao.getAll() : dao.getByUser(me.getId());
        String status = req.getParameter("status");
        if (me.isAdmin() && status != null && !status.isBlank()) orders.removeIf(o -> !o.getStatus().equals(status));
        Collections.reverse(orders);   // newest first
        req.setAttribute("orders", orders);
        req.setAttribute("statuses", Order.STATUSES);

        // information from the other modules, looked up once and shown next to each order
        Map<Integer, Payment> payments = new HashMap<>();
        for (Payment p : Web.payments(ctx).getAll()) payments.put(p.getOrderId(), p);
        Map<Integer, Delivery> deliveries = new HashMap<>();
        for (Delivery d : Web.deliveries(ctx).getAll()) deliveries.put(d.getOrderId(), d);   // latest wins
        Map<Integer, Rating> ratings = new HashMap<>();
        for (Rating r : Web.ratings(ctx).getAll()) ratings.put(r.getOrderId(), r);
        OrderQueue queue = Web.queue(ctx);
        Map<Integer, Integer> queuePos = new HashMap<>();
        for (Order o : orders) if ("Pending".equals(o.getStatus())) queuePos.put(o.getId(), queue.position(o.getId()));
        req.setAttribute("paymentMap", payments);
        req.setAttribute("deliveryMap", deliveries);
        req.setAttribute("ratingMap", ratings);
        req.setAttribute("queuePos", queuePos);
        if (me.isAdmin()) {
            req.setAttribute("queueSize", queue.size());
            req.setAttribute("queueNext", queue.isEmpty() ? null : dao.getById(queue.peek()));
        }
        req.getRequestDispatcher("/orders.jsp").forward(req, resp);
    }

    /** POST: action = place | cancel | status | delete | process */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User me = Web.user(req);
        if (me == null) { Web.redirect(resp, "users?action=login", "Please log in to continue."); return; }
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");

        switch (action) {
            case "place":   place(req, resp, me); break;
            case "cancel":  cancel(req, resp, me); break;
            case "status":  changeStatus(req, resp, me); break;
            case "delete":  deleteOrder(req, resp, me); break;
            case "process": processNext(resp, me); break;
            default:        Web.redirect(resp, "orders", "Unknown action.");
        }
    }

    private void place(HttpServletRequest req, HttpServletResponse resp, User me) throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        Cart cart = Web.cart(req);
        String changes = Web.syncCart(ctx, cart);
        if (cart.isEmpty()) { Web.redirect(resp, "cart", changes.isEmpty() ? "Your cart is empty." : changes); return; }
        if (!changes.isEmpty()) { Web.redirect(resp, "cart", changes); return; }   // let the customer see the new prices first

        String address = Web.clean(req.getParameter("address"));
        String phone = Web.clean(req.getParameter("phone"));
        if (address.isEmpty() || !phone.matches("[0-9+ ]{9,15}")) {
            req.setAttribute("error", "Enter a delivery address and a valid phone number.");
            showCheckout(req, resp, cart);
            return;
        }
        String type = "EXPRESS".equals(req.getParameter("type")) ? "EXPRESS" : "STANDARD";
        String payment = "CARD".equals(req.getParameter("payment")) ? "Card on delivery" : "Cash on delivery";

        Order order = OrderFactory.create(type, 0, me.getId(), me.getUsername(), "Pending", Web.now(),
                address, phone, payment, toItems(cart));
        order.setPromoCode(cart.getPromoCode());
        order.setDiscount(cart.getDiscount());
        dao.add(order);
        OrderFlow.onPlaced(ctx, order);          // queue + payment record
        cart.clear();
        int pos = Web.queue(ctx).position(order.getId());
        Web.redirect(resp, "orders", "Order #" + order.getId() + " placed. You are number " + pos
                + " in the kitchen queue. Estimated delivery " + order.getEta() + ".");
    }

    private void cancel(HttpServletRequest req, HttpServletResponse resp, User me) throws IOException {
        Order o = dao.getById(Web.parseInt(req.getParameter("id")));
        if (o != null && (me.isAdmin() || o.getUserId() == me.getId()) && o.isCancellable()) {
            o.setStatus("Cancelled");
            dao.update(o);
            OrderFlow.onStatusChanged(getServletContext(), o);
            Web.redirect(resp, "orders", "Order #" + o.getId() + " cancelled.");
        } else {
            Web.redirect(resp, "orders", "This order can no longer be cancelled.");
        }
    }

    private void changeStatus(HttpServletRequest req, HttpServletResponse resp, User me) throws IOException {
        String status = req.getParameter("status");
        Order o = dao.getById(Web.parseInt(req.getParameter("id")));
        if (!me.isAdmin() || o == null || !Order.STATUSES.contains(status)) {
            Web.redirect(resp, "orders", "Could not update the order.");
            return;
        }
        o.setStatus(status);
        dao.update(o);
        OrderFlow.onStatusChanged(getServletContext(), o);
        Web.redirect(resp, "orders", "Order #" + o.getId() + " is now " + status + ".");
    }

    private void deleteOrder(HttpServletRequest req, HttpServletResponse resp, User me) throws IOException {
        int id = Web.parseInt(req.getParameter("id"));
        boolean ok = me.isAdmin() && dao.delete(id);
        if (ok) OrderFlow.onDeleted(getServletContext(), id);
        Web.redirect(resp, "orders", ok ? "Order deleted." : "Could not delete the order.");
    }

    /** Admin: take the OLDEST waiting order from the queue (FIFO) and start preparing it. */
    private void processNext(HttpServletResponse resp, User me) throws IOException {
        if (!me.isAdmin()) { Web.redirect(resp, "orders", "Admins only."); return; }
        Order o = OrderFlow.processNext(getServletContext());
        Web.redirect(resp, "orders", o == null ? "No orders are waiting in the queue."
                : "Order #" + o.getId() + " (" + o.getUsername() + ") is now being prepared.");
    }

    private void showCheckout(HttpServletRequest req, HttpServletResponse resp, Cart cart) throws ServletException, IOException {
        List<OrderItem> items = toItems(cart);
        req.setAttribute("standardFee", OrderFactory.create("STANDARD", 0, 0, "", "", "", "", "", "", items).getDeliveryFee());
        req.setAttribute("expressFee", OrderFactory.create("EXPRESS", 0, 0, "", "", "", "", "", "", items).getDeliveryFee());
        req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
    }

    private List<OrderItem> toItems(Cart cart) {
        List<OrderItem> items = new ArrayList<>();
        for (CartItem c : cart.getItems()) {
            items.add(new OrderItem(c.getName(), c.getQty(), c.getPrice(), c.getEmoji(), c.getImageFile()));
        }
        return items;
    }
}
