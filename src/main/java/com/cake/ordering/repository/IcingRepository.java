package com.cake.ordering.repository;

import com.cake.ordering.domain.Icing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Icing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IcingRepository extends JpaRepository<Icing, Long> {}
