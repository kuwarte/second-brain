"use client";

import { useState, type KeyboardEvent } from "react";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { PaperPlaneRight } from "@phosphor-icons/react";
import { useSendMessage } from "@/hooks/use-messages";

export function Composer({ conversationId }: { conversationId: string }) {
    const [value, setValue] = useState("");
    const sendMessage = useSendMessage(conversationId);

    function handleSend() {
        const content = value.trim();
        if (!content || sendMessage.isPending) return;
        setValue("");
        sendMessage.mutate(content);
    }

    function handleKeyDown(e: KeyboardEvent<HTMLTextAreaElement>) {
        if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault();
            handleSend();
        }
    }

    return (
        <div className="px-4 py-3">
            <div className="mx-auto flex max-w-2xl items-center gap-2 rounded-none border bg-background p-2">
                <Textarea
                    value={value}
                    onChange={(e) => setValue(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder="Ask 2ndBrain something…"
                    rows={1}
                    className="min-h-9 resize-none rounded-none border-0 shadow-none focus-visible:ring-0"
                />
                <Button size="icon" className="rounded-none" onClick={handleSend} disabled={sendMessage.isPending || !value.trim()}>
                    {sendMessage.isPending ? <Spinner className="h-4 w-4" /> : <PaperPlaneRight className="h-4 w-4" />}
                </Button>
            </div>
        </div>
    );
}
