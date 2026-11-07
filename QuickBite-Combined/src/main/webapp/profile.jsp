<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Profile"/>
<c:set var="extraCss" value="profile.css"/>
<%@ include file="top.jspf" %>

<main class="container">
  <!-- Profile Header Hero Card (Part C) -->
  <section class="profile-hero-card">
    <div class="profile-avatar-large" style="background-color: ${profileUser.avatarColor};">
      <c:out value="${profileUser.avatarInitial}"/>
    </div>
    <div class="profile-hero-info">
      <div class="profile-hero-name">
        <c:out value="${profileUser.username}"/>
        <c:if test="${profileUser.admin}">
          <span class="admin-tag" style="font-size: 0.8rem; padding: 3px 10px;">Admin Account</span>
        </c:if>
      </div>
      <div class="profile-hero-role">
        <span class="status ${profileUser.admin ? 'status-delivered' : 'status-preparing'}">
          <c:out value="${profileUser.role}"/>
        </span>
        &bull; Registered Member &bull; <c:out value="${profileUser.email}"/>
      </div>
    </div>
  </section>

  <!-- Edit Details Form -->
  <form class="panel form-card" method="post" action="${ctx}/users" style="margin-bottom: 32px;">
    <h2 style="font-size: 1.6rem; margin-bottom: 6px;">Personal Information</h2>
    <p style="color: var(--text-muted); font-size: 0.92rem; margin-bottom: 20px;">Keep your delivery address and contact information up to date.</p>

    <c:if test="${not empty error}">
      <div class="flash error"><c:out value="${error}"/></div>
    </c:if>

    <input type="hidden" name="action" value="update">

    <div class="form-grid">
      <label>
        Email Address <span style="color: var(--brand);">*</span>
        <input required type="email" name="email" value="<c:out value='${profileUser.email}'/>">
      </label>
      <label>
        Phone Number <span style="color: var(--brand);">*</span>
        <input required type="tel" name="phone" value="<c:out value='${profileUser.phone}'/>">
      </label>
      <label class="full">
        Default Delivery Address
        <input name="address" value="<c:out value='${profileUser.address}'/>" placeholder="No. 12, Beach Road, Negombo">
      </label>
      <label class="full">
        New Password <small style="color: var(--text-muted); font-weight: normal;">(Leave empty to keep your current password)</small>
        <input type="password" name="password" minlength="6" placeholder="Enter new password (optional)">
      </label>
    </div>

    <div class="form-actions">
      <button class="btn btn-primary" type="submit">Save Changes</button>
      <a class="btn btn-ghost" href="${ctx}/foods">Back to Menu</a>
    </div>
  </form>

  <!-- Danger Zone Panel -->
  <form class="panel form-card danger-zone-panel" method="post" action="${ctx}/users" data-confirm="Are you sure you want to permanently delete your QuickBite account? This action cannot be undone.">
    <h3 style="color: #dc2626; font-size: 1.3rem; margin-bottom: 6px;">Danger Zone</h3>
    <p style="color: var(--text-secondary); font-size: 0.92rem; margin-bottom: 18px;">
      Permanently deactivate and delete your account. Your past completed orders will remain in system transaction records.
    </p>
    <input type="hidden" name="action" value="delete">
    <button class="btn btn-danger btn-sm" type="submit">Delete My Account</button>
  </form>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
