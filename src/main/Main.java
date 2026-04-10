package main;

import java.util.ArrayList;
import java.util.Random;

import Models.Pokemon;
import Models.Skill;
import Models.BasicPokemon;
import battles.BattleSystem;
import data.SkillData;
import utils.Type;

public class Main {
    public static void main(String[] args) {

        // ===== BUAT POKEMON =====
        Pokemon pikachu = new BasicPokemon("Pikachu", 800, 20, 5, Type.ELECTRIC);
        Pokemon charizard = new BasicPokemon("Charizard", 920, 25, 8, Type.FIRE);

        // ===== AMBIL SKILL SESUAI TYPE =====
        ArrayList<Skill> pikachuSkills = SkillData.getSkillsByType(Type.ELECTRIC);
        ArrayList<Skill> charizardSkills = SkillData.getSkillsByType(Type.FIRE);

        Random rand = new Random();

        // ===== RANDOM 2 SKILL UNTUK PIKACHU =====
        while (pikachu.getSkills().size() < 2) {
            Skill s = pikachuSkills.get(rand.nextInt(pikachuSkills.size()));

            if (!pikachu.getSkills().contains(s)) {
                pikachu.addSkill(s);
            }
        }

        // ===== RANDOM 2 SKILL UNTUK CHARIZARD =====
        while (charizard.getSkills().size() < 2) {
            Skill s = charizardSkills.get(rand.nextInt(charizardSkills.size()));

            if (!charizard.getSkills().contains(s)) {
                charizard.addSkill(s);
            }
        }

        // ===== START BATTLE =====
        BattleSystem battle = new BattleSystem(pikachu, charizard);
        battle.startBattle();
    }
}