<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${editing ? 'Edit Food' : 'Add New Food'}"/>
<c:set var="extraCss" value="food-form.css"/>
<c:set var="extraJs" value="food-form.js"/>
<%@ include file="top.jspf" %>

<main class="container">
  <form class="form-card" method="post" enctype="multipart/form-data" action="${ctx}/foods">
    <div style="border-bottom: 1px solid var(--border-subtle); padding-bottom: 16px; margin-bottom: 24px;">
      <h1 class="page-title" style="margin-bottom: 6px; font-size: 1.85rem;">${editing ? 'Edit Food Dish' : 'Add New Food Dish'}</h1>
      <p style="color: var(--text-muted); font-size: 0.95rem;">Configure menu item details, pricing, photos, and availability.</p>
    </div>

    <c:if test="${not empty error}">
      <div class="flash error"><c:out value="${error}"/></div>
    </c:if>

    <input type="hidden" name="action" value="${editing ? 'update' : 'add'}">
    <input type="hidden" name="id" value="${food.id}">

    <div class="form-grid">
      <label class="full">
        Food Name <span style="color: var(--brand);">*</span>
        <input required name="name" maxlength="60" value="<c:out value='${food.name}'/>" placeholder="e.g. Chicken Kottu Roti">
      </label>

      <label>
        Category <span style="color: var(--brand);">*</span>
        <select name="category">
          <c:forEach var="cat" items="${categories}">
            <option value="${cat}" ${food.category == cat ? 'selected' : ''}><c:out value="${cat}"/></option>
          </c:forEach>
        </select>
      </label>

      <label>
        Price in Sri Lankan Rupees (Rs.) <span style="color: var(--brand);">*</span>
        <input required type="number" step="0.01" min="0.01" name="price" value="${food.price > 0 ? food.price : ''}" placeholder="1250.00">
      </label>

      <label class="full">
        Emoji Fallback Tile <small style="color: var(--text-muted); font-weight: normal;">(Shown when no photo is uploaded)</small>
        <input name="emoji" maxlength="4" value="<c:out value='${food.emoji}'/>" placeholder="🍲">
      </label>

      <!-- Photo Upload & Live Preview (Part D) -->
      <div class="full photo-upload-zone">
        <span style="font-weight: 700; font-size: 0.95rem; margin-bottom: 12px; display: block;">
          Food Photo <small style="color: var(--text-muted); font-weight: normal;">(Optional &bull; JPG, PNG, WEBP or GIF &bull; Max 5 MB)</small>
        </span>
        <div class="photo-row-layout">
          <div class="photo-preview-box" id="photoPreview">
            <c:choose>
              <c:when test="${not empty food.imageFile}">
                <img id="photoImg" src="${ctx}/food-image?name=<c:out value='${food.imageFile}'/>" alt="Current photo">
                <span id="photoEmpty" class="photo-preview-placeholder" hidden>🍽️</span>
              </c:when>
              <c:otherwise>
                <img id="photoImg" alt="" hidden>
                <span id="photoEmpty" class="photo-preview-placeholder">🍽️</span>
              </c:otherwise>
            </c:choose>
          </div>
          <div class="photo-controls-col">
            <input type="file" name="image" id="imageInput" accept="image/jpeg,image/png,image/webp,image/gif">
            <small id="photoMsg" class="photo-error-msg"></small>
            <c:if test="${not empty food.imageFile}">
              <label class="check-label" style="font-size: 0.88rem; font-weight: 600;">
                <input type="checkbox" name="removeImage" id="removeImageCheck" value="true">
                Remove current uploaded photo
              </label>
            </c:if>
          </div>
        </div>
      </div>

      <label class="full check-label" style="margin-top: 8px;">
        <input type="checkbox" name="available" value="true" ${empty food or food.available ? 'checked' : ''}>
        <strong>Available In Stock</strong> &mdash; Untick to flag this food as "Out of stock" on the menu
      </label>

      <label class="full">
        Description
        <textarea name="description" rows="3" maxlength="150" placeholder="Crispy chopped parotta tossed with spices, roast chicken, egg and curry sauce..."><c:out value="${food.description}"/></textarea>
      </label>
    </div>

    <div class="form-actions">
      <button class="btn btn-primary" type="submit">${editing ? 'Save Changes' : 'Create Food Item'}</button>
      <a class="btn btn-ghost" href="${ctx}/foods">Cancel</a>
    </div>
  </form>
</main>

<%@ include file="footer.jspf" %>
</body>
</html>
