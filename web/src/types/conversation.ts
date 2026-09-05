export interface Conversation {
    id: string;
    title: string;
    createdAt: string;
}

export type MessageRole = "user" | "assistant";

export interface Citation {
    documentId: string;
    filename: string;
    chunkText: string;
}

export interface Message {
    id: string;
    role: MessageRole;
    content: string;
    citations: Citation[];
    createdAt: string;
}
