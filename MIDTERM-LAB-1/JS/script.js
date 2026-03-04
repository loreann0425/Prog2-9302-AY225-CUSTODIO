const csvFile = document.getElementById("csvFile");
const resetBtn = document.getElementById("resetBtn");
const summaryBtn = document.getElementById("summaryBtn");
const monthlyBtn = document.getElementById("monthlyBtn");
const topBtn = document.getElementById("topBtn");
const categoryBtn = document.getElementById("categoryBtn");
const exitBtn = document.getElementById("exitBtn");
const clearOutputBtn = document.getElementById("clearOutputBtn");

const output = document.getElementById("output");
const outputTitle = document.getElementById("outputTitle");
const datasetInfo = document.getElementById("datasetInfo");
const statusBadge = document.getElementById("statusBadge");
const selectedFileBox = document.getElementById("selectedFileBox");

let records = [];

class GameRecord {
  constructor(
    img,
    title,
    consoleName,
    genre,
    publisher,
    developer,
    criticScore,
    totalSales,
    naSales,
    jpSales,
    palSales,
    otherSales,
    releaseDate,
    lastUpdate
  ) {
    this.img = img;
    this.title = title;
    this.console = consoleName;
    this.genre = genre;
    this.publisher = publisher;
    this.developer = developer;
    this.criticScore = criticScore;
    this.totalSales = totalSales;
    this.naSales = naSales;
    this.jpSales = jpSales;
    this.palSales = palSales;
    this.otherSales = otherSales;
    this.releaseDate = releaseDate;
    this.lastUpdate = lastUpdate;
  }
}

csvFile.addEventListener("change", handleFileSelection);
resetBtn.addEventListener("click", resetDashboard);
summaryBtn.addEventListener("click", viewDatasetSummary);
monthlyBtn.addEventListener("click", showMonthlySales);
topBtn.addEventListener("click", showTopCustomers);
categoryBtn.addEventListener("click", showCategoryAnalysis);
exitBtn.addEventListener("click", exitProgram);
clearOutputBtn.addEventListener("click", clearOutput);

function setMenuState(enabled) {
  summaryBtn.disabled = !enabled;
  monthlyBtn.disabled = !enabled;
  topBtn.disabled = !enabled;
  categoryBtn.disabled = !enabled;
}

function setStatus(text, loaded = false) {
  statusBadge.textContent = text;
  statusBadge.className = loaded ? "badge badge-success" : "badge badge-idle";
}

function showMessage(title, message) {
  outputTitle.textContent = title;
  output.innerHTML = `<div class="console-text">${escapeHtml(message)}</div>`;
}

function showHTML(title, html) {
  outputTitle.textContent = title;
  output.innerHTML = html;
}

function clearOutput() {
  outputTitle.textContent = "Output Cleared";
  output.innerHTML = `<div class="empty-state">Output has been cleared.</div>`;
}

function escapeHtml(text) {
  return String(text)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;");
}

function parseCSVLine(line) {
  const result = [];
  let current = "";
  let inQuotes = false;

  for (let i = 0; i < line.length; i++) {
    const ch = line[i];
    const next = line[i + 1];

    if (ch === '"') {
      if (inQuotes && next === '"') {
        current += '"';
        i++;
      } else {
        inQuotes = !inQuotes;
      }
    } else if (ch === "," && !inQuotes) {
      result.push(current);
      current = "";
    } else {
      current += ch;
    }
  }

  result.push(current);
  return result;
}

function parseNumber(value) {
  const num = parseFloat(String(value).trim());
  return isNaN(num) ? 0 : num;
}

function shortenText(text, maxLength) {
  if (!text) return "";
  return text.length <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
}

function formatDate(dateValue) {
  const date = new Date(dateValue);
  if (isNaN(date)) return null;
  return date.toISOString().split("T")[0];
}

function validateHeaders(headers) {
  const requiredHeaders = [
    "img",
    "title",
    "console",
    "genre",
    "publisher",
    "developer",
    "critic_score",
    "total_sales",
    "na_sales",
    "jp_sales",
    "pal_sales",
    "other_sales",
    "release_date",
    "last_update"
  ];

  const normalized = headers.map(h => h.trim().toLowerCase());

  for (const header of requiredHeaders) {
    if (!normalized.includes(header)) {
      throw new Error(`Missing required column: ${header}`);
    }
  }
}

function getHeaderMap(headers) {
  const map = {};
  headers.forEach((header, index) => {
    map[header.trim().toLowerCase()] = index;
  });
  return map;
}

function buildInfoBox(fileName) {
  const uniqueConsoles = new Set(records.map(r => r.console).filter(Boolean)).size;
  const uniqueGenres = new Set(records.map(r => r.genre).filter(Boolean)).size;

  datasetInfo.innerHTML = `
    <strong>Loaded File:</strong> ${escapeHtml(fileName)}<br>
    <strong>Total Records:</strong> ${records.length}<br>
    <strong>Unique Consoles:</strong> ${uniqueConsoles}<br>
    <strong>Unique Genres:</strong> ${uniqueGenres}
  `;
}

function isCSVFile(file) {
  const fileName = file.name.toLowerCase();
  return fileName.endsWith(".csv");
}

function handleFileSelection() {
  const file = csvFile.files[0];

  if (!file) {
    selectedFileBox.className = "selected-file-box";
    selectedFileBox.textContent = "No file selected yet.";
    return;
  }

  if (!isCSVFile(file)) {
    records = [];
    setMenuState(false);
    setStatus("No Dataset Loaded", false);
    datasetInfo.textContent = "Waiting for CSV upload...";

    selectedFileBox.className = "selected-file-box file-error";
    selectedFileBox.innerHTML = `
      <strong>Invalid File:</strong> ${escapeHtml(file.name)}<br>
      Only .csv files are allowed.
    `;

    showMessage("Upload Error", "Invalid file uploaded. Please choose a CSV (.csv) file only.");
    return;
  }

  selectedFileBox.className = "selected-file-box file-success";
  selectedFileBox.innerHTML = `
    <strong>Selected File:</strong> ${escapeHtml(file.name)}<br>
    <strong>File Size:</strong> ${(file.size / 1024).toFixed(2)} KB<br>
    <strong>Status:</strong> Loading automatically...
  `;

  loadDatasetAutomatically(file);
}

function loadDatasetAutomatically(file) {
  const reader = new FileReader();

  reader.onload = function (event) {
    try {
      const content = event.target.result;
      const lines = content.split(/\r?\n/).filter(line => line.trim() !== "");

      if (lines.length === 0) {
        throw new Error("CSV file is empty.");
      }

      const headers = parseCSVLine(lines[0]);
      validateHeaders(headers);
      const headerMap = getHeaderMap(headers);

      const loadedRecords = [];

      for (let i = 1; i < lines.length; i++) {
        const values = parseCSVLine(lines[i]);

        while (values.length < headers.length) {
          values.push("");
        }

        const getValue = (column) => {
          const index = headerMap[column];
          return index !== undefined ? String(values[index]).trim() : "";
        };

        const record = new GameRecord(
          getValue("img"),
          getValue("title"),
          getValue("console"),
          getValue("genre"),
          getValue("publisher"),
          getValue("developer"),
          parseNumber(getValue("critic_score")),
          parseNumber(getValue("total_sales")),
          parseNumber(getValue("na_sales")),
          parseNumber(getValue("jp_sales")),
          parseNumber(getValue("pal_sales")),
          parseNumber(getValue("other_sales")),
          getValue("release_date"),
          getValue("last_update")
        );

        loadedRecords.push(record);
      }

      if (loadedRecords.length === 0) {
        throw new Error("CSV file contains no usable data rows.");
      }

      records = loadedRecords;
      setMenuState(true);
      setStatus("Dataset Loaded", true);
      buildInfoBox(file.name);

      selectedFileBox.className = "selected-file-box file-success";
      selectedFileBox.innerHTML = `
        <strong>Selected File:</strong> ${escapeHtml(file.name)}<br>
        <strong>File Size:</strong> ${(file.size / 1024).toFixed(2)} KB<br>
        <strong>Status:</strong> Loaded successfully
      `;

      showMessage(
        "Dataset Loaded Successfully",
        `The selected CSV file is now loaded automatically.\nTotal records loaded: ${records.length}\n\nYou may now use the menu buttons.`
      );
    } catch (error) {
      records = [];
      setMenuState(false);
      setStatus("No Dataset Loaded", false);
      datasetInfo.textContent = "Waiting for CSV upload...";

      selectedFileBox.className = "selected-file-box file-error";
      selectedFileBox.innerHTML = `
        <strong>File:</strong> ${escapeHtml(file.name)}<br>
        <strong>Status:</strong> Failed to load<br>
        <strong>Reason:</strong> ${escapeHtml(error.message)}
      `;

      showMessage("Load Error", error.message);
    }
  };

  reader.onerror = function () {
    records = [];
    setMenuState(false);
    setStatus("No Dataset Loaded", false);
    datasetInfo.textContent = "Waiting for CSV upload...";

    selectedFileBox.className = "selected-file-box file-error";
    selectedFileBox.innerHTML = `
      <strong>File:</strong> ${escapeHtml(file.name)}<br>
      <strong>Status:</strong> Failed to read file
    `;

    showMessage("Read Error", "Failed to read the selected file.");
  };

  reader.readAsText(file);
}

function viewDatasetSummary() {
  if (!records.length) {
    showMessage("Error", "No dataset loaded.");
    return;
  }

  const consoles = new Set();
  const genres = new Set();

  let totalSales = 0;
  let totalCriticScore = 0;
  let criticCount = 0;
  let earliestDate = null;
  let latestDate = null;

  for (const record of records) {
    if (record.console) consoles.add(record.console);
    if (record.genre) genres.add(record.genre);

    totalSales += record.totalSales;

    if (record.criticScore > 0) {
      totalCriticScore += record.criticScore;
      criticCount++;
    }

    const formatted = formatDate(record.releaseDate);
    if (formatted) {
      const dateObj = new Date(formatted);
      if (!earliestDate || dateObj < earliestDate) earliestDate = dateObj;
      if (!latestDate || dateObj > latestDate) latestDate = dateObj;
    }
  }

  const averageCritic = criticCount > 0 ? totalCriticScore / criticCount : 0;

  const html = `
    <div class="helper-note">Overview of the uploaded dataset.</div>
    <div class="table-wrap">
      <table class="output-table">
        <thead>
          <tr>
            <th>Metric</th>
            <th>Value</th>
          </tr>
        </thead>
        <tbody>
          <tr><td>Total Records</td><td>${records.length}</td></tr>
          <tr><td>Unique Consoles</td><td>${consoles.size}</td></tr>
          <tr><td>Unique Genres</td><td>${genres.size}</td></tr>
          <tr><td>Total Global Sales</td><td>${totalSales.toFixed(2)} million</td></tr>
          <tr><td>Average Critic Score</td><td>${averageCritic.toFixed(2)}</td></tr>
          <tr><td>Date Range</td><td>${earliestDate && latestDate ? `${earliestDate.toISOString().split("T")[0]} to ${latestDate.toISOString().split("T")[0]}` : "No valid release dates found"}</td></tr>
        </tbody>
      </table>
    </div>
  `;

  showHTML("Dataset Summary", html);
}

function showMonthlySales() {
  if (!records.length) {
    showMessage("Error", "No dataset loaded.");
    return;
  }

  const monthlySales = {};

  for (const record of records) {
    const formatted = formatDate(record.releaseDate);
    if (!formatted) continue;

    const monthKey = formatted.slice(0, 7);
    monthlySales[monthKey] = (monthlySales[monthKey] || 0) + record.totalSales;
  }

  const sortedMonths = Object.keys(monthlySales).sort();

  if (!sortedMonths.length) {
    showMessage("Monthly Sales", "No valid release dates found for monthly analysis.");
    return;
  }

  let rows = "";
  for (const month of sortedMonths) {
    rows += `
      <tr>
        <td>${escapeHtml(month)}</td>
        <td>${monthlySales[month].toFixed(2)}</td>
      </tr>
    `;
  }

  const html = `
    <div class="helper-note">Monthly total sales based on release date.</div>
    <div class="table-wrap">
      <table class="output-table">
        <thead>
          <tr>
            <th>Month</th>
            <th>Total Sales</th>
          </tr>
        </thead>
        <tbody>
          ${rows}
        </tbody>
      </table>
    </div>
  `;

  showHTML("Monthly Sales", html);
}

function showTopCustomers() {
  if (!records.length) {
    showMessage("Error", "No dataset loaded.");
    return;
  }

  const sorted = [...records].sort((a, b) => b.totalSales - a.totalSales);
  const limit = Math.min(10, sorted.length);

  let rows = "";
  for (let i = 0; i < limit; i++) {
    const r = sorted[i];
    rows += `
      <tr>
        <td>${i + 1}</td>
        <td>${escapeHtml(shortenText(r.title, 45))}</td>
        <td>${escapeHtml(r.publisher || "N/A")}</td>
        <td>${r.totalSales.toFixed(2)}</td>
      </tr>
    `;
  }

  const html = `
    <div class="helper-note">
      This dataset has no customer column, so this section displays <strong>Top-Selling Titles</strong> instead.
    </div>
    <div class="table-wrap">
      <table class="output-table">
        <thead>
          <tr>
            <th>Rank</th>
            <th>Title</th>
            <th>Publisher</th>
            <th>Sales</th>
          </tr>
        </thead>
        <tbody>
          ${rows}
        </tbody>
      </table>
    </div>
  `;

  showHTML("Top Customers", html);
}

function showCategoryAnalysis() {
  if (!records.length) {
    showMessage("Error", "No dataset loaded.");
    return;
  }

  const genreCount = {};
  const genreSales = {};
  const genreCriticTotal = {};
  const genreCriticCount = {};

  for (const record of records) {
    const genre = record.genre || "Unknown";

    genreCount[genre] = (genreCount[genre] || 0) + 1;
    genreSales[genre] = (genreSales[genre] || 0) + record.totalSales;

    if (record.criticScore > 0) {
      genreCriticTotal[genre] = (genreCriticTotal[genre] || 0) + record.criticScore;
      genreCriticCount[genre] = (genreCriticCount[genre] || 0) + 1;
    }
  }

  const sortedGenres = Object.keys(genreSales).sort((a, b) => genreSales[b] - genreSales[a]);

  let rows = "";
  for (const genre of sortedGenres) {
    const count = genreCount[genre] || 0;
    const sales = genreSales[genre] || 0;
    const avgCritic = genreCriticCount[genre]
      ? genreCriticTotal[genre] / genreCriticCount[genre]
      : 0;

    rows += `
      <tr>
        <td>${escapeHtml(genre)}</td>
        <td>${count}</td>
        <td>${sales.toFixed(2)}</td>
        <td>${avgCritic.toFixed(2)}</td>
      </tr>
    `;
  }

  const html = `
    <div class="helper-note">Analysis of dataset categories by genre.</div>
    <div class="table-wrap">
      <table class="output-table">
        <thead>
          <tr>
            <th>Genre</th>
            <th>Count</th>
            <th>Total Sales</th>
            <th>Average Critic Score</th>
          </tr>
        </thead>
        <tbody>
          ${rows}
        </tbody>
      </table>
    </div>
  `;

  showHTML("Category Analysis", html);
}

function resetDashboard() {
  records = [];
  csvFile.value = "";
  selectedFileBox.className = "selected-file-box";
  selectedFileBox.textContent = "No file selected yet.";
  setMenuState(false);
  setStatus("No Dataset Loaded", false);
  datasetInfo.textContent = "Waiting for CSV upload...";
  showMessage("Reset Complete", "Dashboard has been reset. Please choose a CSV file again.");
}

function exitProgram() {
  records = [];
  csvFile.value = "";
  selectedFileBox.className = "selected-file-box";
  selectedFileBox.textContent = "No file selected yet.";
  setMenuState(false);
  setStatus("No Dataset Loaded", false);
  datasetInfo.textContent = "Program ended. Choose a CSV file to start again.";
  showMessage("Program Ended", "The dashboard session has ended.");
}