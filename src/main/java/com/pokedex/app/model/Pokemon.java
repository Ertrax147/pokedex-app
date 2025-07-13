package com.pokedex.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Entity
@Table(name = "pokemon")
public class Pokemon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(unique = true, nullable = false)
    private String name;
    
    @NotNull(message = "El número de Pokédex es obligatorio")
    @Min(value = 1, message = "El número de Pokédex debe ser mayor a 0")
    @Column(unique = true, nullable = false)
    private Integer pokedexNumber;
    
    @NotBlank(message = "El tipo es obligatorio")
    private String type;
    
    private String secondaryType;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Column(length = 1000)
    private String description;
    
    @DecimalMin(value = "0.1", message = "La altura debe ser un valor positivo")
    private Double height;

    @DecimalMin(value = "0.1", message = "El peso debe ser un valor positivo")
    private Double weight;
    
    @Min(value = 1, message = "Los HP deben ser mayor a 0")
    @Max(value = 255, message = "Los HP no pueden exceder 255")
    private Integer hp;
    
    @Min(value = 1, message = "El ataque debe ser mayor a 0")
    @Max(value = 255, message = "El ataque no puede exceder 255")
    private Integer attack;
    
    @Min(value = 1, message = "La defensa debe ser mayor a 0")
    @Max(value = 255, message = "La defensa no puede exceder 255")
    private Integer defense;
    
    @Min(value = 1, message = "El ataque especial debe ser mayor a 0")
    @Max(value = 255, message = "El ataque especial no puede exceder 255")
    private Integer specialAttack;
    
    @Min(value = 1, message = "La defensa especial debe ser mayor a 0")
    @Max(value = 255, message = "La defensa especial no puede exceder 255")
    private Integer specialDefense;
    



    
    @Min(value = 1, message = "La velocidad debe ser mayor a 0")
    @Max(value = 255, message = "La velocidad no puede exceder 255")
    private Integer speed;
    
    private String imageUrl;
    
    private String evolutionChain;
    
    // Campos para información detallada de evolución
    private Integer evolutionLevel;
    private String evolutionMethod; // "level", "stone", "trade", "friendship", etc.
    private Integer previousEvolutionPokedexNumber; // Número de Pokédex del Pokémon del que evoluciona
    private Integer nextEvolutionPokedexNumber; // Número de Pokédex del Pokémon al que evoluciona
    
    // Constructores
    public Pokemon() {}
    
    public Pokemon(String name, Integer pokedexNumber, String type, String description) {
        this.name = name;
        this.pokedexNumber = pokedexNumber;
        this.type = type;
        this.description = description;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getPokedexNumber() {
        return pokedexNumber;
    }
    
    public void setPokedexNumber(Integer pokedexNumber) {
        this.pokedexNumber = pokedexNumber;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getSecondaryType() {
        return secondaryType;
    }
    
    public void setSecondaryType(String secondaryType) {
        this.secondaryType = secondaryType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double getHeight() {
        return height;
    }
    
    public void setHeight(Double height) {
        this.height = height;
    }
    
    public Double getWeight() {
        return weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public Integer getHp() {
        return hp;
    }
    
    public void setHp(Integer hp) {
        this.hp = hp;
    }
    
    public Integer getAttack() {
        return attack;
    }
    
    public void setAttack(Integer attack) {
        this.attack = attack;
    }
    
    public Integer getDefense() {
        return defense;
    }
    
    public void setDefense(Integer defense) {
        this.defense = defense;
    }
    
    public Integer getSpecialAttack() {
        return specialAttack;
    }
    
    public void setSpecialAttack(Integer specialAttack) {
        this.specialAttack = specialAttack;
    }
    
    public Integer getSpecialDefense() {
        return specialDefense;
    }
    
    public void setSpecialDefense(Integer specialDefense) {
        this.specialDefense = specialDefense;
    }
    
    public Integer getSpeed() {
        return speed;
    }
    
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getEvolutionChain() {
        return evolutionChain;
    }
    
    public void setEvolutionChain(String evolutionChain) {
        this.evolutionChain = evolutionChain;
    }
    
    // Getters y Setters para información de evolución
    public Integer getEvolutionLevel() {
        return evolutionLevel;
    }
    
    public void setEvolutionLevel(Integer evolutionLevel) {
        this.evolutionLevel = evolutionLevel;
    }
    
    public String getEvolutionMethod() {
        return evolutionMethod;
    }
    
    public void setEvolutionMethod(String evolutionMethod) {
        this.evolutionMethod = evolutionMethod;
    }
    
    public Integer getPreviousEvolutionPokedexNumber() {
        return previousEvolutionPokedexNumber;
    }
    
    public void setPreviousEvolutionPokedexNumber(Integer previousEvolutionPokedexNumber) {
        this.previousEvolutionPokedexNumber = previousEvolutionPokedexNumber;
    }
    
    public Integer getNextEvolutionPokedexNumber() {
        return nextEvolutionPokedexNumber;
    }
    
    public void setNextEvolutionPokedexNumber(Integer nextEvolutionPokedexNumber) {
        this.nextEvolutionPokedexNumber = nextEvolutionPokedexNumber;
    }
    
    // Método para calcular el total de estadísticas
    public Integer getTotalStats() {
        return (hp != null ? hp : 0) + 
               (attack != null ? attack : 0) + 
               (defense != null ? defense : 0) + 
               (specialAttack != null ? specialAttack : 0) + 
               (specialDefense != null ? specialDefense : 0) + 
               (speed != null ? speed : 0);
    }
    
    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pokedexNumber=" + pokedexNumber +
                ", type='" + type + '\'' +
                '}';
    }
} 