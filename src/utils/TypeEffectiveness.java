package utils;

import utils.Type;

public class TypeEffectiveness {

    public static double getMultiplier(Type attacker, Type defender) {

        // SUPER EFFECTIVE
        if (attacker == Type.FIRE && defender == Type.GRASS) return 2.0;
        if (attacker == Type.WATER && defender == Type.FIRE) return 2.0;
        if (attacker == Type.GRASS && defender == Type.WATER) return 2.0;
        if (attacker == Type.ELECTRIC && defender == Type.WATER) return 2.0;
        if (attacker == Type.ROCK && defender == Type.FLYING) return 2.0;
        if (attacker == Type.FLYING && defender == Type.GRASS) return 2.0;

        // NOT EFFECTIVE
        if (attacker == Type.GRASS && defender == Type.FIRE) return 0.5;
        if (attacker == Type.FIRE && defender == Type.WATER) return 0.5;

        return 1.0;
    }
}