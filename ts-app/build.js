const { buildSync } = require("esbuild");

buildSync({
    entryPoints: ["./src/main.ts"],
    outdir: "dist",
    target: "node20",
    platform: "node",
    bundle: true,
});
