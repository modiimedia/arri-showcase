import { a } from "@arrirpc/schema";

export const User = a.object("User", {
    id: a.string(),
    name: a.string(),
    email: a.nullable(a.string()),
    createdAt: a.timestamp(),
    role: a.enumerator(["STANDARD", "ADMIN", "MODERATOR"]),
});
export type User = a.infer<typeof User>;

export const UserParams = a.object("GetUserParams", {
    userId: a.string(),
});
export type UserParams = a.infer<typeof UserParams>;

export const CreateUserParams = a.omit(User, ["id"], {
    id: "CreateUserParams",
});
export type CreateUserParams = a.infer<typeof CreateUserParams>;
