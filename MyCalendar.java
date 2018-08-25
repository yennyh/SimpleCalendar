
import java.util.TreeMap;
import java.util.TreeSet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * HW1: To create a calendar like the one in the phone.
 * Models a calendar class to load, view dates, create events, go to events,
 * delete and save events.
 * @author yen_my_huynh 09/16/2017
 */

public class MyCalendar implements Serializable {

	// constants for event list view.
	enum MONTHS {
		Jan, Feb, March, Apr, May, June, July, Aug, Sep, Oct, Nov, Dec;
	}
	// constants for months view.
	enum MONTHSVIEW {
		January, February, March, April, May, June, July, August, September, October, November, December;
	}
	// constants for event list view.
	enum DAYS {
		Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
	}
	// constants for days view.
	enum DAYSVIEW {
		Su, Mo, Tu, We, Th, Fr, Sa;
	}

	private Calendar cal;
	private TreeMap<Calendar, TreeSet<Event>> eventList;

	/** 
	 * Constructs a new calendar and a map to save all events.
	 */
	public MyCalendar() {
		cal = new GregorianCalendar();
		cal = Calendar.getInstance();
		eventList = new TreeMap<>();
	}

	/**
	 * Starts the current date when the program runs.
	 * @param month the current month
	 * @param day the current day
	 * @param year the current year
	 * @return the current date.
	 */
	public int startMonth(int month, int day, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.YEAR, year);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return dayOfWeek;
	}

	/**
	 * Resets the time to current time/date.
	 */
	public void resetTime() {
		this.cal = new GregorianCalendar();
	}

	/** 
	 * Prints the main menu with several options.
	 */
	public void printMainMenu() {
		System.out.println("Select one of the following options: ");
		System.out.println("[L]oad   [V]iew by  [C]reate [G]o to [E]vent list [D]elete  [Q]uit");
		System.out.println();
	}

	/**
	 * Checks if it is a leap year or not.
	 * @param year the given year
	 * @return true if leap year, if not, then false
	 */
	public boolean isLeapYear(int year) {
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the new event overlaps with other existing events.
	 * @param e1 the new event to be add
	 * @param e2 the existing event in the list
	 * @return true if they overlap, if not, return false
	 */
	public boolean isOverLapping(Event e1, Event e2) {
		Date[] times1 = e1.getDate();
		Date startTime1 = times1[0];
		Date endTime1 = times1[1];
		Date[] times2 = e2.getDate();
		Date startTime2 = times2[0];
		Date endTime2 = times2[1];

		return ((null == endTime2 || startTime1.before(endTime2)))
				&& ((null == endTime1 || startTime2.before(endTime1)));
	}

	/**
	 * First time loading will notify user that there's no text file,
	 * after running the first time and then saved data using 'quit()' 
	 * later. 
	 * When user loads for a second time after using 'quit()', pre-existing 
	 * events will appear.
	 * @throws IOException throws IOException
	 * @throws FileNotFoundException throws if file not found
	 */
	public void load() throws FileNotFoundException, IOException {
		ObjectInputStream fis = null;
		try {
			fis = new ObjectInputStream(new FileInputStream("events.txt"));
		} catch (IOException e) {
			eventList.clear();
		}
		try {
			if (fis != null) {
				FileInputStream streamIn = new FileInputStream("events.txt");
				fis = new ObjectInputStream(streamIn);
				TreeMap<Calendar, TreeSet<Event>> list = (TreeMap<Calendar, TreeSet<Event>>) fis
						.readObject();
				eventList = list;
				System.out.println("The 'events.txt' file is loaded.");
			} else {
				System.out.println("There is no such 'events.txt' file. This is the first run.\n"
						+ "To save all events later, choose 'Quit' option.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Views the day in weekly format with day, month, date, and year.
	 * If there is any events happening on that day, it will show a list of events 
	 * in order.
	 */
	public void viewDay() {
		MONTHS[] monthsArr = MONTHS.values();
		DAYS[] daysArr = DAYS.values();
		Calendar today = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		System.out.println(daysArr[cal.get(Calendar.DAY_OF_WEEK) - 1] + ", " + monthsArr[cal.get(Calendar.MONTH)] + " "
				+ cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR));
		if (eventList.containsKey(today)) {
			for (Event e : eventList.get(today)) {
				e.printEventFromDayView();
			}
		}
	}

	/**
	 * Views the month in monthly format with month, year, and days with dates below.
	 * If there are any events happening on that month, '{}' will wrap around those dates.
	 */
	public void viewMonth() {
		MONTHSVIEW[] monthsArr = MONTHSVIEW.values();
		DAYSVIEW[] daysArr = DAYSVIEW.values();
		int[] days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		String[] monthNames = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		System.out.print(monthsArr[cal.get(Calendar.MONTH)]);
		System.out.print(" ");
		System.out.print(cal.get(Calendar.YEAR) + "\n");

		for (int i = 0; i < 7; i++) {
			System.out.printf("%6s", daysArr[i]);
		}
		System.out.print("\n");
		if (monthNames[cal.get(Calendar.MONTH)].equals("February")
				&& ((GregorianCalendar) cal).isLeapYear(cal.get(Calendar.YEAR))) {
			days[1] = 29;
		}
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int totalDays = days[month];
		int firstDayOfMonth = startMonth(month, 1, year);
		for (int i = 0; i < firstDayOfMonth; i++) {
			System.out.printf("%6s", "");
		}
		for (int j = 1; j <= totalDays; j++) {
			Calendar today = new GregorianCalendar(year, month, j);

			if (eventList.containsKey(today)) {
				System.out.printf("%3s%d%s", "{", j, "}");
			} else {
				System.out.printf("%6d", j);
			}

			if ((j + firstDayOfMonth) % 7 == 0 || j == totalDays) {
				System.out.println();
			}
		}
	}

	/**
	 * Changes the month by going backward or forward on the calendar.
	 * @param viewNextOpt takes in the current month to change
	 */
	public void changeMonth(String viewNextOpt) {
		if (viewNextOpt.equals("P")) {
			System.out.println();
			cal.add(Calendar.MONTH, -1);
			viewMonth();
		} else if (viewNextOpt.equals("N")) {
			System.out.println();
			cal.add(Calendar.MONTH, 1);
			viewMonth();
		}
	}

	/**
	 * Changes the day by going backward or forward on the calendar. 
	 * @param viewNextOpt takes in the current day to change
	 */
	public void changeDay(String viewNextOpt) {
		if (viewNextOpt.equals("P")) {
			cal.add(Calendar.DAY_OF_WEEK, -1);
			viewDay();
		} else if (viewNextOpt.equals("N")) {
			cal.add(Calendar.DAY_OF_WEEK, 1);
			viewDay();
		}
	}

	/**
	 * Creates an event and checks if there are existing events.
	 * @param day the day of the event to be added
	 * @param e the existing event in the map if any
	 */
	public void createEvent(Calendar day, Event e) {
		if (this.eventList.get(day) == null) {
			TreeSet<Event> eventHolder = new TreeSet<>();
			eventHolder.add(e);
			eventList.put(day, eventHolder);
			System.out.println("Your event has been saved.");
			System.out.println();
		} else {
			try {
				for (Event event : this.eventList.get(day)) {
					if (isOverLapping(e, event)) {
						System.out.println("There's already an existing event during this time. Choose another date/time.");
						System.out.println();
						return;
					}
				}
			} catch (NullPointerException x) {
				System.out.println("The event list is empty.");
				System.out.println();
			}
		}
		this.eventList.get(day).add(e);
	}

	/**
	 * Gets an ordered event list.
	 * @return the event list in order
	 */
	public TreeMap<Calendar, TreeSet<Event>> getEventList() {
		return eventList;
	}

	/**
	 * Views only events from this date.
	 * @param day the day to check if there are any events
	 */
	public void viewEventFromDay(Calendar day) {
		if (eventList.get(day) != null) {
			for (Event e : eventList.get(day)) {
				e.printEventFromDayView();
				System.out.println();
			}
		}
	}

	/**
	 * Prints out the list of all events.
	 */
	public void printEventList() {
		for (Calendar day : this.getEventList().keySet()) {
			System.out.println(day.get(Calendar.YEAR));
			for (Event e : eventList.get(day)) {
				e.printEvent();
				System.out.println();
			}
		}
	}

	/**
	 * Deletes an event from the list.
	 * @param day the day to delete existing events if any
	 */
	public void deleteEvent(Calendar day) {
		eventList.remove(day);
		System.out.println("Everything is clear for this day.");
		System.out.println();
	}

	/**
	 * Clears all events from the list.
	 */
	public void clearEvents() {
		eventList.clear();
		System.out.println("All events have been cleared.");
		System.out.println();
	}

	/**
	 * Quits and saves "events.txt" to populate the calendar the first time.
	 * Quits and saves "event.txt" again when called and added in more events.
	 * @throws IOException throws IOException
	 * @throws FileNotFoundException throws if no file is found
	 */
	public void quit() throws FileNotFoundException, IOException {
		try (FileOutputStream f = new FileOutputStream("events.txt")) {
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(eventList);
			s.close();
			f.close();
			System.out.println("Everything is saved in 'events.txt.' file.");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
}