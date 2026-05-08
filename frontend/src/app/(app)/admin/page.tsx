"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Users, Brain, Activity, BarChart3, Shield, CheckCircle } from "lucide-react";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip as RechartsTooltip, ResponsiveContainer } from "recharts";
import { motion } from "framer-motion";

export default function AdminPage() {
  const [stats, setStats] = useState<any>(null);
  const [users, setUsers] = useState<any[]>([]);
  const [models, setModels] = useState<any>(null);

  useEffect(() => {
    (async () => {
      try {
        const [s, u, m]: any[] = await Promise.all([
          api.get('/admin/stats'), api.get('/admin/users'), api.get('/admin/model-info').catch(() => ({ success: false }))
        ]);
        if (s.success) setStats(s.data);
        if (u.success) setUsers(u.data);
        if (m.success) setModels(m.data);
      } catch {}
    })();
  }, []);

  if (!stats) return <div className="flex h-[60vh] items-center justify-center"><div className="h-10 w-10 rounded-full border-4 border-primary border-t-transparent animate-spin" /></div>;

  const topCards = [
    { label: "Total Users", value: stats.total_users.toLocaleString(), icon: <Users className="h-5 w-5 text-blue-500" />, bg: "bg-blue-500/10" },
    { label: "Active Sessions", value: stats.active_sessions, icon: <Activity className="h-5 w-5 text-emerald-500" />, bg: "bg-emerald-500/10" },
    { label: "Predictions Today", value: stats.predictions_today, icon: <Brain className="h-5 w-5 text-purple-500" />, bg: "bg-purple-500/10" },
    { label: "System Uptime", value: stats.system_uptime, icon: <CheckCircle className="h-5 w-5 text-cyan-500" />, bg: "bg-cyan-500/10" },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold flex items-center gap-2"><Shield className="h-8 w-8 text-primary" /> Admin Panel</h1>
        <p className="text-muted-foreground">Platform analytics & system management</p>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {topCards.map((c, i) => (
          <motion.div key={i} initial={{ opacity: 0, y: 15 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: i * 0.08 }}>
            <Card className="glass-card">
              <CardContent className="p-4 flex items-center gap-3">
                <div className={`p-2.5 rounded-lg ${c.bg}`}>{c.icon}</div>
                <div><div className="text-xl font-bold">{c.value}</div><div className="text-xs text-muted-foreground">{c.label}</div></div>
              </CardContent>
            </Card>
          </motion.div>
        ))}
      </div>

      <div className="grid lg:grid-cols-2 gap-6">
        {/* Monthly Stats Chart */}
        <Card className="glass-card">
          <CardHeader><CardTitle className="text-lg">Platform Growth</CardTitle></CardHeader>
          <CardContent className="h-[280px]">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={stats.monthly_stats}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="hsl(var(--border))" />
                <XAxis dataKey="month" fontSize={12} tickLine={false} axisLine={false} />
                <YAxis fontSize={12} tickLine={false} axisLine={false} />
                <RechartsTooltip contentStyle={{ backgroundColor: 'hsl(var(--card))', borderRadius: '8px' }} />
                <Bar dataKey="users" fill="#2563EB" radius={[4,4,0,0]} name="Users" />
                <Bar dataKey="predictions" fill="#06B6D4" radius={[4,4,0,0]} name="Predictions" />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Model Performance */}
        <Card className="glass-card">
          <CardHeader><CardTitle className="text-lg">ML Model Performance</CardTitle></CardHeader>
          <CardContent>
            {models?.models ? (
              <div className="space-y-4">
                {Object.entries(models.models).map(([name, data]: any) => (
                  <div key={name} className="p-3 rounded-lg bg-muted/50">
                    <div className="font-medium text-sm capitalize mb-2">{name.replace(/_/g, ' ')}</div>
                    <div className="grid grid-cols-3 gap-2 text-xs">
                      {[['Accuracy', data.metrics.accuracy], ['F1 Score', data.metrics.f1_score], ['ROC AUC', data.metrics.roc_auc]].map(([l, v]) => (
                        <div key={l as string} className="text-center p-2 rounded bg-background">
                          <div className="font-bold text-primary">{((v as number) * 100).toFixed(1)}%</div>
                          <div className="text-muted-foreground">{l}</div>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            ) : <p className="text-sm text-muted-foreground">ML service not connected. Start the ML service to view metrics.</p>}
          </CardContent>
        </Card>
      </div>

      {/* User Management */}
      <Card className="glass-card">
        <CardHeader><CardTitle className="text-lg">User Management</CardTitle></CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead><tr className="border-b">
                <th className="text-left p-3 font-medium text-muted-foreground">Name</th>
                <th className="text-left p-3 font-medium text-muted-foreground">Email</th>
                <th className="text-left p-3 font-medium text-muted-foreground">Role</th>
                <th className="text-left p-3 font-medium text-muted-foreground">Status</th>
                <th className="text-left p-3 font-medium text-muted-foreground">Last Login</th>
              </tr></thead>
              <tbody>
                {users.map((u: any) => (
                  <tr key={u.id} className="border-b last:border-0 hover:bg-muted/50">
                    <td className="p-3 font-medium">{u.name}</td>
                    <td className="p-3 text-muted-foreground">{u.email}</td>
                    <td className="p-3"><span className={`text-xs px-2 py-0.5 rounded-full ${u.role === 'admin' ? 'bg-purple-100 text-purple-700 dark:bg-purple-900/30 dark:text-purple-300' : 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-300'}`}>{u.role}</span></td>
                    <td className="p-3"><span className={`text-xs px-2 py-0.5 rounded-full ${u.status === 'active' ? 'bg-emerald-100 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-300' : 'bg-gray-100 text-gray-700 dark:bg-gray-800 dark:text-gray-300'}`}>{u.status}</span></td>
                    <td className="p-3 text-muted-foreground">{u.lastLogin}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
