/**
 * Reports & Analytics Client Script - Member 6
 */
(function() {
  'use strict';

  document.addEventListener('DOMContentLoaded', () => {
    // Animate chart fill widths on page load
    const fills = document.querySelectorAll('.chart-fill');
    fills.forEach(fill => {
      const targetWidth = fill.style.width;
      fill.style.width = '0%';
      requestAnimationFrame(() => {
        setTimeout(() => {
          fill.style.width = targetWidth;
        }, 100);
      });
    });
  });
})();
