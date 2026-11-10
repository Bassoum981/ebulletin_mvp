package tech.bassoum.ebulletin_mvp.controller;

import org.springframework.web.bind.annotation.*;
import tech.bassoum.ebulletin_mvp.dto.NoteResponseDTO;
import tech.bassoum.ebulletin_mvp.model.Note;
import tech.bassoum.ebulletin_mvp.service.BulletinService;

@RestController
@RequestMapping("/api/bulletins")
public class BulletinController {

    private final BulletinService bulletinService;

    public BulletinController(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }

    // Route pour ajouter une note et mettre à jour le bulletin via une requête HTTP POST
    @PostMapping("/ajouter-note")
    public NoteResponseDTO ajouterNote(
            @RequestParam Long eleveId,
            @RequestParam Long matiereId,
            @RequestParam Float valeur,
            @RequestParam String type,
            @RequestParam String trimestre,
            @RequestParam String anneeScolaire
    ) {
        return bulletinService.ajouterNoteEtMettreAJourBulletinDTO(
                eleveId, matiereId, valeur, type, trimestre, anneeScolaire
        );
    }
}