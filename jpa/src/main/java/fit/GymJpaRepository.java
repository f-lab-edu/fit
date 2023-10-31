package fit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GymJpaRepository extends JpaRepository<GymEntity, Long> {
}
