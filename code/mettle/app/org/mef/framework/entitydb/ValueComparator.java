package org.mef.framework.entitydb;

import java.util.Comparator;

public class ValueComparator<T> implements Comparator<T>
{
    private boolean ascending;
	private String fieldName;
	private EntityDB<T> db;
	private IValueMatcher matcher;
	private int matchType;
	
    public ValueComparator(EntityDB<T> db, String fieldName, boolean ascending, IValueMatcher matcher, int matchType)
    {
    	this.db = db;
    	this.fieldName = fieldName;
        this.ascending = ascending;
        this.matcher = matcher;
        this.matchType = matchType;
    }
    
    public int compare(T obj1, T obj2)
    {
    	Object value1 = db.helper.getFieldValue((T) obj1, fieldName);
    	Object value2 = db.helper.getFieldValue((T) obj2, fieldName);

    	if (ascending)
        {
            return matcher.compare(value1, value2, matchType);
        }
        else
        {
            return matcher.compare(value2, value1, matchType);
        }
    }
}   	
