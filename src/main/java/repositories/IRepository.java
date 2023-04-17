package repositories;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import dtos.DTOBase;

public interface IRepository<TDTO extends DTOBase> {

	Connection getConnection();

	void add(TDTO dto);

	void update(TDTO dto);
	
	void addOrUpdate(TDTO dto);

	void delete(TDTO dto);

	Optional<TDTO> findById(int id);

	void beginTransaction();

	void commitTransaction();

	void rollbackTransaction() throws SQLException;
	
	int getCount();
	
	boolean exists(TDTO dto);

	void closeConnection();
}