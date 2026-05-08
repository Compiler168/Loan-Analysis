"use client";

import React, { createContext, useContext, useState, useEffect } from 'react';
import { api } from '@/lib/api';
import { useRouter } from 'next/navigation';

interface User {
  id: string;
  email: string;
  name: string;
  role: string;
  profile?: any;
}

interface AuthContextType {
  user: User | null;
  loading: boolean;
  login: (data: any) => Promise<void>;
  register: (data: any) => Promise<void>;
  logout: () => void;
  updateProfile: (profile: any) => Promise<void>;
}

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => { checkAuth(); }, []);

  const checkAuth = async () => {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        const res: any = await api.get('/auth/me');
        if (res.success) setUser(res.data);
      }
    } catch { localStorage.removeItem('token'); }
    finally { setLoading(false); }
  };

  const login = async (creds: any) => {
    const res: any = await api.post('/auth/login', creds);
    if (res.success) {
      localStorage.setItem('token', res.data.token);
      setUser(res.data.user);
      router.push('/dashboard');
    } else throw new Error(res.error || 'Login failed');
  };

  const register = async (data: any) => {
    const res: any = await api.post('/auth/register', data);
    if (res.success) {
      localStorage.setItem('token', res.data.token);
      setUser(res.data.user);
      router.push('/dashboard');
    } else throw new Error(res.error || 'Registration failed');
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
    router.push('/login');
  };

  const updateProfile = async (profileData: any) => {
    const res: any = await api.put('/auth/profile', profileData);
    if (res.success) setUser(res.data);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout, updateProfile }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
