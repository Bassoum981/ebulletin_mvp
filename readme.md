# 🎒 Cahier des Charges & Roadmap — MVP : eBulletin

## 1. Objectif du Projet
Développer un socle Spring Boot robuste pour l'application **eBulletin** permettant de gérer les structures scolaires, la saisie des notes par les enseignants, et le calcul automatisé des bulletins de notes des élèves.

---

## 2. Périmètre du MVP (Ce qui est inclus vs exclu)

### ✅ Inclus dans le MVP (Fonctionnalités Vitales)
1. **Gestion des Utilisateurs (Héritage) :**
   - `Utilisateur` (Classe abstraite : nom, prénom, email, mot de passe).
   - `Eleve` (Rattaché à une classe, possède des notes et des bulletins).
   - `Enseignant` (Enseigne des matières et saisit des notes).
   - `Administrateur` (Gestion globale).
2. **Structure Pédagogique :**
   - Une `Classe` regroupe des `Eleve`s.
   - Une `Matière` possède un nom et un coefficient.
3. **Évaluation & Notes :**
   - Un `Enseignant` attribue une `Note` à un `Eleve` pour une `Matière` donnée.
4. **Le Bulletin (Cœur du métier) :**
   - Un `Bulletin` regroupe les notes d'un élève pour un trimestre donné.
   - **Calcul automatique** de la moyenne générale pondérée en fonction des coefficients des matières.

### ❌ Exclu du MVP (Pour plus tard / Versions futures)
- Génération et téléchargement de fichiers PDF des bulletins.
- Interface web complète / Front-end Thymeleaf poussé (on se concentrera d'abord sur les tests ou une API REST de base).
- Gestion des emplois du temps, des absences et des retards.
- Système de sécurité avancé (Spring Security / JWT).

---

## 3. Modèle de Données (Entités JPA)

* **`Utilisateur`** (Table: `utilisateurs` - Stratégie d'héritage `JOINED`)
* **`Eleve`** (Hérite de `Utilisateur`, Table: `eleves` - `@ManyToOne` vers `Classe`)
* **`Enseignant`** (Hérite de `Utilisateur`, Table: `enseignants`)
* **`Administrateur`** (Hérite de `Utilisateur`, Table: `administrateurs`)
* **`Classe`** (Table: `classes` - Nom, niveau, année scolaire)
* **`Matiere`** (Table: `matieres` - Nom, coefficient)
* **`Note`** (Table: `notes` - Valeur, type, date, liens vers `Eleve`, `Enseignant`, `Matiere`, `Bulletin`)
* **`Bulletin`** (Table: `bulletins` - Trimestre, année, moyenne générale, appréciation)

---

## 4. Plan de Développement (Roadmap par Étapes)

- [ ] **Étape 1 :** Création des entités de base et des relations JPA (`Utilisateur`, `Eleve`, `Enseignant`, `Administrateur`).
- [ ] **Étape 2 :** Création des entités structurelles (`Classe`, `Matiere`).
- [ ] **Étape 3 :** Création des entités transactionnelles (`Note`, `Bulletin`).
- [ ] **Étape 4 :** Implémentation de la logique métier (la méthode de calcul de la moyenne générale pondérée dans l'entité `Bulletin`).
- [ ] **Étape 5 :** Création des Repositories Spring Data JPA pour tester l'insertion et la récupération en base H2.
