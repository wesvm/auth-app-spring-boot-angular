export interface UserInfoResponse {
    email: string;
    name: string;
    profileImage: string;
    mfaEnabled?: boolean;
    roles?: string;
}