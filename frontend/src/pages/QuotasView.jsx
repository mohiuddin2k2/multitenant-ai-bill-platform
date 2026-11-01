import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { Gauge, ShieldAlert, Zap, DollarSign, Activity, CheckCircle2 } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function QuotasView() {
  const [quota, setQuota] = useState(null);
  const [loading, setLoading] = useState(true);
  const { activeTenantId } = useAuth();

  useEffect(() => {
    fetchQuota();
  }, [activeTenantId]);

  const fetchQuota = () => {
    setLoading(true);
    api.get('/quotas/my-quota')
      .then((res) => setQuota(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  if (loading) {
    return <div className="py-20 text-center text-slate-400">Loading quota diagnostics...</div>;
  }

  if (!quota) return null;

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          <Gauge className="w-6 h-6 text-indigo-400" />
          <span>Tenant AI Budget & Cost Capping</span>
        </h1>
        <p className="text-slate-400 text-sm">
          Strict financial safeguards preventing runaway LLM billing. When the monthly cost threshold is crossed, processing is automatically throttled.
        </p>
      </div>

      {/* Quota Overview Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {/* Cost Cap */}
        <div className="p-5 rounded-2xl bg-slate-900/60 border border-slate-800 flex flex-col justify-between">
          <div className="text-xs text-slate-400 uppercase tracking-wider font-semibold">Monthly Cost Cap</div>
          <div className="text-2xl font-extrabold text-white mt-2">${quota.monthlyCostCapUsd}</div>
          <div className="text-xs text-slate-500 mt-1">Incurred: ${quota.costAccumulatedUsd}</div>
        </div>

        {/* Tokens Consumed */}
        <div className="p-5 rounded-2xl bg-slate-900/60 border border-slate-800 flex flex-col justify-between">
          <div className="text-xs text-slate-400 uppercase tracking-wider font-semibold">Token Consumption</div>
          <div className="text-2xl font-extrabold text-indigo-300 mt-2">
            {new Intl.NumberFormat().format(quota.tokensUsedThisMonth)}
          </div>
          <div className="text-xs text-slate-500 mt-1">
            Limit: {new Intl.NumberFormat().format(quota.monthlyTokenLimit)} tokens
          </div>
        </div>

        {/* Safeguard Status */}
        <div className="p-5 rounded-2xl bg-slate-900/60 border border-slate-800 flex flex-col justify-between">
          <div className="text-xs text-slate-400 uppercase tracking-wider font-semibold">Throttle Protection</div>
          <div className="text-2xl font-extrabold mt-2 flex items-center gap-2">
            {quota.isThrottled ? (
              <span className="text-rose-400 flex items-center gap-1.5 text-base">
                <ShieldAlert className="w-5 h-5" /> Throttled
              </span>
            ) : (
              <span className="text-emerald-400 flex items-center gap-1.5 text-base">
                <CheckCircle2 className="w-5 h-5" /> Operational
              </span>
            )}
          </div>
          <div className="text-xs text-slate-500 mt-1">Cycle resets: {quota.lastResetDate}</div>
        </div>
      </div>

      {/* Budget Meter Bar */}
      <div className="p-6 bg-slate-900/60 border border-slate-800 rounded-2xl space-y-4">
        <div className="flex justify-between items-center text-sm">
          <span className="font-semibold text-slate-200">AI Budget Consumption</span>
          <span className="font-mono text-indigo-300 font-bold">{quota.usagePercent}% used</span>
        </div>

        <div className="w-full h-3 bg-slate-800 rounded-full overflow-hidden">
          <div
            style={{ width: `${Math.min(100, quota.usagePercent)}%` }}
            className={`h-full rounded-full transition-all ${
              quota.usagePercent > 85 ? 'bg-rose-500' : 'bg-indigo-500'
            }`}
          />
        </div>

        <div className="text-xs text-slate-400 leading-relaxed">
          Each bill extraction consumes approximately 500-900 tokens (OCR tokenization + Spring AI JSON serialization). Cost is computed at <code className="text-indigo-300 font-mono">$0.00015 / 1,000 tokens</code>.
        </div>
      </div>

      {/* Rate Limiting Info */}
      <div className="p-6 bg-slate-900/60 border border-slate-800 rounded-2xl flex items-center justify-between gap-4">
        <div className="space-y-1">
          <div className="text-sm font-bold text-white flex items-center gap-2">
            <Activity className="w-4 h-4 text-emerald-400" />
            <span>Token-Bucket API Rate Limiter</span>
          </div>
          <p className="text-xs text-slate-400">
            Enforced at the network boundary using Bucket4j. Each tenant is allocated a burst capacity of 60 requests/min to protect downstream AI queues.
          </p>
        </div>

        <div className="text-right shrink-0">
          <span className="px-3 py-1 rounded-full text-xs font-mono font-semibold bg-emerald-500/15 text-emerald-400 border border-emerald-500/30">
            60 req / min
          </span>
        </div>
      </div>
    </div>
  );
}

