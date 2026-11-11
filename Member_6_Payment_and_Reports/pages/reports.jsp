<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Reports &amp; Analytics"/>
<c:set var="extraCss" value="reports.css"/>
<c:set var="extraJs" value="reports.js"/>
<%@ include file="top.jspf" %>

<main class="container">
  <!-- Header Bar with CSV Download -->
  <div class="panel-title-wrap">
    <div>
      <h1 class="page-title" style="margin-bottom: 4px;">Business Performance &amp; Reports</h1>
      <p style="color: var(--text-muted); font-size: 0.95rem;">Key business metrics, sales revenue trends, best-selling dishes, and customer feedback ratings.</p>
    </div>
    <div>
      <a class="btn btn-primary" href="${ctx}/reports?format=csv">
        <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/></svg>
        Download Orders CSV
      </a>
    </div>
  </div>

  <!-- KPI Metric Summary Tiles -->
  <div class="kpis">
    <div class="kpi">
      <small>Total Orders Placed</small>
      <b>${totalOrders}</b>
    </div>
    <div class="kpi">
      <small>Total Settled Revenue</small>
      <b style="color: var(--accent-green);">Rs. <fmt:formatNumber value="${paid}" pattern="#,##0.00"/></b>
    </div>
    <div class="kpi">
      <small>Pending Payment</small>
      <b style="color: var(--accent-orange);">Rs. <fmt:formatNumber value="${pending}" pattern="#,##0.00"/></b>
    </div>
    <div class="kpi">
      <small>Promotional Discounts</small>
      <b style="color: var(--brand);">Rs. <fmt:formatNumber value="${discounts}" pattern="#,##0.00"/></b>
    </div>
    <div class="kpi">
      <small>Customer Satisfaction</small>
      <b style="color: #f59e0b;">⭐ <fmt:formatNumber value="${ratingAvg}" minFractionDigits="1" maxFractionDigits="1"/> / 5</b>
    </div>
  </div>

  <!-- Charts & Analytics Dashboard Grid -->
  <div class="reports-dashboard-grid">
    <!-- Chart 1: 7-Day Revenue Trend -->
    <section class="report-panel">
      <h2>
        <span>📊 Sales Revenue &ndash; Last 7 Days</span>
      </h2>
      <div class="chart-bar-list">
        <c:forEach var="e" items="${perDay}">
          <div class="chart-bar-item">
            <span class="chart-label"><c:out value="${e.key}"/></span>
            <div class="chart-track">
              <div class="chart-fill fill-green" style="width: ${dayMax > 0 ? (e.value * 100 / dayMax) : 0}%;"></div>
            </div>
            <span class="chart-val">Rs. <fmt:formatNumber value="${e.value}" pattern="#,##0"/></span>
          </div>
        </c:forEach>
      </div>
    </section>

    <!-- Chart 2: Best-Selling Dishes -->
    <section class="report-panel">
      <h2>
        <span>🔥 Top Best-Selling Dishes</span>
      </h2>
      <c:choose>
        <c:when test="${empty topItems}">
          <p class="empty" style="padding: 24px;">No dish sales recorded yet.</p>
        </c:when>
        <c:otherwise>
          <div class="chart-bar-list">
            <c:forEach var="e" items="${topItems}">
              <div class="chart-bar-item">
                <span class="chart-label" title="<c:out value='${e.key}'/>"><c:out value="${e.key}"/></span>
                <div class="chart-track">
                  <div class="chart-fill fill-orange" style="width: ${topMax > 0 ? (e.value * 100 / topMax) : 0}%;"></div>
                </div>
                <span class="chart-val">${e.value} sold</span>
              </div>
            </c:forEach>
          </div>
        </c:otherwise>
      </c:choose>
    </section>

    <!-- Breakdown 3: Order Status & Delivery Speed -->
    <section class="report-panel">
      <h2><span>📋 Order Fulfillment Breakdown</span></h2>
      <table class="table" style="box-shadow: none; margin-bottom: 20px;">
        <thead>
          <tr><th>Order Status</th><th class="num">Count</th></tr>
        </thead>
        <tbody>
          <c:forEach var="e" items="${byStatus}">
            <tr>
              <td><span class="status status-${e.key.toLowerCase().replace(' ', '-')}"><c:out value="${e.key}"/></span></td>
              <td class="num"><strong>${e.value}</strong></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <h3 style="font-size: 1.05rem; margin-bottom: 10px;">Delivery Speed Type (Active Orders)</h3>
      <table class="table" style="box-shadow: none;">
        <tbody>
          <c:forEach var="e" items="${byType}">
            <tr>
              <td><strong><c:out value="${e.key}"/></strong></td>
              <td class="num">${e.value} orders</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </section>

    <!-- Breakdown 4: Promo Code Usage -->
    <section class="report-panel">
      <h2><span>🏷️ Promo Code Utilization</span></h2>
      <c:choose>
        <c:when test="${empty promoUse}">
          <p class="empty" style="padding: 24px;">No promo codes applied yet.</p>
        </c:when>
        <c:otherwise>
          <table class="table" style="box-shadow: none;">
            <thead>
              <tr><th>Promo Code</th><th class="num">Orders Used</th></tr>
            </thead>
            <tbody>
              <c:forEach var="e" items="${promoUse}">
                <tr>
                  <td><span class="admin-tag" style="font-size: 0.8rem;"><c:out value="${e.key}"/></span></td>
                  <td class="num"><strong>${e.value}</strong> orders</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>
    </section>
  </div>

  <!-- Customer Ratings & Review Moderation Panel -->
  <section class="panel">
    <div class="panel-title-wrap">
      <div>
        <h2 style="font-size: 1.35rem; margin-bottom: 4px;">Customer Reviews &amp; Ratings Breakdown</h2>
        <p style="color: var(--text-muted); font-size: 0.88rem;">Overall rating: <strong><fmt:formatNumber value="${ratingAvg}" minFractionDigits="1" maxFractionDigits="1"/> / 5.0</strong> based on ${ratingCount} submitted ratings.</p>
      </div>
    </div>

    <!-- Rating Distribution Bars -->
    <div class="chart-bar-list" style="max-width: 600px; margin-bottom: 32px;">
      <c:forEach var="n" begin="1" end="5">
        <c:set var="k" value="${6 - n}"/>
        <div class="chart-bar-item">
          <span class="chart-label" style="color: #f59e0b; font-weight: 800;">${k} ★</span>
          <div class="chart-track">
            <div class="chart-fill fill-orange" style="width: ${ratingCount > 0 ? (starCounts[k] * 100 / ratingCount) : 0}%;"></div>
          </div>
          <span class="chart-val">${starCounts[k]}</span>
        </div>
      </c:forEach>
    </div>

    <!-- Reviews Moderation List -->
    <c:if test="${not empty latestRatings}">
      <h3 style="font-size: 1.15rem; margin-bottom: 12px; border-top: 1px solid var(--border-subtle); padding-top: 20px;">
        Latest Customer Reviews
      </h3>
      <c:forEach var="r" items="${latestRatings}">
        <div class="review-item-card">
          <div>
            <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 4px;">
              <span class="review-stars"><c:out value="${r.starText}"/></span>
              <strong><c:out value="${r.username}"/></strong>
              <small style="color: var(--text-muted);">&bull; Order #${r.orderId} &bull; <c:out value="${r.date}"/></small>
            </div>
            <c:if test="${not empty r.comment}">
              <p style="color: var(--text-secondary); font-size: 0.92rem; margin-top: 4px;">&ldquo;<c:out value="${r.comment}"/>&rdquo;</p>
            </c:if>
          </div>
          <form method="post" action="${ctx}/ratings" data-confirm="Delete this customer review?">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" value="${r.id}">
            <button class="btn-sm danger" type="submit">Delete</button>
          </form>
        </div>
      </c:forEach>
    </c:if>
  </section>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
