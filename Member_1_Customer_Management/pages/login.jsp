<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Log in"/>
<c:set var="extraCss" value="login.css"/>
<c:set var="extraJs" value="auth.js"/>
<%@ include file="top.jspf" %>

<main class="auth-page-container">
  <div class="auth-split-layout">
    <!-- Visual Side -->
    <div class="auth-visual-side">
      <span class="auth-visual-badge">Welcome Back</span>
      <div class="auth-visual-body">
        <h2>Your next delicious meal is waiting.</h2>
        <p>Log in to reorder your favourites, check live kitchen updates, and enjoy exclusive member discount promo codes.</p>
        <div class="auth-testimonial">
          <p>&ldquo;QuickBite has made lunchtime at work so fast and effortless. The Kottu arrives steaming hot!&rdquo;</p>
          <span>&mdash; Chaminda P., Regular Customer</span>
        </div>
      </div>
      <div style="font-size: 0.85rem; opacity: 0.85;">
        <span>QuickBite Food Delivery &bull; Safe &amp; Secure Access</span>
      </div>
    </div>

    <!-- Form Side -->
    <div class="auth-form-side">
      <div class="auth-header">
        <h1>Sign In to QuickBite</h1>
        <p>Enter your credentials to access your account.</p>
      </div>

      <c:if test="${not empty error}">
        <div class="flash error"><c:out value="${error}"/></div>
      </c:if>

      <form method="post" action="${ctx}/users" class="auth-form-grid">
        <input type="hidden" name="action" value="login">

        <div class="auth-field">
          <label for="loginUsername">Username or Email</label>
          <input id="loginUsername" required name="username" autofocus placeholder="e.g. admin or your email">
        </div>

        <div class="auth-field">
          <label for="loginPassword">Password</label>
          <div class="pwd-input-wrap">
            <input id="loginPassword" required type="password" name="password" placeholder="Enter your password">
            <button class="pwd-toggle-btn" type="button" aria-label="Show password">
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
            </button>
          </div>
        </div>

        <button class="btn btn-primary block" type="submit" style="margin-top: 10px; padding: 13px;">
          Log In to Account
        </button>

        <div style="background: var(--bg-muted); border-radius: var(--radius-sm); padding: 12px; margin-top: 6px; font-size: 0.82rem; color: var(--text-muted); text-align: center;">
          Demo Admin: <strong>admin</strong> &bull; Password: <strong>admin123</strong>
        </div>

        <p class="auth-switch-prompt">
          Don't have an account yet? <a href="${ctx}/users?action=register">Create an account</a>
        </p>
      </form>
    </div>
  </div>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
