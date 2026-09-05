"use client";

import { useQuery } from "@tanstack/react-query";
import { api } from "@/lib/api-client";
import type { Profile } from "@/types/auth";

export function useProfile() {
    return useQuery({
        queryKey: ["profile"],
        queryFn: () => api.get<Profile>("/api/profile"),
    });
}
