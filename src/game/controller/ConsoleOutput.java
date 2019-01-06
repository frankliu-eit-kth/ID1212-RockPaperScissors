package game.controller;

import java.util.ArrayList;
/**
 * provides all the functions needed to display message on user console
 * @author m1339
 *
 */
public interface ConsoleOutput {
	
	public void outputMultipleStrings(ArrayList<String> msg);
	
	public void output(String msg);
	
}
