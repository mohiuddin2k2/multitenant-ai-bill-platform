import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
import { Building2, LogOut, ShieldCheck, ChevronDown } from 'lucide-react';

export default function Navbar() {
  const { user, logout, activeTenantId, switchTenant, isAdmin } = useAuth();
  const [organizations, setOrganizations] = useState([]);

  useEffect(() => {
    if (isAdmin) {
      api.get('/organizations')
        .then((res) => {
          const flatten = (items) => {
            let res = [];
            for (const item of items) {
              res.push(item);
              if (item.children) {
                res = res.concat(flatten(item.children));
              }
            }
            return res;
          };
          setOrganizations(flatten(res.data));
        })
        .catch(console.error);
    }
  }, [isAdmin]);

  const getRoleBadge = (role) => {
    switch (role) {
      case 'ADMIN':
        return 'bg-emerald-500/20 text-emerald-400 border-emerald-500/30';
      case 'APPROVER':
        return 'bg-amber-500/20 text-amber-400 border-amber-500/30';
      case 'ASSISTANT_OFFICER':
        return 'bg-sky-500/20 text-sky-300 border-sky-500/30';
      case 'HIGHER_OFFICER':
        return 'bg-purple-500/20 text-purple-300 border-purple-500/30';
      case 'CLERK':
        return 'bg-cyan-500/20 text-cyan-400 border-cyan-500/30';
      default:
        return 'bg-slate-500/20 text-slate-400 border-slate-500/30';
    }
  };

  return (
    <header className="h-16 border-b border-slate-800 bg-slate-900/90 backdrop-blur sticky top-0 z-40 px-6 flex items-center justify-between">
      {/* Brand & Organization Indicator */}
      <div className="flex items-center space-x-4">
        <div className="flex items-center space-x-2 text-indigo-400 font-bold text-lg tracking-tight">
          <div className="w-9 h-9 rounded-xl bg-indigo-600/20 border border-indigo-500/30 flex items-center justify-center text-indigo-400 font-extrabold text-xl shadow-inner">
            ⚡
          </div>
          <span className="hidden sm:inline bg-gradient-to-r from-white via-slate-200 to-indigo-300 bg-clip-text text-transparent">
            BillFlow AI
          </span>
        </div>

        <div className="h-6 w-px bg-slate-800 hidden sm:block" />

        {/* Tenant Scope Badge */}
        <div className="flex items-center space-x-2 bg-slate-800/80 border border-slate-700/80 px-3 py-1 rounded-lg text-xs">
          <Building2 className="w-3.5 h-3.5 text-indigo-400" />
          <span className="text-slate-400">Tenant:</span>
          {isAdmin && organizations.length > 0 ? (
            <select
              value={activeTenantId || user?.tenantId}
              onChange={(e) => switchTenant(Number(e.target.value))}
              aria-label="Switch active tenant"
              className="bg-transparent text-slate-100 font-semibold focus:outline-none cursor-pointer pr-1"
            >
              {organizations.map((org) => (
                <option key={org.id} value={org.id} className="bg-slate-900 text-slate-200">
                  {org.name} (#{org.id})
                </option>
              ))}
            </select>
          ) : (
            <span className="text-slate-100 font-semibold">
              {user?.organizationName || `Tenant #${user?.tenantId}`}
            </span>
          )}
        </div>
      </div>

      {/* User Info & Actions */}
      <div className="flex items-center space-x-4">
        <div className="flex items-center space-x-3">
          <div className="text-right hidden md:block">
            <div className="text-sm font-medium text-slate-200">{user?.fullName || user?.username}</div>
            <div className="text-xs text-slate-400">{user?.email}</div>
          </div>

          <span className={`px-2.5 py-0.5 rounded-full text-xs font-semibold border ${getRoleBadge(user?.role)}`}>
            {user?.role}
          </span>
        </div>

        <div className="h-6 w-px bg-slate-800" />

        <button
          onClick={logout}
          title="Sign Out"
          className="p-2 rounded-lg text-slate-400 hover:text-rose-400 hover:bg-rose-500/10 border border-transparent hover:border-rose-500/20 transition-all"
        >
          <LogOut className="w-4 h-4" />
        </button>
      </div>
    </header>
  );
}

