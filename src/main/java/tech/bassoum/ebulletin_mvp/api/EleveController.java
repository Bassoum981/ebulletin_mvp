package tech.bassoum.ebulletin_mvp.api;

import org.springframework.web.bind.annotation.*;
import tech.bassoum.ebulletin_mvp.dto.EleveDTO;
import tech.bassoum.ebulletin_mvp.service.EleveService;

import java.util.List;

@RestController
@RequestMapping("/api/eleves")
public class EleveController {

    private final EleveService eleveService;

    // Injection du service via le constructeur
    public EleveController(EleveService eleveService) {
        this.eleveService = eleveService;
    }

    /**
     * Endpoint GET : Lister tous les élèves
     * URL : GET http://localhost:8080/api/eleves
     */
    @GetMapping
    public List<EleveDTO> listerEleves() {
        return eleveService.listerEleves();
    }

    /**
     * Endpoint POST : Créer un nouvel élève
     * URL : POST http://localhost:8080/api/eleves/creer
     */
    @PostMapping("/creer")
    public EleveDTO creerEleve(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String email,
            @RequestParam String motDePasse,
            @RequestParam String numeroEtudiant,
            @RequestParam Long classeId
    ) {
        return eleveService.creerEleve(nom, prenom, email, motDePasse, numeroEtudiant, classeId);
    }
}
