# LeagueTracker

LeagueTracker is a tool that can be used to gather insightful data and statistics about a user's league of legends account. By accessing the riot games api, we can get tons of useful data, that we can convert into presentable and usefull data for the end user.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Backend Setup](#backend-setup)
- [Frontend Setup](#frontend-setup)

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (version 11 or later)
- [Gradle](https://gradle.org/install/)
- [Node.js and npm](https://nodejs.org/) (version 14 or later)

You also need a riot games dev api key, read more at https://developer.riotgames.com/

## Backend Setup

1. Navigate to the `backend` directory:

   ```bash
   cd backend
   ```

2. Clean and build the project:

   ```bash
   ./gradlew clean build
   ```

3. Run the backend:
   ```bash
   ./gradlew bootRun
   ```

## Frontend Setup

1. Navigate to the `frontend` directory:

   ```bash
   cd frontend
   ```

2. Install the dependencies:

   ```bash
   npm install
   ```

3. Run the frontend development server:
   ```bash
   npm run dev
   ```