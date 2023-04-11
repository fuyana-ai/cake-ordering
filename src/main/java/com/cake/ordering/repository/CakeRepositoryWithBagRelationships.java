package com.cake.ordering.repository;

import com.cake.ordering.domain.Cake;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CakeRepositoryWithBagRelationships {
    Optional<Cake> fetchBagRelationships(Optional<Cake> cake);

    List<Cake> fetchBagRelationships(List<Cake> cakes);

    Page<Cake> fetchBagRelationships(Page<Cake> cakes);
}
