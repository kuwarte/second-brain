export interface Preferences {
    theme: "light" | "dark" | "system";
    chatModel: string;
    embeddingModel: string;
    chunkSize: number;
}

export type PreferencesUpdate = Partial<Preferences>;
