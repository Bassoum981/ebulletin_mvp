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
@Table(name = "enseignants")
public class Enseignant extends Utilisateur {

    private String matiereEnseignee;

    @OneToMany(mappedBy = "enseignant")
    private List<Note> notesSaisies = new ArrayList<>();

    @OneToMany(mappedBy = "enseignant")
    private List<Matiere> matieres = new ArrayList<>();

}
