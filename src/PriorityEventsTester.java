// TODO File header

import java.util.NoSuchElementException;

/**
 * Tester class for the CS300 P10 Priority Events project. You may add tester methods to this class
 * but they must be declared private; the existing public tester methods may use the output of these
 * private testers to help determine their output (as with testAddEvent or testCompleteEvent).
 */
public class PriorityEventsTester {
  
  /**
   * This method runs all sub-testers related to testing adding an Event to the priority queue.
   * You may wish to add additional output for clarity, or additional private tester methods related
   * to adding Events.
   * @return true if all tests relating to adding an Event to a priority queue pass; false otherwise
   */
  public static boolean testAddEvent() {
    boolean testAdd = true;
    testAdd &= testAddEventChronological();
    testAdd &= testAddEventAlphabetical();
    return testAdd;
  }
  
  private static boolean testAddEventChronological() {
      PriorityEvents.sortChronologically();
      PriorityEvents queue = new PriorityEvents(5);
      Event e1 = new Event("Sleep", 15, 22, 30);
      Event e2 = new Event("Dinner", 15, 20, 0);
      Event e3 = new Event("Workout", 14, 10, 0);
      queue.addEvent(e1);
      queue.addEvent(e2);
      queue.addEvent(e3);
      return queue.peekNextEvent().equals(e3); // earliest time
  }
  
  private static boolean testAddEventAlphabetical() {
      PriorityEvents.sortAlphabetically();
      PriorityEvents queue = new PriorityEvents(5);
      Event a = new Event("Zoom call", 10, 9, 0);
      Event b = new Event("Breakfast", 10, 8, 0);
      Event c = new Event("Emails", 10, 10, 0);
      queue.addEvent(a);
      queue.addEvent(b);
      queue.addEvent(c);
      return queue.peekNextEvent().equals(b); // "Breakfast" is alphabetically first
  }
  
  /**
   * This method runs all sub-testers related to testing marking an Event in the priority queue as
   * completed. You may wish to add additional output for clarity, or additional private tester 
   * methods related to marking Events as completed.
   * @return true if all tests relating to removing an Event from a priority queue pass; false 
   * otherwise
   */
  public static boolean testCompleteEvent() {
    boolean testComplete = true;
    testComplete &= testCompleteEventChronological();
    testComplete &= testCompleteEventAlphabetical();
    return testComplete;
  }
  
  private static boolean testCompleteEventChronological() {
    PriorityEvents.sortChronologically();
    PriorityEvents queue = new PriorityEvents(3);
    Event e1 = new Event("Study", 12, 12, 0);
    Event e2 = new Event("Class", 12, 10, 0);

    queue.addEvent(e1);
    queue.addEvent(e2);
    queue.completeEvent(); // should complete e2

    Event[] completed = queue.getCompletedEvents();
    return completed.length == 1 && completed[0].equals(e2) && completed[0].isComplete();
  }
  
  private static boolean testCompleteEventAlphabetical() {
    PriorityEvents.sortAlphabetically();
    PriorityEvents queue = new PriorityEvents(3);
    Event e1 = new Event("Beta", 10, 10, 0);
    Event e2 = new Event("Alpha", 10, 10, 0);

    queue.addEvent(e1);
    queue.addEvent(e2);
    queue.completeEvent(); // should complete "Alpha"

    Event[] completed = queue.getCompletedEvents();
    return completed.length == 1 && completed[0].equals(e2) && completed[0].isComplete();
  }
  
  /**
   * Verifies the peekNextEvent() method. You may wish to break this out into smaller sub-testers.
   * @return true if all tests pass; false otherwise
   */
  public static boolean testPeek() {
    {
      PriorityEvents.sortChronologically();
      PriorityEvents queue = new PriorityEvents(2);
      Event a = new Event("First", 10, 9, 0);
      Event b = new Event("Second", 10, 10, 0);
      queue.addEvent(b);
      queue.addEvent(a);
      if ((!queue.peekNextEvent().equals(a) && queue.size() == 2)) {
        return false;
      }
    }
    {
      PriorityEvents.sortChronologically();
      PriorityEvents queue = new PriorityEvents(2);
      try {
        queue.peekNextEvent();
        return false; // should throw exception
      } catch (NoSuchElementException e) {
      }
    }
    return true;
  }
  
  /**
   * Verifies the overloaded PriorityEvents constructor that creates a valid heap from an input
   * array of values. You may wish to break this out into smaller sub-testers.
   * @return true if all tests pass; false otherwise
   */
  public static boolean testHeapify() {
    PriorityEvents.sortChronologically();
    Event[] input = {
      new Event("Z", 15, 12, 0),
      new Event("A", 12, 8, 0),
      new Event("M", 13, 9, 0)
    };
    PriorityEvents queue = new PriorityEvents(input, 3);
    return queue.peekNextEvent().equals(input[1]); // "A" is earliest
  }

  public static void main(String[] args) {
    System.out.println("ADD: "+testAddEvent());
    System.out.println("COMPLETE: "+testCompleteEvent());
    System.out.println("PEEK: "+testPeek());
    System.out.println("HEAPIFY: "+testHeapify());
  }

}
