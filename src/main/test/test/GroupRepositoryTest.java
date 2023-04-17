package test;

import repositories.GroupRepository;
import org.junit.Test;

import dtos.GroupDTO;
import repositories.IGroupRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class GroupRepositoryTest extends RepositoryTestBase<GroupDTO, IGroupRepository> {

	@Test
	public void shouldAddGroup() throws SQLException {
		GroupDTO group = new GroupDTO(1, "Group1", "Description of group1");

		_repository.add(group);

		String query = "select count(GROUP_ID) from `GROUPS`";
		PreparedStatement statement = _repository.getConnection().prepareStatement(query);
		ResultSet result = statement.executeQuery();
		assertTrue(result.next());
		assertEquals(1, result.getInt("count(GROUP_ID)"));
	}

	@Test
	public void existsShouldReturnTrue() {
		GroupDTO group = new GroupDTO(1, "Group1", "Description of group1");
		_repository.add(group);

		boolean exists = _repository.exists(group);

		assertTrue(exists);
	}

	@Test
	public void existsShouldReturnFalseForMissingGroup() {
		GroupDTO group = new GroupDTO(1, "Group1", "Description of group1");

		boolean exists = _repository.exists(group);

		assertFalse(exists);
	}

	@Test
	public void shouldFindGroupByName() {
		GroupDTO group = new GroupDTO(1, "someGroup1", "Description of group1");
		GroupDTO group2 = new GroupDTO(2, "anotherGroup2", "Description of group2");
		_repository.add(group);
		_repository.add(group2);

		List<GroupDTO> listByName = _repository.findByName("Group");

		assertEquals(2, listByName.size());
		assertEquals("someGroup1", listByName.get(0).getName());
		assertEquals("anotherGroup2", listByName.get(1).getName());
	}

	@Test
	public void noGroupFoundByNameShouldReturnEmptyList() {
		List<GroupDTO> listByName = _repository.findByName("Group");

		assertEquals(0, listByName.size());
	}

	@Test
	public void shouldUpdateGroup() {
		GroupDTO group = new GroupDTO(1, "Group1", "Description of group1");
		_repository.add(group);
		group.setName("updatedGroup1");
		group.setDescription("Updated description of group1");

		_repository.update(group);

		Optional<GroupDTO> updatedGroup = _repository.findById(1);
		assertTrue(updatedGroup.isPresent());
		assertEquals(1, updatedGroup.get().getId());
		assertEquals("updatedGroup1", updatedGroup.get().getName());
		assertEquals("Updated description of group1", updatedGroup.get().getDescription());
	}
	
	@Test
	public void addOrUpdateShouldAddWhenGroupMissing() {
		GroupDTO group = new GroupDTO(1, "Group1", "Description of group1");
		_repository.add(group);

		_repository.addOrUpdate(group);

		assertTrue(_repository.exists(group));
	}

	@Test
	public void addOrUpdateShouldUpdateWhenGroupPresent() {
		GroupDTO group = new GroupDTO(1, "Group1", "Description of group1");
		_repository.addOrUpdate(group);
		group.setName("updatedGroup1");
		group.setDescription("Updated description of group1");

		_repository.addOrUpdate(group);

		Optional<GroupDTO> updatedGroup = _repository.findById(1);
		assertTrue(updatedGroup.isPresent());
		assertEquals(1, updatedGroup.get().getId());
		assertEquals("updatedGroup1", updatedGroup.get().getName());
		assertEquals("Updated description of group1", updatedGroup.get().getDescription());
	}

	@Test
	public void delete() {
		GroupDTO group = new GroupDTO(1, "Group1", "Description of group1");
		_repository.add(group);

		_repository.delete(group);

		assertFalse(_repository.exists(group));
	}

	@Test
	public void shouldFindGroupById() {
		GroupDTO group = new GroupDTO(1, "Group1", "Description of group1");
		_repository.add(group);

		Optional<GroupDTO> returnedGroup = _repository.findById(1);

		assertTrue(returnedGroup.isPresent());
		assertEquals(1, returnedGroup.get().getId());
		assertEquals("Group1", returnedGroup.get().getName());
		assertEquals("Description of group1", returnedGroup.get().getDescription());
	}

	@Test
	public void noGroupFoundByIdShouldReturnEmptyOptional() {
		Optional<GroupDTO> returnedGroup = _repository.findById(1);

		assertFalse(returnedGroup.isPresent());
	}

	@Test
	public void shouldGetCount() {
		GroupDTO group = new GroupDTO(1, "someGroup1", "Description of group1");
		GroupDTO group2 = new GroupDTO(2, "anotherGroup2", "Description of group2");
		_repository.add(group);
		_repository.add(group2);

		int count = _repository.getCount();

		assertEquals(2, count);
	}

	@Test
	public void getCountWhenEmptyShouldReturnZero() {
		int count = _repository.getCount();

		assertEquals(0, count);
	}

	@Override
	protected IGroupRepository Create() {
		return new GroupRepository();
	}
}