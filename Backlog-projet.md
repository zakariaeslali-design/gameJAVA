# Backlog - orbit invasion 

Projet ACL M1 - Java 25 / Maven / JUnit 5
Package : org.example

Le jeu : le joueur tourne autour d'un cercle avec les flèches. Des ennemis apparaissent au centre et essaient de sortir du cercle, le joueur doit les tirer avant.

Equipe : Zakariae, Amine, Bilal, Salah, Wassim

Pour chaque sprint on essaie de donner à chacun 2 tâches liées (pas juste une classe toute seule), comme ça personne fait qu'un seul truc pendant tout le sprint. Les tests tournent aussi entre nous, une personne différente s'en occupe à chaque sprint.

---

## Sprint 1 : bases du jeu (pas de graphique, tout en console)

Le but c'est d'avoir un moteur qui marche, même moche, avant de faire joli.

- **GameEntity** : interface sealed avec les méthodes communes à toutes les entités (getPosition, update, isAlive)
- **GameEngine (structure)** : la classe qui va tout gérer, pour l'instant juste le squelette (attributs player, liste d'entities, gameOver)
- **PolarPosition** : record avec radius et angle, plus les méthodes toCartesianX(), toCartesianY() et distanceTo() pour calculer une distance entre deux positions
- **Vector2D** : record avec x et y, pour plus tard quand on aura besoin de vitesses en coordonnées cartésiennes
- **Player** : classe avec position, health, méthode move(angleDelta) pour tourner sur le cercle et takeDamage()
- **Enemy** : classe avec position, speed, alive, méthode update() qui le fait avancer vers l'extérieur
- **Projectile** : classe avec position, speed, active, méthode update() qui avance vers le centre
- **tick(delta)** : la boucle dans GameEngine qui appelle update() sur toutes les entités
- **Main.java** : crée un GameEngine, fait tourner la boucle quelques fois, affiche le résultat en console
- **printStateConsole()** : formatte l'affichage (position, score, vies) en texte lisible
- **pom.xml** : vérifier la config Maven (JDK 25, dépendance JUnit)
- **tests PolarPosition** : vérifier toCartesianX/Y et distanceTo avec des valeurs qu'on connait à l'avance

Qui fait quoi :

- **Zakariae** : PolarPosition + Vector2D + tests sur PolarPosition (il code les maths et les teste lui-même, logique)
- **Salah** : GameEntity + structure de GameEngine
- **Amine** : Player + Enemy
- **Bilal** : Projectile + Main.java (boucle de test dans Main)
- **Wassim** : tick() dans GameEngine + printStateConsole() + vérif pom.xml

Testeur du sprint : personne dédiée en plus, chacun teste ce qu'il fait (vu que c'est petit ce sprint)

---

## Sprint 2 : fenêtre graphique + contrôles

Le but : voir le joueur bouger dans une vraie fenêtre avec les flèches du clavier, et pouvoir tirer.

- **GameView (interface)** : une méthode render(engine) commune
- **ConsoleView** : garde l'affichage texte du sprint 1 mais propre, comme implémentation de GameView
- **GraphicalView** : fenêtre Swing (ou JavaFX), dessine le cercle et le joueur dessus
- **GameController** : reçoit les commandes (bouger, tirer) et les envoie au GameEngine
- **KeyboardInputHandler** : capte les vraies flèches gauche/droite + une touche pour tirer, appelle GameController
- **playerShoot()** dans GameEngine : crée un Projectile à la position du joueur
- dessiner les ennemis et projectiles dans GraphicalView (pas juste le joueur)
- **tests sur Player.move()** : vérifier que l'angle reste bien entre 0 et 2π même après plein de déplacements

Qui fait quoi :

- **Amine** : GraphicalView (dessin du cercle + joueur) et dessin des ennemis/projectiles
- **Bilal** : KeyboardInputHandler + branchement des touches
- **Salah** : playerShoot() + GameController
- **Wassim** : GameView (interface) + ConsoleView propre
- **Zakariae** : tests sur Player.move() et les angles (testeur du sprint)

---

## Sprint 3 : ennemis, collisions, score

Le but : que le jeu soit vraiment jouable, avec un objectif (score) et un risque (perdre).

- **checkCollisions()** : compare distance entre chaque Projectile et chaque Enemy avec distanceTo(), si trop proche = collision
- **spawnEnemy(angle)** : crée un Enemy au centre, appelé à intervalle régulier dans tick()
- **checkEscapes()** : vérifie si un Enemy a dépassé le rayon du joueur
- **takeDamage()** utilisé vraiment : le joueur perd un point de vie si un ennemi le dépasse (au lieu direct game over, ou les deux selon ce qu'on choisit)
- **score** : +1 (ou plus) à chaque fois qu'un Enemy est tué
- **nettoyage** : enlever de la liste entities les ennemis morts et les projectiles qui sont sortis de l'écran
- **HUD** : afficher score et vies dans la fenêtre graphique
- **tests checkCollisions/checkEscapes** : cas simple où ça doit toucher, cas où ça doit pas toucher

Qui fait quoi :

- **Zakariae** : checkCollisions() + tests dessus
- **Bilal** : spawnEnemy() + checkEscapes()
- **Wassim** : takeDamage() utilisé + gestion du score
- **Salah** : nettoyage de la liste entities (enemies morts, projectiles hors écran)
- **Amine** : HUD dans GraphicalView (testeur du sprint, teste le score/vies affichés)

---

## Sprint 4 : plusieurs types d'ennemis

Le but : rendre le jeu moins répétitif avec de la variété.

- **EnemyType** : enum BASIQUE, RAPIDE, TANK
- **EnemyFactory** : une méthode creerEnemy(type, angle) qui retourne un Enemy configuré selon le type (vitesse différente etc)
- **MovementStrategy (interface)** : une méthode calculerDeplacement(delta)
- **RadialMovement** : tout droit vers l'extérieur (ce qu'on a déjà, juste extrait dans une classe à part)
- **SinuousMovement** : déplacement en zigzag avec un cosinus
- **AcceleratedMovement** : commence lentement puis accélère
- **tirs différents pour le joueur** : par exemple tir simple vs tir qui part dans 3 directions
- **couleurs/formes différentes** selon le type d'ennemi dans GraphicalView
- **tests EnemyFactory** : vérifier que chaque type donne bien les bons attributs
- **tests des 3 stratégies de mouvement** : vérifier le calcul pour un delta donné

Qui fait quoi :

- **Amine** : EnemyType + EnemyFactory
- **Zakariae** : les 3 classes de MovementStrategy (Radial, Sinuous, Accelerated)
- **Salah** : les différents tirs du joueur
- **Wassim** : couleurs/formes des ennemis dans la vue graphique
- **Bilal** : tests EnemyFactory + tests des stratégies (testeur du sprint)

---

## Sprint 5 : menus et fichier de config

Le but : un vrai jeu fini, pas juste un prototype qui tourne en boucle.

- **GameState (interface sealed)** : une méthode par exemple onEnter(engine) et handleInput(cmd)
- **MenuState / PlayingState / PausedState / GameOverState** : les 4 implémentations
- **écran titre** : bouton "jouer" et "quitter" dans GraphicalView
- **écran game over** : affiche le score final, propose de rejouer
- **GameConfig** : classe simple qui stocke vitesse de base, intervalle de spawn, difficulté
- **ConfigLoader** : lit un fichier JSON (ou txt) et remplit un GameConfig
- **waves-config.json** : le fichier lui-même avec les vagues d'ennemis
- **équilibrage** : ajuster les valeurs dans le fichier de config pour que ce soit ni trop facile ni trop dur
- **UML final** : remettre à jour tous les diagrammes de classes pour qu'ils collent au code réel
- **packaging .jar** : mvn package doit donner un jar exécutable
- **tests finaux** : au moins un test d'intégration qui fait tourner une petite partie du début à la fin

Qui fait quoi :

- **Amine** : GameState + les 4 états
- **Bilal** : écran titre + écran game over
- **Zakariae** : GameConfig + ConfigLoader + waves-config.json
- **Wassim** : équilibrage du jeu avec le fichier de config
- **Salah** : UML final + packaging jar + tests d'intégration (testeur du sprint)

---

## A faire tout le temps

- faire valider les diagrammes UML par le prof avant de coder le sprint
- commit régulier sur Git, chacun sur sa branche, on merge après relecture rapide entre nous
- si un truc bloque, on en parle avant la séance encadrée suivante, pas le jour même
