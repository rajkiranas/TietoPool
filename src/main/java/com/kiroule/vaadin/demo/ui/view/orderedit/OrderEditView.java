package com.kiroule.vaadin.demo.ui.view.orderedit;

import com.kiroule.vaadin.demo.app.security.SecurityUtils;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueContext;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.kiroule.vaadin.demo.backend.data.OrderState;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.data.entity.OrderItem;
import com.kiroule.vaadin.demo.backend.data.entity.Route;
import com.kiroule.vaadin.demo.backend.data.entity.User;
import com.kiroule.vaadin.demo.backend.service.OrderService;
import com.kiroule.vaadin.demo.backend.service.RouteService;
import com.kiroule.vaadin.demo.backend.service.SubscriptionsService;
import com.kiroule.vaadin.demo.backend.service.UserService;
import com.kiroule.vaadin.demo.ui.components.ConfirmPopup;
import com.kiroule.vaadin.demo.ui.util.DollarPriceConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@SpringView(name = "order")
public class OrderEditView extends OrderEditViewDesign implements View {

    public enum Mode {
		VIEW_EDIT, VIEW, CREATE;
	}

	private final OrderEditPresenter presenter;

	private final DollarPriceConverter priceConverter;

	private BeanValidationBinder<Order> binder;

	private Mode mode;

	private boolean hasChanges;

	private final BeanFactory beanFactory;
        
        @Autowired
        private RouteService routeService;
        
        @Autowired
        private UserService userService;
        
        @Autowired
        private OrderService orderService;
        
        private Order orderBeingSubscribed;
        
        @Autowired
        private SubscriptionsService subscriptionsService;

	@Autowired
	public OrderEditView(OrderEditPresenter presenter, BeanFactory beanFactory, DollarPriceConverter priceConverter) {
		this.presenter = presenter;
		this.beanFactory = beanFactory;
		this.priceConverter = priceConverter;
	}

	@PostConstruct
	public void init() {
		presenter.init(this);

		// We're limiting dueTime to even hours between 07:00 and 17:00
		//dueTime.setItems(IntStream.range(7, 17).mapToObj(h -> LocalTime.of(h, 0)));

		// Binder takes care of binding Vaadin fields defined as Java member
		// fields in this class to properties in the Order bean
		binder = new BeanValidationBinder<>(Order.class);

		// Almost all fields are required, so we don't want to display
		// indicators
		binder.setRequiredConfigurator(null);

		// Bindings are done in the order the fields appear on the screen as we
		// report validation errors for the first invalid field and it is most
		// intuitive for the user that we start from the top if there are
		// multiple errors.
		//binder.bindInstanceFields(this);

		// Must bind sub properties manually until
		// https://github.com/vaadin/framework/issues/9210 is fixed
//		binder.bind(fullName, "customer.fullName");
//		binder.bind(phone, "customer.phoneNumber");
		//binder.bind(details, "customer.details");

		// Track changes manually as we use setBean and nested binders
		//binder.addValueChangeListener(e -> hasChanges = true);

		//addItems.addClickListener(e -> addEmptyOrderItem());
		cancel.addClickListener(e -> presenter.editBackCancelPressed());
		ok.addClickListener(e -> presenter.okPressed(e));
                route.addValueChangeListener(e -> presenter.routeSelected(e));
                inactive.addValueChangeListener(e -> presenter.inactiveClicked(e));
                
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String orderId = event.getParameters();
                System.out.println("*****"+((com.kiroule.vaadin.demo.ui.navigation.NavigationManager)event.getSource()));
                System.out.println("orderIdorderIdorderId="+orderId);
		if (orderId.equalsIgnoreCase("mypool"))  
                {
                    User user = SecurityUtils.getCurrentUser(userService);
			Order order = orderService.findByEmail(user.getEmail());
                        System.out.println("********="+order.getId());
                        presenter.enterView(order.getId());
			                   
		} else if("".equals(orderId))
                {                    
                        presenter.enterView(null);     
                }
                else {
			presenter.enterView(Long.valueOf(orderId));
		}                
	}

	public void setOrder(Order order) {
		//stateLabel.setValue(order.getState().getDisplayName());
		setOrderBeingSubscribed(order);
		fullName.setValue(order.getName());
                email.setValue(order.getEmail());
                phone.setValue(String.valueOf(order.getPhone()));
                
                vehicleBrand.setValue(order.getVehicleBrand());
                vehicleNumber.setValue(order.getVehicleNumber());
                //not handled, only shown currently
                //vehicleType.setValue(4L);
                noSeats.setValue(String.valueOf(order.getNoSeats()));
                
                chargeable.setValue(order.getChargeable());
                startPoint.setValue(order.getStartPoint());                
                endPoint.setValue(order.getEndPoint());
                
                validFrom.setValue(order.getValidFrom());
                validTo.setValue(order.getValidTo());
                
//                LocalTime startTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toLocalTime();
//                LocalDate startDate = LocalDate.now();

                //not required, handled in validFrom
                //order.setStartTime(validFrom.getValue().toLocalTime());
//                order.setEndTime(startTime.plusHours(1));
                
                inactive.setValue(!order.getIsActive());
                
                if(inactive.getValue())
                {
                    inactiveStDate.setValue(order.getInactiveStDt());
                    inactiveEndDate.setValue(order.getInactiveEndDt());
                }
                
                route.setSelectedItem(order.getRoute().getId()+":"+order.getRoute().getSource()+"-to-"+order.getRoute().getDestination());
                		
		//hasChanges = false;
	}
        
        public void modifyButtonsForView()
        {
            ok.setCaption("Subscribe");
                 Button takeRide = new Button("Get ride");
                 takeRide.setIcon(VaadinIcons.ANGLE_DOUBLE_RIGHT);
                 cancelOkBtnLayout.addComponent(takeRide);
                 takeRide.setStyleName("primary icon-align-right");
                 takeRide.addClickListener(e -> presenter.getRide(e));            
        }
        
        public void modifyButtonsForAdmin()
        {
            ok.setCaption("Update");                             
        }

	private void addEmptyOrderItem() {
		OrderItem orderItem = new OrderItem();
		ProductInfo productInfo = createProductInfo(orderItem);
		//productInfoContainer.addComponent(productInfo);
		productInfo.focus();
		//getOrder().getItems().add(orderItem);
	}

	protected void removeOrderItem(OrderItem orderItem) {
		//getOrder().getItems().remove(orderItem);

//		for (Component c : productInfoContainer) {
//			if (c instanceof ProductInfo && ((ProductInfo) c).getItem() == orderItem) {
//				productInfoContainer.removeComponent(c);
//				break;
//			}
//		}
	}

	/**
	 * Create a ProductInfo instance using Spring so that it is injected and can
	 * in turn inject a ProductComboBox and its data provider.
	 *
	 * @param orderItem
	 *            the item to edit
	 *
	 * @return a new product info instance
	 */
	private ProductInfo createProductInfo(OrderItem orderItem) {
		ProductInfo productInfo = beanFactory.getBean(ProductInfo.class);
		productInfo.setItem(orderItem);
		return productInfo;
	}

	protected Order getOrder(Order order) {
		//return binder.getBean();
                if(order==null)
                {
                    order = new Order();
                }
                
                order.setName(fullName.getValue());
                order.setEmail(email.getValue());
                order.setPhone(Long.parseLong(phone.getValue()));
                
                order.setVehicleBrand(vehicleBrand.getValue());
                order.setVehicleNumber(vehicleNumber.getValue());
                order.setVehicleType(4L);
                order.setNoSeats(Integer.parseInt(noSeats.getValue()));
                
                order.setChargeable(chargeable.getValue());
                order.setStartPoint(startPoint.getValue());                
                order.setEndPoint(endPoint.getValue());
                
                order.setValidFrom(validFrom.getValue());
                order.setValidTo(validTo.getValue());
                
//                LocalTime startTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toLocalTime();
//                LocalDate startDate = LocalDate.now();
                order.setStartTime(validFrom.getValue().toLocalTime());
//                order.setEndTime(startTime.plusHours(1));
                
                order.setIsActive(!inactive.getValue());
                
                if(inactive.getValue())
                {
                    order.setInactiveStDt(inactiveStDate.getValue());
                    order.setInactiveEndDt(inactiveEndDate.getValue());
                }
                
                String routeId = route.getValue();
                routeId=routeId.substring(0,routeId.indexOf(":"));
                order.setRoute(routeService.findRoute(Long.parseLong(routeId)));                
                
                return order;
	}

	protected void setSum(int sum) {
		//total.setValue(priceConverter.convertToPresentation(sum, new ValueContext(Locale.US)));
	}

	public void showNotFound() {
		removeAllComponents();
		addComponent(new Label("Order not found"));
	}

	public void setMode(Mode mode) {
		// Allow to style different modes separately
		if (this.mode != null) {
			removeStyleName(this.mode.name().toLowerCase());
		}
		addStyleName(mode.name().toLowerCase());

		this.mode = mode;
		binder.setReadOnly(mode != Mode.VIEW_EDIT);
//		for (Component c : productInfoContainer) {
//			if (c instanceof ProductInfo) {
//				((ProductInfo) c).setReportMode(mode != Mode.EDIT);
//			}
//		}
//		addItems.setVisible(mode == Mode.EDIT);
//		history.setVisible(mode == Mode.REPORT);
		//state.setVisible(mode == Mode.VIEW_EDIT);

		if (mode == Mode.VIEW) {
//			cancel.setCaption("Edit");
//			cancel.setIcon(VaadinIcons.EDIT);
			//Optional<OrderState> nextState = presenter.getNextHappyPathState(getOrder().getState());
//			ok.setCaption("Mark as " + nextState.map(OrderState::getDisplayName).orElse("?"));
//			ok.setVisible(nextState.isPresent());
		} else if (mode == Mode.CREATE) {
			//cancel.setCaption("Back");
			cancel.setIcon(VaadinIcons.ANGLE_LEFT);
			//ok.setCaption("Place order");
			ok.setVisible(true);
		} else if (mode == Mode.VIEW_EDIT) {
			cancel.setCaption("Cancel");
			cancel.setIcon(VaadinIcons.CLOSE);
//			if (getOrder() != null && !getOrder().isNew()) {
//				ok.setCaption("Save");
//			} else {
//				ok.setCaption("Review order");
//			}
//			ok.setVisible(true);
		} else {
			throw new IllegalArgumentException("Unknown mode " + mode);
		}
	}

	public Mode getMode() {
		return mode;
	}

	public Stream<HasValue<?>> validate() {
		Stream<HasValue<?>> errorFields = binder.validate().getFieldValidationErrors().stream()
				.map(BindingValidationStatus::getField);

//		for (Component c : productInfoContainer) {
//			if (c instanceof ProductInfo) {
//				ProductInfo productInfo = (ProductInfo) c;
//				if (!productInfo.isEmpty()) {
//					errorFields = Stream.concat(errorFields, productInfo.validate());
//				}
//			}
//		}
		return errorFields;
	}

	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		if (!containsUnsavedChanges()) {
			event.navigate();
		} else {
			ConfirmPopup confirmPopup = beanFactory.getBean(ConfirmPopup.class);
			confirmPopup.showLeaveViewConfirmDialog(this, event::navigate);
		}
	}

	public void onProductInfoChanged() {
		hasChanges = true;
	}

	public boolean containsUnsavedChanges() {
		return hasChanges;
	}

public void lockTheFormFields() {
                fullName.setEnabled(false);
                email.setEnabled(false);
                phone.setEnabled(false);
                
                vehicleBrand.setEnabled(false);
                vehicleNumber.setEnabled(false);
                //not handled, only shown currently
                //vehicleType.setValue(4L);
                noSeats.setEnabled(false);
                
                chargeable.setEnabled(false);
                startPoint.setEnabled(false);
                endPoint.setEnabled(false);
                
                validFrom.setEnabled(false);
                validTo.setEnabled(false);                
                
                inactive.setEnabled(false);                
                
                    inactiveStDate.setEnabled(false);
                    inactiveEndDate.setEnabled(false);
                route.setEnabled(false);
    }
    
    private void enableFormFields() {
                fullName.setEnabled(true);
                email.setEnabled(true);
                phone.setEnabled(true);
                
                vehicleBrand.setEnabled(true);
                vehicleNumber.setEnabled(true);
                //not handled, only shown currently
                //vehicleType.setValue(4L);
                noSeats.setEnabled(true);
                
                chargeable.setEnabled(true);
                startPoint.setEnabled(true);
                endPoint.setEnabled(true);
                
                validFrom.setEnabled(true);
                validTo.setEnabled(true);                
                
                inactive.setEnabled(true);                
                
                    inactiveStDate.setEnabled(true);
                    inactiveEndDate.setEnabled(true);
                route.setEnabled(true);
    }

    /**
     * @return the orderBeingSubscribed
     */
    public Order getOrderBeingSubscribed() {
        return orderBeingSubscribed;
    }

    /**
     * @param orderBeingSubscribed the orderBeingSubscribed to set
     */
    public void setOrderBeingSubscribed(Order orderBeingSubscribed) {
        this.orderBeingSubscribed = orderBeingSubscribed;
    }
       
}
