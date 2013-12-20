package persistence;

import clog.JsonTests.Thing;

public interface IIdGenerator 
{
	int assignId(Thing thing);
}
