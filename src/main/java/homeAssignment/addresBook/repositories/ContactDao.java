package homeAssignment.addresBook.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import homeAssignment.addresBook.entities.Contact;

@Repository
public interface ContactDao extends CrudRepository<Contact, Long> {

	List<Contact> findByNameStartingWithIgnoreCase(String name);

}
