package mef.presenters.replies;

import java.util.List;
import java.util.Map;

import mef.entities.User;

import org.mef.framework.replies.Reply;

public class UserReply extends Reply
{
	public List<User> _allL;
	public User _entity; //for New and Create
	
	public  Map<String,String> _options;
	
	public UserReply()
	{
	}
}