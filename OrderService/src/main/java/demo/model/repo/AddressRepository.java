package demo.model.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import demo.model.*;

import org.springframework.data.rest.core.annotation.RepositoryRestResource; 

@RepositoryRestResource(collectionResourceRel = "addresses", path = "addresses")
public interface AddressRepository extends CrudRepository<Address, Long> {
}