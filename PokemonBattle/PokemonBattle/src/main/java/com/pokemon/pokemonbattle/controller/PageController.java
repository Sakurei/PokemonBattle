// package com.pokemon.pokemonbattle.controller;

// import com.pokemon.pokemonbattle.data.SkillData;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;

// /**
//  * Page Controller - Handles HTML template rendering
//  */
// @Controller
// public class PageController {

//     @GetMapping("/")
//     public String index(Model model) {
//         model.addAttribute("skills", SkillData.getAllSkills());
//         return "index";
//     }

//     @GetMapping("/game")
//     public String game(Model model) {
//         model.addAttribute("skills", SkillData.getAllSkills());
//         return "game";
//     }
// }


package com.pokemon.pokemonbattle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Page Controller - Handles HTML template rendering
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        // Otomatis mengarahkan localhost:8080/ ke halaman login
        return "redirect:/login"; 
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/collection")
    public String collection() {
        return "collection";
    }

    @GetMapping("/inventory")
    public String inventory() {
        return "inventory";
    }

    @GetMapping("/battle-home")
    public String battleHome() {
        return "battle-home";
    }

    @GetMapping("/battle-ingame")
    public String battleIngame() {
        return "battle-ingame";
    }

    @GetMapping("/battle-result")
    public String battleResult() {
        return "battle-result";
    }
}