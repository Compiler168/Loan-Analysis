"use client";

import { useState } from "react";
import { api } from "@/lib/api";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Progress } from "@/components/ui/progress";
import { Brain, CheckCircle, XCircle, AlertTriangle, ChevronRight, ChevronLeft, Loader2 } from "lucide-react";
import { BarChart, Bar, XAxis, YAxis, ResponsiveContainer, Tooltip as RechartsTooltip, CartesianGrid } from "recharts";
import { motion, AnimatePresence } from "framer-motion";

const STEPS = ["Personal Info", "Financial Details", "Loan Parameters"];

const defaultForm = {
  age: 35, dependents: 1, employment_status: "salaried", employment_years: 5,
  monthly_income: 7500, monthly_expenses: 3000, credit_score: 720,
  existing_loans: 1, existing_emi: 500, loan_amount: 50000,
  loan_term_months: 36, interest_rate: 10, property_value: 150000,
  savings_balance: 25000, missed_payments_last_year: 0, bankruptcies: 0
};

export default function LoanPredictionPage() {
  const [step, setStep] = useState(0);
  const [form, setForm] = useState(defaultForm);
  const [result, setResult] = useState<any>(null);
  const [loading, setLoading] = useState(false);

  const set = (k: string, v: any) => setForm(p => ({ ...p, [k]: v }));

  const predict = async () => {
    setLoading(true);
    try {
      const res: any = await api.post('/loans/predict', form);
      if (res.success) setResult(res.data);
    } catch (e) { console.error(e); }
    finally { setLoading(false); }
  };

  const Field = ({ label, k, type = "number" }: { label: string; k: string; type?: string }) => (
    <div className="space-y-1.5">
      <Label className="text-sm">{label}</Label>
      <Input type={type} value={(form as any)[k]} onChange={e => set(k, type === "number" ? Number(e.target.value) : e.target.value)} />
    </div>
  );

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold">AI Loan Prediction</h1>
        <p className="text-muted-foreground">ML-powered approval probability with explainability</p>
      </div>

      <div className="grid lg:grid-cols-5 gap-6">
        {/* Input Form */}
        <div className="lg:col-span-2">
          <Card className="glass-card">
            <CardHeader>
              {/* Step indicator */}
              <div className="flex items-center gap-2 mb-4">
                {STEPS.map((s, i) => (
                  <div key={i} className="flex items-center gap-2">
                    <div className={`h-8 w-8 rounded-full flex items-center justify-center text-xs font-bold ${i <= step ? 'bg-primary text-white' : 'bg-muted text-muted-foreground'}`}>{i+1}</div>
                    {i < STEPS.length - 1 && <div className={`w-8 h-0.5 ${i < step ? 'bg-primary' : 'bg-muted'}`} />}
                  </div>
                ))}
              </div>
              <CardTitle className="text-lg">{STEPS[step]}</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <AnimatePresence mode="wait">
                <motion.div key={step} initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: -20 }} className="space-y-4">
                  {step === 0 && <>
                    <Field label="Age" k="age" />
                    <Field label="Dependents" k="dependents" />
                    <div className="space-y-1.5">
                      <Label className="text-sm">Employment Status</Label>
                      <select className="w-full h-10 rounded-md border bg-background px-3 text-sm" value={form.employment_status} onChange={e => set('employment_status', e.target.value)}>
                        {["salaried","self_employed","freelancer","business_owner","retired"].map(o => <option key={o} value={o}>{o.replace('_',' ')}</option>)}
                      </select>
                    </div>
                    <Field label="Employment Years" k="employment_years" />
                  </>}
                  {step === 1 && <>
                    <Field label="Monthly Income ($)" k="monthly_income" />
                    <Field label="Monthly Expenses ($)" k="monthly_expenses" />
                    <Field label="Credit Score (300-850)" k="credit_score" />
                    <Field label="Existing Loans" k="existing_loans" />
                    <Field label="Existing EMI ($)" k="existing_emi" />
                    <Field label="Savings Balance ($)" k="savings_balance" />
                  </>}
                  {step === 2 && <>
                    <Field label="Loan Amount ($)" k="loan_amount" />
                    <Field label="Loan Term (months)" k="loan_term_months" />
                    <Field label="Interest Rate (%)" k="interest_rate" />
                    <Field label="Property Value ($)" k="property_value" />
                    <Field label="Missed Payments (last year)" k="missed_payments_last_year" />
                    <Field label="Bankruptcies" k="bankruptcies" />
                  </>}
                </motion.div>
              </AnimatePresence>

              <div className="flex justify-between pt-2">
                <Button variant="outline" disabled={step === 0} onClick={() => setStep(s => s - 1)}>
                  <ChevronLeft className="h-4 w-4 mr-1" /> Back
                </Button>
                {step < 2 ? (
                  <Button onClick={() => setStep(s => s + 1)}>Next <ChevronRight className="h-4 w-4 ml-1" /></Button>
                ) : (
                  <Button onClick={predict} disabled={loading} className="min-w-[140px]">
                    {loading ? <><Loader2 className="h-4 w-4 mr-2 animate-spin" /> Analyzing...</> : <><Brain className="h-4 w-4 mr-2" /> Predict</>}
                  </Button>
                )}
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Results */}
        <div className="lg:col-span-3 space-y-4">
          {result ? (
            <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="space-y-4">
              {/* Ensemble Result */}
              <Card className={`border-2 ${result.ensemble.approved ? 'border-emerald-500/50 bg-emerald-500/5' : 'border-red-500/50 bg-red-500/5'}`}>
                <CardContent className="p-6 flex items-center gap-6">
                  <div className="relative">
                    <svg className="h-28 w-28 -rotate-90">
                      <circle cx="56" cy="56" r="48" stroke="hsl(var(--muted))" strokeWidth="8" fill="none" />
                      <circle cx="56" cy="56" r="48" stroke={result.ensemble.approved ? '#22C55E' : '#EF4444'} strokeWidth="8" fill="none"
                        strokeDasharray={`${result.ensemble.probability * 301.6} 301.6`} strokeLinecap="round" />
                    </svg>
                    <div className="absolute inset-0 flex items-center justify-center">
                      <span className="text-2xl font-bold">{(result.ensemble.probability * 100).toFixed(1)}%</span>
                    </div>
                  </div>
                  <div>
                    <div className="flex items-center gap-2 mb-1">
                      {result.ensemble.approved ? <CheckCircle className="h-6 w-6 text-emerald-500" /> : <XCircle className="h-6 w-6 text-red-500" />}
                      <h3 className="text-xl font-bold">{result.ensemble.approved ? 'Likely Approved' : 'Likely Rejected'}</h3>
                    </div>
                    <p className="text-sm text-muted-foreground">Confidence: {result.ensemble.confidence} ({(result.ensemble.confidence_score * 100).toFixed(0)}% model agreement)</p>
                    <p className="text-sm text-muted-foreground">EMI: ${result.derived_metrics.requested_emi?.toLocaleString()}/mo</p>
                  </div>
                </CardContent>
              </Card>

              {/* Model Breakdown */}
              <Card className="glass-card">
                <CardHeader><CardTitle className="text-lg">Model Breakdown</CardTitle></CardHeader>
                <CardContent>
                  <div className="space-y-3">
                    {Object.entries(result.models).map(([name, m]: any) => (
                      <div key={name} className="space-y-1">
                        <div className="flex justify-between text-sm">
                          <span className="capitalize">{name.replace('_', ' ')}</span>
                          <span className="font-medium">{(m.probability * 100).toFixed(1)}%</span>
                        </div>
                        <Progress value={m.probability * 100} className="h-2" />
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>

              {/* Risk Reasons */}
              <Card className="glass-card">
                <CardHeader><CardTitle className="text-lg">Risk Factors</CardTitle></CardHeader>
                <CardContent className="space-y-2">
                  {result.risk_reasons.map((r: any, i: number) => (
                    <div key={i} className={`p-3 rounded-lg border ${r.severity === 'high' ? 'bg-red-500/5 border-red-500/20' : r.severity === 'medium' ? 'bg-amber-500/5 border-amber-500/20' : 'bg-emerald-500/5 border-emerald-500/20'}`}>
                      <div className="flex items-start gap-2">
                        <AlertTriangle className={`h-4 w-4 mt-0.5 ${r.severity === 'high' ? 'text-red-500' : r.severity === 'medium' ? 'text-amber-500' : 'text-emerald-500'}`} />
                        <div>
                          <p className="text-sm font-medium">{r.factor}: {r.message}</p>
                          <p className="text-xs text-muted-foreground mt-0.5">{r.suggestion}</p>
                        </div>
                      </div>
                    </div>
                  ))}
                </CardContent>
              </Card>

              {/* Feature Importance */}
              <Card className="glass-card">
                <CardHeader><CardTitle className="text-lg">Top Feature Importance</CardTitle></CardHeader>
                <CardContent className="h-[250px]">
                  <ResponsiveContainer width="100%" height="100%">
                    <BarChart data={Object.entries(result.top_factors).map(([k,v]) => ({ name: k.replace(/_/g,' '), value: Number((v as number * 100).toFixed(1)) }))} layout="vertical" margin={{ left: 80 }}>
                      <CartesianGrid strokeDasharray="3 3" horizontal={false} stroke="hsl(var(--border))" />
                      <XAxis type="number" fontSize={12} tickLine={false} axisLine={false} />
                      <YAxis type="category" dataKey="name" fontSize={11} tickLine={false} axisLine={false} width={90} />
                      <Bar dataKey="value" fill="#2563EB" radius={[0,4,4,0]} />
                    </BarChart>
                  </ResponsiveContainer>
                </CardContent>
              </Card>
            </motion.div>
          ) : (
            <div className="flex items-center justify-center h-[400px] border-2 border-dashed rounded-xl">
              <div className="text-center text-muted-foreground">
                <Brain className="h-12 w-12 mx-auto mb-4 opacity-30" />
                <p className="text-lg font-medium">Run Prediction</p>
                <p className="text-sm">Fill in the form and click Predict to see results</p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
