export interface ApiErrorResponse {
    message: string;
    errors?: Array<{ field: string, message: string }>;
}