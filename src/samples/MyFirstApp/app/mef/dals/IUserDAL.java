package mef.dals;

import java.util.List;

import mef.entities.User;

public interface IUserDAL
{
	int size();
	User findById(long id);
	List<User> all();
	void delete(long id);
	void save(User entity);        

}
