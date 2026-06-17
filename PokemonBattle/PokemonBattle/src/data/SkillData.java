package data;

import java.util.ArrayList;

import Models.Skill;
import utils.Type;
import utils.StatusEffect;

public class SkillData {

    // ===== AMBIL SEMUA SKILL (SIMULASI DATABASE) =====
    public static ArrayList<Skill> getAllSkills() {

        ArrayList<Skill> skills = new ArrayList<>();

        // ===== ELECTRIC =====
        skills.add(new Skill("Thunder", Type.ELECTRIC, 15, StatusEffect.PARALYZE, 0.3));
        skills.add(new Skill("Spark", Type.ELECTRIC, 10, StatusEffect.PARALYZE, 0.2));

        // ===== FIRE =====
        skills.add(new Skill("Fire Blast", Type.FIRE, 20, StatusEffect.BURN, 0.4));
        skills.add(new Skill("Flame Wheel", Type.FIRE, 12, StatusEffect.BURN, 0.25));

        // ===== WATER =====
        skills.add(new Skill("Ice Beam", Type.WATER, 18, StatusEffect.FREEZE, 0.25));

        // ===== GRASS =====
        skills.add(new Skill("Toxic", Type.GRASS, 10, StatusEffect.POISON, 0.5));

        // ===== NORMAL (UNIVERSAL) =====
        skills.add(new Skill("Quick Attack", Type.NORMAL, 8, StatusEffect.NONE, 0));

        return skills;
    }

    // ===== FILTER SKILL SESUAI TYPE =====
    public static ArrayList<Skill> getSkillsByType(Type type) {

        ArrayList<Skill> allSkills = getAllSkills();
        ArrayList<Skill> result = new ArrayList<>();

        for (Skill skill : allSkills) {
            if (skill.getType() == type || skill.getType() == Type.NORMAL) {
                result.add(skill);
            }
        }

        return result;
    }
}