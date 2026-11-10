package tech.bassoum.ebulletin_mvp.api;

import org.springframework.web.bind.annotation.*;
import tech.bassoum.ebulletin_mvp.dto.ClasseDTO;
import tech.bassoum.ebulletin_mvp.service.ClasseService;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClasseController {

    private final ClasseService classeService;

    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }

    @GetMapping
    public List<ClasseDTO> listerClasses() {
        return classeService.listerClasses();
    }

    @PostMapping("/creer")
    public ClasseDTO creerClasse(
            @RequestParam String nom,
            @RequestParam String niveau,
            @RequestParam String anneeScolaire
    ) {
        return classeService.creerClasse(nom, niveau, anneeScolaire);
    }
}
