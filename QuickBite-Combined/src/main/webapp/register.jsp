<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Sign up"/>
<c:set var="extraCss" value="register.css"/>
<c:set var="extraJs" value="auth.js"/>
<%@ include file="top.jspf" %>

<main class="auth-page-container">
  <div class="auth-split-layout">
    <!-- Visual Side -->
    <div class="auth-visual-side">
      <span class="auth-visual-badge">Join the Foodies</span>
      <div class="auth-visual-body">
        <h2>Taste happiness delivered right to you.</h2>
        <p>Create an account to save your favourite delivery addresses, speed up checkout, and get notified about flash discounts and promos.</p>
        <div class="auth-testimonial">
          <p>&ldquo;Signing up was quick. I get 15% off using promo codes on weekend family orders!&rdquo;</p>
          <span>&mdash; Fathima R., Colombo</span>
        </div>
      </div>
      <div style="font-size: 0.85rem; opacity: 0.85;">
        <span>QuickBite Food Delivery &bull; Instant Free Registration</span>
      </div>
    </div>

    <!-- Form Side -->
    <div class="auth-form-side">
      <div class="auth-header">
        <h1>Create Your Account</h1>
        <p>Fill in your details below to start ordering in minutes.</p>
      </div>

      <c:if test="${not empty error}">
        <div class="flash error"><c:out value="${error}"/></div>
      </c:if>

      <form method="post" action="${ctx}/users" class="auth-form-grid">
        <input type="hidden" name="action" value="register">

        <div class="auth-form-row">
          <div class="auth-field">
            <label for="regUsername">Username <span style="color: var(--brand);">*</span></label>
            <input id="regUsername" required name="username" maxlength="20" value="<c:out value='${form.username}'/>" placeholder="e.g. nimal_p">
          </div>
          <div class="auth-field">
            <label for="regEmail">Email Address <span style="color: var(--brand);">*</span></label>
            <input id="regEmail" required type="email" name="email" value="<c:out value='${form.email}'/>" placeholder="nimal@email.com">
          </div>
        </div>

        <div class="auth-form-row">
          <div class="auth-field">
            <label for="regPhone">Phone Number <span style="color: var(--brand);">*</span></label>
            <input id="regPhone" required type="tel" name="phone" value="<c:out value='${form.phone}'/>" placeholder="0771234567">
          </div>
          <div class="auth-field">
            <label for="regAddress">Delivery Address</label>
            <input id="regAddress" name="address" value="<c:out value='${form.address}'/>" placeholder="No. 12, Beach Road, Negombo">
          </div>
        </div>

        <div class="auth-form-row">
          <div class="auth-field">
            <label for="regPassword">Password <span style="color: var(--brand);">*</span></label>
            <div class="pwd-input-wrap">
              <input id="regPassword" required type="password" name="password" minlength="6" placeholder="At least 6 characters">
              <button class="pwd-toggle-btn" type="button" aria-label="Show password">
                <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
              </button>
            </div>
          </div>
          <div class="auth-field">
            <label for="regConfirm">Confirm Password <span style="color: var(--brand);">*</span></label>
            <div class="pwd-input-wrap">
              <input id="regConfirm" required type="password" name="confirm" minlength="6" placeholder="Repeat password">
              <button class="pwd-toggle-btn" type="button" aria-label="Show password">
                <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
              </button>
            </div>
          </div>
        </div>

        <button class="btn btn-primary block" type="submit" style="margin-top: 10px; padding: 13px;">
          Create My Account
        </button>

        <p class="auth-switch-prompt">
          Already have an account? <a href="${ctx}/users">Sign In here</a>
        </p>
      </form>
    </div>
  </div>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
