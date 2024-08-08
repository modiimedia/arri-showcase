import { createAppDefinition } from "arri";
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

export default createAppDefinition({
    procedures: {
        "users.getUser": {
            transport: "http",
            path: "/users/get-user",
            method: "get",
            params: UserParams,
            response: User,
        },
        "users.createUser": {
            transport: "http",
            path: "/users/create-user",
            method: "post",
            params: a.omit(User, ["id"], { id: "CreateUserParams" }),
            response: User,
        },
        "users.deleteUser": {
            transport: "http",
            path: "/users/delete-user",
            method: "delete",
            params: UserParams,
        },
    },
});
