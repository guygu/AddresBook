package homeAssignment.addresBook.services;

import java.util.List;

import homeAssignment.addresBook.entities.Contact;
import homeAssignment.addresBook.exceptions.ContactNotFoundException;

public interface ContactService {

	List<Contact> getAll();

	Contact getContact(Long id) throws ContactNotFoundException;

	Contact save(Contact contact);

	List<Contact> search(String name);

	void delete(Long id) throws Exception;

}