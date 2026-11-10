package tech.bassoum.ebulletin_mvp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bassoum.ebulletin_mvp.model.Classe;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {
}
