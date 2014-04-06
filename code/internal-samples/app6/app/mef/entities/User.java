//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.entities;

import mef.gen.User_GEN;
import models.UserModel;

public class User extends User_GEN
{
		private UserModel mModel;
		
        public User()
        {
        	mModel = new UserModel();
			this.mRawModel = mModel;
        }
        public User( String name)
        {
            mModel = new UserModel();
			this.mRawModel = mModel;
            mModel.setName(name);
        }
        public User(UserModel model)
        {
        	this.mModel = model;
        }
//        public Object getUnderlyingModel()
//        {
//        	return mModel;
//        }

        public User(User entity)
        {
        	mModel = new UserModel();
        	this.setId(entity.getId());
        	this.setName(entity.getName());
        }

        //get/set
        public Long getId() 
        {
            return mModel.getId();
        }
        public String getName() 
        {
            return mModel.getName();
        }
        
        
        public void setId(Long val)
        {
        	mModel.setId(val);
        }
        public void setName(String val)
        {
        	mModel.setName(val);
        }
}

