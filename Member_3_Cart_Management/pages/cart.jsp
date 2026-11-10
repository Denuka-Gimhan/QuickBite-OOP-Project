<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Your Cart"/>
<c:set var="extraCss" value="cart.css"/>
<c:set var="extraJs" value="cart.js"/>
<%@ include file="top.jspf" %>

<main class="container">
  <div style="margin-bottom: 24px;">
    <h1 class="page-title" style="margin-bottom: 4px;">Shopping Cart</h1>
    <p style="color: var(--text-muted); font-size: 0.95rem;">Review your selected dishes, apply promo codes, and proceed to checkout.</p>
  </div>

  <c:choose>
    <c:when test="${empty sessionScope.cart.items}">
      <div class="panel center" style="text-align: center; padding: 72px 20px;">
        <div style="font-size: 5rem; margin-bottom: 16px;">🛒</div>
        <h2 style="font-size: 1.8rem; margin-bottom: 8px;">Your cart is empty</h2>
        <p style="color: var(--text-muted); max-width: 440px; margin: 0 auto 28px;">Looks like you haven't added anything to your cart yet. Explore our delicious menu to find your cravings.</p>
        <a class="btn btn-primary" href="${ctx}/foods" style="padding: 12px 32px;">Browse Food Menu</a>
      </div>
    </c:when>

    <c:otherwise>
      <div class="cart-layout">
        <!-- Cart Items List -->
        <div class="cart-items-panel">
          <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-subtle); padding-bottom: 12px;">
            <h2 style="font-size: 1.3rem;">Cart Items (${sessionScope.cart.count})</h2>
            <form method="post" action="${ctx}/cart" data-confirm="Are you sure you want to remove all items from your cart?">
              <input type="hidden" name="action" value="clear">
              <button class="btn-sm danger" type="submit">Clear Cart</button>
            </form>
          </div>

          <c:forEach var="i" items="${sessionScope.cart.items}">
            <div class="cart-item-row">
              <!-- Food Photo Thumbnail / Emoji (Part D) -->
              <div class="cart-thumb-box">
                <c:choose>
                  <c:when test="${not empty i.imageFile}">
                    <img src="${ctx}/food-image?name=<c:out value='${i.imageFile}'/>" alt="<c:out value='${i.name}'/>" loading="lazy">
                  </c:when>
                  <c:otherwise>
                    <span class="cart-thumb-emoji"><c:out value="${i.emoji}"/></span>
                  </c:otherwise>
                </c:choose>
              </div>

              <!-- Item Information -->
              <div class="cart-item-details">
                <div class="cart-item-title"><c:out value="${i.name}"/></div>
                <div class="cart-item-unit-price">Rs. <fmt:formatNumber value="${i.price}" pattern="#,##0.00"/> each</div>
              </div>

              <!-- Quantity Stepper Form -->
              <div class="qty-stepper">
                <form method="post" action="${ctx}/cart" style="margin: 0;">
                  <input type="hidden" name="action" value="dec">
                  <input type="hidden" name="id" value="${i.foodId}">
                  <button type="submit" aria-label="Decrease quantity">&minus;</button>
                </form>
                <span>${i.qty}</span>
                <form method="post" action="${ctx}/cart" style="margin: 0;">
                  <input type="hidden" name="action" value="inc">
                  <input type="hidden" name="id" value="${i.foodId}">
                  <button type="submit" aria-label="Increase quantity">+</button>
                </form>
              </div>

              <!-- Line Total -->
              <div class="cart-item-line-total">
                Rs. <fmt:formatNumber value="${i.lineTotal}" pattern="#,##0.00"/>
              </div>

              <!-- Remove Item Button -->
              <form method="post" action="${ctx}/cart" style="margin: 0;">
                <input type="hidden" name="action" value="remove">
                <input type="hidden" name="id" value="${i.foodId}">
                <button class="btn-sm danger" type="submit" aria-label="Remove item" title="Remove item">
                  <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>
                </button>
              </form>
            </div>
          </c:forEach>
        </div>

        <!-- Cart Summary Sidebar -->
        <aside class="cart-summary-card">
          <h2 class="cart-summary-title">Order Summary</h2>

          <div class="summary-line">
            <span>Total Items</span>
            <strong>${sessionScope.cart.count}</strong>
          </div>

          <div class="summary-line">
            <span>Subtotal</span>
            <strong>Rs. <fmt:formatNumber value="${sessionScope.cart.subtotal}" pattern="#,##0.00"/></strong>
          </div>

          <!-- Promo Code Box (Member 3) -->
          <div class="promo-box-container">
            <c:choose>
              <c:when test="${not empty sessionScope.cart.promo}">
                <div class="promo-active-badge">
                  <span>🎉 Promo <strong><c:out value="${sessionScope.cart.promoCode}"/></strong> (${sessionScope.cart.promo.percent}% off)</span>
                  <form method="post" action="${ctx}/cart" style="margin: 0;">
                    <input type="hidden" name="action" value="removepromo">
                    <button class="btn-sm danger" style="padding: 2px 8px; font-size: 0.75rem;" type="submit">&times; Remove</button>
                  </form>
                </div>
              </c:when>
              <c:otherwise>
                <form class="promo-form-group" method="post" action="${ctx}/cart">
                  <input type="hidden" name="action" value="promo">
                  <input name="code" maxlength="20" placeholder="PROMO CODE" aria-label="Promo code">
                  <button class="btn btn-primary btn-sm" type="submit">Apply</button>
                </form>
                <c:if test="${not empty activePromos}">
                  <div style="margin-top: 10px;">
                    <small style="color: var(--text-muted); font-size: 0.76rem; display: block; margin-bottom: 4px;">Available Promo Codes:</small>
                    <c:forEach var="pc" items="${activePromos}">
                      <a href="#" class="promo-code-chip promo-suggestion" data-code="${pc.code}" style="color: var(--brand); font-weight: 700; text-decoration: underline;">
                        ${pc.code} (${pc.percent}% off orders over Rs. <fmt:formatNumber value="${pc.minSubtotal}" pattern="#,##0"/>)
                      </a>
                    </c:forEach>
                  </div>
                </c:if>
              </c:otherwise>
            </c:choose>
          </div>

          <c:if test="${sessionScope.cart.discount > 0}">
            <div class="summary-line promo-discount-line">
              <span>Promo Discount</span>
              <span>&minus; Rs. <fmt:formatNumber value="${sessionScope.cart.discount}" pattern="#,##0.00"/></span>
            </div>
          </c:if>

          <div class="summary-line total-line">
            <span>Estimated Total</span>
            <span>Rs. <fmt:formatNumber value="${sessionScope.cart.subtotalAfterDiscount}" pattern="#,##0.00"/></span>
          </div>

          <small style="color: var(--text-muted); font-size: 0.82rem; text-align: center;">Delivery fee is calculated at checkout.</small>

          <a class="btn btn-primary block" href="${ctx}/orders?action=checkout" style="padding: 13px; font-size: 1rem;">
            Proceed to Checkout
            <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
          </a>

          <a class="btn btn-ghost block" href="${ctx}/foods" style="font-size: 0.9rem;">
            Continue Shopping
          </a>
        </aside>
      </div>
    </c:otherwise>
  </c:choose>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
