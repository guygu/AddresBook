package homeAssignment.addresBook;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeAssignment.addresBook.controllers.ContactController;
import homeAssignment.addresBook.entities.Contact;
import homeAssignment.addresBook.exceptions.ContactNotFoundException;
import homeAssignment.addresBook.exceptions.InvalidRequestException;
import homeAssignment.addresBook.services.ContactService;
import homeAssignment.addresBook.services.ContactServiceImpl;
import javassist.NotFoundException;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@MockBean
	ContactService contactService;

	private Contact RECORD_1;
	private Contact RECORD_2;
	private Contact RECORD_3;

	@BeforeEach
	public void setUp() {
		RECORD_1 = new Contact(1L, "bob", "0521111111");
		RECORD_2 = new Contact(2L, "rob", "0522222222");
		RECORD_3 = new Contact(3L, "Barni", "0523333333");
	}

	@AfterEach
	public void tearDown() {
		RECORD_1 = RECORD_2 = RECORD_3 = null;
	}

	@Test
	public void getAllContacts_success() throws Exception {

		List<Contact> contacts = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

		Mockito.when(contactService.getAll()).thenReturn(contacts);

		String url = "/contacts";
		mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[2].name", is("Barni")));
	}

	@Test
	public void getContactById_success() throws Exception {

		Mockito.when(contactService.getContact(RECORD_1.getId())).thenReturn(RECORD_1);

		String url = "/contacts/" + RECORD_1.getId();
		mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.name", is("bob")));
	}

	@Test
	public void createContract_success() throws Exception {

		Contact newContact = new Contact("John Doe", "0523666666");
		Contact savedContact = new Contact(5L, newContact.getName(), newContact.getPhoneNumber());

		Mockito.when(contactService.save(newContact)).thenReturn(savedContact);

		String url = "/contacts/create";
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(newContact));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("John Doe"))).andExpect(jsonPath("$.id", is(5)))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void updateContact() throws Exception {

		Contact updatedContact = new Contact("Rayven Zambo", "0523666666");
		updatedContact.setId(1L);

		Mockito.when(contactService.getContact(RECORD_1.getId())).thenReturn(RECORD_1);
		Mockito.when(contactService.save(updatedContact)).thenReturn(updatedContact);

		String url = "/contacts/update/" + RECORD_1.getId();
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(url)
				.contentType(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(updatedContact));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Rayven Zambo"))).andExpect(jsonPath("$.id", is(1)))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void deleteContactById_success() throws Exception {

		Long contactId = 2L;

		Mockito.when(contactService.getContact(contactId)).thenReturn(RECORD_2);
		Mockito.doNothing().when(contactService).delete(contactId);

		String url = "/contacts/delete/" + contactId;
		mockMvc.perform(MockMvcRequestBuilders.delete(url))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

		Mockito.verify(contactService, times(1)).delete(contactId);

	}
	
	
	@Test
	public void deleteContactById_notFound() throws Exception {
		
		Long contactId = 2L;

		Mockito.when(contactService.getContact(contactId)).thenReturn(null);
		Mockito.doNothing().when(contactService).delete(contactId);

		String url = "/contacts/delete/" + contactId;

		mockMvc.perform(MockMvcRequestBuilders.delete(url)).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ContactNotFoundException))
				.andExpect(result -> assertEquals("Contact with ID 2 does not exist.",
						result.getResolvedException().getMessage()))
				.andDo(MockMvcResultHandlers.print());
		
		
		Mockito.verify(contactService, times(1)).delete(contactId);

	}

}
