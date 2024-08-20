package masjubel.platform.config;

import java.nio.file.Paths;

public class MyResourceLoader {

    public void getResourcePath() {
        String resourcePath = Paths.get("src/main/resources/jasper/masjubel_harga.jrxml").toAbsolutePath().toString();
        System.out.println("Resource path: " + resourcePath);
    }
}

