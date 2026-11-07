<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    if (request.getAttribute("popularFoods") == null) {
        try {
            request.setAttribute("popularFoods", com.quickbite.util.Web.foods(application).getAll());
        } catch (Exception ignore) {}
    }
%>
<c:set var="pageTitle" value="Home - Online Food Delivery"/>
<c:set var="extraCss" value="index.css"/>
<%@ include file="top.jspf" %>

<main class="container">
  <!-- Hero Section -->
  <section class="hero-home">
    <div class="hero-content">
      <span class="hero-badge">FRESH &bull; FAST &bull; DELICIOUS</span>
      <h1 class="hero-title">Crave it? Get it delivered <span>hot &amp; fast.</span></h1>
      <p class="hero-desc">From sizzling chicken kottu and spicy curries to gourmet burgers and cool thambili, your favourite meals delivered straight to your door.</p>
      <div class="hero-cta">
        <a class="btn btn-light" href="${ctx}/foods">
          Explore Food Menu
          <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
        </a>
        <a class="btn btn-outline" href="#popular" style="color: #ffffff; border-color: rgba(255,255,255,0.45);">
          Popular Dishes
        </a>
      </div>
      <div class="hero-trust">
        <span>⭐ 4.9/5 Rating (1,200+ reviews)</span>
        <span>🚴 25-40 min Delivery</span>
        <span>🍽️ 100% Fresh Daily</span>
      </div>
    </div>
    <div class="hero-art-box" aria-hidden="true">
      <div class="hero-main-emoji">🥘</div>
      <div class="hero-floating-card hero-float-1">
        <span class="floating-icon">🍔</span>
        <div class="floating-text">
          <strong>Gourmet Burgers</strong>
          <small>Freshly Grilled</small>
        </div>
      </div>
      <div class="hero-floating-card hero-float-2">
        <span class="floating-icon">⚡</span>
        <div class="floating-text">
          <strong>Super Fast</strong>
          <small>Express Delivery</small>
        </div>
      </div>
    </div>
  </section>

  <!-- Categories Section -->
  <section class="categories-section">
    <div class="section-header">
      <span class="section-tag">Taste the Diversity</span>
      <h2 class="section-title">Explore by Category</h2>
      <p class="section-subtitle">Discover handcrafted recipes and refreshments prepared with authentic ingredients.</p>
    </div>
    <div class="categories-grid">
      <a class="category-card" href="${ctx}/foods?cat=Traditional">
        <div class="category-icon">🍛</div>
        <div class="category-name">Traditional</div>
        <div class="category-count">Authentic Rice, Kottu &amp; Curries</div>
      </a>
      <a class="category-card" href="${ctx}/foods?cat=Fast+Food">
        <div class="category-icon">🍔</div>
        <div class="category-name">Fast Food</div>
        <div class="category-count">Burgers, Fries &amp; Quick Bites</div>
      </a>
      <a class="category-card" href="${ctx}/foods?cat=Beverages">
        <div class="category-icon">🥤</div>
        <div class="category-name">Beverages</div>
        <div class="category-count">Iced Milo, King Coconut &amp; Teas</div>
      </a>
      <a class="category-card" href="${ctx}/foods?cat=Desserts">
        <div class="category-icon">🍮</div>
        <div class="category-name">Desserts</div>
        <div class="category-count">Watalappan, Custards &amp; Sweets</div>
      </a>
    </div>
  </section>

  <!-- Popular Dishes Section -->
  <section class="popular-section" id="popular">
    <div class="section-header">
      <span class="section-tag">Chef's Recommendations</span>
      <h2 class="section-title">Popular Dishes</h2>
      <p class="section-subtitle">Most loved by our customers across town today.</p>
    </div>

    <div class="grid" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 24px; margin-bottom: 64px;">
      <c:forEach var="f" items="${popularFoods}" begin="0" end="5">
        <article class="card ${f.available ? '' : 'soldout'}" style="background: var(--bg-card); border-radius: var(--radius-lg); border: 1px solid var(--border-subtle); overflow: hidden; display: flex; flex-direction: column; box-shadow: var(--shadow-sm); transition: transform 0.25s, box-shadow 0.25s;">
          <div class="card-img-wrap" style="position: relative; height: 180px; overflow: hidden; background: ${f.themeColor}; display: grid; place-items: center;">
            <c:choose>
              <c:when test="${not empty f.imageFile}">
                <img src="${ctx}/food-image?name=<c:out value='${f.imageFile}'/>" alt="<c:out value='${f.name}'/>" style="width: 100%; height: 100%; object-fit: cover; transition: transform 0.3s;" loading="lazy">
              </c:when>
              <c:otherwise>
                <span style="font-size: 4.5rem; transition: transform 0.3s;"><c:out value="${f.emoji}"/></span>
              </c:otherwise>
            </c:choose>
            <c:if test="${not f.available}">
              <div style="position: absolute; inset: 0; background: rgba(0,0,0,0.55); backdrop-filter: blur(2px); display: grid; place-items: center; color: #fff; font-weight: 800; font-size: 1rem; text-transform: uppercase;">Out of stock</div>
            </c:if>
          </div>
          <div style="padding: 20px; display: flex; flex-direction: column; flex: 1;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
              <span class="status status-preparing" style="font-size: 0.72rem; padding: 2px 8px;"><c:out value="${f.category}"/></span>
              <small style="color: var(--text-muted); font-weight: 600;">⏱️ ${f.preparationTime} mins</small>
            </div>
            <h3 style="font-size: 1.25rem; margin-bottom: 6px;"><c:out value="${f.name}"/></h3>
            <p style="color: var(--text-secondary); font-size: 0.9rem; margin-bottom: 16px; flex: 1;"><c:out value="${f.description}"/></p>
            <div style="display: flex; align-items: center; justify-content: space-between; border-top: 1px solid var(--border-subtle); padding-top: 14px; margin-top: auto;">
              <span style="font-size: 1.25rem; font-weight: 800; color: var(--text-main); font-family: var(--font-display);">Rs. <fmt:formatNumber value="${f.price}" pattern="#,##0.00"/></span>
              <c:choose>
                <c:when test="${not f.available}">
                  <button class="btn-sm" disabled style="opacity: 0.5;">Out of stock</button>
                </c:when>
                <c:otherwise>
                  <form method="post" action="${ctx}/cart" style="margin: 0;">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="id" value="${f.id}">
                    <button class="btn btn-primary btn-sm" type="submit">+ Add to cart</button>
                  </form>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </article>
      </c:forEach>
    </div>

    <div style="text-align: center; margin-bottom: 64px;">
      <a class="btn btn-primary" href="${ctx}/foods" style="padding: 12px 32px; font-size: 1.05rem;">
        View Complete Menu
        <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
      </a>
    </div>
  </section>

  <!-- How It Works Section -->
  <section class="how-it-works-section">
    <div class="section-header">
      <span class="section-tag">Seamless Experience</span>
      <h2 class="section-title">How QuickBite Works</h2>
      <p class="section-subtitle">From hunger to happiness in three simple steps.</p>
    </div>
    <div class="steps-grid">
      <div class="step-card">
        <div class="step-badge">1</div>
        <h3>Choose Your Meal</h3>
        <p>Explore our wide selection of traditional specialties, fast food favourites, drinks, and desserts.</p>
      </div>
      <div class="step-card">
        <div class="step-badge">2</div>
        <h3>Fresh Preparation</h3>
        <p>Your food is made fresh by experienced cooks and packed hot in eco-friendly tamper-proof containers.</p>
      </div>
      <div class="step-card">
        <div class="step-badge">3</div>
        <h3>Express Delivery</h3>
        <p>Track your rider in real time and enjoy piping hot food delivered right to your front door.</p>
      </div>
    </div>
  </section>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
