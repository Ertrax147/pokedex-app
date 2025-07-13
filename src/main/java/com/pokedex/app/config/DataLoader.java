package com.pokedex.app.config;

import com.pokedex.app.model.Pokemon;
import com.pokedex.app.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private PokemonService pokemonService;
    
    @Override
    public void run(String... args) throws Exception {
        if (pokemonService.countPokemon() == 0) {
            loadFromPokeApiGeneration1();
            updateEvolutionData(new RestTemplate());
        }
    }
    
    private void loadFromPokeApiGeneration1() {
        RestTemplate restTemplate = new RestTemplate();
        String gen1Url = "https://pokeapi.co/api/v2/generation/1/";
        ResponseEntity<Map> response = restTemplate.getForEntity(gen1Url, Map.class);
        List<LinkedHashMap<String, String>> species = (List<LinkedHashMap<String, String>>) response.getBody().get("pokemon_species");
        species.sort((a, b) -> {
            // Ordenar por número de pokédex (url termina en /{id}/)
            int idA = Integer.parseInt(a.get("url").replaceAll(".*/([0-9]+)/", "$1"));
            int idB = Integer.parseInt(b.get("url").replaceAll(".*/([0-9]+)/", "$1"));
            return Integer.compare(idA, idB);
        });
        int count = 0;
        for (LinkedHashMap<String, String> specie : species) {
            if (++count > 151) break;
            String pokeUrl = specie.get("url").replace("pokemon-species", "pokemon");
            // pokeUrl ejemplo: https://pokeapi.co/api/v2/pokemon/1/
            try {
                Map pokeData = restTemplate.getForObject(pokeUrl, Map.class);
                String name = (String) pokeData.get("name");
                Integer pokedexNumber = (Integer) pokeData.get("id");
                List<Map> typesList = (List<Map>) pokeData.get("types");
                String type = typesList.size() > 0 ? (String) ((Map) ((Map) typesList.get(0)).get("type")).get("name") : "";
                String secondaryType = typesList.size() > 1 ? (String) ((Map) ((Map) typesList.get(1)).get("type")).get("name") : null;
                String description = "";
                Double height = ((Integer) pokeData.get("height")) / 10.0;
                Double weight = ((Integer) pokeData.get("weight")) / 10.0;
                Integer hp = null, attack = null, defense = null, specialAttack = null, specialDefense = null, speed = null;
                List<Map> stats = (List<Map>) pokeData.get("stats");
                for (Map stat : stats) {
                    String statName = (String) ((Map)stat.get("stat")).get("name");
                    Integer baseStat = (Integer) stat.get("base_stat");
                    switch (statName) {
                        case "hp": hp = baseStat; break;
                        case "attack": attack = baseStat; break;
                        case "defense": defense = baseStat; break;
                        case "special-attack": specialAttack = baseStat; break;
                        case "special-defense": specialDefense = baseStat; break;
                        case "speed": speed = baseStat; break;
                    }
                }
                String imageUrl = ((Map)((Map)pokeData.get("sprites")).get("other")).get("official-artwork") != null ?
                    (String) ((Map)((Map)((Map)pokeData.get("sprites")).get("other")).get("official-artwork")).get("front_default") : null;
                if (imageUrl == null) imageUrl = (String) ((Map)pokeData.get("sprites")).get("front_default");
                // Cargar descripción desde species endpoint
                Map speciesData = restTemplate.getForObject(specie.get("url"), Map.class);
                List<Map> flavorTextEntries = (List<Map>) speciesData.get("flavor_text_entries");
                for (Map entry : flavorTextEntries) {
                    if ("es".equals(((Map)entry.get("language")).get("name"))) {
                        description = ((String) entry.get("flavor_text")).replaceAll("\n|\f", " ");
                        break;
                    }
                }
                // Guardar Pokémon
                Pokemon pokemon = new Pokemon();
                pokemon.setName(capitalize(name));
                pokemon.setPokedexNumber(pokedexNumber);
                pokemon.setType(capitalize(type));
                pokemon.setSecondaryType(secondaryType != null ? capitalize(secondaryType) : null);
                pokemon.setDescription(description);
                pokemon.setHeight(height);
                pokemon.setWeight(weight);
                pokemon.setHp(hp);
                pokemon.setAttack(attack);
                pokemon.setDefense(defense);
                pokemon.setSpecialAttack(specialAttack);
                pokemon.setSpecialDefense(specialDefense);
                pokemon.setSpeed(speed);
                pokemon.setImageUrl(imageUrl);
                // Cargar información de evolución desde species endpoint
                try {
                    Map evolutionChainMap = (Map) speciesData.get("evolution_chain");
                    String evolutionChainUrl = (String) evolutionChainMap.get("url");
                    if (evolutionChainUrl != null) {
                        Map evolutionChainData = restTemplate.getForObject(evolutionChainUrl, Map.class);
                        Map chain = (Map) evolutionChainData.get("chain");
                        
                        // Buscar este Pokémon en la cadena de evolución
                        Map currentEvolution = findPokemonInChain(chain, pokedexNumber);
                        if (currentEvolution != null) {
                            // Configurar evolución previa
                            List<Map> evolvesTo = (List<Map>) currentEvolution.get("evolves_to");
                            if (evolvesTo != null && !evolvesTo.isEmpty()) {
                                Map nextEvolution = evolvesTo.get(0);
                                Map nextSpecies = (Map) nextEvolution.get("species");
                                String nextUrl = (String) nextSpecies.get("url");
                                Integer nextPokedexNumber = Integer.parseInt(nextUrl.replaceAll(".*/([0-9]+)/", "$1"));
                                pokemon.setNextEvolutionPokedexNumber(nextPokedexNumber);
                                
                                // Configurar nivel de evolución
                                List<Map> evolutionDetails = (List<Map>) nextEvolution.get("evolution_details");
                                if (evolutionDetails != null && !evolutionDetails.isEmpty()) {
                                    Map details = evolutionDetails.get(0);
                                    if (details.get("min_level") != null) {
                                        pokemon.setEvolutionLevel((Integer) details.get("min_level"));
                                        pokemon.setEvolutionMethod("level");
                                    } else if (details.get("item") != null) {
                                        Map item = (Map) details.get("item");
                                        String itemName = (String) item.get("name");
                                        pokemon.setEvolutionMethod(itemName);
                                    } else if (details.get("trigger") != null) {
                                        Map trigger = (Map) details.get("trigger");
                                        String triggerName = (String) trigger.get("name");
                                        pokemon.setEvolutionMethod(triggerName);
                                    }
                                }
                            }
                            
                            // Buscar evolución previa (más complejo, necesitamos recorrer toda la cadena)
                            Integer prevPokedexNumber = findPreviousEvolution(chain, pokedexNumber);
                            if (prevPokedexNumber != null) {
                                pokemon.setPreviousEvolutionPokedexNumber(prevPokedexNumber);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar evolución para " + name + ": " + e.getMessage());
                }
                
                pokemonService.savePokemon(pokemon);
                System.out.println("Pokémon guardado: " + name + " (#" + pokedexNumber + ")");
            } catch (Exception e) {
                System.err.println("Error al cargar Pokémon: " + specie.get("name") + " - " + e.getMessage());
            }
        }
        System.out.println("¡Pokémon de la generación 1 cargados desde PokéAPI!");
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    private Map findPokemonInChain(Map chain, Integer pokedexNumber) {
        Map species = (Map) chain.get("species");
        String url = (String) species.get("url");
        Integer chainPokedexNumber = Integer.parseInt(url.replaceAll(".*/([0-9]+)/", "$1"));
        
        if (chainPokedexNumber.equals(pokedexNumber)) {
            return chain;
        }
        
        List<Map> evolvesTo = (List<Map>) chain.get("evolves_to");
        if (evolvesTo != null) {
            for (Map evolution : evolvesTo) {
                Map result = findPokemonInChain(evolution, pokedexNumber);
                if (result != null) {
                    return result;
                }
            }
        }
        
        return null;
    }
    
    private Integer findPreviousEvolution(Map chain, Integer targetPokedexNumber) {
        List<Map> evolvesTo = (List<Map>) chain.get("evolves_to");
        if (evolvesTo != null) {
            for (Map evolution : evolvesTo) {
                Map species = (Map) evolution.get("species");
                String url = (String) species.get("url");
                Integer evolutionPokedexNumber = Integer.parseInt(url.replaceAll(".*/([0-9]+)/", "$1"));
                
                if (evolutionPokedexNumber.equals(targetPokedexNumber)) {
                    // Encontramos el Pokémon objetivo, su predecesor es el actual
                    Map currentSpecies = (Map) chain.get("species");
                    String currentUrl = (String) currentSpecies.get("url");
                    return Integer.parseInt(currentUrl.replaceAll(".*/([0-9]+)/", "$1"));
                }
                
                // Buscar recursivamente en las evoluciones de este Pokémon
                Integer result = findPreviousEvolution(evolution, targetPokedexNumber);
                if (result != null) {
                    return result;
                }
            }
        }
        
        return null;
    }

    // Después de crear y guardar todos los Pokémon, recorro la cadena evolutiva y actualizo los campos de evolución en el Pokémon de destino
    private void updateEvolutionData(RestTemplate restTemplate) {
        String gen1Url = "https://pokeapi.co/api/v2/generation/1/";
        ResponseEntity<Map> response = restTemplate.getForEntity(gen1Url, Map.class);
        List<LinkedHashMap<String, String>> species = (List<LinkedHashMap<String, String>>) response.getBody().get("pokemon_species");
        for (LinkedHashMap<String, String> specie : species) {
            Map speciesData = restTemplate.getForObject(specie.get("url"), Map.class);
            Map evolutionChainMap = (Map) speciesData.get("evolution_chain");
            String evolutionChainUrl = (String) evolutionChainMap.get("url");
            if (evolutionChainUrl != null) {
                Map evolutionChainData = restTemplate.getForObject(evolutionChainUrl, Map.class);
                Map chain = (Map) evolutionChainData.get("chain");
                processEvolutionChain(chain, null, null, null);
            }
        }
    }

    // Recursivo: actualiza los campos de evolución en el Pokémon de destino
    private void processEvolutionChain(Map chain, Integer prevPokedexNumber, Integer evolutionLevel, String evolutionMethod) {
        Map species = (Map) chain.get("species");
        String url = (String) species.get("url");
        Integer pokedexNumber = Integer.parseInt(url.replaceAll(".*/([0-9]+)/", "$1"));
        if (prevPokedexNumber != null) {
            // Actualiza el Pokémon de destino (el que evoluciona)
            Pokemon pokemon = pokemonService.getPokemonByPokedexNumber(pokedexNumber).orElse(null);
            if (pokemon != null) {
                pokemon.setPreviousEvolutionPokedexNumber(prevPokedexNumber);
                pokemon.setEvolutionLevel(evolutionLevel);
                pokemon.setEvolutionMethod(evolutionMethod);
                pokemonService.savePokemon(pokemon);
            }
            // También actualiza el Pokémon de origen con el campo nextEvolution
            Pokemon prevPokemon = pokemonService.getPokemonByPokedexNumber(prevPokedexNumber).orElse(null);
            if (prevPokemon != null) {
                prevPokemon.setNextEvolutionPokedexNumber(pokedexNumber);
                pokemonService.savePokemon(prevPokemon);
            }
        }
        List<Map> evolvesTo = (List<Map>) chain.get("evolves_to");
        if (evolvesTo != null) {
            for (Map evolution : evolvesTo) {
                List<Map> evolutionDetails = (List<Map>) evolution.get("evolution_details");
                Integer nextLevel = null;
                String nextMethod = null;
                if (evolutionDetails != null && !evolutionDetails.isEmpty()) {
                    Map details = evolutionDetails.get(0);
                    if (details.get("min_level") != null) {
                        nextLevel = (Integer) details.get("min_level");
                        nextMethod = "level";
                    } else if (details.get("item") != null) {
                        Map item = (Map) details.get("item");
                        nextMethod = (String) item.get("name");
                    } else if (details.get("trigger") != null) {
                        Map trigger = (Map) details.get("trigger");
                        nextMethod = (String) trigger.get("name");
                    }
                }
                processEvolutionChain(evolution, pokedexNumber, nextLevel, nextMethod);
            }
        }
    }
} 