package tech.bassoum.ebulletin_mvp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tech.bassoum.ebulletin_mvp.service.ClasseService;
import tech.bassoum.ebulletin_mvp.service.EleveService;

@Controller
public class WebController {

    private final EleveService eleveService;
    private final ClasseService classeService;

    public WebController(EleveService eleveService, ClasseService classeService) {
        this.eleveService = eleveService;
        this.classeService = classeService;
    }

    /**
     * Page d'accueil / Dashboard listant les élèves et les classes
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("eleves", eleveService.listerEleves());
        model.addAttribute("classes", classeService.listerClasses());
        return "index"; // Va chercher le fichier index.html dans src/main/resources/templates/
    }
}
