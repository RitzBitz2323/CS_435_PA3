
public class colemanTests {

	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		PositionalIndex ps = new PositionalIndex("C:\\Users\\cdimm\\Downloads\\IR\\IR");
		System.out.println((System.currentTimeMillis() - timeStart)/1000); // seconds to create inverted index
		System.out.println(ps.termFrequency("s", "Isolated_Power.txt"));
		System.out.println(ps.docFrequency("s"));
		System.out.println(ps.postingsList("s"));
	}
	
}
