import { createContext, useContext, useMemo, useState } from 'react';
import { apiFetch } from '../api';

const AuthContext = createContext(null);

function getInitialAuth() {
  const saved = localStorage.getItem('authData');
  return saved ? JSON.parse(saved) : null;
}

export function AuthProvider({ children }) {
  const [authData, setAuthData] = useState(getInitialAuth());

  async function login(username, password) {
    const user = await apiFetch('/api/auth/me', {
      auth: { username, password }
    });

    const newAuthData = {
      username,
      password,
      user
    };

    localStorage.setItem('authData', JSON.stringify(newAuthData));
    setAuthData(newAuthData);
  }

  function logout() {
    localStorage.removeItem('authData');
    setAuthData(null);
  }

  const value = useMemo(() => ({
    authData,
    user: authData?.user || null,
    auth: authData ? { username: authData.username, password: authData.password } : null,
    isAuthenticated: !!authData,
    login,
    logout
  }), [authData]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}