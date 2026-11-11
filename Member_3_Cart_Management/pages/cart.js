/**
 * Shopping Cart Client Script - Member 3
 */
(function() {
  'use strict';

  document.addEventListener('DOMContentLoaded', () => {
    // Promo Code Auto-fill from suggestion chip
    const promoSuggestions = document.querySelectorAll('.promo-code-chip');
    const promoInput = document.querySelector('input[name="code"]');
    if (promoSuggestions && promoInput) {
      promoSuggestions.forEach(chip => {
        chip.addEventListener('click', function(e) {
          e.preventDefault();
          const code = this.getAttribute('data-code');
          if (code) {
            promoInput.value = code;
            promoInput.focus();
          }
        });
      });
    }
  });
})();
