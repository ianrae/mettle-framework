//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.entities;

import mef.gen.User_GEN;
import models.UserModel;

public class User extends User_GEN
{
		private UserModel model;
		
        public User()
        {
        	model = new UserModel();
        }
        public User( String name)
        {
            model = new UserModel();
            model.setName(name);
        }
        public User(UserModel model)
        {
        	this.model = model;
        }
        public Object getUnderlyingModel()
        {
        	return model;
        }

        public User(User entity)
        {
        	model = new UserModel();
        	this.setId(entity.getId());
        	this.setName(entity.getName());
        }

        //get/set
        public Long getId() 
        {
            return model.getId();
        }
        public String getName() 
        {
            return model.getName();
        }
        
        
        public void setId(Long val)
        {
        	model.setId(val);
        }
        public void setName(String val)
        {
        	model.setName(val);
        }
}

