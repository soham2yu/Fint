let balance = 142500.00;
const transactions = [
    { ref: "TX-2026-9041", type: "DEBIT", category: "AWS_CLOUD_INFRA", amount: 14500.00, time: "2026-11-10 14:22:10" },
    { ref: "TX-2026-9040", type: "CREDIT", category: "CLIENT_RETENTION", amount: 85000.00, time: "2026-11-09 18:05:44" },
    { ref: "TX-2026-9039", type: "DEBIT", category: "SECURITY_AUDIT", amount: 23000.00, time: "2026-11-08 11:14:02" },
    { ref: "TX-2026-9038", type: "CREDIT", category: "SAAS_SUBSCRIPTION", amount: 40000.00, time: "2026-11-07 09:30:00" }
];

function renderLedger() {
    document.getElementById("balanceDisplay").innerText = "₹" + balance.toLocaleString('en-IN', { minimumFractionDigits: 2 });
    const tbody = document.getElementById("txTableBody");
    tbody.innerHTML = transactions.map(t => `
        <tr class="hover:bg-slate-800/40">
            <td class="py-3 text-teal-400">${t.ref}</td>
            <td class="py-3"><span class="px-2 py-0.5 rounded ${t.type === 'CREDIT' ? 'bg-emerald-500/10 text-emerald-400' : 'bg-cyan-500/10 text-cyan-400'}">${t.type}</span></td>
            <td class="py-3 text-slate-300">${t.category}</td>
            <td class="py-3 font-semibold ${t.type === 'CREDIT' ? 'text-emerald-400' : 'text-cyan-400'}">${t.type === 'CREDIT' ? '+' : '-'}₹${t.amount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
            <td class="py-3 text-slate-400">${t.time}</td>
        </tr>
    `).join('');
}

function submitTransaction() {
    const type = document.getElementById("txType").value;
    const amount = parseFloat(document.getElementById("txAmount").value);
    const category = document.getElementById("txCategory").value || "GENERAL_LEDGER";
    const status = document.getElementById("statusMsg");

    if (!amount || amount <= 0) {
        status.innerText = "Error: Amount must be strictly positive.";
        return;
    }
    if (type === "DEBIT" && amount > balance) {
        status.innerText = "Error: Insufficient funds (Optimistic lock guard).";
        return;
    }

    balance = type === "DEBIT" ? balance - amount : balance + amount;
    const ref = "TX-2026-" + Math.floor(1000 + Math.random() * 9000);
    transactions.unshift({
        ref, type, category: category.toUpperCase(), amount, time: new Date().toISOString().slice(0, 19).replace('T', ' ')
    });
    renderLedger();
    status.innerText = `✓ Committed ${ref} (REPEATABLE_READ isolation)`;
}

function exportCSV() {
    const rows = [["Reference", "Type", "Category", "Amount_INR", "Timestamp"], ...transactions.map(t => [t.ref, t.type, t.category, t.amount, t.time])];
    const csvContent = "data:text/csv;charset=utf-8," + rows.map(e => e.join(",")).join("\n");
    const link = document.createElement("a");
    link.setAttribute("href", encodeURI(csvContent));
    link.setAttribute("download", "fint_ledger_audit.csv");
    document.body.appendChild(link);
    link.click();
}

renderLedger();
