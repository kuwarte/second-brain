"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api-client";
import type { DocumentDetail, DocumentSummary } from "@/types/document";

const KEY = ["documents"];

export function useDocuments() {
    return useQuery({
        queryKey: KEY,
        queryFn: () => api.get<DocumentSummary[]>("/api/documents"),
        refetchInterval: (query) =>
            query.state.data?.some((d) => d.status === "pending" || d.status === "processing") ? 3000 : false,
    });
}

export function useDocument(id: string | null) {
    return useQuery({
        queryKey: ["documents", id],
        queryFn: () => api.get<DocumentDetail>(`/api/documents/${id}`),
        enabled: !!id,
    });
}

export function useUploadDocument() {
    const qc = useQueryClient();
    return useMutation({
        mutationFn: (file: File) => {
            const formData = new FormData();
            formData.append("file", file);
            return api.upload<DocumentSummary>("/api/documents", formData);
        },
        onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
    });
}

export function useDeleteDocument() {
    const qc = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => api.delete<void>(`/api/documents/${id}`),
        onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
    });
}
