"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import {
    Sidebar,
    SidebarContent,
    SidebarFooter,
    SidebarGroup,
    SidebarGroupContent,
    SidebarGroupLabel,
    SidebarHeader,
    SidebarMenu,
    SidebarMenuAction,
    SidebarMenuButton,
    SidebarMenuItem,
    useSidebar,
} from "@/components/ui/sidebar";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { ThemeToggle } from "@/components/theme-toggle";
import { useConversations, useCreateConversation, useDeleteConversation } from "@/hooks/use-conversations";
import { useLogout } from "@/hooks/use-auth";
import { Plus, ChatCircleDots, FileText, GearSix, DotsThree, Trash, SignOut } from "@phosphor-icons/react";

function BrandTrigger() {
    const { toggleSidebar } = useSidebar();
    return (
        <button
            onClick={toggleSidebar}
            className="flex w-full items-center rounded-md px-2 py-1.5 text-sm font-semibold hover:bg-sidebar-accent group-data-[collapsible=icon]:justify-center group-data-[collapsible=icon]:px-0"
        >
            <span className="group-data-[collapsible=icon]:hidden">2ndBrain</span>
            <span className="hidden group-data-[collapsible=icon]:inline">2b</span>
        </button>
    );
}

export function AppSidebar() {
    const pathname = usePathname();
    const router = useRouter();
    const { data: conversations, isLoading } = useConversations();
    const createConversation = useCreateConversation();
    const deleteConversation = useDeleteConversation();
    const logout = useLogout();

    async function handleNewConversation() {
        const conversation = await createConversation.mutateAsync(undefined);
        router.push(`/?c=${conversation.id}`);
    }

    return (
        <Sidebar collapsible="icon">
            <SidebarHeader className="px-2 py-3">
                <BrandTrigger />
            </SidebarHeader>

            <SidebarContent>
                <SidebarGroup>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={handleNewConversation}
                        disabled={createConversation.isPending}
                        className="mx-2 h-8 w-[calc(100%-1rem)] shrink-0 justify-start gap-2 group-data-[collapsible=icon]:relative group-data-[collapsible=icon]:z-20 group-data-[collapsible=icon]:mx-auto group-data-[collapsible=icon]:size-8 group-data-[collapsible=icon]:w-8 group-data-[collapsible=icon]:justify-center group-data-[collapsible=icon]:p-0"
                    >
                        <Plus className="h-4 w-4 shrink-0" />
                        <span className="group-data-[collapsible=icon]:hidden">New conversation</span>
                    </Button>
                </SidebarGroup>

                <SidebarGroup>
                    <SidebarGroupLabel>Conversations</SidebarGroupLabel>
                    <SidebarGroupContent>
                        <SidebarMenu>
                            {isLoading &&
                                Array.from({ length: 5 }).map((_, i) => (
                                    <SidebarMenuItem key={i}>
                                        <Skeleton className="h-8 w-full" />
                                    </SidebarMenuItem>
                                ))}

                            {conversations?.map((conversation) => (
                                <SidebarMenuItem key={conversation.id}>
                                    <SidebarMenuButton
                                        tooltip={conversation.title || "Untitled"}
                                        render={<Link href={`/?c=${conversation.id}`} />}
                                    >
                                        <ChatCircleDots className="h-4 w-4" />
                                        <span className="truncate">{conversation.title || "Untitled"}</span>
                                    </SidebarMenuButton>
                                    <DropdownMenu>
                                        <DropdownMenuTrigger render={<SidebarMenuAction />}>
                                            <DotsThree className="h-4 w-4" />
                                        </DropdownMenuTrigger>
                                        <DropdownMenuContent side="right" align="start">
                                            <DropdownMenuItem
                                                variant="destructive"
                                                onClick={() => deleteConversation.mutate(conversation.id)}
                                            >
                                                <Trash className="mr-2 h-4 w-4" />
                                                Delete
                                            </DropdownMenuItem>
                                        </DropdownMenuContent>
                                    </DropdownMenu>
                                </SidebarMenuItem>
                            ))}
                        </SidebarMenu>
                    </SidebarGroupContent>
                </SidebarGroup>
            </SidebarContent>

            <SidebarFooter>
                <SidebarMenu>
                    <SidebarMenuItem>
                        <SidebarMenuButton
                            isActive={pathname === "/documents"}
                            tooltip="Documents"
                            render={<Link href="/documents" />}
                        >
                            <FileText className="h-4 w-4" />
                            Documents
                        </SidebarMenuButton>
                    </SidebarMenuItem>
                    <SidebarMenuItem>
                        <SidebarMenuButton
                            isActive={pathname === "/settings"}
                            tooltip="Settings"
                            render={<Link href="/settings" />}
                        >
                            <GearSix className="h-4 w-4" />
                            Settings
                        </SidebarMenuButton>
                    </SidebarMenuItem>
                </SidebarMenu>

                <div className="flex items-center justify-between px-2 py-2 group-data-[collapsible=icon]:flex-col group-data-[collapsible=icon]:gap-2">
                    <ThemeToggle />
                    <Button variant="ghost" size="icon" aria-label="Sign out" onClick={logout}>
                        <SignOut className="h-4 w-4" />
                    </Button>
                </div>
            </SidebarFooter>
        </Sidebar>
    );
}
