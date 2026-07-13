# AI Resume Screening — Project Status

## Current State (as of pause date)

**Overall Completion:** ~95% of the 21-day plan done

## ✅ What's Working (Local)

- Spring Boot 3.5 backend on port 8080
- User authentication (JWT-based, tested via Postman)
- Job CRUD (POST, GET, DELETE)
- Resume upload with PDF file storage
- PDF text extraction via Apache PDFBox
- Screening entity + service that calls Google Gemini AI
- Global exception handling (@ControllerAdvice)
- Request validation (@Valid + @NotBlank)
- Minimal HTML/JS frontend at `/` that runs the full flow: load jobs → upload resume → analyze → view results
- Toast notifications for user feedback

## ⏸️ What's Paused / Incomplete

- **Render deployment:** existing Render service `uma-ai-assistant` deployed the OLD chatbot version. Multiple newer pushes to GitHub have not deployed on Render (deploys after JWT commit didn't happen — need to investigate).
- **LinkedIn post + resume update:** not written yet.

## 🔑 Environment Variables (For Any Future Deploy)

- `GEMINI_API_KEY` — from https://aistudio.google.com/app/apikey (rotate the exposed key first)
- `GEMINI_API_URL` — https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent
- `JWT_SECRET` — 32+ character random string
- `JWT_EXPIRATION` — 86400000 (24 hours in ms)
- `SPRING_JPA_HIBERNATE_DDL_AUTO` — must be `update` (not `create-drop`)
- `FILE_UPLOAD_DIR` — must be Linux path (e.g. `/app/uploads/resumes/`) when running on Render

## 🎯 Next Steps When Resuming

1. Log into Render with original account (may need GitHub OAuth)
2. Check why deploys stopped after June 24
3. Set fresh environment variables (Windows path -> Linux path is critical)
4. Redeploy from latest commit on `main`
5. Test full flow at live URL
6. Update README with live demo link
7. Write LinkedIn post + resume line

## 📁 Key Files

- Backend: `src/main/java/com/uma/airesumescreening/**`
- Frontend: `src/main/resources/static/index.html`
- Dockerfile: project root
- Excluded from Git: `application.properties`, `uploads/`