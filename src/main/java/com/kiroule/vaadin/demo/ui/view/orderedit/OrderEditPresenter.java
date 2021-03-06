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
import com.kiroule.vaadin.demo.backend.data.entity.Subscriptions;
import com.kiroule.vaadin.demo.backend.data.entity.User;
import com.kiroule.vaadin.demo.backend.service.OrderService;
import com.kiroule.vaadin.demo.backend.service.PickupLocationService;
import com.kiroule.vaadin.demo.backend.service.RouteService;
import com.kiroule.vaadin.demo.backend.service.SubscriptionsService;
import com.kiroule.vaadin.demo.backend.service.UserService;
import com.kiroule.vaadin.demo.ui.navigation.NavigationManager;
import com.kiroule.vaadin.demo.ui.util.GoogleMapsUtil;
import com.kiroule.vaadin.demo.ui.util.SendMailUtil;
import com.kiroule.vaadin.demo.ui.util.StyleUtil;
import com.kiroule.vaadin.demo.ui.view.orderedit.OrderEditView.Mode;
import com.kiroule.vaadin.demo.ui.view.storefront.StorefrontView;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        private SubscriptionsService subscriptionsService;

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
		refresh(view.getOrder(null).getId());
	}

	void init(OrderEditView view) {
		this.view = view;
	}

	/**
	 * Called when the user enters the view.
	 */
	public void enterView(Long id) {
		Order order;
                getRoutesInformation();
                addPlacesMapToTheView();
                        
		if (id == null) {
			// New
			order = new Order();//			
                        view.setMode(Mode.CREATE);
                        
		} else 
                {
                    User user = SecurityUtils.getCurrentUser(userService);
			order = orderService.findOrder(id);
                        
                        if(user.getEmail().equalsIgnoreCase(order.getEmail()))
                        {
                            //admin
                            view.setMode(Mode.VIEW_EDIT);
                            refreshViewAdmin(order);
                        }
                        else
                        {
                            //other users for suscribing
                            view.setMode(Mode.VIEW);                            
                            refreshView(order);
                        }
//			if (order == null) {
//				view.showNotFound();
//				return;
//			}                        
		}
	}

	private void updateTotalSum() {
//		int sum = view.getOrder().getItems().stream().filter(item -> item.getProduct() != null)
//				.collect(Collectors.summingInt(item -> item.getProduct().getPrice() * item.getQuantity()));
//		view.setSum(sum);
	}

	public void editBackCancelPressed() {
		navigationManager.navigateTo(StorefrontView.class);
	}

	public void okPressed(ClickEvent e) {
            System.out.println("*****####");
		if (view.getMode() == Mode.VIEW) {
			// Set next state
			Order order = view.getOrderBeingSubscribed();
			saveSubscription(order,e.getButton().getCaption());                        
                        new Notification("Subscription request successfully sent to the "+order.getName(),"", Notification.Type.WARNING_MESSAGE, true).show(Page.getCurrent());//                      
                        SendMailUtil.send(order.getEmail());
                        navigationManager.navigateTo(StorefrontView.class);
		
                } else if (view.getMode() == Mode.CREATE) {
                                    
			Order order = saveOrder(null);
			if (order != null) {
				// Navigate to edit view so URL is updated correctly
                                new Notification("Carpool successfully created","", Notification.Type.WARNING_MESSAGE, true).show(Page.getCurrent());//                      
				navigationManager.navigateTo(StorefrontView.class);                                
			}
		} else if (view.getMode() == Mode.VIEW_EDIT) {
			Order order = saveOrder(view.getOrderBeingSubscribed());
			if (order != null) {
				// Navigate to edit view so URL is updated correctly
                                new Notification("Carpool successfully updated","", Notification.Type.WARNING_MESSAGE, true).show(Page.getCurrent());//                      
				navigationManager.navigateTo(StorefrontView.class);                                
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
                view.modifyButtonsForView();
                view.lockTheFormFields();
		updateTotalSum();
//		if (order.getId() == null) {
//			view.setMode(Mode.VIEW_EDIT);
//		} else {
//			view.setMode(Mode.VIEW);
//		}
	}
        
        private void refreshViewAdmin(Order order) {
		view.setOrder(order);
                view.modifyButtonsForAdmin();
                addSubscriptionGrid(order);
                
//		if (order.getId() == null) {
//			view.setMode(Mode.VIEW_EDIT);
//		} else {
//			view.setMode(Mode.VIEW);
//		}
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

	private Order saveOrder(Order order) {
		try {
			//filterEmptyProducts();
			order = view.getOrder(order);
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
        mapLayout.setStyleName("storefront");
        
        mapLayout.addComponent(mapLabel);
        view.formAndMapContainer.addComponent(mapLayout);
    }
    
     private void getRoutesInformation() {
        routes=routeService.getAllRoutes();
         System.out.println("*****routes="+routes);
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

    public void inactiveClicked(ValueChangeEvent<Boolean> e) {
        if(e.getValue())
        {
            view.inactiveStDate.setEnabled(true);
            view.inactiveEndDate.setEnabled(true);
        }
        else
        {
            view.inactiveStDate.setEnabled(false);
            view.inactiveEndDate.setEnabled(false);
        }
    }

    private void saveSubscription(Order order, String buttonCaption) {
        short noOfSeats=1;
        User user = SecurityUtils.getCurrentUser(userService);
        Subscriptions s = new Subscriptions();
        s.setEmail(user.getEmail());
        s.setName(user.getName());
        s.setPhone(user.getPhone());
        s.setStatus(String.valueOf(SubscriptionStatus.APPLIED));
        s.setSDate(LocalDate.now());
        s.setNoSeats(noOfSeats);
        s.setListingId(order.getId());
        if(buttonCaption.equalsIgnoreCase("get ride"))
        {
            s.setFromDate(LocalDate.now());
            s.setToDate(LocalDate.now());
        }
        else
        {
            s.setFromDate(order.getValidFrom().toLocalDate());
            s.setToDate(order.getValidTo());
        }
        
        subscriptionsService.saveSubscriptions(s);
    }

    public void getRide(ClickEvent e) {
        
			saveSubscription(view.getOrderBeingSubscribed(),e.getButton().getCaption());
                        //Notification.show("Subscription request successfully sent to the "+order.getName(),"",Notification.Type.HUMANIZED_MESSAGE);
                        new Notification("Subscription request successfully sent to the "+view.getOrderBeingSubscribed().getName(),"", Notification.Type.WARNING_MESSAGE, true).show(Page.getCurrent());
                        navigationManager.navigateTo(StorefrontView.class);
    }

    private void addSubscriptionGrid(Order order) {
        
        List<Subscriptions> subscriptionsList=order.getSubscriptionsList();        
        if(subscriptionsList==null || subscriptionsList.size()==0)
        {
            mapLayout.addComponent(new Label("<h2><b>There are no subscriptions for this pool yet</b></h2>",ContentMode.HTML));
        }
        else
        {
            Grid<Subscriptions> grid = new Grid<Subscriptions>("Subscriptions");
            //grid.setSizeFull();
            grid.setWidth("100%");
            //grid.addStyleName("orders-grid");
            grid.addStyleName(StyleUtil.getRowStyle(subscriptionsList.get(0)));
            grid.setItems(subscriptionsList);
//            grid.addColumn("name");
//            grid.addColumn("phone");
//            grid.addColumn("status");
            grid.addColumn(Subscriptions::getName).setCaption("Subscriber");
            grid.addColumn(Subscriptions::getPhone).setCaption("Phone");
            grid.addColumn(Subscriptions::getStatus).setCaption("Status");
//            Button approveButton = new Button("Approve");
//            approveButton.addClickListener(e -> approveSubscription(e));
            Column approveColumn = grid.addColumn(subscriptions -> "Approve",
            new ButtonRenderer(clickEvent -> {
                processSubscription(clickEvent,"Approve",grid,order);
          }));
            approveColumn.setCaption("Approve");
            
            Column rejectColumn = grid.addColumn(subscriptions -> "Reject",
            new ButtonRenderer(clickEvent -> {
                processSubscription(clickEvent,"Reject",grid,order);
          }));
            rejectColumn.setCaption("Reject");

            
            mapLayout.addComponent(grid);
        }
    }

    private void processSubscription(ClickableRenderer.RendererClickEvent clickEvent, String action,Grid<Subscriptions> grid,Order order) 
    {   System.out.println("###action="+action);
        System.out.println("*******"+((Subscriptions)clickEvent.getItem()).getName());
        Subscriptions s =((Subscriptions)clickEvent.getItem());
        if(action.equalsIgnoreCase("Approve"))
        {
            s.setStatus(String.valueOf(SubscriptionStatus.APPROVED));
            
        }
        else if(action.equalsIgnoreCase("Reject"))
        {
            s.setStatus(String.valueOf(SubscriptionStatus.REJECTED));
        }
        subscriptionsService.saveSubscriptions(s);
        
        order = orderService.findOrder(order.getId());
        System.out.println("######order="+order);
        int nos=order.getNoSeats()-1;
        System.out.println("nos="+nos);
        order.setNoSeats(nos);
        order=orderService.saveOrder(order, null);
        //grid.getDataProvider().refreshAll();
        grid.setItems(order.getSubscriptionsList());
        SendMailUtil.send(s.getEmail());
    }
    
    
    
    public enum SubscriptionStatus {
		APPLIED,APPROVED,REJECTED,CANCELLED,EXPIRED
	}
}
