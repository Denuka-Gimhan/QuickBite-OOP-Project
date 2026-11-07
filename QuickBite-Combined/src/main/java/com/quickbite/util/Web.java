package com.quickbite.util;

import com.quickbite.dao.*;
import com.quickbite.model.*;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Small helpers shared by ALL servlets (shared file - change it only after telling the team).
 * Every DAO is created ONCE here and shared, so two servlets never write the same .txt file through
 * two different objects (that was a source of lost updates).
 */
public final class Web {
    private Web() {}

    // ------------------------------------------------------------------ data folder + shared DAOs
    public static String dataDir(ServletContext ctx) {
        String dir = ctx.getInitParameter("dataDir");
        return (dir == null || dir.isBlank()) ? System.getProperty("user.home") + "/quickbite-data" : dir;
    }

    private interface IoSupplier<T> { T get() throws IOException; }

    @SuppressWarnings("unchecked")
    private static <T> T singleton(ServletContext ctx, String key, IoSupplier<T> make) throws IOException {
        synchronized (ctx) {
            Object o = ctx.getAttribute(key);
            if (o == null) { o = make.get(); ctx.setAttribute(key, o); }
            return (T) o;
        }
    }

    public static UserDAO users(ServletContext c) throws IOException { return singleton(c, "dao.users", () -> new UserDAO(dataDir(c))); }
    public static FoodDAO foods(ServletContext c) throws IOException { return singleton(c, "dao.foods", () -> new FoodDAO(dataDir(c))); }
    public static OrderDAO orders(ServletContext c) throws IOException { return singleton(c, "dao.orders", () -> new OrderDAO(dataDir(c))); }
    public static PromoCodeDAO promos(ServletContext c) throws IOException { return singleton(c, "dao.promos", () -> new PromoCodeDAO(dataDir(c))); }
    public static DeliveryPersonDAO drivers(ServletContext c) throws IOException { return singleton(c, "dao.drivers", () -> new DeliveryPersonDAO(dataDir(c))); }
    public static DeliveryDAO deliveries(ServletContext c) throws IOException { return singleton(c, "dao.deliveries", () -> new DeliveryDAO(dataDir(c))); }
    public static PaymentDAO payments(ServletContext c) throws IOException { return singleton(c, "dao.payments", () -> new PaymentDAO(dataDir(c))); }
    public static RatingDAO ratings(ServletContext c) throws IOException { return singleton(c, "dao.ratings", () -> new RatingDAO(dataDir(c))); }

    /** The FIFO queue of Pending orders. Rebuilt from orders.txt (oldest first) the first time it is needed. */
    public static OrderQueue queue(ServletContext ctx) throws IOException {
        return singleton(ctx, "orderQueue", () -> {
            List<Order> pending = new ArrayList<>();
            for (Order o : orders(ctx).getAll()) if ("Pending".equals(o.getStatus())) pending.add(o);
            pending.sort(Comparator.comparingInt(Order::getId));
            OrderQueue q = new OrderQueue();
            for (Order o : pending) q.enqueue(o.getId());
            return q;
        });
    }

    // ------------------------------------------------------------------ session helpers
    /**
     * The logged-in user, re-read from users.txt on every call so that a deleted account is logged out
     * immediately and a changed role / email takes effect at once.
     */
    public static User user(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return null;
        User sessionUser = (User) s.getAttribute("user");
        if (sessionUser == null) return null;
        try {
            User fresh = users(req.getServletContext()).getById(sessionUser.getId());
            if (fresh == null) { s.invalidate(); return null; }
            s.setAttribute("user", fresh);
            return fresh;
        } catch (IOException e) {
            return sessionUser;
        }
    }

    public static boolean isAdmin(HttpServletRequest req) {
        User u = user(req);
        return u != null && u.isAdmin();
    }

    /** The shopping cart lives in the session (one per browser). */
    public static Cart cart(HttpServletRequest req) {
        HttpSession s = req.getSession();
        Cart c = (Cart) s.getAttribute("cart");
        if (c == null) { c = new Cart(); s.setAttribute("cart", c); }
        return c;
    }

    /**
     * Brings the cart in line with the menu: new prices are applied, deleted / out-of-stock foods are removed,
     * and a promo that is no longer valid is dropped. Returns a message for the customer ("" = nothing changed).
     */
    public static String syncCart(ServletContext ctx, Cart cart) throws IOException {
        List<String> notes = new ArrayList<>();
        FoodDAO foods = foods(ctx);
        for (CartItem item : new ArrayList<>(cart.getItems())) {
            Food f = foods.getById(item.getFoodId());
            if (f == null || !f.isAvailable()) {
                cart.remove(item.getFoodId());
                notes.add(item.getName() + " is no longer available and was removed");
            } else if (item.refreshFrom(f)) {
                notes.add(item.getName() + " price changed to Rs. " + String.format("%,.2f", f.getPrice()));
            }
        }
        PromoCode applied = cart.getPromo();
        if (applied != null) {
            PromoCode fresh = promos(ctx).findByCode(applied.getCode());
            if (fresh == null || !fresh.isActive()) {
                cart.removePromo();
                notes.add("Promo " + applied.getCode() + " is no longer valid");
            } else if (!fresh.isApplicable(cart.getSubtotal())) {
                cart.removePromo();
                notes.add("Promo " + applied.getCode() + " removed (minimum spend Rs. " + String.format("%,.0f", fresh.getMinSubtotal()) + ")");
            } else {
                cart.applyPromo(fresh);
            }
        }
        return String.join(". ", notes);
    }

    // ------------------------------------------------------------------ small utilities
    /** Remove characters that would break the .txt file format ('|' separates the columns). */
    public static String clean(String s) {
        return s == null ? "" : s.replaceAll("[|\\r\\n]+", " ").trim();
    }

    public static int parseInt(String s) {
        try { return Integer.parseInt(s == null ? "" : s.trim()); } catch (Exception e) { return 0; }
    }

    public static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static void redirect(HttpServletResponse resp, String target, String msg) throws IOException {
        String sep = target.contains("?") ? "&" : "?";
        resp.sendRedirect(target + sep + "msg=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
    }
}
