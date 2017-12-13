Conception du ‘Simple’ Bike Game.

**_I – Modifications de l’architecture._**
-
Dans le cadre du développement du jeu, nous avons apportés quelques modifications à l’architecture globale, notamment celle de restructurer les fichiers d’une manière qui nous semblait moins verbose, notamment un package main dans le dossier src/ comme racine de notre projet. Nous avons tout de même noté que remplacer ‘main/java/ch/epfl/cs107/play’ en tant que dossiers imbriqués par un dossier nommé ‘main.java.ch.epfl.cs107.play’ aurait pu mener à certains problèmes chez certains IDE comme Eclipse. Nous avons aussi implémenté une nouvelle forme de texte car nous n’étions pas satisfaits de la police par défaut du projet, avant de finalement mettre la police comme option dans la création de textes.

**_II – Classes et fonctionalitées implémentées dans le projet._**
-
Nous avons en premier écrit les fichiers ActorGame.java, Game.java et GameEntity.java comme conseillé pour ensuite développer l’acteur principal de notre jeu, le Bike (dans Bike.java). Celuici regroupant différents sous objets (les roues et le personnage animé), nous les avons déclarés dans des classes séparées (respectivement Wheel.java et CharacterBike.java). Ceci nous à permis de gérer leurs propriétés respectives, telles que les contraintes ou leur animation séparément.

Ensuite nous nous sommes occupés de la création de nouveaux acteurs, dont voici la liste exhaustive, (pour plus de détails, se référer à la JavaDoc disponible dans les fichiers et sur NB0174.github.io) :

    	Une boîte aux dimensions réglables (Crate.java, demandée dans le tutoriel) ;
    	Un terrain entièrement modularisé, permettant de générer des terrains normaux, boueux, glacés ou d’intérieur avec un appel 
    d’enum différent. (Terrain.java et TerrainType.java, la classe et l’enum respectivement) ;
    	Un point d’attache général pour les divers objets sous contrainte dans le projet. (AnchorPoint.java) ;
    	Une plateforme basique, qui ne sert que de sousobjet à différents types de plateformes. (Platform.java et GenericPlatform.java, 
    le premier pour les PrismaticConstraint et le second pour la WeldConstraint) ;
    	Une plateforme mobile, attachée à un point d’attache et qui se déplace sur un axe en un temps défini. (MovingPlatform.java) ;
    	Une plateforme mobile nécessitant une action de l’utilisateur pour son activation. (TriggeredPlatform.java) ;
    	Un levier permettant de lancer une action tel que le déplacement d’une plateforme (Lever.java) ;
    	Une interface gérant tous les objets que le joueur peut ramasser. (Collectable.java) ; 
    	Des pièces dont la collection impacte le score. (Coin.java) ;
    	Un émetteur de particule extrêmement modulable, permettant de générer des centaines de particules par seconde. (ParticleEmitter.java) ;
    	Une particule qui peut changer de couleur. (Particle.java) ;
    	Un obstacle à la progression linéaire, permettant de générer des falaises ou des rochers. Ceux-ci ont un relief marqué,
     mais cela ne provient pas d'une génération aléatoire. En réalité nous avons créé les formes dans GEOGEBRA, qui à pu exporter 
     les données au format Asymptote, que nous avons pu parser avec Python pour en extraire les coordonées des points des polygones.
      (Obstacle.java) ;
    	Une mine qui explose après un délai lorsque le joueur à roulé dessus. (Mine.java) ;
    	Des checkpoints qui sauvegardent la progression. (Checkpoint.java) ;
    	Des détecteurs de présence (ProximitySensor.java et KeyboardProximitySensor.java) ;
    	Des liquides comme de la lave et de l’acide (Liquid.java) ;
    	Un laser qui émet une oscillation mortelle au joueur (Laser.java) ;
    	Un trampoline qui permet de faire rebondir le joueur (Trampoline.java) ;
    	Un puits de gravité qui propulse le joueur dans la direction indiquée par ses particules (GravityWell.java) ;
    	Un baril explosif ou acide (encore une fois mortel au joueur). (BoomBarrel.java) ;
    	Un acteur de fin de niveau qui déclenche le prochain niveau et quelques animations. (FinishActor.java) ;
    	Quelques armes comme un fusil à pompe ou un missile, pour détruire les barils explosifs. (Shotgun.java et Missile.java).
    	Un gestionnaire de Graphismes. (GameManager.java) ;
    	Une classe statique qui généralise les liens entre deux entités par le biais de contraintes. (Linker.java) ;
    	Une classe permettant de lancer des actions en parallèle au main Swing thread. (ParallelAction.java) ;
    	Une classe énumérant les différents groupes de collision des objets pour la lisibilité. (ObjectGroup.java) ;
    	Une classe de gestion du son, malheureusement l’implémentation du SwingWorker pour délayer le Thread empêche une 
    gestion des longs morceaux en lien avec le niveau, donc une compilation est lancée dés le lancement du jeu. (Audio.java) ;
    	La création d’un fond animé au jeu, pour le rendre plus vivant. (Scenery.java) ;
    	Une superclasse à tous les objets du fond animé. (GraphicalObjects.java représentant par exemple BlowingLeaf.java).
    	Une énumération de modèles graphiques de fond prédéfinis. (Preset.java) ;
    	Un gestionnaire de graphismes de fin de niveau. (EndGameGraphics.java) ;
    	L’affichage d’une police crée par nousmêmes que nous préférions à la police par défaut du projet. (BetterTextGraphics.java) ;
    	Un acteur gérant le dessin de notre nouvelle police. (GraphicalDrawer.java) ;
    	Différents niveaux de jeu.
    	Un écran d'accueil du jeu, qui permet :
        Charger les niveaux que nous avons codé, sur la droite, nommés “LEVEL” suivis du numéro, le niveau 0 est le tutoriel.
        Jouer les niveaux dans l’ordre, au centre de l’écran (bouton “PLAY”)
        Charger un niveau créé avec l’éditeur de niveau sur la gauche, les boutons “SAVE” suivis du numéro de la sauvegarde, à leur 
        gauche se trouve une croix rouge, qui permet de supprimer la sauvegarde (une opération irréversible et sans confirmation,
         dues aux limitation techniques et temporelles).
        Accéder à l'éditeur de niveau, le bouton “LEVEL EDITOR” situé au dessu des boutons “LEVEL”

•	Editeur de niveaux qui permet de créer un Niveau dans le jeu et de le sauvegarder pour pouvoir le recharger depuis le menu principal.

    	Possibilité d’ajouter les acteurs suivants :
    	Caisse (Crate)
    	Point d’apparition du joueur (Spawn point)
    	Checkpoint (Checkpoint)
    	Ligne d'arrivée (Finish point)
    	Pièce (Coin)
    	Plateforme (Platform)
    	Trampoline (Trampoline)
    	Liquide (Liquid)
    	Laser (Laser)
    	Mine (Mine)
    	Baril explosif (BoomBarrel)
    	Pendule (Pendulum)

_Nota_ : Certains de ces acteurs peuvent être configurés à l’aide de boites de texte.

•Possibilité de :
    	Zoomer avec la molette de la souris, 
    	Se déplacer avec WASD ou ←↑→↓, 
    	Se déplacer plus rapidement si la touche CTRL est appuyée
    	Reset la position de la caméra
    	Avoir les coordonnées d’un point à l’écran
    	Sauvegarder le niveau créé, pour le recharger depuis le menu principal

	Menu de pause:
    	Possibilité de revenir au menu principal

_Nota_ : Veuillez pardonner l’éventuel oubli de classe ou de fonctionnalité, la liste est longue…



