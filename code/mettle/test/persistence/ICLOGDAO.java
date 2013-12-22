package persistence;

import java.util.List;

import org.mef.framework.dao.IDAO;

import clog.TMTests;
import clog.TMTests.ClogEntity;

public interface ICLOGDAO  extends IDAO
{
	ClogEntity findById(long id);
	List<ClogEntity> all();
	void save(ClogEntity entity);        
	void update(ClogEntity entity);

}