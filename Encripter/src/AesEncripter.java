import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.MessageDigest;
import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;
import java.nio.charset.StandardCharsets;

public class AesEncripter extends Encripter {

    public enum KeySize {
        SIZE_128,
        SIZE_192,
        SIZE_256
    }

    KeySize keySize;
    String key;

    public enum operationMode {
        ECB,
        CBC,
        CFB,
        OFB,
        CTR
    }

    operationMode mode;
    String iv;

    // ---------- CONSTRUCTOR POR DEFECTO ----------
    public AesEncripter() {
        this.keySize = KeySize.SIZE_256;
        this.mode = operationMode.CBC;
        this.key = "default_key_123456789012";
        this.iv = "default_iv_1234";
    }

    // ---------- CONSTRUCTOR: KEY SIZE ----------
    public AesEncripter(KeySize keySize) {
        this.keySize = keySize;
        this.mode = operationMode.CBC;
        this.key = "default_key_123456789012";
        this.iv = "default_iv_1234";
    }

    // ---------- CONSTRUCTOR: KEY SIZE + MODE ----------
    public AesEncripter(KeySize keySize, operationMode mode) {
        this.keySize = keySize;
        this.mode = mode;
        this.key = "default_key_123456789012";
        this.iv = "default_iv_1234";
    }

    // ---------- CONSTRUCTOR: KEY SIZE + MODE + KEY ----------
    public AesEncripter(KeySize keySize, operationMode mode, String key) {
        this.keySize = keySize;
        this.mode = mode;
        this.key = key;
        this.iv = "default_iv_1234";
    }

    // ---------- CONSTRUCTOR COMPLETO ----------
    public AesEncripter(KeySize keySize, operationMode mode, String key, String iv) {
        this.keySize = keySize;
        this.mode = mode;
        this.key = key;
        this.iv = iv;
    }

    // ---------- CONSTRUCTOR: KEY + IV ----------
    public AesEncripter(String key, String iv) {
        this.keySize = KeySize.SIZE_256; // valor razonable por defecto
        this.mode = operationMode.CBC;
        this.key = key;
        this.iv = iv;
    }

    // ---------- CONSTRUCTOR: SOLO MODE ----------
    public AesEncripter(operationMode mode) {
        this.keySize = KeySize.SIZE_256;
        this.mode = mode;
        this.key = "default_key_123456789012";
        this.iv = "default_iv_1234";
    }

    /**
     * Verifica si una ruta es peligrosa (directorios del sistema o raíz)
     * @param path Path a verificar
     * @return true si es una ruta peligrosa, false si es segura
     */
    private boolean isDangerousPath(Path path) {
        try {
            // Obtener la ruta absoluta normalizada
            Path absolutePath = path.toAbsolutePath().normalize();
            String pathString = absolutePath.toString().toLowerCase();

            // Lista de rutas peligrosas en Windows
            String[] dangerousPaths = {
                "c:\\",
                "c:\\windows",
                "c:\\program files",
                "c:\\program files (x86)",
                "c:\\programdata",
                "c:\\users\\default",
                "c:\\users\\public",
                "c:\\system",
                "c:\\$recycle.bin"
            };

            // Verificar si la ruta es exactamente una raíz de unidad (C:\, D:\, etc.)
            if (pathString.matches("^[a-z]:\\\\$")) {
                return true;
            }

            // Verificar si la ruta empieza con alguna de las rutas peligrosas
            for (String dangerous : dangerousPaths) {
                if (pathString.equals(dangerous) || pathString.startsWith(dangerous + "\\")) {
                    return true;
                }
            }

            // Verificar si tiene menos de 2 niveles de profundidad desde la raíz (muy cerca de C:\)
            int depth = absolutePath.getNameCount();
            if (depth <= 1) {
                return true;
            }

            return false;
        } catch (Exception e) {
            // Si hay error, mejor considerarlo peligroso por seguridad
            System.err.println("Error al validar ruta: " + e.getMessage());
            return true;
        }
    }

    /**
     * Desencripta una carpeta recursivamente
     * @param path Path de la carpeta a desencriptar
     * @return true si se ha desencriptado correctamente, false si no
     */
    @Override
    public boolean decryptFolder(Path path) {
        if (!exist(path) || !isDirectory(path)) {
            System.err.println("La carpeta no existe o no es un directorio válido: " + path);
            return false;
        }

        // Validar que no sea una ruta peligrosa del sistema
        if (isDangerousPath(path)) {
            System.err.println("ERROR: No se permite desencriptar directorios del sistema o raíz: " + path);
            System.err.println("Por seguridad, solo puedes desencriptar carpetas específicas, no directorios raíz o del sistema.");
            return false;
        }

        // Verificar permisos de escritura
        if (!Files.isWritable(path)) {
            System.err.println("ERROR: No tienes permisos de escritura en: " + path);
            return false;
        }

        boolean success = true;
        try (Stream<Path> paths = Files.walk(path)) {
            paths.filter(Files::isRegularFile).forEach(filePath -> {
                try {
                    // Verificar que el archivo sea accesible y escribible
                    if (Files.isWritable(filePath)) {
                        System.out.println("Desencriptando archivo: " + filePath);
                        if (!decryptFile(filePath)) {
                            System.err.println("Falló la desencriptación de: " + filePath);
                        }
                    } else {
                        System.err.println("Saltando archivo sin permisos: " + filePath);
                    }
                } catch (Exception e) {
                    System.err.println("Error al verificar archivo " + filePath + ": " + e.getMessage());
                }
            });
            System.out.println("Carpeta desencriptada exitosamente: " + path);
        } catch (Exception e) {
            System.err.println("Error al desencriptar la carpeta " + path + ": " + e.getMessage());
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    /**
     * Encripta una carpeta recursivamente
     * @param path Path de la carpeta a encriptar
     * @return true si se ha encriptado correctamente, false si no
     */
    @Override
    public boolean encryptFolder(Path path) {
        if (!exist(path) || !isDirectory(path)) {
            System.err.println("La carpeta no existe o no es un directorio válido: " + path);
            return false;
        }

        // Validar que no sea una ruta peligrosa del sistema
        if (isDangerousPath(path)) {
            System.err.println("ERROR: No se permite encriptar directorios del sistema o raíz: " + path);
            System.err.println("Por seguridad, solo puedes encriptar carpetas específicas, no directorios raíz o del sistema.");
            return false;
        }

        // Verificar permisos de escritura
        if (!Files.isWritable(path)) {
            System.err.println("ERROR: No tienes permisos de escritura en: " + path);
            return false;
        }

        boolean success = true;
        try (Stream<Path> paths = Files.walk(path)) {
            paths.filter(Files::isRegularFile).forEach(filePath -> {
                try {
                    // Verificar que el archivo sea accesible y escribible
                    if (Files.isWritable(filePath)) {
                        System.out.println("Encriptando archivo: " + filePath);
                        if (!encryptFile(filePath)) {
                            System.err.println("Falló la encriptación de: " + filePath);
                        }
                    } else {
                        System.err.println("Saltando archivo sin permisos: " + filePath);
                    }
                } catch (Exception e) {
                    System.err.println("Error al verificar archivo " + filePath + ": " + e.getMessage());
                }
            });
            System.out.println("Carpeta encriptada exitosamente: " + path);
        } catch (Exception e) {
            System.err.println("Error al encriptar la carpeta " + path + ": " + e.getMessage());
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    @Override
    public boolean decryptFile(Path path) {
        if (!exist(path) || !isFile(path)) {
            System.err.println("El archivo no existe o no es un archivo válido: " + path);
            return false;
        }

        try {
            // Leer el contenido encriptado del archivo
            byte[] encryptedContent = Files.readAllBytes(path);

            // Desencriptar el contenido
            byte[] decryptedContent = decrypt(encryptedContent);

            // Crear archivo temporal para la desencriptación
            Path tempFile = Path.of(path + ".tmp");
            Files.write(tempFile, decryptedContent);

            // Reemplazar el archivo original con el desencriptado
            Files.move(tempFile, path, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Archivo desencriptado exitosamente: " + path);
            return true;

        } catch (Exception e) {
            System.err.println("Error al desencriptar el archivo " + path + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean encryptFile(Path path) {
        if (!exist(path) || !isFile(path)) {
            System.err.println("El archivo no existe o no es un archivo válido: " + path);
            return false;
        }

        try {
            // Leer el contenido del archivo
            byte[] fileContent = Files.readAllBytes(path);

            // Encriptar el contenido
            byte[] encryptedContent = encrypt(fileContent);

            // Crear archivo temporal para la encriptación
            Path tempFile = Path.of(path + ".tmp");
            Files.write(tempFile, encryptedContent);

            // Reemplazar el archivo original con el encriptado
            Files.move(tempFile, path, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Archivo encriptado exitosamente: " + path);
            return true;

        } catch (Exception e) {
            System.err.println("Error al encriptar el archivo " + path + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método auxiliar para encriptar datos
     * @param data Datos a encriptar
     * @return Datos encriptados
     * @throws Exception Si ocurre un error durante la encriptación
     */
    private byte[] encrypt(byte[] data) throws Exception {
        SecretKey secretKey = generateKey();
        Cipher cipher = Cipher.getInstance(getAlgorithm());

        if (mode == operationMode.ECB) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            IvParameterSpec ivSpec = generateIv();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        }

        return cipher.doFinal(data);
    }

    /**
     * Método auxiliar para desencriptar datos
     * @param encryptedData Datos encriptados
     * @return Datos desencriptados
     * @throws Exception Si ocurre un error durante la desencriptación
     */
    private byte[] decrypt(byte[] encryptedData) throws Exception {
        SecretKey secretKey = generateKey();
        Cipher cipher = Cipher.getInstance(getAlgorithm());

        if (mode == operationMode.ECB) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } else {
            IvParameterSpec ivSpec = generateIv();
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        }

        return cipher.doFinal(encryptedData);
    }

    /**
     * Genera la clave secreta a partir de la clave proporcionada
     * @return SecretKey para AES
     * @throws Exception Si ocurre un error al generar la clave
     */
    private SecretKey generateKey() throws Exception {
        int keyLength = getKeyLength();
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(key.getBytes(StandardCharsets.UTF_8));
        keyBytes = Arrays.copyOf(keyBytes, keyLength / 8); // Ajustar al tamaño de clave deseado
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * Genera el vector de inicialización (IV)
     * @return IvParameterSpec para AES
     * @throws Exception Si ocurre un error al generar el IV
     */
    private IvParameterSpec generateIv() throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] ivBytes = sha.digest(iv.getBytes(StandardCharsets.UTF_8));
        ivBytes = Arrays.copyOf(ivBytes, 16); // IV de AES siempre es de 16 bytes
        return new IvParameterSpec(ivBytes);
    }

    /**
     * Obtiene el tamaño de la clave en bits
     * @return Tamaño de la clave en bits
     */
    private int getKeyLength() {
        switch (keySize) {
            case SIZE_128:
                return 128;
            case SIZE_192:
                return 192;
            case SIZE_256:
                return 256;
            default:
                return 256;
        }
    }

    /**
     * Obtiene el algoritmo completo de cifrado según el modo de operación
     * @return String con el algoritmo (ej: "AES/CBC/PKCS5Padding")
     */
    private String getAlgorithm() {
        String modeStr;
        switch (mode) {
            case ECB:
                modeStr = "ECB";
                break;
            case CBC:
                modeStr = "CBC";
                break;
            case CFB:
                modeStr = "CFB";
                break;
            case OFB:
                modeStr = "OFB";
                break;
            case CTR:
                modeStr = "CTR";
                break;
            default:
                modeStr = "CBC";
        }
        return "AES/" + modeStr + "/PKCS5Padding";
    }
}
