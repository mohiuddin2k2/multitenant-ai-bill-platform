import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { UserPlus, Users } from 'lucide-react';

const initialForm = {
  fullName: '', username: '', email: '', password: '', role: 'CLERK'
};

export default function UserManagement() {
  const { activeTenantId } = useAuth();
  const [users, setUsers] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const loadUsers = () => api.get('/auth/users').then((res) => setUsers(res.data)).catch((err) => setError(err.response?.data?.message || 'Could not load users'));
  useEffect(() => { loadUsers(); }, [activeTenantId]);

  const submit = async (event) => {
    event.preventDefault();
    setSaving(true);
    setMessage('');
    setError('');
    try {
      await api.post('/auth/register', { ...form, tenantId: Number(activeTenantId) });
      setForm(initialForm);
      setMessage('User created successfully. Share their sign-in details securely.');
      await loadUsers();
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create user');
    } finally {
      setSaving(false);
    }
  };

  const field = (key, label, type = 'text') => (
    <label className="block text-xs text-slate-300">
      {label}
      <input required type={type} minLength={type === 'password' ? 6 : undefined}
        value={form[key]} onChange={(e) => setForm({ ...form, [key]: e.target.value })}
        className="mt-1 w-full rounded-lg border border-slate-700 bg-slate-800 px-3 py-2 text-sm text-white focus:border-indigo-500 focus:outline-none" />
    </label>
  );

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white">User Management</h1>
        <p className="text-sm text-slate-400">Create workflow users for the active organization.</p>
      </div>

      {message && <div className="rounded-lg border border-emerald-500/30 bg-emerald-500/10 p-3 text-sm text-emerald-300">{message}</div>}
      {error && <div className="rounded-lg border border-rose-500/30 bg-rose-500/10 p-3 text-sm text-rose-300">{error}</div>}

      <form onSubmit={submit} className="grid gap-4 rounded-2xl border border-slate-800 bg-slate-900/60 p-5 md:grid-cols-2">
        {field('fullName', 'Full name')}
        {field('username', 'Username')}
        {field('email', 'Email', 'email')}
        {field('password', 'Temporary password', 'password')}
        <label className="block text-xs text-slate-300">Role
          <select value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}
            className="mt-1 w-full rounded-lg border border-slate-700 bg-slate-800 px-3 py-2 text-sm text-white">
            <option value="CLERK">Clerk</option>
            <option value="ASSISTANT_OFFICER">Assistant Officer</option>
            <option value="HIGHER_OFFICER">Higher Officer</option>
          </select>
        </label>
        <div className="flex items-end">
          <button disabled={saving} className="flex w-full items-center justify-center gap-2 rounded-lg bg-indigo-600 px-4 py-2.5 text-sm font-semibold text-white hover:bg-indigo-500 disabled:opacity-50">
            <UserPlus className="h-4 w-4" />{saving ? 'Creating…' : 'Create user'}
          </button>
        </div>
      </form>

      <section className="overflow-hidden rounded-2xl border border-slate-800 bg-slate-900/60">
        <div className="flex items-center gap-2 border-b border-slate-800 px-5 py-4 text-sm font-semibold text-white"><Users className="h-4 w-4 text-indigo-400" />Organization users</div>
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-900 text-xs uppercase text-slate-500"><tr><th className="px-5 py-3">Name</th><th className="px-5 py-3">Email</th><th className="px-5 py-3">Role</th><th className="px-5 py-3">Status</th></tr></thead>
            <tbody className="divide-y divide-slate-800">{users.map((user) => <tr key={user.id}><td className="px-5 py-3">{user.fullName}<div className="text-xs text-slate-500">{user.username}</div></td><td className="px-5 py-3">{user.email}</td><td className="px-5 py-3">{user.role.replaceAll('_', ' ')}</td><td className="px-5 py-3">{user.enabled ? 'Active' : 'Disabled'}</td></tr>)}</tbody>
          </table>
          {users.length === 0 && <p className="p-5 text-sm text-slate-500">No users in this organization yet.</p>}
        </div>
      </section>
    </div>
  );
}
