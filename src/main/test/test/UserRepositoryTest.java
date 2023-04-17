package test;

import repositories.UserRepository;
import org.junit.Test;

import dtos.UserDTO;
import repositories.IUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public final class UserRepositoryTest extends RepositoryTestBase<UserDTO, IUserRepository> {

	@Test
	public void shouldAddUser() throws SQLException {
		UserDTO user = new UserDTO(1, "User", "Password");

		_repository.add(user);

		String query = "select count(USER_ID) from USERS";
		PreparedStatement statement = _repository.getConnection().prepareStatement(query);
		ResultSet result = statement.executeQuery();
		assertTrue(result.next());
		assertEquals(1, result.getInt("count(USER_ID)"));
	}

	@Test
	public void existsShouldReturnTrue() {
		UserDTO user = new UserDTO(1, "User", "Password");
		_repository.add(user);

		boolean exists = _repository.exists(user);

		assertTrue(exists);
	}

	@Test
	public void existsShouldReturnFalseForMissingUser() {
		UserDTO missingUser = new UserDTO(1, "User", "Password");

		boolean exists = _repository.exists(missingUser);

		assertFalse(exists);
	}

	@Test
	public void shouldFindUserByName() {
		UserDTO user = new UserDTO(1, "someUser1", "Password");
		UserDTO user2 = new UserDTO(2, "anotherUser2", "Password");
		_repository.add(user);
		_repository.add(user2);

		List<UserDTO> listByName = _repository.findByName("User");

		assertEquals(2, listByName.size());
		assertEquals("someUser1", listByName.get(0).getLogin());
		assertEquals("anotherUser2", listByName.get(1).getLogin());
	}

	@Test
	public void noUserFoundByNameShouldReturnEmptyList() {
		UserDTO user = new UserDTO(1, "someUser", "Password");
		_repository.add(user);

		List<UserDTO> listByName = _repository.findByName("Someone");

		assertEquals(0, listByName.size());
	}

	@Test
	public void shouldFindUserById() {
		UserDTO user = new UserDTO(1, "User", "Password");
		_repository.add(user);

		Optional<UserDTO> returnedUser = _repository.findById(1);

		assertTrue(returnedUser.isPresent());
		assertEquals(1, returnedUser.get().getId());
		assertEquals("User", returnedUser.get().getLogin());
		assertEquals("Password", returnedUser.get().getPassword());
	}

	@Test
	public void noUserFoundByIdShouldReturnEmptyOptional() {
		Optional<UserDTO> returnedUser = _repository.findById(1);

		assertFalse(returnedUser.isPresent());
	}

	@Test
	public void shouldUpdateUser() {
		UserDTO user = new UserDTO(1, "User", "Password");
		_repository.add(user);
		user.setLogin("updatedUser");
		user.setPassword("updatedPassword");

		_repository.update(user);

		Optional<UserDTO> updatedUser = _repository.findById(1);
		assertTrue(updatedUser.isPresent());
		assertEquals(1,updatedUser.get().getId());
		assertEquals("updatedUser", updatedUser.get().getLogin());
		assertEquals("updatedPassword", updatedUser.get().getPassword());
	}

	@Test
	public void addOrUpdateShouldAddWhenUserMissing() {
		UserDTO user = new UserDTO(1, "User", "Password");

		_repository.addOrUpdate(user);

		assertTrue(_repository.exists(user));
	}

	@Test
	public void addOrUpdateShouldUpdateWhenUserPresent() {
		UserDTO user = new UserDTO(1, "User", "Password");
		_repository.add(user);
		user.setLogin("updatedUser");
		user.setPassword("updatedPassword");

		_repository.addOrUpdate(user);

		Optional<UserDTO> updatedUser = _repository.findById(1);
		assertTrue(updatedUser.isPresent());
		assertEquals(1,updatedUser.get().getId());
		assertEquals("updatedUser", updatedUser.get().getLogin());
		assertEquals("updatedPassword", updatedUser.get().getPassword());
	}

	@Test
	public void shouldDeleteUser() {
		UserDTO user = new UserDTO(1, "User", "Password");
		_repository.add(user);

		_repository.delete(user);

		assertFalse(_repository.exists(user));
	}

	@Test
	public void shouldGetCount() {
		UserDTO user1 = new UserDTO(1, "User", "Password");
		UserDTO user2 = new UserDTO(2, "User2", "Password2");
		_repository.add(user1);
		_repository.add(user2);

		int count = _repository.getCount();

		assertEquals(2, count);
	}

	@Test
	public void getCountWhenEmptyShouldReturnZero() {
		int count = _repository.getCount();

		assertEquals(0, count);
	}

	@Override
	protected IUserRepository Create() {
		return new UserRepository();
	}
}