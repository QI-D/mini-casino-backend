# Mini Casino

Take home project for Omega Systems. A RESTful API for a casino platform with player management, game operations, and betting functionality.

## Table of Contents

- [API Documentation](#api-documentation)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running The Application](#4-running-the-application)

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

#### 3. Running the Application

```bash
mvn spring-boot:run
```

The server should start on `http:localhost:8080`.