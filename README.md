# Image Steganography Tool 🔐🖼️
**Java-based message encryption/decryption using RGB pixel manipulation**  
[![Java Version](https://img.shields.io/badge/Java-17%2B-blue)](https://www.oracle.com/java/)

<p align="center">
  <img src="https://via.placeholder.com/400x250.png?text=Encrypted+Image+Preview" alt="UI Preview">
</p>

## Key Features
### 🔒 Encryption
- **Message Embedding**: Hides ASCII text in PNG/JPG images via blue channel manipulation
- **Security Mechanisms**:
  - Random key generation (0-255 range)
  - Pixel shuffling algorithm
  - Counting sort synchronization
- **Image Processing**:
  - Automatic PNG conversion
  - 50x50 pixel size validation
  - RGB value modification tracking

### 🔓 Decryption 
- **Message Extraction**: Retrieves hidden text from encrypted images
- **Data Recovery**:
  - Reads metadata from (0,0) pixel
  - Dual-array synchronization
  - Custom counting sort implementation

### 🛠 Technical Components
classDiagram
class ImageHandler{
+loadImage()
+saveImage()
+convertToPNG()
}
class EncryptedImage{
+encryptMessage()
-generateRandomKey()
-changeBlueValues()
}
class UserWindow{
+textASCIIValueCheck()
+textLengthCheck()
}
class LogFile{
+writeLog()
+closeLogFile()
}
ImageHandler <|-- EncryptedImage
UserWindow --> EncryptedImage
UserWindow --> LogFile

## Technical Highlights
| Component          | Implementation Details                          | OOP Concept Used       |
|--------------------|-------------------------------------------------|------------------------|
| Pixel Manipulation | `BufferedImage` RGB modification                | Encapsulation          |
| Log System         | Thread-safe buffered writer                     | Singleton Pattern      |
| Validation         | ASCII range + length checks                     | Exception Handling     |
| Image Processing   | Inheritance from `ImageHandler` base class      | Inheritance            |

## Documentation
- **Algorithm Flow**:
  1. Generate random key (0-255)
  2. Modify conflicting blue values
  3. Encode metadata in (0,0) pixel:
     - Red: Message length
     - Green: Encryption key
  4. Distribute message chars using shuffled indices

- **Log File Sample**:
```
[17:00:05 04 Mar 2025] Loaded image.jpg (50x50px)
[17:00:07 04 Mar 2025] Generated encryption key: 210
[17:00:09 04 Mar 2025] Encrypted 42-character message
[17:00:11 04 Mar 2025] Saved encrypted_image.png
```

## Requirements
- Java 17+ JDK
- Libraries:
- `javax.imageio` (Image I/O)
- `java.awt` (GUI components)
- `java.util.Collections` (Array shuffling)
