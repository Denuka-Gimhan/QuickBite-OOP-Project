#!/usr/bin/env bash
# QuickBite Verification Tool (Bash / Linux / macOS)
# Verifies that member folders and Shared_Project_Files match QuickBite-Combined
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
COMB="$ROOT_DIR/QuickBite-Combined"
COMB_JAVA="$COMB/src/main/java/com/quickbite"
COMB_WEB="$COMB/src/main/webapp"
COMB_CSS="$COMB_WEB/css"
COMB_JS="$COMB_WEB/js"

echo "Verifying QuickBite codebase synchronization..."

MISMATCHES=0
CHECKED=0

check_match() {
  local fileA="$1"
  local fileB="$2"
  CHECKED=$((CHECKED + 1))
  if [ ! -f "$fileB" ]; then
    echo "  [FAIL] Missing in Combined: $fileB (from $fileA)"
    MISMATCHES=$((MISMATCHES + 1))
    return
  fi
  if ! cmp -s "$fileA" "$fileB"; then
    echo "  [FAIL] Content mismatch: $fileA != $fileB"
    MISMATCHES=$((MISMATCHES + 1))
  fi
}

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
  if [ ! -d "$M_DIR" ]; then
    echo "  [FAIL] Missing member folder: $m"
    MISMATCHES=$((MISMATCHES + 1))
    continue
  fi

  [ -d "$M_DIR/model" ] && for f in "$M_DIR"/model/*.java; do [ -f "$f" ] && check_match "$f" "$COMB_JAVA/model/$(basename "$f")"; done
  [ -d "$M_DIR/dao" ] && for f in "$M_DIR"/dao/*.java; do [ -f "$f" ] && check_match "$f" "$COMB_JAVA/dao/$(basename "$f")"; done
  [ -d "$M_DIR/servlet" ] && for f in "$M_DIR"/servlet/*.java; do [ -f "$f" ] && check_match "$f" "$COMB_JAVA/servlet/$(basename "$f")"; done
  [ -d "$M_DIR/util" ] && for f in "$M_DIR"/util/*.java; do [ -f "$f" ] && check_match "$f" "$COMB_JAVA/util/$(basename "$f")"; done
  if [ -d "$M_DIR/pages" ]; then
    for f in "$M_DIR"/pages/*; do
      [ -f "$f" ] || continue
      case "$f" in
        *.jsp|*.jspf) check_match "$f" "$COMB_WEB/$(basename "$f")" ;;
        *.css) check_match "$f" "$COMB_CSS/$(basename "$f")" ;;
        *.js) check_match "$f" "$COMB_JS/$(basename "$f")" ;;
      esac
    done
  fi
done

# Shared files
SH_DIR="$ROOT_DIR/Shared_Project_Files"
if [ -d "$SH_DIR" ]; then
  [ -d "$SH_DIR/util" ] && for f in "$SH_DIR"/util/*.java; do [ -f "$f" ] && check_match "$f" "$COMB_JAVA/util/$(basename "$f")"; done
  if [ -d "$SH_DIR/pages" ]; then
    for f in "$SH_DIR"/pages/*; do
      [ -f "$f" ] || continue
      case "$f" in
        *.jsp|*.jspf) check_match "$f" "$COMB_WEB/$(basename "$f")" ;;
        *.css) check_match "$f" "$COMB_CSS/$(basename "$f")" ;;
        *.js) check_match "$f" "$COMB_JS/$(basename "$f")" ;;
      esac
    done
  fi
  [ -d "$SH_DIR/css" ] && for f in "$SH_DIR"/css/*.css; do [ -f "$f" ] && check_match "$f" "$COMB_CSS/$(basename "$f")"; done
  [ -d "$SH_DIR/js" ] && for f in "$SH_DIR"/js/*.js; do [ -f "$f" ] && check_match "$f" "$COMB_JS/$(basename "$f")"; done
  [ -f "$SH_DIR/config/web.xml" ] && check_match "$SH_DIR/config/web.xml" "$COMB_WEB/WEB-INF/web.xml"
  [ -f "$SH_DIR/config/pom.xml" ] && check_match "$SH_DIR/config/pom.xml" "$COMB/pom.xml"
fi

echo "Checked $CHECKED files across all modules."
if [ "$MISMATCHES" -eq 0 ]; then
  echo "[OK] Verification successful! Member folders and QuickBite-Combined are 100% IDENTICAL."
  exit 0
else
  echo "[FAIL] Found $MISMATCHES discrepancy(ies)."
  exit 1
fi
