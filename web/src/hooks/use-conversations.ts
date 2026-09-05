"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api-client";
import type { Conversation } from "@/types/conversation";

const KEY = ["conversations"];

export function useConversations() {
    return useQuery({
        queryKey: KEY,
        queryFn: () => api.get<Conversation[]>("/api/conversations"),
    });
}

export function useCreateConversation() {
    const qc = useQueryClient();
    return useMutation({
        mutationFn: (title?: string) => api.post<Conversation>("/api/conversations", { title }),
        onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
    });
}

export function useDeleteConversation() {
    const qc = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => api.delete<void>(`/api/conversations/${id}`),
        onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
    });
}
