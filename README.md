# AI Resume Screening System

> **A Spring Boot backend for AI-powered resume screening. Built with Java 17, Spring Security + JWT, JPA/H2, Apache PDFBox, and Google Gemini AI.**

## 🎯 What It Does

An API that helps HR teams screen resumes against job descriptions. Candidates upload PDF resumes, HR posts job descriptions, and AI (Google Gemini) analyzes each resume to return a match score, skill gaps, and interview questions.

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (jjwt 0.12.6) |
| Database | H2 (in-memory, dev) / MySQL-ready |
| ORM | Spring Data JPA + Hibernate |
| PDF Parsing | Apache PDFBox 3.0 |
| AI | Google Gemini API |
| Build | Maven |
| Deployment | Docker + Render |

## 📚 Features Implemented (Week 1 — Backend Foundation)

- ✅ JWT-based Authentication (register + login)
- ✅ BCrypt password hashing
- ✅ Job Management (full CRUD)
- ✅ Resume Upload (PDF only, up to 10MB)
- ✅ Automatic PDF Text Extraction (Apache PDFBox)
- ✅ Global Exception Handling (@ControllerAdvice)
- ✅ Request Validation (@Valid + @NotBlank)
- ✅ Consistent JSON error responses (timestamp, status, error, message)
- ✅ Layered Architecture (Controller → Service → Repository)
- ✅ DTO Pattern (separate API contracts from entities)

## 🔗 API Endpoints

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login, returns JWT |

### Jobs
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/jobs` | Create a new job |
| GET | `/api/jobs` | List all jobs |
| GET | `/api/jobs/{id}` | Get job by ID |
| DELETE | `/api/jobs/{id}` | Delete a job |

### Resumes
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/resumes/upload` | Upload a PDF resume |
| GET | `/api/resumes` | List all resumes |
| GET | `/api/resumes/{id}` | Get resume by ID |
| DELETE | `/api/resumes/{id}` | Delete a resume |

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+

### Local Setup

```bash
# Clone
git clone https://github.com/umanagamaniy/ai-resume-screening.git
cd ai-resume-screening

# Configure application.properties (see application-example.properties)
# You'll need to add: jwt.secret, gemini.api.key, file.upload-dir

# Run
./mvnw spring-boot:run
```

App runs at `http://localhost:8080`

## 🏗️ Project Structure