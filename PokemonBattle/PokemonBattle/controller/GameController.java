package com.pokemon.pokemonbattle.controller;

import com.pokemon.pokemonbattle.data.SkillData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("skills", SkillData.getAllSkills());
        return "index"; // → resources/templates/index.html
    }
}
