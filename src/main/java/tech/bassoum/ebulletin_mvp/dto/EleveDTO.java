package tech.bassoum.ebulletin_mvp.dto;

import lombok.Data;

@Data
public class EleveDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String numeroEtudiant;
    private ClasseDTO classe; // On imbrique le DTO de la classe, pas l'entité
}
