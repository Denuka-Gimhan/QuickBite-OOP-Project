<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Invoice ${bill.invoiceNo}"/>
<c:set var="extraCss" value="bill.css"/>
<%@ include file="top.jspf" %>

<main class="container">
  <div class="invoice-container-card">
    <!-- Invoice Header -->
    <header class="invoice-brand-header">
      <div>
        <div class="invoice-brand-title">QuickBite</div>
        <small style="color: var(--text-muted); font-size: 0.9rem; font-weight: 600;">Online Food Delivery Network</small><br>
        <small style="color: var(--text-muted); font-size: 0.82rem;">Colombo &amp; Negombo &bull; Hotline: +94 11 234 5678</small>
      </div>
      <div class="invoice-meta-col">
        <div class="invoice-number-badge">${bill.invoiceNo}</div>
        <small style="color: var(--text-muted); font-size: 0.88rem;">Order Date: <strong><c:out value="${bill.order.date}"/></strong></small>
      </div>
    </header>

    <!-- Billed To & Payment Meta -->
    <div class="invoice-billing-grid">
      <div class="invoice-info-block">
        <small>Billed Customer</small>
        <strong><c:out value="${bill.order.username}"/></strong>
        <span style="display: block; font-size: 0.9rem; color: var(--text-secondary);"><c:out value="${bill.order.address}"/></span>
        <span style="display: block; font-size: 0.9rem; color: var(--text-secondary);">Tel: <c:out value="${bill.order.phone}"/></span>
      </div>
      <div class="invoice-info-block" style="text-align: right;">
        <small>Payment Details</small>
        <strong><c:out value="${bill.order.payment}"/></strong>
        <div style="margin-top: 6px;">
          <span class="status ${empty bill.payment ? 'pay-pending' : bill.payment.statusClass}">
            <c:out value="${bill.paymentStatus}"/>
          </span>
        </div>
      </div>
    </div>

    <!-- Items Table with Photos (Part D) -->
    <div class="table-wrap" style="box-shadow: none;">
      <table class="table">
        <thead>
          <tr>
            <th>Dishes Ordered</th>
            <th class="num" style="width: 80px;">Qty</th>
            <th class="num" style="width: 140px;">Unit Price</th>
            <th class="num" style="width: 150px;">Total Amount</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="i" items="${bill.order.items}">
            <tr>
              <td>
                <div class="invoice-item-cell">
                  <!-- Photo Thumbnail or Emoji (Part D) -->
                  <div class="invoice-item-thumb">
                    <c:choose>
                      <c:when test="${not empty i.imageFile}">
                        <img src="${ctx}/food-image?name=<c:out value='${i.imageFile}'/>" alt="<c:out value='${i.name}'/>">
                      </c:when>
                      <c:when test="${not empty i.emoji}">
                        <span style="font-size: 1.5rem;"><c:out value="${i.emoji}"/></span>
                      </c:when>
                      <c:otherwise>
                        <span style="font-size: 1.4rem;">🍽️</span>
                      </c:otherwise>
                    </c:choose>
                  </div>
                  <div>
                    <strong style="font-size: 0.98rem; display: block;"><c:out value="${i.name}"/></strong>
                  </div>
                </div>
              </td>
              <td class="num">${i.qty}</td>
              <td class="num">Rs. <fmt:formatNumber value="${i.price}" pattern="#,##0.00"/></td>
              <td class="num" style="font-weight: 700;">Rs. <fmt:formatNumber value="${i.lineTotal}" pattern="#,##0.00"/></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <!-- Financial Totals -->
    <div class="invoice-totals-box">
      <div class="summary-line">
        <span>Subtotal</span>
        <strong>Rs. <fmt:formatNumber value="${bill.order.subtotal}" pattern="#,##0.00"/></strong>
      </div>

      <c:if test="${bill.order.discount > 0}">
        <div class="summary-line" style="color: var(--accent-green); font-weight: 700;">
          <span>Promo Discount (<c:out value="${bill.order.promoCode}"/>)</span>
          <span>&minus; Rs. <fmt:formatNumber value="${bill.order.discount}" pattern="#,##0.00"/></span>
        </div>
      </c:if>

      <div class="summary-line">
        <span>Delivery Fee (${bill.order.label})</span>
        <strong>Rs. <fmt:formatNumber value="${bill.order.deliveryFee}" pattern="#,##0.00"/></strong>
      </div>

      <div class="summary-line" style="border-top: 1px solid var(--border-subtle); padding-top: 8px;">
        <span>Invoice Total</span>
        <strong>Rs. <fmt:formatNumber value="${bill.order.total}" pattern="#,##0.00"/></strong>
      </div>

      <div class="summary-line invoice-total-due">
        <span>Amount Due</span>
        <span>Rs. <fmt:formatNumber value="${bill.amountDue}" pattern="#,##0.00"/></span>
      </div>
    </div>

    <div style="margin-top: 36px; padding-top: 20px; border-top: 1px dashed var(--border-medium); text-align: center; color: var(--text-muted); font-size: 0.88rem;">
      Thank you for ordering with QuickBite! Please keep this invoice for your transaction records.
    </div>

    <!-- Action Buttons (Print / Back) -->
    <div class="form-actions no-print" style="justify-content: center; margin-top: 28px;">
      <button class="btn btn-primary" type="button" onclick="window.print()">
        <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 6 2 18 2 18 9"/><path d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2"/><rect x="6" y="14" width="12" height="8"/></svg>
        Print Official Invoice
      </button>
      <a class="btn btn-ghost" href="${ctx}/orders">
        &larr; Back to Orders
      </a>
    </div>
  </div>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
