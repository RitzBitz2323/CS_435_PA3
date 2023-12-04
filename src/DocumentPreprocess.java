import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class DocumentPreprocess {

    public String processing(String exact_inputFile){
        int lastSeparatorIndex = exact_inputFile.lastIndexOf(File.separator);
        int lastPeriodIndex = exact_inputFile.lastIndexOf(".");
        String fileName = exact_inputFile.substring(lastSeparatorIndex + 1, lastPeriodIndex);
        String outputFilePath = "/Users/rambekar/CS_435/CS_435_PA3/data/preprocessed_files/output_" + fileName + ".txt";

        try {
            Set<String> stopwords = new HashSet<>();
            stopwords.add("the");
            stopwords.add("is");
            stopwords.add("are");
            
            // Symbols to remove
            String symbolsToRemove = ",\"?[]'{}:;()";
            
            BufferedReader reader = new BufferedReader(new FileReader(exact_inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();
                line = line.replaceAll("[" + Pattern.quote(symbolsToRemove) + "]", "");


                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (!stopwords.contains(word) && word.length() > 2) {
                        writer.write(word + " ");
                    }
                }

                writer.newLine();
            }

            reader.close();
            writer.close();

            System.out.println("Text preprocessing completed. Preprocessed text saved to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFilePath;
    }

    public static void clearOutputFolder() {
        System.out.println("Clearing output folder...");
        File folder = new File("data/preprocessed_files");
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            file.delete();
        }
    }
}
