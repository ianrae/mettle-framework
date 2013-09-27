package mef.dals;

import mef.entities.*;
import java.util.List;
public interface IUserDAL
{
	int size();
	User findById(long id);
	List<User> all();
	void delete(long id);
	void save(User entity);        
        }
