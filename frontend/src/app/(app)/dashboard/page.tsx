"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip as RechartsTooltip, ResponsiveContainer,
  BarChart, Bar, RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, Radar, LineChart, Line
} from 'recharts';
import { Brain, TrendingUp, AlertTriangle, Wallet, CreditCard, Activity, Target, Clock } from "lucide-react";
import { useAuth } from "@/contexts/AuthContext";
import { motion } from "framer-motion";

export default function DashboardPage() {
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    (async () => {
      try {
        const res: any = await api.get('/financial/dashboard');
        if (res.success) setData(res.data);
      } catch (e) { console.error(e); }
      finally { setLoading(false); }
    })();
  }, []);

  if (loading) return (
    <div className="flex h-[80vh] items-center justify-center">
      <div className="flex flex-col items-center gap-4">
        <div className="h-12 w-12 rounded-full border-4 border-primary border-t-transparent animate-spin" />
        <p className="text-muted-foreground animate-pulse">Running AI financial analysis...</p>
      </div>
    </div>
  );

  if (!data) return <div className="text-center py-20 text-muted-foreground">Failed to load dashboard</div>;

  const stats = [
    { title: "Loan Approval", value: `${data.loan_probability}%`, icon: <Target className="h-5 w-5 text-blue-500" />, trend: "+2.5% from last month", bg: "bg-blue-500/10" },
    { title: "Health Score", value: `${data.health_score}/100`, icon: <Activity className="h-5 w-5 text-emerald-500" />, trend: "Grade: B+", bg: "bg-emerald-500/10" },
    { title: "Risk Level", value: data.risk_level.charAt(0).toUpperCase() + data.risk_level.slice(1), icon: <AlertTriangle className="h-5 w-5 text-amber-500" />, trend: "Maintain habits", bg: "bg-amber-500/10" },
    { title: "Credit Score", value: data.credit_score, icon: <CreditCard className="h-5 w-5 text-purple-500" />, trend: "+15 this quarter", bg: "bg-purple-500/10" },
    { title: "Monthly Savings", value: `$${data.monthly_savings.toLocaleString()}`, icon: <Wallet className="h-5 w-5 text-cyan-500" />, trend: "23% savings rate", bg: "bg-cyan-500/10" },
    { title: "Debt-to-Income", value: `${(data.dti_ratio * 100).toFixed(1)}%`, icon: <TrendingUp className="h-5 w-5 text-rose-500" />, trend: "-2% since Jan", bg: "bg-rose-500/10" },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">AI Financial Dashboard</h1>
        <p className="text-muted-foreground">Welcome back, {user?.name}. Your intelligent overview.</p>
      </div>

      {/* Stats Grid */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {stats.map((s, i) => (
          <motion.div key={i} initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: i * 0.08 }}>
            <Card className="glass-card card-hover">
              <CardHeader className="flex flex-row items-center justify-between pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">{s.title}</CardTitle>
                <div className={`p-2 rounded-lg ${s.bg}`}>{s.icon}</div>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{s.value}</div>
                <p className="text-xs text-muted-foreground mt-1">{s.trend}</p>
              </CardContent>
            </Card>
          </motion.div>
        ))}
      </div>

      {/* Main Charts */}
      <div className="grid gap-4 lg:grid-cols-7">
        <Card className="lg:col-span-4 glass-card">
          <CardHeader><CardTitle className="text-lg">Financial Growth Projection</CardTitle></CardHeader>
          <CardContent className="h-[300px]">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={data.financial_growth}>
                <defs>
                  <linearGradient id="gSavings" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#2563EB" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="#2563EB" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="hsl(var(--border))" />
                <XAxis dataKey="month" stroke="hsl(var(--muted-foreground))" fontSize={12} tickLine={false} axisLine={false} />
                <YAxis stroke="hsl(var(--muted-foreground))" fontSize={12} tickLine={false} axisLine={false} tickFormatter={v => `$${v/1000}k`} />
                <RechartsTooltip contentStyle={{ backgroundColor: 'hsl(var(--card))', borderColor: 'hsl(var(--border))', borderRadius: '8px' }} />
                <Area type="monotone" dataKey="net_worth" stroke="#2563EB" strokeWidth={3} fillOpacity={1} fill="url(#gSavings)" />
              </AreaChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* AI Insights */}
        <Card className="lg:col-span-3 bg-gradient-to-br from-blue-600/5 to-cyan-500/5 border-primary/20 relative overflow-hidden">
          <div className="absolute top-0 right-0 p-4 opacity-10"><Brain className="h-32 w-32" /></div>
          <CardHeader>
            <div className="flex items-center gap-2"><Brain className="h-5 w-5 text-primary" /><CardTitle className="text-lg">AI Smart Insights</CardTitle></div>
          </CardHeader>
          <CardContent className="space-y-3">
            {data.insights.map((ins: any, i: number) => (
              <motion.div key={i} initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} transition={{ delay: 0.5 + i * 0.1 }}
                className="flex gap-3 p-3 rounded-lg bg-background/50 border backdrop-blur-sm shadow-sm card-hover">
                <div className="text-xl">{ins.icon}</div>
                <div>
                  <h4 className="text-sm font-semibold">{ins.title}</h4>
                  <p className="text-xs text-muted-foreground mt-0.5 leading-snug">{ins.message}</p>
                </div>
              </motion.div>
            ))}
          </CardContent>
        </Card>
      </div>

      {/* Secondary Charts */}
      <div className="grid gap-4 lg:grid-cols-2">
        <Card className="glass-card">
          <CardHeader><CardTitle className="text-lg">Income vs Expenses</CardTitle></CardHeader>
          <CardContent className="h-[260px]">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={data.income_vs_expenses} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="hsl(var(--border))" />
                <XAxis dataKey="month" stroke="hsl(var(--muted-foreground))" fontSize={12} tickLine={false} axisLine={false} />
                <YAxis stroke="hsl(var(--muted-foreground))" fontSize={12} tickLine={false} axisLine={false} tickFormatter={v => `$${v/1000}k`} />
                <RechartsTooltip cursor={{fill: 'hsl(var(--muted))'}} contentStyle={{ backgroundColor: 'hsl(var(--card))', borderRadius: '8px' }} />
                <Bar dataKey="income" fill="#22C55E" radius={[4,4,0,0]} />
                <Bar dataKey="expenses" fill="#EF4444" radius={[4,4,0,0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card className="glass-card">
          <CardHeader><CardTitle className="text-lg">Risk Radar Analysis</CardTitle></CardHeader>
          <CardContent className="h-[260px]">
            <ResponsiveContainer width="100%" height="100%">
              <RadarChart data={data.risk_radar}>
                <PolarGrid stroke="hsl(var(--border))" />
                <PolarAngleAxis dataKey="category" tick={{ fill: 'hsl(var(--muted-foreground))', fontSize: 12 }} />
                <PolarRadiusAxis angle={30} domain={[0, 100]} tick={false} axisLine={false} />
                <Radar name="Score" dataKey="value" stroke="#06B6D4" fill="#06B6D4" fillOpacity={0.4} />
              </RadarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>

      {/* Activity Timeline */}
      <Card className="glass-card">
        <CardHeader><CardTitle className="text-lg flex items-center gap-2"><Clock className="h-5 w-5 text-primary" /> Recent Activity</CardTitle></CardHeader>
        <CardContent>
          <div className="space-y-3">
            {data.recent_activity.map((a: any, i: number) => (
              <div key={i} className="flex items-center justify-between p-3 rounded-lg bg-muted/50">
                <div className="flex items-center gap-3">
                  <div className={`h-2 w-2 rounded-full ${a.result === 'approved' ? 'bg-emerald-500' : 'bg-blue-500'}`} />
                  <span className="text-sm">{a.message}</span>
                </div>
                <div className="flex items-center gap-4">
                  <span className="text-sm font-medium">{a.result}</span>
                  <span className="text-xs text-muted-foreground">{a.time}</span>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
