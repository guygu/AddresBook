package homeAssignment.addresBook.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import homeAssignment.addresBook.entities.Contact;
import homeAssignment.addresBook.exceptions.ContactNotFoundException;
import homeAssignment.addresBook.repositories.ContactDao;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactDao contactDao;

	@Override
	public List<Contact> getAll() {

		List<Contact> contacts = new ArrayList<>();
		contactDao.findAll().forEach(contact -> contacts.add(contact));
		return contacts;

	}

	@Override
	public Contact getContact(Long id) throws ContactNotFoundException {

		return contactDao.findById(id).orElseThrow(() -> new ContactNotFoundException(id));
	}

	@Override
	public Contact save(Contact contact) {

		return contactDao.save(contact);
	}

	@Override
	public List<Contact> search(String name) {

		List<Contact> contacts = new ArrayList<>();

		contactDao.findByNameStartingWithIgnoreCase(name).forEach(contact -> contacts.add(contact));
		return contacts;

	}

	@Override
	public void delete(Long id) throws Exception {

		getContact(id);

		contactDao.deleteById(id);
	}

}
