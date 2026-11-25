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
    void lesTroisProjectilesDuTirTripleSortentDeLaPositionDuJoueur() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 3));
        engine.switchWeapon();

        engine.playerShoot();

        List<Projectile> tirs = projectiles(engine);
        assertEquals(3, tirs.size());
        // Les trois tirs partent de la position exacte du joueur, pas d'un point decale
        for (Projectile p : tirs) {
            assertEquals(ANGLE_JOUEUR, p.getPosition().getAngle(), TOLERANCE);
            assertEquals(Player.ORBIT_RADIUS, p.getPosition().getRayon(), TOLERANCE);
            assertTrue(p.isAlive());
        }
    }

    @Test
    void lesTirsLaterauxS_ecartentPuisConvergentAuCentre() {
        GameEngine engine = new GameEngine(new Player(ANGLE_JOUEUR, 3));
        engine.switchWeapon();
        engine.playerShoot();

        List<Projectile> tirs = projectiles(engine);
        int demiCourse = (int) (Player.ORBIT_RADIUS / 2 / GameEngine.PROJECTILE_SPEED);

        // A mi-course (rayon = ORBIT_RADIUS / 2), l'ecart lateral est maximal
        for (int i = 0; i < demiCourse; i++) {
            tirs.forEach(Projectile::update);
        }
        assertEquals(ANGLE_JOUEUR - GameEngine.TRIPLE_SHOT_SPREAD, tirs.get(0).getPosition().getAngle(), 1e-6);
        assertEquals(ANGLE_JOUEUR, tirs.get(1).getPosition().getAngle(), TOLERANCE);
        assertEquals(ANGLE_JOUEUR + GameEngine.TRIPLE_SHOT_SPREAD, tirs.get(2).getPosition().getAngle(), 1e-6);

        // Au centre, les trois tirs se rejoignent sur l'angle du joueur
        for (int i = 0; i < demiCourse; i++) {
            tirs.forEach(Projectile::update);
        }
        for (Projectile p : tirs) {
            assertFalse(p.isAlive());
            assertEquals(ANGLE_JOUEUR, p.getPosition().getAngle(), 1e-6);
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
        // Ennemi place sur la trajectoire courbe du projectile lateral, proche de l'orbite
        double rayonEnnemi = 280.0;
        double ecart = GameEngine.TRIPLE_SHOT_SPREAD * Math.sin(Math.PI * (1.0 - rayonEnnemi / Player.ORBIT_RADIUS));
        Enemy enemy = new Enemy(rayonEnnemi, ecart, 0.0);
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
