import React, { useState } from 'react';
import { AlertTriangle, AlertOctagon, Info, ShieldAlert } from 'lucide-react';

export default function AnomalyBadge({ isAnomaly, severity, reasons }) {
  const [showTooltip, setShowTooltip] = useState(false);

  if (!isAnomaly) return null;

  const getStyle = () => {
    switch (severity) {
      case 'CRITICAL':
        return {
          bg: 'bg-rose-500/15 border-rose-500/30 text-rose-400',
          icon: <AlertOctagon className="w-3.5 h-3.5 mr-1" />,
          label: 'Critical Anomaly',
        };
      case 'HIGH':
        return {
          bg: 'bg-amber-500/15 border-amber-500/30 text-amber-400',
          icon: <AlertTriangle className="w-3.5 h-3.5 mr-1" />,
          label: 'High Risk',
        };
      case 'MEDIUM':
        return {
          bg: 'bg-yellow-500/15 border-yellow-500/30 text-yellow-400',
          icon: <Info className="w-3.5 h-3.5 mr-1" />,
          label: 'Medium Risk',
        };
      default:
        return {
          bg: 'bg-blue-500/15 border-blue-500/30 text-blue-400',
          icon: <ShieldAlert className="w-3.5 h-3.5 mr-1" />,
          label: 'Flagged',
        };
    }
  };

  const style = getStyle();

  return (
    <div className="relative inline-block">
      <button
        type="button"
        onClick={() => setShowTooltip(!showTooltip)}
        onMouseEnter={() => setShowTooltip(true)}
        onMouseLeave={() => setShowTooltip(false)}
        className={`inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium border ${style.bg} transition-all hover:scale-105 cursor-pointer`}
      >
        {style.icon}
        {style.label}
      </button>

      {showTooltip && reasons && (
        <div className="absolute z-50 bottom-full left-1/2 -translate-x-1/2 mb-2 w-72 p-3 bg-slate-900 border border-slate-700 rounded-lg shadow-2xl text-xs text-slate-200 pointer-events-none animate-in fade-in zoom-in-95 duration-150">
          <div className="font-semibold text-rose-400 mb-1 flex items-center">
            <ShieldAlert className="w-4 h-4 mr-1 text-rose-400 inline" />
            AI Anomaly Diagnostic:
          </div>
          <p className="leading-relaxed text-slate-300">{reasons}</p>
          <div className="absolute top-full left-1/2 -translate-x-1/2 border-4 border-transparent border-t-slate-700" />
        </div>
      )}
    </div>
  );
}

