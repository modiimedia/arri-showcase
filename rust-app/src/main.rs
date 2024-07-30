use std::collections::HashMap;

use arri_client::{reqwest, ArriClientConfig, ArriClientService};
use client::{MyClient, SayHelloParams};

#[path = "client.g.rs"]
mod client;

#[tokio::main]
async fn main() {
    let client = MyClient::create(ArriClientConfig {
        http_client: reqwest::Client::new(),
        base_url: "http://localhost:3000".to_string(),
        headers: HashMap::new(),
    });
    let result = client
        .say_hello(SayHelloParams {
            name: "John".to_string(),
        })
        .await;
    println!("{}", result.unwrap().message);
}
