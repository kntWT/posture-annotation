package com.example.api.utils;

import java.util.Base64;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveFile {

    static private String basePath = System.getenv("IMAGE_DIR");

    SaveFile() {
    }

    public static boolean saveBase64Image(String fileName, String base64) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        try {
            Path path = Paths.get(basePath, fileName);
            Files.write(path, decodedBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveBase64Image(String fileName, String base64, String dir) {
        if (Files.notExists(Paths.get(dir))) {
            try {
                Files.createDirectory(Paths.get(dir));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            Path path = Paths.get(dir, fileName);
            String base64Image = base64;
            if (base64Image.startsWith("data:image/jpeg;base64,")) {
                base64Image = base64Image.replace("data:image/jpeg;base64,", "");
            }
            byte[] data = Base64.getDecoder().decode(base64Image);
            OutputStream out = new FileOutputStream(path.toString());
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
