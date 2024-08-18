package com.borges.project.dao;

import com.borges.project.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDAO extends CrudRepository<Customer, String> {

    @Override
    List<Customer> findAll();

}
