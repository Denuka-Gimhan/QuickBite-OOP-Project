package com.quickbite.servlet;

import com.quickbite.dao.UserDAO;
import com.quickbite.model.RegularUser;
import com.quickbite.model.User;
import com.quickbite.util.PasswordUtil;
import com.quickbite.util.Web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private static final java.util.regex.Pattern EMAIL = java.util.regex.Pattern.compile("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
    private UserDAO dao;

    @Override
    public void init() throws ServletException {
        try { dao = Web.users(getServletContext()); }
        catch (IOException e) { throw new ServletException("Cannot open users file", e); }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");
        User me = Web.user(req);

        switch (action) {
            case "register":
                forward(req, resp, "/register.jsp");
                break;
            case "logout":
                if (req.getSession(false) != null) req.getSession(false).invalidate();
                Web.redirect(resp, "foods", "You have been logged out.");
                break;
            case "profile":
                if (me == null) { Web.redirect(resp, "users?action=login", "Please log in first."); return; }
                req.setAttribute("profileUser", dao.getById(me.getId()));
                forward(req, resp, "/profile.jsp");
                break;
            case "list":
                if (me == null || !me.isAdmin()) { Web.redirect(resp, "foods", "Admins only."); return; }
                req.setAttribute("users", dao.search(req.getParameter("q")));
                forward(req, resp, "/users.jsp");
                break;
            default:
                forward(req, resp, "/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");

        switch (action) {
            case "login":    login(req, resp); break;
            case "register": register(req, resp); break;
            case "update":   update(req, resp); break;
            case "delete":   delete(req, resp); break;
            default:         Web.redirect(resp, "foods", "Unknown action.");
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Web.clean(req.getParameter("username"));
        User u = dao.authenticate(username, req.getParameter("password") == null ? "" : req.getParameter("password"));
        if (u == null) {
            req.setAttribute("error", "Wrong username or password.");
            forward(req, resp, "/login.jsp");
            return;
        }
        req.getSession().setAttribute("user", u);
        Web.redirect(resp, "foods", "Welcome back, " + u.getUsername() + "!");
    }

    private void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Web.clean(req.getParameter("username"));
        String email = Web.clean(req.getParameter("email"));
        String phone = Web.clean(req.getParameter("phone"));
        String address = Web.clean(req.getParameter("address"));
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        String confirm = req.getParameter("confirm") == null ? "" : req.getParameter("confirm");

        User draft = new RegularUser(0, username, "", email, phone, address);
        String error = null;
        if (!username.matches("[A-Za-z0-9_]{3,20}")) error = "Username must be 3-20 letters, numbers or underscores.";
        else if (!EMAIL.matcher(email).matches()) error = "Enter a valid email address.";
        else if (!phone.matches("[0-9+ ]{9,15}")) error = "Enter a valid phone number.";
        else if (password.length() < 6) error = "Password must be at least 6 characters.";
        else if (!password.equals(confirm)) error = "Passwords do not match.";
        else if (dao.findByUsername(username) != null) error = "That username is already taken.";
        else if (dao.findByEmail(email) != null) error = "That email is already registered. Try logging in.";

        if (error == null) {
            draft.setPasswordHash(PasswordUtil.hash(password));
            if (!dao.add(draft)) error = "Could not create the account. Please try again.";
        }
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("form", draft);
            forward(req, resp, "/register.jsp");
            return;
        }
        req.getSession().setAttribute("user", draft);
        Web.redirect(resp, "foods", "Account created. Welcome, " + username + "!");
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User me = Web.user(req);
        if (me == null) { Web.redirect(resp, "users?action=login", "Please log in first."); return; }

        User u = dao.getById(me.getId());
        if (u == null) { Web.redirect(resp, "users?action=login", "Please log in first."); return; }
        u.setEmail(Web.clean(req.getParameter("email")));
        u.setPhone(Web.clean(req.getParameter("phone")));
        u.setAddress(Web.clean(req.getParameter("address")));
        String newPass = req.getParameter("password") == null ? "" : req.getParameter("password");

        String error = null;
        User sameEmail = dao.findByEmail(u.getEmail());
        if (!EMAIL.matcher(u.getEmail()).matches()) error = "Enter a valid email address.";
        else if (sameEmail != null && sameEmail.getId() != u.getId()) error = "That email is already used by another account.";
        else if (!u.getPhone().matches("[0-9+ ]{9,15}")) error = "Enter a valid phone number.";
        else if (!newPass.isEmpty() && newPass.length() < 6) error = "New password must be at least 6 characters.";

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("profileUser", u);
            forward(req, resp, "/profile.jsp");
            return;
        }
        if (!newPass.isEmpty()) u.setPasswordHash(PasswordUtil.hash(newPass));
        dao.update(u);
        req.getSession().setAttribute("user", u);
        Web.redirect(resp, "users?action=profile", "Profile updated.");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User me = Web.user(req);
        if (me == null) { Web.redirect(resp, "users?action=login", "Please log in first."); return; }

        int id = Web.parseInt(req.getParameter("id"));
        boolean self = (id == 0 || id == me.getId());
        if (!self && !me.isAdmin()) { Web.redirect(resp, "foods", "Not allowed."); return; }
        if (self && me.isAdmin() && dao.countAdmins() <= 1) {
            Web.redirect(resp, "users?action=profile", "You are the only admin, so this account cannot be deleted.");
            return;
        }
        dao.delete(self ? me.getId() : id);
        if (self) {
            req.getSession().invalidate();
            Web.redirect(resp, "foods", "Your account was deleted.");
        } else {
            Web.redirect(resp, "users?action=list", "User deleted.");
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String page) throws ServletException, IOException {
        req.getRequestDispatcher(page).forward(req, resp);
    }
}
