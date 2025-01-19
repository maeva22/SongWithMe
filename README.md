# Présentation du Projet SongWithMe

Projet fait pour Maëva Desablens et Nathan Sans

## Présentationt technique 

### Organisation du Projet

Le projet a été segmenter en plusieur fichier pour faciliter la lisibiliter et la faciliter de modification / amélioration. 

Ainsi on a un fichier charger :
  - Du lancement de l'application
  - De l'affichage d'erreur
  - D'afficher la liste de musique
  - D'afficher les paroles
  - De transformer les fichier de musique en structure de donnée
  - Du téléchargement du fichier .mp3
  - Du téléchargement du fihcier .md
  - D'un fichier de navigation

Notre navigation entre les pages se fait grace a un "AppNavigation". Ce dernier nous permet de faciliter le passage du menu a une page d'erreur ou a la page de Karaoké.

### Téléchargement des données

Nos donnée son télécharger et stoquer dans un cache et dans des fichier. Les musique sont elle télécharger en durs sur le téléphone. 

Un bouton a été mis en place pour force l'application a recharger les donnée. Ainsi a chaque lancement l'application ne ferra pas de requête pour récuperer de nouvelle donnée.

### Traitement des fichier .md

Pour faciliter le traitement des donner nous avons appliquer la regex suivante : 

</code>(?=(?>\{ *([^\n {}]+) *\}([^\n{}]+\n?)(?>\{ *([^\n {}]+) *\}(\n)?)))</code>

Cette dernièrr permet de nous faire des packs de données. Une fois récuperer elle seront traiter et ajouter a une structure.

Ainsi a la fin on aura un tableau de cette structure pour chaque ligne. Dans cette structure on aura de nouveau des tableau pour simuler le cas ou l'on peu avoir plusieur limiteur de temsp au seins d'une seule et même ligne. 
Il suffira donc de pacourire tout les tableau de la structure puis de passer a la suivante dans le tebleau pour parcourir toutes les paroles. 

Pour s'assurer de la stabiliter un sécuriter a été ajouter a notre code de telle sorte que nous ne commençons l'effet de karaoké sur un parole temps que le temps de début de la ligne et de le temps du player ne sont pas identique. 

### Amélioration Possible 

Le temps ne nous a pas suffit pour améliorer autant que shouaiter le projet nous avons donc décrit ci dessous ce que nous aurions aimer améliorer.

#### Le format de la regex : 
Si la regex fonctionne pour les fichier test, les regex sont peu flexible. Donc en cas d'une petit modification le code ne fonctionerais plus comme voulue. Il faudrais donc vérifier le bon format de nos fichier. 
Pour ce faire plusieur méthode son possible. On pourrais ajouter des regex de verification pour au moins s'assurer que le fichier sois dans le bon format ( ie, temps au fromat  { mm:ss }, temps pour chaque début de ligne, ... ).

Une autre vérification serait de s'assurer que notre structure est bien rmeplis et ne contient pas des valeur trop étrange, comme un temps plus petit d'une ligne a l'autre. 

D'autre amélioration aurait pus être aussi appliquer sur l'annimation pour que la barre d'avancement soit déjà en mouvement avant les paroles. Par ici on entend que la barre de progression avanerais dans la vide pour que l'utilisateur comprenne quand la ligne commencera. 

