"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api-client";
import type { Message } from "@/types/conversation";

export function useMessages(conversationId: string | null) {
    return useQuery({
        queryKey: ["messages", conversationId],
        queryFn: () => api.get<Message[]>(`/api/conversations/${conversationId}/messages`),
        enabled: !!conversationId,
    });
}

export function useSendMessage(conversationId: string | null) {
    const qc = useQueryClient();
    return useMutation({
        mutationFn: (content: string) =>
            api.post<Message>(`/api/conversations/${conversationId}/messages`, { content }),
        onSuccess: () => {
            qc.invalidateQueries({ queryKey: ["messages", conversationId] });
            qc.invalidateQueries({ queryKey: ["conversations"] });
        },
    });
}
