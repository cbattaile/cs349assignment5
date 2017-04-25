import java.util.HashMap;

public class MapTask implements IntMapTask {
	private String mapperName;

	public MapTask(String name) {
		this.mapperName = name;
	}


	public void processInput(String input, IntMaster theMaster) {
		// counts frequency of words
		input = input.replaceAll("[^A-Za-z\\s]", "");
		input = input.toLowerCase();
		String[] tokens = input.split("[ ]+");
		HashMap<String, Integer> outputs = new HashMap<String, Integer>();
		for (String token : tokens) {
			if (outputs.containsKey(token)) {
				int current = outputs.get(token);
				current++;
				outputs.remove(token);
				outputs.put(token, current);
			} else {
				outputs.put(token, 1);
			}
		}
		// get reducers
		try {
			String[] words = outputs.keySet().toArray(new String[outputs.size()]);
			System.out.println("M: getting reducers for " + input);
			IntReduceTask[] reducers = theMaster.getReducers(words);
			System.out.println("M: got " + reducers.length + " IntReducers from master for" + input);
			for (int i = 0; i < words.length; i++) {
				reducers[i].receiveValues(outputs.get(words[i]));
			}
			// tells master that it is done
			System.out.println("M: calling MarkMapperDone(" + this.mapperName);
			theMaster.markMapperDone(this.mapperName);

		} catch (Exception e) {
			System.err.println("M:Master Exception (could not get reducers from master)" + e.toString());
			e.printStackTrace();
		}

	}

}
