<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Orders"/>
<c:set var="extraCss" value="orders.css"/>
<c:set var="extraJs" value="orders.js"/>
<%@ include file="top.jspf" %>

<main class="container">
  <!-- Header Bar -->
  <div class="panel-title-wrap">
    <div>
      <h1 class="page-title" style="margin-bottom: 4px;">
        ${sessionScope.user.admin ? 'All Customer Orders' : 'My Orders'}
      </h1>
      <p style="color: var(--text-muted); font-size: 0.95rem;">
        ${sessionScope.user.admin ? 'Monitor kitchen queue, order fulfillment, and delivery dispatches.' : 'Track live status, delivery rider assignments, and view receipts.'}
      </p>
    </div>

    <!-- Status Filters for Admin -->
    <c:if test="${sessionScope.user.admin}">
      <nav class="filter-pills-bar" aria-label="Order status filters">
        <a class="filter-pill ${empty param.status ? 'active' : ''}" href="${ctx}/orders">All Orders</a>
        <c:forEach var="s" items="${statuses}">
          <c:url var="sUrl" value="/orders"><c:param name="status" value="${s}"/></c:url>
          <a class="filter-pill ${param.status == s ? 'active' : ''}" href="${sUrl}"><c:out value="${s}"/></a>
        </c:forEach>
      </nav>
    </c:if>
  </div>

  <!-- Admin FIFO Kitchen Queue Panel (Member 4) -->
  <c:if test="${sessionScope.user.admin}">
    <section class="kitchen-queue-card">
      <div class="kitchen-queue-info">
        <h3>🍳 Kitchen Preparation Queue <small style="font-size: 0.85rem; color: var(--text-muted);">(First In, First Out)</small></h3>
        <c:choose>
          <c:when test="${queueSize > 0 and not empty queueNext}">
            <p><strong>${queueSize}</strong> orders waiting &bull; Next in queue: <strong>Order #${queueNext.id}</strong> by <c:out value="${queueNext.username}"/> (placed <c:out value="${queueNext.date}"/>)</p>
          </c:when>
          <c:when test="${queueSize > 0}">
            <p><strong>${queueSize}</strong> orders waiting in kitchen queue.</p>
          </c:when>
          <c:otherwise>
            <p>Kitchen queue is clear &mdash; no orders currently pending preparation.</p>
          </c:otherwise>
        </c:choose>
      </div>
      <form method="post" action="${ctx}/orders" style="margin: 0;">
        <input type="hidden" name="action" value="process">
        <button class="btn btn-primary" type="submit" ${queueSize > 0 ? '' : 'disabled'}>
          <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 18 15 12 9 6"/></svg>
          Process Next Order
        </button>
      </form>
    </section>
  </c:if>

  <!-- Orders List -->
  <c:forEach var="o" items="${orders}">
    <section class="order-card-panel" id="order-${o.id}">
      <div class="order-header-bar">
        <div class="order-title-group">
          <h2>
            Order #${o.id}
            <span class="status ${o.statusClass}"><c:out value="${o.status}"/></span>
          </h2>
          <span class="order-meta-info">
            Placed on <c:out value="${o.date}"/> &bull; <strong>${o.label}</strong> (Est. ${o.eta})
            <c:if test="${sessionScope.user.admin}"> &bull; Customer: <strong><c:out value="${o.username}"/></strong></c:if>
          </span>
        </div>
        <div style="text-align: right;">
          <span style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display); color: var(--text-main);">
            Rs. <fmt:formatNumber value="${o.total}" pattern="#,##0.00"/>
          </span>
        </div>
      </div>

      <!-- MEMBER 3: Order Tracking Timeline -->
      <%@ include file="order-tracking.jspf" %>

      <!-- Items List -->
      <table class="order-items-table">
        <tbody>
          <c:forEach var="i" items="${o.items}">
            <tr>
              <td>
                <span class="item-qty-badge">${i.qty}&times;</span>
                <strong><c:out value="${i.name}"/></strong>
              </td>
              <td class="num" style="color: var(--text-muted); font-size: 0.88rem;">
                @ Rs. <fmt:formatNumber value="${i.price}" pattern="#,##0.00"/>
              </td>
              <td class="num" style="font-weight: 700; width: 140px;">
                Rs. <fmt:formatNumber value="${i.lineTotal}" pattern="#,##0.00"/>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <!-- Financials Breakdown -->
      <div style="background: var(--bg-subtle); padding: 14px 18px; border-radius: var(--radius-sm); margin: 14px 0;">
        <c:if test="${o.discount > 0}">
          <div class="order-finance-row" style="color: var(--accent-green); font-weight: 700;">
            <span>Promo Code Discount (<c:out value="${o.promoCode}"/>)</span>
            <span>&minus; Rs. <fmt:formatNumber value="${o.discount}" pattern="#,##0.00"/></span>
          </div>
        </c:if>
        <div class="order-finance-row">
          <span>Delivery Fee (${o.label})</span>
          <span>Rs. <fmt:formatNumber value="${o.deliveryFee}" pattern="#,##0.00"/></span>
        </div>
        <div class="order-finance-row grand-total">
          <span>Grand Total</span>
          <span style="color: var(--brand);">Rs. <fmt:formatNumber value="${o.total}" pattern="#,##0.00"/></span>
        </div>
      </div>

      <!-- Delivery Address Info -->
      <div class="order-info-line">
        <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg>
        <span>Deliver to: <strong><c:out value="${o.address}"/></strong> &bull; Contact: <strong><c:out value="${o.phone}"/></strong></span>
      </div>

      <!-- MEMBER 5: Delivery Rider Info Fragment -->
      <%@ include file="order-delivery.jspf" %>

      <!-- MEMBER 6: Payment Status & Bill Link Fragment -->
      <%@ include file="order-payment.jspf" %>

      <!-- Order Actions Toolbar -->
      <div class="order-actions-bar">
        <c:if test="${o.cancellable}">
          <form method="post" action="${ctx}/orders" data-confirm="Are you sure you want to cancel Order #${o.id}?">
            <input type="hidden" name="action" value="cancel">
            <input type="hidden" name="id" value="${o.id}">
            <button class="btn btn-sm danger" type="submit">Cancel Order</button>
          </form>
        </c:if>

        <c:if test="${sessionScope.user.admin}">
          <form method="post" action="${ctx}/orders" class="status-change-form">
            <input type="hidden" name="action" value="status">
            <input type="hidden" name="id" value="${o.id}">
            <select name="status" aria-label="Update order status">
              <c:forEach var="s" items="${statuses}">
                <option value="${s}" ${o.status == s ? 'selected' : ''}><c:out value="${s}"/></option>
              </c:forEach>
            </select>
            <button class="btn btn-sm" type="submit">Update Status</button>
          </form>

          <form method="post" action="${ctx}/orders" data-confirm="Are you sure you want to delete order record #${o.id}?">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" value="${o.id}">
            <button class="btn btn-sm danger" type="submit">Delete Order</button>
          </form>
        </c:if>
      </div>

      <!-- MEMBER 6: Rating Widget Fragment -->
      <%@ include file="order-rating.jspf" %>
    </section>
  </c:forEach>

  <c:if test="${empty orders}">
    <div class="empty-menu-box">
      <div class="empty-menu-icon">📦</div>
      <h3 class="empty-menu-title">No orders found</h3>
      <p class="empty-menu-desc">You don't have any past or active orders yet. Browse our tasty menu and place your first order!</p>
      <a class="btn btn-primary" href="${ctx}/foods">Explore Food Menu</a>
    </div>
  </c:if>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
