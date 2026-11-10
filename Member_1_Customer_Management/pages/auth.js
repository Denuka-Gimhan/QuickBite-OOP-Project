/**
 * Authentication Client Script - Member 1
 */
(function() {
  'use strict';

  document.addEventListener('DOMContentLoaded', () => {
    // Show / Hide Password Toggles
    const toggleButtons = document.querySelectorAll('.pwd-toggle-btn');
    toggleButtons.forEach(btn => {
      btn.addEventListener('click', function(e) {
        e.preventDefault();
        const wrap = this.closest('.pwd-input-wrap');
        const input = wrap.querySelector('input');
        if (!input) return;

        const isPassword = input.type === 'password';
        input.type = isPassword ? 'text' : 'password';
        this.setAttribute('aria-label', isPassword ? 'Hide password' : 'Show password');
        this.innerHTML = isPassword
          ? '<svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/><line x1="1" y1="1" x2="23" y2="23"/></svg>'
          : '<svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>';
      });
    });

    // Password Match Validation
    const regForm = document.querySelector('form[action*="/users"]');
    const pwd = document.querySelector('input[name="password"]');
    const confirmPwd = document.querySelector('input[name="confirm"]');
    if (regForm && pwd && confirmPwd) {
      regForm.addEventListener('submit', function(e) {
        if (pwd.value !== confirmPwd.value) {
          e.preventDefault();
          if (window.showToast) window.showToast('Passwords do not match. Please re-enter.', 'error');
          else alert('Passwords do not match.');
          confirmPwd.focus();
        }
      });
    }
  });
})();
