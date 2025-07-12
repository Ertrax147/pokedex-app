package com.pokedex.app.controller;

import com.pokedex.app.model.Pokemon;
import com.pokedex.app.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class PokemonWebController {
    
    @Autowired
    private PokemonService pokemonService;
    
    // Página principal - Lista de todos los Pokémon
    @GetMapping("/")
    public String home(Model model) {
        List<Pokemon> pokemonList = pokemonService.getAllPokemon();
        model.addAttribute("pokemonList", pokemonList);
        model.addAttribute("totalPokemon", pokemonService.countPokemon());
        return "index";
    }
    
    // Página de detalles de un Pokémon
    @GetMapping("/pokemon/{id}")
    public String pokemonDetail(@PathVariable Long id, Model model) {
        Optional<Pokemon> pokemon = pokemonService.getPokemonById(id);
        if (pokemon.isPresent()) {
            Pokemon currentPokemon = pokemon.get();
            model.addAttribute("pokemon", currentPokemon);
            
            // Agregar información de evolución
            List<Pokemon> evolutionChain = pokemonService.getEvolutionChain(id);
            model.addAttribute("evolutionChain", evolutionChain);
            
            // Obtener evolución previa y siguiente
            Optional<Pokemon> previousEvolution = pokemonService.getPreviousEvolution(id);
            Optional<Pokemon> nextEvolution = pokemonService.getNextEvolution(id);
            
            model.addAttribute("previousEvolution", previousEvolution.orElse(null));
            model.addAttribute("nextEvolution", nextEvolution.orElse(null));
            
            return "pokemon-detail";
        } else {
            return "redirect:/";
        }
    }
    
    // Página de búsqueda
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q, Model model) {
        if (q != null && !q.trim().isEmpty()) {
            List<Pokemon> results = pokemonService.searchPokemonByName(q);
            model.addAttribute("searchResults", results);
            model.addAttribute("searchQuery", q);
            model.addAttribute("resultCount", results.size());
        }
        return "search";
    }
    
    // Página de Pokémon por tipo
    @GetMapping("/type/{type}")
    public String pokemonByType(@PathVariable String type, Model model) {
        List<Pokemon> pokemonList = pokemonService.getPokemonByType(type);
        model.addAttribute("pokemonList", pokemonList);
        model.addAttribute("type", type);
        model.addAttribute("count", pokemonList.size());
        return "pokemon-by-type";
    }
    
    // Página de mejores atacantes
    @GetMapping("/top/attackers")
    public String topAttackers(Model model) {
        List<Pokemon> attackers = pokemonService.getTopAttackers();
        model.addAttribute("pokemonList", attackers);
        model.addAttribute("title", "Mejores Atacantes");
        return "top-pokemon";
    }
    
    // Página de Pokémon más rápidos
    @GetMapping("/top/speedsters")
    public String topSpeedsters(Model model) {
        List<Pokemon> speedsters = pokemonService.getTopSpeedsters();
        model.addAttribute("pokemonList", speedsters);
        model.addAttribute("title", "Pokémon Más Rápidos");
        return "top-pokemon";
    }
    
    // Página de estadísticas
    @GetMapping("/stats")
    public String stats(Model model) {
        List<Object[]> statsByType = pokemonService.getPokemonStatsByType();
        model.addAttribute("statsByType", statsByType);
        model.addAttribute("totalPokemon", pokemonService.countPokemon());
        return "stats";
    }
    
    // Página para agregar nuevo Pokémon
    @GetMapping("/pokemon/new")
    public String newPokemonForm(Model model) {
        model.addAttribute("pokemon", new Pokemon());
        return "pokemon-form";
    }
    
    // Página para editar Pokémon
    @GetMapping("/pokemon/{id}/edit")
    public String editPokemonForm(@PathVariable Long id, Model model) {
        Optional<Pokemon> pokemon = pokemonService.getPokemonById(id);
        if (pokemon.isPresent()) {
            model.addAttribute("pokemon", pokemon.get());
            return "pokemon-form";
        } else {
            return "redirect:/";
        }
    }
    
    // Procesar formulario de creación/edición
    @PostMapping("/pokemon/save")
    public String savePokemon(@ModelAttribute Pokemon pokemon) {
        try {
            Pokemon savedPokemon;
            if (pokemon.getId() != null) {
                savedPokemon = pokemonService.updatePokemon(pokemon.getId(), pokemon);
            } else {
                savedPokemon = pokemonService.savePokemon(pokemon);
            }
            return "redirect:/pokemon/" + savedPokemon.getId();
        } catch (RuntimeException e) {
            // En una aplicación real, manejarías el error de forma más elegante
            return "redirect:/";
        }
    }
    
    // Eliminar Pokémon
    @PostMapping("/pokemon/{id}/delete")
    public String deletePokemon(@PathVariable Long id) {
        try {
            pokemonService.deletePokemon(id);
        } catch (RuntimeException e) {
            // Manejar error
        }
        return "redirect:/";
    }
    
    // Página de rango de Pokémon
    @GetMapping("/range")
    public String pokemonRange(
            @RequestParam(defaultValue = "1") Integer start,
            @RequestParam(defaultValue = "151") Integer end,
            Model model) {
        List<Pokemon> pokemonList = pokemonService.getPokemonByRange(start, end);
        model.addAttribute("pokemonList", pokemonList);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("count", pokemonList.size());
        return "pokemon-range";
    }
    
    // Página de Pokémon con estadísticas mínimas
    @GetMapping("/stats/min")
    public String pokemonByMinStats(
            @RequestParam(defaultValue = "400") Integer minStats,
            Model model) {
        List<Pokemon> pokemonList = pokemonService.getPokemonByMinTotalStats(minStats);
        model.addAttribute("pokemonList", pokemonList);
        model.addAttribute("minStats", minStats);
        model.addAttribute("count", pokemonList.size());
        return "pokemon-by-stats";
    }
} 