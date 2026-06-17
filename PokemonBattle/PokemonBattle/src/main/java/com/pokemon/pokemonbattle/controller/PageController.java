package com.pokemon.pokemonbattle.controller;

import com.pokemon.pokemonbattle.data.SkillData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Page Controller - Handles HTML template rendering
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("skills", SkillData.getAllSkills());
        return "index";
    }

    @GetMapping("/game")
    public String game(Model model) {
        model.addAttribute("skills", SkillData.getAllSkills());
        return "game";
    }
}
