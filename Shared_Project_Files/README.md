# Shared Project Files

This folder contains files shared across the entire QuickBite team. Changes to these files affect everyone and should be made in consensus.

## Flat Directory Layout
- **util/**:
  - `Web.java`: Application-wide singleton DAO lookup, session helpers, sanitization, and currency utilities.
  - `FileHandler.java`: Thread-safe text file storage helper reading/writing to `<user home>/quickbite-data/`.
- **pages/**:
  - `header.jspf`: Top navigation bar with logo, search, role actions, dynamic letter avatar dropdown, dark mode toggle, and mobile menu.
  - `top.jspf`: HTML head with Google Fonts (Baloo 2 and Nunito Sans), viewport meta, and CSS/JS includes.
  - `index.jsp`: Modern food delivery home landing page with hero banner, category chips, popular dishes, how-it-works, and trust badges.
  - `footer.jspf`: Site-wide footer with branding, working hours, and navigation links.
  - `index.css`: Home landing page styles.
- **css/**:
  - `style.css`: Master design system with CSS custom properties, dark theme tokens, responsive layouts, toasts, and accessible modals.
- **js/**:
  - `app.js`: Global vanilla JavaScript providing dark mode persistence, toast alerts, avatar menu, mobile navigation, and modal dialogues.
- **config/**:
  - `web.xml`: Jakarta Servlet 6.0 deployment descriptor, UTF-8 JSP configuration, and welcome file list.
  - `pom.xml`: Maven configuration (Java 17, Tomcat 10.1 Jakarta Servlet API, JSTL).
- **docs/**:
  - `MEMBERS.md`: Comprehensive module breakdown and team member responsibilities.
  - `GIT_GUIDE.md`: Step-by-step Git branching and PR workflow.
  - `TEST_CHECKLIST.md`: Full end-to-end testing verification plan.
