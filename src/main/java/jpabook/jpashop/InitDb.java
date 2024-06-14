package jpabook.jpashop;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 총 주문 2개
 * * userA
 *   * JPA1 BOOK
 *   * JPA2 BOOK
 * * userB
 *   * SPRING1 BOOK
 *   * SPRING2 BOOK
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

	private final InitService initService;

	@PostConstruct
	public void init() {
		initService.dbInit1();
		initService.dbInit2();
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {

		private final EntityManager em;

		public void dbInit1() {
			Member member = createMember("userA", "부산", "1", "1111");
			em.persist(member);

			Book book1 = createBook("JPA1 BOOK", 10_000, 100);
			em.persist(book1);

			Book book2 = createBook("JPA2 BOOK", 20_000, 100);
			em.persist(book2);

			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10_000, 1);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20_000, 2);

			Delivery delivery = createDelivery(member);
			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
		}

		public void dbInit2() {
			Member member = createMember("userB", "대전", "2", "2222");
			em.persist(member);

			Book book1 = createBook("SPRING1 BOOK", 20_000, 200);
			em.persist(book1);

			Book book2 = createBook("SPRING2 BOOK", 40_000, 300);
			em.persist(book2);

			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20_000, 3);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40_000, 4);

			Delivery delivery = createDelivery(member);
			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
		}

		private static Member createMember(String name, String city, String street,
			String zipcode) {
			Member member = new Member();
			member.setName(name);
			member.setAddress(new Address(city, street, zipcode));
			return member;
		}

		private static Book createBook(String name, int price, int stockQuantity) {
			Book book1 = new Book();
			book1.setName(name);
			book1.setPrice(price);
			book1.setStockQuantity(stockQuantity);
			return book1;
		}

		private static Delivery createDelivery(Member member) {
			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());
			return delivery;
		}
	}
}
