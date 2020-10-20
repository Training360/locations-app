package locationsapp.repository;

import locationsapp.entities.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationsRepository extends JpaRepository<Location, Long> {

    @Query(value = "select distinct l from Location l left join fetch l.tags order by l.name",
    countQuery = "select count(l) from Location l")
    Page<Location> findAllWithTags(Pageable pageable);

    @Query(value = "select distinct l from Location l left join fetch l.tags where l.id = :id",
            countQuery = "select count(l) from Location l where l.id = :id")
    Optional<Location> findByIdWithTags(@Param("id") long id);

}
