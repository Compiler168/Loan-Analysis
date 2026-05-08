"use client";

import { useState } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Brain, ArrowRight, Eye, EyeOff } from "lucide-react";
import { AuthProvider, useAuth } from "@/contexts/AuthContext";

function LoginForm() {
  const { login } = useAuth();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPw, setShowPw] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try { await login({ email, password }); }
    catch (err: any) { setError(err.message || "Login failed"); }
    finally { setLoading(false); }
  };

  const handleDemo = () => { setEmail("demo@smartloan.ai"); setPassword("demo123"); };

  return (
    <div className="min-h-screen flex">
      {/* Left brand panel */}
      <div className="hidden lg:flex lg:w-1/2 gradient-bg relative flex-col justify-center p-12">
        <div className="absolute inset-0 bg-black/10" />
        <div className="relative z-10">
          <div className="flex items-center gap-3 mb-8">
            <div className="h-12 w-12 rounded-xl bg-white/20 backdrop-blur flex items-center justify-center">
              <Brain className="h-7 w-7 text-white" />
            </div>
            <span className="text-3xl font-bold text-white">SmartLoan AI+</span>
          </div>
          <h2 className="text-4xl font-bold text-white mb-4 leading-tight">Intelligent Financial<br />Advisory Platform</h2>
          <p className="text-white/70 text-lg max-w-md">Powered by real ML models — Logistic Regression, Random Forest, and XGBoost ensemble prediction.</p>
          <div className="mt-8 space-y-3">
            {["AI Loan Prediction", "Financial Health Scoring", "Intelligent Risk Analysis", "NLP-Powered Chatbot"].map((f, i) => (
              <div key={i} className="flex items-center gap-2 text-white/80"><div className="h-1.5 w-1.5 rounded-full bg-cyan-400" />{f}</div>
            ))}
          </div>
        </div>
      </div>

      {/* Right form panel */}
      <div className="flex-1 flex items-center justify-center p-8">
        <Card className="w-full max-w-md border-0 shadow-none">
          <CardHeader className="text-center">
            <div className="lg:hidden flex items-center justify-center gap-2 mb-4">
              <Brain className="h-8 w-8 text-primary" />
              <span className="text-2xl font-bold gradient-text">SmartLoan AI+</span>
            </div>
            <CardTitle className="text-2xl">Welcome back</CardTitle>
            <CardDescription>Sign in to your AI dashboard</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              {error && <div className="p-3 rounded-lg bg-destructive/10 text-destructive text-sm">{error}</div>}
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input id="email" type="email" placeholder="you@example.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
              </div>
              <div className="space-y-2">
                <div className="flex justify-between">
                  <Label htmlFor="password">Password</Label>
                  <button type="button" className="text-xs text-primary hover:underline">Forgot password?</button>
                </div>
                <div className="relative">
                  <Input id="password" type={showPw ? "text" : "password"} placeholder="••••••••" value={password} onChange={(e) => setPassword(e.target.value)} required />
                  <button type="button" className="absolute right-3 top-2.5 text-muted-foreground" onClick={() => setShowPw(!showPw)}>
                    {showPw ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                  </button>
                </div>
              </div>
              <Button type="submit" className="w-full h-11" disabled={loading}>
                {loading ? "Signing in..." : "Sign In"} {!loading && <ArrowRight className="ml-2 h-4 w-4" />}
              </Button>
              <Button type="button" variant="outline" className="w-full" onClick={handleDemo}>
                Use Demo Account
              </Button>
            </form>
            <p className="text-center text-sm text-muted-foreground mt-6">
              Don&apos;t have an account? <Link href="/register" className="text-primary hover:underline font-medium">Sign up</Link>
            </p>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

export default function LoginPage() {
  return <AuthProvider><LoginForm /></AuthProvider>;
}
