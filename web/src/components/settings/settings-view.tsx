"use client";

import { SidebarTrigger } from "@/components/ui/sidebar";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { PreferencesForm } from "@/components/settings/preferences-form";
import { ProfileCard } from "@/components/settings/profile-card";

export function SettingsView() {
    return (
        <div className="flex h-svh flex-col">
            <div className="flex items-center gap-2 border-b px-4 py-3">
                <SidebarTrigger className="md:hidden" />
                <div className="text-sm font-semibold">Settings</div>
            </div>

            <div className="mx-auto w-full max-w-xl flex-1 overflow-auto p-4">
                <Tabs defaultValue="preferences">
                    <TabsList>
                        <TabsTrigger value="preferences">Preferences</TabsTrigger>
                        <TabsTrigger value="profile">Profile</TabsTrigger>
                    </TabsList>
                    <TabsContent value="preferences" className="mt-4">
                        <PreferencesForm />
                    </TabsContent>
                    <TabsContent value="profile" className="mt-4">
                        <ProfileCard />
                    </TabsContent>
                </Tabs>
            </div>
        </div>
    );
}
