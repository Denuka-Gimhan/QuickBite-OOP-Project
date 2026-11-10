<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="User Accounts"/>
<c:set var="extraCss" value="users.css"/>
<%@ include file="top.jspf" %>

<main class="container">
  <div class="panel-title-wrap">
    <div>
      <h1 class="page-title" style="margin-bottom: 4px;">User Accounts</h1>
      <p style="color: var(--text-muted); font-size: 0.95rem;">Manage registered customers and staff administrators.</p>
    </div>

    <!-- Search Form -->
    <form method="get" action="${ctx}/users" class="nav-search" style="max-width: 320px;">
      <input type="hidden" name="action" value="list">
      <span class="nav-search-icon">
        <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="7"/><path d="m20 20-3.5-3.5"/>
        </svg>
      </span>
      <input class="nav-search-input" type="search" name="q" value="<c:out value='${param.q}'/>" placeholder="Search user or email...">
    </form>
  </div>

  <div class="table-wrap">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 80px;">ID</th>
          <th>User</th>
          <th>Role</th>
          <th>Email</th>
          <th>Phone</th>
          <th style="text-align: right;">Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="u" items="${users}">
          <tr>
            <td>#${u.id}</td>
            <td>
              <div class="user-cell">
                <span class="user-mini-avatar" style="background-color: ${u.avatarColor};">
                  <c:out value="${u.avatarInitial}"/>
                </span>
                <strong><c:out value="${u.username}"/></strong>
              </div>
            </td>
            <td>
              <span class="status ${u.admin ? 'status-delivered' : 'status-pending'}">
                <c:out value="${u.role}"/>
              </span>
            </td>
            <td><c:out value="${u.email}"/></td>
            <td><c:out value="${u.phone}"/></td>
            <td style="text-align: right;">
              <c:if test="${u.id != sessionScope.user.id}">
                <form method="post" action="${ctx}/users" data-confirm="Are you sure you want to delete user account '${u.username}'?">
                  <input type="hidden" name="action" value="delete">
                  <input type="hidden" name="id" value="${u.id}">
                  <button class="btn-sm danger" type="submit">Delete</button>
                </form>
              </c:if>
              <c:if test="${u.id == sessionScope.user.id}">
                <span style="color: var(--text-muted); font-size: 0.82rem; font-weight: 600;">(You)</span>
              </c:if>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>

  <c:if test="${empty users}">
    <p class="empty">No user accounts found matching your query.</p>
  </c:if>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
