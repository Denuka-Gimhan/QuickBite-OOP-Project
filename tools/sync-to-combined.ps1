# QuickBite Sync Tool (Windows PowerShell)
# Copies all member flat folders and shared files into QuickBite-Combined
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

Write-Host "Syncing Member Folders & Shared Files into QuickBite-Combined..." -ForegroundColor Cyan

# 1. Sync Member 1 to 6
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
    if (!(Test-Path $mDir)) { continue }
    Write-Host "  -> Syncing $m..." -ForegroundColor Gray

    # model
    if (Test-Path "$mDir\model") {
        Copy-Item "$mDir\model\*.java" "$combJava\model\" -Force -ErrorAction SilentlyContinue
    }
    # dao
    if (Test-Path "$mDir\dao") {
        Copy-Item "$mDir\dao\*.java" "$combJava\dao\" -Force -ErrorAction SilentlyContinue
    }
    # servlet
    if (Test-Path "$mDir\servlet") {
        Copy-Item "$mDir\servlet\*.java" "$combJava\servlet\" -Force -ErrorAction SilentlyContinue
    }
    # util
    if (Test-Path "$mDir\util") {
        Copy-Item "$mDir\util\*.java" "$combJava\util\" -Force -ErrorAction SilentlyContinue
    }
    # pages: jsp, jspf -> webapp; css -> webapp/css; js -> webapp/js
    if (Test-Path "$mDir\pages") {
        Get-ChildItem "$mDir\pages" | ForEach-Object {
            if ($_.Extension -in @(".jsp", ".jspf")) {
                Copy-Item $_.FullName "$combWeb\" -Force
            } elseif ($_.Extension -eq ".css") {
                Copy-Item $_.FullName "$combCss\" -Force
            } elseif ($_.Extension -eq ".js") {
                Copy-Item $_.FullName "$combJs\" -Force
            }
        }
    }
}

# 2. Sync Shared_Project_Files
$shDir = Join-Path $rootDir "Shared_Project_Files"
if (Test-Path $shDir) {
    Write-Host "  -> Syncing Shared_Project_Files..." -ForegroundColor Gray
    if (Test-Path "$shDir\util") {
        Copy-Item "$shDir\util\*.java" "$combJava\util\" -Force -ErrorAction SilentlyContinue
    }
    if (Test-Path "$shDir\pages") {
        Get-ChildItem "$shDir\pages" | ForEach-Object {
            if ($_.Extension -in @(".jsp", ".jspf")) {
                Copy-Item $_.FullName "$combWeb\" -Force
            } elseif ($_.Extension -eq ".css") {
                Copy-Item $_.FullName "$combCss\" -Force
            } elseif ($_.Extension -eq ".js") {
                Copy-Item $_.FullName "$combJs\" -Force
            }
        }
    }
    if (Test-Path "$shDir\css") {
        Copy-Item "$shDir\css\*.css" "$combCss\" -Force -ErrorAction SilentlyContinue
    }
    if (Test-Path "$shDir\js") {
        Copy-Item "$shDir\js\*.js" "$combJs\" -Force -ErrorAction SilentlyContinue
    }
    if (Test-Path "$shDir\config\web.xml") {
        Copy-Item "$shDir\config\web.xml" "$combWeb\WEB-INF\web.xml" -Force
    }
    if (Test-Path "$shDir\config\pom.xml") {
        Copy-Item "$shDir\config\pom.xml" "$comb\pom.xml" -Force
    }
    if (Test-Path "$shDir\docs") {
        Get-ChildItem "$shDir\docs" | ForEach-Object {
            Copy-Item $_.FullName "$comb\" -Force
        }
    }
}

Write-Host "Sync Complete! QuickBite-Combined is up to date." -ForegroundColor Green
