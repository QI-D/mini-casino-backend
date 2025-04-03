# Mini Casino

This repository contains a RESTful API for managing a casino platform, featuring player management, game operations, and betting functionality. It serves as a backend system for a casino-like application, where players can register, deposit funds, view their balance, and participate in games

The frontend for this project is available at: [Mini Casino Frontend](https://github.com/QI-D/moni-casino-frontend)

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running The Application](#running-the-application)
- [Deployment](#deployment)
- [API Documentation](#api-documentation)

## Features

### Authentication

- **Player Registration**: Allows new players to register with their name, username, password, and birthdate.
- **Login**: Enables players to authenticate and receive a JWT token for secure access to other features.

### Player Operations

- **Deposit Funds**: Players can deposit funds into their accounts.
- **Check Balance**: Players can check their current account balance at any time.

### Game Operations

- **View All Games**: Get a list of all available casino games.
- **View Game Details**: Retrieve detailed information about specific games.
- **Upload New Games**: Admins can upload new games from an XML file.
- **Search Games**: Players or admins can search for games by name.

### Betting Operations

- **Place a Bet**: Players can place bets on their chosen games.
- **Bet Summary**: View a summary of all bets, including total amounts, winnings, and net profit.

### Security

- **JWT Authentication**: Secure player and betting operations with JSON Web Tokens (JWT).

## Technologies Used

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **JPA/Hibernate**
- **MySQL**
- **Maven**

## Getting Started

### Prerequisites

- **Java 17**: Ensure you have Java Development Kit (JDK) 17 installed.
- **MySQL**: A running MySQL database instance.
- **Maven**: For building and managing dependencies.

### Installation

#### 1. Create a `.env` File

Create a `.env` file in the root directory with the following environment variables. **Customize the values according to your setup:**

```ini
DATABASE_URL=jdbc:mysql://<DB_URL>
DATABASE_USERNAME=<DB_USERNAME>
DATABASE_PASSWORD=<DB_PASSWORD>
ADMIN_USER=<ADMIN_USER>
ADMIN_PASSWORD=<ADMIN_PASSWORD>
JWT_SECRET=<JWT_SECRET>
```

Ensure you replace the above values with your actual database credentials, admin credentials, and JWT secret.

#### 2. Install the Dependencies

```bash
mvn clean install
```

### Running the Application

```bash
mvn spring-boot:run
```

The server should start on `http:localhost:8080`.

## Deployment

To deploy the mini casino backend and frontend services using Docker, follow the steps below.

### Prerequisites

- **Docker**: Ensure Docker is installed and running on your system.
- **Docker Compose**: Docker Compose is required to manage multi-container Docker applications.
- **Git**: Ensure Git is installed to clone the repositories.

### Cloning the Repositories

1.  Clone the backend repository:

    ```bash
    git clone https://github.com/QI-D/mini-casino-backend.git
    ```

2.  Clone the frontend repository:

    ```bash
    git clone https://github.com/QI-D/mini-casino-frontend.git
    ```

3.  Place both the mini-casino-backend and mini-casino-frontend directories in a common parent directory.

    Example structure:

        /mini-casino
            /mini-casino-backend
            /mini-casino-frontend

### Running the Deployment

1. Navigate to the mini-casino-backend directory where the `docker-compose.yml` file is located.

   ```bash
   cd mini-casino-backend
   ```

2. Create a .env file in the root directory with the following environment variables. Customize the values according to your setup:

   ```ini
    DATABASE_URL=jdbc:mysql://<DB_URL>
    DATABASE_NAME=<DATABASE_NAME>
    DATABASE_USERNAME=<DATABASE_USERNAME>
    DATABASE_PASSWORD=<DATABASE_PASSWORD>
    ADMIN_USER=<ADMIN_USER>
    ADMIN_PASSWORD=<ADMIN_PASSWORD>
    JWT_SECRET=<JWT_SECRET>
   ```

3. Run the following command to start the backend, frontend, and MySQL services:

   ```bash
   docker-compose up --build
   ```

   This command will:

   - Build the Docker images for the backend and frontend.
   - Start the services defined in the docker-compose.yml file.
   - Expose the backend service on port 8080 and the frontend service on port 3000.

4. Access the application:

   - The backend will be available at `http://localhost:8080`.
   - The frontend will be available at `http://localhost:3000`.

### Stopping the Services

To stop the running services, use the following command:

```bash
docker-compose down
```

## API Documentation

### Authentication

#### Register a new player `POST /auth/register`

Request Body:

```json
{
  "name": "string",
  "username": "string",
  "password": "string",
  "birthdate": "yyyy-mm-dd"
}
```

Example Response:

```json
{
  "status": 200,
  "message": "Player registered successfully",
  "player": {
    "id": 1,
    "username": "player1",
    "balance": 1000.0
  }
}
```

#### Authenticate and get JWT token `POST /auth/login`

Request Body:

```json
{
  "username": "string",
  "password": "string"
}
```

Example Response:

```json
{
  "status": 200,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiJ9......",
  "player": {
    "id": 1,
    "name": "player 1",
    "username": "player1",
    "password": "$2a$10$v7OTMB08B....",
    "balance": 0.0,
    "birthdate": "yyyy-mm-dd"
  }
}
```

### Player Operations

#### Deposit funds `POST /player/deposit`

Headers:

`Authorization: Bearer {token}`

Request Body:

```json
{
  "amount": "number"
}
```

Example Response:

```json
{
  "status": 200,
  "message": "Deposit successful",
  "balance": 100.0
}
```

#### Check balance `GET /player/balance`

Headers:

`Authorization: Bearer {token}`

Example Response:

```json
{
  "status": 200,
  "message": "Balance retrieved successfully",
  "balance": 100.0
}
```

### Game Operations

#### Get all games `GET /games`

Example Response:

```json
{
  "status": 200,
  "message": "Games retrieved successfully",
  "gameList": [
    {
      "id": 1,
      "name": "Blackjack",
      "chanceOfWinning": 0.42,
      "winningMultiplier": 2.0,
      "maxBet": 1000.0,
      "minBet": 10.0
    },
    {
      "id": 2,
      "name": "Roulette",
      "chanceOfWinning": 0.48,
      "winningMultiplier": 1.8,
      "maxBet": 500.0,
      "minBet": 5.0
    }
  ]
}
```

#### Get game by ID `GET /games/{id}`

Example Response:

```json
{
  "status": 200,
  "message": "Game retrieved successfully",
  "game": {
    "id": 1,
    "name": "Blackjack",
    "chanceOfWinning": 0.42,
    "winningMultiplier": 2.0,
    "maxBet": 1000.0,
    "minBet": 10.0
  }
}
```

#### Upload games from XML `POST /games/upload`

Headers:

```bash
Content-Type: multipart/form-data
Authorization: Bearer {token}
```

Body:

`file`: XML file containing game data

Example success response:

```json
{
  "status": 200,
  "message": "1/1 games uploaded successfully",
  "gameList": [
    {
      "id": 3,
      "name": "Slot Machine - Lucky 7s",
      "chanceOfWinning": 0.15,
      "winningMultiplier": 10.0,
      "maxBet": 100.0,
      "minBet": 1.0
    }
  ]
}
```

Example partial success response:

```json
{
  "status": 207,
  "message": "1/2 games uploaded successfully",
  "gameList": [
    {
      "id": 3,
      "name": "Slot Machine - Lucky 7s",
      "chanceOfWinning": 0.15,
      "winningMultiplier": 10.0,
      "maxBet": 100.0,
      "minBet": 1.0
    },
    {
      "id": 4,
      "name": "Poker - Texas Hold'em",
      "chanceOfWinning": 0.35,
      "winningMultiplier": 3.5,
      "maxBet": 2000.0,
      "minBet": 20.0
    }
  ]
}
```

#### Search game `GET /games/search?name={gameName}`

Example Response:

```json
{
  "status": 200,
  "message": "Games found: 1",
  "gameList": [
    {
      "id": 1,
      "name": "Blackjack",
      "chanceOfWinning": 0.42,
      "winningMultiplier": 2.0,
      "maxBet": 1000.0,
      "minBet": 10.0
    }
  ]
}
```

### Bet Operations

#### Place a bet `/bet/place`

Headers:

`Authorization: Bearer {token}`

Request body:

```json
{
  "gameId": 1,
  "betAmount": 30
}
```

Example Response:

```json
{
  "status": 200,
  "message": "Bet placed successfully",
  "bet": {
    "id": 7,
    "playerId": 1,
    "gameId": 1,
    "betAmount": 30.0,
    "won": true,
    "winnings": 60.0
  }
}
```

### Get bet summary `GET /bet/summary`

Headers:

`Authorization: Bearer {token}`

Example response:

```json
{
  "status": 200,
  "message": "Bet summary retrieved successfully",
  "betSummary": {
    "totalBets": 6,
    "totalBetAmount": 130.0,
    "totalWinnings": 40.0,
    "netProfit": -90.0
  }
}
```
