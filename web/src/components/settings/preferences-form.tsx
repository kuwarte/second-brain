"use client";

import { useEffect, useState } from "react";
import { useTheme } from "next-themes";
import { toast } from "sonner";
import { Field, FieldDescription, FieldGroup, FieldLabel } from "@/components/ui/field";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Label } from "@/components/ui/label";
import { Spinner } from "@/components/ui/spinner";
import { Skeleton } from "@/components/ui/skeleton";
import { usePreferences, useUpdatePreferences } from "@/hooks/use-preferences";

const CHAT_MODELS = ["qwen3.5:0.8b", "llama3.2", "llama3.1", "mistral"];
const EMBEDDING_MODELS = ["nomic-embed-text", "mxbai-embed-large"];

export function PreferencesForm() {
    const { data: preferences, isLoading } = usePreferences();
    const update = useUpdatePreferences();
    const { theme, setTheme } = useTheme();
    const [chunkSize, setChunkSize] = useState("512");

    useEffect(() => {
        if (preferences) setChunkSize(String(preferences.chunkSize));
    }, [preferences]);

    if (isLoading) {
        return (
            <div className="space-y-3">
                <Skeleton className="h-16 w-full" />
                <Skeleton className="h-16 w-full" />
                <Skeleton className="h-16 w-full" />
            </div>
        );
    }

    function saveOk() {
        toast.success("Preferences saved");
    }

    return (
        <div className="rounded-md border">
            <FieldGroup className="divide-y">
                <Field orientation="horizontal" className="p-4">
                    <div>
                        <FieldLabel>Chat model</FieldLabel>
                        <FieldDescription>Used to generate answers</FieldDescription>
                    </div>
                    <Select value={preferences?.chatModel} onValueChange={(value) => update.mutate({ chatModel: value }, { onSuccess: saveOk })}>
                        <SelectTrigger className="w-44 font-mono text-xs">
                            <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                            {CHAT_MODELS.map((model) => (
                                <SelectItem key={model} value={model} className="font-mono text-xs">
                                    {model}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </Field>

                <Field orientation="horizontal" className="p-4">
                    <div>
                        <FieldLabel>Embedding model</FieldLabel>
                        <FieldDescription>Used to index new documents</FieldDescription>
                    </div>
                    <Select value={preferences?.embeddingModel} onValueChange={(value) => update.mutate({ embeddingModel: value }, { onSuccess: saveOk })}>
                        <SelectTrigger className="w-44 font-mono text-xs">
                            <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                            {EMBEDDING_MODELS.map((model) => (
                                <SelectItem key={model} value={model} className="font-mono text-xs">
                                    {model}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </Field>

                <Field orientation="horizontal" className="p-4">
                    <div>
                        <FieldLabel>Chunk size</FieldLabel>
                        <FieldDescription>Tokens per chunk on upload</FieldDescription>
                    </div>
                    <Input
                        value={chunkSize}
                        onChange={(e) => setChunkSize(e.target.value)}
                        onBlur={() => {
                            const parsed = Number(chunkSize);
                            if (!Number.isNaN(parsed) && parsed !== preferences?.chunkSize) {
                                update.mutate({ chunkSize: parsed }, { onSuccess: saveOk });
                            }
                        }}
                        className="w-24 font-mono text-xs"
                    />
                </Field>

                <Field orientation="horizontal" className="p-4">
                    <div>
                        <FieldLabel>Theme</FieldLabel>
                        <FieldDescription>Interface appearance</FieldDescription>
                    </div>
                    <RadioGroup value={theme} onValueChange={setTheme} className="flex gap-4">
                        {["light", "dark", "system"].map((option) => (
                            <div key={option} className="flex items-center gap-1.5">
                                <RadioGroupItem value={option} id={option} />
                                <Label htmlFor={option} className="text-xs capitalize">
                                    {option}
                                </Label>
                            </div>
                        ))}
                    </RadioGroup>
                </Field>
            </FieldGroup>

            {update.isPending && (
                <div className="flex items-center gap-2 border-t p-3 text-xs text-muted-foreground">
                    <Spinner className="h-3.5 w-3.5" /> Saving…
                </div>
            )}
        </div>
    );
}
