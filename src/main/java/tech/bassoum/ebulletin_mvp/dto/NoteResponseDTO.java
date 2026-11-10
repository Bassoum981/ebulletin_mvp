package tech.bassoum.ebulletin_mvp.dto;

import lombok.Data;
import java.util.Date;

@Data
public class NoteResponseDTO {
    private Long id;
    private Float valeur;
    private String type;
    private Date date;
    private String matiereNom;
    private Float matiereCoefficient;
    private Float moyenneGeneraleBulletin;
    private String appreciationBulletin;
    private EleveDTO eleve;
}
