"use client";

import { useState } from "react";
import { QueryClientProvider } from "@tanstack/react-query";
import { ThemeProvider } from "next-themes";
import { TooltipProvider } from "@/components/ui/tooltip";
import { Toaster } from "@/components/ui/sonner";
import { makeQueryClient } from "@/lib/query-client";

export function Providers({ children }: { children: React.ReactNode }) {
    const [queryClient] = useState(makeQueryClient);

    return (
        <QueryClientProvider client={queryClient}>
            <ThemeProvider attribute="class" defaultTheme="system" enableSystem disableTransitionOnChange>
                <TooltipProvider>
                    {children}
                    <Toaster />
                </TooltipProvider>
            </ThemeProvider>
        </QueryClientProvider>
    );
}
