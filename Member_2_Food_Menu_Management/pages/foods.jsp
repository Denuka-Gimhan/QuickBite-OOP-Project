<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Food Menu"/>
<c:set var="extraCss" value="foods.css"/>
<c:set var="extraJs" value="foods.js"/>
<%@ include file="top.jspf" %>

<main class="container" id="menu">
  <!-- Menu Header & Category Navigation -->
  <header class="menu-header-bar">
    <div class="menu-title-row">
      <div>
        <h1 class="page-title" style="margin-bottom: 4px;">Our Food Menu</h1>
        <p style="color: var(--text-muted); font-size: 0.95rem;">
          <c:choose>
            <c:when test="${not empty param.q}">
              Search results for "<strong><c:out value='${param.q}'/></strong>" &bull; <a href="${ctx}/foods" style="color: var(--brand);">Clear search</a>
            </c:when>
            <c:when test="${not empty param.cat}">
              Showing category: "<strong><c:out value='${param.cat}'/></strong>" &bull; <a href="${ctx}/foods" style="color: var(--brand);">Show all</a>
            </c:when>
            <c:otherwise>Delicious local delicacies, fast food, refreshing drinks, and desserts.</c:otherwise>
          </c:choose>
        </p>
      </div>

      <c:if test="${sessionScope.user.admin}">
        <a class="btn btn-primary" href="${ctx}/foods?action=new">
          <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M12 5v14M5 12h14"/></svg>
          Add New Food
        </a>
      </c:if>
    </div>

    <!-- Category Filter Pills -->
    <nav class="filter-pills-bar" aria-label="Food category filters">
      <c:url var="allUrl" value="/foods">
        <c:if test="${not empty param.sort}"><c:param name="sort" value="${param.sort}"/></c:if>
        <c:if test="${not empty param.q}"><c:param name="q" value="${param.q}"/></c:if>
      </c:url>
      <a class="filter-pill ${empty param.cat ? 'active' : ''}" href="${allUrl}">All Categories</a>
      
      <c:forEach var="cat" items="${categories}">
        <c:url var="catUrl" value="/foods">
          <c:param name="cat" value="${cat}"/>
          <c:if test="${not empty param.sort}"><c:param name="sort" value="${param.sort}"/></c:if>
          <c:if test="${not empty param.q}"><c:param name="q" value="${param.q}"/></c:if>
        </c:url>
        <a class="filter-pill ${param.cat == cat ? 'active' : ''}" href="${catUrl}"><c:out value="${cat}"/></a>
      </c:forEach>
    </nav>

    <!-- Sort & Summary Row -->
    <div class="menu-controls-row">
      <div class="sort-group">
        <span>Sort by price:</span>
        <c:url var="ascUrl" value="/foods">
          <c:param name="sort" value="price_asc"/>
          <c:if test="${not empty param.cat}"><c:param name="cat" value="${param.cat}"/></c:if>
          <c:if test="${not empty param.q}"><c:param name="q" value="${param.q}"/></c:if>
        </c:url>
        <c:url var="descUrl" value="/foods">
          <c:param name="sort" value="price_desc"/>
          <c:if test="${not empty param.cat}"><c:param name="cat" value="${param.cat}"/></c:if>
          <c:if test="${not empty param.q}"><c:param name="q" value="${param.q}"/></c:if>
        </c:url>
        <c:url var="noSortUrl" value="/foods">
          <c:if test="${not empty param.cat}"><c:param name="cat" value="${param.cat}"/></c:if>
          <c:if test="${not empty param.q}"><c:param name="q" value="${param.q}"/></c:if>
        </c:url>
        <a class="sort-chip ${param.sort == 'price_asc' ? 'active' : ''}" href="${ascUrl}">Price: Low &rarr; High</a>
        <a class="sort-chip ${param.sort == 'price_desc' ? 'active' : ''}" href="${descUrl}">Price: High &rarr; Low</a>
        <c:if test="${not empty param.sort}">
          <a class="sort-chip" href="${noSortUrl}">Clear sort</a>
        </c:if>
      </div>

      <div class="food-count-badge">
        <c:choose>
          <c:when test="${not empty foods}">${foods.size()} items available</c:when>
          <c:otherwise>0 items</c:otherwise>
        </c:choose>
      </div>
    </div>
  </header>

  <!-- Foods Grid -->
  <div class="food-grid">
    <c:forEach var="f" items="${foods}">
      <article class="food-card ${f.available ? '' : 'soldout'}">
        <!-- Media Wrap: Photo or Colored Tile with Emoji -->
        <div class="food-card-media" style="background-color: ${f.themeColor};">
          <span class="category-tag-badge"><c:out value="${f.category}"/></span>
          <c:choose>
            <c:when test="${not empty f.imageFile}">
              <img class="food-card-img" src="${ctx}/food-image?name=<c:out value='${f.imageFile}'/>" alt="<c:out value='${f.name}'/>" loading="lazy" data-fallback-emoji="<c:out value='${f.emoji}'/>">
            </c:when>
            <c:otherwise>
              <span class="food-card-emoji"><c:out value="${f.emoji}"/></span>
            </c:otherwise>
          </c:choose>
          <c:if test="${not f.available}">
            <span class="stock-ribbon">Out of Stock</span>
          </c:if>
        </div>

        <!-- Body Details -->
        <div class="food-card-body">
          <div class="food-card-meta">
            <span class="food-card-time">
              <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
              ${f.preparationTime} min prep
            </span>
            <small style="color: var(--text-muted);"><c:out value="${f.details}"/></small>
          </div>

          <h2 class="food-card-title"><c:out value="${f.name}"/></h2>
          <p class="food-card-desc"><c:out value="${f.description}"/></p>

          <!-- Footer: Price + Add Button -->
          <div class="food-card-footer">
            <span class="food-card-price">Rs. <fmt:formatNumber value="${f.price}" pattern="#,##0.00"/></span>

            <c:choose>
              <c:when test="${not f.available and not sessionScope.user.admin}">
                <button class="btn-sm" disabled style="opacity: 0.55; cursor: not-allowed;">Out of stock</button>
              </c:when>
              <c:when test="${not sessionScope.user.admin}">
                <form method="post" action="${ctx}/cart" style="margin: 0;">
                  <input type="hidden" name="action" value="add">
                  <input type="hidden" name="id" value="${f.id}">
                  <button class="btn btn-primary btn-sm" type="submit">
                    <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M12 5v14M5 12h14"/></svg>
                    Add to cart
                  </button>
                </form>
              </c:when>
            </c:choose>
          </div>

          <!-- Admin Action Bar: Dedicated, neat, guaranteed never to wrap or cut off -->
          <c:if test="${sessionScope.user.admin}">
            <div class="admin-action-bar">
              <a class="btn-sm" href="${ctx}/foods?action=edit&amp;id=${f.id}" title="Edit food details">
                ✏️ Edit
              </a>
              <form method="post" action="${ctx}/foods">
                <input type="hidden" name="action" value="stock">
                <input type="hidden" name="id" value="${f.id}">
                <button class="btn-sm" type="submit" title="Toggle Stock">
                  ${f.available ? '🚫 Unstock' : '✅ In stock'}
                </button>
              </form>
              <form method="post" action="${ctx}/foods" data-confirm="Are you sure you want to permanently delete '${f.name}'?">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${f.id}">
                <button class="btn-sm danger" type="submit" title="Delete Food">
                  🗑️ Delete
                </button>
              </form>
            </div>
          </c:if>
        </div>
      </article>
    </c:forEach>
  </div>

  <!-- Empty State -->
  <c:if test="${empty foods}">
    <div class="empty-menu-box">
      <div class="empty-menu-icon">🍽️</div>
      <h3 class="empty-menu-title">No matching dishes found</h3>
      <p class="empty-menu-desc">We couldn't find any dishes matching your current search or category filter. Try clearing filters or searching for something else.</p>
      <a class="btn btn-primary" href="${ctx}/foods">View All Foods</a>
    </div>
  </c:if>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
