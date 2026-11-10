package tech.bassoum.ebulletin_mvp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bulletins")
public class Bulletin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trimestre;
    private String anneeScolaire;
    private float moyenneGenerale;
    private String appreciation;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @OneToMany(mappedBy = "bulletin", cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();

    // Méthode métier pour calculer la moyenne générale pondérée
    public float calculerMoyenneGenerale() {
        if (notes == null || notes.isEmpty()) return 0f;

        float sommeNotesCoeff = 0f;
        float sommeCoeff = 0f;

        for (Note note : notes) {
            float coeff = (note.getMatiere() != null) ? note.getMatiere().getCoefficient() : 1.0f;
            sommeNotesCoeff += note.getValeur() * coeff;
            sommeCoeff += coeff;
        }

        if (sommeCoeff > 0) {
            this.moyenneGenerale = sommeNotesCoeff / sommeCoeff;
        }
        return this.moyenneGenerale;
    }
}
