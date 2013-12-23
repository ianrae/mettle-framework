package persistence;


public interface IIdGenerator 
{
	void assignId(Thing thing);
	void forceNextId(Long id);
}
