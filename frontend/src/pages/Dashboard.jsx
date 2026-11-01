import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { 
  DollarSign, 
  Clock, 
  CheckCircle2, 
  AlertTriangle, 
  TrendingUp, 
  FileText, 
  ArrowUpRight,
  Sparkles,
  Zap
} from 'lucide-react';
import { Link } from 'react-router-dom';

export default function Dashboard() {
  const { activeTenantId, user } = useAuth();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboard();
  }, [activeTenantId]);

  const fetchDashboard = () => {
    setLoading(true);
    api.get('/dashboard/summary')
      .then((res) => setData(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  const formatCurrency = (val) => {
    if (!val) return '₹0';
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
      maximumFractionDigits: 0,
    }).format(val);
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64 text-slate-400">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-500 mr-3"></div>
        <span>Loading tenant analytics...</span>
      </div>
    );
  }

  if (!data) return null;

  const processedCount = data.approvedBills + data.paidBills + data.rejectedBills;
  const processedPercent = data.totalBills > 0 ? Math.round((processedCount / data.totalBills) * 100) : 0;

  return (
    <div className="space-y-6">
      {/* Header Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-gradient-to-r from-slate-900 via-indigo-950/40 to-slate-900 border border-slate-800 p-6 rounded-2xl">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            <span>{data.tenantName}</span>
            <span className="text-xs px-2.5 py-0.5 rounded-full font-mono bg-indigo-500/20 text-indigo-400 border border-indigo-500/30">
              Tenant #{data.tenantId}
            </span>
          </h1>
          <p className="text-slate-400 text-sm mt-1">
            Real-time pipeline overview, approval workflows, and AI ingestion analytics.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <Link
            to="/upload"
            className="inline-flex items-center space-x-2 px-4 py-2 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl text-sm font-semibold shadow-lg shadow-indigo-600/20 transition-all"
          >
            <Sparkles className="w-4 h-4" />
            <span>Upload New Bill</span>
          </Link>
        </div>
      </div>

      {/* Metric Stat Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Total Spend */}
        <div className="p-5 rounded-2xl bg-slate-900/60 border border-slate-800 flex flex-col justify-between">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold uppercase tracking-wider">Total Invoiced Spend</span>
            <div className="w-8 h-8 rounded-lg bg-indigo-500/10 text-indigo-400 flex items-center justify-center">
              <DollarSign className="w-4 h-4" />
            </div>
          </div>
          <div className="text-2xl font-extrabold text-white tracking-tight">
            {formatCurrency(data.totalSpend)}
          </div>
          <div className="text-xs text-slate-500 mt-2">
            Across <span className="text-slate-300 font-semibold">{data.totalBills}</span> submitted bills
          </div>
        </div>

        {/* Pending Processing */}
        <div className="p-5 rounded-2xl bg-slate-900/60 border border-slate-800 flex flex-col justify-between">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold uppercase tracking-wider">Pending Approvals</span>
            <div className="w-8 h-8 rounded-lg bg-amber-500/10 text-amber-400 flex items-center justify-center">
              <Clock className="w-4 h-4" />
            </div>
          </div>
          <div className="text-2xl font-extrabold text-amber-300 tracking-tight">
            {data.pendingBills + data.inReviewBills}
          </div>
          <div className="text-xs text-slate-500 mt-2">
            Value: <span className="text-amber-400/90 font-semibold">{formatCurrency(data.pendingSpend)}</span>
          </div>
        </div>

        {/* Approved Bills */}
        <div className="p-5 rounded-2xl bg-slate-900/60 border border-slate-800 flex flex-col justify-between">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold uppercase tracking-wider">Approved / Processed</span>
            <div className="w-8 h-8 rounded-lg bg-emerald-500/10 text-emerald-400 flex items-center justify-center">
              <CheckCircle2 className="w-4 h-4" />
            </div>
          </div>
          <div className="text-2xl font-extrabold text-emerald-300 tracking-tight">
            {data.approvedBills + data.paidBills}
          </div>
          <div className="text-xs text-slate-500 mt-2">
            Value: <span className="text-emerald-400/90 font-semibold">{formatCurrency(data.approvedSpend)}</span>
          </div>
        </div>

        {/* AI Anomalies Flagged */}
        <div className="p-5 rounded-2xl bg-slate-900/60 border border-slate-800 flex flex-col justify-between">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold uppercase tracking-wider">Anomalies Detected</span>
            <div className="w-8 h-8 rounded-lg bg-rose-500/10 text-rose-400 flex items-center justify-center">
              <AlertTriangle className="w-4 h-4" />
            </div>
          </div>
          <div className="text-2xl font-extrabold text-rose-400 tracking-tight">
            {data.anomalyCount}
          </div>
          <div className="text-xs text-slate-500 mt-2">
            Duplicates or statistical outliers
          </div>
        </div>
      </div>

      {/* Processed vs Pending Analytics Bar */}
      <div className="p-6 bg-slate-900/60 border border-slate-800 rounded-2xl">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between mb-4">
          <div>
            <h2 className="text-base font-bold text-white">Processed vs. Pending Pipeline</h2>
            <p className="text-xs text-slate-400">Throughput breakdown across the current tenant workflow</p>
          </div>
          <div className="text-xs font-semibold text-slate-300 mt-2 sm:mt-0">
            {processedPercent}% Processed ({processedCount} / {data.totalBills})
          </div>
        </div>

        {/* Multi-segment progress bar */}
        <div className="w-full h-3 bg-slate-800 rounded-full overflow-hidden flex">
          <div 
            style={{ width: `${data.totalBills > 0 ? (data.approvedBills / data.totalBills) * 100 : 0}%` }}
            className="bg-emerald-500 h-full transition-all"
            title={`Approved: ${data.approvedBills}`}
          />
          <div 
            style={{ width: `${data.totalBills > 0 ? (data.paidBills / data.totalBills) * 100 : 0}%` }}
            className="bg-indigo-500 h-full transition-all"
            title={`Paid: ${data.paidBills}`}
          />
          <div 
            style={{ width: `${data.totalBills > 0 ? (data.pendingBills / data.totalBills) * 100 : 0}%` }}
            className="bg-amber-500 h-full transition-all"
            title={`Pending: ${data.pendingBills}`}
          />
          <div 
            style={{ width: `${data.totalBills > 0 ? (data.inReviewBills / data.totalBills) * 100 : 0}%` }}
            className="bg-sky-500 h-full transition-all"
            title={`In Review: ${data.inReviewBills}`}
          />
          <div 
            style={{ width: `${data.totalBills > 0 ? (data.rejectedBills / data.totalBills) * 100 : 0}%` }}
            className="bg-rose-500 h-full transition-all"
            title={`Rejected: ${data.rejectedBills}`}
          />
        </div>

        <div className="flex flex-wrap gap-4 mt-4 text-xs">
          <div className="flex items-center space-x-1.5">
            <span className="w-2.5 h-2.5 rounded-full bg-emerald-500"></span>
            <span className="text-slate-400">Approved ({data.approvedBills})</span>
          </div>
          <div className="flex items-center space-x-1.5">
            <span className="w-2.5 h-2.5 rounded-full bg-indigo-500"></span>
            <span className="text-slate-400">Paid ({data.paidBills})</span>
          </div>
          <div className="flex items-center space-x-1.5">
            <span className="w-2.5 h-2.5 rounded-full bg-amber-500"></span>
            <span className="text-slate-400">Pending ({data.pendingBills})</span>
          </div>
          <div className="flex items-center space-x-1.5">
            <span className="w-2.5 h-2.5 rounded-full bg-sky-500"></span>
            <span className="text-slate-400">In Review ({data.inReviewBills})</span>
          </div>
          <div className="flex items-center space-x-1.5">
            <span className="w-2.5 h-2.5 rounded-full bg-rose-500"></span>
            <span className="text-slate-400">Rejected ({data.rejectedBills})</span>
          </div>
        </div>
      </div>

      {/* Two-Column: Category Breakdown & Top Vendors */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        
        {/* Category Breakdown */}
        <div className="p-6 bg-slate-900/60 border border-slate-800 rounded-2xl">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-base font-bold text-white">Spend by Category</h2>
            <span className="text-xs text-slate-500 font-mono">LIVE AGGREGATE</span>
          </div>

          <div className="space-y-3">
            {data.categoryBreakdown?.map((cat) => (
              <div key={cat.category} className="p-3 bg-slate-800/40 rounded-xl border border-slate-700/50 flex items-center justify-between">
                <div>
                  <div className="text-sm font-semibold text-slate-200">{cat.category}</div>
                  <div className="text-xs text-slate-400">{cat.billCount} invoices submitted</div>
                </div>
                <div className="text-right">
                  <div className="text-sm font-bold text-indigo-300">{formatCurrency(cat.totalAmount)}</div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Top Vendors */}
        <div className="p-6 bg-slate-900/60 border border-slate-800 rounded-2xl">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-base font-bold text-white">Top Vendors by Spend</h2>
            <Link to="/bills" className="text-xs text-indigo-400 hover:text-indigo-300 flex items-center">
              View all <ArrowUpRight className="w-3.5 h-3.5 ml-0.5" />
            </Link>
          </div>

          <div className="space-y-3">
            {data.topVendors?.map((v) => (
              <div key={v.vendorName} className="p-3 bg-slate-800/40 rounded-xl border border-slate-700/50 flex items-center justify-between">
                <div>
                  <div className="text-sm font-semibold text-slate-200">{v.vendorName}</div>
                  <div className="text-xs text-slate-400">{v.billCount} invoices processed</div>
                </div>
                <div className="text-right">
                  <div className="text-sm font-bold text-emerald-300">{formatCurrency(v.totalAmount)}</div>
                </div>
              </div>
            ))}
          </div>
        </div>

      </div>

      {/* Tenant AI Cost Safeguard Banner */}
      <div className="p-6 bg-slate-900/80 border border-indigo-500/30 rounded-2xl flex flex-col md:flex-row items-center justify-between gap-6">
        <div className="flex items-center space-x-4">
          <div className="w-12 h-12 rounded-xl bg-indigo-500/10 border border-indigo-500/20 flex items-center justify-center text-indigo-400 shrink-0">
            <Zap className="w-6 h-6" />
          </div>
          <div>
            <h3 className="text-base font-bold text-white flex items-center gap-2">
              <span>Tenant AI Quota & Budget Safeguard</span>
              <span className="text-xs px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                Active
              </span>
            </h3>
            <p className="text-xs text-slate-400 mt-0.5">
              Cost cap: ${data.monthlyCostCapUsd} / month. Incurred so far: ${data.costAccumulatedUsd}
            </p>
          </div>
        </div>

        <div className="w-full md:w-64">
          <div className="flex justify-between text-xs text-slate-400 mb-1">
            <span>Remaining Budget</span>
            <span className="font-semibold text-indigo-300">{data.quotaRemainingPercent}%</span>
          </div>
          <div className="w-full h-2 bg-slate-800 rounded-full overflow-hidden">
            <div 
              style={{ width: `${data.quotaRemainingPercent}%` }} 
              className="bg-indigo-500 h-full rounded-full transition-all"
            />
          </div>
        </div>
      </div>

    </div>
  );
}

