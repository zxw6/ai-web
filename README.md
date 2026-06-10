# ai-web
ring Boot 2 + WebClient + JWT GPT Mirror Plan
Summary
This project is a GPT-style web chat application built from scratch with a separated frontend and backend.

Frontend: React + Vite
Backend: Spring Boot 2.7.18
Java: Java 17 recommended, Java 8+ supported by Spring Boot 2.7
Streaming: Spring MVC SseEmitter for browser output
Model client: Spring WebClient for OpenAI streaming requests
Authentication: Spring Security + JWT
Database: MySQL
WebClient is used by the backend to call the model provider. The browser-facing stream is exposed separately through an SSE endpoint.

Project Structure
gpt/
  frontend/                 React + Vite frontend
  backend/                  Spring Boot 2.7 backend
  sql/                      MySQL schema scripts
  docs/                     Planning and design documents
  README.md                 Setup and run instructions
  .env.example              Environment variable example
Backend package layout:

backend/src/main/java/com/example/gpt/
  GptApplication.java

  config/
    SecurityConfig.java
    CorsConfig.java
    OpenAiProperties.java
    JwtProperties.java

  controller/
    AuthController.java
    ChatController.java
    ConversationController.java
    UserController.java

  service/
    AuthService.java
    JwtService.java
    ChatService.java
    OpenAiService.java
    ConversationService.java

  entity/
    User.java
    Conversation.java
    Message.java

  repository/
    UserRepository.java
    ConversationRepository.java
    MessageRepository.java

  dto/
    LoginRequest.java
    RegisterRequest.java
    AuthResponse.java
    ChatRequest.java
    ConversationResponse.java
    MessageResponse.java

  exception/
    GlobalExceptionHandler.java
Frontend package layout:

frontend/src/
  pages/
    LoginPage.tsx
    RegisterPage.tsx
    ChatPage.tsx

  components/
    Sidebar.tsx
    ChatWindow.tsx
    MessageList.tsx
    Composer.tsx

  services/
    authApi.ts
    chatApi.ts
    conversationApi.ts
    tokenStorage.ts
Backend Design
Core dependencies:

spring-boot-starter-web for REST APIs and SseEmitter
spring-boot-starter-webflux for WebClient
spring-boot-starter-security for authentication and authorization
spring-boot-starter-data-jpa for MySQL access
mysql-connector-java for the MySQL driver
jjwt for JWT generation and validation
lombok for reducing boilerplate
Main APIs:

POST   /api/auth/register       Register
POST   /api/auth/login          Login
GET    /api/users/me            Get current user

GET    /api/conversations       List current user's conversations
POST   /api/conversations       Create conversation
GET    /api/conversations/{id}  Get conversation messages
PATCH  /api/conversations/{id}  Rename conversation
DELETE /api/conversations/{id}  Delete conversation

POST   /api/chat/stream         Send a message and stream the assistant reply
Authentication rules:

/api/auth/register and /api/auth/login are public.
All other /api/** endpoints require JWT authentication.
Authenticated requests must include:
Authorization: Bearer <jwt_token>
Chat streaming flow:

Frontend sends the user's message to /api/chat/stream.
Backend identifies the current user from the JWT.
Backend validates that the conversation belongs to the current user.
Backend saves the user message.
Backend calls OpenAI with WebClient.
Backend forwards streamed chunks to the frontend with SseEmitter.
Backend saves the completed assistant message.
Database Design
Core MySQL tables:

users
  id
  username
  password_hash
  nickname
  created_at
  updated_at

conversations
  id
  user_id
  title
  created_at
  updated_at

messages
  id
  conversation_id
  role
  content
  status
  created_at
Constraints:

users.username must be unique.
conversations.user_id references users.id.
messages.conversation_id references conversations.id.
Conversation and message queries must always be scoped by the current user.
Passwords are stored with BCrypt hashes only.
Frontend Design
The first screen should be the actual chat app, not a landing page.

User flow:

Unauthenticated users go to the login page.
Register creates an account and logs the user in automatically.
Login stores the JWT locally.
Chat API requests include the JWT in the Authorization header.
Expired or invalid JWTs clear local auth state and return the user to login.
Chat UI areas:

Sidebar: new chat, conversation list, delete conversation.
Main chat area: user messages, assistant messages, streaming state, error state.
Composer: multiline input, send button, stop generation button.
Settings entry: model, temperature, and system prompt can be added after the core flow works.
Test Plan
Backend checks:

Register and login return a valid JWT.
/api/users/me returns the current user with a valid JWT.
Protected APIs reject missing or invalid JWTs.
Users cannot read, modify, or delete another user's conversations.
Chat saves both user and assistant messages.
Missing OpenAI API key returns a clear error.
Model-provider failure sends an SSE error event.
Frontend checks:

Login and register pages render correctly.
Successful login redirects to the chat page.
JWT is attached to authenticated API calls.
Conversation list loads after login.
Sending a message displays streamed assistant chunks.
Refreshing keeps the user logged in if the JWT is valid.
Expired JWT returns the user to login.
Assumptions
The backend uses Spring Boot 2.7.18, not Spring Boot 3.
The first version uses JWT, not server sessions.
The first version uses MySQL, not SQLite.
WebClient is used for OpenAI calls; SseEmitter is used for browser streaming.
The first version supports username/password login only.
No admin console is included in the first version.
Users can only access their own chat history.
