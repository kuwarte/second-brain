import { Badge } from "@/components/ui/badge";
import type { DocumentStatus } from "@/types/document";
import { cn } from "@/lib/utils";

const LABELS: Record<DocumentStatus, string> = {
    pending: "Pending",
    processing: "Processing",
    ready: "Ready",
    failed: "Failed",
};

export function StatusBadge({ status }: { status: DocumentStatus }) {
    return (
        <Badge
            variant="outline"
            className={cn(
                "gap-1.5 font-mono text-[10px] uppercase tracking-wide",
                status === "ready" && "border-emerald-500/30 text-emerald-600 dark:text-emerald-400",
                status === "failed" && "border-destructive/30 text-destructive",
                status === "processing" && "text-muted-foreground"
            )}
        >
            <span className={cn("h-1.5 w-1.5 rounded-full bg-current", status === "processing" && "animate-pulse")} />
            {LABELS[status]}
        </Badge>
    );
}
