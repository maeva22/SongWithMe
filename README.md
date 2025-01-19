# Présentation du Projet SongWithMe

Projet réalisé pour Maëva Desablens et Nathan Sans

## APK : 
[ApkSongWithMeMdNs](SongWithMeMdNs.apk)

## Présentation technique


### Organisation du Projet

Le projet a été segmenté en plusieurs fichiers pour faciliter lLa lisibilité, la modification et l'adaptation du code.

Chaque fichier est chargé d'une tâche spécifique :  
- Lancement de l'application  
- Gestion de l'affichage des erreurs  
- Affichage de la liste des musiques  
- Affichage des paroles  
- Transformation des fichiers de musique en structure de données  
- Téléchargement des fichiers `.mp3`  
- Téléchargement des fichiers `.md`  
- Gestion de la navigation dans l'application  


La navigation entre les pages est assurée par un système appelé **AppNavigation**, permettant de passer facilement du menu principal, à une page d'erreur ou à la page de karaoké.

---

### Téléchargement des données

Les données sont téléchargées et stockées dans un cache ou dans des fichiers locaux. Les musiques sont enregistrées directement sur l’appareil.

Un bouton a été ajouté pour forcer l’application à recharger les données. Ainsi, lors de chaque lancement, l’application n’effectue pas de requête pour récupérer des données supplémentaires. Sauf lors de son premier lancement.

---

### Traitement des fichiers `.md`

Pour faciliter le traitement des données, la regex suivante a été appliquée :  

```regex
(?=(?>\{ *([^\n {}]+) *\}([^\n{}]+\n?)(?>\{ *([^\n {}]+) *\}(\n)?)))
```
Cette regex permet de regrouper les données en "packs". Une fois récupérées, elles sont traitées et ajoutées à une structure.  

Ainsi, à la fin, nous obtenons :  
- Un tableau de cette dite structure où chaque ligne y est representer.  
- Dans chaque structure, des sous-tableaux simulent les cas où plusieurs marqueurs de temps sont présents dans cette même ligne.  

Pour parcourir toutes les paroles :  
1. Il suffit de parcourir tous les tableaux de chaque structure.  
2. Puis, passer à la structure suivante dans le tableau principal.  

Pour garantir la stabilité, une sécurité a été ajoutée au code :  
- L’effet karaoké ne commence que lorsque le temps de début de la ligne correspond exactement au temps du lecteur audio.  

---

### Améliorations possibles

Le temps imparti ne nous a pas permis d’apporter toutes les améliorations souhaitées au projet. Voici ce que nous aurions aimé améliorer :

#### 1. Le format de la regex
Bien que la regex fonctionne pour les fichiers tests, elle reste peu flexible. Une petite modification du fichier pourrait empêcher le code de fonctionner correctement.  

Pour améliorer cela, plusieurs méthodes sont possibles :  
- Ajouter des regex de validation pour s’assurer que le fichier respecte un format strict (par exemple : temps au format `{ mm:ss }`, présence d’un temps pour chaque début de ligne, etc.).  
- Vérifier que la structure est correctement remplie et qu’elle ne contient pas de valeurs incohérentes (par exemple : un temps plus petit d’une ligne à l’autre).  

#### 2. L’animation de la barre de progression
Nous aurions également souhaité améliorer l’animation de la barre de progression.  

Actuellement :  
- La barre commence à avancer seulement lorsque les paroles débutent.  

Amélioration envisagée :  
- Faire avancer la barre dans le vide avant que les paroles ne commencent.  
- Cela permettrait à l’utilisateur de visualiser quand la ligne débutera, rendant l’interface plus intuitive.


