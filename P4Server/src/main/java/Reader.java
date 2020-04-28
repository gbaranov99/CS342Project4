import java.io.*;
import java.util.ArrayList;

public class Reader {

	public ArrayList<String> readFile(String fileName) {
		ArrayList<String> arr = new ArrayList<>();
        try {
            // the slash '/' is required
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream("/" + fileName), "UTF-8"));
            String line = null;

            while ((line = br.readLine()) != null) {
                arr.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

		return arr;
    }
}

