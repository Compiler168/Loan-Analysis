import Link from "next/link";
import { Button } from "@/components/ui/button";
import { ArrowRight, Brain, BarChart3, ShieldCheck, Zap, Bot, LineChart, CheckCircle } from "lucide-react";

export default function LandingPage() {
  return (
    <div className="min-h-screen bg-background flex flex-col">
      {/* Navbar */}
      <header className="sticky top-0 z-50 w-full border-b backdrop-blur-md bg-background/80">
        <div className="container flex h-16 items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="h-8 w-8 rounded-lg gradient-bg flex items-center justify-center">
              <Brain className="h-5 w-5 text-white" />
            </div>
            <span className="text-xl font-bold gradient-text">SmartLoan AI+</span>
          </div>
          <nav className="hidden md:flex gap-6">
            <Link href="#features" className="text-sm font-medium hover:text-primary transition-colors">Features</Link>
            <Link href="#stats" className="text-sm font-medium hover:text-primary transition-colors">Performance</Link>
          </nav>
          <div className="flex items-center gap-3">
            <Link href="/login"><Button variant="ghost">Log in</Button></Link>
            <Link href="/register"><Button className="rounded-full">Get Started <ArrowRight className="ml-2 h-4 w-4" /></Button></Link>
          </div>
        </div>
      </header>

      <main className="flex-1">
        {/* Hero */}
        <section className="relative px-4 py-24 md:py-36 overflow-hidden flex flex-col items-center text-center">
          <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-blue-900/20 via-background to-background -z-10" />
          <div className="absolute top-20 left-1/4 w-72 h-72 bg-blue-500/10 rounded-full blur-3xl -z-10" />
          <div className="absolute top-40 right-1/4 w-96 h-96 bg-cyan-500/10 rounded-full blur-3xl -z-10" />

          <div className="inline-flex items-center rounded-full border px-4 py-1.5 text-sm font-medium mb-8 bg-muted/50 backdrop-blur-sm">
            <span className="flex h-2 w-2 rounded-full bg-emerald-500 mr-2 animate-pulse" />
            AI Engine v2.0 — Live
          </div>

          <h1 className="text-5xl md:text-7xl font-extrabold tracking-tight max-w-4xl mb-6 leading-[1.1]">
            Intelligent <span className="gradient-text">Financial Advisory</span> & Loan Prediction
          </h1>

          <p className="text-xl text-muted-foreground max-w-2xl mb-10 leading-relaxed">
            Harness machine learning to predict loan approvals, analyze financial health, detect risks, and get AI-driven personalized strategies.
          </p>

          <div className="flex flex-col sm:flex-row gap-4">
            <Link href="/register">
              <Button size="lg" className="text-lg h-14 px-8 rounded-full shadow-lg shadow-blue-500/25 hover:shadow-blue-500/40 transition-shadow">
                Start Free Analysis <ArrowRight className="ml-2 h-5 w-5" />
              </Button>
            </Link>
            <Link href="/login">
              <Button variant="outline" size="lg" className="text-lg h-14 px-8 rounded-full bg-background/50 backdrop-blur-sm">
                Demo Login
              </Button>
            </Link>
          </div>

          <p className="mt-4 text-sm text-muted-foreground">demo@smartloan.ai / demo123</p>
        </section>

        {/* Features */}
        <section id="features" className="container py-24">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold mb-4">Enterprise-Grade Financial AI</h2>
            <p className="text-muted-foreground text-lg max-w-2xl mx-auto">
              Advanced ML models process hundreds of data points for institutional-level intelligence.
            </p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            {[
              { icon: <TargetIcon className="h-6 w-6" />, title: "AI Loan Prediction", desc: "Ensemble ML models (XGBoost, Random Forest, LR) predict approval probability with feature-importance explainability." },
              { icon: <BarChart3 className="h-6 w-6" />, title: "Financial Health Score", desc: "Custom algorithm assessing DTI, savings, spending, and stability to generate a comprehensive 0-100 score." },
              { icon: <Bot className="h-6 w-6" />, title: "Intelligent Advisor", desc: "NLP chatbot with intent classification, context memory, and personalized financial reasoning." },
              { icon: <ShieldCheck className="h-6 w-6" />, title: "Risk Analysis Engine", desc: "Detect overspending, EMI strain, and default probability with AI-powered preventive suggestions." },
              { icon: <LineChart className="h-6 w-6" />, title: "Scenario Simulator", desc: "What-if financial projections: income changes, expense reduction, and loan impact analysis." },
              { icon: <Zap className="h-6 w-6" />, title: "Real-time AI Insights", desc: "Dynamic dashboard with continuous personalized recommendations based on financial behavior." },
            ].map((f, i) => (
              <div key={i} className="glass-card p-8 rounded-2xl card-hover">
                <div className="h-12 w-12 rounded-xl bg-primary/10 flex items-center justify-center mb-6 text-primary">{f.icon}</div>
                <h3 className="text-xl font-bold mb-3">{f.title}</h3>
                <p className="text-muted-foreground leading-relaxed">{f.desc}</p>
              </div>
            ))}
          </div>
        </section>

        {/* Stats */}
        <section id="stats" className="py-24 bg-muted/40">
          <div className="container">
            <div className="text-center mb-16">
              <h2 className="text-3xl font-bold mb-4">Built with Real Machine Learning</h2>
              <p className="text-muted-foreground max-w-xl mx-auto">Not a wrapper around APIs — custom-trained models with production-grade ML pipelines.</p>
            </div>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
              {[
                { value: "94.2%", label: "Model Accuracy" },
                { value: "12,000+", label: "Training Samples" },
                { value: "21", label: "Feature Variables" },
                { value: "3", label: "Ensemble Models" },
              ].map((s, i) => (
                <div key={i} className="text-center">
                  <div className="text-4xl md:text-5xl font-extrabold gradient-text mb-2">{s.value}</div>
                  <div className="text-sm text-muted-foreground font-medium">{s.label}</div>
                </div>
              ))}
            </div>
          </div>
        </section>

        {/* CTA */}
        <section className="py-24">
          <div className="container">
            <div className="relative rounded-3xl overflow-hidden gradient-bg p-12 md:p-20 text-center">
              <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48Y2lyY2xlIGN4PSIyMCIgY3k9IjIwIiByPSIxIiBmaWxsPSJyZ2JhKDI1NSwyNTUsMjU1LDAuMSkiLz48L3N2Zz4=')] opacity-30" />
              <h2 className="text-3xl md:text-5xl font-bold text-white mb-6 relative">Ready to Transform Your Financial Future?</h2>
              <p className="text-white/80 text-lg mb-8 max-w-2xl mx-auto relative">Get personalized AI-driven financial analysis in minutes.</p>
              <Link href="/register">
                <Button size="lg" className="bg-white text-blue-600 hover:bg-white/90 text-lg h-14 px-10 rounded-full font-semibold relative">
                  Get Started Free <ArrowRight className="ml-2 h-5 w-5" />
                </Button>
              </Link>
            </div>
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="border-t bg-muted/40 py-12">
        <div className="container flex flex-col md:flex-row justify-between items-center gap-6">
          <div className="flex items-center gap-2">
            <Brain className="h-6 w-6 text-primary" />
            <span className="text-xl font-bold">SmartLoan AI+</span>
          </div>
          <div className="flex gap-6 text-sm text-muted-foreground">
            <span>ML-Powered</span>
            <span>•</span>
            <span>End-to-End Encrypted</span>
            <span>•</span>
            <span>SOC 2 Ready</span>
          </div>
          <p className="text-sm text-muted-foreground">© 2026 SmartLoan AI+. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
}

function TargetIcon(props: React.SVGProps<SVGSVGElement>) {
  return (
    <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10" /><circle cx="12" cy="12" r="6" /><circle cx="12" cy="12" r="2" />
    </svg>
  );
}
