package Database;

import java.sql.Connection;
import java.sql.Statement;

public class InitDB {

    public static void init() {

        try {
            Connection conn = Database.connect();
            Statement stmt = conn.createStatement();

            // 🔥 WAJIB untuk SQLite foreign key
            stmt.execute("PRAGMA foreign_keys = ON");

            // ===== PLAYER =====
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS player (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT
                );
            """);

            // ===== POKEMON =====
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pokemon (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    hp INTEGER,
                    attack INTEGER,
                    defense INTEGER,
                    type TEXT,
                    legendary INTEGER,
                    player_id INTEGER,
                    FOREIGN KEY (player_id) REFERENCES player(id)
                );
            """);

            // ===== SKILL =====
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS skill (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    power INTEGER,
                    type TEXT,
                    effect TEXT,
                    effectChance REAL
                );
            """);

            // ===== RELASI =====
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pokemon_skill (
                    pokemon_id INTEGER,
                    skill_id INTEGER,
                    FOREIGN KEY (pokemon_id) REFERENCES pokemon(id),
                    FOREIGN KEY (skill_id) REFERENCES skill(id)
                );
            """);

            System.out.println("✅ Database siap!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}