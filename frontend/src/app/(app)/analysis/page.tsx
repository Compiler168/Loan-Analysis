"use client";

import { useState } from "react";
import { api } from "@/lib/api";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Progress } from "@/components/ui/progress";
import { Activity, ShieldAlert, Loader2, ArrowRight } from "lucide-react";
import { motion } from "framer-motion";

const defaults = {
  monthly_income: 7500, monthly_expenses: 3000, savings_balance: 25000,
  existing_emi: 500, existing_loans: 1, credit_score: 720, employment_years: 5,
  missed_payments_last_year: 0, bankruptcies: 0, age: 35, dependents: 1,
  property_value: 150000, loan_amount: 50000, loan_term_months: 36, interest_rate: 10
};

export default function AnalysisPage() {
  const [form, setForm] = useState(defaults);
  const [health, setHealth] = useState<any>(null);
  const [risk, setRisk] = useState<any>(null);
  const [loading, setLoading] = useState("");

  const set = (k: string, v: number) => setForm(p => ({ ...p, [k]: v }));
  const F = ({ label, k }: { label: string; k: string }) => (
    <div className="space-y-1"><Label className="text-xs">{label}</Label>
      <Input type="number" value={(form as any)[k]} onChange={e => set(k, Number(e.target.value))} className="h-9" /></div>
  );

  const runHealth = async () => {
    setLoading("health");
    try { const r: any = await api.post('/financial/health-score', form); if (r.success) setHealth(r.data); } catch {}
    setLoading("");
  };

  const runRisk = async () => {
    setLoading("risk");
    try { const r: any = await api.post('/financial/risk-analysis', form); if (r.success) setRisk(r.data); } catch {}
    setLoading("");
  };

  const scoreColor = (s: number) => s >= 75 ? 'text-emerald-500' : s >= 50 ? 'text-amber-500' : 'text-red-500';
  const sevColor = (s: string) => s === 'low' ? 'bg-emerald-500/10 border-emerald-500/20 text-emerald-700' : s === 'medium' ? 'bg-amber-500/10 border-amber-500/20 text-amber-700' : 'bg-red-500/10 border-red-500/20 text-red-700';

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold">Financial Analysis</h1>
        <p className="text-muted-foreground">Health scoring & risk assessment powered by AI</p>
      </div>

      <div className="grid lg:grid-cols-3 gap-6">
        {/* Input */}
        <Card className="glass-card">
          <CardHeader><CardTitle className="text-lg">Financial Parameters</CardTitle></CardHeader>
          <CardContent className="space-y-3">
            <F label="Monthly Income ($)" k="monthly_income" />
            <F label="Monthly Expenses ($)" k="monthly_expenses" />
            <F label="Savings ($)" k="savings_balance" />
            <F label="Credit Score" k="credit_score" />
            <F label="Existing EMI ($)" k="existing_emi" />
            <F label="Existing Loans" k="existing_loans" />
            <F label="Employment Years" k="employment_years" />
            <F label="Missed Payments" k="missed_payments_last_year" />
            <div className="grid grid-cols-2 gap-2 pt-2">
              <Button onClick={runHealth} disabled={!!loading} className="w-full">
                {loading === "health" ? <Loader2 className="h-4 w-4 animate-spin" /> : <><Activity className="h-4 w-4 mr-1" /> Health</>}
              </Button>
              <Button onClick={runRisk} disabled={!!loading} variant="outline" className="w-full">
                {loading === "risk" ? <Loader2 className="h-4 w-4 animate-spin" /> : <><ShieldAlert className="h-4 w-4 mr-1" /> Risk</>}
              </Button>
            </div>
          </CardContent>
        </Card>

        {/* Results */}
        <div className="lg:col-span-2">
          <Tabs defaultValue="health">
            <TabsList className="mb-4"><TabsTrigger value="health">Health Score</TabsTrigger><TabsTrigger value="risk">Risk Analysis</TabsTrigger></TabsList>

            <TabsContent value="health">
              {health ? (
                <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-4">
                  <Card className="glass-card">
                    <CardContent className="p-6 flex items-center gap-6">
                      <div className="relative">
                        <svg className="h-28 w-28 -rotate-90">
                          <circle cx="56" cy="56" r="48" stroke="hsl(var(--muted))" strokeWidth="8" fill="none" />
                          <circle cx="56" cy="56" r="48" stroke={health.overall_score >= 75 ? '#22C55E' : health.overall_score >= 50 ? '#EAB308' : '#EF4444'} strokeWidth="8" fill="none" strokeDasharray={`${health.overall_score/100*301.6} 301.6`} strokeLinecap="round" />
                        </svg>
                        <div className="absolute inset-0 flex flex-col items-center justify-center">
                          <span className="text-2xl font-bold">{health.overall_score}</span>
                          <span className="text-xs text-muted-foreground">/100</span>
                        </div>
                      </div>
                      <div>
                        <h3 className="text-2xl font-bold">{health.grade} — {health.grade_label}</h3>
                        <p className="text-sm text-muted-foreground mt-1">{health.summary}</p>
                      </div>
                    </CardContent>
                  </Card>

                  <Card className="glass-card">
                    <CardHeader><CardTitle className="text-lg">Category Breakdown</CardTitle></CardHeader>
                    <CardContent className="space-y-4">
                      {health.breakdown.map((b: any, i: number) => (
                        <div key={i} className="space-y-1.5">
                          <div className="flex justify-between text-sm"><span>{b.category}</span><span className={`font-bold ${scoreColor(b.score)}`}>{b.score}/100</span></div>
                          <Progress value={b.score} className="h-2" />
                          <p className="text-xs text-muted-foreground">{b.reasoning[0]}</p>
                        </div>
                      ))}
                    </CardContent>
                  </Card>

                  {health.roadmap.length > 0 && (
                    <Card className="glass-card">
                      <CardHeader><CardTitle className="text-lg">Improvement Roadmap</CardTitle></CardHeader>
                      <CardContent className="space-y-3">
                        {health.roadmap.map((r: any, i: number) => (
                          <div key={i} className="p-3 rounded-lg border bg-muted/50">
                            <div className="flex justify-between mb-2">
                              <span className="font-medium text-sm">{r.category}</span>
                              <span className={`text-xs px-2 py-0.5 rounded-full ${r.priority === 'high' ? 'bg-red-100 text-red-700' : 'bg-amber-100 text-amber-700'}`}>{r.priority}</span>
                            </div>
                            {r.actions.map((a: string, j: number) => (
                              <div key={j} className="flex items-center gap-2 text-xs text-muted-foreground mt-1"><ArrowRight className="h-3 w-3" />{a}</div>
                            ))}
                          </div>
                        ))}
                      </CardContent>
                    </Card>
                  )}
                </motion.div>
              ) : <EmptyState text="Run Health Score to see results" />}
            </TabsContent>

            <TabsContent value="risk">
              {risk ? (
                <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-4">
                  <Card className={`border-2 ${risk.risk_level === 'low' ? 'border-emerald-500/50' : risk.risk_level === 'moderate' ? 'border-amber-500/50' : 'border-red-500/50'}`}>
                    <CardContent className="p-6">
                      <div className="flex items-center justify-between">
                        <div>
                          <h3 className="text-2xl font-bold capitalize">{risk.risk_level} Risk</h3>
                          <p className="text-sm text-muted-foreground">{risk.summary}</p>
                        </div>
                        <div className="text-4xl font-bold" style={{ color: risk.risk_color }}>{risk.overall_risk}</div>
                      </div>
                    </CardContent>
                  </Card>

                  <Card className="glass-card">
                    <CardHeader><CardTitle className="text-lg">Risk Dimensions</CardTitle></CardHeader>
                    <CardContent className="space-y-3">
                      {risk.dimensions.map((d: any, i: number) => (
                        <div key={i} className={`p-3 rounded-lg border ${sevColor(d.severity)}`}>
                          <div className="flex justify-between mb-1">
                            <span className="font-medium text-sm">{d.dimension}</span>
                            <span className="text-sm font-bold">{d.value}</span>
                          </div>
                          <Progress value={d.score} className="h-1.5 mb-1" />
                          <p className="text-xs opacity-80">{d.message}</p>
                        </div>
                      ))}
                    </CardContent>
                  </Card>
                </motion.div>
              ) : <EmptyState text="Run Risk Analysis to see results" />}
            </TabsContent>
          </Tabs>
        </div>
      </div>
    </div>
  );
}

function EmptyState({ text }: { text: string }) {
  return (
    <div className="flex items-center justify-center h-[300px] border-2 border-dashed rounded-xl">
      <p className="text-muted-foreground">{text}</p>
    </div>
  );
}
