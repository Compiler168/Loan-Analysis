"use client";

import { useState } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Brain, ArrowRight, Eye, EyeOff } from "lucide-react";
import { AuthProvider, useAuth } from "@/contexts/AuthContext";

function RegisterForm() {
  const { register } = useAuth();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPw, setShowPw] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const strength = password.length >= 8 ? (password.match(/[A-Z]/) && password.match(/[0-9]/) ? 3 : 2) : password.length > 0 ? 1 : 0;
  const strengthLabels = ["", "Weak", "Fair", "Strong"];
  const strengthColors = ["", "bg-red-500", "bg-yellow-500", "bg-emerald-500"];

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try { await register({ name, email, password }); }
    catch (err: any) { setError(err.message || "Registration failed"); }
    finally { setLoading(false); }
  };

  return (
    <div className="min-h-screen flex">
      <div className="hidden lg:flex lg:w-1/2 gradient-bg relative flex-col justify-center p-12">
        <div className="absolute inset-0 bg-black/10" />
        <div className="relative z-10">
          <div className="flex items-center gap-3 mb-8">
            <div className="h-12 w-12 rounded-xl bg-white/20 backdrop-blur flex items-center justify-center">
              <Brain className="h-7 w-7 text-white" />
            </div>
            <span className="text-3xl font-bold text-white">SmartLoan AI+</span>
          </div>
          <h2 className="text-4xl font-bold text-white mb-4">Start Your AI-Powered<br />Financial Journey</h2>
          <p className="text-white/70 text-lg">Free access to enterprise-grade financial intelligence tools.</p>
        </div>
      </div>

      <div className="flex-1 flex items-center justify-center p-8">
        <Card className="w-full max-w-md border-0 shadow-none">
          <CardHeader className="text-center">
            <div className="lg:hidden flex items-center justify-center gap-2 mb-4">
              <Brain className="h-8 w-8 text-primary" /><span className="text-2xl font-bold gradient-text">SmartLoan AI+</span>
            </div>
            <CardTitle className="text-2xl">Create Account</CardTitle>
            <CardDescription>Start analyzing your finances with AI</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              {error && <div className="p-3 rounded-lg bg-destructive/10 text-destructive text-sm">{error}</div>}
              <div className="space-y-2">
                <Label htmlFor="name">Full Name</Label>
                <Input id="name" placeholder="John Doe" value={name} onChange={(e) => setName(e.target.value)} required />
              </div>
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input id="email" type="email" placeholder="you@example.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
              </div>
              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <div className="relative">
                  <Input id="password" type={showPw ? "text" : "password"} placeholder="Min 8 characters" value={password} onChange={(e) => setPassword(e.target.value)} required />
                  <button type="button" className="absolute right-3 top-2.5 text-muted-foreground" onClick={() => setShowPw(!showPw)}>
                    {showPw ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                  </button>
                </div>
                {password && (
                  <div className="flex items-center gap-2">
                    <div className="flex-1 flex gap-1">{[1,2,3].map(i => <div key={i} className={`h-1 flex-1 rounded-full ${i <= strength ? strengthColors[strength] : 'bg-muted'}`} />)}</div>
                    <span className="text-xs text-muted-foreground">{strengthLabels[strength]}</span>
                  </div>
                )}
              </div>
              <Button type="submit" className="w-full h-11" disabled={loading}>
                {loading ? "Creating account..." : "Create Account"} {!loading && <ArrowRight className="ml-2 h-4 w-4" />}
              </Button>
            </form>
            <p className="text-center text-sm text-muted-foreground mt-6">
              Already have an account? <Link href="/login" className="text-primary hover:underline font-medium">Sign in</Link>
            </p>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

export default function RegisterPage() {
  return <AuthProvider><RegisterForm /></AuthProvider>;
}
