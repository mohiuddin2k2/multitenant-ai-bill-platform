import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { ShieldCheck, UserCheck, ArrowRight, Lock, Mail, Eye, EyeOff } from 'lucide-react';

export default function Login() {
  const showDemoLogins = import.meta.env.VITE_DEMO_MODE !== 'false';
  const [username, setUsername] = useState('admin@shaksham.com');
  const [password, setPassword] = useState(showDemoLogins ? 'Password@123' : '');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const { login, loading } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    if (e) e.preventDefault();
    setError('');
    try {
      await login(username, password);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid username or password');
    }
  };

  const handleQuickLogin = async (userEmail) => {
    setUsername(userEmail);
    setPassword('Password@123');
    try {
      await login(userEmail, 'Password@123');
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Quick login failed');
    }
  };

  const personas = [
    {
      role: 'North Approver',
      email: 'approver_north@shaksham.com',
      badge: 'Tenant 2 • APPROVER',
      desc: 'Can review, approve, reject North Division bills',
      color: 'border-amber-500/40 hover:bg-amber-500/10 text-amber-300'
    },
    {
      role: 'North Clerk',
      email: 'clerk_north@shaksham.com',
      badge: 'Tenant 2 • CLERK',
      desc: 'Can upload bills and view drafts / pending bills',
      color: 'border-cyan-500/40 hover:bg-cyan-500/10 text-cyan-300'
    },
    {
      role: 'South Approver',
      email: 'approver_south@shaksham.com',
      badge: 'Tenant 3 • APPROVER',
      desc: 'Approves South Operations bills (strictly isolated)',
      color: 'border-purple-500/40 hover:bg-purple-500/10 text-purple-300'
    },
    {
      role: 'South Clerk',
      email: 'clerk_south@shaksham.com',
      badge: 'Tenant 3 • CLERK',
      desc: 'Uploads South bills (cannot see North data)',
      color: 'border-blue-500/40 hover:bg-blue-500/10 text-blue-300'
    },
    {
      role: 'HQ Global Admin',
      email: 'admin@shaksham.com',
      badge: 'Tenant 1 • ADMIN',
      desc: 'Full hierarchical visibility, quota controls & override',
      color: 'border-emerald-500/40 hover:bg-emerald-500/10 text-emerald-300'
    }
  ];

  return (
    <div className="min-h-screen flex items-center justify-center p-6 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-indigo-950 via-slate-950 to-black">
        <div className={`w-full ${showDemoLogins ? 'max-w-4xl grid md:grid-cols-2 gap-8' : 'max-w-xl'} bg-slate-900/80 border border-slate-800 rounded-2xl p-8 backdrop-blur shadow-2xl`}>
        
        {/* Left Column: Direct Login */}
        <div className="flex flex-col justify-between">
          <div>
            <div className="flex items-center space-x-2 text-indigo-400 font-bold text-2xl mb-2">
              <span className="w-10 h-10 rounded-xl bg-indigo-600/30 border border-indigo-500/40 flex items-center justify-center text-xl">⚡</span>
              <span>BillFlow AI</span>
            </div>
            <p className="text-slate-400 text-sm mb-6">
              Enterprise Multi-Tenant AI Bill Processing Platform with strict hierarchy and automated anomaly detection.
            </p>

            {error && (
              <div className="p-3 mb-4 rounded-lg bg-rose-500/15 border border-rose-500/30 text-rose-300 text-xs font-medium">
                {error}
              </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">Username or Email</label>
                <div className="relative">
                  <Mail className="w-4 h-4 text-slate-500 absolute left-3 top-3" />
                  <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                    className="w-full bg-slate-800/80 border border-slate-700 rounded-lg pl-9 pr-3 py-2 text-sm text-slate-200 focus:outline-none focus:border-indigo-500 transition-colors"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">Password</label>
                <div className="relative">
                  <Lock className="w-4 h-4 text-slate-500 absolute left-3 top-3" />
                  <input
                    type={showPassword ? 'text' : 'password'}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    className="w-full bg-slate-800/80 border border-slate-700 rounded-lg pl-9 pr-10 py-2 text-sm text-slate-200 focus:outline-none focus:border-indigo-500 transition-colors"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword((visible) => !visible)}
                    aria-label={showPassword ? 'Hide password' : 'Show password'}
                    aria-pressed={showPassword}
                    className="absolute right-3 top-2.5 text-slate-500 hover:text-slate-200 focus:outline-none focus:text-indigo-400 transition-colors"
                  >
                    {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                  </button>
                </div>
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full mt-2 py-2.5 px-4 bg-indigo-600 hover:bg-indigo-500 text-white font-medium text-sm rounded-lg shadow-lg shadow-indigo-600/20 flex items-center justify-center space-x-2 transition-all disabled:opacity-50"
              >
                <span>{loading ? 'Authenticating...' : 'Sign In'}</span>
                <ArrowRight className="w-4 h-4" />
              </button>
            </form>
          </div>

          <div className="mt-6 pt-4 border-t border-slate-800 text-xs text-slate-500 flex items-center space-x-1">
            <ShieldCheck className="w-4 h-4 text-emerald-400 shrink-0" />
            {/* <span>JWT Multi-Tenant RBAC Security Active</span> */}
          </div>
        </div>

        {/* Right Column: 1-Click Persona Simulator */}
        {showDemoLogins && <div className="border-t md:border-t-0 md:border-l border-slate-800 pt-6 md:pt-0 md:pl-8 flex flex-col justify-between">
          <div>
            <div className="flex items-center space-x-2 text-slate-200 font-semibold text-sm mb-1">
              <UserCheck className="w-4 h-4 text-indigo-400" />
              <span>Instant Persona Login (1-Click)</span>
            </div>
            <p className="text-slate-400 text-xs mb-4">
              Switch roles to experience tenant boundary enforcement and approval workflow permissions:
            </p>

            <div className="space-y-2.5">
              {personas.map((p) => (
                <button
                  key={p.email}
                  type="button"
                  onClick={() => handleQuickLogin(p.email)}
                  className={`w-full text-left p-3 rounded-xl border bg-slate-800/40 transition-all ${p.color} flex flex-col`}
                >
                  <div className="flex items-center justify-between">
                    <span className="font-semibold text-sm">{p.role}</span>
                    <span className="text-[10px] px-2 py-0.5 rounded font-mono uppercase bg-slate-900/80 border border-slate-700">
                      {p.badge}
                    </span>
                  </div>
                  <span className="text-xs text-slate-400 mt-1">{p.desc}</span>
                </button>
              ))}
            </div>
          </div>

          {/* <div className="mt-4 text-[11px] text-slate-500 font-mono text-center">
            Default credentials for all: <code className="text-indigo-300">Password@123</code>
          </div> */}
        </div>}

      </div>
    </div>
  );
}

