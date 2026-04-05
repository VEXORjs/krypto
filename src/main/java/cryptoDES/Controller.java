package cryptoDES;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.security.SecureRandom;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class);
    @FXML private TextField filePathField;
    @FXML private PasswordField keyField;
    private byte[] keyFromFile = null;
    @FXML private Label statusLabel;
    @FXML private RadioButton textModeRadio;
    @FXML private VBox textSection;
    @FXML private VBox fileSection;
    @FXML private TextArea inputTextArea;
    @FXML private TextArea outputTextArea;

    private File selectedFile;
    private final DES_cipher cipher = new DES_cipher();
    private final DES_KeyGenerator keyGen = new DES_KeyGenerator();

    @FXML
    private void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleModeChange() {
        boolean isTextMode = textModeRadio.isSelected();
        textSection.setVisible(isTextMode);
        textSection.setManaged(isTextMode);

        fileSection.setVisible(!isTextMode);
        fileSection.setManaged(!isTextMode);
    }

    @FXML
    private void handleEncrypt() {
        processData(false);
    }

    @FXML
    private void handleDecrypt() {
        processData(true);
    }

    @FXML
    private void handleLoadKeyFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik klucza");
        File keyFile = fileChooser.showOpenDialog(null);

        if (keyFile != null) {
            try {
                byte[] rawKey = Files.readAllBytes(keyFile.toPath());
                if (rawKey.length < 8) {
                    statusLabel.setText("Błąd: Plik klucza musi mieć min. 8 bajtów!");
                    return;
                }
                keyFromFile = Arrays.copyOf(rawKey, 8);
                keyField.setText("********");
                keyField.setEditable(false);
                statusLabel.setText("Wczytano klucz: " + keyFile.getName());
            } catch (Exception e) {
                statusLabel.setText("Błąd klucza: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleGenerateKey() {
        byte[] randomKey = new byte[8];
        new SecureRandom().nextBytes(randomKey);

        keyFromFile = randomKey;
        keyField.setText("********");
        keyField.setEditable(false);
        statusLabel.setText("Wygenerowano nowy losowy klucz.");
    }

    @FXML
    private void handleSaveKey() {
        byte[] keyToSave = getEffectiveKey();

        if (keyToSave == null) {
            statusLabel.setText("Błąd: Brak klucza do zapisania!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz klucz do pliku");
        fileChooser.setInitialFileName("des.key");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                Files.write(file.toPath(), keyToSave);
                statusLabel.setText("Klucz zapisany w: " + file.getName());
            } catch (Exception e) {
                statusLabel.setText("Błąd zapisu klucza: " + e.getMessage());
            }
        }
    }

    private byte[] getEffectiveKey() {
        if (keyFromFile != null) return keyFromFile;

        String textKey = keyField.getText();
        if (textKey.length() >= 8) {
            return Arrays.copyOf(textKey.getBytes(), 8);
        }
        return null;
    }

    private void processData(boolean decrypt) {
        byte[] finalKey = getEffectiveKey();
        if (finalKey == null) {
            statusLabel.setText("Błąd: Brak klucza!");
            return;
        }

        try {
            byte[] inputData;

            if (textModeRadio.isSelected()) {
                String inputText = inputTextArea.getText();
                if (inputText.isEmpty()) throw new Exception("Pole wejściowe jest puste!");

                inputData = decrypt ? java.util.Base64.getDecoder().decode(inputText) : inputText.getBytes();
            } else {
                if (selectedFile == null) throw new Exception("Nie wybrano pliku!");
                inputData = Files.readAllBytes(selectedFile.toPath());
            }

            byte[][] subkeys = keyGen.generateSubkeys(finalKey);
            byte[] result;

            if (!decrypt) {

                result = performDES(padPKCS7(inputData), subkeys, false);

                if (textModeRadio.isSelected()) {
                    String base64Result = java.util.Base64.getEncoder().encodeToString(result);
                    outputTextArea.setText(base64Result);
                    statusLabel.setText("Zaszyfrowano do Base64.");
                } else {
                    saveFile(result, selectedFile.getAbsolutePath() + ".des");
                    statusLabel.setText("Plik .des został zapisany.");
                }
            } else {

                result = performDES(inputData, subkeys, true);
                result = unpadPKCS7(result);

                if (textModeRadio.isSelected()) {
                    outputTextArea.setText(new String(result));
                    statusLabel.setText("Tekst odszyfrowany pomyślnie.");
                } else {
                    saveFile(result, selectedFile.getAbsolutePath().replace(".des", ""));
                    statusLabel.setText("Plik został odszyfrowany.");
                }
            }
        } catch (IllegalArgumentException e) {
            statusLabel.setText("Błąd: Tekst wejściowy nie jest poprawnym Base64!");
            logger.error(e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Błąd: ");
            logger.error(e.getMessage());
        }
    }

    private byte[] padPKCS7(byte[] in) {
        int p = 8 - (in.length % 8);
        byte[] out = new byte[in.length + p];
        System.arraycopy(in, 0, out, 0, in.length);
        for (int i = 0; i < p; i++) {
            out[in.length + i] = (byte) p;
        }
        return out;
    }

    private byte[] performDES(byte[] input, byte[][] subkeys, boolean decrypt) {
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i += 8) {
            byte[] block = Arrays.copyOfRange(input, i, i + 8);
            byte[] processed = cipher.processBlock(block, subkeys, decrypt);
            System.arraycopy(processed, 0, output, i, 8);
        }
        return output;
    }

    private byte[] unpadPKCS7(byte[] in) {
        int pad = in[in.length - 1];
        return Arrays.copyOfRange(in, 0, in.length - pad);
    }

    private void saveFile(byte[] data, String path) throws Exception {
        Files.write(new File(path).toPath(), data);
    }
}