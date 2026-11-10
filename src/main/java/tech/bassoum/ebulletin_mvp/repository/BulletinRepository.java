package tech.bassoum.ebulletin_mvp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bassoum.ebulletin_mvp.model.Bulletin;


@Repository
public interface BulletinRepository  extends JpaRepository<Bulletin, Long> {
}
