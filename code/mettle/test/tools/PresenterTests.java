package tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class PresenterTests extends BaseTest
{
	public static class MRequest
	{
		
	}
	public static class MReply
	{
		
	}
	public static class Presenter
	{
		MReply process(MRequest request)
		{
			return null;
		}
	}
	
	public static class PlayResult
	{
		
	}
	
	public static class Boundary
	{
		public PlayResult process(MRequest request, Presenter presenter, Object route)
		{
			MReply res = presenter.process(request);
			if (false)
			{
				return new PlayResult();
			}
			else
			{
				return null;
			}
		}
		
		public Presenter createPresenter()
		{
			return new Presenter();
		}
	}

	@Test
	public void test() 
	{
//		Boundary bb = new Boundary();
//		Presenter presenter = bb.createPresenter();
//		
//		if (bb.process(new MRequest(), presenter, null))
//		{
//			//redirect///
//		}
//		
//		
//		if (result != null)
//		{
//			return ;//result;
//		}
//		return; //views.index.html.render(bb.getReply())
	}

}
