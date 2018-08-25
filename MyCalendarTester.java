import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * HW1: To create a calendar like the one in the phone. Models a tester class to
 * let user have several options to choose such as view day, create events,
 * etc., when using the calendar program.
 * 
 * @author yen_my_huynh 09/16/2017
 */
public class MyCalendarTester {
	/**
	 * Main method to test out codes.
	 * @param args no argument is taken this time, mainly Scanner is in used
	 * @throws ParseException throws when parsing format is wrong
	 * @throws FileNotFoundException throws when a file is not found
	 * @throws IOException throws IOException 
	 */
	public static void main(String[] args)
			throws ParseException, FileNotFoundException, IOException{
		MyCalendar cal = new MyCalendar();
		Scanner scan = new Scanner(System.in);
		while (true) {
			cal.printMainMenu();
			cal.resetTime();
			String option = scan.nextLine().toUpperCase();

			// when user chooses to load an existing file.
			if (option.equals("L")) {
				cal.load();

				// when users chooses to view day or month.
			} else if (option.equals("V")) {
				System.out.println("[D]ay view or [M]onth view ?");
				String viewOpt = scan.nextLine().toUpperCase();
				if (viewOpt.equals("D")) {
					System.out.println("Current date:");
					cal.viewDay();
					System.out.println();
					System.out.println("[P]revious or [N]ext or [M]ain menu ?");
					// the program will keep running unless selecting "Q" to
					// quit.
					while (true) {
						String viewNextOpt = scan.nextLine().toUpperCase();
						if (viewNextOpt.equals("P")) {
							System.out.println("Previous date:");
							cal.changeDay(viewNextOpt);
							System.out.println();
							System.out.println("[P]revious or [N]ext or [M]ain menu ?");
						} else if (viewNextOpt.equals("N")) {
							System.out.println("Next date:");
							cal.changeDay(viewNextOpt);
							System.out.println();
							System.out.println("[P]revious or [N]ext or [M]ain menu ?");
						} else if (viewNextOpt.equals("M")) {
							break;
						} else {
							System.out.println();
							System.out.println("Please enter a valid letter.");
						}
					}

					// when user chooses to go back to the main menu.
				} else if (viewOpt.equals("M")) {
					cal.viewMonth();
					System.out.println();
					System.out.println("[P]revious or [N]ext or [M]ain menu ?");
					while (true) {
						String viewNextOpt = scan.nextLine().toUpperCase();
						// when user chooses to go back a day or month.
						if (viewNextOpt.equals("P")) {
							cal.changeMonth(viewNextOpt);
							System.out.println();
							System.out.println("[P]revious or [N]ext or [M]ain menu ?");
							// when user chooses to go forth a day or month.
						} else if (viewNextOpt.equals("N")) {
							cal.changeMonth(viewNextOpt);
							System.out.println();
							System.out.println("[P]revious or [N]ext or [M]ain menu ?");
						} else if (viewNextOpt.equals("M")) {
							break;
						} else {
							System.out.println("Please enter a valid letter.");
							viewNextOpt = scan.nextLine();
						}
					}
				} else {
					System.out.println("Please enter a valid letter.");
					viewOpt = scan.nextLine();
				}

				// when user chooses to create an event.
			} else if (option.equals("C")) {
				String pattern = "MM/dd/yyyy HH:mm";
				System.out.println("To create an event, enter a name for it: ");
				String name = scan.nextLine().toUpperCase();

				System.out.println("Enter a date for the event in MM/DD/YYYY format: ");
				String date = scan.nextLine();
				if (date.length() < 8 || !(date.contains("/"))){
					System.out.println("Re-Enter a date in MM/DD/YYYY format:");
					date = scan.nextLine();
				}
				String[] splash = date.split("/");
				int yearInNum = Integer.parseInt(splash[2]);
				int monthInNum = Integer.parseInt(splash[0]);
				if (monthInNum - 1 >= 0)
					monthInNum -= 1;
				int dInNum = Integer.parseInt(splash[1]);
				System.out.println("Enter the starting time and/ or the ending time in military time: [Separate by ';'"
						+ "'e.g 11:00; 18:00']");
				System.out.println("If no ending time, then don't enter ';', e.g [11:00]");
				String times = scan.nextLine();
				if (times.length() < 4 || !(times.contains(":"))){
					System.out.println("Re-Enter the starting time and/ or the ending time in military time: [Separate by ';'"
							+ "'e.g 11:00; 18:00']");
					System.out.println("If no ending time, then don't enter ';', e.g [11:00]");
					times = scan.nextLine();
				}
				String[] semicolon = times.split("; ");
				String startTime = semicolon[0];
				String endTime;
				if (semicolon.length > 1 && semicolon.length <= 2) {
					endTime = semicolon[1];
				} else {
					endTime = "23:59";
				}
				String[] wholeDate = new String[2];
				wholeDate[0] = date + " " + startTime;
				wholeDate[1] = date + " " + endTime;
				System.out.println("Start time: " + wholeDate[0]);
				if (endTime.equals("23:59")) {
					System.out.println(
							"End time: " + date + " " + "No ending time, other events of this date will be cancelled.");
				} else {
					System.out.println("End time: " + wholeDate[1]);
				}
				Date[] dateHolder = new Date[2];
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				for (int i = 0; i < wholeDate.length; i++) {
					Date dateParse = simpleDateFormat.parse(wholeDate[i]);

					if (wholeDate[1] == null) {
						dateParse = simpleDateFormat.parse(wholeDate[0]);
					}
					dateHolder[i] = dateParse;
				}
				Calendar day = new GregorianCalendar(yearInNum, monthInNum, dInNum);
				Event event = new Event(name, day, dateHolder);
				cal.createEvent(day, event);

				// when user chooses to check certain date for any events.
			} else if (option.equals("G")) {
				System.out.println("Enter a date in MM/DD/YYYY format:");
				String date = scan.nextLine();
				if (date.length() < 8 || !(date.contains("/"))){
					System.out.println("Re-Enter a date in MM/DD/YYYY format:");
					date = scan.nextLine();
				}
				String[] splash = date.split("/");
				int yearInNum = Integer.parseInt(splash[2]);
				int monthInNum = Integer.parseInt(splash[0]);
				if (monthInNum - 1 >= 0)
					monthInNum -= 1;
				int dInNum = Integer.parseInt(splash[1]);
				Calendar day = new GregorianCalendar(yearInNum, monthInNum, dInNum);
				cal.viewEventFromDay(day);
			} else if (option.equals("E")) {
				cal.printEventList();

				// when user wants to delete events.
			} else if (option.equals("D")) {
				System.out.println("[S]elected or [A]ll ? ");
				String viewOpt = scan.nextLine().toUpperCase();
				// when user select a certain date to delete all events of that
				// date.
				if (viewOpt.equals("S")) {
					System.out.println("Enter the date in MM/DD/YYYY format: ");
					String date = scan.nextLine();
					if (date.length() < 8 || !(date.contains("/"))){
						System.out.println("Re-Enter a date in MM/DD/YYYY format:");
						date = scan.nextLine();
					}
					String[] splash = date.split("/");
					int yearInNum = Integer.parseInt(splash[2]);
					int monthInNum = Integer.parseInt(splash[0]);
					if (monthInNum - 1 >= 0)
						monthInNum -= 1;
					int dInNum = Integer.parseInt(splash[1]);
					Calendar day = new GregorianCalendar(yearInNum, monthInNum, dInNum);
					cal.deleteEvent(day);
					// when user chooses to delete all event.s on the calendar.
				} else if (viewOpt.equals("A")) {
					cal.clearEvents();
				}

				// when user chooses to quit and save all events.
			} else if (option.equals("Q")) {
				cal.quit();
				break;
			}
		}
		scan.close();
	}
}