import { a } from "@arrirpc/schema";
import { defineRpc } from "@arrirpc/server";

export default defineRpc({
    method: "get",
    params: a.object("SayHelloParams", {
        name: a.string(),
    }),
    response: a.object("SayHelloResponse", {
        message: a.string(),
    }),
    handler({ params }) {
        return {
            message: `Hello ${params.name}`,
        };
    },
});
