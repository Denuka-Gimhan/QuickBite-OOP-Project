#!/usr/bin/env bash
# QuickBite Sync Tool (Bash / Linux / macOS)
# Copies all member flat folders and shared files into QuickBite-Combined
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
COMB="$ROOT_DIR/QuickBite-Combined"
COMB_JAVA="$COMB/src/main/java/com/quickbite"
COMB_WEB="$COMB/src/main/webapp"
COMB_CSS="$COMB_WEB/css"
COMB_JS="$COMB_WEB/js"

echo "Syncing Member Folders & Shared Files into QuickBite-Combined..."

MEMBERS=(
  "Member_1_Customer_Management"
  "Member_2_Food_Menu_Management"
  "Member_3_Cart_Management"
  "Member_4_Order_Management"
  "Member_5_Delivery_Management"
  "Member_6_Payment_and_Reports"
)

for m in "${MEMBERS[@]}"; do
  M_DIR="$ROOT_DIR/$m"
  if [ -d "$M_DIR" ]; then
    echo "  -> Syncing $m..."
    [ -d "$M_DIR/model" ] && cp -f "$M_DIR"/model/*.java "$COMB_JAVA/model/" 2>/dev/null || true
    [ -d "$M_DIR/dao" ] && cp -f "$M_DIR"/dao/*.java "$COMB_JAVA/dao/" 2>/dev/null || true
    [ -d "$M_DIR/servlet" ] && cp -f "$M_DIR"/servlet/*.java "$COMB_JAVA/servlet/" 2>/dev/null || true
    [ -d "$M_DIR/util" ] && cp -f "$M_DIR"/util/*.java "$COMB_JAVA/util/" 2>/dev/null || true
    if [ -d "$M_DIR/pages" ]; then
      for f in "$M_DIR/pages"/*; do
        [ -f "$f" ] || continue
        case "$f" in
          *.jsp|*.jspf) cp -f "$f" "$COMB_WEB/" ;;
          *.css) cp -f "$f" "$COMB_CSS/" ;;
          *.js) cp -f "$f" "$COMB_JS/" ;;
        esac
      done
    fi
  fi
done

# Shared files
SH_DIR="$ROOT_DIR/Shared_Project_Files"
if [ -d "$SH_DIR" ]; then
  echo "  -> Syncing Shared_Project_Files..."
  [ -d "$SH_DIR/util" ] && cp -f "$SH_DIR"/util/*.java "$COMB_JAVA/util/" 2>/dev/null || true
  if [ -d "$SH_DIR/pages" ]; then
    for f in "$SH_DIR/pages"/*; do
      [ -f "$f" ] || continue
      case "$f" in
        *.jsp|*.jspf) cp -f "$f" "$COMB_WEB/" ;;
        *.css) cp -f "$f" "$COMB_CSS/" ;;
        *.js) cp -f "$f" "$COMB_JS/" ;;
      esac
    done
  fi
  [ -d "$SH_DIR/css" ] && cp -f "$SH_DIR"/css/*.css "$COMB_CSS/" 2>/dev/null || true
  [ -d "$SH_DIR/js" ] && cp -f "$SH_DIR"/js/*.js "$COMB_JS/" 2>/dev/null || true
  [ -f "$SH_DIR/config/web.xml" ] && cp -f "$SH_DIR/config/web.xml" "$COMB_WEB/WEB-INF/web.xml" || true
  [ -f "$SH_DIR/config/pom.xml" ] && cp -f "$SH_DIR/config/pom.xml" "$COMB/pom.xml" || true
  [ -d "$SH_DIR/docs" ] && cp -f "$SH_DIR"/docs/* "$COMB/" 2>/dev/null || true
fi

echo "Sync Complete! QuickBite-Combined is up to date."
