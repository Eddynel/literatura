

```markdown
# 📚 LiterAlura - Catálogo de Libros

Bienvenido a **LiterAlura**, una aplicación de consola desarrollada en Java con Spring Boot que te permite construir tu propio catálogo de libros. Este proyecto consume la API pública de Gutendex para buscar libros de dominio público, procesa la información y la almacena en una base de datos relacional PostgreSQL para futuras consultas y estadísticas.

Este proyecto fue desarrollado como parte del desafío **LiterAlura** del programa Oracle Next Education (ONE) en Alura Latam.

## 🚀 Funcionalidades

El sistema cuenta con un menú interactivo en la consola que ofrece las siguientes opciones:

1. **Buscar libro por título:** Consulta la API de Gutendex y guarda automáticamente el primer resultado (libro y autor) en la base de datos PostgreSQL, evitando duplicados.
2. **Listar libros registrados:** Muestra una lista detallada de todos los libros que se han guardado en la base de datos local.
3. **Listar autores registrados:** Extrae y muestra todos los autores almacenados, incluyendo sus años de nacimiento y fallecimiento.
4. **Listar autores vivos en un determinado año:** Utiliza *Derived Queries* (JPQL) para filtrar y mostrar qué autores del catálogo estaban vivos en el año especificado por el usuario.
5. **Listar libros por idioma:** Permite filtrar los libros almacenados según su idioma (ej. `es`, `en`, `fr`, `pt`).
6. **Estadísticas globales:** Utiliza *Java Streams* para calcular y exhibir el promedio, máximo y mínimo de descargas de todos los libros en la base de datos.

## 🛠️ Tecnologías Utilizadas

* **Java 17+**
* **Spring Boot** (Data JPA)
* **PostgreSQL** (Base de datos relacional)
* **Jackson** (Mapeo de JSON a Objetos Java)
* **API de Gutendex** (Fuente de datos externa)

## ⚙️ Configuración del Proyecto

Para ejecutar este proyecto localmente, necesitas tener instalados Java, Maven y PostgreSQL.

1. **Clona el repositorio:**
   ```bash
   git clone (https://github.com/Eddynel/literatura)

```

2. **Configura la Base de Datos:**
Crea una base de datos en PostgreSQL llamada `literalura_db`. Luego, abre el archivo `src/main/resources/application.properties` y actualiza tus credenciales:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update

```


3. **Ejecuta la aplicación:**
Corre el método `main` desde la clase `LiteraluraApplication.java` en tu IDE favorito (IntelliJ IDEA, Eclipse, etc.). La base de datos creará las tablas automáticamente gracias a Hibernate.

## 🧠 Arquitectura y Conceptos Aplicados

* **Consumo de API Rest:** Uso de `HttpClient`, `HttpRequest` y `HttpResponse`.
* **Deserialización de JSON:** Uso de la anotación `@JsonAlias` y `ObjectMapper` para transformar datos estructurados.
* **Persistencia de Datos (ORM):** Uso de `@Entity`, relaciones `@ManyToOne` con `CascadeType.ALL` para insertar autores y libros simultáneamente.
* **Manejo de Excepciones:** Bloques `try-catch` para manejar entradas inválidas del usuario (ej. `NumberFormatException`) y violaciones de restricciones de base de datos.
* **Streams y Lambdas:** Para la generación eficiente de estadísticas sobre los datos recuperados de la base de datos.

---

**Desarrollado con ❤️ por [Eddynel Munoz Rodriguez]**

```
