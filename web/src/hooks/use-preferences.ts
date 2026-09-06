"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api-client";
import type { Preferences, PreferencesUpdate } from "@/types/preferences";

const KEY = ["preferences"];

export function usePreferences() {
    return useQuery({
        queryKey: KEY,
        queryFn: () => api.get<Preferences>("/api/preferences"),
    });
}

export function useUpdatePreferences() {
    const qc = useQueryClient();
    return useMutation({
        mutationFn: (update: PreferencesUpdate) => api.patch<Preferences>("/api/preferences", update),
        onSuccess: (data) => qc.setQueryData(KEY, data),
    });
}
