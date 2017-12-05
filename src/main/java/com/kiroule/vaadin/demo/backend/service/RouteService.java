package com.kiroule.vaadin.demo.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kiroule.vaadin.demo.backend.RouteRepository;
import com.kiroule.vaadin.demo.backend.data.DashboardData;
import com.kiroule.vaadin.demo.backend.data.DeliveryStats;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.data.entity.Product;
import com.kiroule.vaadin.demo.backend.data.entity.Route;
import com.kiroule.vaadin.demo.backend.data.entity.User;

@Service
public class RouteService {

	private final RouteRepository routeRepository;

//	private static Set<OrderState> notAvailableStates;
//
//	static {
//		notAvailableStates = new HashSet<>(Arrays.asList(OrderState.values()));
//		notAvailableStates.remove(OrderState.DELIVERED);
//		notAvailableStates.remove(OrderState.READY);
//		notAvailableStates.remove(OrderState.CANCELLED);
//	}

	@Autowired
	public RouteService(RouteRepository routeRepository) {
		this.routeRepository = routeRepository;
	}

	public Route findRoute(Long id) {
		return routeRepository.findOne(id);
	}


}
