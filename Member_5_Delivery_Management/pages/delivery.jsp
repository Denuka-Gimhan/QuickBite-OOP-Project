<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Delivery Management"/>
<c:set var="extraCss" value="delivery.css"/>
<c:set var="extraJs" value="delivery.js"/>
<%@ include file="top.jspf" %>

<main class="container">
  <div style="margin-bottom: 24px;">
    <h1 class="page-title" style="margin-bottom: 4px;">Delivery Dispatch &amp; Riders</h1>
    <p style="color: var(--text-muted); font-size: 0.95rem;">Assign delivery personnel to prepared kitchen orders and monitor live delivery tracking.</p>
  </div>

  <div class="delivery-section-grid">
    <!-- Panel 1: Orders Waiting for Rider -->
    <section class="panel">
      <div class="panel-title-wrap">
        <div>
          <h2 style="font-size: 1.35rem; margin-bottom: 4px;">1. Orders Waiting for a Rider</h2>
          <p style="color: var(--text-muted); font-size: 0.88rem;">Orders appear here once the kitchen marks them as Preparing.</p>
        </div>
        <span class="status ${empty ready ? 'status-delivered' : 'status-pending'}">
          ${empty ready ? '0 orders waiting' : ready.size().concat(' waiting')}
        </span>
      </div>

      <c:choose>
        <c:when test="${empty ready}">
          <p class="empty" style="padding: 28px 16px;">All kitchen orders currently have riders assigned.</p>
        </c:when>
        <c:otherwise>
          <div class="table-wrap">
            <table class="table">
              <thead>
                <tr>
                  <th style="width: 100px;">Order ID</th>
                  <th>Customer</th>
                  <th>Delivery Destination</th>
                  <th>Speed</th>
                  <th style="text-align: right;">Assign Rider</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="o" items="${ready}">
                  <tr>
                    <td><strong>#${o.id}</strong></td>
                    <td>
                      <strong><c:out value="${o.username}"/></strong><br>
                      <small style="color: var(--text-muted);"><c:out value="${o.phone}"/></small>
                    </td>
                    <td><c:out value="${o.address}"/></td>
                    <td><span class="status status-preparing">${o.label}</span></td>
                    <td style="text-align: right;">
                      <c:choose>
                        <c:when test="${empty freeRiders}">
                          <span style="color: #dc2626; font-size: 0.85rem; font-weight: 700;">No riders available</span>
                        </c:when>
                        <c:otherwise>
                          <form method="post" action="${ctx}/delivery" class="status-change-form" style="justify-content: flex-end;">
                            <input type="hidden" name="action" value="assign">
                            <input type="hidden" name="orderId" value="${o.id}">
                            <select name="personId" aria-label="Select rider">
                              <c:forEach var="r" items="${freeRiders}">
                                <option value="${r.id}"><c:out value="${r.name}"/> (<c:out value="${r.vehicle}"/>)</option>
                              </c:forEach>
                            </select>
                            <button class="btn btn-primary btn-sm" type="submit">Assign</button>
                          </form>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </c:otherwise>
      </c:choose>
    </section>

    <!-- Panel 2: Deliveries In Progress -->
    <section class="panel">
      <div class="panel-title-wrap">
        <div>
          <h2 style="font-size: 1.35rem; margin-bottom: 4px;">2. Active Deliveries in Progress</h2>
          <p style="color: var(--text-muted); font-size: 0.88rem;">Live delivery tracking from pickup to doorstep handover.</p>
        </div>
      </div>

      <c:choose>
        <c:when test="${empty active}">
          <p class="empty" style="padding: 28px 16px;">No orders are currently out on the road.</p>
        </c:when>
        <c:otherwise>
          <div class="table-wrap">
            <table class="table">
              <thead>
                <tr>
                  <th style="width: 100px;">Order ID</th>
                  <th>Assigned Rider</th>
                  <th>Delivery Address &amp; Total</th>
                  <th>Progress</th>
                  <th style="text-align: right;">Update Dispatch</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="d" items="${active}">
                  <c:set var="o" value="${orderMap[d.orderId]}"/>
                  <tr>
                    <td><strong>#${d.orderId}</strong></td>
                    <td>
                      <div class="rider-badge-cell">
                        <span class="rider-icon-box">🚴</span>
                        <div>
                          <strong><c:out value="${d.personName}"/></strong><br>
                          <small style="color: var(--text-muted);"><c:out value="${d.personPhone}"/></small>
                        </div>
                      </div>
                    </td>
                    <td>
                      <c:out value="${o.address}"/><br>
                      <small style="color: var(--text-muted); font-weight: 600;">
                        <c:out value="${o.phone}"/> &bull; Rs. <fmt:formatNumber value="${o.total}" pattern="#,##0.00"/> (<c:out value="${o.payment}"/>)
                      </small>
                    </td>
                    <td>
                      <span class="status ${d.status == 'Picked up' ? 'status-on-the-way' : 'status-preparing'}">
                        <c:out value="${d.status}"/>
                      </span>
                    </td>
                    <td class="row-actions">
                      <c:if test="${d.status == 'Assigned'}">
                        <form method="post" action="${ctx}/delivery">
                          <input type="hidden" name="action" value="pickup">
                          <input type="hidden" name="id" value="${d.id}">
                          <button class="btn-sm" type="submit">📦 Picked Up</button>
                        </form>
                        <form method="post" action="${ctx}/delivery" data-confirm="Unassign rider from order #${d.orderId}?">
                          <input type="hidden" name="action" value="unassign">
                          <input type="hidden" name="id" value="${d.id}">
                          <button class="btn-sm danger" type="submit">Unassign</button>
                        </form>
                      </c:if>
                      <c:if test="${d.status == 'Picked up'}">
                        <form method="post" action="${ctx}/delivery">
                          <input type="hidden" name="action" value="deliver">
                          <input type="hidden" name="id" value="${d.id}">
                          <button class="btn btn-primary btn-sm" type="submit">✅ Mark Delivered</button>
                        </form>
                      </c:if>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </c:otherwise>
      </c:choose>
    </section>

    <!-- Panel 3: Riders Roster -->
    <section class="panel">
      <div class="panel-title-wrap">
        <div>
          <h2 style="font-size: 1.35rem; margin-bottom: 4px;">3. Fleet Riders Roster</h2>
          <p style="color: var(--text-muted); font-size: 0.88rem;">Manage delivery riders, vehicle types, and work availability status.</p>
        </div>
      </div>

      <div class="table-wrap">
        <table class="table">
          <thead>
            <tr>
              <th style="width: 80px;">Rider ID</th>
              <th>Name</th>
              <th>Phone</th>
              <th>Vehicle Type</th>
              <th>Availability</th>
              <th style="text-align: right;">Roster Actions</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="r" items="${riders}">
              <c:set var="isBusy" value="${busyRiders.contains(r.id)}"/>
              <tr>
                <td>#${r.id}</td>
                <td><strong><c:out value="${r.name}"/></strong></td>
                <td><c:out value="${r.phone}"/></td>
                <td><span style="font-size: 0.88rem;"><c:out value="${r.vehicle}"/></span></td>
                <td>
                  <span class="status ${isBusy ? 'status-on-the-way' : (r.available ? 'status-delivered' : 'status-cancelled')}">
                    ${isBusy ? 'On Delivery' : (r.available ? 'Available' : 'Off Duty')}
                  </span>
                </td>
                <td class="row-actions">
                  <c:if test="${not isBusy}">
                    <form method="post" action="${ctx}/delivery">
                      <input type="hidden" name="action" value="toggleRider">
                      <input type="hidden" name="id" value="${r.id}">
                      <button class="btn-sm" type="submit">
                        ${r.available ? 'Set Off Duty' : 'Set Available'}
                      </button>
                    </form>
                    <form method="post" action="${ctx}/delivery" data-confirm="Delete rider '${r.name}' from fleet?">
                      <input type="hidden" name="action" value="deleteRider">
                      <input type="hidden" name="id" value="${r.id}">
                      <button class="btn-sm danger" type="submit">Delete</button>
                    </form>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>

      <!-- Add Rider Card -->
      <form method="post" action="${ctx}/delivery" class="add-rider-card" style="margin-top: 20px;">
        <h3 style="font-size: 1.15rem; margin-bottom: 12px;">+ Add New Fleet Rider</h3>
        <input type="hidden" name="action" value="addRider">
        <div class="form-grid" style="margin-top: 0; margin-bottom: 14px;">
          <label>
            Rider Full Name <span style="color: var(--brand);">*</span>
            <input required name="name" maxlength="40" placeholder="e.g. Ruwan Perera">
          </label>
          <label>
            Phone Number <span style="color: var(--brand);">*</span>
            <input required name="phone" type="tel" maxlength="15" placeholder="0779876543">
          </label>
          <label class="full">
            Vehicle Type <span style="color: var(--brand);">*</span>
            <select name="vehicle">
              <c:forEach var="v" items="${vehicles}">
                <option value="${v}"><c:out value="${v}"/></option>
              </c:forEach>
            </select>
          </label>
        </div>
        <button class="btn btn-primary" type="submit">+ Register Rider</button>
      </form>
    </section>

    <!-- Recent Deliveries History -->
    <c:if test="${not empty history}">
      <section class="panel">
        <h2 style="font-size: 1.35rem; margin-bottom: 16px;">Completed Deliveries History</h2>
        <div class="table-wrap">
          <table class="table">
            <thead>
              <tr>
                <th style="width: 100px;">Order ID</th>
                <th>Rider</th>
                <th>Dispatched At</th>
                <th>Delivered At</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="d" items="${history}">
                <tr>
                  <td><strong>#${d.orderId}</strong></td>
                  <td><c:out value="${d.personName}"/></td>
                  <td><c:out value="${d.assignedAt}"/></td>
                  <td><span class="status status-delivered"><c:out value="${d.deliveredAt}"/></span></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </section>
    </c:if>
  </div>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
