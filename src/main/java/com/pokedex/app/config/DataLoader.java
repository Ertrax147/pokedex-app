package com.pokedex.app.config;

import com.pokedex.app.model.Pokemon;
import com.pokedex.app.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private PokemonService pokemonService;
    
    @Override
    public void run(String... args) throws Exception {
        loadInitialPokemon();
    }
    
    private void loadInitialPokemon() {
        // Borrar todos los Pokémon existentes
        pokemonService.deleteAllPokemon();
        // Pokémon iniciales de la primera generación
        createPokemon("Bulbasaur", 1, "Planta", "Veneno",
            "Un extraño semilla fue plantada en su espalda al nacer. La planta y el Pokémon crecen juntos.",
            0.7, 6.9, 45, 49, 49, 65, 65, 45,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            "Bulbasaur → Ivysaur → Venusaur",
            null, 16, "level", null, 2);

        createPokemon("Ivysaur", 2, "Planta", "Veneno",
            "Cuando el bulbo en su espalda crece, parece que no puede ponerse de pie en sus patas traseras.",
            1.0, 13.0, 60, 62, 63, 80, 80, 60,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png",
            "Bulbasaur → Ivysaur → Venusaur",
            16, 32, "level", 1, 3);

        createPokemon("Venusaur", 3, "Planta", "Veneno",
            "La planta florece cuando absorbe energía solar. Permanecerá en movimiento buscando la luz del sol.",
            2.0, 100.0, 80, 82, 83, 100, 100, 80,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/3.png",
            "Bulbasaur → Ivysaur → Venusaur",
            32, null, "level", 2, null);

        createPokemon("Charmander", 4, "Fuego", null,
            "Prefiere las cosas calientes. Se dice que cuando llueve sale vapor de la punta de su cola.",
            0.6, 8.5, 39, 52, 43, 60, 50, 65,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png",
            "Charmander → Charmeleon → Charizard",
            null, 16, "level", null, 5);

        createPokemon("Charmeleon", 5, "Fuego", null,
            "Tiene una naturaleza bárbara. En batalla, lanza su cola ardiente y corta con garras afiladas.",
            1.1, 19.0, 58, 64, 58, 80, 65, 80,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/5.png",
            "Charmander → Charmeleon → Charizard",
            16, 36, "level", 4, 6);

        createPokemon("Charizard", 6, "Fuego", "Volador",
            "Escupe un fuego tan caliente que funde la roca. Causa incendios forestales sin querer.",
            1.7, 90.5, 78, 84, 78, 109, 85, 100,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png",
            "Charmander → Charmeleon → Charizard",
            36, null, "level", 5, null);

        createPokemon("Squirtle", 7, "Agua", null,
            "Cuando retrae su largo cuello dentro de la concha, lanza un chorro de agua vigorosa.",
            0.5, 9.0, 44, 48, 65, 50, 64, 43,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png",
            "Squirtle → Wartortle → Blastoise",
            null, 16, "level", null, 8);

        createPokemon("Wartortle", 8, "Agua", null,
            "Se reconoce por su cola larga y peluda. Se vuelve más oscuro con la edad. Rasca a sus enemigos con sus garras afiladas.",
            1.0, 22.5, 59, 63, 80, 65, 80, 58,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/8.png",
            "Squirtle → Wartortle → Blastoise",
            16, 36, "level", 7, 9);

        createPokemon("Blastoise", 9, "Agua", null,
            "Aprieta a su enemigo con su peso corporal para causar desmayos. En un pellizco, se retirará dentro de su concha.",
            1.6, 85.5, 79, 83, 100, 85, 105, 78,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/9.png",
            "Squirtle → Wartortle → Blastoise",
            36, null, "level", 8, null);

        createPokemon("Pikachu", 25, "Eléctrico", null,
            "Cuando varios de estos Pokémon se juntan, su electricidad puede causar tormentas eléctricas.",
            0.4, 6.0, 35, 55, 40, 50, 50, 90,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
            "Pichu → Pikachu → Raichu",
            20, null, "friendship", 172, 26);

        createPokemon("Raichu", 26, "Eléctrico", null,
            "Su cola larga sirve como tierra para protegerse a sí mismo de su propio poder eléctrico de alto voltaje.",
            0.8, 30.0, 60, 90, 55, 90, 80, 110,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/26.png",
            "Pichu → Pikachu → Raichu",
            null, null, "thunderstone", 25, null);

        createPokemon("Pichu", 172, "Eléctrico", null,
            "Es pequeño y aún no puede almacenar mucha electricidad. Cada vez que se asusta, descarga electricidad.",
            0.3, 2.0, 20, 40, 15, 35, 35, 60,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/172.png",
            "Pichu → Pikachu → Raichu",
            null, 20, "friendship", null, 25);

        createPokemon("Mewtwo", 150, "Psíquico", null,
            "Su ADN es casi el mismo que el de Mew. Sin embargo, su tamaño y disposición son muy diferentes.",
            2.0, 122.0, 106, 110, 90, 154, 90, 130,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/150.png",
            "Mewtwo",
            null, null, "none", null, null);

        createPokemon("Mew", 151, "Psíquico", null,
            "Tan raro que se dice que es un Pokémon legendario. Solo unos pocos han visto este Pokémon.",
            0.4, 4.0, 100, 100, 100, 100, 100, 100,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/151.png",
            "Mew",
            null, null, "none", null, null);
        
        System.out.println("¡Datos iniciales de Pokémon cargados exitosamente!");
    }
    
    private void createPokemon(String name, Integer pokedexNumber, String type, String secondaryType,
                              String description, Double height, Double weight,
                              Integer hp, Integer attack, Integer defense,
                              Integer specialAttack, Integer specialDefense, Integer speed,
                              String imageUrl, String evolutionChain,
                              Integer evolutionLevel, Integer maxLevel, String method,
                              Integer previousEvolutionPokedexNumber, Integer nextEvolutionPokedexNumber) {
        try {
            Pokemon pokemon = new Pokemon();
            pokemon.setName(name);
            pokemon.setPokedexNumber(pokedexNumber);
            pokemon.setType(type);
            pokemon.setSecondaryType(secondaryType);
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
            pokemon.setEvolutionChain(evolutionChain);
            pokemon.setEvolutionLevel(evolutionLevel);
            pokemon.setEvolutionMethod(method);
            pokemon.setPreviousEvolutionPokedexNumber(previousEvolutionPokedexNumber);
            pokemon.setNextEvolutionPokedexNumber(nextEvolutionPokedexNumber);
            
            pokemonService.savePokemon(pokemon);
        } catch (Exception e) {
            System.err.println("Error al crear Pokémon " + name + ": " + e.getMessage());
        }
    }
} 