"use client";

import { useState, useRef, useEffect } from "react";
import { api } from "@/lib/api";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Bot, Send, User, Brain, Sparkles } from "lucide-react";
import ReactMarkdown from "react-markdown";
import { motion, AnimatePresence } from "framer-motion";

interface Message { role: "user" | "assistant"; content: string; suggestions?: string[]; ts: string; }

const PROMPTS = [
  "Am I eligible for a loan?",
  "How to improve my credit score?",
  "Budget tips for saving more",
  "What are my financial risks?",
  "Calculate my EMI",
  "Explain DTI ratio",
];

export default function ChatbotPage() {
  const [messages, setMessages] = useState<Message[]>([
    { role: "assistant", content: "Hello! 👋 I'm your **SmartLoan AI Financial Advisor**. I can help with loan eligibility, credit scores, budgeting, risk analysis, and more. What would you like to know?", suggestions: PROMPTS.slice(0, 4), ts: new Date().toISOString() }
  ]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const endRef = useRef<HTMLDivElement>(null);

  useEffect(() => { endRef.current?.scrollIntoView({ behavior: "smooth" }); }, [messages]);

  const send = async (text?: string) => {
    const msg = text || input.trim();
    if (!msg || loading) return;
    setInput("");
    const userMsg: Message = { role: "user", content: msg, ts: new Date().toISOString() };
    setMessages(p => [...p, userMsg]);
    setLoading(true);
    try {
      const res: any = await api.post('/chat/message', { message: msg, session_id: "main" });
      if (res.success) {
        setMessages(p => [...p, { role: "assistant", content: res.data.response, suggestions: res.data.suggestions, ts: res.data.timestamp }]);
      }
    } catch { setMessages(p => [...p, { role: "assistant", content: "Sorry, I couldn't process that. Please try again.", ts: new Date().toISOString() }]); }
    setLoading(false);
  };

  return (
    <div className="h-[calc(100vh-8rem)] flex flex-col">
      <div className="mb-4">
        <h1 className="text-3xl font-bold flex items-center gap-2"><Bot className="h-8 w-8 text-primary" /> AI Financial Advisor</h1>
        <p className="text-muted-foreground">Intelligent chatbot with NLP intent detection & financial reasoning</p>
      </div>

      <Card className="flex-1 flex flex-col overflow-hidden glass-card">
        {/* Messages */}
        <div className="flex-1 overflow-y-auto p-4 space-y-4 chat-scroll">
          <AnimatePresence>
            {messages.map((m, i) => (
              <motion.div key={i} initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className={`flex gap-3 ${m.role === 'user' ? 'justify-end' : ''}`}>
                {m.role === 'assistant' && (
                  <div className="h-8 w-8 rounded-full gradient-bg flex items-center justify-center flex-shrink-0 mt-1">
                    <Brain className="h-4 w-4 text-white" />
                  </div>
                )}
                <div className={`max-w-[75%] ${m.role === 'user' ? 'bg-primary text-primary-foreground rounded-2xl rounded-tr-sm px-4 py-3' : 'bg-muted/80 rounded-2xl rounded-tl-sm px-4 py-3'}`}>
                  <div className="text-sm prose prose-sm dark:prose-invert max-w-none [&>p]:mb-1 [&>ul]:my-1 [&>ol]:my-1">
                    <ReactMarkdown>{m.content}</ReactMarkdown>
                  </div>
                  {m.suggestions && m.suggestions.length > 0 && (
                    <div className="flex flex-wrap gap-1.5 mt-3 pt-2 border-t border-border/50">
                      {m.suggestions.map((s, j) => (
                        <button key={j} onClick={() => send(s)} className="text-xs px-2.5 py-1 rounded-full border bg-background/80 hover:bg-primary/10 hover:border-primary/30 transition-colors text-foreground">
                          {s}
                        </button>
                      ))}
                    </div>
                  )}
                </div>
                {m.role === 'user' && (
                  <div className="h-8 w-8 rounded-full bg-secondary flex items-center justify-center flex-shrink-0 mt-1">
                    <User className="h-4 w-4" />
                  </div>
                )}
              </motion.div>
            ))}
          </AnimatePresence>

          {loading && (
            <div className="flex gap-3">
              <div className="h-8 w-8 rounded-full gradient-bg flex items-center justify-center flex-shrink-0">
                <Brain className="h-4 w-4 text-white" />
              </div>
              <div className="bg-muted/80 rounded-2xl rounded-tl-sm px-4 py-3 flex gap-1">
                <div className="h-2 w-2 rounded-full bg-muted-foreground typing-dot" />
                <div className="h-2 w-2 rounded-full bg-muted-foreground typing-dot" />
                <div className="h-2 w-2 rounded-full bg-muted-foreground typing-dot" />
              </div>
            </div>
          )}
          <div ref={endRef} />
        </div>

        {/* Suggested Prompts */}
        {messages.length <= 1 && (
          <div className="px-4 pb-2">
            <div className="flex items-center gap-1.5 text-xs text-muted-foreground mb-2"><Sparkles className="h-3 w-3" /> Suggested</div>
            <div className="flex flex-wrap gap-2">
              {PROMPTS.map((p, i) => (
                <button key={i} onClick={() => send(p)} className="text-sm px-3 py-1.5 rounded-full border bg-muted/50 hover:bg-primary/10 hover:border-primary/30 transition-colors">{p}</button>
              ))}
            </div>
          </div>
        )}

        {/* Input */}
        <div className="p-4 border-t bg-background/80 backdrop-blur">
          <form onSubmit={e => { e.preventDefault(); send(); }} className="flex gap-2">
            <Input placeholder="Ask about loans, credit, budgets, risks..." value={input} onChange={e => setInput(e.target.value)} className="flex-1 h-11" disabled={loading} />
            <Button type="submit" size="icon" className="h-11 w-11 rounded-full" disabled={loading || !input.trim()}>
              <Send className="h-4 w-4" />
            </Button>
          </form>
        </div>
      </Card>
    </div>
  );
}
