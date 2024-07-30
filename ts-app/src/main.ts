import { MyClient } from "./client.g";

async function main() {
    const client = new MyClient({
        baseUrl: "http://localhost:3000",
    });
    const result = await client.sayHello({ name: "John" });
    console.log(result.message);
}

main();
