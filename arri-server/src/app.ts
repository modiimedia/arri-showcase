import { ArriApp, handleCors } from "@arrirpc/server";

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

export default app;
