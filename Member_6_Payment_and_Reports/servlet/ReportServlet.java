package com.quickbite.servlet;

import com.quickbite.model.*;
import com.quickbite.util.Web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

/** Admin reports: sales, order status, best sellers, promo usage, ratings. /reports?format=csv downloads all orders. */
@WebServlet("/reports")
public class ReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Web.isAdmin(req)) { Web.redirect(resp, "users?action=login", "Admin login required."); return; }

        List<Order> orders = Web.orders(getServletContext()).getAll();
        List<Payment> payments = Web.payments(getServletContext()).getAll();
        List<Rating> ratings = Web.ratings(getServletContext()).getAll();
        Map<Integer, Payment> payByOrder = new HashMap<>();
        for (Payment p : payments) payByOrder.put(p.getOrderId(), p);

        if ("csv".equals(req.getParameter("format"))) { writeCsv(resp, orders, payByOrder); return; }

        // ---- orders by status / type
        Map<String, Integer> byStatus = new LinkedHashMap<>();
        for (String s : Order.STATUSES) byStatus.put(s, 0);
        Map<String, Integer> byType = new LinkedHashMap<>();
        byType.put("Standard", 0); byType.put("Express", 0);
        Map<String, Integer> itemQty = new HashMap<>();
        Map<String, Integer> promoUse = new TreeMap<>();
        double discounts = 0;
        for (Order o : orders) {
            byStatus.merge(o.getStatus(), 1, Integer::sum);
            if ("Cancelled".equals(o.getStatus())) continue;       // cancelled orders do not count as sales
            byType.merge(o.getLabel(), 1, Integer::sum);
            discounts += o.getDiscount();
            if (!o.getPromoCode().isEmpty()) promoUse.merge(o.getPromoCode(), 1, Integer::sum);
            for (OrderItem i : o.getItems()) itemQty.merge(i.getName(), i.getQty(), Integer::sum);
        }

        // ---- best sellers (top 5)
        List<Map.Entry<String, Integer>> top = new ArrayList<>(itemQty.entrySet());
        top.sort((a, b) -> b.getValue() != a.getValue().intValue() ? b.getValue() - a.getValue() : a.getKey().compareTo(b.getKey()));
        if (top.size() > 5) top = new ArrayList<>(top.subList(0, 5));
        int topMax = top.isEmpty() ? 1 : top.get(0).getValue();

        // ---- money
        double paid = 0, pending = 0, refunded = 0;
        Map<String, Double> perDay = new LinkedHashMap<>();       // last 7 days, oldest first
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) perDay.put(today.minusDays(i).toString(), 0.0);
        for (Payment p : payments) {
            if ("Paid".equals(p.getStatus())) {
                paid += p.getAmount();
                String day = p.getDate().length() >= 10 ? p.getDate().substring(0, 10) : "";
                perDay.computeIfPresent(day, (k, v) -> v + p.getAmount());
            } else if ("Pending".equals(p.getStatus())) pending += p.getAmount();
            else if ("Refunded".equals(p.getStatus())) refunded += p.getAmount();
        }
        double dayMax = 1;
        for (double v : perDay.values()) dayMax = Math.max(dayMax, v);

        // ---- ratings
        int[] stars = new int[6];
        double sum = 0;
        for (Rating r : ratings) { stars[r.getStars()]++; sum += r.getStars(); }
        List<Rating> latest = new ArrayList<>(ratings);
        Collections.reverse(latest);
        if (latest.size() > 8) latest = new ArrayList<>(latest.subList(0, 8));

        req.setAttribute("totalOrders", orders.size());
        req.setAttribute("byStatus", byStatus);
        req.setAttribute("byType", byType);
        req.setAttribute("topItems", top);
        req.setAttribute("topMax", topMax);
        req.setAttribute("promoUse", promoUse);
        req.setAttribute("discounts", discounts);
        req.setAttribute("paid", paid);
        req.setAttribute("pending", pending);
        req.setAttribute("refunded", refunded);
        req.setAttribute("perDay", perDay);
        req.setAttribute("dayMax", dayMax);
        req.setAttribute("ratingCount", ratings.size());
        req.setAttribute("ratingAvg", ratings.isEmpty() ? 0 : sum / ratings.size());
        req.setAttribute("starCounts", stars);
        req.setAttribute("latestRatings", latest);
        req.getRequestDispatcher("/reports.jsp").forward(req, resp);
    }

    private void writeCsv(HttpServletResponse resp, List<Order> orders, Map<Integer, Payment> pay) throws IOException {
        resp.setContentType("text/csv;charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"quickbite-orders.csv\"");
        PrintWriter w = resp.getWriter();
        w.print('\uFEFF');   // so Excel opens it as UTF-8
        w.println("Order,Date,Customer,Type,Status,Subtotal,Promo,Discount,Delivery fee,Total,Payment method,Payment status");
        for (Order o : orders) {
            Payment p = pay.get(o.getId());
            w.println(String.join(",", String.valueOf(o.getId()), csv(o.getDate()), csv(o.getUsername()), csv(o.getLabel()),
                    csv(o.getStatus()), String.valueOf(o.getSubtotal()), csv(o.getPromoCode()), String.valueOf(o.getDiscount()),
                    String.valueOf(o.getDeliveryFee()), String.valueOf(o.getTotal()), csv(o.getPayment()),
                    csv(p == null ? "" : p.getStatus())));
        }
    }

    private String csv(String s) {
        if (s == null) return "";
        return (s.contains(",") || s.contains("\"") || s.contains("\n")) ? "\"" + s.replace("\"", "\"\"") + "\"" : s;
    }
}
