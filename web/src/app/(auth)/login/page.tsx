"use client";

import { useState, type FormEvent } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Field, FieldGroup, FieldLabel, FieldDescription } from "@/components/ui/field";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Spinner } from "@/components/ui/spinner";
import { WarningCircle } from "@phosphor-icons/react";
import { useLogin } from "@/hooks/use-auth";

export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const login = useLogin();

    function onSubmit(e: FormEvent) {
        e.preventDefault();
        login.mutate({ email, password });
    }

    return (
        <div className="flex min-h-svh items-center justify-center bg-background px-4">
            <Card className="w-full max-w-sm">
                <CardHeader className="space-y-1 text-center">
                    <CardTitle className="text-base font-medium">Sign in</CardTitle>
                    <CardDescription>Access your personal knowledge base</CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={onSubmit} className="space-y-4">
                        <FieldGroup>
                            <Field>
                                <FieldLabel htmlFor="email">Email</FieldLabel>
                                <Input id="email" type="email" required value={email} onChange={(e) => setEmail(e.target.value)} />
                            </Field>
                            <Field>
                                <FieldLabel htmlFor="password">Password</FieldLabel>
                                <Input
                                    id="password"
                                    type="password"
                                    required
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                            </Field>
                        </FieldGroup>

                        {login.isError && (
                            <Alert variant="destructive">
                                <WarningCircle weight="fill" className="h-4 w-4" />
                                <AlertTitle>Couldn't sign in</AlertTitle>
                                <AlertDescription>Check your email and password and try again.</AlertDescription>
                            </Alert>
                        )}

                        <Button type="submit" className="w-full" disabled={login.isPending}>
                            {login.isPending && <Spinner className="mr-2 h-4 w-4" />}
                            Sign in
                        </Button>
                    </form>

                    <FieldDescription className="mt-4 text-center">
                        No account?{" "}
                        <Link href="/register" className="underline underline-offset-4">
                            Create one
                        </Link>
                    </FieldDescription>
                </CardContent>
            </Card>
        </div>
    );
}
