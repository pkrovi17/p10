//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    Priority Queue of Events Testing Class
// Course:   CS 300 Spring 2025
//
// Author:   Pranav Krovi
// Email:    pkrovi@wisc.edu
// Lecturer: Mouna Kacem
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
// 
// Partner Name:    x
// Partner Email:   x
// Partner Lecturer's Name: x
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//   x Write-up states that pair programming is allowed for this assignment.
//   x We have both read and understand the course Pair Programming Policy.
//   x We have registered our team prior to the team registration deadline.
//
//////////////////////// ASSISTANCE/HELP CITATIONS ////////////////////////////
//
// Persons:         Mouna Kacem
//                  The diagram present in the project docs that helped me understand the heap structure better
// Online Sources:  Washington University's Mr Siever's test case guide
//                  - https://siever.info/cs351/hw/prj2/PQTestCases.html
//                  - helped me understand what tests i could run and how to do it efficiently
//
///////////////////////////////////////////////////////////////////////////////
// imports
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

    PriorityEvents queue = new PriorityEvents(3);
    queue.clearCompletedEvents(); // clear completed events
    Event early = new Event("Early", 10, 8, 0);
    Event late = new Event("Late", 10, 18, 0);
    queue.addEvent(late);
    queue.addEvent(early);
    int before = queue.size();
    PriorityEvents.sortChronologically();
    queue.sortChronologically(); // sort the queue in chronological order
    queue.completeEvent(); // should complete "Early"
    if (queue.size() != before - 1) {
      System.out.println("Size mismatch: " + queue.size() + " != " + (before - 1));
      return false;
    }
    Event[] completed = queue.getCompletedEvents();
    if (completed.length != 6) {
      System.out.println("Completed events length mismatch: " + completed.length + " != 6");
      return false;
    }
    Event c = completed[0];
    if (!c.getDescription().equals("Early")) {
      System.out.println("Description mismatch: " + c.getDescription() + " != " + early.getDescription());
      return false;
    }
    if (!c.getStartTimeAsString().equals(early.getStartTimeAsString())) {
      System.out.println("Start time mismatch: " + c.getStartTimeAsString() + " != " + early.getStartTimeAsString());
      return false;
    }
    if (!c.isComplete()) {
      System.out.println("Event not marked as complete: " + c.isComplete() + " != true");
      return false;
    }
    return true;
  }
  /*
   * This method tests the completeEvent() method by completing an event in alphabetical order 
   * and checking if the completed event is correct
   * @return true if the completed event is correct; false otherwise
   */
  private static boolean testCompleteEventAlphabetical() {
    // Cite: Mr. Siever
    // https://siever.info/cs351/hw/prj2/PQTestCases.html

    PriorityEvents queue = new PriorityEvents(3);
    queue.clearCompletedEvents(); // clear completed events
    Event a = new Event("Alpha", 10, 10, 0);
    Event b = new Event("Beta", 10, 12, 0);
    queue.addEvent(b);
    queue.addEvent(a);
    int before = queue.size();
    PriorityEvents.sortAlphabetically();
    queue.sortAlphabetically(); // sort the queue in alphabetical order
    queue.completeEvent(); // should complete "Alpha"
    if (queue.size() != before - 1) {
      System.out.println("Size mismatch: " + queue.size() + " != " + (before - 1));
      return false;
    }
    Event[] completed = queue.getCompletedEvents();
    if (completed.length != 6) {
      System.out.println("Completed events length mismatch: " + completed.length + " != 6");
      return false;
    }
    Event c = completed[0];
    if (!c.getDescription().equals("Alpha")) {
      System.out.println("Description mismatch: " + c.getDescription() + " != " + a.getDescription());
      return false;
    }
    if (!c.getStartTimeAsString().equals(a.getStartTimeAsString())) {
      System.out.println("Start time mismatch: " + c.getStartTimeAsString() + " != " + a.getStartTimeAsString());
      return false;
    }
    if (!c.isComplete()) {
      System.out.println("Event not marked as complete: " + c.isComplete() + " != true");
      return false;
    }
    return true;
  }
  
  /**
   * Verifies the peekNextEvent() method. You may wish to break this out into smaller sub-testers.
   * @return true if all tests pass; false otherwise
   */
  public static boolean testPeek() {
    boolean pass = true;
    pass &= testOriginal();
    pass &= testSecondary();
    return pass;
  }
  /*
   * This method tests the peekNextEvent() method by checking if the next event is correct
   * @return true if the next event is correct; false otherwise
   */
  private static boolean testOriginal(){
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
  /*
   * This method tests the peekNextEvent() method by checking if the next event is correct
   * @return true if the next event is correct; false otherwise
   */
  private static boolean testSecondary() {
    // Cite: Mr. Siever
    // https://siever.info/cs351/hw/prj2/PQTestCases.html
      PriorityEvents.sortChronologically();
      PriorityEvents queue = new PriorityEvents(3);
      Event e1 = new Event("One", 10, 9, 0);
      Event e2 = new Event("Two", 10, 10, 0);
      queue.addEvent(e2);
      queue.addEvent(e1); // e1 should be next
      // peek shouldn't remove it
      Event peeked = queue.peekNextEvent();
      if (!peeked.getDescription().equals("One")) return false;
      // it should still be at the front
      if (queue.size() != 2) return false; 
      Event peekAgain = queue.peekNextEvent();
      if (!peekAgain.getDescription().equals("One")) return false;
      return true;
  }


  
  /**
   * Verifies the overloaded PriorityEvents constructor that creates a valid heap from an input
   * array of values. You may wish to break this out into smaller sub-testers.
   * @return true if all tests pass; false otherwise
   */
  public static boolean testHeapify() {
    boolean pass = true;
    pass &= testHeapifyChronological();
    pass &= testHeapifyAlphabetical();
    pass &= testHeapifyRejectsCompleted();
    return pass;
  }
  /*
   * This method tests the heapify() method by checking if the next event is correct in chronological order
   * @return true if the next event is correct; false otherwise
   */
  private static boolean testHeapifyChronological() {
    try {
      PriorityEvents.sortChronologically();
      Event[] input = {
        new Event("Z", 15, 12, 0),
        new Event("A", 12, 8, 0),
        new Event("M", 13, 9, 0)
      };
      PriorityEvents queue = new PriorityEvents(input, 3);
      return queue.peekNextEvent().getDescription().equals("A");
    } catch (Exception e) {
      return false;
    }
  }
  /*
   * This method tests the heapify() method by checking if the next event is correct in alphabetical order
   * @return true if the next event is correct; false otherwise
   */
  private static boolean testHeapifyAlphabetical() {
    try {
      PriorityEvents.sortAlphabetically();
      Event[] input = {
        new Event("Zebra", 10, 10, 0),
        new Event("Alpha", 10, 11, 0),
        new Event("Monkey", 10, 12, 0)
      };
      PriorityEvents queue = new PriorityEvents(input, 3);
      return queue.peekNextEvent().getDescription().equals("Alpha");
    } catch (Exception e) {
      return false;
    }
  }
  /*
   * This method tests the heapify() method by checking if the next event is correct
   * @return true if the next event is correct; false otherwise
   */
  private static boolean testHeapifyRejectsCompleted() {
    try {
      Event[] input = {
        new Event("X", 10, 10, 0),
        new Event("Y", 10, 11, 0)
      };
      input[1].markAsComplete();
  
      new PriorityEvents(input, 2); // should throw
      return false;
    } catch (IllegalArgumentException e) {
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static void main(String[] args) {
    System.out.println("ADD: "+testAddEvent());
    System.out.println("COMPLETE: "+testCompleteEvent());
    System.out.println("PEEK: "+testPeek());
    System.out.println("HEAPIFY: "+testHeapify());
  }

}
