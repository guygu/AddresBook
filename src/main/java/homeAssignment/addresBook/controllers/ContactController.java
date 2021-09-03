package homeAssignment.addresBook.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import homeAssignment.addresBook.entities.Contact;
import homeAssignment.addresBook.exceptions.ContactNotFoundException;
import homeAssignment.addresBook.exceptions.InvalidRequestException;
import homeAssignment.addresBook.services.ContactService;

@RestController
public class ContactController {

	
	@Autowired
	private ContactService contactService;
	
	// GET
	@GetMapping("/contacts")
	public @ResponseBody List<Contact> contacts() {

		return contactService.getAll();
	}

	@GetMapping("/contacts/{id}")
	public Contact getContact(@PathVariable("id") Long id) throws ContactNotFoundException {
		
		if (id == null) {
			throw new InvalidRequestException("ID must not be null!");
		}

		return contactService.getContact(id);
	}

	@GetMapping("/contacts/search/{name}")
	public List<Contact> getContactByName(@PathVariable("name") String name) {

		if (name == null || name.isEmpty()) {
			throw new InvalidRequestException("name must not be empty!");
		}

		return contactService.search(name);

	}

	// POST
	@PostMapping("/contacts/create")
	public Contact createContact(@Valid @RequestBody Contact contact) {

		return contactService.save(contact);

	}

	// DELETE
	@DeleteMapping("/contacts/delete/{id}")
	public void deleteContact(@PathVariable("id") Long id) throws Exception {

		if (id == null) {
			throw new InvalidRequestException("ID must not be null!");
		}

		contactService.delete(id);
	}

	// PUT
	@PutMapping("/contacts/update/{id}")
	Contact updateContact(@PathVariable("id") Long id, @Valid @RequestBody Contact updatedContact) throws Exception {


		if (id == null) {
			throw new InvalidRequestException("ID must not be null!");
		}
		
		Contact existingContact = contactService.getContact(id);

		existingContact.setName(updatedContact.getName());
		existingContact.setPhoneNumber(updatedContact.getPhoneNumber());
		return contactService.save(existingContact);

	}

}
