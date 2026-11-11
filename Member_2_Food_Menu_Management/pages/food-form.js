/**
 * Food Form Image Upload & Validation - Member 2
 */
(function() {
  'use strict';

  document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('imageInput');
    const previewImg = document.getElementById('photoImg');
    const placeholder = document.getElementById('photoEmpty');
    const msg = document.getElementById('photoMsg');
    const removeCheck = document.getElementById('removeImageCheck');

    if (!input) return;

    input.addEventListener('change', function() {
      msg.textContent = '';
      const file = this.files[0];
      if (!file) return;

      // 5 MB limit
      if (file.size > 5 * 1024 * 1024) {
        msg.textContent = 'Photo must be 5 MB or smaller.';
        this.value = '';
        return;
      }

      // Valid extensions
      if (!/^image\/(jpeg|png|webp|gif)$/.test(file.type)) {
        msg.textContent = 'Please choose a JPG, PNG, WEBP or GIF image.';
        this.value = '';
        return;
      }

      const objectUrl = URL.createObjectURL(file);
      previewImg.src = objectUrl;
      previewImg.hidden = false;
      if (placeholder) placeholder.hidden = true;
      if (removeCheck) removeCheck.checked = false;
    });

    if (removeCheck) {
      removeCheck.addEventListener('change', function() {
        if (this.checked) {
          input.value = '';
          previewImg.hidden = true;
          if (placeholder) placeholder.hidden = false;
          msg.textContent = '';
        }
      });
    }
  });
})();
