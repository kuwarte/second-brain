export interface AuthResponse {
    token: string;
    expiresAt: string;
}

export interface LoginPayload {
    email: string;
    password: string;
}

export type RegisterPayload = LoginPayload;

export interface Profile {
    id: string;
    email: string;
    createdAt: string;
}
