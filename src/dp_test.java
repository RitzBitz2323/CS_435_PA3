 

public class dp_test {
        public static void main(String[] args) {
            DocumentPreprocess dp = new DocumentPreprocess();
            String output = dp.processing("/Users/rambekar/CS_435/CS_435_PA3/data/IR/21st_century.txt");
            System.out.println("Output: " + output);
            dp.clearOutputFolder();
        }
    
}
