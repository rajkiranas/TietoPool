package com.kiroule.vaadin.demo.ui.view.subscriptions;

import com.kiroule.vaadin.demo.ui.view.orderedit.*;
import com.kiroule.vaadin.demo.app.security.SecurityUtils;
import com.kiroule.vaadin.demo.backend.data.MySubscriptionsBean;
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
import com.kiroule.vaadin.demo.backend.data.entity.Subscriptions;
import com.kiroule.vaadin.demo.backend.data.entity.User;
import com.kiroule.vaadin.demo.backend.service.OrderService;
import com.kiroule.vaadin.demo.backend.service.RouteService;
import com.kiroule.vaadin.demo.backend.service.SubscriptionsService;
import com.kiroule.vaadin.demo.backend.service.UserService;
import com.kiroule.vaadin.demo.ui.components.ConfirmPopup;
import com.kiroule.vaadin.demo.ui.util.DollarPriceConverter;
import com.kiroule.vaadin.demo.ui.util.SendMailUtil;
import com.kiroule.vaadin.demo.ui.util.StyleUtil;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@SpringView(name = "mysubscriptions")
public class MySubscriptionsView extends MySubscriptions implements View {

            
        @Autowired
        private SubscriptionsService subscriptionsService;
        
        @Autowired
        private UserService userService;
        
        @Autowired
        private OrderService orderService;
        
	
	public MySubscriptionsView() {		
	}

	@PostConstruct
	public void init() {
		
                
	}

	@Override
	public void enter(ViewChangeEvent event) {
            verticalWrapper.setStyleName("storefront");
            
            addSubscriptionGrid();
	}

	

//	@Override
//	public void beforeLeave(ViewBeforeLeaveEvent event) {
//		if (!containsUnsavedChanges()) {
//			event.navigate();
//		} else {
//			ConfirmPopup confirmPopup = beanFactory.getBean(ConfirmPopup.class);
//			confirmPopup.showLeaveViewConfirmDialog(this, event::navigate);
//		}
//	}

    private void addSubscriptionGrid() {
        User user = SecurityUtils.getCurrentUser(userService);
        List<MySubscriptionsBean> subscriptionsList=subscriptionsService.findMySubscriptions(user.getEmail());
        if(subscriptionsList==null || subscriptionsList.size()==0)
        {
            verticalWrapper.addComponent(new Label("<h2><b>you do not have any subscriptions</b></h2>",ContentMode.HTML));
        }
        else
        {
            Grid<MySubscriptionsBean> grid = new Grid<MySubscriptionsBean>("MySubscriptions");
            grid.setSizeFull();
            grid.setItems(subscriptionsList);
            grid.addStyleName("bold");
            grid.addStyleName(StyleUtil.getRowStyle(subscriptionsList.get(0)));
           
            grid.addColumn(MySubscriptionsBean::getListingId).setCaption("Id");
            grid.addColumn(MySubscriptionsBean::getName).setCaption("Name");
            grid.addColumn(MySubscriptionsBean::getPhone).setCaption("Phone");
            //grid.addColumn(MySubscriptionsBean::getVehicleBrand).setCaption("Car");
            grid.addColumn(MySubscriptionsBean::getVehicleNumber).setCaption("Number");
            grid.addColumn(MySubscriptionsBean::getFromDate).setCaption("From");
            grid.addColumn(MySubscriptionsBean::getToDate).setCaption("To");
            grid.addColumn(MySubscriptionsBean::getStartPoint).setCaption("Start point");            
            grid.addColumn(MySubscriptionsBean::getStartTime).setCaption("Start time");
            //grid.addColumn(MySubscriptionsBean::getEndPoint).setCaption("End point");
            grid.addColumn(MySubscriptionsBean::getChargeable).setCaption("Chargeable");
            //grid.addColumn(MySubscriptionsBean::getInactiveStDt).setCaption("Inactive start date");
            //grid.addColumn(MySubscriptionsBean::getInactiveEndDt).setCaption("Inactive end date");
            grid.addColumn(MySubscriptionsBean::getStatus).setCaption("Status");

            //orderService.findOrder(Long.MIN_VALUE)
            //orderService.findOrder(Long.MIN_VALUE);
            Grid.Column cancelColumn = grid.addColumn(mysubscriptionsbean -> "Cancel",
            new ButtonRenderer(clickEvent -> {
                processSubscription(clickEvent,"Cancel",grid);
          }));
            cancelColumn.setCaption("Cancel");
            
           verticalWrapper.addComponent(grid);
        }
    }
    
     private void processSubscription(ClickableRenderer.RendererClickEvent clickEvent, String action,Grid<MySubscriptionsBean> grid) 
    {   System.out.println("###action="+action);
        System.out.println("*******"+((MySubscriptionsBean)clickEvent.getItem()).getListingId());
        MySubscriptionsBean msb =((MySubscriptionsBean)clickEvent.getItem());
        
        Subscriptions s = subscriptionsService.findSubscription(msb.getSubscriptionId());
        s.setStatus(String.valueOf(OrderEditPresenter.SubscriptionStatus.CANCELLED));
        
        subscriptionsService.saveSubscriptions(s);
        
        Order o =orderService.findOrder(s.getListingId());
        o.setNoSeats(o.getNoSeats()+1);
        o=orderService.saveOrder(o, null);
        
        User user = SecurityUtils.getCurrentUser(userService);
        List<MySubscriptionsBean> subscriptionsList=subscriptionsService.findMySubscriptions(user.getEmail());
        //grid.getDataProvider().refreshAll();
        grid.setItems(subscriptionsList);
        SendMailUtil.send(s.getEmail());
    }

	
       
}
