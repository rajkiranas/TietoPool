package com.kiroule.vaadin.demo.ui.view.orderedit;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.kiroule.vaadin.demo.app.HasLogger;
import com.kiroule.vaadin.demo.app.security.SecurityUtils;
import com.kiroule.vaadin.demo.backend.data.OrderState;
import com.kiroule.vaadin.demo.backend.data.entity.Customer;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.data.entity.OrderItem;
import com.kiroule.vaadin.demo.backend.data.entity.Route;
import com.kiroule.vaadin.demo.backend.service.OrderService;
import com.kiroule.vaadin.demo.backend.service.PickupLocationService;
import com.kiroule.vaadin.demo.backend.service.RouteService;
import com.kiroule.vaadin.demo.backend.service.UserService;
import com.kiroule.vaadin.demo.ui.navigation.NavigationManager;
import com.kiroule.vaadin.demo.ui.util.GoogleMapsUtil;
import com.kiroule.vaadin.demo.ui.view.orderedit.OrderEditView.Mode;
import com.kiroule.vaadin.demo.ui.view.storefront.StorefrontView;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

@SpringComponent
@ViewScope
public class OrderEditPresenter implements Serializable, HasLogger {

	private OrderEditView view;

	private final OrderService orderService;
	private final UserService userService;

	private final PickupLocationService pickupLocationService;

	private final NavigationManager navigationManager;

	private final ViewEventBus viewEventBus;

	private static final List<OrderState> happyPath = Arrays.asList(OrderState.NEW, OrderState.CONFIRMED,
			OrderState.READY, OrderState.DELIVERED);
        
        @Autowired
        private GoogleMapsUtil mapsUtil;
        
        @Autowired
        private RouteService routeService;    
        private List<Route> routes;
        private VerticalLayout mapLayout;
        private Label mapLabel;

	@Autowired
	public OrderEditPresenter(ViewEventBus viewEventBus, NavigationManager navigationManager, OrderService orderService,
			UserService userService, PickupLocationService pickupLocationService) {
		this.viewEventBus = viewEventBus;
		this.navigationManager = navigationManager;
		this.orderService = orderService;
		this.userService = userService;
		this.pickupLocationService = pickupLocationService;
		viewEventBus.subscribe(this);
	}

	@PreDestroy
	public void destroy() {
		viewEventBus.unsubscribe(this);
	}

	@EventBusListenerMethod
	private void onProductInfoChange(ProductInfoChangeEvent event) {
		updateTotalSum();
		view.onProductInfoChanged();
	}

	@EventBusListenerMethod
	private void onOrderItemDelete(OrderItemDeletedEvent event) {
		removeOrderItem(event.getOrderItem());
		view.onProductInfoChanged();
	}

	@EventBusListenerMethod
	private void onOrderItemUpdate(OrderUpdatedEvent event) {
		refresh(view.getOrder().getId());
	}

	void init(OrderEditView view) {
		this.view = view;
	}

	/**
	 * Called when the user enters the view.
	 */
	public void enterView(Long id) {
		Order order;
		if (id == null) {
			// New
			order = new Order();
//			order.setState(OrderState.NEW);
//			order.setItems(new ArrayList<>());
//			order.setCustomer(new Customer());
//			order.setDueDate(LocalDate.now().plusDays(1));
//			order.setDueTime(LocalTime.of(8, 00));
//			order.setPickupLocation(pickupLocationService.getDefault());
                        view.setMode(Mode.CREATE);
                        getRoutesInformation();
                        addPlacesMapToTheView();
		} else {
			order = orderService.findOrder(id);
			if (order == null) {
				view.showNotFound();
				return;
			}
                        refreshView(order);
		}
	}

	private void updateTotalSum() {
//		int sum = view.getOrder().getItems().stream().filter(item -> item.getProduct() != null)
//				.collect(Collectors.summingInt(item -> item.getProduct().getPrice() * item.getQuantity()));
//		view.setSum(sum);
	}

	public void editBackCancelPressed() {
		if (view.getMode() == Mode.VIEW) {
			// Edit order
			view.setMode(Mode.VIEW_EDIT);
		} else if (view.getMode() == Mode.CREATE) {
			// Back to edit
			view.setMode(Mode.VIEW_EDIT);
		} else if (view.getMode() == Mode.VIEW_EDIT) {
			// Cancel edit
			Long id = view.getOrder().getId();
			if (id == null) {
				navigationManager.navigateTo(StorefrontView.class);
			} else {
				enterView(id);
			}
		}
	}

	public void okPressed() {
            System.out.println("*****####");
		if (view.getMode() == Mode.VIEW) {
			// Set next state
			Order order = view.getOrder();
			//Optional<OrderState> nextState = getNextHappyPathState(order.getState());
                        Optional<OrderState> nextState = null;
			if (!nextState.isPresent()) {
				throw new IllegalStateException(
						"The next state button should never be enabled when the state does not follow the happy path");
			}
			//orderService.changeState(order, nextState.get(), SecurityUtils.getCurrentUser(userService));
			refresh(order.getId());
		} else if (view.getMode() == Mode.CREATE) {
                                    
			Order order = saveOrder();
			if (order != null) {
				// Navigate to edit view so URL is updated correctly
				navigationManager.navigateTo(StorefrontView.class);
                                //updateViewParameter("" + order.getId());
				//enterView(order.getId());
			}
		} else if (view.getMode() == Mode.VIEW_EDIT) {
			Optional<HasValue<?>> firstErrorField = view.validate().findFirst();
			if (firstErrorField.isPresent()) {
				((Focusable) firstErrorField.get()).focus();
				return;
			}
			// New order should still show a confirmation page
			Order order = view.getOrder();
			if (order.getId() == null) {
				filterEmptyProducts();
				view.setMode(Mode.CREATE);
			} else {
				order = saveOrder();
				if (order != null) {
					refresh(order.getId());
				}
			}
		}
                
                   
                
	}

	private void refresh(Long id) {
		Order order = orderService.findOrder(id);
		if (order == null) {
			view.showNotFound();
			return;
		}
		refreshView(order);

	}

	private void refreshView(Order order) {
		view.setOrder(order);
		updateTotalSum();
		if (order.getId() == null) {
			view.setMode(Mode.VIEW_EDIT);
		} else {
			view.setMode(Mode.VIEW);
		}
	}

	private void filterEmptyProducts() {
		LinkedList<OrderItem> emptyRows = new LinkedList<>();
//		view.getOrder().getItems().forEach(orderItem -> {
//			if (orderItem.getProduct() == null) {
//				emptyRows.add(orderItem);
//			}
//		});
		emptyRows.forEach(this::removeOrderItem);
	}

	private Order saveOrder() {
		try {
			//filterEmptyProducts();
			Order order = view.getOrder();
                        System.out.println("order="+order);
			return orderService.saveOrder(order, SecurityUtils.getCurrentUser(userService));
		} catch (ValidationException e) {
			// Should not get here if validation is setup properly
			Notification.show("Please check the contents of the fields: " + e.getMessage(), Type.ERROR_MESSAGE);
			getLogger().error("Validation error during order save", e);
			return null;
		} catch (OptimisticLockingFailureException e) {
			// Somebody else probably edited the data at the same time
			Notification.show("Somebody else might have updated the data. Please refresh and try again.",
					Type.ERROR_MESSAGE);
			getLogger().debug("Optimistic locking error while saving order", e);
			return null;
		} catch (Exception e) {
			// Something went wrong, no idea what
			Notification.show("An unexpected error occurred while saving. Please refresh and try again.",
					Type.ERROR_MESSAGE);
			getLogger().error("Unable to save order", e);
			return null;
		}
	}

	public Optional<OrderState> getNextHappyPathState(OrderState current) {
		final int currentIndex = happyPath.indexOf(current);
		if (currentIndex == -1 || currentIndex == happyPath.size() - 1) {
			return Optional.empty();
		}
		return Optional.of(happyPath.get(currentIndex + 1));
	}

	private void removeOrderItem(OrderItem orderItem) {
		view.removeOrderItem(orderItem);
		updateTotalSum();
	}

    private void createPool() {
        System.out.println("***"+view.email.getValue());
    }

    private void addPlacesMapToTheView() {
        mapLabel = new Label(mapsUtil.getPlaceMap("Pune"), ContentMode.HTML);
        mapLayout = new VerticalLayout();
        mapLayout.setSizeFull();
        mapLayout.setMargin(true);
        mapLayout.addComponent(mapLabel);
        view.formAndMapContainer.addComponent(mapLayout);
    }
    
     private void getRoutesInformation() {
        routes=routeService.getAllRoutes();
        List<String> stringRoutes = new ArrayList<String>();
         for (Route r : routes) {
             stringRoutes.add(r.getId()+":"+r.getSource()+"-to-"+r.getDestination());
         }
         view.route.setItems(stringRoutes);        
    }

    public void routeSelected(ValueChangeEvent e) {
        System.out.println("***"+e.getValue());
//        System.out.println("***"+e.getSource().getValue());
//        System.out.println("***"+e.getComponent());
        String fromTo = String.valueOf(e.getSource().getValue());
        updateMap(fromTo);
    }

    private void updateMap(String fromTo) 
    {
        //2:Manjri to Eon-Kharadi
        fromTo=fromTo.substring(fromTo.indexOf(":")+1);
        //System.out.println("fromTo="+fromTo);
        String[] fromToArray = fromTo.split("-to-");
//        for (String string : fromToArray) {
//            System.out.println("string="+string);
//            
//        }
        
//        mapLabel = new Label(mapsUtil.getPlaceMap("Pune"), ContentMode.HTML);
//        mapLayout = new VerticalLayout();
//        mapLayout.setSizeFull();
//        mapLayout.setMargin(true);
        mapLayout.removeComponent(mapLabel);
        //System.out.println("####"+fromToArray[0]+" "+fromToArray[1]);
          mapLabel = new Label(mapsUtil.getDirectionsMap(fromToArray[0],fromToArray[1]), ContentMode.HTML);
         // mapLabel.
        mapLayout.addComponent(mapLabel);
        //view.formAndMapContainer.addComponent(mapLayout);
    }
}
