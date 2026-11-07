package com.quickbite.servlet;

import com.quickbite.dao.OrderDAO;
import com.quickbite.dao.PaymentDAO;
import com.quickbite.model.*;
import com.quickbite.util.Web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

/**
 * Billing and payments.
 *   GET /billing?orderId=1001  -> the invoice of one order (the customer who owns it, or an admin)
 *   GET /billing               -> admin: list of all payments
 *   POST action = markPaid | refund  (admin)
 */
@WebServlet("/billing")
public class PaymentServlet extends HttpServlet {
    private PaymentDAO payments;
    private OrderDAO orders;

    @Override
    public void init() throws ServletException {
        try {
            payments = Web.payments(getServletContext());
            orders = Web.orders(getServletContext());
        } catch (IOException e) { throw new ServletException("Cannot open payment files", e); }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User me = Web.user(req);
        if (me == null) { Web.redirect(resp, "users?action=login", "Please log in to continue."); return; }

        String orderIdParam = req.getParameter("orderId");
        if (orderIdParam != null && !orderIdParam.isBlank()) {
            Order o = orders.getById(Web.parseInt(orderIdParam));
            if (o == null || !(me.isAdmin() || o.getUserId() == me.getId())) {
                Web.redirect(resp, "orders", "Bill not found.");
                return;
            }
            req.setAttribute("bill", new Bill(o, payments.getByOrder(o.getId())));
            req.getRequestDispatcher("/bill.jsp").forward(req, resp);
            return;
        }

        if (!me.isAdmin()) { resp.sendRedirect("orders"); return; }

        String status = req.getParameter("status");
        List<Payment> list = payments.getAll();
        double paid = 0, pending = 0, refunded = 0;
        for (Payment p : list) {
            if ("Paid".equals(p.getStatus())) paid += p.getAmount();
            else if ("Pending".equals(p.getStatus())) pending += p.getAmount();
            else if ("Refunded".equals(p.getStatus())) refunded += p.getAmount();
        }
        if (status != null && !status.isBlank()) list.removeIf(p -> !p.getStatus().equals(status));
        Collections.reverse(list);

        Map<Integer, Order> orderMap = new HashMap<>();
        for (Order o : orders.getAll()) orderMap.put(o.getId(), o);

        req.setAttribute("payments", list);
        req.setAttribute("orderMap", orderMap);
        req.setAttribute("statuses", Payment.STATUSES);
        req.setAttribute("totalPaid", paid);
        req.setAttribute("totalPending", pending);
        req.setAttribute("totalRefunded", refunded);
        req.getRequestDispatcher("/billing.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Web.isAdmin(req)) { Web.redirect(resp, "users?action=login", "Admin login required."); return; }
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");
        Payment p = payments.getById(Web.parseInt(req.getParameter("id")));
        String msg;
        if (p == null) msg = "Payment not found.";
        else if (action.equals("markPaid") && "Pending".equals(p.getStatus())) {
            p.setStatus("Paid"); payments.update(p); msg = "Payment for order #" + p.getOrderId() + " marked as paid.";
        } else if (action.equals("refund") && "Paid".equals(p.getStatus())) {
            p.setStatus("Refunded"); payments.update(p); msg = "Payment for order #" + p.getOrderId() + " refunded.";
        } else msg = "That change is not possible for a " + p.getStatus() + " payment.";
        Web.redirect(resp, "billing", msg);
    }
}
