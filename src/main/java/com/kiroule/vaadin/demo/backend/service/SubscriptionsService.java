package com.kiroule.vaadin.demo.backend.service;

import com.kiroule.vaadin.demo.backend.OrderRepository;
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
import com.kiroule.vaadin.demo.backend.SubscriptionsRepository;
import com.kiroule.vaadin.demo.backend.data.DashboardData;
import com.kiroule.vaadin.demo.backend.data.DeliveryStats;
import com.kiroule.vaadin.demo.backend.data.MySubscriptionsBean;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.data.entity.Product;
import com.kiroule.vaadin.demo.backend.data.entity.Route;
import com.kiroule.vaadin.demo.backend.data.entity.Subscriptions;
import com.kiroule.vaadin.demo.backend.data.entity.User;

@Service
public class SubscriptionsService {

        @Autowired
	private SubscriptionsRepository subscriptionsRepository;
        
        @Autowired
	private OrderRepository orderRepository;

//	private static Set<OrderState> notAvailableStates;
//
//	static {
//		notAvailableStates = new HashSet<>(Arrays.asList(OrderState.values()));
//		notAvailableStates.remove(OrderState.DELIVERED);
//		notAvailableStates.remove(OrderState.READY);
//		notAvailableStates.remove(OrderState.CANCELLED);
//	}

	@Autowired
	public SubscriptionsService(SubscriptionsRepository subscriptionsRepository) {
		this.subscriptionsRepository = subscriptionsRepository;
	}

	public Subscriptions findSubscription(Long id) {
		return subscriptionsRepository.findOne(id);
	}
        
        public List<Subscriptions> getAllSubscriptions() {
		return subscriptionsRepository.findAll();
	}
        
        public void saveSubscriptions(Subscriptions s)
        {
            subscriptionsRepository.save(s);
        }
        
        public List<Subscriptions> findByListingId(Long listingId)
        {
            return subscriptionsRepository.findByListingId(listingId);
        }
        
        public List<Subscriptions> findByEmail(String email)
        {
            return subscriptionsRepository.findByEmailContainingIgnoreCase(email);
        }
        
        public List<MySubscriptionsBean> findMySubscriptions(String email)
        {
            List<MySubscriptionsBean> mySubBeanList = new ArrayList<MySubscriptionsBean>();
            MySubscriptionsBean b=null;
            List<Subscriptions> subList = subscriptionsRepository.findByEmailContainingIgnoreCase(email);
            //System.out.println("%%%%%%%%subList="+subList.size());
            for (Subscriptions s : subList) {
                Order o = orderRepository.findOne(s.getListingId());
              //  System.out.println("***** o = "+o);
                b=new MySubscriptionsBean();
                
                b.setChargeable(o.getChargeable());
                b.setEndPoint(o.getEndPoint());
                b.setFromDate(o.getValidFrom().toLocalDate());
                b.setInactiveEndDt(o.getInactiveEndDt());
                b.setInactiveStDt(o.getInactiveStDt());
                b.setName(o.getName());
                b.setPhone(o.getPhone());
                b.setStartPoint(o.getStartPoint());
                b.setStartTime(o.getValidFrom().toLocalTime());
                b.setToDate(o.getValidTo());
                b.setVehicleBrand(o.getVehicleBrand());
                b.setVehicleNumber(o.getVehicleNumber());
                b.setListingId(o.getId());
                b.setStatus(s.getStatus());
                b.setSubscriptionId(s.getId());
                mySubBeanList.add(b);                
            }
            
            return mySubBeanList;
        }
}
