package sample.lazyblob.service.impl;

import sample.lazyblob.service.RateService;
import sample.lazyblob.domain.Rate;
import sample.lazyblob.repository.RateRepository;
import sample.lazyblob.service.dto.RateDTO;
import sample.lazyblob.service.mapper.RateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Rate}.
 */
@Service
@Transactional
public class RateServiceImpl implements RateService {

    private final Logger log = LoggerFactory.getLogger(RateServiceImpl.class);

    private final RateRepository rateRepository;

    private final RateMapper rateMapper;

    public RateServiceImpl(RateRepository rateRepository, RateMapper rateMapper) {
        this.rateRepository = rateRepository;
        this.rateMapper = rateMapper;
    }

    /**
     * Save a rate.
     *
     * @param rateDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RateDTO save(RateDTO rateDTO) {
        log.debug("Request to save Rate : {}", rateDTO);
        Rate rate = rateMapper.toEntity(rateDTO);
        rate = rateRepository.save(rate);
        return rateMapper.toDto(rate);
    }

    /**
     * Get all the rates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rates");
        return rateRepository.findAll(pageable)
            .map(rateMapper::toDto);
    }


    /**
     * Get one rate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RateDTO> findOne(Long id) {
        log.debug("Request to get Rate : {}", id);
        return rateRepository.findById(id)
            .map(rateMapper::toDto);
    }

    /**
     * Delete the rate by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rate : {}", id);
        rateRepository.deleteById(id);
    }
}
