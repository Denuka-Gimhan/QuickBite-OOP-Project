/**
 * Orders Management Client Script - Member 4
 */
(function() {
  'use strict';

  document.addEventListener('DOMContentLoaded', () => {
    // Smooth scroll to target order if hash present
    if (window.location.hash) {
      const target = document.querySelector(window.location.hash);
      if (target) {
        setTimeout(() => {
          target.scrollIntoView({ behavior: 'smooth', block: 'center' });
          target.style.borderColor = 'var(--brand)';
        }, 200);
      }
    }
  });
})();
