package mate.academy.bookstore.repository;

import java.util.Set;
import mate.academy.bookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Set<Order> findAllByUserEmail(String email);
}
