import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.StringTokenizer; 
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;



class DiagnoseMe {
	Scanner read;	//scanner that reads the file to be encrypted
	PrintWriter output;	//printwriter that writes out the output file
	ArrayList<Illness> thelist = new ArrayList<Illness>();
	int count = 1;
	boolean exit = false;

	ArrayList<Illness> remaining = new ArrayList<Illness>();
	ArrayList<Illness> temp = new ArrayList<Illness>();
	ArrayList<String> preps = new ArrayList<String>();
	ArrayList<String> pastSymp = new ArrayList<String>();


	String header = " \n\n ______   ___  _______  _______  __    _  _______  _______  _______       __   __  _______\n|      | |   ||   _   ||       ||  |  | ||       ||       ||       |     |  |_|  ||       |\n|  _    ||   ||  |_|  ||    ___||   |_| ||   _   ||  _____||    ___|     |       ||    ___|\n| | |   ||   ||       ||   | __ |       ||  | |  || |_____ |   |___      |       ||   |___\n| |_|   ||   ||       ||   ||  ||  _    ||  |_|  ||_____  ||    ___| ___ |       ||    ___|\n|       ||   ||   _   ||   |_| || | |   ||       | _____| ||   |___ |   || ||_|| ||   |___\n|______| |___||__| |__||_______||_|  |__||_______||_______||_______||___||_|   |_||_______|";
	String more = "For example, some statements could be:\n\n'i'm ok, thanks for asking. However, i've been having a minor headache...'\n OR 'I've been sleepless and have nausea and burning stomach pain.'\n OR 'Help I was bitten by a snake!'\n\nFor the full list of all illnesses, type 'ALL'\n--";

	public static void main(String[] args) {
		
		DiagnoseMe run = new DiagnoseMe();
		
		run.setup();
		run.processData();
		run.promptUser();	//ask for necessary input
	}

	void setup() {
		InputStream is = getClass().getResourceAsStream("ignore.txt");
		Scanner uRead = new Scanner(is);
		while(uRead.hasNext()) {
			String line = uRead.next();
			preps.add(line);
		}
	}

	static void updateProgress(double progressPercentage) {
	    final int width = 50; // progress bar width in chars

	    System.out.print("\r[");
	    int i = 0;
	    for (; i <= (int)(progressPercentage*width); i++) {
	      System.out.print(">");
	    }
	    for (; i < width; i++) {
	      System.out.print(" ");
	    }
	    System.out.print("]");
	    System.out.printf("  %.1f", progressPercentage*100 +1);
	    System.out.print("%");
  	}

	public void processData() {
		try {
	      for (double progressPercentage = 0.0; progressPercentage < 1.0; progressPercentage += 0.01) {
	        updateProgress(progressPercentage);
	        Thread.sleep(3);
	      }
	    } catch (InterruptedException e) {}

	    InputStream is = getClass().getResourceAsStream("input.txt");
		read = new Scanner(is);

		String prevName = "";
		ArrayList<String> prevSymp = new ArrayList<String>();
		String prevD = "";

		while(read.hasNext()) {
			String line = read.nextLine();
			if(line.length() <= 0) continue;
			if(line.charAt(0) == '+') {	// new illness
				if(prevName != "") {
					thelist.add(new Illness(prevName, prevSymp, prevD));
					prevSymp = new ArrayList<String>();
					count++;
				}
				prevName = line.substring(6);
				prevSymp.add(line.substring(6));

			}
			else if(line.charAt(0) == '*') {
				prevSymp.add( line.substring(2));
			}
			else if(line.charAt(0) == '-') {
				prevD = line.substring(3);
			}
		}
		remaining = new ArrayList<Illness>(thelist);
	}

	public void remainingData() {
		for(Illness i : remaining) {

			System.out.println(i.illName.toUpperCase() + " ########");
			for(int k = 1; k < i.symptoms.size(); k++) {
				System.out.println("  * " + i.symptoms.get(k));
			}
			System.out.println();
			//System.out.println("    ----- " + i.description + "\n");
		}
	}

	public void regurgitateData() {
		for(Illness i : thelist) {

			System.out.println(i.illName.toUpperCase() + " ########");
			for(int k = 1; k < i.symptoms.size(); k++) {
				System.out.println("  * " + i.symptoms.get(k));
			}
			System.out.println();
			//System.out.println("    ----- " + i.description + "\n");
		}

		System.out.println("Number of stored illnesses: " + count);
	}

	public void askMore() {
		//randomly ask for more info using RNG, print the number of remaining possible problems
		if(Math.random() > 0.4 ) {
			System.out.print("Tell me more. What other symptoms are you feeling: ");
		} else System.out.print("Hmmmm, what other symptoms are there?: ");

	}

	public void askMore(String lol) {
		System.out.print("Come on, I need specifics to diagnose you: ");

	}

	public void giveMore() {
		System.out.println(more);
	}

	public void promptUser() {
		boolean donefirst = false;
		
		Scanner kb = new Scanner(System.in);
		System.out.print(header + "\n");
		System.out.print("\nHey there, welcome to DIAGNOSE.ME\n The purpose of this 'AI' is to diagnose the user of any illnesses out of a database compiled through the web. There are " + remaining.size() + " different possible diagnoses.");
		System.out.print("\n--\nTo begin, how are you feeling? Speak freely about your condition (for examples, type 'more'): ");

		while(exit != true) {
			if(donefirst != false) {
				askMore();
			}
			String input = kb.nextLine();
			System.out.println();
			if(input.contains("more")) {
				giveMore();
				//System.out.print(remaining.size());
			}
			else if(input.toLowerCase().contains("all")) {
				remainingData();
				//System.out.print(remaining.size());
			}
			else if((input.toLowerCase().contains("q") && input.length() < 2)|| input.toLowerCase().equals("exit") || input.toLowerCase().equals("quit")) {
				exit = true;
			}
			else if(input.toLowerCase().equals("none")  || input.toLowerCase().equals("nothing") ) {
				askMore("lol");
				input = kb.nextLine();
				System.out.println();
			}
			else {
				String type = " (PRECISE SEARCH)";
				cutList(input);

				if(temp.size() == 0) {
					cutList(input, true); //less anal
					type = " (VAGUE SEARCH - try more conventional query)";
				}

				if(temp.size() > 0) {
					//System.out.print(remaining.size() > 0);
					remaining = new ArrayList<Illness>(temp);
					remainingData();
					if(remaining.size() > 1)System.out.println(remaining.size() + " REMAINING POSSIBLE ILLNESSES" + type);
					else System.out.println(remaining.size() + " REMAINING POSSIBLE ILLNESS" + type);

					if(remaining.size() < 4 && remaining.size() != 1) {
						System.out.print("There are " + remaining.size() + " diagnoses left. Would you like to continue, or choose the most relevant one? -> ");
						Scanner kb2 = new Scanner(System.in);
						String answer = kb2.nextLine();
						if(answer.toLowerCase().contains("continue")) {

						} else if(answer.toLowerCase().contains("choose") || answer.toLowerCase().contains("one")) {
							diagnoses(remaining.get(chooseBest()));
							exit = true;
						} else {
							System.out.print("Sorry, I didn't get that. Please type 'continue' or choose': ");
							Scanner kb1 = new Scanner(System.in);
							String answer1 = kb1.nextLine();
							if(answer1.toLowerCase().contains("continue")) {

							} else if(answer1.toLowerCase().contains("choose") || answer1.toLowerCase().contains("one")) {
								diagnoses(remaining.get(chooseBest()));
								exit = true;
							}
							else exit = true;
						}
					} else if(remaining.size() == 1) {
						System.out.print("\n");
							try {
						      for (double progressPercentage = 0.0; progressPercentage < 1.0; progressPercentage += 0.01) {
						        updateProgress(progressPercentage);
						        Thread.sleep(5);
						      }
						    } catch (InterruptedException e) {}
						    System.out.print("\n");
						diagnoses(remaining.get(0));
						exit = true;
					}
				}
				else {
					boolean go = false;
					while(!go) {
						go = tryAgain();
						System.out.println();
					}

				}
			}
			donefirst = true;

		}
		
		System.out.println("\nHope you get better! Don't hesitate to ask more questions :) \n\n");

		//key = OpenFile.openToRead(keyFile);		//uses OpenFile to return all the scanners and store them as variables
		//output = OpenFile.openToWrite("output.txt");	//uses OpenFile to return PrintWriter
		
	}

	boolean tryAgain() {
		System.out.print("Hmmmmm, there seems to be no results. Try again? (y/n): ");
		Scanner kb = new Scanner(System.in);
		String answer = kb.next();
		if(answer.toLowerCase().contains("y") && answer.length() <= 2) {
			remainingData();
			return true;
		}
		else if(answer.toLowerCase().contains("n") && answer.length() <= 2) {
			exit = true;
			return true;
		}
		return false;
	}

	public boolean useless(String word) {
		for(String option : preps) {
			if(word.toLowerCase().equals(option.toLowerCase())) {
				//System.out.print("USELESS");
				return true;
			}
		}
		return false;
	}

	public void cutList(String sympToCut, boolean hey) {
		temp = new ArrayList<Illness>();
		pastSymp.add(sympToCut);
		int stutter = 0;
		boolean add = false;
		ArrayList<Illness> keepers = new ArrayList<Illness>();

		ArrayList<Integer> toCut = new ArrayList<Integer>();
		for(int i = 0; i < remaining.size(); i++) {
			Illness ill = remaining.get(i);
			for(String symp : ill.symptoms) {
				//System.out.print(symp);
				StringTokenizer st = new StringTokenizer(sympToCut, " ,.!"); 
					while(st.hasMoreTokens()) { 
						String key = st.nextToken();
						if(useless(key)) continue;
						//System.out.print(key);
						//System.out.print(key);
						if(symp.toLowerCase().contains(key.toLowerCase())) add = true;
					} 
			}
			if(add == true) {
				toCut.add(i);
				add = false;
			}
		}
		for(int k : toCut) {
			keepers.add(remaining.remove(k-stutter));
			stutter++;
		}

		temp = new ArrayList<Illness>(keepers);
	}

	public void cutList(String sympToCut) {
		temp = new ArrayList<Illness>();
		pastSymp.add(sympToCut);
		int stutter = 0;
		boolean add = true;
		ArrayList<Illness> keepers = new ArrayList<Illness>();

		ArrayList<Integer> toCut = new ArrayList<Integer>();
		for(int i = 0; i < remaining.size(); i++) {
			Illness ill = remaining.get(i);
			String bigblock = "";
			for(String symp : ill.symptoms) {
				bigblock += symp;
			}
				//System.out.print(symp);
				StringTokenizer st = new StringTokenizer(sympToCut, " ,.!"); 
					while(st.hasMoreTokens()) { 
						String key = st.nextToken();
						if(useless(key)) continue;
						//System.out.print(key);
						//System.out.print(key);
						if(!bigblock.toLowerCase().contains(key.toLowerCase())) add = false;
					} 
			
			if(add == true) {
				toCut.add(i);
			}
			add = true;
		}
		for(int k : toCut) {
			keepers.add(remaining.remove(k-stutter));
			stutter++;
		}

		temp = new ArrayList<Illness>(keepers);
	}

	public void diagnoses(Illness target) {

		System.out.println("\n-----");
		System.out.println( ("Name of Illness: " + target.illName).toUpperCase());
		System.out.println(("Description: " + target.description).toUpperCase() +"\n-----");
	}

	public int chooseBest() {
		int best = 0;
		int bestScore = 0;
		int score = 0;

		for(int m = 0; m < remaining.size(); m++) {
			score = 0;
			Illness ill = remaining.get(m);
			for(int k = 0; k < ill.symptoms.size(); k++) {
				for(int l = 0; l < pastSymp.size(); l++) {

					String temp = new String(ill.symptoms.get(k));
					int index = temp.indexOf(pastSymp.get(l));
					while (index != -1) {
					    score++;
					    temp = temp.substring(index + 1);
					    index = temp.indexOf(pastSymp.get(l));
					}
				}

			}

			String bigblock = "";
			for(String symp : ill.symptoms) {
				bigblock += symp;
			}
			score = score/bigblock.length();
			if(bestScore < score ) {
				bestScore = score;
				best = m;
			}
		}

		System.out.print("\n");
		try {
	      for (double progressPercentage = 0.0; progressPercentage < 1.0; progressPercentage += 0.01) {
	        updateProgress(progressPercentage);
	        Thread.sleep(10);
	      }
	    } catch (InterruptedException e) {}
	    System.out.print("\n");
		return best;
	}

	public DiagnoseMe() {

	
	}



}