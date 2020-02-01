package sample.lazyblob.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import sample.lazyblob.domain.Rate;
import sample.lazyblob.domain.*; // for static metamodels
import sample.lazyblob.repository.RateRepository;
import sample.lazyblob.service.dto.RateCriteria;
import sample.lazyblob.service.dto.RateDTO;
import sample.lazyblob.service.mapper.RateMapper;

/**
 * Service for executing complex queries for {@link Rate} entities in the database.
 * The main input is a {@link RateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RateDTO} or a {@link Page} of {@link RateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RateQueryService extends QueryService<Rate> {

    private final Logger log = LoggerFactory.getLogger(RateQueryService.class);

    private final RateRepository rateRepository;

    private final RateMapper rateMapper;

    public RateQueryService(RateRepository rateRepository, RateMapper rateMapper) {
        this.rateRepository = rateRepository;
        this.rateMapper = rateMapper;
    }

    /**
     * Return a {@link List} of {@link RateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RateDTO> findByCriteria(RateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Rate> specification = createSpecification(criteria);
        return rateMapper.toDto(rateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RateDTO> findByCriteria(RateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rate> specification = createSpecification(criteria);
        return rateRepository.findAll(specification, page)
            .map(rateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Rate> specification = createSpecification(criteria);
        return rateRepository.count(specification);
    }

    /**
     * Function to convert {@link RateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rate> createSpecification(RateCriteria criteria) {
        Specification<Rate> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Rate_.id));
            }
            if (criteria.getRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRate(), Rate_.rate));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Rate_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Rate_.updatedAt));
            }
            if (criteria.getPhotoId() != null) {
                specification = specification.and(buildSpecification(criteria.getPhotoId(),
                    root -> root.join(Rate_.photo, JoinType.LEFT).get(Photo_.id)));
            }
            if (criteria.getFromId() != null) {
                specification = specification.and(buildSpecification(criteria.getFromId(),
                    root -> root.join(Rate_.from, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
