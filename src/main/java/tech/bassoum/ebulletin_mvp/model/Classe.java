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
@Table(name = "classes")
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String niveau;
    private String anneeScolaire;

    @OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
    private List<Eleve> eleves = new ArrayList<>();
}
