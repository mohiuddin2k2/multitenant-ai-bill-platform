import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { Network, Building2, ShieldCheck, ChevronRight, Layers } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function HierarchyView() {
  const [hierarchy, setHierarchy] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user, activeTenantId, switchTenant, isAdmin } = useAuth();

  useEffect(() => {
    api.get('/organizations')
      .then((res) => setHierarchy(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const renderNode = (node, depth = 0) => {
    const isCurrent = node.id === Number(activeTenantId);

    return (
      <div key={node.id} className="space-y-3" style={{ marginLeft: `${depth * 28}px` }}>
        <div
          className={`p-4 rounded-xl border transition-all flex flex-col sm:flex-row sm:items-center justify-between gap-3 ${
            isCurrent
              ? 'bg-indigo-950/40 border-indigo-500/60 shadow-lg shadow-indigo-950/50'
              : 'bg-slate-900/60 border-slate-800 hover:border-slate-700'
          }`}
        >
          <div className="flex items-center space-x-3">
            <div className={`w-9 h-9 rounded-lg flex items-center justify-center font-bold text-sm ${
              isCurrent ? 'bg-indigo-600 text-white' : 'bg-slate-800 text-slate-300'
            }`}>
              {depth === 0 ? 'HQ' : 'SUB'}
            </div>

            <div>
              <div className="flex items-center space-x-2">
                <span className="font-bold text-white text-sm">{node.name}</span>
                <span className="font-mono text-xs px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700">
                  {node.code}
                </span>
                {isCurrent && (
                  <span className="text-[10px] px-2 py-0.5 rounded-full font-bold bg-indigo-500 text-white">
                    Active View
                  </span>
                )}
              </div>
              <div className="text-xs text-slate-400 font-mono mt-0.5">
                Tenant ID: {node.id} • Path: <code className="text-indigo-400">{node.orgPath}</code>
              </div>
            </div>
          </div>

          {isAdmin && !isCurrent && (
            <button
              onClick={() => switchTenant(node.id)}
              className="px-3 py-1.5 rounded-lg text-xs font-semibold bg-slate-800 hover:bg-indigo-600 hover:text-white text-slate-300 border border-slate-700 transition-all shrink-0"
            >
              Switch Scope to #{node.id}
            </button>
          )}
        </div>

        {node.children && node.children.map((child) => renderNode(child, depth + 1))}
      </div>
    );
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          <Network className="w-6 h-6 text-indigo-400" />
          <span>Multi-Tenant Hierarchy Architecture</span>
        </h1>
        <p className="text-slate-400 text-sm">
          Strict tenant isolation enforced at both the security and query plan layers. Sub-organizations can never inspect sibling or parent records.
        </p>
      </div>

      <div className="p-6 bg-slate-900/60 border border-slate-800 rounded-2xl shadow-xl space-y-4">
        <div className="flex items-center justify-between pb-3 border-b border-slate-800 text-xs">
          <span className="text-slate-400 font-semibold uppercase tracking-wider">Tenant Tree Topology</span>
          <span className="text-emerald-400 font-mono flex items-center gap-1">
            <ShieldCheck className="w-4 h-4" /> Materialized Path LTree Indexing
          </span>
        </div>

        {loading ? (
          <div className="py-12 text-center text-slate-400">Loading organization hierarchy...</div>
        ) : (
          <div className="space-y-4 py-2">
            {hierarchy.map((root) => renderNode(root, 0))}
          </div>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-xs text-slate-400">
        <div className="p-4 bg-slate-900/40 border border-slate-800 rounded-xl space-y-1">
          <span className="font-bold text-slate-200">Hierarchical Indexing</span>
          <p>
            The <code>org_path</code> column stores the materialized path (e.g. <code>/1/2/</code>). An indexed <code>LIKE 'prefix%'</code> query allows parent organizations to execute subtree aggregations in <span className="text-indigo-400 font-mono">O(1)</span> time.
          </p>
        </div>
        <div className="p-4 bg-slate-900/40 border border-slate-800 rounded-xl space-y-1">
          <span className="font-bold text-slate-200">Query Layer Isolation</span>
          <p>
            Every SQL plan dynamically joins <code>WHERE tenant_id = :tenantId</code> via JPA specifications and JWT filters, guaranteeing zero cross-tenant leakage.
          </p>
        </div>
      </div>
    </div>
  );
}

