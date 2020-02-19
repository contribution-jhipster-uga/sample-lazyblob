package sample.lazyblob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sample.lazyblob.domain.PhotoLite;


@Repository
public interface PhotoLiteRepository extends JpaRepository<PhotoLite, Long>, JpaSpecificationExecutor<PhotoLite> {

}

