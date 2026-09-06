"use client";

import { toast } from "sonner";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Card, CardContent } from "@/components/ui/card";
import { ButtonGroup } from "@/components/ui/button-group";
import { Button } from "@/components/ui/button";
import { Tooltip, TooltipContent, TooltipTrigger } from "@/components/ui/tooltip";
import { Copy, Robot, User } from "@phosphor-icons/react";
import { CitationsPanel } from "@/components/chat/citations-panel";
import type { Message } from "@/types/conversation";
import { cn } from "@/lib/utils";

export function MessageBubble({ message }: { message: Message }) {
    const isUser = message.role === "user";

    function copyContent() {
        navigator.clipboard.writeText(message.content);
        toast.success("Copied to clipboard");
    }

    return (
        <div className={cn("flex gap-3", isUser && "flex-row-reverse")}>
            <Avatar className="h-7 w-7 shrink-0">
                <AvatarFallback className="text-[10px]">
                    {isUser ? <User weight="bold" className="h-3.5 w-3.5" /> : <Robot weight="fill" className="h-3.5 w-3.5" />}
                </AvatarFallback>
            </Avatar>

            <div className={cn("flex max-w-[85%] flex-col gap-1.5", isUser && "items-end")}>
                <Card className={cn("border-border/80 py-0", isUser && "bg-muted")}>
                    <CardContent className="px-3.5 py-2.5 text-sm leading-relaxed">{message.content}</CardContent>
                </Card>

                {!isUser && message.citations?.length > 0 && <CitationsPanel citations={message.citations} />}

                {!isUser && (
                    <ButtonGroup>
                        <Tooltip>
                            <TooltipTrigger asChild>
                                <Button variant="ghost" size="icon" className="h-7 w-7" onClick={copyContent}>
                                    <Copy className="h-3.5 w-3.5" />
                                </Button>
                            </TooltipTrigger>
                            <TooltipContent>Copy</TooltipContent>
                        </Tooltip>
                    </ButtonGroup>
                )}
            </div>
        </div>
    );
}
