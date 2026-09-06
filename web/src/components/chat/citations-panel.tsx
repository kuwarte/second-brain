"use client";

import { useState } from "react";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible";
import { HoverCard, HoverCardContent, HoverCardTrigger } from "@/components/ui/hover-card";
import { Badge } from "@/components/ui/badge";
import { CaretDown, FileText } from "@phosphor-icons/react";
import type { Citation } from "@/types/conversation";

export function CitationsPanel({ citations }: { citations: Citation[] }) {
    const [open, setOpen] = useState(false);

    return (
        <Collapsible open={open} onOpenChange={setOpen} className="w-full">
            <CollapsibleTrigger className="flex items-center gap-1.5 font-mono text-[11px] uppercase tracking-wide text-muted-foreground hover:text-foreground">
                <CaretDown className={`h-3 w-3 transition-transform ${open ? "rotate-180" : ""}`} />
                Sources · {citations.length}
            </CollapsibleTrigger>
            <CollapsibleContent className="mt-1.5 rounded-md border">
                {citations.map((citation, i) => (
                    <HoverCard key={i} openDelay={150}>
                        <HoverCardTrigger asChild>
                            <div className="flex cursor-default items-center gap-2 border-b px-3 py-2 text-xs last:border-b-0 hover:bg-muted/50">
                                <FileText className="h-3.5 w-3.5 shrink-0 text-muted-foreground" />
                                <span className="truncate font-medium">{citation.filename}</span>
                                <Badge variant="secondary" className="ml-auto font-mono text-[10px]">
                                    {String(i + 1).padStart(2, "0")}
                                </Badge>
                            </div>
                        </HoverCardTrigger>
                        <HoverCardContent className="w-72 text-xs leading-relaxed">{citation.chunkText}</HoverCardContent>
                    </HoverCard>
                ))}
            </CollapsibleContent>
        </Collapsible>
    );
}
