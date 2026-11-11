/**
 * Checkout Client Script - Member 3
 */
(function() {
  'use strict';

  const money = n => 'Rs. ' + n.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});

  function updateTotals() {
    const checkedDelivery = document.querySelector('input[name="type"]:checked');
    if (!checkedDelivery) return;

    const fee = parseFloat(checkedDelivery.dataset.fee || '0');
    const subEl = document.getElementById('sub');
    if (!subEl) return;

    const sub = parseFloat(subEl.dataset.v || '0') - parseFloat(subEl.dataset.d || '0');
    const feeEl = document.getElementById('fee');
    const totEl = document.getElementById('tot');

    if (feeEl) feeEl.textContent = fee === 0 ? 'Free' : money(fee);
    if (totEl) totEl.textContent = money(sub + fee);
  }

  document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('input[name="type"]').forEach(r => {
      r.addEventListener('change', updateTotals);
    });
    updateTotals();
  });
})();
