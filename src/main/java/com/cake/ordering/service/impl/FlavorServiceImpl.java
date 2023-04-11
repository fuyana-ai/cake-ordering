package com.cake.ordering.service.impl;

import com.cake.ordering.domain.Flavor;
import com.cake.ordering.repository.FlavorRepository;
import com.cake.ordering.service.FlavorService;
import com.cake.ordering.service.dto.FlavorDTO;
import com.cake.ordering.service.mapper.FlavorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Flavor}.
 */
@Service
@Transactional
public class FlavorServiceImpl implements FlavorService {

    private final Logger log = LoggerFactory.getLogger(FlavorServiceImpl.class);

    private final FlavorRepository flavorRepository;

    private final FlavorMapper flavorMapper;

    public FlavorServiceImpl(FlavorRepository flavorRepository, FlavorMapper flavorMapper) {
        this.flavorRepository = flavorRepository;
        this.flavorMapper = flavorMapper;
    }

    @Override
    public FlavorDTO save(FlavorDTO flavorDTO) {
        log.debug("Request to save Flavor : {}", flavorDTO);
        Flavor flavor = flavorMapper.toEntity(flavorDTO);
        flavor = flavorRepository.save(flavor);
        return flavorMapper.toDto(flavor);
    }

    @Override
    public FlavorDTO update(FlavorDTO flavorDTO) {
        log.debug("Request to update Flavor : {}", flavorDTO);
        Flavor flavor = flavorMapper.toEntity(flavorDTO);
        flavor = flavorRepository.save(flavor);
        return flavorMapper.toDto(flavor);
    }

    @Override
    public Optional<FlavorDTO> partialUpdate(FlavorDTO flavorDTO) {
        log.debug("Request to partially update Flavor : {}", flavorDTO);

        return flavorRepository
            .findById(flavorDTO.getId())
            .map(existingFlavor -> {
                flavorMapper.partialUpdate(existingFlavor, flavorDTO);

                return existingFlavor;
            })
            .map(flavorRepository::save)
            .map(flavorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlavorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Flavors");
        return flavorRepository.findAll(pageable).map(flavorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FlavorDTO> findOne(Long id) {
        log.debug("Request to get Flavor : {}", id);
        return flavorRepository.findById(id).map(flavorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Flavor : {}", id);
        flavorRepository.deleteById(id);
    }
}
