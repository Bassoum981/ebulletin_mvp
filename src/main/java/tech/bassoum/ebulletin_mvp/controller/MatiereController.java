package tech.bassoum.ebulletin_mvp.controller;

import org.springframework.web.bind.annotation.*;
import tech.bassoum.ebulletin_mvp.dto.MatiereDTO;
import tech.bassoum.ebulletin_mvp.service.MatiereService;

import java.util.List;

@RestController
@RequestMapping("/api/matieres")
public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    @GetMapping
    public List<MatiereDTO> listerMatieres() {
        return matiereService.listerMatieres();
    }

    @PostMapping("/creer")
    public MatiereDTO creerMatiere(
            @RequestParam String nom,
            @RequestParam float coefficient
    ) {
        return matiereService.creerMatiere(nom, coefficient);
    }
}
