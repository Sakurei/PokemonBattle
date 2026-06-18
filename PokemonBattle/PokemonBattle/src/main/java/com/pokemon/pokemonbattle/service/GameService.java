package com.pokemon.pokemonbattle.service;

import com.pokemon.pokemonbattle.model.*;
import com.pokemon.pokemonbattle.system.CaptureSystem;
import com.pokemon.pokemonbattle.data.SkillData;
import com.pokemon.pokemonbattle.utils.Type;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Random;

/**
 * GameService - Handles all game logic and state management for Spring Boot
 */
@Service
public class GameService {

    private Player currentPlayer;
    private Random random = new Random();
    private int captureAttempts = 3;
    private Pokemon currentEnemy;
    private boolean battleActive = false;

    // ===== INITIALIZE GAME =====
    public void initializeGame(String playerName) {
        this.currentPlayer = new Player(playerName);
        
        // Give starter pokemon
        Pokemon starter = new BasicPokemon("Pikachu", 100, 20, 5, Type.ELECTRIC);
        assignRandomSkills(starter);
        currentPlayer.addPokemon(starter);
        currentPlayer.setActivePokemon(0);
        
        this.captureAttempts = 3;
    }

    // ===== START NEW BATTLE =====
    public Pokemon startNewBattle() {
        double roll = Math.random();
        
        if (roll < 0.2) {
            currentEnemy = new LegendaryPokemon("Bulbosaurus", 150, 30, 10, Type.GRASS);
        } else {
            currentEnemy = new BasicPokemon("Charmander", 100, 18, 5, Type.FIRE);
        }
        
        assignRandomSkills(currentEnemy);
        battleActive = true;
        return currentEnemy;
    }

    // ===== GET CURRENT BATTLE STATE =====
    public BattleState getCurrentBattleState() {
        if (!battleActive) {
            return null;
        }
        
        BattleState state = new BattleState();
        state.setPlayerPokemon(currentPlayer.getActivePokemon());
        state.setEnemyPokemon(currentEnemy);
        state.setCaptureAttempts(captureAttempts);
        state.setCanCapture(currentEnemy.getHpRatio() <= 0.2);
        
        return state;
    }

    // ===== PLAYER ATTACK =====
    public BattleResult playerAttack(int skillIndex) {
        if (!battleActive || currentPlayer.getActivePokemon() == null) {
            return new BattleResult("Battle not active", false);
        }
        
        Pokemon playerPoke = currentPlayer.getActivePokemon();
        ArrayList<Skill> skills = playerPoke.getSkills();
        
        if (skillIndex < 0 || skillIndex >= skills.size()) {
            return new BattleResult("Invalid skill index", false);
        }
        
        Skill skill = skills.get(skillIndex);
        playerPoke.useSkill(skillIndex, currentEnemy);
        
        // Enemy counter attack if alive
        if (currentEnemy.isAlive() && currentEnemy.canMove()) {
            int enemySkillIndex = random.nextInt(currentEnemy.getSkills().size());
            currentEnemy.useSkill(enemySkillIndex, playerPoke);
        }
        
        // Check battle end
        BattleResult result = new BattleResult();
        result.setAction("Player used " + skill.getName());
        result.setPlayerHp(playerPoke.getHp());
        result.setEnemyHp(currentEnemy.getHp());
        result.setBattleActive(playerPoke.isAlive() && currentEnemy.isAlive());
        
        if (!currentEnemy.isAlive()) {
            result.setMessage("🎉 Menang! Lawan kalah!");
            battleActive = false;
        } else if (!playerPoke.isAlive()) {
            result.setMessage("❌ Kalah! Pokemon mu tidak bisa melawan!");
            battleActive = false;
        }
        
        return result;
    }

    // ===== CAPTURE POKEMON =====
    public CaptureResult capturePokemon() {
        CaptureResult result = new CaptureResult();
        
        if (captureAttempts <= 0) {
            result.setSuccess(false);
            result.setMessage("Pokeball habis!");
            return result;
        }
        
        if (!currentEnemy.isAlive()) {
            result.setSuccess(false);
            result.setMessage("Pokémon sudah mati, tidak bisa ditangkap!");
            return result;
        }
        
        boolean captured = CaptureSystem.tryCapture(currentEnemy);
        captureAttempts--;
        
        result.setSuccess(captured);
        result.setCaptureAttempts(captureAttempts);
        
        if (captured) {
            currentPlayer.addPokemon(currentEnemy);
            result.setMessage("🎉 Berhasil menangkap " + currentEnemy.getName() + "!");
            battleActive = false;
        } else {
            result.setMessage("❌ Gagal menangkap!");
        }
        
        return result;
    }

    // ===== USE ITEM =====
    public String useItem(int itemIndex) {
        // For now, just heal the active pokemon
        // TODO: Implement inventory system properly
        Pokemon activePokemon = currentPlayer.getActivePokemon();
        
        if (activePokemon == null) {
            return "Tidak ada pokemon aktif!";
        }
        
        int healAmount = 20;
        activePokemon.setHp(activePokemon.getHp() + healAmount);
        
        return "Gunakan item! HP " + activePokemon.getName() + " pulih " + healAmount;
    }

    // ===== GET PLAYER DATA =====
    public Player getPlayer() {
        return currentPlayer;
    }

    // ===== GET PLAYER POKEMON COLLECTION =====
    public ArrayList<Pokemon> getPlayerPokemonCollection() {
        return currentPlayer.getCollection();
    }

    // ===== SWITCH ACTIVE POKEMON =====
    public String switchPokemon(int index) {
        ArrayList<Pokemon> collection = currentPlayer.getCollection();
        
        if (index < 0 || index >= collection.size()) {
            return "Pokemon tidak ditemukan!";
        }
        
        currentPlayer.setActivePokemon(index);
        return "Memilih " + collection.get(index).getName() + "!";
    }

    // ===== ASSIGN RANDOM SKILLS =====
    private void assignRandomSkills(Pokemon pokemon) {
        ArrayList<Skill> skills = SkillData.getSkillsByType(pokemon.getType());
        
        while (pokemon.getSkills().size() < 2 && skills.size() > 0) {
            Skill s = skills.get(random.nextInt(skills.size()));
            
            if (!pokemon.getSkills().contains(s)) {
                pokemon.addSkill(s);
            }
        }
    }

    // ===== END BATTLE =====
    public void endBattle() {
        battleActive = false;
        currentEnemy = null;
        captureAttempts = 3;
    }

    public boolean isBattleActive() {
        return battleActive;
    }

    // ===== Inner DTOs =====
    public static class BattleState {
        private Pokemon playerPokemon;
        private Pokemon enemyPokemon;
        private int captureAttempts;
        private boolean canCapture;

        // Getters & Setters
        public Pokemon getPlayerPokemon() { return playerPokemon; }
        public void setPlayerPokemon(Pokemon p) { this.playerPokemon = p; }
        public Pokemon getEnemyPokemon() { return enemyPokemon; }
        public void setEnemyPokemon(Pokemon p) { this.enemyPokemon = p; }
        public int getCaptureAttempts() { return captureAttempts; }
        public void setCaptureAttempts(int a) { this.captureAttempts = a; }
        public boolean isCanCapture() { return canCapture; }
        public void setCanCapture(boolean c) { this.canCapture = c; }
    }

    public static class BattleResult {
        private String action;
        private int playerHp;
        private int enemyHp;
        private boolean battleActive;
        private String message;

        public BattleResult() {}
        public BattleResult(String message, boolean active) {
            this.message = message;
            this.battleActive = active;
        }

        // Getters & Setters
        public String getAction() { return action; }
        public void setAction(String a) { this.action = a; }
        public int getPlayerHp() { return playerHp; }
        public void setPlayerHp(int hp) { this.playerHp = hp; }
        public int getEnemyHp() { return enemyHp; }
        public void setEnemyHp(int hp) { this.enemyHp = hp; }
        public boolean isBattleActive() { return battleActive; }
        public void setBattleActive(boolean b) { this.battleActive = b; }
        public String getMessage() { return message; }
        public void setMessage(String m) { this.message = m; }
    }

    public static class CaptureResult {
        private boolean success;
        private String message;
        private int captureAttempts;

        // Getters & Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean s) { this.success = s; }
        public String getMessage() { return message; }
        public void setMessage(String m) { this.message = m; }
        public int getCaptureAttempts() { return captureAttempts; }
        public void setCaptureAttempts(int a) { this.captureAttempts = a; }
    }
}
