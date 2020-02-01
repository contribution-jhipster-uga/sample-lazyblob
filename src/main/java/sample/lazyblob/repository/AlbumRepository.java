package sample.lazyblob.repository;
import sample.lazyblob.domain.Album;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Album entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlbumRepository extends JpaRepository<Album, Long>, JpaSpecificationExecutor<Album> {

    @Query("select album from Album album where album.ownedBy.login = ?#{principal.username}")
    List<Album> findByOwnedByIsCurrentUser();

}
