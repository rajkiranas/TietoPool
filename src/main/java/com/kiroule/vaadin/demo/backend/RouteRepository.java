package com.kiroule.vaadin.demo.backend;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.data.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {

	@Override
	@EntityGraph(value = "Route.getRouteData", type = EntityGraphType.LOAD)
	Route findOne(Long id);

//	@Override
//	@EntityGraph(value = "Route.getAllRoutes", type = EntityGraphType.LOAD)
//	Page<Route> findAll(Pageable pageable);
}
