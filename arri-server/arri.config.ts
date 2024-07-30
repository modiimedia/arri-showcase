import { defineConfig, generators } from "arri";

export default defineConfig({
    entry: "app.ts",
    port: 3000,
    generators: [
        generators.typescriptClient({
            clientName: "MyClient",
            outputFile: "../ts-app/src/client.g.ts",
        }),
        generators.dartClient({
            clientName: "MyClient",
            outputFile: "../dart-app/lib/client.g.dart",
        }),
        generators.rustClient({
            clientName: "MyClient",
            outputFile: "../rust-app/src/client.g.rs",
        }),
        generators.kotlinClient({
            clientName: "MyClient",
            outputFile: "../kotlin-app/src/main/kotlin/Client.g.kt",
        }),
    ],
});
