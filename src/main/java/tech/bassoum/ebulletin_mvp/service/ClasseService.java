package tech.bassoum.ebulletin_mvp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bassoum.ebulletin_mvp.dto.ClasseDTO;
import tech.bassoum.ebulletin_mvp.model.Classe;
import tech.bassoum.ebulletin_mvp.repository.ClasseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClasseService {

    private final ClasseRepository classeRepository;

    public ClasseService(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    /**
     * Lister toutes les classes
     */
    public List<ClasseDTO> listerClasses() {
        return classeRepository.findAll().stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    /**
     * Créer une nouvelle classe
     */
    @Transactional
    public ClasseDTO creerClasse(String nom, String niveau, String anneeScolaire) {
        Classe classe = new Classe();
        classe.setNom(nom);
        classe.setNiveau(niveau);
        classe.setAnneeScolaire(anneeScolaire);

        Classe classeSauvegardee = classeRepository.save(classe);
        return convertirEnDTO(classeSauvegardee);
    }

    /**
     * Conversion Entité -> DTO
     */
    public ClasseDTO convertirEnDTO(Classe classe) {
        ClasseDTO dto = new ClasseDTO();
        dto.setId(classe.getId());
        dto.setNom(classe.getNom());
        dto.setNiveau(classe.getNiveau());
        dto.setAnneeScolaire(classe.getAnneeScolaire());
        return dto;
    }
}
