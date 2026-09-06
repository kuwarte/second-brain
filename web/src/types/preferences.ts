export type ChatProvider = "OLLAMA" | "GROQ";

export interface Preferences {
    theme: "light" | "dark" | "system";
    chatProvider: ChatProvider;
    chatModel: string;
    embeddingModel: string;
    chunkSize: number;
}

export type PreferencesUpdate = Partial<Preferences>;
