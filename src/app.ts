import {
    ArriApp,
    defineError,
    defineRpc,
    defineService,
    handleCors,
} from "@arrirpc/server";
import { User, UserParams } from "./models";
import { a } from "@arrirpc/schema";

const app = new ArriApp({
    appInfo: {
        version: "1",
    },
    onRequest(event) {
        handleCors(event, {
            origin: "*",
        });
    },
});

const userService = defineService("users", {
    getUser: defineRpc({
        params: UserParams,
        response: User,
        handler() {
            throw defineError(500, {
                message: "Not implemented",
            });
        },
    }),
    createUser: defineRpc({
        params: a.omit(User, ["id"], { id: "CreateUserParams" }),
        response: User,
        handler() {
            throw defineError(500, {
                message: "Not implemented",
            });
        },
    }),
    deleteUser: defineRpc({
        params: UserParams,
        response: undefined,
        handler() {
            throw defineError(500, {
                message: "Not implemented",
            });
        },
    }),
});

app.use(userService);

export default app;
