//package org.mef.dalgen.unittests.gen;
//
//import org.mef.dal.*;
//import play.data.*;
//@Entity
//public class TaskModel extends Model
//{
//	@Transient
//    private Task entity;
//    public void initEntity(Task entity)
//    {
//        this.entity = entity;
//    }
//
//	public static Finder<Long,Task> find = new Finder(
//			Long.class, Task.class
//			);  
//
//
//    //getters and setters
//        //    long id;
//
//    public long getId() {
//        return this.entity.id;
//    }
//    public void forceId(long val) {
//        this.entity.id = val;
//    }
//
////   @Required 
//    String label;
//
//    public String getLabel() {
//        return this.entity.label;
//    }
//    public void setLabel(String val) {
//        this.entity.label = val;
//    }
//
////    boolean enabled;
//
//    public boolean getEnabled() {
//        return this.entity.enabled;
//    }
//    public void setEnabled(boolean val) {
//        this.entity.enabled = val;
//    }
//
//}
