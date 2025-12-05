import java.nio.file.Path;

public class maintest {
    public static void main(String[] args) {
        AesEncripter aesEncripter= new AesEncripter();
        aesEncripter.decryptFolder(Path.of("carpeta_pruebas"));
    }
}
