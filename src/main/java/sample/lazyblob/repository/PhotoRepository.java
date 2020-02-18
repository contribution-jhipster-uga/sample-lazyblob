package sample.lazyblob.repository;
import sample.lazyblob.domain.Photo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sample.lazyblob.domain.PhotoLite;

import java.util.Optional;


/**
 * Spring Data  repository for the Photo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {

    /*@Query(value = "Select * from Photo p where p.id = ?1", nativeQuery = true)
    Optional<Photo> findById(Long id);*/
}

