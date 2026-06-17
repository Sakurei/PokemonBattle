# Pokemon Battle Game - Spring Boot REST API Guide

## 🏗️ Arsitektur Spring Boot yang Sudah Diimplementasi

### Layer Architecture:
```
├── Controller Layer (REST API)
│   ├── GameRestController.java      (REST endpoints)
│   └── PageController.java          (Thymeleaf templates)
│
├── Service Layer (Business Logic)
│   └── GameService.java             (Game state & logic)
│
├── Model Layer (Data Entities)
│   ├── Player.java
│   ├── Pokemon.java (Abstract)
│   ├── BasicPokemon.java
│   ├── LegendaryPokemon.java
│   ├── Skill.java
│   ├── Item.java
│   └── StatusEffect.java
│
├── System Layer (Utilities)
│   ├── CaptureSystem.java
│   ├── SaveSystem.java
│   └── BattleSystemLegacy.java      (Console-based)
│
└── Data Layer (Static Data)
    ├── SkillData.java               (Skills database)
    └── ItemData.java                (Items database)
```

## 🚀 Cara Menjalankan

### 1. Build & Run
```bash
cd PokemonBattle

# Compile
.\mvnw -DskipTests compile

# Run
.\mvnw spring-boot:run
```

Aplikasi akan berjalan di: `http://localhost:8080`

## 📡 REST API Endpoints

### 1. **Initialize Game**
```
POST /api/game/init?playerName=YourName
```
Response:
```json
{
  "success": true,
  "message": "Game initialized for YourName",
  "player": { /* player data */ },
  "collection": [ /* pokemon collection */ ]
}
```

### 2. **Start Battle**
```
POST /api/game/battle/start
```
Response:
```json
{
  "success": true,
  "message": "Battle started!",
  "enemy": { /* enemy pokemon */ },
  "battleState": { /* current battle state */ }
}
```

### 3. **Get Battle State**
```
GET /api/game/battle/state
```
Returns current battle status with player and enemy pokemon HP, capture attempts, etc.

### 4. **Player Attack**
```
POST /api/game/battle/attack?skillIndex=0
```
Player uses skill at index `skillIndex` to attack enemy.

### 5. **Capture Pokemon**
```
POST /api/game/battle/capture
```
Attempt to catch the enemy pokemon. Success depends on HP ratio and pokemon status.

### 6. **Use Item**
```
POST /api/game/battle/use-item?itemIndex=0
```
Use item to heal pokemon.

### 7. **Switch Pokemon**
```
POST /api/game/pokemon/switch?index=1
```
Switch active pokemon to another from collection.

### 8. **Get Collection**
```
GET /api/game/pokemon/collection
```
Get list of player's pokemon collection.

### 9. **End Battle**
```
POST /api/game/battle/end
```
End current battle and reset state.

### 10. **Get All Skills**
```
GET /api/skills
```
Get all available skills in the game.

### 11. **Health Check**
```
GET /api/game/health
```
Check if API is running.

## 📝 Example Flow

### 1. Start Game
```bash
curl -X POST "http://localhost:8080/api/game/init?playerName=Ash"
```

### 2. Start Battle
```bash
curl -X POST "http://localhost:8080/api/game/battle/start"
```

### 3. Attack
```bash
curl -X POST "http://localhost:8080/api/game/battle/attack?skillIndex=0"
```

### 4. Check State
```bash
curl -X GET "http://localhost:8080/api/game/battle/state"
```

### 5. Try to Capture (if enemy HP ≤ 20%)
```bash
curl -X POST "http://localhost:8080/api/game/battle/capture"
```

## 🔄 How Spring Boot Connects Everything

### Component Flow:

1. **User Request** → API Endpoint (GameRestController)
2. **GameRestController** → Calls GameService methods
3. **GameService** → Manages game state & logic
4. **GameService** → Uses Model classes (Player, Pokemon, etc.)
5. **GameService** → Uses System utilities (CaptureSystem, etc.)
6. **Response** → JSON back to client

### State Management:

GameService maintains singleton instance of:
- `currentPlayer` - Player object with collection
- `currentEnemy` - Current battle pokemon
- `battleActive` - Battle status flag
- `captureAttempts` - Remaining pokeballs

## 🎨 Frontend Integration

For HTML/JavaScript frontend, send AJAX requests to these endpoints:

```javascript
// Initialize game
fetch('/api/game/init?playerName=Ash', { method: 'POST' })
  .then(r => r.json())
  .then(data => console.log(data));

// Start battle
fetch('/api/game/battle/start', { method: 'POST' })
  .then(r => r.json())
  .then(data => {
    console.log('Enemy: ', data.enemy);
    console.log('Your Pokemon: ', data.battleState.playerPokemon);
  });

// Attack
fetch('/api/game/battle/attack?skillIndex=0', { method: 'POST' })
  .then(r => r.json())
  .then(data => console.log(data.result));
```

## 📋 Database Configuration

Currently using H2 in-memory database. Configured in `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

Access H2 console at: `http://localhost:8080/h2-console`

## 🛠️ Key Classes

### GameService
- Manages game state
- Handles battle logic
- Provides business logic methods

### GameRestController
- REST API endpoints
- Request/response handling
- JSON serialization

### Pokemon (Model)
- Abstract base class
- HP, Attack, Defense, Type
- Skills and status effects
- Battle mechanics

### Player
- Name and pokemon collection
- Active pokemon
- Inventory (future)

## ⚙️ Configuration Tips

### Add CORS Support
Already enabled in GameRestController with `@CrossOrigin(origins = "*")`

### Add Authentication
Can add Spring Security later with JWT tokens

### Database Persistence
Replace H2 with PostgreSQL/MySQL:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pokemonbattle
spring.datasource.username=user
spring.datasource.password=pass
spring.jpa.hibernate.ddl-auto=update
```

## 🐛 Troubleshooting

1. **Port already in use**
   ```bash
   .\mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
   ```

2. **Controller not found**
   - Ensure package structure: `com.pokemon.pokemonbattle.controller`
   - Controllers must be in main application package or subpackage

3. **Service not autowiring**
   - Add `@Service` annotation to GameService
   - Ensure it's in correct package for component scanning

## 📚 Next Steps

1. **Create HTML/JavaScript frontend** to consume REST API
2. **Add database persistence** with JPA repositories
3. **Implement User authentication** with Spring Security
4. **Add WebSocket** for real-time multiplayer battles
5. **Deploy to production** (Heroku, AWS, etc.)

---

**Game URLs:**
- Game Home: `http://localhost:8080/`
- API Docs: `http://localhost:8080/api/game/health`
- H2 Console: `http://localhost:8080/h2-console`
