"use client";

import { useSearchParams } from "next/navigation";
import { SidebarTrigger } from "@/components/ui/sidebar";
import { Separator } from "@/components/ui/separator";
import { Empty, EmptyHeader, EmptyTitle, EmptyDescription, EmptyMedia } from "@/components/ui/empty";
import { ChatCircleDots } from "@phosphor-icons/react";
import { MessageThread } from "@/components/chat/message-thread";
import { Composer } from "@/components/chat/composer";
import { useConversations } from "@/hooks/use-conversations";

export function ChatView() {
    const searchParams = useSearchParams();
    const conversationId = searchParams.get("c");
    const { data: conversations } = useConversations();
    const conversation = conversations?.find((c) => c.id === conversationId);

    if (!conversationId) {
        return (
            <div className="flex h-svh flex-col">
                <div className="flex items-center gap-2 border-b px-4 py-3">
                    <SidebarTrigger className="md:hidden" />
                    <div className="text-sm font-semibold">2ndBrain</div>
                </div>
                <Empty className="flex-1">
                    <EmptyHeader>
                        <EmptyMedia variant="icon">
                            <ChatCircleDots className="h-6 w-6" />
                        </EmptyMedia>
                        <EmptyTitle>No conversation selected</EmptyTitle>
                        <EmptyDescription>Start a new conversation or pick one from the sidebar.</EmptyDescription>
                    </EmptyHeader>
                </Empty>
            </div>
        );
    }

    return (
        <div className="flex h-svh flex-col">
            <div className="flex items-center gap-2 border-b px-4 py-3">
                <SidebarTrigger className="md:hidden" />
                <div className="text-sm font-semibold">{conversation?.title || "Untitled conversation"}</div>
            </div>

            <MessageThread conversationId={conversationId} />
            <Separator />
            <Composer conversationId={conversationId} />
        </div>
    );
}
