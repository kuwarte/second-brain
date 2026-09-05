export type DocumentStatus = "pending" | "processing" | "ready" | "failed";

export interface DocumentSummary {
    id: string;
    filename: string;
    status: DocumentStatus;
    uploadedAt: string;
}

export interface DocumentDetail extends DocumentSummary {
    chunkCount: number;
}
