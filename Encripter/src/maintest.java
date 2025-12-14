import java.nio.file.Path;

/**
 * Clase de prueba para demostrar el uso seguro de AesEncripter
 */
public class maintest {
    public static void main(String[] args) {
        // Crear instancia del encriptador
        AesEncripter encripter = new AesEncripter(
            AesEncripter.KeySize.SIZE_256,
            AesEncripter.operationMode.CBC,
            "mi_clave_secreta_123",
            "mi_vector_inicial"
        );

        System.out.println("=== PRUEBA DE ENCRIPTACIÓN SEGURA ===\n");

        // ❌ EJEMPLO DE RUTA PELIGROSA (NO FUNCIONARÁ)
        System.out.println("--- Intentando encriptar C:\\ (DEBERÍA FALLAR) ---");
        Path rutaPeligrosa = Path.of("C:\\");
        boolean resultado1 = encripter.encryptFolder(rutaPeligrosa);
        System.out.println("Resultado: " + (resultado1 ? "ÉXITO" : "FALLÓ (como se esperaba)"));
        System.out.println();

        // ❌ EJEMPLO DE RUTA DEL SISTEMA (NO FUNCIONARÁ)
        System.out.println("--- Intentando encriptar C:\\Windows (DEBERÍA FALLAR) ---");
        Path rutaSistema = Path.of("C:\\Windows");
        boolean resultado2 = encripter.encryptFolder(rutaSistema);
        System.out.println("Resultado: " + (resultado2 ? "ÉXITO" : "FALLÓ (como se esperaba)"));
        System.out.println();

        // ✅ EJEMPLO DE RUTA SEGURA (FUNCIONARÁ)
        System.out.println("--- Intentando encriptar carpeta_pruebas (DEBERÍA FUNCIONAR) ---");
        Path rutaSegura = Path.of("carpeta_pruebas");

        // Verificar que existe la carpeta de pruebas
        if (!encripter.exist(rutaSegura)) {
            System.err.println("ERROR: La carpeta 'carpeta_pruebas' no existe.");
            System.out.println("Por favor, ejecuta primero 'main.java' para crear la estructura de prueba.");
            return;
        }

        boolean resultado3 = encripter.encryptFolder(rutaSegura);
        System.out.println("Resultado: " + (resultado3 ? "ÉXITO ✓" : "FALLÓ ✗"));
        System.out.println();

        // Si la encriptación fue exitosa, probar desencriptación
        if (resultado3) {
            System.out.println("--- Desencriptando carpeta_pruebas ---");
            boolean resultado4 = encripter.decryptFolder(rutaSegura);
            System.out.println("Resultado: " + (resultado4 ? "ÉXITO ✓" : "FALLÓ ✗"));
        }

        System.out.println("\n=== GUÍA DE USO SEGURO ===");
        System.out.println("✅ SÍ puedes encriptar:");
        System.out.println("   - Carpetas dentro de tus documentos");
        System.out.println("   - Carpetas de proyectos específicas");
        System.out.println("   - Carpetas creadas por ti en ubicaciones seguras");
        System.out.println();
        System.out.println("❌ NO puedes encriptar:");
        System.out.println("   - C:\\ (raíz del disco)");
        System.out.println("   - C:\\Windows (carpeta del sistema)");
        System.out.println("   - C:\\Program Files");
        System.out.println("   - Otras carpetas del sistema operativo");
        System.out.println();
        System.out.println("📝 Ejemplo de uso correcto:");
        System.out.println("   Path miCarpeta = Path.of(\"C:\\\\Users\\\\TuUsuario\\\\MisDocumentos\\\\CarpetaPrivada\");");
        System.out.println("   encripter.encryptFolder(miCarpeta);");
    }
}

