"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import {
  Brain, LayoutDashboard, Target, BarChart3, Bot,
  LineChart, FileText, Settings, Shield, ChevronLeft, ChevronRight
} from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/button";

const navItems = [
  { href: "/dashboard", icon: LayoutDashboard, label: "Dashboard" },
  { href: "/loan-prediction", icon: Target, label: "Loan Prediction" },
  { href: "/analysis", icon: BarChart3, label: "Financial Analysis" },
  { href: "/chatbot", icon: Bot, label: "AI Chatbot" },
  { href: "/simulator", icon: LineChart, label: "Simulator" },
  { href: "/reports", icon: FileText, label: "Reports" },
  { href: "/settings", icon: Settings, label: "Settings" },
  { href: "/admin", icon: Shield, label: "Admin Panel" },
];

export default function Sidebar() {
  const pathname = usePathname();
  const [collapsed, setCollapsed] = useState(false);

  return (
    <aside className={cn(
      "flex flex-col h-full border-r bg-card/80 backdrop-blur-md transition-all duration-300",
      collapsed ? "w-[68px]" : "w-[250px]"
    )}>
      {/* Logo */}
      <div className="flex items-center gap-2 p-4 border-b">
        <div className="h-9 w-9 rounded-lg gradient-bg flex items-center justify-center flex-shrink-0">
          <Brain className="h-5 w-5 text-white" />
        </div>
        {!collapsed && (
          <span className="text-lg font-bold gradient-text whitespace-nowrap">SmartLoan AI+</span>
        )}
      </div>

      {/* Navigation */}
      <nav className="flex-1 p-3 space-y-1 overflow-y-auto no-scrollbar">
        {navItems.map((item) => {
          const isActive = pathname === item.href;
          return (
            <Link key={item.href} href={item.href}
              className={cn(
                "flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-all duration-200",
                isActive
                  ? "bg-primary/10 text-primary shadow-sm"
                  : "text-muted-foreground hover:bg-muted hover:text-foreground"
              )}
            >
              <item.icon className={cn("h-5 w-5 flex-shrink-0", isActive && "text-primary")} />
              {!collapsed && <span>{item.label}</span>}
            </Link>
          );
        })}
      </nav>

      {/* Collapse toggle */}
      <div className="p-3 border-t">
        <Button variant="ghost" size="sm" className="w-full justify-center" onClick={() => setCollapsed(!collapsed)}>
          {collapsed ? <ChevronRight className="h-4 w-4" /> : <ChevronLeft className="h-4 w-4" />}
        </Button>
      </div>
    </aside>
  );
}
