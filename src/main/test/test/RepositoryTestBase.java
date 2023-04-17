package test;

import org.junit.After;
import org.junit.Before;

import dtos.DTOBase;
import repositories.IRepository;

import java.sql.SQLException;

public abstract class RepositoryTestBase<TDTO extends DTOBase, TRepository extends IRepository<TDTO>> {

	protected TRepository _repository;

	@Before
	public void before() {
		_repository = Create();
		if (_repository != null) {
			_repository.beginTransaction();
		}
	}

	@After
	public void after() throws SQLException {
		if (_repository != null) {
			_repository.rollbackTransaction();
			_repository.closeConnection();
		}
	}

	protected abstract TRepository Create();
}