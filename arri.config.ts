import { defineConfig, generators } from "arri";

export default defineConfig({
    generators: [
        generators.typescriptClient({
            clientName: "Client",
            outputFile: "./Client.ts",
        }),
        generators.dartClient({
            clientName: "Client",
            outputFile: "./Client.dart",
        }),
    ],
});
