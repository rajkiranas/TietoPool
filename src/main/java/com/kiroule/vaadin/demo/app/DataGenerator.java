package com.kiroule.vaadin.demo.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.spring.annotation.SpringComponent;
import com.kiroule.vaadin.demo.backend.OrderRepository;
import com.kiroule.vaadin.demo.backend.PickupLocationRepository;
import com.kiroule.vaadin.demo.backend.ProductRepository;
import com.kiroule.vaadin.demo.backend.UserRepository;
import com.kiroule.vaadin.demo.backend.data.OrderState;
import com.kiroule.vaadin.demo.backend.data.Role;
import com.kiroule.vaadin.demo.backend.data.entity.Customer;
import com.kiroule.vaadin.demo.backend.data.entity.HistoryItem;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.data.entity.OrderItem;
import com.kiroule.vaadin.demo.backend.data.entity.PickupLocation;
import com.kiroule.vaadin.demo.backend.data.entity.Product;
import com.kiroule.vaadin.demo.backend.data.entity.Route;
import com.kiroule.vaadin.demo.backend.data.entity.User;
import com.kiroule.vaadin.demo.backend.service.RouteService;
import java.time.ZoneId;
import java.util.Date;

@SpringComponent
public class DataGenerator implements HasLogger {

	private static final String[] FILLING = new String[] { "Strawberry", "Chocolate", "Blueberry", "Raspberry",
			"Vanilla" };
	private static final String[] TYPE = new String[] { "Cake", "Pastry", "Tart", "Muffin", "Biscuit", "Bread", "Bagel",
			"Bun", "Brownie", "Cookie", "Cracker", "Cheese Cake" };
	private static final String[] FIRST_NAME = new String[] { "Ori", "Amanda", "Octavia", "Laurel", "Lael", "Delilah",
			"Jason", "Skyler", "Arsenio", "Haley", "Lionel", "Sylvia", "Jessica", "Lester", "Ferdinand", "Elaine",
			"Griffin", "Kerry", "Dominique" };
	private static final String[] LAST_NAME = new String[] { "Carter", "Castro", "Rich", "Irwin", "Moore", "Hendricks",
			"Huber", "Patton", "Wilkinson", "Thornton", "Nunez", "Macias", "Gallegos", "Blevins", "Mejia", "Pickett",
			"Whitney", "Farmer", "Henry", "Chen", "Macias", "Rowland", "Pierce", "Cortez", "Noble", "Howard", "Nixon",
			"Mcbride", "Leblanc", "Russell", "Carver", "Benton", "Maldonado", "Lyons" };
        private static final String[] PLACES = new String[] { "Kothrud", "Shivaji Nagar", "Baner", "EON",
			"Kharadi","Chandani Choowk","Pashan", "SB Road","Hadapsar"};
        private static final String[] EMAILS = new String[] { "vishal.ambalge@tieto.com", "neha.patil@tieto.com", "pranita.gandhi@tieto.com"};
        private static final String[] NAMES = new String[] { "Vishal Ambalge", "Neha Patil", "Pranita Gandhi"};
        private static final String[] CARS = new String[] { "Maruti Suzuki Baleno", "Ford Fiesta", "Volksvagen Vento"};
        private static final String[] CAR_NUMBERS = new String[] { "MH20CH283", "MH20CH33", "MH20CH77"};
        private static final Long[] ROUTES = new Long[] { 1L,2L,3L,4L,5L,6L,7L,8L,9L,10L};

	private final Random random = new Random(1L);

	private final List<PickupLocation> pickupLocations = new ArrayList<>();
	private final List<Product> products = new ArrayList<>();
	private User baker;
	private User barista;
        private boolean shuffleDate=false;

	@Bean
	public CommandLineRunner loadData(OrderRepository orderRepository, UserRepository userRepository,
			ProductRepository productRepository, PickupLocationRepository pickupLocationRepository,
			PasswordEncoder passwordEncoder, RouteService routeService) {
		return args -> {
			if (hasData(userRepository)) {
				getLogger().info("Using existing database");
				return;
			}

			
                        try {
                            getLogger().info("Generating demo data");
                            getLogger().info("... generating users");
                            createUsers(userRepository, passwordEncoder);
    //			getLogger().info("... generating products");
    //			createProducts(productRepository);
    //			getLogger().info("... generating pickup locations");
    //			createPickupLocations(pickupLocationRepository);
                            getLogger().info("... generating orders");
                            createOrders(orderRepository, routeService);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
			

			getLogger().info("Generated demo data");
		};
	}

	private boolean hasData(UserRepository userRepository) {
		return userRepository.count() != 0L;
	}

	private Customer createCustomer() {
		Customer customer = new Customer();
		String first = getRandom(FIRST_NAME);
		String last = getRandom(LAST_NAME);
		customer.setFullName(first + " " + last);
		customer.setPhoneNumber(getRandomPhone());
		if (random.nextInt(10) == 0) {
			customer.setDetails("Very important customer");
		}
		return customer;
	}

	private String getRandomPhone() {
		return "+1-555-" + String.format("%04d", random.nextInt(10000));
	}

	private void createOrders(OrderRepository orderRepository, RouteService routeService) {
		int yearsToInclude = 2;
		List<Order> orders = new ArrayList<>();

		LocalDate now = LocalDate.now();
		LocalDate oldestDate = LocalDate.of(now.getYear() - yearsToInclude, 1, 1);
		LocalDate newestDate = now.plusMonths(1L);

                int j=0;
//		for (LocalDate dueDate = oldestDate; dueDate.isBefore(newestDate); dueDate = dueDate.plusDays(1)) {
//			// Create a slightly upwards trend - everybody wants to be
//			// successful
//			int relativeYear = dueDate.getYear() - now.getYear() + yearsToInclude;
//			int relativeMonth = relativeYear * 12 + dueDate.getMonthValue();
//			double multiplier = 1.0 + 0.03 * relativeMonth;
//			int ordersThisDay = (int) (random.nextInt(10) + 1 * multiplier);
//			for (int i = 0; i < ordersThisDay; i++) {
//				orders.add(createOrder(routeService));
//			}
//                        j++;
//                        if(j==100)
//                            break;
//		}

                Order o = createOrder("Vishal Ambalge", routeService);
                orders.add(o);
                o = createOrder("Neha Patil", routeService);
                orders.add(o);
                o = createOrder("Pranita Gandhi", routeService);
                orders.add(o);
                o = createOrder("Rajkiran Sonde", routeService);
                orders.add(o);
                o = createOrder("Pradnyapal Nagrale", routeService);
                orders.add(o);
		orderRepository.save(orders);
	}

	private Order createOrder(String name, RouteService routeService) {
		Order order = new Order();
                
                order.setChargeable(false);                
                //String name=getRandom(NAMES);
                order.setName(name);
                name=name.replaceAll(" ",".");
                name=name+"@tieto.com";
                order.setEmail(name.toLowerCase());
                order.setEndPoint(getRandom(PLACES));
                LocalTime startTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toLocalTime();
                order.setStartTime(startTime);
                order.setEndTime(startTime.plusHours(1));
                order.setIsActive(true);
                
                order.setPhone(9881409240L);                
                order.setRoute(routeService.findRoute(getRandom(ROUTES)));
                order.setStartPoint(getRandom(PLACES));
                
                if(shuffleDate)
                {
                    LocalDateTime startDate = LocalDateTime.now();                
                    order.setValidFrom(startDate);
                    order.setValidTo(startDate.plusDays(0).toLocalDate());
                    shuffleDate=false;
                }
                else
                {
                    LocalDateTime startDate = LocalDateTime.now();                
                    //startDate=startDate.minusDays(20);
                    order.setValidFrom(startDate);
                    order.setValidTo(startDate.plusDays(30).toLocalDate());                    
                    shuffleDate=true;
                }
                
                order.setVehicleBrand(getRandom(CARS));
                order.setVehicleNumber(getRandom(CAR_NUMBERS));
                order.setVehicleType(4L);
                order.setNoSeats(4);

//		int itemCount = random.nextInt(3);
//		List<OrderItem> items = new ArrayList<>();
//		for (int i = 0; i <= itemCount; i++) {
//			OrderItem item = new OrderItem();
//			Product product;
//			do {
//				product = getRandomProduct();
//			} while (containsProduct(items, product));
//			item.setProduct(product);
//			item.setQuantity(random.nextInt(10) + 1);
//			if (random.nextInt(5) == 0) {
//				if (random.nextBoolean()) {
//					item.setComment("Lactose free");
//				} else {
//					item.setComment("Gluten free");
//				}
//			}
//			items.add(item);
//		}
//		order.setItems(items);
//
//		order.setHistory(createOrderHistory(order));

		return order;
	}

//	private List<HistoryItem> createOrderHistory(Order order) {
//		ArrayList<HistoryItem> history = new ArrayList<>();
//		HistoryItem item = new HistoryItem(getBarista(), "Order placed");
//		item.setNewState(OrderState.NEW);
//		LocalDateTime orderPlaced = order.getDueDate().minusDays(random.nextInt(5) + 2L).atTime(random.nextInt(10) + 7,
//				00);
//		item.setTimestamp(orderPlaced);
//		history.add(item);
//		if (order.getState() == OrderState.CANCELLED) {
//			item = new HistoryItem(getBarista(), "Order cancelled");
//			item.setNewState(OrderState.CANCELLED);
//			item.setTimestamp(orderPlaced.plusDays(random
//					.nextInt((int) orderPlaced.until(order.getDueDate().atTime(order.getDueTime()), ChronoUnit.DAYS))));
//			history.add(item);
//		} else if (order.getState() == OrderState.CONFIRMED || order.getState() == OrderState.DELIVERED
//				|| order.getState() == OrderState.PROBLEM || order.getState() == OrderState.READY) {
//			item = new HistoryItem(getBaker(), "Order confirmed");
//			item.setNewState(OrderState.CONFIRMED);
//			item.setTimestamp(orderPlaced.plusDays(random.nextInt(2)).plusHours(random.nextInt(5)));
//			history.add(item);
//
//			if (order.getState() == OrderState.PROBLEM) {
//				item = new HistoryItem(getBaker(), "Can't make it. Did not get any ingredients this morning");
//				item.setNewState(OrderState.PROBLEM);
//				item.setTimestamp(order.getDueDate().atTime(random.nextInt(4) + 4, 0));
//				history.add(item);
//			} else if (order.getState() == OrderState.READY || order.getState() == OrderState.DELIVERED) {
//				item = new HistoryItem(getBaker(), "Order ready for pickup");
//				item.setNewState(OrderState.READY);
//				item.setTimestamp(order.getDueDate().atTime(random.nextInt(2) + 8, random.nextBoolean() ? 0 : 30));
//				history.add(item);
//				if (order.getState() == OrderState.DELIVERED) {
//					item = new HistoryItem(getBaker(), "Order delivered");
//					item.setNewState(OrderState.DELIVERED);
//					item.setTimestamp(order.getDueDate().atTime(order.getDueTime().minusMinutes(random.nextInt(120))));
//					history.add(item);
//				}
//			}
//		}
//
//		return history;
//	}

	private boolean containsProduct(List<OrderItem> items, Product product) {
		for (OrderItem item : items) {
			if (item.getProduct() == product) {
				return true;
			}
		}
		return false;
	}

	private LocalTime getRandomDueTime() {
		int time = 8 + 4 * random.nextInt(3);

		return LocalTime.of(time, 0);
	}

	private OrderState getRandomState(LocalDate due) {
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);
		LocalDate twoDays = today.plusDays(2);

		if (due.isBefore(today)) {
			if (random.nextDouble() < 0.9) {
				return OrderState.DELIVERED;
			} else {
				return OrderState.CANCELLED;
			}
		} else {
			if (due.isAfter(twoDays)) {
				return OrderState.NEW;
			} else if (due.isAfter(tomorrow)) {
				// in 1-2 days
				double resolution = random.nextDouble();
				if (resolution < 0.8) {
					return OrderState.NEW;
				} else if (resolution < 0.9) {
					return OrderState.PROBLEM;
				} else {
					return OrderState.CANCELLED;
				}
			} else {
				double resolution = random.nextDouble();
				if (resolution < 0.6) {
					return OrderState.READY;
				} else if (resolution < 0.8) {
					return OrderState.DELIVERED;
				} else if (resolution < 0.9) {
					return OrderState.PROBLEM;
				} else {
					return OrderState.CANCELLED;
				}
			}

		}
	}

	private Product getRandomProduct() {
		double cutoff = 2.5;
		double g = random.nextGaussian();
		g = Math.min(cutoff, g);
		g = Math.max(-cutoff, g);
		g += cutoff;
		g /= (cutoff * 2.0);

		return products.get((int) (g * (products.size() - 1)));
	}

	private PickupLocation getRandomPickupLocation() {
		return getRandom(pickupLocations);
	}

	private User getBaker() {
		return baker;
	}

	private User getBarista() {
		return barista;
	}

	private <T> T getRandom(List<T> items) {
		return items.get(random.nextInt(items.size()));
	}

	private <T> T getRandom(T[] array) {
		return array[random.nextInt(array.length)];
	}

	private void createPickupLocations(PickupLocationRepository pickupLocationRepository) {
		PickupLocation store = new PickupLocation();
		store.setName("Store");
		pickupLocations.add(pickupLocationRepository.save(store));
		PickupLocation bakery = new PickupLocation();
		bakery.setName("Bakery");
		pickupLocations.add(pickupLocationRepository.save(bakery));
	}

	private void createProducts(ProductRepository productRepository) {
		for (int i = 0; i < 10; i++) {
			Product product = new Product();
			product.setName(getRandomProductName());
			double doublePrice = 2.0 + random.nextDouble() * 100.0;
			product.setPrice((int) (doublePrice * 100.0));
			products.add(productRepository.save(product));
		}
	}

	private String getRandomProductName() {
		String firstFilling = getRandom(FILLING);
		String name;
		if (random.nextBoolean()) {
			String secondFilling;
			do {
				secondFilling = getRandom(FILLING);
			} while (secondFilling.equals(firstFilling));

			name = firstFilling + " " + secondFilling;
		} else {
			name = firstFilling;
		}
		name += " " + getRandom(TYPE);

		return name;
	}

	private void createUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//		baker = userRepository.save(new User("baker@vaadin.com", "Heidi", passwordEncoder.encode("baker"), Role.BAKER,9881409240L));
//		User user = new User("barista@vaadin.com", "Malin", passwordEncoder.encode("barista"), Role.BARISTA,9881409240L);
//		user.setLocked(true);
//		barista = userRepository.save(user);
                
		User user = new User("vishal.ambalge@tieto.com", "Vishal Ambalge", passwordEncoder.encode("tieto"), Role.ADMIN,9881409240L);
		user.setLocked(true);
		userRepository.save(user);
                
                user = new User("neha.patil@tieto.com", "Neha Patil", passwordEncoder.encode("tieto"), Role.ADMIN,9881409240L);
		user.setLocked(true);
		userRepository.save(user);
                
                user = new User("pranita.gandhi@tieto.com", "Pranita Gandhi", passwordEncoder.encode("tieto"), Role.ADMIN,9881409240L);
		user.setLocked(true);
		userRepository.save(user);
                
                user = new User("manish.puri@tieto.com", "Manish Raj Puri", passwordEncoder.encode("tieto"), Role.ADMIN,9881409240L);
		user.setLocked(true);
		userRepository.save(user);
	}
}
