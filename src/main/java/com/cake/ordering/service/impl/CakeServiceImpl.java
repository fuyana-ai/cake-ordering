package com.cake.ordering.service.impl;

import com.cake.ordering.domain.Cake;
import com.cake.ordering.repository.CakeRepository;
import com.cake.ordering.service.CakeService;
import com.cake.ordering.service.dto.CakeDTO;
import com.cake.ordering.service.mapper.CakeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cake}.
 */
@Service
@Transactional
public class CakeServiceImpl implements CakeService {

    private final Logger log = LoggerFactory.getLogger(CakeServiceImpl.class);

    private final CakeRepository cakeRepository;

    private final CakeMapper cakeMapper;

    public CakeServiceImpl(CakeRepository cakeRepository, CakeMapper cakeMapper) {
        this.cakeRepository = cakeRepository;
        this.cakeMapper = cakeMapper;
    }

    @Override
    public CakeDTO save(CakeDTO cakeDTO) {
        log.debug("Request to save Cake : {}", cakeDTO);
        Cake cake = cakeMapper.toEntity(cakeDTO);
        cake = cakeRepository.save(cake);
        return cakeMapper.toDto(cake);
    }

    @Override
    public CakeDTO update(CakeDTO cakeDTO) {
        log.debug("Request to update Cake : {}", cakeDTO);
        Cake cake = cakeMapper.toEntity(cakeDTO);
        cake = cakeRepository.save(cake);
        return cakeMapper.toDto(cake);
    }

    @Override
    public Optional<CakeDTO> partialUpdate(CakeDTO cakeDTO) {
        log.debug("Request to partially update Cake : {}", cakeDTO);

        return cakeRepository
            .findById(cakeDTO.getId())
            .map(existingCake -> {
                cakeMapper.partialUpdate(existingCake, cakeDTO);

                return existingCake;
            })
            .map(cakeRepository::save)
            .map(cakeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CakeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cakes");
        return cakeRepository.findAll(pageable).map(cakeMapper::toDto);
    }

    public Page<CakeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cakeRepository.findAllWithEagerRelationships(pageable).map(cakeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CakeDTO> findOne(Long id) {
        log.debug("Request to get Cake : {}", id);
        return cakeRepository.findOneWithEagerRelationships(id).map(cakeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cake : {}", id);
        cakeRepository.deleteById(id);
    }
}
