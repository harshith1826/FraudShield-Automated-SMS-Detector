# 🛡️ FraudShield – Automated SMS Fraud Detection System

An AI-powered SMS Fraud Detection System that identifies phishing, scam, and malicious SMS messages using Machine Learning, rule-based security, URL analysis, and multilingual threat warnings.

---

## 📌 Overview

FraudShield is a full-stack security solution designed to detect fraudulent SMS messages in real time. It integrates an Android application, Spring Boot backend, FastAPI-based Machine Learning service, and MongoDB to provide end-to-end fraud detection and monitoring.

---

## 🚀 Features

- 📩 Real-time SMS Analysis
- 🤖 AI/ML-Based Fraud Detection
- 🔗 URL Extraction & Analysis
- ✅ Whitelist Verification
- 🚫 Blacklist Detection
- ⚠️ Risk Score Calculation
- 🌐 Multilingual Fraud Warnings
- 📊 Interactive Admin Dashboard
- 📱 Android Application Integration
- 💾 MongoDB Data Storage
- 🔄 REST API Communication

---

## 🏗️ System Architecture

```
Android Application
        │
        ▼
Spring Boot Backend
        │
        ├── URL Extraction
        ├── Whitelist Check
        ├── Blacklist Check
        ├── Risk Calculator
        │
        ▼
FastAPI Machine Learning Service
        │
        ▼
MongoDB
        │
        ▼
Admin Dashboard
```

---

## 💻 Tech Stack

### Backend

- Java 23
- Spring Boot 3.5
- Maven
- REST APIs

### AI Service

- Python
- FastAPI
- Scikit-Learn
- Playwright

### Database

- MongoDB

### Android

- Kotlin
- Retrofit
- WorkManager

### Frontend Dashboard

- HTML
- CSS
- JavaScript
- Bootstrap
- Chart.js

---

## 📂 Project Structure

```
FraudShield-Automated-SMS-Detector/
│
├── fraud-sms-detector-main      # Spring Boot Backend
├── fraud-sms-detector-ml        # FastAPI AI Service
├── fraudshield-android          # Android Application
```

---

## 🔄 Workflow

1. Android receives an SMS.
2. SMS is sent to the Spring Boot backend.
3. Backend validates the request.
4. URL Extraction is performed.
5. Whitelist & Blacklist verification.
6. AI Service predicts fraud probability.
7. Results are stored in MongoDB.
8. Dashboard displays analytics.
9. Android receives the prediction and warning.

---

## 📊 Dashboard

The backend provides an interactive dashboard featuring:

- Total SMS Analysed
- Fraud SMS Count
- Safe SMS Count
- Threat Category Distribution
- Daily Analysis
- SMS History
- Whitelist & Blacklist Monitoring

---

## 📡 REST APIs

### Health Check

```
GET /health
```

### SMS Ingestion

```
POST /api/ingest
```

### ML Feedback

```
GET /api/getmlfeedback/{job_id}
```

### User Feedback

```
POST /api/userfeedback
```

---

## 👥 Team

- **Backend Development:** Harshith M
- **Android Development:** Shreya
- **Machine Learning:** Navya

---

## 📄 License

This project was developed for academic and educational purposes.
