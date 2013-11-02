//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.
package mef;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import org.mef.framework.auth.AuthRole;
import org.mef.framework.commands.Command;
import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.EditCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.commands.NewCommand;
import org.mef.framework.commands.ShowCommand;
import org.mef.framework.commands.UpdateCommand;
import org.mef.framework.presenters.Presenter;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxContext;

import mef.core.DaoFinder;
import mef.core.Initializer;
import mef.daos.IAuthRoleDAO;
import mef.daos.IAuthRuleDAO;
import mef.daos.IBlogDAO;
import mef.daos.mocks.MockAuthSubjectDAO;
import mef.daos.mocks.MockBlogDAO;
import mef.entities.AuthRule;
import mef.entities.AuthSubject;
import mef.entities.Blog;
import mef.presenters.BlogPresenter;
import mef.presenters.commands.IndexBlogCommand;
import mef.presenters.replies.BlogReply;
import org.mef.framework.test.helpers.MockFormBinder;

public class BlogPresenterTests extends BasePresenterTest
{

	//HTTP  URL             ACTION  FRM    VIEW/REDIR     
	//GET 	users/    		index          INDEX
	//GET 	users/new 		new            NEW
	//POST  users/new   	create  form   r:index, NEW(if validation fails)
	//GET   users/:id/edit  edit           EDIT, NOTFOUND(if invalid id)
	//POST  users/:id/edit  update  form   r:index, NOTFOUND(if invalid id), EDIT(if validation fails)
	//POST 	users/:id/delete delete		   r:index, NOTFOUND(if invalid id)
	//GET   users/:id       show           SHOW,NOTFOUND

	//--- index ---
	@Test
	public void indexTest() 
	{
		buildSubjects();
		AuthSubject subj = this._authSubjDAO.all().get(0);
		buildRoles(subj);
		IndexBlogCommand cmd = new IndexBlogCommand(subj);
		BlogReply reply = (BlogReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.VIEW_INDEX, null);
		chkDalSize(0);
		chkReplyWithoutEntity(reply, true, 0);
	}

	@Test
	public void indexTestOne() 
	{
		buildSubjects();
		AuthSubject subj = this._authSubjDAO.all().get(0);
		buildRoles(subj);

		Blog u = createBlog("bob");
		_dao.save(u);
		assertEquals("bob", _dao.all().get(0).name);
		IndexBlogCommand cmd = new IndexBlogCommand(subj);
		BlogReply reply = (BlogReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.VIEW_INDEX, null);
		chkDalSize(1);
		chkReplyWithoutEntity(reply, true, 1);
	}

	//--- new ---
	@Test
	public void testNewBlog() 
	{
		BlogReply reply = (BlogReply) _presenter.process( new NewCommand());

		chkReplySucessful(reply, Reply.VIEW_NEW, null);
		chkDalSize(0);
		chkReplyWithEntity(reply);
	}

	//--- create ---
	@Test
	public void testCreateBlog() 
	{
		Blog t = initBlog();
		chkDalSize(0);
		Command cmd = createWithBinder(new CreateCommand(), t, true);

		BlogReply reply = (BlogReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.FORWARD_INDEX, "created entity");
		chkDalSize(1);
		chkReplyForwardOnly(reply);
		t = _dao.findById(1);
		assertEquals(new Long(1L), t.id);
	}

	@Test
	public void testCreateBlog_ValFail() 
	{
		Blog t = initBlog();
		Command cmd = createWithBinder(new CreateCommand(), t, false);

		BlogReply reply = (BlogReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.VIEW_NEW, "binding failed!");
		chkDalSize(0);
		chkReplyWithEntity(reply);
	}

	//--- edit ---
	@Test
	public void testEditBlog() 
	{
		Blog t = initAndSaveBlog();
		BlogReply reply = (BlogReply) _presenter.process(new EditCommand(t.id));

		chkReplySucessful(reply, Reply.VIEW_EDIT, null);
		chkDalSize(1);
		chkReplyWithEntity(reply);
	}
	@Test
	public void testEditBlog_NotFound() 
	{
		Blog t = initAndSaveBlog();
		BlogReply reply = (BlogReply) _presenter.process(new EditCommand(99L));

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, null);
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}

	//--- update ---
	@Test
	public void testUpdateBlog() 
	{
		Blog t = initAndSaveBlog();
		chkDalSize(1);
		t.name = "user2"; //simulate user edit
		Command cmd = createWithBinder(new UpdateCommand(t.id), t, true);

		BlogReply reply = (BlogReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(1);
		chkReplyForwardOnly(reply);

		Blog t2 = _dao.findById(t.id);
		assertEquals("user2", t2.name);
	}
	@Test
	public void testUpdateBlog_ValFail() 
	{
		Blog t = initAndSaveBlog();
		t.name = "user2"; //simulate user edit
		Command cmd = createWithBinder(new UpdateCommand(t.id), t, false);

		BlogReply reply = (BlogReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.VIEW_EDIT, "binding failed!");
		chkDalSize(1);
		chkReplyWithEntity(reply);

		Blog t2 = _dao.findById(t.id);
		assertEquals("bob", t2.name); //unchanged (but mock dal kinda broken)
	}
	@Test
	public void testUpdateBlog_NotFound() 
	{
		Blog t = initAndSaveBlog();
		Command cmd = createWithBinder(new UpdateCommand(99L), t, true);
		BlogReply reply = (BlogReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, null);
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}


	//--- delete ---
	@Test
	public void testDeleteBlog() 
	{
		Blog t = initAndSaveBlog();
		BlogReply reply = (BlogReply) _presenter.process( new DeleteCommand(t.id));

		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(0);
		chkReplyForwardOnly(reply);
	}

	@Test
	public void testBadDeleteBlog() 
	{
		Blog t = initAndSaveBlog();
		BlogReply reply = (BlogReply) _presenter.process(new DeleteCommand(99L)); //not exist

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, "could not find entity");
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}

	//--- show ---
	@Test
	public void testShowBlog() 
	{
		Blog t = initAndSaveBlog();
		BlogReply reply = (BlogReply) _presenter.process(new ShowCommand(t.id));

		chkReplySucessful(reply, Reply.VIEW_SHOW, null);
		chkDalSize(1);
		chkReplyWithEntity(reply);
	}
	@Test
	public void testShowBlog_NotFound() 
	{
		Blog t = initAndSaveBlog();
		BlogReply reply = (BlogReply) _presenter.process(new ShowCommand(99L));

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, "could not find entity");
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}


	//--------- helper fns--------------
	protected void chkDalSize(int expected)
	{
		assertEquals(expected, _dao.size());
	}
	private void chkReplyWithEntity(BlogReply reply)
	{
		chkReplyWithEntity(reply, false, 0);
	}
	private void chkReplyWithEntity(BlogReply reply, boolean listExists, int expected)
	{
		assertNotNull(reply._entity);
		if (listExists)
		{
			assertNotNull(reply._allL);
			assertEquals(expected, reply._allL.size());
		}
		else
		{
			assertNull(reply._allL);
		}
	}
	private void chkReplyWithoutEntity(BlogReply reply, boolean listExists, int expected)
	{
		assertEquals(null, reply._entity);
		if (listExists)
		{
			assertNotNull(reply._allL);
			assertEquals(expected, reply._allL.size());
		}
		else
		{
			assertNull(reply._allL);
		}
	}
	private void chkReplyForwardOnly(BlogReply reply)
	{
		assertEquals(null, reply._entity);
		assertNull(reply._allL);
	}

	private MockBlogDAO _dao;
	private MockAuthSubjectDAO _authSubjDAO;
	private BlogPresenter _presenter;
	@Before
	public void init()
	{
		super.init();
		_dao = getDAO();
		_authSubjDAO = (MockAuthSubjectDAO) DaoFinder.getAuthSubjectDao();
		//Initializer.loadSeedData(Initializer.theCtx);
		
		this._presenter = new BlogPresenter(_ctx);
	}

	private MockBlogDAO getDAO()
	{
		MockBlogDAO dal = (MockBlogDAO) DaoFinder.getBlogDao(); 
		return dal;
	}

	private Blog initBlog()
	{
		return initBlog("bob");
	}
	private Blog initBlog(String name)
	{
		Blog t = new Blog();
		t.id = 0L; //dal will assign id
		t.name = name;
		assertEquals(0, _dao.size());
		return t;
	}

	private Blog initAndSaveBlog()
	{
		return initAndSaveBlog("bob");
	}

	private Blog initAndSaveBlog(String name)
	{
		Blog t = initBlog(name);
		_dao.save(t);
		assertEquals(1, _dao.size());
		return _dao.findById(t.id);
	}
	private Blog createBlog(String name)
	{
		Blog u = new Blog();
		u.name = name;
		return u;
	}

	protected Command createWithBinder(Command cmd, Blog t, boolean bindingIsValid)
	{
		MockFormBinder binder = new MockFormBinder(t);
		cmd.setFormBinder(binder);
		binder.isValid = bindingIsValid;
		return cmd;
	}

	private void buildSubjects()
	{
		AuthSubject subj = new AuthSubject("alice", 0L);
		_authSubjDAO.save(subj);
		subj = new AuthSubject("bob", 0L);
		_authSubjDAO.save(subj);
		
		assertEquals(2, _authSubjDAO.size());
	}

	private void buildRoles(AuthSubject subj)
	{
		IAuthRoleDAO roleDao = DaoFinder.getAuthRoleDao();
		
		AuthRole role = new AuthRole("Viewer");
		roleDao.save(role);
		role = new AuthRole("Full");
		roleDao.save(role);
		
		assertEquals(2, roleDao.size());
		
		IAuthRuleDAO ruleDao = DaoFinder.getAuthRuleDao();
		AuthRule rule = new AuthRule(subj, role, null);
		ruleDao.save(rule);
		
	}



}
