# Pokédex App

Una aplicación web moderna para explorar y gestionar información de Pokémon, desarrollada con Spring Boot y Thymeleaf.

## 🚀 Características

- **Gestión completa de Pokémon**: Crear, leer, actualizar y eliminar Pokémon
- **Búsqueda avanzada**: Buscar por nombre, tipo, número de Pokédex
- **Estadísticas detalladas**: Visualización de todas las estadísticas base
- **Interfaz moderna**: Diseño responsive con Bootstrap 5
- **API REST**: Endpoints para integración con otras aplicaciones
- **Base de datos H2**: Base de datos en memoria para desarrollo
- **Datos iniciales**: Pokémon de la primera generación precargados

## 🛠️ Tecnologías Utilizadas

- **Backend**: Spring Boot 3.2.0
- **Base de Datos**: H2 Database
- **ORM**: Spring Data JPA / Hibernate
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome
- **Validación**: Bean Validation
- **Build Tool**: Maven

## 📋 Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior
- Navegador web moderno

## 🔧 Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd pokedex-app
```

### 2. Compilar el proyecto
```bash
mvn clean compile
```

### 3. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

### 4. Acceder a la aplicación
Abre tu navegador y ve a: `http://localhost:8080`

## 📖 Uso de la Aplicación

### Página Principal
- **URL**: `/`
- **Descripción**: Lista todos los Pokémon con información básica
- **Funcionalidades**: 
  - Ver todos los Pokémon en formato de tarjetas
  - Búsqueda rápida en la barra superior
  - Navegación a otras secciones

### Detalles de Pokémon
- **URL**: `/pokemon/{id}`
- **Descripción**: Vista detallada de un Pokémon específico
- **Funcionalidades**:
  - Información completa del Pokémon
  - Estadísticas con barras de progreso
  - Cadena de evolución
  - Opciones de editar y eliminar

### Búsqueda
- **URL**: `/search?q={query}`
- **Descripción**: Buscar Pokémon por nombre
- **Funcionalidades**:
  - Búsqueda parcial por nombre
  - Resultados en tiempo real

### Pokémon por Tipo
- **URL**: `/type/{type}`
- **Descripción**: Filtrar Pokémon por tipo
- **Funcionalidades**:
  - Ver todos los Pokémon de un tipo específico
  - Contador de resultados

### Estadísticas
- **URL**: `/stats`
- **Descripción**: Estadísticas generales de la Pokédex
- **Funcionalidades**:
  - Distribución por tipos
  - Total de Pokémon
  - Gráficos y métricas

### Top Pokémon
- **URL**: `/top/attackers` y `/top/speedsters`
- **Descripción**: Pokémon con mejores estadísticas
- **Funcionalidades**:
  - Mejores atacantes
  - Pokémon más rápidos

### Gestión de Pokémon
- **Crear**: `/pokemon/new`
- **Editar**: `/pokemon/{id}/edit`
- **Eliminar**: Formulario en la página de detalles

## 🔌 API REST

La aplicación también expone una API REST completa en `/api/pokemon`:

### Endpoints Principales

#### Obtener todos los Pokémon
```http
GET /api/pokemon
```

#### Obtener Pokémon por ID
```http
GET /api/pokemon/{id}
```

#### Buscar Pokémon por nombre
```http
GET /api/pokemon/search?q={query}
```

#### Obtener Pokémon por tipo
```http
GET /api/pokemon/type/{type}
```

#### Crear nuevo Pokémon
```http
POST /api/pokemon
Content-Type: application/json

{
  "name": "NuevoPokemon",
  "pokedexNumber": 152,
  "type": "Agua",
  "description": "Descripción del Pokémon",
  "height": 1.0,
  "weight": 20.0,
  "hp": 50,
  "attack": 60,
  "defense": 50,
  "specialAttack": 60,
  "specialDefense": 50,
  "speed": 70
}
```

#### Actualizar Pokémon
```http
PUT /api/pokemon/{id}
Content-Type: application/json

{
  // Datos actualizados del Pokémon
}
```

#### Eliminar Pokémon
```http
DELETE /api/pokemon/{id}
```

### Endpoints Especializados

#### Obtener mejores atacantes
```http
GET /api/pokemon/top/attackers
```

#### Obtener Pokémon más rápidos
```http
GET /api/pokemon/top/speedsters
```

#### Obtener estadísticas por tipo
```http
GET /api/pokemon/stats/by-type
```

#### Contar total de Pokémon
```http
GET /api/pokemon/count
```

## 🗄️ Base de Datos

### Configuración
- **Tipo**: H2 Database (en memoria)
- **URL**: `jdbc:h2:mem:pokedexdb`
- **Usuario**: `sa`
- **Contraseña**: `password`
- **Consola H2**: `http://localhost:8080/h2-console`

### Estructura de la Tabla

```sql
CREATE TABLE pokemon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    pokedex_number INTEGER NOT NULL UNIQUE,
    type VARCHAR(255) NOT NULL,
    secondary_type VARCHAR(255),
    description VARCHAR(1000) NOT NULL,
    height DOUBLE,
    weight DOUBLE,
    hp INTEGER,
    attack INTEGER,
    defense INTEGER,
    special_attack INTEGER,
    special_defense INTEGER,
    speed INTEGER,
    image_url VARCHAR(500),
    evolution_chain VARCHAR(500)
);
```

## 🎨 Personalización

### Agregar Nuevos Tipos de Pokémon
1. Los tipos se manejan como strings en la base de datos
2. Puedes agregar cualquier tipo nuevo sin modificar el código
3. Los tipos se muestran automáticamente en las vistas

### Modificar el Diseño
- **CSS**: Los estilos están en las plantillas HTML
- **Bootstrap**: Usando Bootstrap 5 para el diseño responsive
- **Iconos**: Font Awesome para los iconos

### Agregar Nuevas Funcionalidades
1. Crear nuevos métodos en `PokemonService`
2. Agregar endpoints en `PokemonRestController`
3. Crear vistas en `templates/`
4. Actualizar el controlador web si es necesario

## 🧪 Pruebas

### Ejecutar Tests
```bash
mvn test
```

### Pruebas de Integración
La aplicación incluye tests básicos para:
- Repositorio de Pokémon
- Servicio de Pokémon
- Controladores REST y Web

## 🚀 Despliegue

### Despliegue Local
```bash
mvn spring-boot:run
```

### Crear JAR Ejecutable
```bash
mvn clean package
java -jar target/pokedex-app-1.0.0.jar
```

### Despliegue en Producción
1. Cambiar la base de datos a MySQL/PostgreSQL
2. Configurar variables de entorno
3. Usar un servidor de aplicaciones como Tomcat

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📞 Soporte

Si tienes preguntas o problemas:

1. Revisa la documentación
2. Busca en los issues existentes
3. Crea un nuevo issue con detalles del problema

## 🎯 Roadmap

- [ ] Agregar más generaciones de Pokémon
- [ ] Implementar sistema de usuarios
- [ ] Agregar funcionalidad de equipos Pokémon
- [ ] Integración con PokeAPI
- [ ] Aplicación móvil
- [ ] Sistema de batallas
- [ ] Gráficos avanzados de estadísticas

---

¡Disfruta explorando el mundo de los Pokémon! 🎮✨ 