package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import src.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {

}
