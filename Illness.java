import java.util.ArrayList;

class Illness {
	public String illName = "";
	public ArrayList<String> symptoms = new ArrayList<String>();
	public String description = "";

	public Illness(String illN, ArrayList<String> illSymp, String d) {
		illName = illN;
		symptoms = illSymp;
		description = d;
	}
}