package repositories;

import java.util.List;

import dtos.UserDTO;

public interface IUserRepository extends IRepository<UserDTO> {

	List<UserDTO> findByName(String username);
}