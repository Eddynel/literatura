package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Alura pide buscar por idioma. Esto es una "Derived Query".
    // Spring Boot entiende que debe buscar en la columna 'idioma'.
    List<Libro> findByIdioma(String idioma);
    // Spring Data JPA transformará esto en un "SELECT COUNT(*) FROM libros WHERE idioma = ?"
    Long countByIdioma(String idioma);
}
