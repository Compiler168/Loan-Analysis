"use client";

import { useAuth } from "@/contexts/AuthContext";
import { Button } from "@/components/ui/button";
import { Moon, Sun, Bell, LogOut, User } from "lucide-react";
import { useTheme } from "next-themes";

export default function Header() {
  const { user, logout } = useAuth();
  const { theme, setTheme } = useTheme();

  return (
    <header className="h-16 border-b bg-card/80 backdrop-blur-md flex items-center justify-between px-6">
      <div>
        <h2 className="text-sm font-medium text-muted-foreground">Welcome back,</h2>
        <p className="text-lg font-semibold">{user?.name || "User"}</p>
      </div>

      <div className="flex items-center gap-2">
        <Button variant="ghost" size="icon" className="relative">
          <Bell className="h-5 w-5" />
          <span className="absolute -top-1 -right-1 h-4 w-4 rounded-full bg-red-500 text-[10px] text-white flex items-center justify-center font-bold">3</span>
        </Button>

        <Button variant="ghost" size="icon" onClick={() => setTheme(theme === "dark" ? "light" : "dark")}>
          {theme === "dark" ? <Sun className="h-5 w-5" /> : <Moon className="h-5 w-5" />}
        </Button>

        <div className="h-8 w-px bg-border mx-1" />

        <div className="flex items-center gap-2">
          <div className="h-8 w-8 rounded-full gradient-bg flex items-center justify-center">
            <User className="h-4 w-4 text-white" />
          </div>
          <Button variant="ghost" size="sm" onClick={logout} className="text-muted-foreground hover:text-red-500">
            <LogOut className="h-4 w-4 mr-1" /> Sign out
          </Button>
        </div>
      </div>
    </header>
  );
}
