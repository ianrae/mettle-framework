//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.List;
import org.mef.framework.dal.IDAL;
import mef.dals.*;
import mef.mocks.*;
import boundaries.dals.*;
public class AllKnownDALs_GEN  
{
public List<IDAL> getDALs(boolean createMocks)
{
	ArrayList<IDAL> L = new ArrayList<IDAL>();
    
if (createMocks)
{
	L.add(new MockTaskDAL());
}
else
{
	L.add(new TaskDAL());
}	
if (createMocks)
{
	L.add(new MockUserDAL());
}
else
{
	L.add(new UserDAL());
}	
if (createMocks)
{
	L.add(new MockPhoneDAL());
}
else
{
	L.add(new PhoneDAL());
}	
	return L;
}
}
