package org.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GraphicalView extends Pane implements GameView {
    private final Canvas canvas;
    private final AssetLoader assets;
    private final double width = 800;
    private final double height = 800;
    private final double centerX = width / 2;
    private final double centerY = height / 2;

    public GraphicalView() {
        this.canvas = new Canvas(width, height);
        this.assets = new AssetLoader();
        this.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }

    @Override
    public void render(GameEngine engine) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(2);
        double orbitRadius = Player.ORBIT_RADIUS;
        gc.strokeOval(
                centerX - orbitRadius,
                centerY - orbitRadius,
                orbitRadius * 2,
                orbitRadius * 2
        );

        for (GameEntity entity : engine.getEntities()) {
            if (!entity.isAlive()) continue;

            PolarPosition pos = entity.getPosition();

            double x = centerX + pos.toCartesianX();
            double y = centerY - pos.toCartesianY();

            switch (entity) {
                case Player p -> drawPlayer(gc, x, y, pos.angle());
                case Enemy e -> drawEnemy(gc, x, y, e);
                case Projectile proj -> drawProjectile(gc, x, y);
            }
        }

        drawHud(gc, engine);
    }

    private void drawHud(GraphicsContext gc, GameEngine engine) {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 20));
        gc.fillText(
                formatHudText(engine.getScore(), engine.getPlayer().getHealth(), engine.isGameOver()),
                20,
                35
        );

        if (engine.isGameOver()) {
            gc.setFill(Color.RED);
            gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 40));
            gc.fillText("GAME OVER", centerX - 110, centerY);
        }
    }

    static String formatHudText(int score, int health, boolean gameOver) {
        String base = String.format("Score : %d | Vies : %d", score, health);
        return gameOver ? base + " [GAME OVER]" : base;
    }

    private void drawPlayer(GraphicsContext gc, double x, double y, double angleRad) {
        Image sprite = assets.getImage(AssetLoader.PLAYER_SPRITE);
        if (sprite == null) {
            // Forme de secours si l'image manque (ne plante jamais)
            gc.setFill(Color.CYAN);
            gc.fillOval(x - 10, y - 10, 20, 20);
            return;
        }

        double drawSize = 40.0;
        double rotationDeg = -Math.toDegrees(angleRad) - 90.0;

        gc.save();
        gc.translate(x, y);
        gc.rotate(rotationDeg);
        gc.drawImage(sprite, -drawSize / 2, -drawSize / 2, drawSize, drawSize);
        gc.restore();
    }

    private void drawEnemy(GraphicsContext gc, double x, double y, Enemy enemy) {
        // Le sprite suit le rayon de collision du type : le tank est plus gros
        double drawSize = 2 * enemy.getCollisionRadius();

        Image sprite = switch (enemy.getType()) {
            case BASIQUE -> assets.getImage(AssetLoader.ENEMY_BASIQUE_SPRITE);
            case RAPIDE -> assets.getImage(AssetLoader.ENEMY_RAPIDE_SPRITE);
            case TANK -> assets.getImage(AssetLoader.ENEMY_TANK_SPRITE);
        };

        if (sprite == null) {
            // Forme de secours si l'image manque (ne plante jamais)
            gc.setFill(Color.RED);
            gc.fillOval(x - drawSize / 2, y - drawSize / 2, drawSize, drawSize);
            return;
        }

        gc.drawImage(sprite, x - drawSize / 2, y - drawSize / 2, drawSize, drawSize);
    }

    private void drawProjectile(GraphicsContext gc, double x, double y) {
        double drawSize = 14.0;
        Image sprite = assets.getImage(AssetLoader.PROJECTILE_SPRITE);

        if (sprite == null) {
            // Forme de secours si l'image manque (ne plante jamais)
            gc.setFill(Color.YELLOW);
            gc.fillOval(x - drawSize / 2, y - drawSize / 2, drawSize, drawSize);
            return;
        }

        gc.drawImage(sprite, x - drawSize / 2, y - drawSize / 2, drawSize, drawSize);
    }
}