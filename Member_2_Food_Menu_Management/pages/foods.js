/**
 * Food Menu Client Script - Member 2
 */
(function() {
  'use strict';

  document.addEventListener('DOMContentLoaded', () => {
    // Animate Add to Cart button clicks
    const addForms = document.querySelectorAll('form[action*="/cart"]');
    addForms.forEach(form => {
      form.addEventListener('submit', function() {
        const btn = this.querySelector('button');
        if (btn) {
          btn.style.transform = 'scale(0.92)';
          setTimeout(() => { btn.style.transform = ''; }, 180);
        }
      });
    });

    // Keyboard shortcut '/' to focus search input
    document.addEventListener('keydown', (e) => {
      if (e.key === '/' && document.activeElement.tagName !== 'INPUT' && document.activeElement.tagName !== 'TEXTAREA') {
        e.preventDefault();
        const searchInput = document.querySelector('.nav-search-input');
        if (searchInput) searchInput.focus();
      }
    });
  });
})();
