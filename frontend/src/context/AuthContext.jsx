import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('user');
    return saved ? JSON.parse(saved) : null;
  });
  const [token, setToken] = useState(() => localStorage.getItem('token') || null);
  const [activeTenantId, setActiveTenantId] = useState(() => {
    return localStorage.getItem('tenant_override') || (user ? user.tenantId : null);
  });
  const [loading, setLoading] = useState(false);

  const login = async (username, password) => {
    setLoading(true);
    try {
      const res = await api.post('/auth/login', { username, password });
      const authData = res.data;
      setUser(authData);
      setToken(authData.token);
      setActiveTenantId(authData.tenantId);
      localStorage.setItem('token', authData.token);
      localStorage.setItem('user', JSON.stringify(authData));
      localStorage.removeItem('tenant_override');
      return authData;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    setActiveTenantId(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('tenant_override');
  };

  const switchTenant = (newTenantId) => {
    if (newTenantId === user?.tenantId) {
      localStorage.removeItem('tenant_override');
      setActiveTenantId(user.tenantId);
    } else {
      localStorage.setItem('tenant_override', newTenantId);
      setActiveTenantId(newTenantId);
    }
    // Refresh window or state
    window.location.reload();
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        activeTenantId,
        loading,
        login,
        logout,
        switchTenant,
        isAuthenticated: !!token,
        isAdmin: user?.role === 'ADMIN',
        isApprover: user?.role === 'APPROVER' || user?.role === 'ADMIN',
        isClerk: user?.role === 'CLERK' || user?.role === 'ADMIN',
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

