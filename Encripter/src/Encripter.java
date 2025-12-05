import java.nio.file.Files;
import java.nio.file.Path;
/** * Clase abstracta para encriptar y desencriptar ficheros y carpetas
 */
public abstract class Encripter{
    /**
     * Comprueba si el path existe
     * @param path Path a comprobar
     * @return true si existe, false si no existe
     */
    public boolean exist(Path path){
        return Files.exists(path);
    }
    /** Comprueba si el path es un directorio
     * @param path Path a comprobar
     * @return true si es un directorio, false si no lo es
     */
    public boolean isDirectory(Path path){
        return Files.isDirectory(path);
    }
    /** Comprueba si el path es un fichero
     * @param path Path a comprobar
     * @return true si es un fichero, false si no lo es
     */
    public boolean isFile(Path path){
        return Files.isRegularFile(path);
    }
    /** Encripta un fichero
     * @param path Path del fichero a encriptar
     * @return true si se ha encriptado correctamente, false si no
     */
    public abstract boolean encryptFile(Path path);
    /** Desencripta un fichero
     * @param path Path del fichero a desencriptar
     * @return true si se ha desencriptado correctamente, false si no
     */
    public abstract boolean decryptFile(Path path);
    /** Encripta una carpeta
     * @param path Path de la carpeta a encriptar
     * @return true si se ha encriptado correctamente, false si no
     */
    public abstract boolean encryptFolder(Path path);
    /** Desencripta una carpeta
     * @param path Path de la carpeta a desencriptar
     * @return true si se ha desencriptado correctamente, false si no
     */
    public abstract boolean decryptFolder(Path path);
    /** Obtiene la extensión de un fichero
     * @param path Path del fichero
     * @return Extensión del fichero
     */
    public String getExtension(Path path){
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return ""; // No hay extensión
        }
        return fileName.substring(dotIndex + 1);

    }


}