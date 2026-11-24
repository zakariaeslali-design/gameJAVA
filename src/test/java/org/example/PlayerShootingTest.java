package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerShootingTest {
    private static final double TOLERANCE = 1e-9;
    private static final double ANGLE_JOUEUR = 1.0;

    private static List<Projectile> projectiles(GameEngine engine) {
        return engine.getEntities().stream()
                .filter(e -> e instanceof Projectile)
                .map(e -> (Projectile) e)
                .toList();
    }

    @Test
    void l_armeParDefautEstLeTirSimple() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 3));
        assertEquals(WeaponType.SIMPLE, engine.getWeapon());
    }

    @Test
    void tirSimpleCreeUnSeulProjectile() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 3));

        engine.playerShoot();

        List<Projectile> tirs = projectiles(engine);
        assertEquals(1, tirs.size());
        assertEquals(ANGLE_JOUEUR, tirs.get(0).getPosition().getAngle(), TOLERANCE);
        assertEquals(Player.ORBIT_RADIUS, tirs.get(0).getPosition().getRayon(), TOLERANCE);
    }

    @Test
    void tirTripleCreeTroisProjectilesAuxBonsAngles() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 3));
        engine.switchWeapon();

        engine.playerShoot();

        List<Projectile> tirs = projectiles(engine);
        assertEquals(3, tirs.size());
        assertEquals(ANGLE_JOUEUR - GameEngine.TRIPLE_SHOT_SPREAD, tirs.get(0).getPosition().getAngle(), TOLERANCE);
        assertEquals(ANGLE_JOUEUR, tirs.get(1).getPosition().getAngle(), TOLERANCE);
        assertEquals(ANGLE_JOUEUR + GameEngine.TRIPLE_SHOT_SPREAD, tirs.get(2).getPosition().getAngle(), TOLERANCE);
        for (Projectile p : tirs) {
            assertEquals(Player.ORBIT_RADIUS, p.getPosition().getRayon(), TOLERANCE);
            assertTrue(p.isAlive());
        }
    }

    @Test
    void switchWeaponAlterneEntreLesDeuxArmes() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 3));

        engine.switchWeapon();
        assertEquals(WeaponType.TRIPLE, engine.getWeapon());
        engine.switchWeapon();
        assertEquals(WeaponType.SIMPLE, engine.getWeapon());
    }

    @Test
    void onPeutRevenirAuTirSimpleApresLeTriple() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 3));
        engine.switchWeapon();
        engine.switchWeapon();

        engine.playerShoot();

        assertEquals(1, projectiles(engine).size());
    }

    @Test
    void plusieursTirsSAccumulent() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 3));
        engine.playerShoot();
        engine.switchWeapon();
        engine.playerShoot();

        assertEquals(4, projectiles(engine).size()); // 1 + 3
    }

    @Test
    void pasDeTirApresLeGameOver() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 1));
        engine.switchWeapon();
        engine.damagePlayer(); // game over

        engine.playerShoot();

        assertEquals(0, projectiles(engine).size());
    }

    @Test
    void lesProjectilesDuTirTripleAvancentVersLeCentre() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 3));
        engine.switchWeapon();
        engine.playerShoot();

        for (Projectile p : projectiles(engine)) {
            p.update();
            assertEquals(Player.ORBIT_RADIUS - GameEngine.PROJECTILE_SPEED, p.getPosition().getRayon(), TOLERANCE);
        }
    }

    @Test
    void leTirTripleTueUnEnnemiSurLesCotes() {
        GameEngine engine = new GameEngine(new Player(0.0, 3));
        engine.switchWeapon();
        // Ennemi sur le rayon du projectile de gauche/droite (angle +15 degres), proche de l'orbite
        Enemy enemy = new Enemy(280.0, GameEngine.TRIPLE_SHOT_SPREAD, 0.0);
        engine.addEntity(enemy);

        engine.playerShoot();
        engine.checkCollisions();
        // distance projectile (300) - ennemi (280) = 20 > 15 : pas encore touche
        assertTrue(enemy.isAlive());
        for (int i = 0; i < 4; i++) {
            projectiles(engine).forEach(Projectile::update); // 300 -> 280
        }
        engine.checkCollisions();

        assertFalse(enemy.isAlive());
        assertEquals(1, engine.getScore());
    }

    @Test
    void leControllerChangeLArme() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 3));
        GameController controller = new GameController(engine);

        controller.switchWeapon();
        assertEquals(WeaponType.TRIPLE, engine.getWeapon());
        controller.shoot();

        assertEquals(3, projectiles(engine).size());
    }
}
