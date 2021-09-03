package homeAssignment.addresBook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import homeAssignment.addresBook.entities.Contact;
import homeAssignment.addresBook.repositories.ContactDao;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ContactDaoTest {

	@Autowired
	private ContactDao contactDao;

	@Test
	//@Rollback(false)
	public void testCreateContact() {

		Contact contact = new Contact("guy guri", "0523584124");
		Contact savedContact = contactDao.save(contact);
		assertNotNull(savedContact);
	}

	@Test
	//@Rollback(false)
	public void testFindContactByName() {

		Contact contact1 = new Contact("guy", "0523333333");
		contactDao.save(contact1);
		Contact contact2 = new Contact("amir", "0524444444");
		contactDao.save(contact2);
		Contact contact3 = new Contact("amit", "0525555555");
		contactDao.save(contact3);

		String name = "am";
		List<Contact> contacts = contactDao.findByNameStartingWithIgnoreCase(name);
		List<String> names = contacts.stream().map(contact -> contact.getName()).collect(Collectors.toList());
		assertTrue(names.contains(contact2.getName()));
		assertTrue(names.contains(contact3.getName()));
		assertTrue(!names.contains(contact1.getName()));

	}

	@Test
	//@Rollback(false)
	public void testDeleteContact() {

		Contact contact1 = new Contact("guy", "0523333333");
		contact1 = contactDao.save(contact1);

		boolean isExistBeforeDelete = contactDao.findById(contact1.getId()).isPresent();

		contactDao.deleteById(contact1.getId());

		boolean notExistAfterDelete = contactDao.findById(contact1.getId()).isPresent();

		assertTrue(isExistBeforeDelete);
		assertFalse(notExistAfterDelete);

	}

	@Test
	//@Rollback(false)
	public void testFindContactById() {

		Contact contact1 = new Contact("guy", "0523333333");
		contact1 = contactDao.save(contact1);

		var contactOpt = contactDao.findById(contact1.getId());
		if (contactOpt.isPresent()) {
			assertEquals(contactOpt.get(), contact1);
		} else
			assertNotNull(null);

	}

	@Test
	//@Rollback(false)
	public void testUpdateContact() {

		String contactName = "Daniel work";
		Contact contact = new Contact(1L, contactName, "0523666666");
		contact = contactDao.save(contact);

		var contactOpt = contactDao.findById(contact.getId());
		if (contactOpt.isPresent()) {
			assertEquals(contactOpt.get(), contact);
		} else
			assertNotNull(null);

	}

	@Test
	//@Rollback(false)
	public void testListContacts() {

		Contact contact1 = new Contact("guy", "0523333333");
		contactDao.save(contact1);
		Contact contact2 = new Contact("amir", "0524444444");
		contactDao.save(contact2);
		Contact contact3 = new Contact("amit", "0525555555");
		contactDao.save(contact3);

		List<Contact> contacts = (List<Contact>) contactDao.findAll();

		for (Contact contact : contacts) {
			System.out.println("name: " + contact.getName() + " phone: " + contact.getPhoneNumber());
		}

		assertTrue(contacts.size() > 0);
	}

}
