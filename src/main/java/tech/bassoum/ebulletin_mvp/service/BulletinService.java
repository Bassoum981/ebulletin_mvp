package tech.bassoum.ebulletin_mvp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bassoum.ebulletin_mvp.dto.*;
import tech.bassoum.ebulletin_mvp.model.*;
import tech.bassoum.ebulletin_mvp.repository.*;

import java.util.Date;

@Service
public class BulletinService {

    private final EleveRepository eleveRepository;
    private final NoteRepository noteRepository;
    private final BulletinRepository bulletinRepository;
    private final MatiereRepository matiereRepository;

    // Injection de dépendances via le constructeur
    public BulletinService(EleveRepository eleveRepository,
                           NoteRepository noteRepository,
                           BulletinRepository bulletinRepository,
                           MatiereRepository matiereRepository) {
        this.eleveRepository = eleveRepository;
        this.noteRepository = noteRepository;
        this.bulletinRepository = bulletinRepository;
        this.matiereRepository = matiereRepository;
    }

    /**
     * Saisir une note pour un élève et recalculer automatiquement son bulletin.
     */
    @Transactional
    public Note ajouterNoteEtMettreAJourBulletin(Long eleveId, Long matiereId, Float valeur, String type, String trimestre, String anneeScolaire) {
        // 1. Récupérer l'élève et la matière
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new RuntimeException("Élève introuvable avec l'ID: " + eleveId));

        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new RuntimeException("Matière introuvable avec l'ID: " + matiereId));

        // 2. Chercher ou créer le bulletin correspondant au trimestre
        Bulletin bulletin = bulletinRepository.findAll().stream()
                .filter(b -> b.getEleve().getId().equals(eleveId) && b.getTrimestre().equals(trimestre))
                .findFirst()
                .orElseGet(() -> {
                    Bulletin nouveauBulletin = new Bulletin();
                    nouveauBulletin.setEleve(eleve);
                    nouveauBulletin.setTrimestre(trimestre);
                    nouveauBulletin.setAnneeScolaire(anneeScolaire);
                    return bulletinRepository.save(nouveauBulletin);
                });

        // 3. Créer et enregistrer la nouvelle note
        Note note = new Note();
        note.setValeur(valeur);
        note.setType(type);
        note.setDate(new Date());
        note.setEleve(eleve);
        note.setMatiere(matiere);
        note.setBulletin(bulletin);

        Note noteSauvegardee = noteRepository.save(note);

        // 4. Ajouter la note au bulletin et recalculer la moyenne générale
        bulletin.getNotes().add(noteSauvegardee);
        bulletin.calculerMoyenneGenerale();

        // 5. Mettre à jour l'appréciation en fonction de la moyenne
        float moyenne = bulletin.getMoyenneGenerale();
        if (moyenne >= 16) {
            bulletin.setAppreciation("Excellent travail !");
        } else if (moyenne >= 14) {
            bulletin.setAppreciation("Très bien, continuez.");
        } else if (moyenne >= 10) {
            bulletin.setAppreciation("Passable.");
        } else {
            bulletin.setAppreciation("Insuffisant, des efforts sont requis.");
        }

        bulletinRepository.save(bulletin);

        return noteSauvegardee;
    }

    /**
     * Nouvelle méthode qui appelle la logique métier et retourne un DTO propre (sans boucle infinie)
     */
    @Transactional
    public NoteResponseDTO ajouterNoteEtMettreAJourBulletinDTO(Long eleveId, Long matiereId, Float valeur, String type, String trimestre, String anneeScolaire) {
        Note noteSauvegardee = ajouterNoteEtMettreAJourBulletin(eleveId, matiereId, valeur, type, trimestre, anneeScolaire);
        return convertirEnNoteDTO(noteSauvegardee);
    }

    /**
     * Méthode de mapping (Transformation Entité -> DTO)
     */
    private NoteResponseDTO convertirEnNoteDTO(Note note) {
        NoteResponseDTO dto = new NoteResponseDTO();
        dto.setId(note.getId());
        dto.setValeur(note.getValeur());
        dto.setType(note.getType());
        dto.setDate(note.getDate());

        // Infos matière
        if (note.getMatiere() != null) {
            dto.setMatiereNom(note.getMatiere().getNom());
            dto.setMatiereCoefficient(note.getMatiere().getCoefficient());
        }

        // Infos bulletin
        if (note.getBulletin() != null) {
            dto.setMoyenneGeneraleBulletin(note.getBulletin().getMoyenneGenerale());
            dto.setAppreciationBulletin(note.getBulletin().getAppreciation());
        }

        // Infos élève
        if (note.getEleve() != null) {
            Eleve eleve = note.getEleve();
            EleveDTO eleveDTO = new EleveDTO();
            eleveDTO.setId(eleve.getId());
            eleveDTO.setNom(eleve.getNom());
            eleveDTO.setPrenom(eleve.getPrenom());
            eleveDTO.setEmail(eleve.getEmail());
            eleveDTO.setNumeroEtudiant(eleve.getNumeroEtudiant());

            // Infos classe de l'élève
            if (eleve.getClasse() != null) {
                Classe classe = eleve.getClasse();
                ClasseDTO classeDTO = new ClasseDTO();
                classeDTO.setId(classe.getId());
                classeDTO.setNom(classe.getNom());
                classeDTO.setNiveau(classe.getNiveau());
                classeDTO.setAnneeScolaire(classe.getAnneeScolaire());
                eleveDTO.setClasse(classeDTO);
            }

            dto.setEleve(eleveDTO);
        }

        return dto;
    }
}
