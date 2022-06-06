package ugla.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVDatabase {
  private ArrayList<String[]> data;
  private String keys[];
  private File file;

  public CSVDatabase(String path) throws IOException {
    // Load the database from the file
    this(path, (String[]) null);
  }

  public CSVDatabase(String path, String... keys) throws IOException {
    // Load the database from the file with the given keys
    file = new File(path);
    this.keys = keys;
    data = new ArrayList<String[]>();
    read();
  }

  public void add(String... values) throws IOException {
    // Add an item to the database
    if (values.length != keys.length) {
      throw new IllegalArgumentException("Wrong number of values");
    }
    data.add(values);
  }

  public String[] get(String[] keys) throws IllegalArgumentException {
    // Get an item from the database with the given keys
    if (keys.length != this.keys.length) {
      throw new IllegalArgumentException("Wrong number of keys");
    }
    for (String[] item : data) {
      if (matchItem(item, keys)) {
        return item;
      }
    }
    return null;
  }

  public String[][] getAll() {
    // Get all items from the database
    return data.toArray(new String[1][]);
  }

  public String[][] getMultiple(String[] keys) throws IllegalArgumentException {
    // Get multiple items from the database with the given keys
    ArrayList<String[]> res;
    if (keys.length != this.keys.length) {
      throw new IllegalArgumentException("Wrong number of keys");
    }
    res = new ArrayList<String[]>();
    for (String[] item : data) {
      if (matchItem(item, keys)) {
        res.add(item);
      }
    }
    return res.toArray(new String[1][]);
  }

  private boolean matchItem(String[] item, String[] keys) throws IllegalArgumentException {
    // Check if the item matches the given keys (empty values are ignored)
    int i;
    if (item.length != keys.length) {
      throw new IllegalArgumentException("Wrong number of keys");
    }
    for (i = 0; i < keys.length; i++) {
      if (!keys[i].equals("")) {
        if (!item[i].equalsIgnoreCase(keys[i])) {
          return false;
        }
      }
    }
    return true;
  }

  private void read() throws IOException {
    // Read the database from the file
    BufferedReader reader;
    String line, values[];
    // Check if file exists
    if (!file.exists()) {
      if (keys == null) {
        throw new IOException("File not found: " + file.getAbsolutePath());
      } else {
        write();
      }
    }
    // Read the file
    reader = new BufferedReader(new FileReader(file));
    // Check the header
    if ((line = reader.readLine()) == null) {
      write();
      line = reader.readLine();
    }
    if (keys != null) {
      if (!line.equals(String.join(",", keys))) {
        reader.close();
        throw new IOException("Invalid keys");
      }
    } else {
      keys = line.split(",");
    }
    // Read data line by line
    while ((line = reader.readLine()) != null) {
      if (line.length() == 0) {
        continue;
      }
      values = splitLine(line);
      if (values.length != keys.length) {
        reader.close();
        throw new IOException("Invalid line: " + line);
      }
      data.add(values);
    }
    // Close the reader
    reader.close();
  }

  public void remove(String[] item) {
    // Remove the item from the data
    data.remove(item);
  }

  public void removeMultiple(String[][] items) {
    // Remove the items from the data
    for (String[] item : items) {
      remove(item);
    }
  }

  public void sort() {
    // Sort by first key (if equal, sort by second key, etc.)
    data.sort((String[] a, String[] b) -> {
      int i;
      for (i = 0; i < keys.length; i++) {
        if (a[i].compareToIgnoreCase(b[i]) != 0) {
          return a[i].compareToIgnoreCase(b[i]);
        }
      }
      return 0;
    });
  }

  protected String[] splitLine(String line) {
    // Split the line into values
    ArrayList<String> res;
    String s;
    int i;
    char c;
    res = new ArrayList<String>();
    s = "";
    for (i = 0; i < line.length(); i++) {
      if ((c = line.charAt(i)) == ',') {
        res.add(s);
        s = "";
      } else {
        s += c;
      }
    }
    res.add(s);
    return res.toArray(new String[1]);
  }

  public void write() throws IOException {
    // Write the database to the file
    BufferedWriter writer;
    // Sort the data before writing
    sort();
    writer = new BufferedWriter(new FileWriter(file));
    // Write the header
    if (keys == null) {
      writer.close();
      throw new IOException("No keys");
    }
    writer.write(String.join(",", keys) + "\n");
    // Write data
    for (String[] values : data) {
      writer.write(String.join(",", values) + "\n");
    }
    // Close the writer
    writer.close();
  }

  public String toString() {
    // Return a string representation of the CSV database
    StringBuilder sb = new StringBuilder();
    sb.append(String.join(",", keys) + "\n");
    for (String[] values : data) {
      sb.append(String.join(",", values) + "\n");
    }
    return sb.toString();
  }
}
