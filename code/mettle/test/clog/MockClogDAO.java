package clog;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.binder.IFormBinder;

import clog.TMTests.ClogEntity;
import persistence.ICLOGDAO;

public class MockClogDAO implements ICLOGDAO
{
	public boolean emptyDAO;
	private String fakeJson;
	
	public MockClogDAO(String fakeJson)
	{
		this.fakeJson = fakeJson;
	}
	
	@Override
	public int size() {
		return 1;
	}
	@Override
	public void delete(long id) {
	}
	@Override
	public void updateFrom(IFormBinder binder) {
	}
	@Override
	public ClogEntity findById(long id) 
	{
		ClogEntity entity = new ClogEntity();
		entity.blob = (emptyDAO) ? "" : fakeJson;
		return entity;
	}


	@Override
	public List<ClogEntity> all() 
	{
		ArrayList<ClogEntity> L = new ArrayList<TMTests.ClogEntity>();
		L.add(findById(1));
		return L;
	}
	@Override
	public void save(ClogEntity entity) 
	{
	}
	@Override
	public void update(ClogEntity entity)
	{
	}

}
