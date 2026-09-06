"use client";

import { flexRender, getCoreRowModel, getPaginationRowModel, useReactTable, type ColumnDef } from "@tanstack/react-table";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Pagination, PaginationContent, PaginationItem } from "@/components/ui/pagination";
import { Button } from "@/components/ui/button";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Empty, EmptyDescription, EmptyHeader, EmptyMedia, EmptyTitle } from "@/components/ui/empty";
import { StatusBadge } from "@/components/documents/status-badge";
import { useDeleteDocument } from "@/hooks/use-documents";
import { FileText, Trash } from "@phosphor-icons/react";
import type { DocumentSummary } from "@/types/document";

function RowActions({ document }: { document: DocumentSummary }) {
    const deleteDocument = useDeleteDocument();
    return (
        <AlertDialog>
            <AlertDialogTrigger
                render={<Button variant="ghost" size="icon" className="h-7 w-7 text-muted-foreground hover:text-destructive" />}
            >
                <Trash className="h-3.5 w-3.5" />
            </AlertDialogTrigger>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Delete “{document.filename}”?</AlertDialogTitle>
                    <AlertDialogDescription>
                        This removes the file and its indexed chunks. Conversations that cited it keep the text but lose the
                        link back to the source.
                    </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                    <AlertDialogAction onClick={() => deleteDocument.mutate(document.id)}>Delete</AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    );
}

const columns: ColumnDef<DocumentSummary>[] = [
    {
        accessorKey: "filename",
        header: "Filename",
        cell: ({ row }) => (
            <div className="flex items-center gap-2">
                <FileText className="h-4 w-4 shrink-0 text-muted-foreground" />
                <span className="font-medium">{row.original.filename}</span>
            </div>
        ),
    },
    { accessorKey: "status", header: "Status", cell: ({ row }) => <StatusBadge status={row.original.status} /> },
    {
        accessorKey: "uploadedAt",
        header: "Uploaded",
        cell: ({ row }) => (
            <span className="font-mono text-xs text-muted-foreground">
                {new Date(row.original.uploadedAt).toLocaleDateString()}
            </span>
        ),
    },
    { id: "actions", header: "", cell: ({ row }) => <RowActions document={row.original} /> },
];

export function DocumentsTable({ documents }: { documents: DocumentSummary[] }) {
    const table = useReactTable({
        data: documents,
        columns,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel(),
        initialState: { pagination: { pageSize: 10 } },
    });

    if (documents.length === 0) {
        return (
            <Empty>
                <EmptyHeader>
                    <EmptyMedia variant="icon">
                        <FileText className="h-6 w-6" />
                    </EmptyMedia>
                    <EmptyTitle>No documents yet</EmptyTitle>
                    <EmptyDescription>Upload a file to start building your knowledge base.</EmptyDescription>
                </EmptyHeader>
            </Empty>
        );
    }

    return (
        <div className="space-y-3">
            <div className="rounded-md border">
                <Table>
                    <TableHeader>
                        {table.getHeaderGroups().map((hg) => (
                            <TableRow key={hg.id}>
                                {hg.headers.map((header) => (
                                    <TableHead key={header.id}>{flexRender(header.column.columnDef.header, header.getContext())}</TableHead>
                                ))}
                            </TableRow>
                        ))}
                    </TableHeader>
                    <TableBody>
                        {table.getRowModel().rows.map((row) => (
                            <TableRow key={row.id}>
                                {row.getVisibleCells().map((cell) => (
                                    <TableCell key={cell.id}>{flexRender(cell.column.columnDef.cell, cell.getContext())}</TableCell>
                                ))}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>

            {table.getPageCount() > 1 && (
                <Pagination>
                    <PaginationContent>
                        <PaginationItem>
                            <Button variant="outline" size="sm" onClick={() => table.previousPage()} disabled={!table.getCanPreviousPage()}>
                                Previous
                            </Button>
                        </PaginationItem>
                        <PaginationItem>
                            <Button variant="outline" size="sm" onClick={() => table.nextPage()} disabled={!table.getCanNextPage()}>
                                Next
                            </Button>
                        </PaginationItem>
                    </PaginationContent>
                </Pagination>
            )}
        </div>
    );
}
