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
import { useRegister } from "@/hooks/use-auth";

export default function RegisterPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const register = useRegister();

    function onSubmit(e: FormEvent) {
        e.preventDefault();
        register.mutate({ email, password });
    }

    return (
        <div className="flex min-h-svh items-center justify-center bg-background px-4">
            <Card className="w-full max-w-sm">
                <CardHeader className="space-y-1 text-center">
                    <CardTitle className="text-base font-medium">Create an account</CardTitle>
                    <CardDescription>Start building your knowledge base</CardDescription>
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
                                    minLength={8}
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                            </Field>
                        </FieldGroup>

                        {register.isError && (
                            <Alert variant="destructive">
                                <WarningCircle weight="fill" className="h-4 w-4" />
                                <AlertTitle>Couldn't create your account</AlertTitle>
                                <AlertDescription>That email may already be registered.</AlertDescription>
                            </Alert>
                        )}

                        <Button type="submit" className="w-full" disabled={register.isPending}>
                            {register.isPending && <Spinner className="mr-2 h-4 w-4" />}
                            Create account
                        </Button>
                    </form>

                    <FieldDescription className="mt-4 text-center">
                        Already have an account?{" "}
                        <Link href="/login" className="underline underline-offset-4">
                            Sign in
                        </Link>
                    </FieldDescription>
                </CardContent>
            </Card>
        </div>
    );
}
