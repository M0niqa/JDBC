package repositories;

import java.util.List;

import dtos.GroupDTO;
import repositories.IRepository;

public interface IGroupRepository extends IRepository<GroupDTO> {

    List<GroupDTO> findByName(String name);
}