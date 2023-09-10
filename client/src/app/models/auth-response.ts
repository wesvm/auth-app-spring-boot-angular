export interface AuthResponse {
    mfa_enabled?: boolean;
    access_token: string;
    refresh_token?: string;
}