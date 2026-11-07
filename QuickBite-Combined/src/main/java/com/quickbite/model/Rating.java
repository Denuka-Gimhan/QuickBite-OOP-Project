package com.quickbite.model;

/** A customer's star rating (1-5) and short comment for a delivered order. */
public class Rating {
    private int id;
    private final int orderId;
    private final int userId;
    private final String username;
    private final int stars;
    private final String comment;
    private final String date;

    public Rating(int id, int orderId, int userId, String username, int stars, String comment, String date) {
        this.id = id; this.orderId = orderId; this.userId = userId; this.username = username;
        this.stars = stars; this.comment = comment; this.date = date;
    }

    /** id|orderId|userId|username|stars|date|comment   (comment last, it is free text) */
    public String toFileLine() {
        return String.join("|", String.valueOf(id), String.valueOf(orderId), String.valueOf(userId),
                username, String.valueOf(stars), date, comment);
    }

    /** e.g. "★★★★☆" */
    public String getStarText() { return "★".repeat(stars) + "☆".repeat(5 - stars); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrderId() { return orderId; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public int getStars() { return stars; }
    public String getComment() { return comment; }
    public String getDate() { return date; }
}
