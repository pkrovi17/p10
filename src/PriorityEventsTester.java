// TODO File header

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
    testAdd &= testAddInvalidEvent();
    testAdd &= testAddUpdatesSize();
    testAdd &= testHeapOrderNotSorted();
    return testAdd;
  }
  /*
   * This method tests the addEvent() method by adding a invalid Event to the queue and checking for errors
   * @return true if the exception is thrown; false otherwise
   */
  private static boolean testAddInvalidEvent() {
    try {
      PriorityEvents queue = new PriorityEvents(1);
      queue.addEvent(null); // should throw
      return false; // If it doesn’t throw, that’s a fail
    } catch (IllegalArgumentException e) {
      return true; // Correct
    } catch (Exception e) {
      return false; // Wrong exception
    }
  }
  /*
   * This method tests the addEvent() method by adding a valid Event to the queue and checking if the size is updated
   * @return true if the size is updated correctly; false otherwise
   */
  private static boolean testAddUpdatesSize() {
      PriorityEvents queue = new PriorityEvents(3);
      queue.addEvent(new Event("One", 10, 9, 0));
      queue.addEvent(new Event("Two", 10, 10, 0));
      queue.addEvent(new Event("Three", 10, 11, 0));
      return queue.size() == 3;
  }
  /**
   * This method tests the addEvent() method by adding a valid Event to the queue and checking if the heap order is not sorted
   * @return true if the heap order is not sorted; false otherwise
   */
  private static boolean testHeapOrderNotSorted() {
      PriorityEvents.sortChronologically();
      PriorityEvents queue = new PriorityEvents(3);
      Event e1 = new Event("C", 12, 12, 0); // last
      Event e2 = new Event("A", 10, 10, 0); // first
      Event e3 = new Event("B", 11, 11, 0); // middle
      queue.addEvent(e1);
      queue.addEvent(e2);
      queue.addEvent(e3);
      Event top = queue.peekNextEvent();
      if (!top.equals(e2)) return false;
      Event[] data = queue.getHeapData();
      return !(data[0].equals(e2) && data[1].equals(e3) && data[2].equals(e1)); // reject sorted order
  }
  /*
   * This method tests the addEvent() method by adding a valid Event to the queue and 
   * checking if the earliest time is at the top of the heap
   * @return true if the earliest time is at the top of the heap; false otherwise
   */
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
  /*
   * This method tests the addEvent() method by adding a valid Event to the queue and 
   * checking if the alphabetically first event is at the top of the heap
   * @return true if the alphabetically first event is at the top of the heap; false otherwise
   */
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
    testComplete &= testCompleteEventInvalid(); // invalid event
    testComplete &= testCompleteIsFull(); // full array
    testComplete &= testCompleteEventNotSortedQueue(); // not sorted queue
    testComplete &= testCompleteEventChronological();
    testComplete &= testCompleteEventAlphabetical();
    return testComplete;
  }

  /*
   * This method tests the completeEvent() method by checking if the completed events array is full
   * @return true if the completed event is correct; false otherwise
   */
  private static boolean testCompleteIsFull() {
    try {
      PriorityEvents queue = new PriorityEvents(1); // completed will be size 2
      queue.addEvent(new Event("One", 10, 10, 0));
      queue.completeEvent();
      queue.addEvent(new Event("Two", 10, 11, 0));
      queue.completeEvent();
      queue.addEvent(new Event("Three", 10, 12, 0));
      queue.completeEvent(); // should throw now, completed is full
      return false;
    } catch (IllegalStateException e) {
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  /*
   * This method tests the completeEvent() method by completing an event that does not exist in the queue
   * @return true if the completed event is correct; false otherwise
   */
  private static boolean testCompleteEventInvalid() {
      try {
        PriorityEvents queue = new PriorityEvents(2);
        queue.completeEvent(); // should throw
        return false; // if no exception, test fails
      } catch (IllegalStateException e) {
        return true;
      } catch (Exception e) {
        return false;
      }
  }
  /*
   * This method tests the completeEvent() method by completing an event that is not sorted in the queue
   * @return true if the completed event is correct; false otherwise
   */
  private static boolean testCompleteEventNotSortedQueue() {
    try {
      PriorityEvents queue = new PriorityEvents(3);
      Event e1 = new Event("C", 10, 12, 0);
      Event e2 = new Event("A", 10, 10, 0);
      Event e3 = new Event("B", 10, 11, 0);
      queue.addEvent(e1);
      queue.addEvent(e2);
      queue.addEvent(e3);
      PriorityEvents.sortChronologically();
      queue.sortChronologically(); // sort the queue in chronological order
      queue.completeEvent(); // should complete e2 (earliest)
      Event[] completed = queue.getCompletedEvents();
      // check that queue is not fully sorted internally (heap only keeps min at root)
      Event[] heap = queue.getHeapData();
      boolean fullySorted = heap[0] != null && heap[1] != null && heap[2] != null
                            && heap[0].compareTo(heap[1]) <= 0 && heap[1].compareTo(heap[2]) <= 0;
      return !fullySorted; // we expect not fully sorted
    } catch (Exception e) {
      return false;
    }
  }
  
  /*
   * This method tests the completeEvent() method by completing an event in chronological order 
   * and checking if the completed event is correct
   * @return true if the completed event is correct; false otherwise
   */
  private static boolean testCompleteEventChronological() {
    return false; // TODO
  }
  
  private static boolean testCompleteEventAlphabetical() {
    return false; // TODO
  }
  
  /**
   * Verifies the peekNextEvent() method. You may wish to break this out into smaller sub-testers.
   * @return true if all tests pass; false otherwise
   */
  public static boolean testPeek() {
    return false; // TODO
  }
  
  /**
   * Verifies the overloaded PriorityEvents constructor that creates a valid heap from an input
   * array of values. You may wish to break this out into smaller sub-testers.
   * @return true if all tests pass; false otherwise
   */
  public static boolean testHeapify() {
    return false; // TODO
  }

  public static void main(String[] args) {
    System.out.println("ADD: "+testAddEvent());
    System.out.println("COMPLETE: "+testCompleteEvent());
    System.out.println("PEEK: "+testPeek());
    System.out.println("HEAPIFY: "+testHeapify());
  }

}
