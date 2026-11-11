package com.quickbite.servlet;

import com.quickbite.dao.FoodDAO;
import com.quickbite.model.Food;
import com.quickbite.model.FoodFactory;
import com.quickbite.util.Web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/** The form can send a photo, so this servlet accepts multipart requests (photo max 5 MB). */
@WebServlet("/foods")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = FoodServlet.MAX_IMAGE_BYTES, maxRequestSize = FoodServlet.MAX_IMAGE_BYTES + 512 * 1024)
public class FoodServlet extends HttpServlet {
    static final long MAX_IMAGE_BYTES = 5L * 1024 * 1024;
    private FoodDAO dao;

    @Override
    public void init() throws ServletException {
        try {
            dao = Web.foods(getServletContext());
            log("Food data folder: " + Web.dataDir(getServletContext()));
        } catch (IOException e) {
            throw new ServletException("Cannot open data file", e);
        }
    }

    /** GET: list/search/sort (default), blank add form (action=new), edit form (action=edit&id=..). */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action") == null ? "list" : req.getParameter("action");
        req.setAttribute("categories", Food.CATEGORIES);

        if ((action.equals("new") || action.equals("edit")) && !Web.isAdmin(req)) {
            Web.redirect(resp, "users?action=login", "Admin login required.");
            return;
        }

        if (action.equals("new")) {
            req.setAttribute("editing", false);
            req.getRequestDispatcher("/food-form.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            Food food = dao.getById(Web.parseInt(req.getParameter("id")));
            if (food == null) { Web.redirect(resp, "foods", "Food not found."); return; }
            req.setAttribute("editing", true);
            req.setAttribute("food", food);
            req.getRequestDispatcher("/food-form.jsp").forward(req, resp);
        } else {
            // sort = price_asc | price_desc  (done with our own Quick Sort inside FoodDAO.search)
            req.setAttribute("foods", dao.search(req.getParameter("q"), req.getParameter("cat"), req.getParameter("sort")));
            req.getRequestDispatcher("/foods.jsp").forward(req, resp);
        }
    }

    /** POST: action = add | update | delete | stock (toggle in stock / out of stock) */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action;
        try {
            action = req.getParameter("action") == null ? "" : req.getParameter("action");
        } catch (IllegalStateException tooBig) {   // Tomcat refuses uploads above the 5 MB limit
            Web.redirect(resp, "foods", "Upload failed: the photo must be 5 MB or smaller.");
            return;
        }

        if (!Web.isAdmin(req)) {
            Web.redirect(resp, "users?action=login", "Admin login required.");
            return;
        }

        if (action.equals("delete")) {
            int id = Web.parseInt(req.getParameter("id"));
            Food old = dao.getById(id);
            boolean ok = dao.delete(id);
            if (ok && old != null) deleteImageFile(old.getImageFile());
            Web.redirect(resp, "foods", ok ? "Food deleted." : "Food not found.");
            return;
        }

        if (action.equals("stock")) {
            Food f = dao.getById(Web.parseInt(req.getParameter("id")));
            if (f == null) { Web.redirect(resp, "foods", "Food not found."); return; }
            f.setAvailable(!f.isAvailable());
            dao.update(f);
            Web.redirect(resp, "foods", f.getName() + (f.isAvailable() ? " is back in stock." : " is now out of stock."));
            return;
        }

        Food food = readFood(req);
        boolean updating = action.equals("update");

        // keep the photo that is already saved unless a new one is uploaded / removed below
        String existingImage = "";
        if (updating) {
            Food old = dao.getById(food.getId());
            if (old != null) existingImage = old.getImageFile();
        }
        food.setImageFile(existingImage);

        String error = validate(food);
        String newImage = null;
        if (error == null) {
            try {
                newImage = saveUploadedImage(req);
            } catch (IllegalArgumentException e) {
                error = e.getMessage();
            }
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("editing", updating);
            req.setAttribute("food", food);
            req.setAttribute("categories", Food.CATEGORIES);
            req.getRequestDispatcher("/food-form.jsp").forward(req, resp);
            return;
        }

        if (newImage != null) {                                         // new photo replaces the old one
            food.setImageFile(newImage);
            deleteImageFile(existingImage);
        } else if ("true".equals(req.getParameter("removeImage"))) {    // "Remove current photo" ticked
            food.setImageFile("");
            deleteImageFile(existingImage);
        }

        if (updating) {
            boolean ok = dao.update(food);
            Web.redirect(resp, "foods", ok ? "Food updated." : "Food not found.");
        } else {
            dao.add(food);
            Web.redirect(resp, "foods", "Food added.");
        }
    }

    /**
     * Saves the uploaded photo (field name "image") to  <dataDir>/images  under a random name.
     * Returns the new file name, or null when no photo was chosen.
     * Throws IllegalArgumentException with a message for the user when the file is not acceptable.
     */
    private String saveUploadedImage(HttpServletRequest req) throws IOException, ServletException {
        String type = req.getContentType();
        if (type == null || !type.toLowerCase().startsWith("multipart/")) return null;

        Part part;
        try {
            part = req.getPart("image");
        } catch (IllegalStateException tooBig) {
            throw new IllegalArgumentException("The photo must be 5 MB or smaller.");
        }
        if (part == null || part.getSize() == 0) return null;
        if (part.getSize() > MAX_IMAGE_BYTES) throw new IllegalArgumentException("The photo must be 5 MB or smaller.");

        byte[] data = part.getInputStream().readAllBytes();
        String ext = detectImageType(data);
        if (ext == null) throw new IllegalArgumentException("Please choose a JPG, PNG, WEBP or GIF image.");

        String name = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Path file = FoodImageServlet.imagePath(Web.dataDir(getServletContext()), name);
        Files.createDirectories(file.getParent());
        Files.write(file, data);
        return name;
    }

    /** Looks at the first bytes of the file (not the file name) to be sure it is really an image. */
    private static String detectImageType(byte[] b) {
        if (b.length >= 3 && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF) return "jpg";
        if (b.length >= 8 && (b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G') return "png";
        if (b.length >= 6 && b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8') return "gif";
        if (b.length >= 12 && b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F'
                && b[8] == 'W' && b[9] == 'E' && b[10] == 'B' && b[11] == 'P') return "webp";
        return null;
    }

    private void deleteImageFile(String name) {
        if (!FoodImageServlet.isValidName(name)) return;
        try {
            Files.deleteIfExists(FoodImageServlet.imagePath(Web.dataDir(getServletContext()), name));
        } catch (IOException e) {
            log("Could not delete image " + name, e);
        }
    }

    private Food readFood(HttpServletRequest r) {
        double price;
        try { price = Double.parseDouble(r.getParameter("price")); } catch (Exception e) { price = -1; }
        String emoji = Web.clean(r.getParameter("emoji"));
        return FoodFactory.create(Web.parseInt(r.getParameter("id")), Web.clean(r.getParameter("name")),
                r.getParameter("category"), price, Web.clean(r.getParameter("description")),
                emoji.isEmpty() ? "🍽️" : emoji, "true".equals(r.getParameter("available")));
    }

    private String validate(Food f) {
        if (f.getName().isEmpty()) return "Food name is required.";
        if (f.getPrice() <= 0 || Double.isNaN(f.getPrice()) || Double.isInfinite(f.getPrice())) return "Enter a valid price greater than 0.";
        return null;
    }
}
