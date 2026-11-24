package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

class EnemyFactoryTest {
    private static final double TOLERANCE = 1e-9;

    @Test
    void basiqueAvecLesBonsAttributs() {
        Enemy e = EnemyFactory.creerEnemy(EnemyType.BASIQUE, 0.0);

        assertEquals(EnemyType.BASIQUE, e.getType());
        assertEquals(1, e.getHealth());
        assertEquals(1, e.getScorePoints());
        assertEquals(Enemy.DEFAULT_COLLISION_RADIUS, e.getCollisionRadius(), TOLERANCE);
        assertInstanceOf(RadialMovement.class, e.getStrategy());
    }

    @Test
    void rapideAvecLesBonsAttributs() {
        Enemy e = EnemyFactory.creerEnemy(EnemyType.RAPIDE, 0.0);

        assertEquals(EnemyType.RAPIDE, e.getType());
        assertEquals(1, e.getHealth());
        assertEquals(2, e.getScorePoints());
        assertEquals(12.0, e.getCollisionRadius(), TOLERANCE);
        assertInstanceOf(SinuousMovement.class, e.getStrategy());
    }

    @Test
    void tankAvecLesBonsAttributs() {
        Enemy e = EnemyFactory.creerEnemy(EnemyType.TANK, 0.0);

        assertEquals(EnemyType.TANK, e.getType());
        assertEquals(3, e.getHealth());
        assertEquals(3, e.getScorePoints());
        assertEquals(30.0, e.getCollisionRadius(), TOLERANCE);
        assertInstanceOf(AcceleratedMovement.class, e.getStrategy());
    }

    @Test
    void leTankEstPlusGrosQueLesAutres() {
        double tank = EnemyFactory.creerEnemy(EnemyType.TANK, 0.0).getCollisionRadius();
        double basique = EnemyFactory.creerEnemy(EnemyType.BASIQUE, 0.0).getCollisionRadius();
        double rapide = EnemyFactory.creerEnemy(EnemyType.RAPIDE, 0.0).getCollisionRadius();

        assertTrue(tank > basique);
        assertTrue(tank > rapide);
    }

    @Test
    void l_ennemiApparaitAuCentreAvecLAngleDemande() {
        Enemy e = EnemyFactory.creerEnemy(EnemyType.TANK, 1.2);

        assertEquals(0.0, e.getPosition().getRayon(), TOLERANCE);
        assertEquals(1.2, e.getPosition().getAngle(), TOLERANCE);
        assertTrue(e.isAlive());
    }

    @Test
    void chaqueAppelCreeUnNouvelEnnemiAvecSaPropreStrategie() {
        Enemy a = EnemyFactory.creerEnemy(EnemyType.RAPIDE, 0.0);
        Enemy b = EnemyFactory.creerEnemy(EnemyType.RAPIDE, 0.0);

        assertNotSame(a, b);
        assertNotSame(a.getStrategy(), b.getStrategy());
    }

    @Test
    void leTankMeurApresTroisTirs() {
        Enemy tank = EnemyFactory.creerEnemy(EnemyType.TANK, 0.0);

        tank.takeDamage();
        assertEquals(2, tank.getHealth());
        assertTrue(tank.isAlive());
        tank.takeDamage();
        assertEquals(1, tank.getHealth());
        assertTrue(tank.isAlive());
        tank.takeDamage();
        assertEquals(0, tank.getHealth());
        assertFalse(tank.isAlive());
    }

    @Test
    void laVieNeDevientJamaisNegative() {
        Enemy basique = EnemyFactory.creerEnemy(EnemyType.BASIQUE, 0.0);

        basique.takeDamage();
        basique.takeDamage();

        assertEquals(0, basique.getHealth());
        assertFalse(basique.isAlive());
    }

    @Test
    void leTankAccelereQuandIlSeDeplace() {
        Enemy tank = EnemyFactory.creerEnemy(EnemyType.TANK, 0.0);

        tank.update();
        double premierPas = tank.getPosition().getRayon();
        tank.update();
        double deuxiemePas = tank.getPosition().getRayon() - premierPas;

        assertTrue(deuxiemePas > premierPas, "le 2e pas doit etre plus grand que le 1er");
    }

    @Test
    void leConstructeurDeLaFactoryEstPrive() throws Exception {
        Constructor<?> c = EnemyFactory.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(c.getModifiers()));
    }
}
