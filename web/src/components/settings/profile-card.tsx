"use client";

import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Badge } from "@/components/ui/badge";
import { useProfile } from "@/hooks/use-profile";

export function ProfileCard() {
    const { data: profile, isLoading } = useProfile();

    if (isLoading) return <Skeleton className="h-24 w-full" />;

    return (
        <Card>
            <CardContent className="divide-y p-0">
                <div className="flex items-center justify-between p-4">
                    <div>
                        <div className="text-sm font-medium">Email</div>
                        <div className="font-mono text-xs text-muted-foreground">{profile?.email}</div>
                    </div>
                    <Badge variant="secondary" className="text-[10px]">
                        Verified
                    </Badge>
                </div>
                <div className="p-4">
                    <div className="text-sm font-medium">Member since</div>
                    <div className="font-mono text-xs text-muted-foreground">
                        {profile ? new Date(profile.createdAt).toLocaleDateString() : ""}
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}
