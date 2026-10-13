# Backlog : Orbit Invasion (version 2)

Projet ACL M1, Java 25, Maven, JavaFX, JUnit 5
Package : `org.example`

Le jeu : le joueur tourne autour d'un cercle (l'orbite) avec les flèches. Des ennemis apparaissent au centre et essaient de sortir du cercle, le joueur doit les tirer avant. Il peut aussi charger une **super attaque** et ramasser un **cœur** sur l'orbite.

**Équipe : Zakariae, Amine, Bilal, Salah**

## Ce qui change par rapport à la version 1

* **5 sprints** (les sprints 1 et 2 sont terminés, il reste les sprints 3, 4 et 5).
* Nouvelles fonctionnalités : super attaque, drop cœur, assets graphiques (joueur, ennemis, projectiles, cœur).
* Pour tenir en 5 sprints : un seul type de drop (le cœur) et pas de pause (`PausedState`). Ils sont en idées en réserve.
* **UML à chaque sprint** (pas seulement à la fin), validé par le prof avant de coder le sprint.
* Les tâches sont redistribuées entre les 4 membres.
* Règle conservée : 2 tâches liées par personne et par sprint, un testeur différent à chaque sprint.

## Définition de "terminé" (pour chaque tâche)

* Le code compile : `mvn clean compile`
* Les tests passent : `mvn clean test` (le plugin surefire 3.2.5 est dans le pom, donc `Tests run` ne doit plus afficher 0)
* Le jeu se lance : `mvn javafx:run`
* Le diagramme UML du sprint est à jour avec le code
* Le README reste valable (commandes de lancement)
* Commit sur sa branche, relecture rapide par un autre membre, puis merge sur `main`


## Sprint 3 : jeu jouable (collisions, ennemis, score, vies)

But : un jeu où on peut gagner des points et perdre. Pas de nouveautés visuelles encore.

Tâches :
* **checkCollisions()** : pour chaque projectile et chaque ennemi vivant, si `distanceTo()` est sous un seuil, les deux sont marqués morts. Attention au cas où les deux se croisent entre deux ticks (tester l'écart de rayon avec un seuil, pas une égalité).
* **nettoyage** : retirer de `entities` les ennemis morts et les projectiles sortis de l'orbite. Ne jamais modifier la liste pendant qu'on la parcourt.
* **spawnEnemy(angle)** : crée un ennemi au centre, appelé à intervalle régulier dans `tick()`, avec un angle aléatoire. Supprimer l'ennemi temporaire de `GameApp`.
* **checkEscapes()** : un ennemi qui dépasse le rayon de l'orbite est marqué mort et déclenche la perte d'une vie.
* **takeDamage() utilisé** : le joueur perd une vie par ennemi échappé, `gameOver` passe à `true` à 0 vie.
* **score** : +1 par ennemi tué, stocké dans `GameEngine`.
* **HUD** : score et vies affichés dans `GraphicalView`.
* **constante ORBIT_RADIUS** : une seule valeur partagée entre le moteur et la vue, et agrandir l'orbite dans la fenêtre.
* **ConsoleView** : afficher aussi le score.
* **tests** : `checkCollisions` (touche / ne touche pas / croisement entre deux ticks), `checkEscapes`, nettoyage, score, game over.
* **UML sprints 1 et 2** : diagramme de classes réel du code actuel + diagramme de séquence de la boucle de jeu (`GameApp` → `tick` → `update` → `render`).

Qui fait quoi :
* **Zakariae** : checkCollisions() + nettoyage des entités (les deux modifient la liste des entités)
* **Bilal** : spawnEnemy() + checkEscapes() + suppression de l'ennemi temporaire de GameApp
* **Salah** : takeDamage() utilisé + game over + score, puis UML des sprints 1 et 2
* **Amine** : HUD + ORBIT_RADIUS dans GraphicalView, et **testeur du sprint** (tests des collisions, des échappés, du score et du HUD)

## Sprint 4 : variété des ennemis et vrais assets

But : un jeu moins répétitif et qui a un vrai design (fini les ronds de couleur).

Tâches :
* **EnemyType** : enum `BASIQUE`, `RAPIDE`, `TANK` (le tank a plusieurs points de vie).
* **EnemyFactory** : `creerEnemy(type, angle)` retourne un ennemi configuré (vitesse, vie, points de score).
* **MovementStrategy** (interface) : `calculerDeplacement(delta)`.
* **RadialMovement** : tout droit vers l'extérieur (le comportement actuel).
* **SinuousMovement** : zigzag avec un cosinus.
* **AcceleratedMovement** : démarre lentement puis accélère.
* **Tirs du joueur** : tir simple, tir triple (3 directions), choix de l'arme avec une touche.
* **Assets graphiques** :
  * choisir un pack libre de droits (par exemple kenney.nl, licence CC0) et noter les sources dans le README (`CREDITS`),
  * dossier `src/main/resources/images/`,
  * classe `AssetLoader` qui charge chaque image **une seule fois** (jamais dans `render()`),
  * sprite du vaisseau joueur, orienté vers le centre,
  * un sprite par type d'ennemi, un sprite de projectile,
  * si une image manque, la vue dessine une forme de secours (pour ne pas planter).
* **Rayon de collision** : un rayon par type d'ennemi (le tank est plus gros).
* **tests** : `EnemyFactory` (chaque type a les bons attributs), les 3 stratégies (valeur attendue pour un delta donné), le tir triple (3 projectiles, angles attendus).
* **UML** : diagramme de classes du Factory et du Strategy (`EnemyType`, `EnemyFactory`, `MovementStrategy` et ses 3 implémentations).
* **Revue du sprint 3**.

Qui fait quoi :
* **Zakariae** : les 3 stratégies de mouvement + leurs tests
* **Amine** : EnemyType + EnemyFactory, puis AssetLoader + sprite du joueur
* **Salah** : sprites des ennemis (un par type) + sprite du projectile + rayons de collision par type
* **Bilal** : tirs du joueur (simple, triple, changement d'arme) et **testeur du sprint** (tests EnemyFactory, stratégies, tirs) + UML du sprint

Revue du sprint 3 : Zakariae.

## Sprint 5 : super attaque, drop cœur, menus, configuration, finitions

But : un vrai jeu fini, pas un prototype qui tourne en boucle.

### Super attaque

Règles proposées (à valider en équipe) :
* Une barre de charge de 0 à 100. Chaque ennemi tué la remplit d'un certain montant (`SUPER_CHARGE_PAR_KILL`, dans la config).
* Quand la barre est pleine, une touche (par exemple `S`) déclenche l'explosion.
* L'explosion part du centre de l'orbite, grandit en quelques dixièmes de secondes, et **tue tous les ennemis dans son rayon**. La barre revient à 0.
* Les ennemis tués par l'explosion rapportent du score mais ne rechargent pas la barre.

Tâches :
* **Charge** : attribut `superCharge` dans `GameEngine` (ou `Player`), incrémenté quand un ennemi est tué dans `checkCollisions()`, plafonné à 100.
* **activateSuper()** : refuse si la barre n'est pas pleine, sinon crée une entité `Explosion` et remet la charge à 0.
* **Explosion** : nouvelle entité (ajout dans le `permits` de `GameEntity` et dans le `switch` de `GraphicalView`), son rayon grandit à chaque `update()` puis elle disparaît.
* **Effet de l'explosion** : tue les ennemis dont `distanceTo()` au centre est inférieur au rayon courant.
* **Commande clavier** : branchement de la touche dans `KeyboardInputHandler` et `GameController`.
* **Barre de charge** dans le HUD, qui change d'apparence quand elle est pleine.
* **Animation de l'explosion** dans `GraphicalView` (cercle qui grandit et s'estompe, ou sprite).

### Drop cœur sur l'orbite

Règles proposées :
* Un cœur apparaît à un angle aléatoire **sur l'orbite**, reste quelques secondes, clignote avant de disparaître.
* Le joueur le ramasse en passant dessus (écart d'angle sous un seuil) et gagne 1 vie, dans la limite du maximum.

Tâches :
* **Drop** : nouvelle entité avec durée de vie (ajout dans `permits` et dans le `switch` de `GraphicalView`).
* **Apparition** : timer dans `tick()` avec un intervalle aléatoire.
* **checkPickup()** : détecte le ramassage, ajoute la vie, retire le drop. Tester le cas où l'angle passe par 0 / 2π (écart d'angle circulaire).
* **Plafond de vies** : le cœur ne dépasse pas le maximum.
* **Sprite du cœur** et clignotement avant disparition.
* **Retour visuel** au ramassage (petit effet ou texte flottant).

### Menus, configuration, finitions

Tâches :
* **GameState** (interface sealed) : `onEnter(engine)` et `handleInput(cmd)`.
* **MenuState / PlayingState / GameOverState** : les 3 implémentations.
* **Écran titre** : boutons "Jouer" et "Quitter".
* **Écran game over** : score final, meilleur score de la session, rejouer.
* **GameConfig** : vitesse de base, intervalle de spawn, difficulté, charge de la super attaque, fréquence du cœur.
* **ConfigLoader** : lit un fichier JSON et remplit un `GameConfig`.
* **waves_config.json** : les vagues d'ennemis (types, nombre, cadence).
* **Équilibrage** : ajuster les valeurs du fichier de config (ni trop facile ni trop dur, la super attaque ne doit pas être permanente).
* **Packaging** : `mvn package` donne un jar qui démarre `GameApp` (corriger le `mainClass`, qui vaut `org.example.Main` alors que le jeu démarre par `GameApp`, JavaFX doit être inclus ou documenté).

### Tests, UML, revue

* **tests** : la charge augmente quand un ennemi est tué, pas d'activation barre non pleine, charge remise à 0 après activation, l'explosion tue les ennemis dedans et pas ceux dehors, le cœur ajoute une vie et respecte le plafond, un drop expiré disparaît, ramassage de part et d'autre de l'angle 0.
* **Tests finaux** : au moins un test d'intégration qui fait tourner une petite partie du début à la fin (spawn, tir, collision, score, game over) et des tests du `ConfigLoader` (fichier valide, fichier manquant, valeur absente).
* **UML** : diagramme de séquence de la super attaque (touche → contrôleur → moteur → explosion → ennemis tués) et diagramme de classes des drops (`Drop`, `Explosion`, `GameEntity`).
* **UML final** : diagramme de classes complet, diagramme d'états de `GameState`, diagramme de cas d'utilisation, diagramme de séquence de la boucle de jeu. Tous doivent correspondre au code réel.
* **README final** : lancement, commandes, structure, crédits des assets.
* **Revue du sprint 4** et revue finale de tout le projet.

Qui fait quoi :
* **Zakariae** : charge de la super attaque + `activateSuper()` + `Explosion` et son effet (il réutilise `distanceTo()` et la logique de collision), puis `GameConfig` + `ConfigLoader` + `waves_config.json` (la charge par ennemi tué et la fréquence du cœur viennent de la config)
* **Amine** : touche clavier + barre de charge dans le HUD + animation de l'explosion, puis `GameState` + les 3 états + diagramme d'états et UML final
* **Bilal** : drop cœur (`Drop`, apparition aléatoire) + `checkPickup()` et plafond de vies, puis écran titre + écran game over (réutilise les états d'Amine) + README final
* **Salah** : sprite du cœur + clignotement et retour visuel, puis équilibrage avec le fichier de config + packaging du jar, et **testeur du sprint** (tests de la super attaque, du cœur, du loader + test d'intégration) + UML du sprint

Revue du sprint 4 : Amine.
Revue finale de tout le projet : Salah.

## Idées en réserve (seulement s'il reste du temps)

* Pause (`PausedState`) : touche pour mettre en pause et reprendre.
* Autres drops avec `DropType` : `CHARGE_SUPER`, `BONUS_SCORE`, `BOUCLIER`, `TIR_RAPIDE`.
* Un ennemi "boss" toutes les N vagues.
* Sauvegarde du meilleur score dans un fichier.
* Sons (tir, explosion, ramassage).

## À faire tout le temps

* Faire valider les diagrammes UML par le prof **avant** de coder le sprint.
* Commit régulier sur Git, une branche par personne, merge après relecture rapide entre nous.
* `git pull` avant de toucher un fichier partagé : `pom.xml`, `GameApp.java`, `GraphicalView.java`, `GameEntity.java`.
* Si un truc bloque, on en parle avant la séance encadrée suivante, pas le jour même.
* Le prof récupère le projet avec `git pull` puis `mvn javafx:run` : toujours vérifier que ça marche avant de pousser sur `main`.
