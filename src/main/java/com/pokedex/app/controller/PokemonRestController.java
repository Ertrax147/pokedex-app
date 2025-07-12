package com.pokedex.app.controller;

import com.pokedex.app.model.Pokemon;
import com.pokedex.app.service.PokemonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin(origins = "*")
public class PokemonRestController {
    
    @Autowired
    private PokemonService pokemonService;
    
    // GET /api/pokemon - Obtener todos los Pokémon
    @GetMapping
    public ResponseEntity<List<Pokemon>> getAllPokemon() {
        List<Pokemon> pokemonList = pokemonService.getAllPokemon();
        return ResponseEntity.ok(pokemonList);
    }
    
    // GET /api/pokemon/{id} - Obtener Pokémon por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> getPokemonById(@PathVariable Long id) {
        Optional<Pokemon> pokemon = pokemonService.getPokemonById(id);
        return pokemon.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/pokemon/name/{name} - Obtener Pokémon por nombre
    @GetMapping("/name/{name}")
    public ResponseEntity<Pokemon> getPokemonByName(@PathVariable String name) {
        Optional<Pokemon> pokemon = pokemonService.getPokemonByName(name);
        return pokemon.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/pokemon/number/{number} - Obtener Pokémon por número de Pokédex
    @GetMapping("/number/{number}")
    public ResponseEntity<Pokemon> getPokemonByNumber(@PathVariable Integer number) {
        Optional<Pokemon> pokemon = pokemonService.getPokemonByPokedexNumber(number);
        return pokemon.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/pokemon/search?q={query} - Buscar Pokémon por nombre
    @GetMapping("/search")
    public ResponseEntity<List<Pokemon>> searchPokemon(@RequestParam String q) {
        List<Pokemon> pokemonList = pokemonService.searchPokemonByName(q);
        return ResponseEntity.ok(pokemonList);
    }
    
    // GET /api/pokemon/type/{type} - Obtener Pokémon por tipo
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Pokemon>> getPokemonByType(@PathVariable String type) {
        List<Pokemon> pokemonList = pokemonService.getPokemonByType(type);
        return ResponseEntity.ok(pokemonList);
    }
    
    // GET /api/pokemon/range?start={start}&end={end} - Obtener Pokémon por rango
    @GetMapping("/range")
    public ResponseEntity<List<Pokemon>> getPokemonByRange(
            @RequestParam Integer start, 
            @RequestParam Integer end) {
        List<Pokemon> pokemonList = pokemonService.getPokemonByRange(start, end);
        return ResponseEntity.ok(pokemonList);
    }
    
    // GET /api/pokemon/stats/min/{minStats} - Obtener Pokémon con estadísticas mínimas
    @GetMapping("/stats/min/{minStats}")
    public ResponseEntity<List<Pokemon>> getPokemonByMinStats(@PathVariable Integer minStats) {
        List<Pokemon> pokemonList = pokemonService.getPokemonByMinTotalStats(minStats);
        return ResponseEntity.ok(pokemonList);
    }
    
    // GET /api/pokemon/top/attackers - Obtener mejores atacantes
    @GetMapping("/top/attackers")
    public ResponseEntity<List<Pokemon>> getTopAttackers() {
        List<Pokemon> pokemonList = pokemonService.getTopAttackers();
        return ResponseEntity.ok(pokemonList);
    }
    
    // GET /api/pokemon/top/speedsters - Obtener Pokémon más rápidos
    @GetMapping("/top/speedsters")
    public ResponseEntity<List<Pokemon>> getTopSpeedsters() {
        List<Pokemon> pokemonList = pokemonService.getTopSpeedsters();
        return ResponseEntity.ok(pokemonList);
    }
    
    // GET /api/pokemon/stats/by-type - Obtener estadísticas por tipo
    @GetMapping("/stats/by-type")
    public ResponseEntity<List<Object[]>> getStatsByType() {
        List<Object[]> stats = pokemonService.getPokemonStatsByType();
        return ResponseEntity.ok(stats);
    }
    
    // GET /api/pokemon/count - Contar total de Pokémon
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getPokemonCount() {
        long count = pokemonService.countPokemon();
        Map<String, Long> response = new HashMap<>();
        response.put("total", count);
        return ResponseEntity.ok(response);
    }
    
    // POST /api/pokemon - Crear nuevo Pokémon
    @PostMapping
    public ResponseEntity<?> createPokemon(@Valid @RequestBody Pokemon pokemon) {
        try {
            Pokemon savedPokemon = pokemonService.savePokemon(pokemon);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPokemon);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // PUT /api/pokemon/{id} - Actualizar Pokémon existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePokemon(@PathVariable Long id, @Valid @RequestBody Pokemon pokemon) {
        try {
            Pokemon updatedPokemon = pokemonService.updatePokemon(id, pokemon);
            return ResponseEntity.ok(updatedPokemon);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // DELETE /api/pokemon/{id} - Eliminar Pokémon
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePokemon(@PathVariable Long id) {
        try {
            pokemonService.deletePokemon(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Pokémon eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // GET /api/pokemon/paginated?page={page}&size={size} - Obtener Pokémon paginados
    @GetMapping("/paginated")
    public ResponseEntity<List<Pokemon>> getPaginatedPokemon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<Pokemon> pokemonList = pokemonService.getPokemonWithPagination(page, size);
        return ResponseEntity.ok(pokemonList);
    }
} 