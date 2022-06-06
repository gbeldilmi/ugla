package ugla;

import java.io.IOException;

public class Ugla {
  public static String contactListPath;
  public static void main(String[] args) {
    // Get the path to the contact list
    if (args.length != 1) {
      contactListPath = "contacts.csv";
    } else {
      contactListPath = args[args.length - 1];
    }
    // Run the contact manager
    try {
      new ContactManager(contactListPath).run();
    } catch (IOException e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}
