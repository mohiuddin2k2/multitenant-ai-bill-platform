import React from 'react';
import { NavLink } from 'react-router-dom';
import { 
  LayoutDashboard, 
  Receipt, 
  FileUp, 
  Sparkles, 
  Network, 
  Gauge,
  Users
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function Sidebar() {
  const { isAdmin } = useAuth();
  const navItems = [
    { to: '/', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/bills', label: 'Bills & Approvals', icon: Receipt },
    { to: '/upload', label: 'AI Bill Ingestion', icon: FileUp },
    { to: '/search', label: 'Natural Language Search', icon: Sparkles },
    { to: '/hierarchy', label: 'Tenant Hierarchy', icon: Network },
    { to: '/quotas', label: 'AI Quotas & Budget', icon: Gauge },
  ];
  if (isAdmin) navItems.push({ to: '/users', label: 'User Management', icon: Users });

  return (
    <aside className="w-64 border-r border-slate-800 bg-slate-900/40 p-4 flex flex-col justify-between shrink-0">
      <div className="space-y-1">
        <div className="px-3 py-2 text-xs font-semibold uppercase tracking-wider text-slate-500">
          Navigation
        </div>
        {navItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.to === '/'}
              className={({ isActive }) =>
                `flex items-center space-x-3 px-3 py-2.5 rounded-xl text-sm font-medium transition-all ${
                  isActive
                    ? 'bg-indigo-600/15 text-indigo-400 border border-indigo-500/30 shadow-sm'
                    : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/60 border border-transparent'
                }`
              }
            >
              <Icon className="w-4 h-4 shrink-0" />
              <span>{item.label}</span>
            </NavLink>
          );
        })}
      </div>

      {/* Isolation Status pill */}
      <div className="p-3 bg-slate-800/50 border border-slate-700/60 rounded-xl text-xs space-y-1 text-slate-400">
        <div className="flex items-center justify-between text-slate-300 font-semibold">
          <span>Tenant Barrier</span>
          <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
        </div>
        {/* <p className="text-[11px] leading-tight text-slate-400">
          Strict SQL & JWT isolation active on all query plans.
        </p> */}
      </div>
    </aside>
  );
}

