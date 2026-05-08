"use client";

import { useState } from "react";
import { useAuth } from "@/contexts/AuthContext";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { User, Lock, Palette, Bell, Save, CheckCircle } from "lucide-react";
import { useTheme } from "next-themes";

export default function SettingsPage() {
  const { user, updateProfile } = useAuth();
  const { theme, setTheme } = useTheme();
  const [name, setName] = useState(user?.name || "");
  const [saved, setSaved] = useState(false);

  const save = async () => {
    await updateProfile({ name });
    setSaved(true);
    setTimeout(() => setSaved(false), 2000);
  };

  return (
    <div className="space-y-6 max-w-3xl">
      <div>
        <h1 className="text-3xl font-bold">Settings</h1>
        <p className="text-muted-foreground">Manage your account and preferences</p>
      </div>

      <Card className="glass-card">
        <CardHeader><CardTitle className="text-lg flex items-center gap-2"><User className="h-5 w-5 text-primary" /> Profile</CardTitle></CardHeader>
        <CardContent className="space-y-4">
          <div className="space-y-2"><Label>Name</Label><Input value={name} onChange={e => setName(e.target.value)} /></div>
          <div className="space-y-2"><Label>Email</Label><Input value={user?.email || ""} disabled className="opacity-60" /></div>
          <div className="space-y-2"><Label>Role</Label><Input value={user?.role || ""} disabled className="opacity-60 capitalize" /></div>
          <Button onClick={save} className="gap-2">
            {saved ? <><CheckCircle className="h-4 w-4" /> Saved!</> : <><Save className="h-4 w-4" /> Save Changes</>}
          </Button>
        </CardContent>
      </Card>

      <Card className="glass-card">
        <CardHeader><CardTitle className="text-lg flex items-center gap-2"><Palette className="h-5 w-5 text-primary" /> Appearance</CardTitle></CardHeader>
        <CardContent>
          <div className="flex gap-3">
            {[{ value: "light", label: "☀️ Light" }, { value: "dark", label: "🌙 Dark" }, { value: "system", label: "💻 System" }].map(t => (
              <button key={t.value} onClick={() => setTheme(t.value)}
                className={`flex-1 p-3 rounded-lg border text-sm font-medium transition-all ${theme === t.value ? 'border-primary bg-primary/5' : 'hover:bg-muted/50'}`}>
                {t.label}
              </button>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card className="glass-card">
        <CardHeader><CardTitle className="text-lg flex items-center gap-2"><Bell className="h-5 w-5 text-primary" /> Notifications</CardTitle></CardHeader>
        <CardContent className="space-y-3">
          {["AI Insights & Recommendations", "Loan Status Updates", "Financial Health Alerts", "Weekly Summary Reports"].map((n, i) => (
            <div key={i} className="flex items-center justify-between p-3 rounded-lg bg-muted/50">
              <span className="text-sm">{n}</span>
              <input type="checkbox" defaultChecked className="accent-primary h-4 w-4" />
            </div>
          ))}
        </CardContent>
      </Card>
    </div>
  );
}
