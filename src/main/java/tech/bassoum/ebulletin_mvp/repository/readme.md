# 🗃️ Guide Complet : Les Repositories Spring Data JPA & Requêtes Personnalisées

Ce document récapitule tout ce qu'il faut savoir sur les **Repositories** dans Spring Boot : leur rôle, leur fonctionnement interne, ainsi que les différentes techniques pour y ajouter des méthodes et requêtes spécifiques.

---

## 1. C'est quoi un Repository ?

Dans l'architecture en couches de Spring Boot (Controller ➔ Service ➔ Repository ➔ Base de Données), le **Repository** est la couche d'accès aux données (**DAO** - *Data Access Object*).

Il sert de pont entre vos objets Java (Entités JPA) et les tables SQL de votre base de données.

```text
+-------------------+      +-------------------+      +-------------------+
|  Service (Métier) | ---> |    Repository     | ---> |  Base de Données  |
| (ex: BulletinSvc) |      | (Spring Data JPA) |      |      (MySQL)      |
+-------------------+      +-------------------+      +-------------------+
```

---

## 2. La Magie de `JpaRepository<T, ID>`

En déclarant une simple interface Java qui hérite de `JpaRepository<T, ID>` :

```java
package tech.bassoum.ebulletin_mvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bassoum.ebulletin_mvp.model.Eleve;

@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {
}
```

- **`T` (`Eleve`)** : L'entité JPA gérée par ce repository.
- **`ID` (`Long`)** : Le type de la clé primaire (`@Id`) de l'entité.

### 🛠️ Méthodes fournies de base (sans écrire aucun code) :
| Méthode | Rôle | Équivalent SQL |
| :--- | :--- | :--- |
| `save(eleve)` | Ajoute ou met à jour un élève. | `INSERT INTO ...` / `UPDATE ...` |
| `findById(id)` | Cherche un élève par son ID. | `SELECT * FROM eleves WHERE id = ?` |
| `findAll()` | Récupère tous les élèves. | `SELECT * FROM eleves` |
| `deleteById(id)` | Supprime un élève par son ID. | `DELETE FROM eleves WHERE id = ?` |
| `count()` | Compte le nombre d'élèves. | `SELECT COUNT(*) FROM eleves` |

---

## 3. Comment ajouter des fonctionnalités spécifiques ?

Il existe **3 grandes façons** d'ajouter des requêtes personnalisées dans un Repository Spring Data JPA.

---

### Method 1 : Les Méthodes Dérivées (*Derived Query Methods*)

Spring Data JPA est capable de **lire le nom de vos méthodes** et d'en déduire automatiquement la requête SQL correspondante !

#### Exemples dans `EleveRepository.java` :

```java
@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {

    // 1. Chercher un élève par son numéro d'étudiant
    // SQL généré : SELECT * FROM eleves WHERE numero_etudiant = ?
    Optional<Eleve> findByNumeroEtudiant(String numeroEtudiant);

    // 2. Chercher des élèves par leur nom (insensible à la casse)
    // SQL généré : SELECT * FROM usuarios u JOIN eleves e ON ... WHERE LOWER(u.nom) = LOWER(?)
    List<Eleve> findByNomIgnoreCase(String nom);

    // 3. Chercher tous les élèves d'une classe donnée par l'ID de la classe
    // SQL généré : SELECT * FROM eleves WHERE classe_id = ?
    List<Eleve> findByClasseId(Long classeId);

    // 4. Vérifier si un élève existe par son email (hérité de Utilisateur)
    boolean existsByEmail(String email);
}
```

#### 💡 Mots-clés utilisables dans les noms de méthodes :
- `findBy...`, `readBy...`, `queryBy...`, `countBy...`, `existsBy...`
- `And`, `Or` (ex: `findByNomAndPrenom`)
- `Containing`, `StartingWith`, `EndingWith` (ex: `findByNomContaining`)
- `GreaterThan`, `LessThan`, `Between`
- `OrderBy...Asc` / `OrderBy...Desc`

---

### Method 2 : Les Requêtes JPQL avec `@Query`

Si le nom de la méthode devient trop long ou complexe, vous pouvez écrire vous-même la requête en **JPQL** (*Java Persistence Query Language*).

> **Note :** En JPQL, on manipule les **classes Java et leurs attributs**, et non les tables et colonnes SQL !

#### Exemple dans `BulletinRepository.java` :

```java
@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Long> {

    // Récupérer tous les bulletins d'un élève pour un trimestre donné
    @Query("SELECT b FROM Bulletin b WHERE b.eleve.id = :eleveId AND b.trimestre = :trimestre")
    List<Bulletin> trouverBulletinsParEleveEtTrimestre(
        @Param("eleveId") Long eleveId, 
        @Param("trimestre") String trimestre
    );
}
```

---

### Method 3 : Les Requêtes SQL Natures (*Native Queries*)

Si vous devez exécuter une requête SQL complexe spécifique à MySQL (ex: jointures complexes, fonctions d'agrégation avancées), vous pouvez utiliser `nativeQuery = true`.

#### Exemple dans `NoteRepository.java` :

```java
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // Calculer directement en SQL la moyenne brute d'un élève pour une matière
    @Query(value = "SELECT AVG(valeur) FROM notes WHERE eleve_id = :eleveId AND matiere_id = :matiereId", nativeQuery = true)
    Float calculerMoyenneBruteParMatiere(@Param("eleveId") Long eleveId, @Param("matiereId") Long matiereId);
}
```

---

## 4. Synthèse des Choix pour vos Méthodes Personnalisées

| Besoins | Meilleure Option | Exemple |
| :--- | :--- | :--- |
| Recherche simple sur un champ (`email`, `numeroEtudiant`). | **Méthode Dérivée** (`findBy...`) | `findByEmail(String email)` |
| Recherche avec conditions multiples ou tris simples. | **Méthode Dérivée** (`findBy...And...`) | `findByClasseIdOrderByNomAsc(...)` |
| Jointures sur plusieurs entités / Logique complexe. | **Requête JPQL** (`@Query("...")`) | `@Query("SELECT b FROM Bulletin b ...")` |
| Optimisation SQL brute / Fonctions spécifiques MySQL. | **SQL Native** (`@Query(..., nativeQuery = true)`) | `@Query(value = "...", nativeQuery = true)` |
