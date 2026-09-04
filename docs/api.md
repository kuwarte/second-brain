# API

Base URL: `http://localhost:8080`

## Auth

| Method | Route | Body |
|---|---|---|
| POST | `/api/auth/register` | `{ email, password }` |
| POST | `/api/auth/login` | `{ email, password }` |

## Documents

_Requires `Authorization: Bearer <token>`._

| Method | Route | Notes |
|---|---|---|
| POST | `/api/documents` | multipart file upload |
| GET | `/api/documents` | list |
| GET | `/api/documents/{id}` | detail + status |
| DELETE | `/api/documents/{id}` | remove |

## Chat

_Requires `Authorization: Bearer <token>`._

| Method | Route | Notes |
|---|---|---|
| POST | `/api/conversations` | create a conversation |
| GET | `/api/conversations` | list |
| GET | `/api/conversations/{id}/messages` | message history |
| POST | `/api/conversations/{id}/messages` | ask a question, get an answer + citations |
| DELETE | `/api/conversations/{id}` | remove |

_TODO: fill in exact response shapes once controllers are implemented._
