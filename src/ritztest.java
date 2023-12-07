public class ritztest {

	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		PositionalIndex ps = new PositionalIndex("/Users/rambekar/CS_435/CS_435_PA3/data/IR");
		System.out.println(((System.currentTimeMillis() - timeStart)/1000) + " seconds"); // seconds to create inverted index
		System.out.println(ps.termFrequency("beach", "Zach_Duke.txt"));
		System.out.println(ps.docFrequency("beach"));
		//System.out.println(ps.postingsList("s"));
	}
	
}
