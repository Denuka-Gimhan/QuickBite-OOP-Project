/**
 * QuickBite Application JavaScript
 * Vanilla JS for Theme, Toasts, Dropdowns, Modals, and Micro-interactions
 */
(function() {
  'use strict';

  // 1. Theme (Dark / Light Mode)
  const THEME_KEY = 'quickbite_theme';
  function initTheme() {
    const saved = localStorage.getItem(THEME_KEY);
    const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
    const theme = saved || (prefersDark ? 'dark' : 'light');
    setTheme(theme, false);

    document.querySelectorAll('.theme-toggle-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        const current = document.documentElement.getAttribute('data-theme') || 'light';
        const next = current === 'dark' ? 'light' : 'dark';
        setTheme(next, true);
      });
    });
  }

  function setTheme(theme, save) {
    document.documentElement.setAttribute('data-theme', theme);
    if (save) localStorage.setItem(THEME_KEY, theme);
    document.querySelectorAll('.theme-toggle-btn').forEach(btn => {
      btn.setAttribute('aria-label', theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode');
      btn.innerHTML = theme === 'dark' 
        ? '<svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="5"/><line x1="12" y1="1" x2="12" y2="3"/><line x1="12" y1="21" x2="12" y2="23"/><line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/><line x1="1" y1="12" x2="3" y2="12"/><line x1="21" y1="12" x2="23" y2="12"/><line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/></svg>'
        : '<svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>';
    });
  }

  // 2. Toast Notifications
  function initToasts() {
    let container = document.getElementById('toast-container');
    if (!container) {
      container = document.createElement('div');
      container.id = 'toast-container';
      document.body.appendChild(container);
    }

    // Convert flash message from server into animated toast
    const flashElements = document.querySelectorAll('.flash');
    flashElements.forEach(flash => {
      const text = flash.textContent.trim();
      const isError = flash.classList.contains('error');
      if (text) {
        showToast(text, isError ? 'error' : 'success');
        // Hide standard flash wrap if toast was created
        const wrap = flash.closest('.flash-wrap');
        if (wrap) wrap.style.display = 'none';
      }
    });

    // Also check query param ?msg=
    const params = new URLSearchParams(window.location.search);
    const msg = params.get('msg');
    if (msg && flashElements.length === 0) {
      const isErr = msg.toLowerCase().includes('fail') || msg.toLowerCase().includes('error') || msg.toLowerCase().includes('not found');
      showToast(msg, isErr ? 'error' : 'success');
    }
  }

  window.showToast = function(message, type = 'info', duration = 4000) {
    let container = document.getElementById('toast-container');
    if (!container) {
      container = document.createElement('div');
      container.id = 'toast-container';
      document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    
    let icon = '🔔';
    if (type === 'success') icon = '✓';
    else if (type === 'error') icon = '✕';
    else if (type === 'info') icon = 'ℹ';

    toast.innerHTML = `
      <span class="toast-icon">${icon}</span>
      <span class="toast-msg">${escapeHtml(message)}</span>
      <button class="toast-close" aria-label="Close notification">&times;</button>
    `;

    toast.querySelector('.toast-close').addEventListener('click', () => {
      removeToast(toast);
    });

    container.appendChild(toast);
    // Trigger transition
    requestAnimationFrame(() => {
      toast.classList.add('show');
    });

    if (duration > 0) {
      setTimeout(() => {
        removeToast(toast);
      }, duration);
    }
  };

  function removeToast(toast) {
    toast.classList.remove('show');
    setTimeout(() => {
      if (toast.parentNode) toast.parentNode.removeChild(toast);
    }, 300);
  }

  function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/[&<>"']/g, m => ({
      '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    }[m]));
  }

  // 3. User Avatar Dropdown (Part C)
  function initUserMenu() {
    const avatarBtn = document.getElementById('userAvatarBtn');
    const dropdown = document.getElementById('userDropdown');
    if (!avatarBtn || !dropdown) return;

    avatarBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      dropdown.classList.toggle('open');
      avatarBtn.setAttribute('aria-expanded', dropdown.classList.contains('open'));
    });

    document.addEventListener('click', (e) => {
      if (!dropdown.contains(e.target) && !avatarBtn.contains(e.target)) {
        dropdown.classList.remove('open');
        avatarBtn.setAttribute('aria-expanded', 'false');
      }
    });

    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape' && dropdown.classList.contains('open')) {
        dropdown.classList.remove('open');
        avatarBtn.setAttribute('aria-expanded', 'false');
        avatarBtn.focus();
      }
    });
  }

  // 4. Mobile Navigation Drawer Toggle
  function initMobileNav() {
    const toggleBtn = document.getElementById('mobileNavToggle');
    const mobileMenu = document.getElementById('mobileNavMenu');
    if (!toggleBtn || !mobileMenu) return;

    toggleBtn.addEventListener('click', () => {
      mobileMenu.classList.toggle('open');
      const isOpen = mobileMenu.classList.contains('open');
      toggleBtn.setAttribute('aria-expanded', isOpen);
      toggleBtn.innerHTML = isOpen 
        ? '<svg viewBox="0 0 24 24" width="24" height="24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>'
        : '<svg viewBox="0 0 24 24" width="24" height="24" fill="none" stroke="currentColor" stroke-width="2"><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="18" x2="21" y2="18"/></svg>';
    });
  }

  // 5. Accessible Confirmation Modal
  let activeConfirmCallback = null;
  function initConfirmModal() {
    const backdrop = document.createElement('div');
    backdrop.id = 'customConfirmModal';
    backdrop.className = 'custom-modal-backdrop';
    backdrop.innerHTML = `
      <div class="custom-modal" role="dialog" aria-modal="true" aria-labelledby="confirmModalTitle">
        <h3 id="confirmModalTitle" class="custom-modal-title">Confirm Action</h3>
        <p id="confirmModalDesc" class="custom-modal-desc">Are you sure you want to proceed?</p>
        <div class="custom-modal-actions">
          <button id="confirmCancelBtn" type="button" class="btn btn-ghost">Cancel</button>
          <button id="confirmOkBtn" type="button" class="btn btn-primary btn-danger">Confirm</button>
        </div>
      </div>
    `;
    document.body.appendChild(backdrop);

    const cancelBtn = backdrop.querySelector('#confirmCancelBtn');
    const okBtn = backdrop.querySelector('#confirmOkBtn');

    cancelBtn.addEventListener('click', () => {
      closeConfirm(false);
    });

    okBtn.addEventListener('click', () => {
      closeConfirm(true);
    });

    backdrop.addEventListener('click', (e) => {
      if (e.target === backdrop) closeConfirm(false);
    });

    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape' && backdrop.classList.contains('open')) {
        closeConfirm(false);
      }
    });

    // Intercept forms with data-confirm or standard destructive confirmations
    document.addEventListener('submit', (e) => {
      const form = e.target;
      const confirmMsg = form.getAttribute('data-confirm');
      if (confirmMsg && !form._confirmed) {
        e.preventDefault();
        window.showConfirmDialog('Confirm Action', confirmMsg, () => {
          form._confirmed = true;
          form.submit();
        });
      }
    });
  }

  window.showConfirmDialog = function(title, message, onConfirm) {
    const backdrop = document.getElementById('customConfirmModal');
    if (!backdrop) {
      if (confirm(message)) onConfirm();
      return;
    }
    backdrop.querySelector('#confirmModalTitle').textContent = title || 'Confirm Action';
    backdrop.querySelector('#confirmModalDesc').textContent = message || 'Are you sure?';
    activeConfirmCallback = onConfirm;
    backdrop.classList.add('open');
    backdrop.querySelector('#confirmCancelBtn').focus();
  };

  function closeConfirm(confirmed) {
    const backdrop = document.getElementById('customConfirmModal');
    if (!backdrop) return;
    backdrop.classList.remove('open');
    if (confirmed && typeof activeConfirmCallback === 'function') {
      const cb = activeConfirmCallback;
      activeConfirmCallback = null;
      cb();
    } else {
      activeConfirmCallback = null;
    }
  }

  // 6. Food Image Fallback
  function initImageFallbacks() {
    document.querySelectorAll('img').forEach(img => {
      img.addEventListener('error', function() {
        const altEmoji = this.getAttribute('data-fallback-emoji') || '🍽️';
        const parent = this.parentElement;
        if (parent && parent.classList.contains('card-img')) {
          parent.innerHTML = `<span class="emoji-fallback">${altEmoji}</span>`;
        }
      });
    });
  }

  // Initialize all components on DOMContentLoaded
  document.addEventListener('DOMContentLoaded', () => {
    initTheme();
    initToasts();
    initUserMenu();
    initMobileNav();
    initConfirmModal();
    initImageFallbacks();
  });
})();
