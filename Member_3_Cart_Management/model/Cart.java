package com.quickbite.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/** Shopping cart kept in the HTTP session. Can hold one promo code. */
public class Cart implements java.io.Serializable {
    private final Map<Integer, CartItem> items = new LinkedHashMap<>();
    private PromoCode promo;

    public void add(Food f) {
        CartItem existing = items.get(f.getId());
        if (existing == null) items.put(f.getId(), new CartItem(f.getId(), f.getName(), f.getPrice(), f.getEmoji(), f.getImageFile(), 1));
        else existing.setQty(existing.getQty() + 1);
    }

    public void change(int foodId, int delta) {
        CartItem item = items.get(foodId);
        if (item == null) return;
        item.setQty(item.getQty() + delta);
        if (item.getQty() <= 0) items.remove(foodId);
    }

    public void remove(int foodId) { items.remove(foodId); }
    public void clear() { items.clear(); promo = null; }

    public Collection<CartItem> getItems() { return items.values(); }
    public boolean isEmpty() { return items.isEmpty(); }

    public int getCount() {
        int n = 0;
        for (CartItem i : items.values()) n += i.getQty();
        return n;
    }

    public double getSubtotal() {
        double t = 0;
        for (CartItem i : items.values()) t += i.getLineTotal();
        return t;
    }

    // ---- promo code ----
    public PromoCode getPromo() { return promo; }
    public void applyPromo(PromoCode p) { this.promo = p; }
    public void removePromo() { this.promo = null; }
    public String getPromoCode() { return promo == null ? "" : promo.getCode(); }

    /** Money taken off the subtotal (0 when there is no promo or the minimum spend is not reached). */
    public double getDiscount() { return promo == null ? 0 : promo.discountFor(getSubtotal()); }
    public double getSubtotalAfterDiscount() { return getSubtotal() - getDiscount(); }
}
