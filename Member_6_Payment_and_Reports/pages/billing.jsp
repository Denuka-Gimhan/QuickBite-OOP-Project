<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Billing &amp; Payments"/>
<c:set var="extraCss" value="billing.css"/>
<c:set var="extraJs" value="billing.js"/>
<%@ include file="top.jspf" %>

<main class="container">
  <div class="panel-title-wrap">
    <div>
      <h1 class="page-title" style="margin-bottom: 4px;">Billing &amp; Revenue Transactions</h1>
      <p style="color: var(--text-muted); font-size: 0.95rem;">Track customer order payments, cash-on-delivery settlements, and processed refunds.</p>
    </div>

    <!-- Filter Pills -->
    <nav class="filter-pills-bar" aria-label="Payment status filters">
      <a class="filter-pill ${empty param.status ? 'active' : ''}" href="${ctx}/billing">All Payments</a>
      <c:forEach var="s" items="${statuses}">
        <c:url var="sUrl" value="/billing"><c:param name="status" value="${s}"/></c:url>
        <a class="filter-pill ${param.status == s ? 'active' : ''}" href="${sUrl}"><c:out value="${s}"/></a>
      </c:forEach>
    </nav>
  </div>

  <!-- KPI Cards -->
  <div class="kpis">
    <div class="kpi">
      <small>Total Paid Revenue</small>
      <b style="color: var(--accent-green);">Rs. <fmt:formatNumber value="${totalPaid}" pattern="#,##0.00"/></b>
    </div>
    <div class="kpi">
      <small>Pending Settlement</small>
      <b style="color: var(--accent-orange);">Rs. <fmt:formatNumber value="${totalPending}" pattern="#,##0.00"/></b>
    </div>
    <div class="kpi">
      <small>Total Refunded</small>
      <b style="color: var(--text-muted);">Rs. <fmt:formatNumber value="${totalRefunded}" pattern="#,##0.00"/></b>
    </div>
  </div>

  <!-- Transactions Table -->
  <div class="table-wrap">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 90px;">Trans ID</th>
          <th style="width: 90px;">Order</th>
          <th>Customer</th>
          <th>Payment Method</th>
          <th>Total Amount</th>
          <th>Payment Status</th>
          <th>Date</th>
          <th style="text-align: right;">Action</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="p" items="${payments}">
          <tr>
            <td><strong>#${p.id}</strong></td>
            <td><a href="${ctx}/orders#order-${p.orderId}" style="font-weight: 700;">#${p.orderId}</a></td>
            <td><strong><c:out value="${orderMap[p.orderId].username}"/></strong></td>
            <td>
              <span class="payment-method-tag">
                ${p.method.contains('Card') ? '💳' : '💵'} <c:out value="${p.method}"/>
              </span>
            </td>
            <td><strong style="font-family: var(--font-display); font-size: 1.05rem;">Rs. <fmt:formatNumber value="${p.amount}" pattern="#,##0.00"/></strong></td>
            <td><span class="status ${p.statusClass}"><c:out value="${p.status}"/></span></td>
            <td><small style="color: var(--text-muted);"><c:out value="${p.date}"/></small></td>
            <td class="row-actions">
              <a class="btn-sm" href="${ctx}/billing?orderId=${p.orderId}" title="View printable invoice">📄 Invoice</a>
              <c:if test="${p.status == 'Pending'}">
                <form method="post" action="${ctx}/billing">
                  <input type="hidden" name="action" value="markPaid">
                  <input type="hidden" name="id" value="${p.id}">
                  <button class="btn btn-primary btn-sm" type="submit">Mark Paid</button>
                </form>
              </c:if>
              <c:if test="${p.status == 'Paid'}">
                <form method="post" action="${ctx}/billing" data-confirm="Are you sure you want to refund payment #${p.id} for order #${p.orderId}?">
                  <input type="hidden" name="action" value="refund">
                  <input type="hidden" name="id" value="${p.id}">
                  <button class="btn-sm danger" type="submit">Refund</button>
                </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>

  <c:if test="${empty payments}">
    <p class="empty">No payment transactions found matching the filter.</p>
  </c:if>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
