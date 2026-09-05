# API

Base URL: `http://localhost:8080`

All routes except `/api/auth/**` require:
```
Authorization: Bearer <token>
```

## Table of contents

- [Auth](#auth)
- [Profile](#profile)
- [Preferences](#preferences)
- [Documents](#documents)
- [Chat](#chat)

## Auth

| Method | Route | Body | Response |
|---|---|---|---|
| POST | `/api/auth/register` | `{ email, password }` | `{ token, expiresAt }` |
| POST | `/api/auth/login` | `{ email, password }` | `{ token, expiresAt }` |

## Profile

| Method | Route | Body | Response |
|---|---|---|---|
| GET | `/api/profile` | — | `{ id, email, createdAt }` |
| PATCH | `/api/profile` | `{ email? }` | `{ id, email, createdAt }` |

## Preferences

| Method | Route | Body | Response |
|---|---|---|---|
| GET | `/api/preferences` | — | `{ theme, chatModel, embeddingModel, chunkSize }` |
| PUT | `/api/preferences` | `{ theme?, chatModel?, embeddingModel?, chunkSize? }` | `{ theme, chatModel, embeddingModel, chunkSize }` |

## Documents

| Method | Route | Body | Response |
|---|---|---|---|
| POST | `/api/documents` | multipart file upload | `{ id, filename, status }` |
| GET | `/api/documents` | — | `[{ id, filename, status, uploadedAt }]` |
| GET | `/api/documents/{id}` | — | `{ id, filename, status, chunkCount, uploadedAt }` |
| DELETE | `/api/documents/{id}` | — | `204 No Content` |

`status` is one of: `pending`, `processing`, `ready`, `failed`.

## Chat

| Method | Route | Body | Response |
|---|---|---|---|
| POST | `/api/conversations` | `{ title? }` | `{ id, title, createdAt }` |
| GET | `/api/conversations` | — | `[{ id, title, createdAt }]` |
| GET | `/api/conversations/{id}/messages` | — | `[{ id, role, content, citations, createdAt }]` |
| POST | `/api/conversations/{id}/messages` | `{ content }` | `{ id, role: "assistant", content, citations }` |
| DELETE | `/api/conversations/{id}` | — | `204 No Content` |

`role` is one of: `user`, `assistant`.

`citations` shape:
```json
[
  {
    "documentId": "uuid",
    "filename": "notes.pdf",
    "chunkText": "..."
  }
]
```

---
