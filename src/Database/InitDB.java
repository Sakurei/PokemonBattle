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

            // 1. ===== TYPE (Urutan Pertama: Agar ID bisa direferensikan) =====
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS type (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT UNIQUE
                );
            """);

            stmt.execute("DELETE FROM type;");
            stmt.execute("""
                INSERT INTO type (id, name) VALUES
                    (1, 'FIRE'), (2, 'WATER'), (3, 'GRASS'), 
                    (4, 'ELECTRIC'), (5, 'ICE'), (6, 'POISON'),
                    (7, 'PSYCHIC'), (8, 'DRAGON'), (9, 'GROUND'),
                    (10, 'STEEL'), (11, 'FAIRY'), (12, 'GHOST');
            """);

            // 2. ===== PLAYER =====
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS player (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT
                );
            """);

            // 3. ===== POKEMON (Perbaikan: Kolom harus type_id sesuai referensi) =====
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pokemon (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    hp INTEGER,
                    attack INTEGER,
                    defense INTEGER,
                    type_id INTEGER,
                    is_legendary INTEGER DEFAULT 0,
                    FOREIGN KEY (type_id) REFERENCES type(id)
                );
             """);
            
            stmt.execute("DELETE FROM pokemon;");

            // ===== INSERT 30 POKEMON LEGENDARY =====
            stmt.execute("""
                INSERT INTO pokemon (name, hp, attack, defense, type_id, is_legendary) VALUES
                    ('Entei', 190, 135, 105, 1, 1), ('Ho-Oh', 185, 150, 110, 1, 1),
                    ('Suicune', 180, 95, 135, 2, 1), ('Kyogre', 180, 120, 110, 2, 1),
                    ('Virizion', 170, 110, 92, 3, 1), ('Shaymin', 160, 100, 100, 3, 1),
                    ('Zapdos', 170, 110, 105, 4, 1), ('Raikou', 170, 105, 95, 4, 1),
                    ('Zekrom', 180, 170, 140, 4, 1), ('Thundurus', 160, 135, 90, 4, 1),
                    ('Articuno', 170, 105, 120, 5, 1), ('Regice', 160, 70, 150, 5, 1),
                    ('Kyurem', 200, 150, 110, 5, 1), ('Mewtwo', 180, 190, 120, 7, 1),
                    ('Lugia', 190, 110, 150, 7, 1), ('Latios', 160, 110, 100, 8, 1),
                    ('Latias', 160, 100, 120, 8, 1), ('Rayquaza', 185, 180, 120, 8, 1),
                    ('Dialga', 185, 150, 150, 10, 1), ('Palkia', 170, 150, 120, 2, 1),
                    ('Groudon', 185, 180, 160, 9, 1), ('Giratina', 220, 120, 120, 12, 1),
                    ('Arceus', 200, 150, 150, 7, 1), ('Xerneas', 200, 150, 115, 11, 1),
                    ('Yveltal', 200, 150, 115, 12, 1), ('Solgaleo', 210, 160, 120, 10, 1),
                    ('Lunala', 210, 135, 100, 7, 1), ('Zacian', 175, 190, 135, 10, 1),
                    ('Zamazenta', 175, 140, 165, 10, 1), ('Eternatus', 220, 105, 115, 8, 1);
            """);
            
            // ===== INSERT 30 POKEMON NON-LEGENDARY =====
            stmt.execute("""
                INSERT INTO pokemon (name, hp, attack, defense, type_id) VALUES
                    ('Charizard', 140, 105, 100, 1), ('Ninetales', 135, 95, 95, 1),
                    ('Arcanine', 160, 130, 100, 1), ('Blastoise', 140, 105, 120, 2),
                    ('Gyarados', 165, 155, 100, 2), ('Venusaur', 140, 100, 100, 3),
                    ('Sceptile', 130, 110, 85, 3), ('Raichu', 125, 110, 75, 4),
                    ('Luxray', 140, 140, 90, 4), ('Lapras', 200, 105, 100, 5),
                    ('Gengar', 125, 85, 80, 12), ('Alakazam', 110, 70, 65, 7),
                    ('Dragonite', 165, 155, 115, 8), ('Salamence', 165, 160, 100, 8),
                    ('Garchomp', 175, 155, 115, 9), ('Lucario', 130, 130, 90, 10),
                    ('Gardevoir', 135, 85, 85, 11), ('Metagross', 150, 155, 150, 10),
                    ('Tyranitar', 170, 155, 130, 9), ('Machamp', 160, 150, 100, 9),
                    ('Snorlax', 220, 130, 85, 9), ('Steelix', 155, 105, 200, 10),
                    ('Aggron', 140, 130, 180, 10), ('Milotic', 165, 80, 95, 2),
                    ('Flygon', 150, 120, 100, 8), ('Volcarona', 155, 80, 85, 1),
                    ('Haxorus', 145, 167, 110, 8), ('Hydreigon', 160, 125, 110, 12),
                    ('Togekiss', 155, 70, 115, 11), ('Goodra', 160, 120, 90, 8);
            """);

            // 4. ===== SKILL =====
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

            // 5. ===== RELASI =====
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pokemon_skill (
                    pokemon_id INTEGER,
                    skill_id INTEGER,
                    FOREIGN KEY (pokemon_id) REFERENCES pokemon(id),
                    FOREIGN KEY (skill_id) REFERENCES skill(id)
                );
            """);

            System.out.println("✅ Database siap! Total: 30 Legendary & 30 Normal Pokemon.");

        } catch (Exception e) {
            System.err.println("❌ Terjadi kesalahan saat inisialisasi:");
            e.printStackTrace();
        }
    }
}