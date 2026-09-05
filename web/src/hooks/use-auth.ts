"use client";

import { useMutation } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { api } from "@/lib/api-client";
import { setStoredToken, clearStoredToken } from "@/lib/auth";
import type { AuthResponse, LoginPayload, RegisterPayload } from "@/types/auth";

export function useLogin() {
    const router = useRouter();
    return useMutation({
        mutationFn: (payload: LoginPayload) => api.post<AuthResponse>("/api/auth/login", payload),
        onSuccess: (data) => {
            setStoredToken(data.token);
            router.push("/");
        },
    });
}

export function useRegister() {
    const router = useRouter();
    return useMutation({
        mutationFn: (payload: RegisterPayload) => api.post<AuthResponse>("/api/auth/register", payload),
        onSuccess: (data) => {
            setStoredToken(data.token);
            router.push("/");
        },
    });
}

export function useLogout() {
    const router = useRouter();
    return () => {
        clearStoredToken();
        router.push("/login");
    };
}
