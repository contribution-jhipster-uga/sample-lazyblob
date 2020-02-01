package sample.lazyblob.web.rest;

import sample.lazyblob.service.RateService;
import sample.lazyblob.web.rest.errors.BadRequestAlertException;
import sample.lazyblob.service.dto.RateDTO;
import sample.lazyblob.service.dto.RateCriteria;
import sample.lazyblob.service.RateQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link sample.lazyblob.domain.Rate}.
 */
@RestController
@RequestMapping("/api")
public class RateResource {

    private final Logger log = LoggerFactory.getLogger(RateResource.class);

    private static final String ENTITY_NAME = "rate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RateService rateService;

    private final RateQueryService rateQueryService;

    public RateResource(RateService rateService, RateQueryService rateQueryService) {
        this.rateService = rateService;
        this.rateQueryService = rateQueryService;
    }

    /**
     * {@code POST  /rates} : Create a new rate.
     *
     * @param rateDTO the rateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rateDTO, or with status {@code 400 (Bad Request)} if the rate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rates")
    public ResponseEntity<RateDTO> createRate(@Valid @RequestBody RateDTO rateDTO) throws URISyntaxException {
        log.debug("REST request to save Rate : {}", rateDTO);
        if (rateDTO.getId() != null) {
            throw new BadRequestAlertException("A new rate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RateDTO result = rateService.save(rateDTO);
        return ResponseEntity.created(new URI("/api/rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rates} : Updates an existing rate.
     *
     * @param rateDTO the rateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rateDTO,
     * or with status {@code 400 (Bad Request)} if the rateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rates")
    public ResponseEntity<RateDTO> updateRate(@Valid @RequestBody RateDTO rateDTO) throws URISyntaxException {
        log.debug("REST request to update Rate : {}", rateDTO);
        if (rateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RateDTO result = rateService.save(rateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rates} : get all the rates.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rates in body.
     */
    @GetMapping("/rates")
    public ResponseEntity<List<RateDTO>> getAllRates(RateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Rates by criteria: {}", criteria);
        Page<RateDTO> page = rateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /rates/count} : count all the rates.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/rates/count")
    public ResponseEntity<Long> countRates(RateCriteria criteria) {
        log.debug("REST request to count Rates by criteria: {}", criteria);
        return ResponseEntity.ok().body(rateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rates/:id} : get the "id" rate.
     *
     * @param id the id of the rateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rates/{id}")
    public ResponseEntity<RateDTO> getRate(@PathVariable Long id) {
        log.debug("REST request to get Rate : {}", id);
        Optional<RateDTO> rateDTO = rateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rateDTO);
    }

    /**
     * {@code DELETE  /rates/:id} : delete the "id" rate.
     *
     * @param id the id of the rateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rates/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        log.debug("REST request to delete Rate : {}", id);
        rateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
