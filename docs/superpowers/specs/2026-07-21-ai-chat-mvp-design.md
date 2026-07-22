# AI Chat Design

## Goal

Provide a reliable AI assistant for the football club management system. The assistant answers business questions by calling Java-side tools against the real database, then formats deterministic plain-text replies on the backend.

## Current Architecture

```text
AiChatController
  -> AiChatService (orchestration)
       -> AiProviderClient (OpenAI-compatible API)
       -> AiToolDefinitionService (6 function tools)
       -> AiToolExecutor (real DB queries)
       -> AiFallbackRouter (keyword fallback when model misses tool call)
       -> AiReplyFormatter (deterministic backend formatting)
       -> AiExecutionRecorder (tool/params/duration/result logging)
```

## Scope

- Request/response chat endpoint at `/ai/chat`
- Function Calling with 6 tools: players, announcements, matches, training plans, contracts, player data
- Backend deterministic reply formatting (no Markdown / emoji in business replies)
- Fallback routing when the model does not call tools for system-data questions
- Offline evaluation dataset and online evaluation runner
- Public endpoint with IP rate limiting

Not in scope for this phase:

- SSE streaming
- Vue 3 migration
- Microservice split
- Message queue

## Configuration

`application.yml` `ai` section:

- `api-key`: `${DEEPSEEK_API_KEY:}`
- `api-url`: OpenAI-compatible chat completions endpoint
- `model`: default `deepseek-chat`
- `timeout`: default `30000`
- `rate-limit.max-requests`: default `20` per window
- `rate-limit.window-seconds`: default `60`

Database credentials are loaded from environment variables (`DB_USERNAME`, `DB_PASSWORD`, `DB_URL`).

## Error Handling

- Empty message -> `R.error(400, "请输入问题")`
- Missing API key -> friendly setup message
- Provider failure -> short failure message, UI remains usable
- Rate limit exceeded -> `429` business error

## Authentication

`/ai/chat` remains `@IgnoreAuth` for the public portal chat page, but is protected by `AiRateLimitInterceptor`.

## Evaluation

- Offline tests: `src/test/resources/ai/eval-dataset.json` + `AiOfflineEvaluatorTest`
- Online evaluation: `AiOnlineEvaluator` (enable with `ai.eval.online-enabled=true`)

Metrics tracked:

- tool selection accuracy (online)
- parameter extraction accuracy (online)
- fact consistency rate
- hallucination guardrails (must-not-contain assertions)
- fallback trigger rate
- response latency

## Later Extensions

- SSE streaming with `SseEmitter`
- Authenticated AI sessions per role
- Expanded evaluation dashboard
