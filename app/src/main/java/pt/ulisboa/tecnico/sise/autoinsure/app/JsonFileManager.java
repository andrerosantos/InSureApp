package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class JsonFileManager {

    private static final String LINE_SEP = System.getProperty("line.separator");

    private static final String TAG = "JsonFM";

    public static void jsonWriteToFile(Context context, String filename, String input) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(input.getBytes());
            Log.e(TAG, "File has been written => " + filename);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found", e);
        } catch (IOException e) {
            Log.e(TAG, "IO problem", e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Log.d(TAG, "Close error.");
            }
        }
    }

    public static String jsonReadFromFile(Context context, String filename) throws FileNotFoundException {
        FileInputStream fis = null;
        Scanner scanner = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = context.openFileInput(filename);
            scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine() + LINE_SEP);
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.d("FileExplorer", "Close error.");
                }
            }
            if (scanner != null) {
                scanner.close();
            }
        }
        return sb.toString();
    }
}
