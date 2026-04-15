# Gestion_Biblio

## Description
Gestion_Biblio est une application Java simple qui modelise une bibliotheque municipale.

Le programme permet de gerer :
- les livres
- les membres
- les emprunts
- les retours
- les amendes
- la caisse
- le transfert entre annexes
- la conservation des ouvrages

Les parties UML et base de donnees etaient retirees du projet.  
Le travail a donc ete centre sur la programmation orientee objet en Java.

## Classes principales
- `Bibliotheque` : gere les livres, les membres, les prets et la caisse
- `Livre` : represente un livre avec ses informations principales
- `Membre` : represente un membre de la bibliotheque
- `Pret` : represente l'emprunt d'un livre par un membre

## Fonctionnalites
Le programme simule un scenario complet :
- creation de la bibliotheque
- ajout de livres et de membres
- emprunt d'un livre
- retour avec retard
- paiement d'une amende
- transfert d'un livre vers une autre annexe
- verification de la conservation d'un ouvrage

Apres chaque action importante, l'etat du programme est affiche dans la console.

## Verification
Le programme respecte l'encapsulation avec des attributs `private` et contient des verifications simples pour gerer certains cas invalides :
- nom vide
- stock negatif
- retard negatif
- montant de paiement incorrect

## Execution
Compiler :
```powershell
javac -d out src\Main.java
