package com.quickbite.servlet;

import com.quickbite.util.Web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/** Sends an uploaded food photo from  <dataDir>/images  to the browser:  /food-image?name=abc123.jpg */
@WebServlet("/food-image")
public class FoodImageServlet extends HttpServlet {
    /** Only names we generated ourselves are allowed, so nobody can ask for other files on the disk. */
    private static final Pattern VALID_NAME = Pattern.compile("[0-9a-f]{32}\\.(jpg|png|gif|webp)");

    public static boolean isValidName(String name) {
        return name != null && VALID_NAME.matcher(name).matches();
    }

    public static Path imagePath(String dataDir, String name) {
        return Paths.get(dataDir, "images", name);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if (!isValidName(name)) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }

        Path file = imagePath(Web.dataDir(getServletContext()), name);
        if (!Files.isRegularFile(file)) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }

        String ext = name.substring(name.lastIndexOf('.') + 1);
        resp.setContentType(ext.equals("jpg") ? "image/jpeg" : "image/" + ext);
        resp.setContentLengthLong(Files.size(file));
        resp.setHeader("Cache-Control", "public, max-age=86400");   // every upload gets a new name, so caching is safe
        resp.setHeader("X-Content-Type-Options", "nosniff");
        Files.copy(file, resp.getOutputStream());
    }
}
