package com.alura.literalura;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner lectura = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n******************************************
                    --- BIENVENIDO A LITERALURA ---
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Estadísticas por idioma y generales
                    0 - Salir
                    ******************************************
                    Elija una opción:
                    """;
            System.out.println(menu);

            try {
                opcion = Integer.parseInt(lectura.nextLine());

                switch (opcion) {
                    case 1 -> buscarLibroWeb();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosEnAnio();
                    case 5 -> listarLibrosPorIdioma();
                    case 6 -> mostrarEstadisticas();
                    case 0 -> System.out.println("Cerrando la aplicación...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
    }

    private void buscarLibroWeb() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var nombreLibro = lectura.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        if (!datos.resultados().isEmpty()) {
            DatosLibro datosLibro = datos.resultados().get(0);
            Libro libro = new Libro(datosLibro);

            if (!datosLibro.autor().isEmpty()) {
                Autor autor = new Autor(datosLibro.autor().get(0));
                libro.setAutor(autor);
            }

            try {
                libroRepository.save(libro);
                System.out.println("\n[Libro guardado con éxito]");
                System.out.println(libro);
            } catch (Exception e) {
                System.out.println("\n[!] El libro ya se encuentra en la base de datos.");
            }
        } else {
            System.out.println("No se encontró ese libro en la web.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("\n--- AUTORES REGISTRADOS ---");
            autores.forEach(a -> System.out.println(
                    "Autor: " + a.getNombre() +
                            "\nFecha de nacimiento: " + a.getFechaDeNacimiento() +
                            "\nFecha de fallecimiento: " + (a.getFechaDeFallecimiento() != null ? a.getFechaDeFallecimiento() : "N/A") +
                            "\n---------------------------"
            ));
        }
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año que desea consultar:");
        try {
            var anio = Integer.parseInt(lectura.nextLine());
            List<Autor> autoresVivos = autorRepository.buscarAutoresVivosEnAnio(anio);

            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron registros de autores vivos en el año " + anio);
            } else {
                System.out.println("\n--- AUTORES VIVOS EN EL AÑO " + anio + " ---");
                autoresVivos.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Por favor, ingrese un año válido (número).");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el código del idioma:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        var idioma = lectura.nextLine();
        List<Libro> libros = libroRepository.findByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No hay libros en ese idioma registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void mostrarEstadisticas() {
        System.out.println("""
                Ingrese el código del idioma para ver la cantidad de libros:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        var idioma = lectura.nextLine();
        Long cantidad = libroRepository.countByIdioma(idioma);

        String nombreIdioma = switch (idioma) {
            case "es" -> "Español";
            case "en" -> "Inglés";
            case "fr" -> "Francés";
            case "pt" -> "Portugués";
            default -> "Idioma desconocido";
        };

        System.out.println("\n------------------------------------------");
        System.out.printf("Cantidad de libros en [%s]: %d\n", nombreIdioma, cantidad);

        // Uso de Streams para estadísticas generales
        List<Libro> todosLosLibros = libroRepository.findAll();
        if (!todosLosLibros.isEmpty()) {
            DoubleSummaryStatistics est = todosLosLibros.stream()
                    .filter(l -> l.getNumeroDeDescargas() != null) // Filtro para evitar errores si es nulo
                    .mapToDouble(l -> l.getNumeroDeDescargas()) // <--- SI AQUI SIGUE EL ERROR, REVISA TU CLASE LIBRO
                    .summaryStatistics();
            System.out.println("\n--- ESTADÍSTICAS GLOBALES DE DESCARGAS ---");
            System.out.println("Promedio: " + String.format("%.2f", est.getAverage()));
            System.out.println("Máximo: " + est.getMax());
            System.out.println("Mínimo: " + est.getMin());
        }
        System.out.println("------------------------------------------");
    }
}