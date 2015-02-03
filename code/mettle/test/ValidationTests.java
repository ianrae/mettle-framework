import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import play.data.format.*;

public class ValidationTests
{
	public static class TimeFormatter extends Formatters.SimpleFormatter<LocalTime>
	{
	    private Pattern timePattern = Pattern.compile(
	            "([012]?\\d)(?:[\\s:\\._\\-]+([0-5]\\d))?"
	    );

	    @Override
	    public LocalTime parse(String input, Locale l) throws ParseException {
	        Matcher m = timePattern.matcher(input);
	        if (!m.find()) throw new ParseException("No valid Input", 0);
	        int hour = Integer.valueOf(m.group(1));
	        int min = m.group(2) == null ? 0 : Integer.valueOf(m.group(2));
	        return new LocalTime(hour, min);
	    }

	    @Override
	    public String print(LocalTime localTime, Locale l) {
	        return localTime.toString("HH:mm");
	    }
	}
	@Test
	public void test() throws ParseException 
	{
		TimeFormatter xx = new TimeFormatter();
		Locale locale = LocaleContextHolder.getLocale();
		assertNotNull(locale);
		Locale l2 = Locale.getDefault();
		LocalTime time = xx.parse("11:30", Locale.getDefault());
		assertEquals(11, time.getHourOfDay());
		
		String s = xx.print(time, Locale.getDefault());
		assertEquals("11:30", s);
	}

}
