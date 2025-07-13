package com.pokedex.app.service;

import com.pokedex.app.model.Pokemon;
import com.pokedex.app.repository.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Transactional
public class PokemonService {
    
    @Autowired
    private PokemonRepository pokemonRepository;
    
    // Obtener todos los Pokémon
    public List<Pokemon> getAllPokemon() {
        return pokemonRepository.findAll();
    }
    
    // Obtener Pokémon por ID
    public Optional<Pokemon> getPokemonById(Long id) {
        return pokemonRepository.findById(id);
    }
    
    // Obtener Pokémon por nombre
    public Optional<Pokemon> getPokemonByName(String name) {
        return pokemonRepository.findByNameIgnoreCase(name);
    }
    
    // Obtener Pokémon por número de Pokédex
    public Optional<Pokemon> getPokemonByPokedexNumber(Integer pokedexNumber) {
        return pokemonRepository.findByPokedexNumber(pokedexNumber);
    }
    
    // Buscar Pokémon por nombre (búsqueda parcial)
    public List<Pokemon> searchPokemonByName(String name) {
        return pokemonRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Obtener Pokémon por tipo
    public List<Pokemon> getPokemonByType(String type) {
        return pokemonRepository.findByTypeOrSecondaryTypeIgnoreCase(type);
    }
    
    // Obtener Pokémon por rango de números de Pokédex
    public List<Pokemon> getPokemonByRange(Integer start, Integer end) {
        return pokemonRepository.findByPokedexNumberBetween(start, end);
    }
    
    // Obtener Pokémon con estadísticas totales superiores a un valor
    public List<Pokemon> getPokemonByMinTotalStats(Integer minTotalStats) {
        return pokemonRepository.findByTotalStatsGreaterThan(minTotalStats);
    }
    
    // Obtener los Pokémon con mejor ataque
    public List<Pokemon> getTopAttackers() {
        return pokemonRepository.findTopByAttackOrderByAttackDesc();
    }
    
    // Obtener los Pokémon con mejor velocidad
    public List<Pokemon> getTopSpeedsters() {
        return pokemonRepository.findTopBySpeedOrderBySpeedDesc();
    }
    
    // Obtener estadísticas por tipo
    public List<Object[]> getPokemonStatsByType() {
        return pokemonRepository.countPokemonByType();
    }
    
    // Guardar un nuevo Pokémon
    public Pokemon savePokemon(Pokemon pokemon) {
        // Validar que no exista un Pokémon con el mismo nombre o número
        if (pokemon.getId() == null) {
            if (pokemonRepository.existsByNameIgnoreCase(pokemon.getName())) {
                throw new RuntimeException("Ya existe un Pokémon con el nombre: " + pokemon.getName());
            }
            if (pokemonRepository.existsByPokedexNumber(pokemon.getPokedexNumber())) {
                throw new RuntimeException("Ya existe un Pokémon con el número: " + pokemon.getPokedexNumber());
            }
        }
        return pokemonRepository.save(pokemon);
    }
    
    // Actualizar un Pokémon existente
    public Pokemon updatePokemon(Long id, Pokemon pokemonDetails) {
        Optional<Pokemon> existingPokemon = pokemonRepository.findById(id);
        if (existingPokemon.isPresent()) {
            Pokemon pokemon = existingPokemon.get();
            
            // Actualizar campos
            pokemon.setName(pokemonDetails.getName());
            pokemon.setPokedexNumber(pokemonDetails.getPokedexNumber());
            pokemon.setType(pokemonDetails.getType());
            pokemon.setSecondaryType(pokemonDetails.getSecondaryType());
            pokemon.setDescription(pokemonDetails.getDescription());
            pokemon.setHeight(pokemonDetails.getHeight());
            pokemon.setWeight(pokemonDetails.getWeight());
            pokemon.setHp(pokemonDetails.getHp());
            pokemon.setAttack(pokemonDetails.getAttack());
            pokemon.setDefense(pokemonDetails.getDefense());
            pokemon.setSpecialAttack(pokemonDetails.getSpecialAttack());
            pokemon.setSpecialDefense(pokemonDetails.getSpecialDefense());
            pokemon.setSpeed(pokemonDetails.getSpeed());
            pokemon.setImageUrl(pokemonDetails.getImageUrl());
            pokemon.setEvolutionChain(pokemonDetails.getEvolutionChain());
            
            return pokemonRepository.save(pokemon);
        } else {
            throw new RuntimeException("Pokémon no encontrado con ID: " + id);
        }
    }
    
    // Eliminar un Pokémon
    public void deletePokemon(Long id) {
        if (pokemonRepository.existsById(id)) {
            pokemonRepository.deleteById(id);
        } else {
            throw new RuntimeException("Pokémon no encontrado con ID: " + id);
        }
    }
    
    // Verificar si existe un Pokémon
    public boolean existsPokemon(Long id) {
        return pokemonRepository.existsById(id);
    }
    
    // Contar total de Pokémon
    public long countPokemon() {
        return pokemonRepository.count();
    }
    
    // Borrar todos los Pokémon
    public void deleteAllPokemon() {
        pokemonRepository.deleteAll();
    }
    
    // Métodos para información de evolución
    public Optional<Pokemon> getPreviousEvolution(Long pokemonId) {
        Optional<Pokemon> pokemon = pokemonRepository.findById(pokemonId);
        if (pokemon.isPresent() && pokemon.get().getPreviousEvolutionPokedexNumber() != null) {
            return pokemonRepository.findByPokedexNumber(pokemon.get().getPreviousEvolutionPokedexNumber());
        }
        return Optional.empty();
    }
    
    public Optional<Pokemon> getNextEvolution(Long pokemonId) {
        Optional<Pokemon> pokemon = pokemonRepository.findById(pokemonId);
        if (pokemon.isPresent() && pokemon.get().getNextEvolutionPokedexNumber() != null) {
            return pokemonRepository.findByPokedexNumber(pokemon.get().getNextEvolutionPokedexNumber());
        }
        return Optional.empty();
    }
    

    // Archivo: src/main/java/com/pokedex/app/service/PokemonService.java

    public List<Pokemon> getEvolutionChain(Long pokemonId) {
        List<Pokemon> evolutionChain = new ArrayList<>();
        
        // 1. Obtener el Pokémon actual que se está viendo
        Optional<Pokemon> currentPokemonOptional = pokemonRepository.findById(pokemonId);
        if (!currentPokemonOptional.isPresent()) {
            return evolutionChain; // Devuelve una lista vacía si el Pokémon no existe
        }

        Pokemon pokemonToTrace = currentPokemonOptional.get();

        // 2. Encontrar la forma base de la evolución retrocediendo en la cadena
        while (pokemonToTrace.getPreviousEvolutionPokedexNumber() != null) {
            Optional<Pokemon> previousForm = pokemonRepository.findByPokedexNumber(pokemonToTrace.getPreviousEvolutionPokedexNumber());
            if (previousForm.isPresent()) {
                pokemonToTrace = previousForm.get();
            } else {
                // Si la cadena está rota (datos inconsistentes), detenemos la búsqueda
                break; 
            }
        }

        // 3. Construir la cadena completa hacia adelante desde la forma base
        while (pokemonToTrace != null) {
            evolutionChain.add(pokemonToTrace); // Añadir la evolución actual a la lista
            
            if (pokemonToTrace.getNextEvolutionPokedexNumber() != null) {
                // Buscar la siguiente evolución por su número de Pokédex
                Optional<Pokemon> nextForm = pokemonRepository.findByPokedexNumber(pokemonToTrace.getNextEvolutionPokedexNumber());
                pokemonToTrace = nextForm.orElse(null); // Actualizar al siguiente o terminar si no se encuentra
            } else {
                pokemonToTrace = null; // Terminar el bucle si no hay más evoluciones
            }
        }

        return evolutionChain;
    }
    
    // Obtener Pokémon paginados (para futuras implementaciones)
    public List<Pokemon> getPokemonWithPagination(int page, int size) {
        // Implementación básica - se puede mejorar con Pageable
        List<Pokemon> allPokemon = pokemonRepository.findAll();
        int start = page * size;
        int end = Math.min(start + size, allPokemon.size());
        
        if (start >= allPokemon.size()) {
            return List.of();
        }
        
        return allPokemon.subList(start, end);
    }
} 