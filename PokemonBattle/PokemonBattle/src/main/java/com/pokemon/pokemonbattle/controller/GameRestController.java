package com.pokemon.pokemonbattle.controller;

import com.pokemon.pokemonbattle.model.Pokemon;
import com.pokemon.pokemonbattle.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * REST API Controller for Pokemon Battle Game
 * Provides endpoints for game initialization, battles, and interactions
 */
@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameRestController {

    @Autowired
    private GameService gameService;

    // ===== INITIALIZE GAME =====
    @PostMapping("/init")
    public Map<String, Object> initGame(@RequestParam(defaultValue = "Ash") String playerName) {
        gameService.initializeGame(playerName);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Game initialized for " + playerName);
        response.put("player", gameService.getPlayer());
        response.put("collection", gameService.getPlayerPokemonCollection());
        
        return response;
    }

    // ===== START NEW BATTLE =====
    @PostMapping("/battle/start")
    public Map<String, Object> startBattle() {
        Pokemon enemy = gameService.startNewBattle();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Battle started!");
        response.put("enemy", enemy);
        response.put("battleState", gameService.getCurrentBattleState());
        
        return response;
    }

    // ===== GET CURRENT BATTLE STATE =====
    @GetMapping("/battle/state")
    public Map<String, Object> getBattleState() {
        GameService.BattleState state = gameService.getCurrentBattleState();
        
        Map<String, Object> response = new HashMap<>();
        if (state != null) {
            response.put("success", true);
            response.put("battleActive", gameService.isBattleActive());
            response.put("state", state);
        } else {
            response.put("success", false);
            response.put("message", "No active battle");
        }
        
        return response;
    }

    // ===== PLAYER ATTACK =====
    @PostMapping("/battle/attack")
    public Map<String, Object> playerAttack(@RequestParam int skillIndex) {
        GameService.BattleResult result = gameService.playerAttack(skillIndex);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("result", result);
        response.put("battleState", gameService.getCurrentBattleState());
        
        return response;
    }

    // ===== CAPTURE POKEMON =====
    @PostMapping("/battle/capture")
    public Map<String, Object> capturePokemon() {
        GameService.CaptureResult result = gameService.capturePokemon();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("result", result);
        response.put("battleState", gameService.getCurrentBattleState());
        
        return response;
    }

    // ===== USE ITEM =====
    @PostMapping("/battle/use-item")
    public Map<String, Object> useItem(@RequestParam int itemIndex) {
        String message = gameService.useItem(itemIndex);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("battleState", gameService.getCurrentBattleState());
        
        return response;
    }

    // ===== SWITCH POKEMON =====
    @PostMapping("/pokemon/switch")
    public Map<String, Object> switchPokemon(@RequestParam int index) {
        String message = gameService.switchPokemon(index);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("collection", gameService.getPlayerPokemonCollection());
        response.put("activePokemon", gameService.getPlayer().getActivePokemon());
        
        return response;
    }

    // ===== GET PLAYER COLLECTION =====
    @GetMapping("/pokemon/collection")
    public Map<String, Object> getCollection() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("collection", gameService.getPlayerPokemonCollection());
        response.put("activePokemon", gameService.getPlayer().getActivePokemon());
        
        return response;
    }

    // ===== END BATTLE =====
    @PostMapping("/battle/end")
    public Map<String, Object> endBattle() {
        gameService.endBattle();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Battle ended");
        
        return response;
    }

    // ===== GET ALL SKILLS =====
    @GetMapping("/skills")
    public Map<String, Object> getAllSkills() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("skills", com.pokemon.pokemonbattle.data.SkillData.getAllSkills());
        
        return response;
    }

    // ===== HEALTH CHECK =====
    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Pokemon Battle Game API is running!");
        
        return response;
    }
}
