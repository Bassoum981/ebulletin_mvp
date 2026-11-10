package tech.bassoum.ebulletin_mvp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
}
