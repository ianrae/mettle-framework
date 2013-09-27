package mef.dals;

import mef.entities.*;
import java.util.List;

public interface IPhoneDAL
{
	int size();
	Phone findById(long id);
	List<Phone> all();
	void delete(long id);
	void save(Phone entity);        
        }
