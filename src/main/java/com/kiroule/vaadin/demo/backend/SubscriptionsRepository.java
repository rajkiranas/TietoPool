package com.kiroule.vaadin.demo.backend;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.data.entity.Route;
import com.kiroule.vaadin.demo.backend.data.entity.Subscriptions;
import java.util.List;

public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Long> {

	@Override
	@EntityGraph(value = "Subscriptions.getSubscriptionsData", type = EntityGraphType.LOAD)
	Subscriptions findOne(Long id);

	@Override
	//@EntityGraph(value = "Route.getAllRoutes", type = EntityGraphType.LOAD)
	List<Subscriptions> findAll();
}
