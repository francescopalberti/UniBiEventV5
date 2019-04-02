package it.unibs;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Main {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		Data dataOdierna = new Data((int)calendar.get(Calendar.DATE), ((int)calendar.get(Calendar.MONTH)+1), (int)calendar.get(Calendar.YEAR));
		Ora oraAttuale = new Ora((int)calendar.get(Calendar.HOUR), (int)calendar.get(Calendar.MINUTE));
	
		Application anApplication = new Application(dataOdierna, oraAttuale);
		anApplication.runApplication();

	}
}
