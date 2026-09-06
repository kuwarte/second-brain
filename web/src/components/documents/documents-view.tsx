"use client";

import { SidebarTrigger } from "@/components/ui/sidebar";
import { UploadDropzone } from "@/components/documents/upload-dropzone";
import { DocumentsTable } from "@/components/documents/documents-table";
import { useDocuments } from "@/hooks/use-documents";

export function DocumentsView() {
    const { data: documents } = useDocuments();
    const readyCount = documents?.filter((d) => d.status === "ready").length ?? 0;

    return (
        <div className="flex h-svh flex-col">
            <div className="flex items-center justify-between gap-3 border-b px-4 py-3">
                <div className="flex items-center gap-2">
                    <SidebarTrigger className="md:hidden" />
                    <div>
                        <div className="text-sm font-semibold">Documents</div>
                        <div className="font-mono text-[11px] text-muted-foreground">
                            {documents?.length ?? 0} files · {readyCount} ready
                        </div>
                    </div>
                </div>
                <UploadDropzone />
            </div>

            <div className="flex-1 overflow-auto p-4">
                <DocumentsTable documents={documents ?? []} />
            </div>
        </div>
    );
}
