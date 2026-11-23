package org.example;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

public class AssetLoader {
    public static final String PLAYER_SPRITE = "/images/player.png";
    public static final String ENEMY_BASIQUE_SPRITE = "/images/enemy_basique.png";
    public static final String ENEMY_RAPIDE_SPRITE = "/images/enemy_rapide.png";
    public static final String ENEMY_TANK_SPRITE = "/images/enemy_tank.png";
    public static final String PROJECTILE_SPRITE = "/images/projectile.png";

    private final Map<String, Image> cache = new HashMap<>();

    public AssetLoader() {
        preload(PLAYER_SPRITE);
        preload(ENEMY_BASIQUE_SPRITE);
        preload(ENEMY_RAPIDE_SPRITE);
        preload(ENEMY_TANK_SPRITE);
        preload(PROJECTILE_SPRITE);
    }

    private void preload(String resourcePath) {
        try (InputStream stream = AssetLoader.class.getResourceAsStream(resourcePath)) {
            if (stream != null) {
                Image image = new Image(stream);
                if (!image.isError()) {
                    cache.put(resourcePath, image);
                }
            }
        } catch (Exception ignored) {
            // Si une image manque ou échoue, le cache ne la contient pas et la vue dessinera la forme de secours.
        }
    }

    public Image getImage(String resourcePath) {
        return cache.get(resourcePath);
    }
}