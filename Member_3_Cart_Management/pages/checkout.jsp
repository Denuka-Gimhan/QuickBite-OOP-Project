<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Checkout"/>
<c:set var="extraCss" value="checkout.css"/>
<c:set var="extraJs" value="checkout.js"/>
<%@ include file="top.jspf" %>

<main class="container">
  <!-- Step Indicator -->
  <div class="checkout-steps">
    <div class="checkout-step-node">
      <span class="checkout-step-badge" style="background: var(--accent-green); color: #fff;">✓</span>
      <span>Cart</span>
    </div>
    <div class="checkout-step-divider"></div>
    <div class="checkout-step-node active">
      <span class="checkout-step-badge">2</span>
      <span>Delivery &amp; Details</span>
    </div>
    <div class="checkout-step-divider"></div>
    <div class="checkout-step-node">
      <span class="checkout-step-badge">3</span>
      <span>Confirmation</span>
    </div>
  </div>

  <form method="post" action="${ctx}/orders" class="cart-layout">
    <input type="hidden" name="action" value="place">

    <!-- Left: Delivery & Payment Details -->
    <div class="cart-items-panel">
      <h2 style="font-size: 1.5rem; margin-bottom: 4px;">Delivery Information</h2>
      <p style="color: var(--text-muted); font-size: 0.92rem; margin-bottom: 20px;">Please provide the delivery destination and contact number for rider updates.</p>

      <c:if test="${not empty error}">
        <div class="flash error"><c:out value="${error}"/></div>
      </c:if>

      <div class="form-grid">
        <label class="full">
          Delivery Address <span style="color: var(--brand);">*</span>
          <input required name="address" value="<c:out value='${empty param.address ? sessionScope.user.address : param.address}'/>" placeholder="No. 12, Beach Road, Negombo">
        </label>
        <label class="full">
          Contact Phone <span style="color: var(--brand);">*</span>
          <input required type="tel" name="phone" value="<c:out value='${empty param.phone ? sessionScope.user.phone : param.phone}'/>" placeholder="0771234567">
        </label>
      </div>

      <!-- Delivery Speed Selection -->
      <div style="margin-top: 16px;">
        <label style="font-weight: 700; font-size: 1rem; color: var(--text-main); margin-bottom: 10px; display: block;">Select Delivery Speed</label>
        <div class="radio-options-grid">
          <label class="radio-card-option">
            <input type="radio" name="type" value="STANDARD" data-fee="${standardFee}" checked>
            <span class="radio-card-title">🚴 Standard Delivery</span>
            <span class="radio-card-desc">Estimated 40-60 minutes</span>
            <span style="margin-top: 8px; font-weight: 700; color: var(--brand); font-size: 0.9rem;">
              <c:choose>
                <c:when test="${standardFee == 0}">Free Delivery</c:when>
                <c:otherwise>Rs. <fmt:formatNumber value="${standardFee}" pattern="#,##0"/> (Free over Rs. 5,000)</c:otherwise>
              </c:choose>
            </span>
          </label>

          <label class="radio-card-option">
            <input type="radio" name="type" value="EXPRESS" data-fee="${expressFee}">
            <span class="radio-card-title">⚡ Express Delivery</span>
            <span class="radio-card-desc">Priority rush: 20-30 minutes</span>
            <span style="margin-top: 8px; font-weight: 700; color: var(--brand); font-size: 0.9rem;">
              Rs. <fmt:formatNumber value="${expressFee}" pattern="#,##0"/> Flat Fee
            </span>
          </label>
        </div>
      </div>

      <!-- Payment Method Selection -->
      <div style="margin-top: 8px;">
        <label style="font-weight: 700; font-size: 1rem; color: var(--text-main); margin-bottom: 10px; display: block;">Select Payment Method</label>
        <div class="radio-options-grid">
          <label class="radio-card-option">
            <input type="radio" name="payment" value="CASH" checked>
            <span class="radio-card-title">💵 Cash on Delivery</span>
            <span class="radio-card-desc">Pay with cash when the rider arrives at your door.</span>
          </label>
          <label class="radio-card-option">
            <input type="radio" name="payment" value="CARD">
            <span class="radio-card-title">💳 Card on Delivery</span>
            <span class="radio-card-desc">The delivery rider brings a portable POS terminal.</span>
          </label>
        </div>
      </div>
    </div>

    <!-- Right: Order Summary Sidebar -->
    <aside class="cart-summary-card">
      <h3 class="cart-summary-title">Order Items (${sessionScope.cart.count})</h3>

      <!-- Item rows with thumbnail photos (Part D) -->
      <div style="max-height: 280px; overflow-y: auto; display: flex; flex-direction: column; gap: 10px; padding-right: 4px;">
        <c:forEach var="i" items="${sessionScope.cart.items}">
          <div style="display: flex; align-items: center; gap: 12px; font-size: 0.9rem;">
            <div style="width: 44px; height: 44px; border-radius: var(--radius-xs); overflow: hidden; background: var(--bg-muted); display: grid; place-items: center; flex-shrink: 0; border: 1px solid var(--border-subtle);">
              <c:choose>
                <c:when test="${not empty i.imageFile}">
                  <img src="${ctx}/food-image?name=<c:out value='${i.imageFile}'/>" alt="<c:out value='${i.name}'/>" style="width: 100%; height: 100%; object-fit: cover;">
                </c:when>
                <c:otherwise>
                  <span style="font-size: 1.4rem;"><c:out value="${i.emoji}"/></span>
                </c:otherwise>
              </c:choose>
            </div>
            <div style="flex: 1; min-width: 0;">
              <strong style="display: block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;"><c:out value="${i.name}"/></strong>
              <small style="color: var(--text-muted);">${i.qty} &times; Rs. <fmt:formatNumber value="${i.price}" pattern="#,##0"/></small>
            </div>
            <strong style="font-variant-numeric: tabular-nums;">Rs. <fmt:formatNumber value="${i.lineTotal}" pattern="#,##0"/></strong>
          </div>
        </c:forEach>
      </div>

      <div class="summary-line" style="border-top: 1px solid var(--border-subtle); padding-top: 12px;">
        <span>Subtotal</span>
        <span id="sub" data-v="${sessionScope.cart.subtotal}" data-d="${sessionScope.cart.discount}">
          Rs. <fmt:formatNumber value="${sessionScope.cart.subtotal}" pattern="#,##0.00"/>
        </span>
      </div>

      <c:if test="${sessionScope.cart.discount > 0}">
        <div class="summary-line promo-discount-line">
          <span>Promo (<c:out value="${sessionScope.cart.promoCode}"/>)</span>
          <span>&minus; Rs. <fmt:formatNumber value="${sessionScope.cart.discount}" pattern="#,##0.00"/></span>
        </div>
      </c:if>

      <div class="summary-line">
        <span>Delivery Fee</span>
        <span id="fee" style="font-weight: 700;">Calculating...</span>
      </div>

      <div class="summary-line total-line">
        <span>Grand Total</span>
        <span id="tot" style="color: var(--brand);">Rs. 0.00</span>
      </div>

      <button class="btn btn-primary block" type="submit" style="padding: 13px; font-size: 1rem; margin-top: 6px;">
        Place Order Now
      </button>

      <a class="btn btn-ghost block" href="${ctx}/cart" style="font-size: 0.9rem;">
        &larr; Back to Cart
      </a>
    </aside>
  </form>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
