package tech.bassoum.ebulletin_mvp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bassoum.ebulletin_mvp.dto.ClasseDTO;
import tech.bassoum.ebulletin_mvp.dto.EleveDTO;
import tech.bassoum.ebulletin_mvp.model.Classe;
import tech.bassoum.ebulletin_mvp.model.Eleve;
import tech.bassoum.ebulletin_mvp.repository.ClasseRepository;
import tech.bassoum.ebulletin_mvp.repository.EleveRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EleveService {

    private final EleveRepository eleveRepository;
    private final ClasseRepository classeRepository;

    public EleveService(EleveRepository eleveRepository, ClasseRepository classeRepository) {
        this.eleveRepository = eleveRepository;
        this.classeRepository = classeRepository;
    }

    /**
     * Récupérer la liste de tous les élèves convertie en DTO
     */
    public List<EleveDTO> listerEleves() {
        return eleveRepository.findAll().stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    /**
     * Créer un nouvel élève et le rattacher à une classe
     */
    @Transactional
    public EleveDTO creerEleve(String nom, String prenom, String email, String motDePasse, String numeroEtudiant, Long classeId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new RuntimeException("Classe introuvable avec l'ID: " + classeId));

        Eleve eleve = new Eleve();
        eleve.setNom(nom);
        eleve.setPrenom(prenom);
        eleve.setEmail(email);
        eleve.setMotDepasse(motDePasse);
        eleve.setNumeroEtudiant(numeroEtudiant);
        eleve.setDateNaissance(new Date());
        eleve.setClasse(classe);

        Eleve eleveSauvegarde = eleveRepository.save(eleve);
        return convertirEnDTO(eleveSauvegarde);
    }

    /**
     * Méthode de conversion Entité -> DTO
     */
    public EleveDTO convertirEnDTO(Eleve eleve) {
        EleveDTO dto = new EleveDTO();
        dto.setId(eleve.getId());
        dto.setNom(eleve.getNom());
        dto.setPrenom(eleve.getPrenom());
        dto.setEmail(eleve.getEmail());
        dto.setNumeroEtudiant(eleve.getNumeroEtudiant());

        if (eleve.getClasse() != null) {
            Classe classe = eleve.getClasse();
            ClasseDTO classeDTO = new ClasseDTO();
            classeDTO.setId(classe.getId());
            classeDTO.setNom(classe.getNom());
            classeDTO.setNiveau(classe.getNiveau());
            classeDTO.setAnneeScolaire(classe.getAnneeScolaire());
            dto.setClasse(classeDTO);
        }
        return dto;
    }
}
