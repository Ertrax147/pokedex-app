package com.pokedex.app.repository;

import com.pokedex.app.model.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    
    // Buscar por nombre (ignorando mayúsculas/minúsculas)
    Optional<Pokemon> findByNameIgnoreCase(String name);
    
    // Buscar por número de Pokédex
    Optional<Pokemon> findByPokedexNumber(Integer pokedexNumber);
    
    // Buscar por tipo
    List<Pokemon> findByTypeIgnoreCase(String type);
    
    // Buscar por tipo primario o secundario
    @Query("SELECT p FROM Pokemon p WHERE LOWER(p.type) = LOWER(:type) OR LOWER(p.secondaryType) = LOWER(:type)")
    List<Pokemon> findByTypeOrSecondaryTypeIgnoreCase(@Param("type") String type);
    
    // Buscar por nombre que contenga el texto (ignorando mayúsculas/minúsculas)
    List<Pokemon> findByNameContainingIgnoreCase(String name);
    
    // Buscar por rango de números de Pokédex
    List<Pokemon> findByPokedexNumberBetween(Integer start, Integer end);
    
    // Buscar Pokémon con estadísticas totales superiores a un valor
    @Query("SELECT p FROM Pokemon p WHERE (p.hp + p.attack + p.defense + p.specialAttack + p.specialDefense + p.speed) > :minTotalStats")
    List<Pokemon> findByTotalStatsGreaterThan(@Param("minTotalStats") Integer minTotalStats);
    
    // Buscar los Pokémon con las mejores estadísticas de ataque
    @Query("SELECT p FROM Pokemon p ORDER BY p.attack DESC")
    List<Pokemon> findTopByAttackOrderByAttackDesc();
    
    // Buscar los Pokémon con las mejores estadísticas de velocidad
    @Query("SELECT p FROM Pokemon p ORDER BY p.speed DESC")
    List<Pokemon> findTopBySpeedOrderBySpeedDesc();
    
    // Contar Pokémon por tipo
    @Query("SELECT p.type, COUNT(p) FROM Pokemon p GROUP BY p.type ORDER BY COUNT(p) DESC")
    List<Object[]> countPokemonByType();
    
    // Verificar si existe un Pokémon por nombre
    boolean existsByNameIgnoreCase(String name);
    


    
    // Verificar si existe un Pokémon por número de Pokédex
    boolean existsByPokedexNumber(Integer pokedexNumber);
} 