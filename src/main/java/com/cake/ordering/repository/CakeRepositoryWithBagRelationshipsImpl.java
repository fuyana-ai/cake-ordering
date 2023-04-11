package com.cake.ordering.repository;

import com.cake.ordering.domain.Cake;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CakeRepositoryWithBagRelationshipsImpl implements CakeRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Cake> fetchBagRelationships(Optional<Cake> cake) {
        return cake.map(this::fetchFlavors).map(this::fetchColors);
    }

    @Override
    public Page<Cake> fetchBagRelationships(Page<Cake> cakes) {
        return new PageImpl<>(fetchBagRelationships(cakes.getContent()), cakes.getPageable(), cakes.getTotalElements());
    }

    @Override
    public List<Cake> fetchBagRelationships(List<Cake> cakes) {
        return Optional.of(cakes).map(this::fetchFlavors).map(this::fetchColors).orElse(Collections.emptyList());
    }

    Cake fetchFlavors(Cake result) {
        return entityManager
            .createQuery("select cake from Cake cake left join fetch cake.flavors where cake is :cake", Cake.class)
            .setParameter("cake", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Cake> fetchFlavors(List<Cake> cakes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, cakes.size()).forEach(index -> order.put(cakes.get(index).getId(), index));
        List<Cake> result = entityManager
            .createQuery("select distinct cake from Cake cake left join fetch cake.flavors where cake in :cakes", Cake.class)
            .setParameter("cakes", cakes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Cake fetchColors(Cake result) {
        return entityManager
            .createQuery("select cake from Cake cake left join fetch cake.colors where cake is :cake", Cake.class)
            .setParameter("cake", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Cake> fetchColors(List<Cake> cakes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, cakes.size()).forEach(index -> order.put(cakes.get(index).getId(), index));
        List<Cake> result = entityManager
            .createQuery("select distinct cake from Cake cake left join fetch cake.colors where cake in :cakes", Cake.class)
            .setParameter("cakes", cakes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
