import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class main {
    public static void main(String[] args) {
        try {

            Path base = Path.of("carpeta_pruebas");
            Files.createDirectories(base);

            // Configurable
            int numSubcarpetas = 5;      // cuántas subcarpetas quieres
            int archivosPorCarpeta = 3;  // cuántos ficheros en cada una

            for (int i = 1; i <= numSubcarpetas; i++) {

                // Crear subcarpeta dinámica
                Path sub = base.resolve("subcarpeta_" + i);
                Files.createDirectories(sub);

                // Crear archivos dinámicos dentro
                for (int j = 1; j <= archivosPorCarpeta; j++) {
                    Path file = sub.resolve("archivo_" + j + ".txt");
                    String contenido =
                            "Este es el archivo " + j + " dentro de subcarpeta " + i + "\n" +
                                    "Generado automáticamente para pruebas.";
                    crearTxt(file, contenido);
                }
            }

            System.out.println("Estructura generada correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void crearTxt(Path ruta, String contenido) throws IOException {
        Files.write(
                ruta,
                contenido.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
    }

