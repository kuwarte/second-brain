"use client";

import { useRef, useState, type DragEvent } from "react";
import { toast } from "sonner";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { UploadSimple } from "@phosphor-icons/react";
import { useUploadDocument } from "@/hooks/use-documents";
import { cn } from "@/lib/utils";

export function UploadDropzone() {
    const [open, setOpen] = useState(false);
    const [dragging, setDragging] = useState(false);
    const inputRef = useRef<HTMLInputElement>(null);
    const upload = useUploadDocument();

    async function handleFiles(files: FileList | null) {
        const file = files?.[0];
        if (!file) return;
        try {
            await upload.mutateAsync(file);
            toast.success(`${file.name} uploaded`);
            setOpen(false);
        } catch {
            toast.error(`Couldn't upload ${file.name}`);
        }
    }

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger render={<Button variant="outline" size="sm" className="gap-2" />}>
                <UploadSimple className="h-4 w-4" />
                Upload document
            </DialogTrigger>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>Upload a document</DialogTitle>
                    <DialogDescription>PDF, Markdown, Word, or plain text — it'll be chunked and indexed automatically.</DialogDescription>
                </DialogHeader>

                <div
                    onDragOver={(e: DragEvent<HTMLDivElement>) => {
                        e.preventDefault();
                        setDragging(true);
                    }}
                    onDragLeave={() => setDragging(false)}
                    onDrop={(e: DragEvent<HTMLDivElement>) => {
                        e.preventDefault();
                        setDragging(false);
                        handleFiles(e.dataTransfer.files);
                    }}
                    onClick={() => inputRef.current?.click()}
                    className={cn(
                        "flex cursor-pointer flex-col items-center gap-2 rounded-md border border-dashed p-8 text-center text-sm text-muted-foreground transition-colors",
                        dragging && "border-primary bg-muted/50"
                    )}
                >
                    <UploadSimple className="h-6 w-6" />
                    <span>Drag a file here, or click to browse</span>
                </div>

                <input ref={inputRef} type="file" className="hidden" onChange={(e) => handleFiles(e.target.files)} />
            </DialogContent>
        </Dialog>
    );
}
