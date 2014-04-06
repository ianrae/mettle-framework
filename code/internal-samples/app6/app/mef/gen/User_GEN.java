//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import org.mef.framework.entities.Entity;
import mef.gen.*;
import models.*;
import mef.entities.*;
import java.util.Date;
public class User_GEN extends Entity
{

		private UserModel mModel;

        public User_GEN()
        {
			this.mModel = new UserModel();
        }
        public User_GEN(UserModel model)
        {
			this.mModel = model;
        }

        public User_GEN( String name)
        {
                this.setName(name);
        }

        public User_GEN(User_GEN entity)
        {
                this.setId(entity.getId());
                this.setName(entity.getName());
        }

	public Long getId()
	{
		return this.mModel.getId();
	}
	public void setId(Long val)
	{
		this.mModel.setId(val);
	}


	public String getName()
	{
		return this.mModel.getName();
	}
	public void setName(String val)
	{
		this.mModel.setName(val);
	}

}
