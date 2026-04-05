🔐 DES Cryptography Tool

A desktop application written in Java and JavaFX that implements the Data Encryption Standard (DES) algorithm from scratch. It allows for secure encryption and decryption of both raw text and binary files.
🚀 Features

    Custom DES Implementation: Full Feistel network implementation, including subkey generation, S-Boxes, and permutations.

    Two Input Modes:

        Text Mode: Encrypt/decrypt messages directly. Uses Base64 encoding for ciphertext to ensure character safety.

        File Mode: Process any file type (Images, PDFs, ZIPs, etc.) as binary data.

    Advanced Key Management:

        Manual 8-character key entry.

        Cryptographically secure Random Key Generation.

        Load/Save keys from .key or .txt files.

    Padding: Full support for PKCS7 padding to handle blocks of any size.

    Modern UI: Intuitive interface built with JavaFX and FXML.

🛠️ Requirements

    JDK 17 or higher.

    JavaFX SDK (Controls, FXML).

    Maven (optional, recommended for dependency management).

📦 Installation & Setup
1. Clone the repository
Bash

git clone https://github.com/yourusername/cryptoDES.git
cd cryptoDES

2. Configure Dependencies (Maven)

If you are using Maven, add the following to your pom.xml:
XML

<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21</version>
    </dependency>
</dependencies>

3. Run the Application

Run the App.java class:
Bash

mvn javafx:run

📖 How to Use
Encrypting Text

    Select "Tekst" mode.

    Type your message in the "Tekst wejściowy" area.

    Enter an 8-character key or click "Generuj klucz".

    Click "SZYFRUJ". The Base64 result will appear in the output area.

Encrypting Files

    Select "Plik" mode.

    Click "Przeglądaj" to choose a file.

    Provide a key.

    Click "SZYFRUJ". A new file with the .des extension will be created.

⚠️ Disclaimer

This project was created for educational purposes to demonstrate how the DES algorithm works. For production-level security, it is recommended to use AES (Advanced Encryption Standard), as DES is considered cryptographically weak against modern brute-force attacks.
📄 License

This project is open-source and available under the MIT License.
