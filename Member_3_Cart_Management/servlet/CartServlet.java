package com.quickbite.servlet;

import com.quickbite.dao.FoodDAO;
import com.quickbite.dao.PromoCodeDAO;
import com.quickbite.model.Cart;
import com.quickbite.model.Food;
import com.quickbite.model.PromoCode;
import com.quickbite.util.Web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private FoodDAO foods;
    private PromoCodeDAO promos;

    @Override
    public void init() throws ServletException {
        try {
            foods = Web.foods(getServletContext());
            promos = Web.promos(getServletContext());
        } catch (IOException e) { throw new ServletException(e); }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = Web.cart(req);
        // prices / stock may have changed since the item was added: refresh, and tell the customer once
        String changes = Web.syncCart(getServletContext(), cart);
        if (!changes.isEmpty()) { Web.redirect(resp, "cart", changes); return; }
        java.util.List<PromoCode> active = new java.util.ArrayList<>();
        for (PromoCode p : promos.getAll()) if (p.isActive()) active.add(p);
        req.setAttribute("activePromos", active);
        req.getRequestDispatcher("/cart.jsp").forward(req, resp);
    }

    /** action = add | inc | dec | remove | clear | promo | removepromo. Prices always come from foods.txt, never from the browser. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Cart cart = Web.cart(req);
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");
        int id = Web.parseInt(req.getParameter("id"));

        switch (action) {
            case "add": {
                Food f = foods.getById(id);
                if (f == null) { Web.redirect(resp, "foods", "That food is no longer available."); return; }
                if (!f.isAvailable()) { Web.redirect(resp, "foods", f.getName() + " is out of stock."); return; }
                cart.add(f);
                Web.redirect(resp, "foods", f.getName() + " added to cart.");
                return;
            }
            case "inc":    cart.change(id, 1); break;
            case "dec":    cart.change(id, -1); break;
            case "remove": cart.remove(id); break;
            case "clear":  cart.clear(); break;
            case "removepromo": cart.removePromo(); Web.redirect(resp, "cart", "Promo code removed."); return;
            case "promo": {
                String code = Web.clean(req.getParameter("code"));
                PromoCode p = promos.findByCode(code);
                String msg;
                if (code.isEmpty()) msg = "Enter a promo code.";
                else if (p == null || !p.isActive()) msg = "Invalid promo code.";
                else if (!p.isApplicable(cart.getSubtotal())) msg = p.getCode() + " needs a cart of at least Rs. " + String.format("%,.0f", p.getMinSubtotal()) + ".";
                else { cart.applyPromo(p); msg = p.getCode() + " applied: " + p.getPercent() + "% off."; }
                Web.redirect(resp, "cart", msg);
                return;
            }
            default: break;
        }
        // an item removed / decreased can drop the cart below the promo's minimum spend
        Web.syncCart(getServletContext(), cart);
        resp.sendRedirect("cart");
    }
}
