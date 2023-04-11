package com.cake.ordering.repository;

import com.cake.ordering.domain.Flavor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Flavor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlavorRepository extends JpaRepository<Flavor, Long> {}
