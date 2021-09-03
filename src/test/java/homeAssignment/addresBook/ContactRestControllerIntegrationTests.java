package homeAssignment.addresBook;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeAssignment.addresBook.entities.Contact;
import homeAssignment.addresBook.exceptions.ContactNotFoundException;
import homeAssignment.addresBook.repositories.ContactDao;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactRestControllerIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ContactDao contactDao;
	
	

	@Test
	public void testContactNameMustNotBeBlank() throws Exception {

		Contact contact = new Contact("", "0523444444");

		String url = "/contacts/create";
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(contact));

		mockMvc.perform(mockRequest).andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void getAllContacts() throws Exception {

		String url = "/contacts";
		mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk());

	}

	@Test
	public void getContactById() throws Exception {

		Contact contact = contactDao.save(new Contact("guy", "0523444444"));
		Long contactId = contact.getId();

		String url = "/contacts/" + contactId;
		mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.name", is("guy")));
	}

	@Test
	public void createContract() throws Exception {

		Contact newContact = new Contact("John Doe", "0523666666");

		String url = "/contacts/create";
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(newContact));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("John Doe"))).andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void updateContact() throws Exception {

		Contact contact = contactDao.save(new Contact("guy", "0523444444"));
		Long contactId = contact.getId();

		Contact updatedContact = new Contact("Rayven Zambo", "0523666666");

		String url = "/contacts/update/" + contactId;
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(url)
				.contentType(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(updatedContact));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Rayven Zambo"))).andExpect(jsonPath("$.id", is(contactId.intValue())))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void deleteContactById() throws Exception {

		Contact contact = contactDao.save(new Contact("guy", "0523444444"));
		Long contactId = contact.getId();

		String url = "/contacts/delete/" + contactId;
		mockMvc.perform(MockMvcRequestBuilders.delete(url)).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());

		Optional<Contact> result = contactDao.findById(contactId);
		assertTrue(!result.isPresent());

	}

	@Test
	public void deleteContactById_notFound() throws Exception {

		// contact Id that does not exist in the DB
		String url = "/contacts/delete/" + 100L;

		mockMvc.perform(MockMvcRequestBuilders.delete(url)).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ContactNotFoundException))
				.andExpect(result -> assertEquals("Contact with ID 100 does not exist.",
						result.getResolvedException().getMessage()))
				.andDo(MockMvcResultHandlers.print());

	}

}
