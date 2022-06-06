package ugla;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ugla.db.ContactList;

public class ContactManager {
  private ContactList contacts;

  public ContactManager(String path) throws IOException {
    // Load the database from the file
    contacts = new ContactList(path);
  }

  private void add() {
    // Add a contact
    String keys[];
    keys = new String[5];
    System.out.println("\n\tAdd a contact");
    System.out.println("Last name:");
    keys[0] = getInput();
    System.out.println("First name:");
    keys[1] = getInput();
    System.out.println("Phone:");
    keys[2] = getInput();
    System.out.println("Email:");
    keys[3] = getInput();
    System.out.println("Birthday:");
    keys[4] = getInput();
    // Ask confirmation to the user before adding the contact
    System.out.println("\n\tDo you wish to add this contact to the contact list ? (y/N)");
    printContact(keys);
    switch (getInput()) {
      case "y":
      case "yes":
      case "Y":
      case "Yes":
      case "YES":
        try {
          contacts.add(keys[0], keys[1], keys[2], keys[3], keys[4]);
        } catch (IOException e) {
          System.err.println("Error: " + e.getMessage());
        }
        break;
      default:
    }
  }

  private void birthday() {
    // Print all contacts with birthday
    ArrayList<String[]> bday;
    bday = new ArrayList<>();
    for (String[] item : contacts.getAll()) {
      if (!item[4].equals("")) {
        bday.add(item);
      }
    }
    // Sort by birthday
    bday.sort((String[] a, String[] b) -> {
      int r;
      r = Integer.parseInt(a[4].substring(3, 5)) - Integer.parseInt(b[4].substring(3, 5));
      if (r == 0) {
        r = Integer.parseInt(a[4].substring(0, 2)) - Integer.parseInt(b[4].substring(0, 2));
      }
      return r;
    });
    // Print
    System.out.println("\nBirthdays:\n");
    for (String[] c : bday) {
      System.out.println("\t" + c[4].substring(0, 5) + " => " + c[0] + " " + c[1]);
    }
  }

  private void print() {
    // Print all contacts
    contacts.sort();
    for (String[] contact : contacts.getAll()) {
      printContact(contact);
    }
    System.out.println("\n\tTotal: " + contacts.getAll().length + " contacts");
  }

  private void printContact(String[] contact) {
    // Print a contact
    System.out.println(contact[0] + " " + contact[1] + "\n\tPhone :  \t" + contact[2] + "\n\tMail :   \t" + contact[3]
        + "\n\tBirthday :\t" + contact[4]);
  }

  private void remove() {
    // Remove a contact
    String keys[], c[];
    keys = new String[5];
    System.out.println("\n\tRemove a contact");
    System.out.println("Leave a field blank to ignore it");
    System.out.println("Last name:");
    keys[0] = getInput();
    System.out.println("First name:");
    keys[1] = getInput();
    System.out.println("Phone:");
    keys[2] = getInput();
    System.out.println("Email:");
    keys[3] = getInput();
    System.out.println("Birthday:");
    keys[4] = getInput();
    if ((c = contacts.get(keys)) != null) {
      // Ask confirmation to the user before removing the contact
      System.out.println("\n\tDo you wish to remove this contact to the contact list ? (y/N)");
      printContact(c);
      switch (getInput()) {
        case "y":
        case "yes":
        case "Y":
        case "Yes":
        case "YES":
          contacts.remove(c);
          break;
        default:
      }
    } else {
      System.out.println("\n\tNo contact found");
    }
  }

  public void run() throws IOException {
    // Main loop
    boolean running;
    System.out.println("\n\tUgla Contact Manager");
    running = true;
    while (running) {
      System.out.println("\n1. Print contacts");
      System.out.println("2. Show birthdays to be wished");
      System.out.println("3. Search for a contact");
      System.out.println("4. Add a contact");
      System.out.println("5. Remove a contact");
      System.out.println("0. Exit");
      switch (getInput()) {
        case "0":
          running = false;
          break;
        case "1":
          print();
          break;
        case "2":
          birthday();
          break;
        case "3":
          search();
          break;
        case "4":
          add();
          break;
        case "5":
          remove();
          break;
        default:
      }
    }
    contacts.sort();
    contacts.write();
  }

  private void search() {
    // Search for a contact
    String keys[], matches[][];
    keys = new String[5];
    System.out.println("\n\tSearch for a contact");
    System.out.println("Leave a field blank to ignore it");
    System.out.println("Last name:");
    keys[0] = getInput();
    System.out.println("First name:");
    keys[1] = getInput();
    System.out.println("Phone:");
    keys[2] = getInput();
    System.out.println("Email:");
    keys[3] = getInput();
    System.out.println("Birthday:");
    keys[4] = getInput();
    // Search for contacts matching the keys and print them
    matches = contacts.getMultiple(keys);
    System.out.println("\n\tResults:\n");
    if (matches.length == 0) {
      System.out.println("No matches found");
    } else {
      for (String[] c : matches) {
        printContact(c);
      }
      System.out.println("\n\tTotal: " + matches.length + " matches");
    }
  }

  private static String getInput() {
    // Get input from user
    String input;
    input = "";
    try {
      System.out.print("\t >> ");
      input = new BufferedReader(new InputStreamReader(System.in)).readLine();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
    return input;
  }
}
