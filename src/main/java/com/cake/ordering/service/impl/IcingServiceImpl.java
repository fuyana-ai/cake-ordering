package com.cake.ordering.service.impl;

import com.cake.ordering.domain.Icing;
import com.cake.ordering.repository.IcingRepository;
import com.cake.ordering.service.IcingService;
import com.cake.ordering.service.dto.IcingDTO;
import com.cake.ordering.service.mapper.IcingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Icing}.
 */
@Service
@Transactional
public class IcingServiceImpl implements IcingService {

    private final Logger log = LoggerFactory.getLogger(IcingServiceImpl.class);

    private final IcingRepository icingRepository;

    private final IcingMapper icingMapper;

    public IcingServiceImpl(IcingRepository icingRepository, IcingMapper icingMapper) {
        this.icingRepository = icingRepository;
        this.icingMapper = icingMapper;
    }

    @Override
    public IcingDTO save(IcingDTO icingDTO) {
        log.debug("Request to save Icing : {}", icingDTO);
        Icing icing = icingMapper.toEntity(icingDTO);
        icing = icingRepository.save(icing);
        return icingMapper.toDto(icing);
    }

    @Override
    public IcingDTO update(IcingDTO icingDTO) {
        log.debug("Request to update Icing : {}", icingDTO);
        Icing icing = icingMapper.toEntity(icingDTO);
        icing = icingRepository.save(icing);
        return icingMapper.toDto(icing);
    }

    @Override
    public Optional<IcingDTO> partialUpdate(IcingDTO icingDTO) {
        log.debug("Request to partially update Icing : {}", icingDTO);

        return icingRepository
            .findById(icingDTO.getId())
            .map(existingIcing -> {
                icingMapper.partialUpdate(existingIcing, icingDTO);

                return existingIcing;
            })
            .map(icingRepository::save)
            .map(icingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IcingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Icings");
        return icingRepository.findAll(pageable).map(icingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IcingDTO> findOne(Long id) {
        log.debug("Request to get Icing : {}", id);
        return icingRepository.findById(id).map(icingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Icing : {}", id);
        icingRepository.deleteById(id);
    }
}
