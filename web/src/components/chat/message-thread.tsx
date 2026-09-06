"use client";

import { useEffect, useRef } from "react";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Skeleton } from "@/components/ui/skeleton";
import { MessageBubble } from "@/components/chat/message-bubble";
import { useMessages } from "@/hooks/use-messages";

export function MessageThread({ conversationId }: { conversationId: string }) {
    const { data: messages, isLoading } = useMessages(conversationId);
    const bottomRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages?.length]);

    return (
        <ScrollArea className="flex-1">
            <div className="mx-auto flex max-w-2xl flex-col gap-6 px-4 py-6">
                {isLoading && Array.from({ length: 3 }).map((_, i) => <Skeleton key={i} className="h-16 w-full" />)}
                {messages?.map((message) => (
                    <MessageBubble key={message.id} message={message} />
                ))}
                <div ref={bottomRef} />
            </div>
        </ScrollArea>
    );
}
