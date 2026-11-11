package com.quickbite.servlet;

import com.quickbite.dao.OrderDAO;
import com.quickbite.dao.RatingDAO;
import com.quickbite.model.Order;
import com.quickbite.model.Rating;
import com.quickbite.model.User;
import com.quickbite.util.Web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Customers rate a delivered order (1-5 stars + comment). Admins can delete a rating. */
@WebServlet("/ratings")
public class RatingServlet extends HttpServlet {
    private RatingDAO ratings;
    private OrderDAO orders;

    @Override
    public void init() throws ServletException {
        try {
            ratings = Web.ratings(getServletContext());
            orders = Web.orders(getServletContext());
        } catch (IOException e) { throw new ServletException("Cannot open ratings file", e); }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(Web.isAdmin(req) ? "reports" : "orders");
    }

    /** action = add | delete */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User me = Web.user(req);
        if (me == null) { Web.redirect(resp, "users?action=login", "Please log in to continue."); return; }
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");

        if (action.equals("delete")) {
            boolean ok = me.isAdmin() && ratings.delete(Web.parseInt(req.getParameter("id")));
            Web.redirect(resp, "reports", ok ? "Rating deleted." : "Could not delete the rating.");
            return;
        }

        int orderId = Web.parseInt(req.getParameter("orderId"));
        int stars = Web.parseInt(req.getParameter("stars"));
        String comment = Web.clean(req.getParameter("comment"));
        if (comment.length() > 150) comment = comment.substring(0, 150);

        Order o = orders.getById(orderId);
        String msg;
        if (o == null || o.getUserId() != me.getId()) msg = "Order not found.";
        else if (!"Delivered".equals(o.getStatus())) msg = "You can rate an order after it is delivered.";
        else if (stars < 1 || stars > 5) msg = "Choose 1 to 5 stars.";
        else if (!ratings.add(new Rating(0, orderId, me.getId(), me.getUsername(), stars, comment, Web.now()))) msg = "You have already rated this order.";
        else msg = "Thank you for rating order #" + orderId + "!";
        Web.redirect(resp, "orders", msg);
    }
}
