package com.quickbite.servlet;

import com.quickbite.dao.DeliveryDAO;
import com.quickbite.dao.DeliveryPersonDAO;
import com.quickbite.dao.OrderDAO;
import com.quickbite.model.*;
import com.quickbite.util.OrderFlow;
import com.quickbite.util.Web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

/** Admin page for riders and deliveries: assign a rider -> picked up -> delivered. */
@WebServlet("/delivery")
public class DeliveryServlet extends HttpServlet {
    public static final List<String> VEHICLES = List.of("Motorbike", "Scooter", "Bicycle", "Three-wheeler");

    private OrderDAO orders;
    private DeliveryDAO deliveries;
    private DeliveryPersonDAO riders;

    @Override
    public void init() throws ServletException {
        try {
            orders = Web.orders(getServletContext());
            deliveries = Web.deliveries(getServletContext());
            riders = Web.drivers(getServletContext());
        } catch (IOException e) { throw new ServletException("Cannot open delivery files", e); }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Web.isAdmin(req)) { Web.redirect(resp, "users?action=login", "Admin login required."); return; }

        Map<Integer, Order> orderMap = new HashMap<>();
        for (Order o : orders.getAll()) orderMap.put(o.getId(), o);

        List<Delivery> all = deliveries.getAll();
        Set<Integer> hasActive = new HashSet<>();
        List<Delivery> active = new ArrayList<>(), history = new ArrayList<>();
        for (Delivery d : all) {
            if (d.isActive()) { active.add(d); hasActive.add(d.getOrderId()); } else history.add(d);
        }
        Collections.reverse(history);
        if (history.size() > 10) history = new ArrayList<>(history.subList(0, 10));

        // orders being prepared that still need a rider (oldest first)
        List<Order> ready = new ArrayList<>();
        for (Order o : orderMap.values()) if ("Preparing".equals(o.getStatus()) && !hasActive.contains(o.getId())) ready.add(o);
        ready.sort(Comparator.comparingInt(Order::getId));

        List<DeliveryPerson> allRiders = riders.getAll();
        Set<Integer> busy = new HashSet<>();
        for (Delivery d : active) busy.add(d.getPersonId());

        req.setAttribute("ready", ready);
        req.setAttribute("active", active);
        req.setAttribute("history", history);
        req.setAttribute("orderMap", orderMap);
        req.setAttribute("riders", allRiders);
        req.setAttribute("freeRiders", riders.getAvailable());
        req.setAttribute("busyRiders", busy);
        req.setAttribute("vehicles", VEHICLES);
        req.getRequestDispatcher("/delivery.jsp").forward(req, resp);
    }

    /** action = assign | unassign | pickup | deliver | addRider | toggleRider | deleteRider */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!Web.isAdmin(req)) { Web.redirect(resp, "users?action=login", "Admin login required."); return; }
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");
        String msg;
        switch (action) {
            case "assign":      msg = assign(req); break;
            case "unassign":    msg = unassign(req); break;
            case "pickup":      msg = pickup(req); break;
            case "deliver":     msg = deliver(req); break;
            case "addRider":    msg = addRider(req); break;
            case "toggleRider": msg = toggleRider(req); break;
            case "deleteRider": msg = deleteRider(req); break;
            default:            msg = "Unknown action.";
        }
        Web.redirect(resp, "delivery", msg);
    }

    private String assign(HttpServletRequest req) throws IOException {
        Order o = orders.getById(Web.parseInt(req.getParameter("orderId")));
        DeliveryPerson p = riders.getById(Web.parseInt(req.getParameter("personId")));
        if (o == null || p == null) return "Choose an order and a rider.";
        if (!"Preparing".equals(o.getStatus())) return "Only orders that are being prepared can be given to a rider.";
        if (deliveries.getActiveByOrder(o.getId()) != null) return "Order #" + o.getId() + " already has a rider.";
        if (!p.isAvailable()) return p.getName() + " is not available.";
        deliveries.add(new Delivery(0, o.getId(), p.getId(), p.getName(), p.getPhone(), "Assigned", Web.now(), ""));
        p.setAvailable(false);
        riders.update(p);
        return "Order #" + o.getId() + " assigned to " + p.getName() + ".";
    }

    private String unassign(HttpServletRequest req) throws IOException {
        Delivery d = deliveries.getById(Web.parseInt(req.getParameter("id")));
        if (d == null || !"Assigned".equals(d.getStatus())) return "Only a rider who has not picked up yet can be removed.";
        deliveries.delete(d.getId());
        DeliveryPerson p = riders.getById(d.getPersonId());
        if (p != null) { p.setAvailable(true); riders.update(p); }
        return "Rider removed from order #" + d.getOrderId() + ".";
    }

    private String pickup(HttpServletRequest req) throws IOException {
        Delivery d = deliveries.getById(Web.parseInt(req.getParameter("id")));
        if (d == null || !"Assigned".equals(d.getStatus())) return "This delivery cannot be marked as picked up.";
        Order o = orders.getById(d.getOrderId());
        if (o == null) return "Order not found.";
        d.setStatus("Picked up");
        deliveries.update(d);
        o.setStatus("On the way");
        orders.update(o);
        OrderFlow.onStatusChanged(getServletContext(), o);
        return "Order #" + o.getId() + " is on the way with " + d.getPersonName() + ".";
    }

    private String deliver(HttpServletRequest req) throws IOException {
        Delivery d = deliveries.getById(Web.parseInt(req.getParameter("id")));
        if (d == null || !"Picked up".equals(d.getStatus())) return "This delivery cannot be marked as delivered yet.";
        Order o = orders.getById(d.getOrderId());
        if (o == null) return "Order not found.";
        o.setStatus("Delivered");
        orders.update(o);
        OrderFlow.onStatusChanged(getServletContext(), o);   // closes the trip, frees the rider, payment -> Paid
        return "Order #" + o.getId() + " delivered. Payment marked as paid.";
    }

    private String addRider(HttpServletRequest req) throws IOException {
        String name = Web.clean(req.getParameter("name"));
        String phone = Web.clean(req.getParameter("phone"));
        String vehicle = req.getParameter("vehicle");
        if (name.length() < 2) return "Enter the rider's name.";
        if (!phone.matches("[0-9+ ]{9,15}")) return "Enter a valid phone number.";
        if (!VEHICLES.contains(vehicle)) vehicle = VEHICLES.get(0);
        riders.add(new DeliveryPerson(0, name, phone, vehicle, true));
        return "Rider " + name + " added.";
    }

    private String toggleRider(HttpServletRequest req) throws IOException {
        DeliveryPerson p = riders.getById(Web.parseInt(req.getParameter("id")));
        if (p == null) return "Rider not found.";
        if (deliveries.riderIsBusy(p.getId())) return p.getName() + " is on a delivery right now.";
        p.setAvailable(!p.isAvailable());
        riders.update(p);
        return p.getName() + (p.isAvailable() ? " is available." : " is marked as off duty.");
    }

    private String deleteRider(HttpServletRequest req) throws IOException {
        DeliveryPerson p = riders.getById(Web.parseInt(req.getParameter("id")));
        if (p == null) return "Rider not found.";
        if (deliveries.riderIsBusy(p.getId())) return p.getName() + " is on a delivery and cannot be deleted.";
        riders.delete(p.getId());
        return "Rider " + p.getName() + " deleted.";
    }
}
