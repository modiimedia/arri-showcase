#![allow(
    dead_code,
    unused_imports,
    unused_variables,
    unconditional_recursion,
    deprecated
)]
use arri_client::{
    chrono::{DateTime, FixedOffset},
    parsed_arri_request,
    reqwest::{self, Request},
    serde_json::{self, Map},
    sse::{parsed_arri_sse_request, ArriParsedSseRequestOptions, SseController, SseEvent},
    utils::{serialize_date_time, serialize_string},
    ArriClientConfig, ArriClientService, ArriEnum, ArriModel, ArriParsedRequestOptions,
    ArriServerError, EmptyArriModel, InternalArriClientConfig,
};
use std::collections::{BTreeMap, HashMap};

#[derive(Clone)]
pub struct MyClient {
    _config: InternalArriClientConfig,
}

impl ArriClientService for MyClient {
    fn create(config: ArriClientConfig) -> Self {
        Self {
            _config: InternalArriClientConfig::from(config),
        }
    }
    fn update_headers(&self, headers: HashMap<&'static str, String>) {
        let mut unwrapped_headers = self._config.headers.write().unwrap();
        *unwrapped_headers = headers.clone();
    }
}

impl MyClient {
    pub async fn say_hello(
        &self,
        params: SayHelloParams,
    ) -> Result<SayHelloResponse, ArriServerError> {
        parsed_arri_request(
            ArriParsedRequestOptions {
                http_client: &self._config.http_client,
                url: format!("{}/say-hello", &self._config.base_url),
                method: reqwest::Method::POST,
                headers: self._config.headers.clone(),
                client_version: "1".to_string(),
            },
            Some(params),
            |body| return SayHelloResponse::from_json_string(body),
        )
        .await
    }
}

#[derive(Clone, Debug, PartialEq)]
pub struct SayHelloParams {
    pub name: String,
}

impl ArriModel for SayHelloParams {
    fn new() -> Self {
        Self {
            name: "".to_string(),
        }
    }
    fn from_json(input: serde_json::Value) -> Self {
        match input {
            serde_json::Value::Object(_val_) => {
                let name = match _val_.get("name") {
                    Some(serde_json::Value::String(name_val)) => name_val.to_owned(),
                    _ => "".to_string(),
                };
                Self { name }
            }
            _ => Self::new(),
        }
    }
    fn from_json_string(input: String) -> Self {
        match serde_json::from_str(input.as_str()) {
            Ok(val) => Self::from_json(val),
            _ => Self::new(),
        }
    }
    fn to_json_string(&self) -> String {
        let mut _json_output_ = "{".to_string();

        _json_output_.push_str("\"name\":");
        _json_output_.push_str(serialize_string(&self.name).as_str());
        _json_output_.push('}');
        _json_output_
    }
    fn to_query_params_string(&self) -> String {
        let mut _query_parts_: Vec<String> = Vec::new();
        _query_parts_.push(format!("name={}", &self.name));
        _query_parts_.join("&")
    }
}

#[derive(Clone, Debug, PartialEq)]
pub struct SayHelloResponse {
    pub message: String,
}

impl ArriModel for SayHelloResponse {
    fn new() -> Self {
        Self {
            message: "".to_string(),
        }
    }
    fn from_json(input: serde_json::Value) -> Self {
        match input {
            serde_json::Value::Object(_val_) => {
                let message = match _val_.get("message") {
                    Some(serde_json::Value::String(message_val)) => message_val.to_owned(),
                    _ => "".to_string(),
                };
                Self { message }
            }
            _ => Self::new(),
        }
    }
    fn from_json_string(input: String) -> Self {
        match serde_json::from_str(input.as_str()) {
            Ok(val) => Self::from_json(val),
            _ => Self::new(),
        }
    }
    fn to_json_string(&self) -> String {
        let mut _json_output_ = "{".to_string();

        _json_output_.push_str("\"message\":");
        _json_output_.push_str(serialize_string(&self.message).as_str());
        _json_output_.push('}');
        _json_output_
    }
    fn to_query_params_string(&self) -> String {
        let mut _query_parts_: Vec<String> = Vec::new();
        _query_parts_.push(format!("message={}", &self.message));
        _query_parts_.join("&")
    }
}
