"use client";

import { useState } from "react";
import { api } from "@/lib/api";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { FileText, Download, Loader2, CheckCircle } from "lucide-react";

const TYPES = [
  { id: "financial_summary", label: "Financial Summary", desc: "Comprehensive overview of your financial health" },
  { id: "loan_analysis", label: "Loan Analysis", desc: "Detailed loan eligibility and recommendation report" },
  { id: "risk_report", label: "Risk Assessment", desc: "Full risk profile with preventive measures" },
  { id: "ai_recommendations", label: "AI Recommendations", desc: "Personalized AI-generated improvement plan" },
];

export default function ReportsPage() {
  const [selected, setSelected] = useState("financial_summary");
  const [report, setReport] = useState<any>(null);
  const [loading, setLoading] = useState(false);

  const generate = async () => {
    setLoading(true);
    try { const r: any = await api.post('/reports/generate', { type: selected }); if (r.success) setReport(r.data); } catch {}
    setLoading(false);
  };

  const download = () => {
    if (!report) return;
    const w = window.open('', '_blank');
    if (!w) return;
    w.document.write(`<html><head><title>${report.title}</title><style>body{font-family:Inter,sans-serif;max-width:800px;margin:40px auto;padding:20px;color:#1a1a2e;}h1{color:#2563EB;border-bottom:2px solid #2563EB;padding-bottom:10px;}h2{color:#4F46E5;margin-top:30px;}table{width:100%;border-collapse:collapse;margin:15px 0;}td,th{padding:8px 12px;border:1px solid #e5e7eb;text-align:left;}th{background:#f8fafc;}</style></head><body>`);
    w.document.write(`<h1>${report.title}</h1><p style="color:#666">Generated: ${new Date(report.generated_at).toLocaleString()}</p>`);
    report.sections.forEach((s: any) => {
      w.document.write(`<h2>${s.title}</h2>`);
      if (s.content) w.document.write(`<p>${s.content}</p>`);
      if (s.items) {
        w.document.write('<table><tr><th>Metric</th><th>Value</th></tr>');
        s.items.forEach((it: any) => w.document.write(`<tr><td>${it.label}</td><td>${it.value}</td></tr>`));
        w.document.write('</table>');
      }
    });
    w.document.write('</body></html>');
    w.document.close();
    w.print();
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold">Reports</h1>
        <p className="text-muted-foreground">Generate and download AI-powered financial reports</p>
      </div>

      <div className="grid lg:grid-cols-3 gap-6">
        <div className="space-y-4">
          <Card className="glass-card">
            <CardHeader><CardTitle className="text-lg">Report Type</CardTitle></CardHeader>
            <CardContent className="space-y-2">
              {TYPES.map(t => (
                <button key={t.id} onClick={() => setSelected(t.id)}
                  className={`w-full text-left p-3 rounded-lg border transition-all ${selected === t.id ? 'border-primary bg-primary/5 shadow-sm' : 'hover:bg-muted/50'}`}>
                  <div className="flex items-center gap-2">
                    <FileText className={`h-4 w-4 ${selected === t.id ? 'text-primary' : 'text-muted-foreground'}`} />
                    <span className="font-medium text-sm">{t.label}</span>
                  </div>
                  <p className="text-xs text-muted-foreground mt-1 ml-6">{t.desc}</p>
                </button>
              ))}
              <Button onClick={generate} disabled={loading} className="w-full mt-2">
                {loading ? <Loader2 className="h-4 w-4 animate-spin mr-2" /> : <FileText className="h-4 w-4 mr-2" />}
                Generate Report
              </Button>
            </CardContent>
          </Card>
        </div>

        <div className="lg:col-span-2">
          {report ? (
            <Card className="glass-card">
              <CardHeader className="flex flex-row items-center justify-between">
                <div>
                  <CardTitle>{report.title}</CardTitle>
                  <p className="text-sm text-muted-foreground mt-1">Generated: {new Date(report.generated_at).toLocaleString()}</p>
                </div>
                <Button variant="outline" onClick={download}><Download className="h-4 w-4 mr-2" /> Print / PDF</Button>
              </CardHeader>
              <CardContent className="space-y-6">
                {report.sections.map((s: any, i: number) => (
                  <div key={i}>
                    <h3 className="text-lg font-semibold mb-2">{s.title}</h3>
                    {s.content && <p className="text-sm text-muted-foreground leading-relaxed">{s.content}</p>}
                    {s.items && (
                      <div className="space-y-2 mt-2">
                        {s.items.map((it: any, j: number) => (
                          <div key={j} className="flex justify-between items-center p-3 rounded-lg bg-muted/50">
                            <span className="text-sm">{it.label}</span>
                            <div className="flex items-center gap-2">
                              <span className="text-sm font-semibold">{it.value}</span>
                              {it.status && <div className={`h-2 w-2 rounded-full ${it.status === 'good' ? 'bg-emerald-500' : 'bg-amber-500'}`} />}
                            </div>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                ))}
              </CardContent>
            </Card>
          ) : (
            <div className="flex items-center justify-center h-[400px] border-2 border-dashed rounded-xl">
              <p className="text-muted-foreground">Select a report type and generate</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
