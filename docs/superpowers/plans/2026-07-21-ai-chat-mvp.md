# AI Chat MVP Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a working AI chat MVP to the existing football club management system.

**Architecture:** Add a Spring MVC controller at `/ai/chat`, a service that calls an OpenAI-compatible chat completions API through Hutool, and a front-end chat page that posts messages and renders replies. Keep this first version non-streaming and dependency-light.

**Tech Stack:** Spring Boot 2.2.2, Java 8, Hutool HTTP, FastJSON, static HTML, Vue 2 CDN, Layui.

---

### Task 1: Backend Chat Endpoint

**Files:**
- Create: `src/main/java/com/service/AiChatService.java`
- Create: `src/main/java/com/controller/AiChatController.java`
- Modify: `src/main/resources/application.yml`

- [ ] **Step 1:** Create `AiChatService` with `public String chat(String message)`.
- [ ] **Step 2:** Return a setup hint when `ai.api-key` is blank.
- [ ] **Step 3:** Send `model`, `messages`, and `temperature` to the configured chat completions URL.
- [ ] **Step 4:** Parse `choices[0].message.content` from the provider response.
- [ ] **Step 5:** Create `POST /ai/chat` with `@IgnoreAuth`.
- [ ] **Step 6:** Return `R.error(400, "请输入问题")` for blank input and `R.ok().put("data", {"reply": "..."})` for normal replies.
- [ ] **Step 7:** Add `ai.api-key`, `ai.api-url`, `ai.model`, and `ai.timeout` to `application.yml`.

### Task 2: Front-End Chat Page

**Files:**
- Modify: `src/main/resources/front/front/pages/chat/chat.html`

- [ ] **Step 1:** Replace the disabled placeholder with a working chat UI.
- [ ] **Step 2:** Use `fetch` to post JSON to `${contextPath}/ai/chat`.
- [ ] **Step 3:** Render user messages, assistant replies, loading state, and errors.

### Task 3: Run Check

- [ ] **Step 1:** Run `mvn -q -DskipTests package`.
- [ ] **Step 2:** Start with `mvn spring-boot:run`.
- [ ] **Step 3:** Open `/zuqiujulebguanli/front/pages/chat/chat.html` and verify the page renders.
- [ ] **Step 4:** Verify missing API key returns the setup hint; configured API key returns a model reply.
