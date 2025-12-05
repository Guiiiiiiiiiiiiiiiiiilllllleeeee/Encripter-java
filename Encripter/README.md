# 🔐 AES Encrypter

Un encriptador de archivos y carpetas robusto y flexible implementado en Java, utilizando el algoritmo de encriptación AES (Advanced Encryption Standard).

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Security](https://img.shields.io/badge/Security-AES-green?style=for-the-badge)

## ✨ Características

- 🔒 **Encriptación AES robusta** con soporte para múltiples tamaños de clave (128, 192, 256 bits)
- 📁 **Encriptación de archivos individuales** con preservación de estructura
- 📂 **Encriptación recursiva de carpetas** completas
- 🔄 **Múltiples modos de operación**: ECB, CBC, CFB, OFB, CTR
- 🔑 **Claves personalizables** con generación automática mediante SHA-256
- 🛡️ **Vector de inicialización (IV)** configurable para mayor seguridad
- ⚡ **Procesamiento eficiente** de archivos grandes
- 🎯 **API simple e intuitiva** para integración fácil

## 🚀 Inicio Rápido

### Prerrequisitos

- Java 11 o superior
- JDK instalado y configurado en tu sistema

### Compilación y Ejecución

1. Compila el proyecto:
```bash
javac -d out src/*.java
```

2. Ejecuta el programa:
```bash
java -cp out main
```

## 📖 Uso

### Ejemplo Básico

```java
import java.nio.file.Path;

// Crear instancia con configuración por defecto (AES-256, CBC)
Encripter encripter = new AesEncripter();

// Encriptar un archivo
Path archivo = Path.of("documento.txt");
boolean exitoEncriptacion = encripter.encryptFile(archivo);

// Desencriptar el archivo
boolean exitoDesencriptacion = encripter.decryptFile(archivo);
```

### Configuración Personalizada

```java
// Constructor con clave y IV personalizados
Encripter encripter = new AesEncripter(
    "MiClaveSecreta123", 
    "MiIVSeguro12345"
);

// Constructor completo con todas las opciones
Encripter encripter = new AesEncripter(
    AesEncripter.KeySize.SIZE_256,           // Tamaño de clave
    AesEncripter.operationMode.CBC,          // Modo de operación
    "MiClaveUltraSecreta",                   // Clave
    "MiIVUltraSeguro"                        // Vector de inicialización
);
```

### Encriptar Carpetas Completas

```java
// Encripta todos los archivos de una carpeta recursivamente
Path carpeta = Path.of("documentos_privados");
boolean exito = encripter.encryptFolder(carpeta);

// Desencripta toda la carpeta
boolean exitoDesencriptacion = encripter.decryptFolder(carpeta);
```

## 🔧 Configuración Avanzada

### Tamaños de Clave Disponibles

```java
AesEncripter.KeySize.SIZE_128  // 128 bits
AesEncripter.KeySize.SIZE_192  // 192 bits
AesEncripter.KeySize.SIZE_256  // 256 bits (recomendado)
```

### Modos de Operación

```java
AesEncripter.operationMode.ECB  // Electronic Codebook (no recomendado para producción)
AesEncripter.operationMode.CBC  // Cipher Block Chaining (recomendado)
AesEncripter.operationMode.CFB  // Cipher Feedback
AesEncripter.operationMode.OFB  // Output Feedback
AesEncripter.operationMode.CTR  // Counter
```

## 🛠️ Estructura del Proyecto

```
Encripter/
├── src/
│   ├── Encripter.java       # Clase abstracta base
│   ├── AesEncripter.java    # Implementación AES
│   └── main.java            # Programa principal con ejemplos
├── Encripter.iml            # Configuración del módulo
└── README.md                # Este archivo
```

## 🔐 Seguridad

### Mejores Prácticas

- ✅ **Usa AES-256**: Es el estándar más seguro actualmente
- ✅ **Evita el modo ECB**: No es seguro para datos repetitivos
- ✅ **Usa claves fuertes**: Mínimo 16 caracteres con mezcla de letras, números y símbolos
- ✅ **Protege tus claves**: Nunca las guardes en texto plano en el código
- ✅ **Usa modo CBC o superior**: Proporciona mejor seguridad que ECB
- ✅ **Genera IVs únicos**: Para cada operación de encriptación

### Advertencias

⚠️ **IMPORTANTE**: 
- Este proyecto es educativo. Para producción, considera usar librerías establecidas como Bouncy Castle
- Mantén tus claves seguras y fuera del control de versiones
- Realiza backups antes de encriptar datos importantes
- La pérdida de la clave resulta en pérdida permanente de datos


## 📊 Casos de Uso

- 📄 **Protección de documentos confidenciales**
- 💾 **Backup encriptado de datos sensibles**
- 🔒 **Archivado seguro de información personal**
- 🏢 **Cumplimiento de normativas de protección de datos**
- 🎓 **Aprendizaje de criptografía y seguridad**

## 🤝 Contribuir

Las contribuciones son bienvenidas! Siéntete libre de sugerir mejoras o reportar problemas.

## 📝 API Reference

### Clase `Encripter` (Abstracta)

#### Métodos Públicos

| Método | Descripción | Retorno |
|--------|-------------|---------|
| `exist(Path path)` | Verifica si un path existe | `boolean` |
| `isDirectory(Path path)` | Verifica si es un directorio | `boolean` |
| `isFile(Path path)` | Verifica si es un archivo | `boolean` |
| `encryptFile(Path path)` | Encripta un archivo | `boolean` |
| `decryptFile(Path path)` | Desencripta un archivo | `boolean` |
| `encryptFolder(Path path)` | Encripta una carpeta completa | `boolean` |
| `decryptFolder(Path path)` | Desencripta una carpeta completa | `boolean` |

### Clase `AesEncripter`

#### Constructores

```java
// Constructor por defecto: AES-256, modo CBC, clave "default", IV "default"
AesEncripter()

// Con tamaño de clave personalizado
AesEncripter(KeySize keySize)

// Con modo de operación personalizado
AesEncripter(operationMode mode)

// Con clave y IV personalizados
AesEncripter(String key, String iv)

// Configuración completa
AesEncripter(KeySize keySize, operationMode mode, String key, String iv)
```

## 🐛 Solución de Problemas

### Error: "Illegal key size"
- **Causa**: Políticas de jurisdicción limitada de Java
- **Solución**: Actualiza a Java 8u161 o superior, o instala JCE Unlimited Strength

### Error al desencriptar
- **Causa**: Clave o IV incorrectos
- **Solución**: Asegúrate de usar exactamente la misma clave e IV que usaste para encriptar

### Archivos muy grandes
- **Recomendación**: Para archivos > 1GB, considera procesar por chunks o usar streaming


## 🙏 Agradecimientos

- Inspirado en las mejores prácticas de seguridad de OWASP
- Basado en el estándar AES (FIPS 197)
- Gracias a la comunidad Java por su documentación

---

<div align="center">

Made with ❤️ and ☕

</div>

