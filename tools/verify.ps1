# QuickBite Verification Tool (Windows PowerShell)
# Verifies that member folders and Shared_Project_Files match QuickBite-Combined
$ErrorActionPreference = "Stop"

$rootDir = Split-Path -Parent $PSScriptRoot
if (!(Test-Path "$rootDir\QuickBite-Combined")) {
    $rootDir = Get-Location
}

$comb = Join-Path $rootDir "QuickBite-Combined"
$combJava = Join-Path $comb "src\main\java\com\quickbite"
$combWeb = Join-Path $comb "src\main\webapp"
$combCss = Join-Path $combWeb "css"
$combJs = Join-Path $combWeb "js"

Write-Host "Verifying QuickBite codebase synchronization..." -ForegroundColor Cyan

$mismatches = @()
$checkedFiles = 0
$matchedCombinedFiles = [System.Collections.Generic.HashSet[string]]::new()

function Compare-TwoFiles($fileA, $fileB, $label) {
    $script:checkedFiles++
    if (!(Test-Path $fileB)) {
        $script:mismatches += "MISSING in Combined: $fileB (from $fileA)"
        return
    }
    $hashA = (Get-FileHash -Path $fileA -Algorithm SHA256).Hash
    $hashB = (Get-FileHash -Path $fileB -Algorithm SHA256).Hash
    if ($hashA -ne $hashB) {
        $script:mismatches += "CONTENT MISMATCH: $fileA != $fileB"
    } else {
        $script:matchedCombinedFiles.Add((Resolve-Path $fileB).Path) | Out-Null
    }
}

# 1. Check Members 1..6
$members = @(
    "Member_1_Customer_Management",
    "Member_2_Food_Menu_Management",
    "Member_3_Cart_Management",
    "Member_4_Order_Management",
    "Member_5_Delivery_Management",
    "Member_6_Payment_and_Reports"
)

foreach ($m in $members) {
    $mDir = Join-Path $rootDir $m
    if (!(Test-Path $mDir)) {
        $mismatches += "Missing member folder: $m"
        continue
    }

    # model
    if (Test-Path "$mDir\model") {
        Get-ChildItem "$mDir\model\*.java" | ForEach-Object {
            Compare-TwoFiles $_.FullName "$combJava\model\$($_.Name)" "$m\model"
        }
    }
    # dao
    if (Test-Path "$mDir\dao") {
        Get-ChildItem "$mDir\dao\*.java" | ForEach-Object {
            Compare-TwoFiles $_.FullName "$combJava\dao\$($_.Name)" "$m\dao"
        }
    }
    # servlet
    if (Test-Path "$mDir\servlet") {
        Get-ChildItem "$mDir\servlet\*.java" | ForEach-Object {
            Compare-TwoFiles $_.FullName "$combJava\servlet\$($_.Name)" "$m\servlet"
        }
    }
    # util
    if (Test-Path "$mDir\util") {
        Get-ChildItem "$mDir\util\*.java" | ForEach-Object {
            Compare-TwoFiles $_.FullName "$combJava\util\$($_.Name)" "$m\util"
        }
    }
    # pages
    if (Test-Path "$mDir\pages") {
        Get-ChildItem "$mDir\pages" | ForEach-Object {
            if ($_.Extension -in @(".jsp", ".jspf")) {
                Compare-TwoFiles $_.FullName "$combWeb\$($_.Name)" "$m\pages"
            } elseif ($_.Extension -eq ".css") {
                Compare-TwoFiles $_.FullName "$combCss\$($_.Name)" "$m\pages"
            } elseif ($_.Extension -eq ".js") {
                Compare-TwoFiles $_.FullName "$combJs\$($_.Name)" "$m\pages"
            }
        }
    }
}

# 2. Check Shared_Project_Files
$shDir = Join-Path $rootDir "Shared_Project_Files"
if (Test-Path $shDir) {
    if (Test-Path "$shDir\util") {
        Get-ChildItem "$shDir\util\*.java" | ForEach-Object {
            Compare-TwoFiles $_.FullName "$combJava\util\$($_.Name)" "Shared\util"
        }
    }
    if (Test-Path "$shDir\pages") {
        Get-ChildItem "$shDir\pages" | ForEach-Object {
            if ($_.Extension -in @(".jsp", ".jspf")) {
                Compare-TwoFiles $_.FullName "$combWeb\$($_.Name)" "Shared\pages"
            } elseif ($_.Extension -eq ".css") {
                Compare-TwoFiles $_.FullName "$combCss\$($_.Name)" "Shared\pages"
            } elseif ($_.Extension -eq ".js") {
                Compare-TwoFiles $_.FullName "$combJs\$($_.Name)" "Shared\pages"
            }
        }
    }
    if (Test-Path "$shDir\css") {
        Get-ChildItem "$shDir\css\*.css" | ForEach-Object {
            Compare-TwoFiles $_.FullName "$combCss\$($_.Name)" "Shared\css"
        }
    }
    if (Test-Path "$shDir\js") {
        Get-ChildItem "$shDir\js\*.js" | ForEach-Object {
            Compare-TwoFiles $_.FullName "$combJs\$($_.Name)" "Shared\js"
        }
    }
    if (Test-Path "$shDir\config\web.xml") {
        Compare-TwoFiles "$shDir\config\web.xml" "$combWeb\WEB-INF\web.xml" "Shared\config"
    }
    if (Test-Path "$shDir\config\pom.xml") {
        Compare-TwoFiles "$shDir\config\pom.xml" "$comb\pom.xml" "Shared\config"
    }
}

# 3. Check for any orphaned source files in QuickBite-Combined src that are not in member folders
$allCombinedSrcFiles = (Get-ChildItem -Recurse "$comb\src" | Where-Object { !$_.PSIsContainer }).FullName
foreach ($cFile in $allCombinedSrcFiles) {
    $norm = (Resolve-Path $cFile).Path
    if (!$matchedCombinedFiles.Contains($norm)) {
        # Allow target/ or build directories, only check src
        $rel = $norm.Substring($comb.Length)
        $mismatches += "ORPHANED in Combined (not in member/shared folders): $rel"
    }
}

# Report results
Write-Host "Checked $checkedFiles files across all modules." -ForegroundColor Gray
if ($mismatches.Count -eq 0) {
    Write-Host "[OK] Verification successful! Member folders and QuickBite-Combined are 100% IDENTICAL." -ForegroundColor Green
    exit 0
} else {
    Write-Host "[FAIL] Found $($mismatches.Count) discrepancy(ies):" -ForegroundColor Red
    foreach ($err in $mismatches) {
        Write-Host "  - $err" -ForegroundColor Red
    }
    exit 1
}
