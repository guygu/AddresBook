package homeAssignment.addresBook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import homeAssignment.addresBook.entities.Contact;
import homeAssignment.addresBook.repositories.ContactDao;
import homeAssignment.addresBook.services.ContactServiceImpl;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ContactServiceTest {

	@MockBean
	private ContactDao contactDao;

	@InjectMocks
	private ContactServiceImpl contactService;

	
	@Test
	public void createContactTest() {
		Contact contact = new Contact(1L, "Lokesh", "0523444444");

		contactService.save(contact);

		verify(contactDao, times(1)).save(contact);
	}

	@Test
	public void getAllContacts() {
		
		List<Contact> list = new ArrayList<>();
		Contact contact1 = new Contact(1L, "John", "0523588852");
		Contact contact2 = new Contact(2L, "Alex", "0523588852");
		Contact contact3 = new Contact(3L, "Steve", "0523588852");

		list.add(contact1);
		list.add(contact2);
		list.add(contact3);

		when(contactDao.findAll()).thenReturn(list);

		List<Contact> contactList = contactService.getAll();

		assertEquals(3, contactList.size());
		verify(contactDao, times(1)).findAll();
	}

	@Test
	public void getContactByIdTest() throws Exception {

		when(contactDao.findById(1L)).thenReturn(Optional.of(new Contact(1L, "Lokesh", "0523444444")));

		Contact contact = contactService.getContact(1L);

		assertEquals("Lokesh", contact.getName());
		assertEquals("0523444444", contact.getPhoneNumber());
	}

}
