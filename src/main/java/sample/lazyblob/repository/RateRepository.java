package sample.lazyblob.repository;
import sample.lazyblob.domain.Rate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Rate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RateRepository extends JpaRepository<Rate, Long>, JpaSpecificationExecutor<Rate> {

    @Query("select rate from Rate rate where rate.from.login = ?#{principal.username}")
    List<Rate> findByFromIsCurrentUser();

}
