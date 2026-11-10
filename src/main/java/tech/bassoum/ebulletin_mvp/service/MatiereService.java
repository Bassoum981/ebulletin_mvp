package tech.bassoum.ebulletin_mvp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bassoum.ebulletin_mvp.dto.MatiereDTO;
import tech.bassoum.ebulletin_mvp.model.Matiere;
import tech.bassoum.ebulletin_mvp.repository.MatiereRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatiereService {

    private final MatiereRepository matiereRepository;

    public MatiereService(MatiereRepository matiereRepository) {
        this.matiereRepository = matiereRepository;
    }

    public List<MatiereDTO> listerMatieres() {
        return matiereRepository.findAll().stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MatiereDTO creerMatiere(String nom, float coefficient) {
        Matiere matiere = new Matiere();
        matiere.setNom(nom);
        matiere.setCoefficient(coefficient);

        Matiere matiereSauvegardee = matiereRepository.save(matiere);
        return convertirEnDTO(matiereSauvegardee);
    }

    public MatiereDTO convertirEnDTO(Matiere matiere) {
        MatiereDTO dto = new MatiereDTO();
        dto.setId(matiere.getId());
        dto.setNom(matiere.getNom());
        dto.setCoefficient(matiere.getCoefficient());
        return dto;
    }
}
