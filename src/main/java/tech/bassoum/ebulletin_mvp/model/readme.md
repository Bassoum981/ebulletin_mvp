# 📚 Guide Explicatif : Architecture & Concepts Spring Boot (eBulletin MVP)

Ce document récapitule les concepts fondamentaux de la programmation et du mapping objet-relationnel (ORM) utilisés dans le projet **`ebulletin_mvp`**.

---

## 1. L'Analogie du Monde Réel vs Code

Dans notre projet de gestion scolaire (`ebulletin`), les classes Java traduisent le fonctionnement réel d'un établissement :

1. **Les Personnes :** Tout le monde partage des attributs de base (nom, prénom, email, mot de passe). En Java, cela est regroupé dans une classe mère `Utilisateur`.
2. **Les Structures Pédagogiques :** Les `Classe`s (ex: *Licence 3 GIT*) et les `Matiere`s (ex: *Java*, *PHP* avec leurs coefficients).
3. **Les Opérations et Livrables :** 
   - Un `Enseignant` attribue une `Note` à un `Eleve` pour une `Matiere`.
   - Le système regroupe ces `Note`s dans un `Bulletin` et effectue le calcul automatique de la moyenne.

---

## 2. Les 3 Piliers de notre Code

### 🤖 A. Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`)
En Java standard, pour chaque attribut (ex: `private String nom;`), il faut réécrire manuellement des getters, des setters et des constructeurs (code lourd appelé *boilerplate*).

- **`@Getter` / `@Setter`** : Indique à Lombok de générer automatiquement les méthodes de lecture (`getNom()`) et d'écriture (`setNom()`) lors de la compilation.
- **`@NoArgsConstructor`** : Génère un constructeur sans argument (`public Eleve() {}`), indispensable pour qu'Hibernate puisse instancier les objets en lisant la base de données.

---

### 🗄️ B. JPA / Hibernate (`@Entity`, `@Table`, `@Id`)
JPA (Jakarta Persistence API) fait le pont entre le monde Orienté Objet (Java) et le monde Relationnel (SQL).

- **`@Entity`** : Signale à Spring Boot que cette classe Java correspond à une table dans la base de données.
- **`@Table(name = "...")`** : Précise le nom exact de la table SQL associée.
- **`@Id` & `@GeneratedValue`** : Définit la clé primaire unique auto-incrémentée (1, 2, 3...) de la table.

---

### 🔗 C. Les Relations entre Entités (`@ManyToOne`, `@OneToMany`)

Les annotations de relation traduisent directement la logique métier du diagramme UML :

#### 1. `@ManyToOne` (Plusieurs à Un)
- **Logique :** **Plusieurs** élèves (`Many`) appartiennent à **une seule** classe (`One`).
- **Exemple dans `Eleve.java` :**
  ```java
  @ManyToOne
  @JoinColumn(name = "classe_id")
  private Classe classe;
  ```
- **Résultat SQL :** Crée une clé étrangère `classe_id` dans la table `eleves`.

#### 2. `@OneToMany` (Un à Plusieurs)
- **Logique :** **Une seule** classe (`One`) contient **plusieurs** élèves (`Many`).
- **Exemple dans `Classe.java` :**
  ```java
  @OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
  private List<Eleve> eleves = new ArrayList<>();
  ```
- **Le rôle de `mappedBy` :** Indique que la relation est déjà gérée du côté de l'attribut `classe` dans la classe `Eleve`. Cela évite de créer des tables d'association inutiles.

---

## 3. Synthèse des 7 Classes du Modèle

| Classe | Rôle Métier | Relations Principales |
| :--- | :--- | :--- |
| **`Utilisateur`** | Classe mère abstraite (infos communes). | Héritage `JOINED` vers `Eleve`, `Enseignant`, `Administrateur`. |
| **`Eleve`** | Représente un étudiant. | `@ManyToOne` avec `Classe`, `@OneToMany` avec `Note` et `Bulletin`. |
| **`Enseignant`** | Représente un professeur. | `@OneToMany` avec `Note` (notes qu'il a saisies). |
| **`Administrateur`** | Gestionnaire du système. | Hérite de `Utilisateur`. |
| **`Classe`** | Groupe d'élèves (ex: L3). | `@OneToMany` avec `Eleve`. |
| **`Matiere`** | Discipline enseignée + coefficient. | Reliée aux `Note`s. |
| **`Note`** | Évaluation attribuée. | Relie **1 Élève**, **1 Enseignant**, **1 Matière**, et **1 Bulletin**. |
| **`Bulletin`** | Synthèse des notes du trimestre. | `@ManyToOne` avec `Eleve`, `@OneToMany` avec `Note` + Méthode `calculerMoyenneGenerale()`. |
