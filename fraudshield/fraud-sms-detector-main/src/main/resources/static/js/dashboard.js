const API_BASE = '';
const AUTO_REFRESH_INTERVAL_MS = 8000;

let fraudSafeChart = null;
let categoryChart = null;
let confidenceChart = null;
let dailyChart = null;
let topSendersChart = null;
let topUrlsChart = null;

let currentPage = 0;
let pageSize = 10;
let totalPages = 1;
let knownRecordIds = new Set();
let autoRefreshTimer = null;

document.addEventListener('DOMContentLoaded', () => {
  bindEvents();
  refreshAll();
  startAutoRefresh();
});

function bindEvents() {
  document.getElementById('applyFilters').addEventListener('click', () => {
    currentPage = 0;
    loadHistory();
  });

  document.getElementById('clearFilters').addEventListener('click', () => {
    document.getElementById('searchBox').value = '';
    document.getElementById('filterSender').value = '';
    document.getElementById('filterUrl').value = '';
    document.getElementById('filterVerdict').value = '';
    document.getElementById('filterDate').value = '';
    document.getElementById('sortBy').value = 'analyzedAt';
    document.getElementById('sortDir').value = 'desc';
    currentPage = 0;
    loadHistory();
  });

  document.getElementById('searchBox').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
      currentPage = 0;
      loadHistory();
    }
  });

  document.getElementById('refreshBtn').addEventListener('click', () => refreshAll(true));

  document.getElementById('prevPage').addEventListener('click', () => {
    if (currentPage > 0) {
      currentPage--;
      loadHistory();
    }
  });

  document.getElementById('nextPage').addEventListener('click', () => {
    if (currentPage < totalPages - 1) {
      currentPage++;
      loadHistory();
    }
  });

  document.getElementById('exportCsv').addEventListener('click', exportCsv);
}

function startAutoRefresh() {
  if (autoRefreshTimer) clearInterval(autoRefreshTimer);
  autoRefreshTimer = setInterval(() => refreshAll(false), AUTO_REFRESH_INTERVAL_MS);
}

function refreshAll(showToast) {
  loadStats();
  loadHistory();
  loadWhitelist();
  loadBlacklist();
  updateLastUpdated();
  if (showToast) {
    showToastMessage('Dashboard refreshed', 'success');
  }
}

function updateLastUpdated() {
  const now = new Date();
  document.getElementById('lastUpdated').textContent = now.toLocaleTimeString();
}

function showToastMessage(message, type) {
  const container = document.getElementById('toastContainer');
  const toast = document.createElement('div');
  toast.className = `toast-item ${type || ''}`;
  toast.textContent = message;
  container.appendChild(toast);
  setTimeout(() => toast.remove(), 3500);
}

async function fetchJson(url) {
  const res = await fetch(url);
  if (!res.ok) {
    throw new Error('Request failed: ' + res.status);
  }
  const body = await res.json();
  return body.data;
}

function escapeHtml(str) {
  if (str === null || str === undefined) return '';
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');
}

function formatTimestamp(ts) {
  if (!ts) return '-';
  if (Array.isArray(ts)) {
    const [y, m, d, h = 0, min = 0, s = 0] = ts;
    return `${y}-${String(m).padStart(2,'0')}-${String(d).padStart(2,'0')} ${String(h).padStart(2,'0')}:${String(min).padStart(2,'0')}:${String(s).padStart(2,'0')}`;
  }
  return String(ts);
}

function verdictBadge(verdict) {
  const v = (verdict || 'UNKNOWN').toUpperCase();
  let cls = 'badge-unknown';
  if (v === 'FRAUD') cls = 'badge-fraud';
  else if (v === 'SAFE') cls = 'badge-safe';
  else if (v === 'SUSPICIOUS') cls = 'badge-suspicious';
  return `<span class="badge-fs ${cls}">${escapeHtml(v)}</span>`;
}

function actionClass(action) {
  const a = (action || '').toUpperCase();
  if (a === 'BLOCK') return 'badge-action-block';
  if (a === 'ALLOW') return 'badge-action-allow';
  if (a === 'WARN') return 'badge-action-warn';
  return '';
}

function animateValue(el, newValue) {
  el.classList.add('updating');
  setTimeout(() => {
    el.textContent = newValue;
    el.classList.remove('updating');
  }, 120);
}

async function loadStats() {
  try {
    const stats = await fetchJson(`${API_BASE}/dashboard/stats`);
    renderCards(stats);
    renderFraudSafeChart(stats);
    renderCategoryChart(stats);
    renderConfidenceChart(stats);
    renderDailyChart(stats);
    renderTopSendersChart(stats);
    renderTopUrlsChart(stats);
    renderSystemStatus(stats.systemStatus);
  } catch (err) {
    console.error('Failed to load stats', err);
    showToastMessage('Failed to load dashboard stats', 'error');
    renderSystemStatus(null);
  }
}

function renderSystemStatus(status) {
  const mongoEl = document.getElementById('statusMongo');
  const aiEl = document.getElementById('statusAi');
  const latencyEl = document.getElementById('latencyValue');

  if (!status) {
    mongoEl.classList.remove('up');
    mongoEl.classList.add('down');
    aiEl.classList.remove('up');
    aiEl.classList.add('down');
    latencyEl.textContent = '--';
    return;
  }

  mongoEl.classList.toggle('up', status.mongoUp);
  mongoEl.classList.toggle('down', !status.mongoUp);
  aiEl.classList.toggle('up', status.aiServiceReachable);
  aiEl.classList.toggle('down', !status.aiServiceReachable);
  latencyEl.textContent = status.responseTimeMs ?? '--';
}

function renderCards(stats) {
  animateValue(document.getElementById('cardTotal'), stats.totalSmsAnalysed ?? 0);
  animateValue(document.getElementById('cardFraud'), stats.fraudSms ?? 0);
  animateValue(document.getElementById('cardSafe'), stats.safeSms ?? 0);
  animateValue(document.getElementById('cardSuspicious'), stats.suspiciousSms ?? 0);
  animateValue(document.getElementById('cardBlacklist'), stats.blockedByBlacklist ?? 0);
  animateValue(document.getElementById('cardWhitelist'), stats.allowedByWhitelist ?? 0);
  animateValue(document.getElementById('cardAi'), stats.totalAiPredictions ?? 0);
  animateValue(document.getElementById('cardAvgConfidence'), (stats.averageConfidence ?? 0).toFixed(1));
  animateValue(document.getElementById('cardToday'), stats.todaysAnalysisCount ?? 0);
}

function renderFraudSafeChart(stats) {
  const ctx = document.getElementById('fraudSafeChart').getContext('2d');
  const fraud = stats.fraudSms ?? 0;
  const safe = stats.safeSms ?? 0;
  const suspicious = stats.suspiciousSms ?? 0;

  if (fraudSafeChart) fraudSafeChart.destroy();
  fraudSafeChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['Fraud', 'Safe', 'Suspicious'],
      datasets: [{
        data: [fraud, safe, suspicious],
        backgroundColor: ['#ff4d6d', '#00d68f', '#ffb547'],
        borderColor: '#10161f',
        borderWidth: 3
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 600 },
      plugins: { legend: { position: 'bottom', labels: { color: '#8a97ab', font: { size: 11 } } } },
      cutout: '68%'
    }
  });
}

function renderCategoryChart(stats) {
  const ctx = document.getElementById('categoryChart').getContext('2d');
  const dist = stats.categoryDistribution || {};
  const labels = Object.keys(dist);
  const values = Object.values(dist);

  if (categoryChart) categoryChart.destroy();
  categoryChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels.length ? labels : ['No data'],
      datasets: [{
        label: 'Count',
        data: values.length ? values : [0],
        backgroundColor: '#3da9fc',
        borderRadius: 4,
        maxBarThickness: 28
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 600 },
      plugins: { legend: { display: false } },
      scales: {
        x: { ticks: { color: '#8a97ab', font: { size: 10 } }, grid: { display: false } },
        y: { ticks: { color: '#8a97ab', font: { size: 10 } }, grid: { color: '#232c3a' }, beginAtZero: true }
      }
    }
  });
}

function renderConfidenceChart(stats) {
  const ctx = document.getElementById('confidenceChart').getContext('2d');
  const dist = stats.confidenceDistribution || {};
  const order = ['0-29%', '30-49%', '50-69%', '70-89%', '90-100%'];
  const labels = order.filter(k => dist[k] !== undefined);
  const values = labels.map(k => dist[k]);

  if (confidenceChart) confidenceChart.destroy();
  confidenceChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels.length ? labels : ['No data'],
      datasets: [{
        label: 'Predictions',
        data: values.length ? values : [0],
        backgroundColor: '#ffb547',
        borderRadius: 4,
        maxBarThickness: 28
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 600 },
      plugins: { legend: { display: false } },
      scales: {
        x: { ticks: { color: '#8a97ab', font: { size: 10 } }, grid: { display: false } },
        y: { ticks: { color: '#8a97ab', font: { size: 10 } }, grid: { color: '#232c3a' }, beginAtZero: true }
      }
    }
  });
}

function renderDailyChart(stats) {
  const ctx = document.getElementById('dailyChart').getContext('2d');
  const daily = stats.dailyAnalysisCounts || [];
  const labels = daily.map(d => d.date);
  const totals = daily.map(d => d.count);
  const frauds = daily.map(d => d.fraudCount);
  const safes = daily.map(d => d.safeCount);

  if (dailyChart) dailyChart.destroy();
  dailyChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels.length ? labels : ['No data'],
      datasets: [
        { label: 'Total', data: totals.length ? totals : [0], borderColor: '#3da9fc', backgroundColor: 'rgba(61,169,252,0.08)', tension: 0.35, fill: true, pointRadius: 2 },
        { label: 'Fraud', data: frauds.length ? frauds : [0], borderColor: '#ff4d6d', backgroundColor: 'transparent', tension: 0.35, pointRadius: 2 },
        { label: 'Safe', data: safes.length ? safes : [0], borderColor: '#00d68f', backgroundColor: 'transparent', tension: 0.35, pointRadius: 2 }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 600 },
      plugins: { legend: { position: 'bottom', labels: { color: '#8a97ab', font: { size: 11 } } } },
      scales: {
        x: { ticks: { color: '#8a97ab', font: { size: 10 } }, grid: { display: false } },
        y: { ticks: { color: '#8a97ab', font: { size: 10 } }, grid: { color: '#232c3a' }, beginAtZero: true }
      }
    }
  });
}

function renderTopSendersChart(stats) {
  const ctx = document.getElementById('topSendersChart').getContext('2d');
  const top = stats.topFraudSenders || [];
  const labels = top.map(t => t.label);
  const values = top.map(t => t.count);

  if (topSendersChart) topSendersChart.destroy();
  topSendersChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels.length ? labels : ['No data'],
      datasets: [{ label: 'Fraud Count', data: values.length ? values : [0], backgroundColor: '#ff4d6d', borderRadius: 4 }]
    },
    options: {
      indexAxis: 'y',
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 600 },
      plugins: { legend: { display: false } },
      scales: {
        x: { ticks: { color: '#8a97ab', font: { size: 10 } }, grid: { color: '#232c3a' }, beginAtZero: true },
        y: { ticks: { color: '#8a97ab', font: { size: 10 } }, grid: { display: false } }
      }
    }
  });
}

function renderTopUrlsChart(stats) {
  const ctx = document.getElementById('topUrlsChart').getContext('2d');
  const top = stats.topFraudUrls || [];
  const labels = top.map(t => t.label.length > 22 ? t.label.slice(0, 22) + '…' : t.label);
  const values = top.map(t => t.count);

  if (topUrlsChart) topUrlsChart.destroy();
  topUrlsChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels.length ? labels : ['No data'],
      datasets: [{ label: 'Fraud Count', data: values.length ? values : [0], backgroundColor: '#ffb547', borderRadius: 4 }]
    },
    options: {
      indexAxis: 'y',
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 600 },
      plugins: { legend: { display: false } },
      scales: {
        x: { ticks: { color: '#8a97ab', font: { size: 10 } }, grid: { color: '#232c3a' }, beginAtZero: true },
        y: { ticks: { color: '#8a97ab', font: { size: 10 } }, grid: { display: false } }
      }
    }
  });
}

async function loadHistory() {
  const sender = document.getElementById('filterSender').value.trim();
  const verdict = document.getElementById('filterVerdict').value.trim();
  const url = document.getElementById('filterUrl').value.trim();
  const date = document.getElementById('filterDate').value.trim();
  const search = document.getElementById('searchBox').value.trim();
  const sortBy = document.getElementById('sortBy').value;
  const sortDir = document.getElementById('sortDir').value;

  const params = new URLSearchParams();
  if (sender) params.append('sender', sender);
  if (verdict) params.append('verdict', verdict);
  if (url) params.append('url', url);
  if (date) params.append('date', date);
  if (search) params.append('search', search);
  params.append('page', currentPage);
  params.append('size', pageSize);
  params.append('sortBy', sortBy);
  params.append('sortDir', sortDir);

  const tbody = document.getElementById('historyTableBody');

  try {
    const data = await fetchJson(`${API_BASE}/dashboard/history?${params.toString()}`);
    const records = data.records || [];
    totalPages = data.totalPages || 1;

    if (records.length === 0) {
      tbody.innerHTML = `<tr><td colspan="8" class="empty-row">No matching SMS records found</td></tr>`;
    } else {
      tbody.innerHTML = records.map(r => {
        const recordId = r.id || (r.sender + r.analyzedAt);
        const isNew = !knownRecordIds.has(recordId);
        knownRecordIds.add(recordId);
        return `
        <tr class="${isNew ? 'row-new' : ''}">
          <td>${escapeHtml(r.sender)}</td>
          <td class="msg-cell" title="${escapeHtml(r.message)}">${escapeHtml(r.message)}</td>
          <td class="url-cell" title="${escapeHtml(r.primaryUrl)}">${escapeHtml(r.primaryUrl || '-')}</td>
          <td>${verdictBadge(r.prediction)}</td>
          <td>${(r.confidence ?? 0).toFixed(1)}%</td>
          <td>${escapeHtml(r.category || '-')}</td>
          <td><span class="${actionClass(r.action)}">${escapeHtml(r.action || '-')}</span></td>
          <td>${formatTimestamp(r.analyzedAt)}</td>
        </tr>`;
      }).join('');
    }

    document.getElementById('paginationInfo').textContent =
      `Showing ${records.length} of ${data.totalRecords} records`;
    document.getElementById('pageIndicator').textContent = `${data.currentPage + 1} / ${data.totalPages}`;
    document.getElementById('prevPage').disabled = data.currentPage <= 0;
    document.getElementById('nextPage').disabled = data.currentPage >= data.totalPages - 1;

  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="8" class="empty-row">Failed to load records</td></tr>`;
    console.error(err);
    showToastMessage('Failed to load SMS history', 'error');
  }
}

async function loadWhitelist() {
  const tbody = document.getElementById('whitelistTableBody');
  try {
    const data = await fetchJson(`${API_BASE}/dashboard/whitelist`);
    if (!data || data.length === 0) {
      tbody.innerHTML = `<tr><td colspan="4" class="empty-row">No whitelisted entries found</td></tr>`;
      return;
    }
    tbody.innerHTML = data.map(w => `
      <tr>
        <td>${escapeHtml(w.sender)}</td>
        <td>${escapeHtml(w.url)}</td>
        <td>${escapeHtml(w.type)}</td>
        <td>${escapeHtml(w.createdDate)}</td>
      </tr>
    `).join('');
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="4" class="empty-row">Failed to load whitelist</td></tr>`;
    console.error(err);
  }
}

async function loadBlacklist() {
  const tbody = document.getElementById('blacklistTableBody');
  try {
    const data = await fetchJson(`${API_BASE}/dashboard/blacklist`);
    if (!data || data.length === 0) {
      tbody.innerHTML = `<tr><td colspan="4" class="empty-row">No blacklisted entries found</td></tr>`;
      return;
    }
    tbody.innerHTML = data.map(b => `
      <tr>
        <td>${escapeHtml(b.sender)}</td>
        <td>${escapeHtml(b.url)}</td>
        <td>${escapeHtml(b.type)}</td>
        <td>${escapeHtml(b.createdDate)}</td>
      </tr>
    `).join('');
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="4" class="empty-row">Failed to load blacklist</td></tr>`;
    console.error(err);
  }
}

function exportCsv() {
  const rows = document.querySelectorAll('#historyTableBody tr');
  if (rows.length === 0 || rows[0].classList.contains('empty-row')) {
    showToastMessage('No data to export', 'error');
    return;
  }

  const headers = ['Sender', 'Message', 'URL', 'Verdict', 'Confidence', 'Category', 'Action', 'Timestamp'];
  const csvRows = [headers.join(',')];

  rows.forEach(row => {
    const cells = row.querySelectorAll('td');
    if (cells.length < 8) return;
    const values = Array.from(cells).map(cell => {
      let text = cell.textContent.trim().replace(/"/g, '""');
      return `"${text}"`;
    });
    csvRows.push(values.join(','));
  });

  const csvContent = csvRows.join('\n');
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
  const link = document.createElement('a');
  const url = URL.createObjectURL(blob);
  link.setAttribute('href', url);
  link.setAttribute('download', `fraudshield_sms_export_${new Date().toISOString().slice(0,10)}.csv`);
  link.style.visibility = 'hidden';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  showToastMessage('CSV exported successfully', 'success');
}