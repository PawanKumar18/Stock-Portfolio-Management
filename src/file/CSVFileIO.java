package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSVFileIO implements the FileIO interface. It performs the file operations on a csv file.
 */
public class CSVFileIO implements FileIO {

  private List<String> assignValues(int headersLength, String line) {
    if (headersLength == 0) {
      return new ArrayList<>(Arrays.asList(line.split(",")));
    }
    List<String> out = new ArrayList<>(Arrays.asList(line.split(",")));
    while (out.size() < headersLength) {
      out.add(null);
    }
    return out;
  }

  @Override
  public List<Map<String, String>> read(Map<String, String> filters, InputStreamReader is)
      throws IOException {
    BufferedReader reader = new BufferedReader(is);
    String line;
    List<Map<String, String>> out = new ArrayList<>();
    List<String> cols = new ArrayList<>();
    int count = 0;
    while ((line = reader.readLine()) != null) {
      if (count == 0) {
        cols = this.assignValues(0, line);
        count = 1;
      } else {
        List<String> data = this.assignValues(cols.size(), line);
        int filtersQualified = 0;
        for (Map.Entry<String, String> filter : filters.entrySet()) {
          if (!cols.contains(filter.getKey())) {
            throw new IllegalArgumentException(
                String.format("Given Col %s does not exist!", filter.getKey()));
          }
          if (filter.getValue().equalsIgnoreCase(data.get(cols.indexOf(filter.getKey())))) {
            filtersQualified += 1;
          }
        }
        if (filtersQualified == filters.size()) {
          Map<String, String> colAdd = new HashMap<>();
          for (int k = 0; k < data.size(); k++) {
            colAdd.put(cols.get(k), data.get(k));
          }
          out.add(colAdd);
        }
      }
    }
    return out;
  }

  @Override
  public void write(String value, FileWriter os) throws IOException {
    os.write(value);
  }

  @Override
  public void update(String old, String store, String newString)
      throws IOException {
    File tempFile = new File("temp.txt");

    BufferedReader reader = new BufferedReader(new FileReader(store));
    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

    String currentLine;
    boolean temp = false;
    while ((currentLine = reader.readLine()) != null) {
      String trimmedLine = currentLine.trim();
      if (trimmedLine.startsWith(old)) {
        if (temp) {
          writer.newLine();
        } else {
          temp = true;
        }
        writer.write(newString);
        continue;
      }
      if (temp) {
        writer.newLine();
      } else {
        temp = true;
      }
      writer.write(currentLine);
    }
    writer.close();
    reader.close();

    boolean successful = tempFile.renameTo(new File(store));
  }
}
