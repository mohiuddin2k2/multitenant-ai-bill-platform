import React, { useState } from 'react';
import api from '../services/api';
import { Sparkles, Search, ShieldCheck, ArrowRight, CornerDownRight, CheckCircle2 } from 'lucide-react';
import AnomalyBadge from '../components/AnomalyBadge';

export default function NaturalLanguageSearch() {
  const [prompt, setPrompt] = useState('pending bills over 50,000 this month');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const sampleQueries = [
    'pending bills over 50,000 this month',
    'utilities bills from Reliance Power',
    'all anomaly bills above 1 lakh',
    'approved bills from Tata Consultancy Services',
    'rejected bills with duplicates',
  ];

  const handleSearch = async (queryText) => {
    const q = queryText || prompt;
    if (!q.trim()) return;
    setLoading(true);
    try {
      const res = await api.post('/bills/search/natural-language', { prompt: q });
      setResult(res.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Search query failed');
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (val) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
      maximumFractionDigits: 0,
    }).format(val || 0);
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Title */}
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          <Sparkles className="w-6 h-6 text-indigo-400" />
          <span>Natural Language Query Engine</span>
        </h1>
        <p className="text-slate-400 text-sm">
          Ask questions in plain English. Converted via Spring AI to safe, parameterized JPA Criteria specifications with strict tenant boundaries.
        </p>
      </div>

      {/* Query Bar */}
      <div className="p-4 bg-slate-900 border border-slate-800 rounded-2xl shadow-xl space-y-3">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSearch();
          }}
          className="flex items-center gap-2"
        >
          <div className="relative flex-1">
            <Search className="w-5 h-5 text-slate-500 absolute left-3.5 top-3" />
            <input
              type="text"
              value={prompt}
              onChange={(e) => setPrompt(e.target.value)}
              placeholder="e.g. pending bills over 50,000 this month..."
              className="w-full bg-slate-800/80 border border-slate-700/80 rounded-xl pl-11 pr-4 py-2.5 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:border-indigo-500"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="px-5 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl text-sm font-semibold shadow-lg shadow-indigo-600/20 flex items-center space-x-2 transition-all shrink-0"
          >
            <span>{loading ? 'Translating...' : 'Search'}</span>
            <ArrowRight className="w-4 h-4" />
          </button>
        </form>

        {/* Suggestion Chips */}
        <div className="flex flex-wrap items-center gap-2 pt-2 border-t border-slate-800 text-xs">
          <span className="text-slate-500">Try asking:</span>
          {sampleQueries.map((q) => (
            <button
              key={q}
              type="button"
              onClick={() => {
                setPrompt(q);
                handleSearch(q);
              }}
              className="px-2.5 py-1 bg-slate-800/60 hover:bg-indigo-900/30 text-slate-300 hover:text-indigo-300 rounded-lg border border-slate-700/60 transition-all text-[11px]"
            >
              "{q}"
            </button>
          ))}
        </div>
      </div>

      {/* Query Diagnostic & Interpretation Card */}
      {result && (
        <div className="space-y-4">
          <div className="p-4 bg-indigo-950/30 border border-indigo-500/30 rounded-xl space-y-2">
            <div className="flex items-center justify-between text-xs">
              <span className="font-semibold text-indigo-400 flex items-center gap-1.5">
                <ShieldCheck className="w-4 h-4 text-emerald-400" />
                Parameterized JPA Specification Translation:
              </span>
              <span className="font-mono text-slate-400">Total Matches: {result.totalMatches}</span>
            </div>
            <p className="text-sm font-medium text-slate-200">{result.explanation}</p>
            <div className="text-[11px] font-mono text-slate-400 bg-slate-900/70 p-2 rounded border border-slate-800">
              Query Predicates: tenantId = activeTenantId, {JSON.stringify(result.parsedCriteria)}
            </div>
          </div>

          {/* Results List */}
          <div className="bg-slate-900/60 border border-slate-800 rounded-2xl overflow-hidden shadow-xl">
            {result.bills?.length === 0 ? (
              <div className="p-8 text-center text-slate-400 text-sm">
                No bills matched the query criteria in your tenant scope.
              </div>
            ) : (
              <div className="divide-y divide-slate-800">
                {result.bills.map((bill) => (
                  <div key={bill.id} className="p-4 hover:bg-slate-800/40 transition-colors flex flex-col sm:flex-row sm:items-center justify-between gap-3">
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="font-mono font-bold text-white text-sm">{bill.invoiceNumber}</span>
                        <span className="text-xs px-2 py-0.5 rounded-full bg-slate-800 border border-slate-700 text-slate-300">
                          {bill.status}
                        </span>
                        {bill.isAnomaly && (
                          <AnomalyBadge
                            isAnomaly={bill.isAnomaly}
                            severity={bill.anomalySeverity}
                            reasons={bill.anomalyReasons}
                          />
                        )}
                      </div>
                      <div className="text-xs text-slate-400 mt-1">
                        Vendor: <span className="text-slate-200 font-semibold">{bill.vendorName}</span> • Category: {bill.category} • Date: {bill.billDate}
                      </div>
                    </div>

                    <div className="text-right shrink-0">
                      <div className="text-base font-extrabold text-white font-mono">
                        {formatCurrency(bill.totalAmount)}
                      </div>
                      <div className="text-[11px] text-slate-500">
                        Confidence: {bill.aiConfidenceScore || 99}%
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

