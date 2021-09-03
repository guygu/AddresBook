package homeAssignment.addresBook.exceptions;

import javassist.NotFoundException;

public class ContactNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 1L;

	public ContactNotFoundException(Long id) {
		super(String.format("Contact with ID %d does not exist.", id));
	}

}
