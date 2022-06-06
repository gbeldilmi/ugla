package ugla.db;

import java.io.IOException;

public class ContactList extends CSVDatabase {
  public ContactList(String path) throws IOException {
    // It's just the constructor
    super(path, "Last name", "First name", "Phone", "Email", "Birthday");
  }

  public void add(String lastName, String firstName, String phone, String email, String birthday)
      throws IOException, IllegalArgumentException {
    // Add multiple contact from the list
    if (lastName == null || firstName == null || phone == null || email == null || birthday == null) {
      throw new IllegalArgumentException("Null argument");
    }
    super.add(lastName.toUpperCase(), firstName, phone, email.toLowerCase(), birthday);
  }

  public void remove(String[] keys) {
    // Remove a contact from the list
    String[] item;
    if (keys == null) {
      throw new IllegalArgumentException("Null argument");
    }
    if ((item = super.get(keys)) != null) {
      super.remove(item);
    }
  }

  public void removeMultiple(String[] keys) {
    // Remove multiple contact from the list
    String[][] items;
    if (keys == null) {
      throw new IllegalArgumentException("Null argument");
    }
    if ((items = super.getMultiple(keys)) != null) {
      super.removeMultiple(items);
    }
  }
}
