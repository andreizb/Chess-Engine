package chess;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import units.*;

public class MainClass {
	// Game related
	public static Engine engine = new Engine();
	
	// Engine related
	public static Map<String, Runnable> funcMap = new HashMap<>();
	public static Scanner scanner = new Scanner(System.in);
	public static String[] input;
	
	public static PrintWriter log;
	
	public static void initEngine() {
		try {
			log = new PrintWriter(new BufferedWriter(new FileWriter("logYapalPapal.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		funcMap.put("protover", MainClass::offerParams);
		funcMap.put("xboard", MainClass::initGame);
		funcMap.put("new", MainClass::newGame);
		funcMap.put("force", MainClass::forceSpectate);
		funcMap.put("go", MainClass::forcePlay);
		funcMap.put("time", MainClass::updateTime);
		funcMap.put("white", MainClass::playAsWhite);
		funcMap.put("black", MainClass::playAsBlack);
		funcMap.put("resign", MainClass::opponentResigns);
		funcMap.put("quit", MainClass::quitGame);
		// TODO: OPTIONAL - level 0 5 2, post, hard
		
		
	}
	
	public static void termEngine() {		
		scanner.close();
		log.close();
	}
	
	public static void offerParams() {		
		System.out.println("feature sigterm=0 sigint=0 san=0");
	}
	
	public static void initGame() {
		
	}
	
	public static void newGame() {
		engine = new Engine();
	}
	
	public static void forceSpectate() {
		engine.isForced = true;
	}
	
	public static void forcePlay() {
		engine.isForced = false;
		engine.makeMove(); // if necessary
	}
	
	public static void updateTime() {
		engine.updateTime(Long.parseLong(input[1]));
	}
	
	public static void playAsWhite() {
		engine.isWhite = true;
		engine.makeMove(); // if necessary
	}
	
	public static void playAsBlack() {
		engine.isWhite = false;
		engine.makeMove(); // if necessary
	}
	
	public static void opponentResigns() {
		
	}
	
	public static void quitGame() {
		log.flush();
	}

	public static void main(String[] args) {
		try {
			initEngine();
			
			while (true) {
				// get input
				if (!scanner.hasNextLine()) continue;
				
				String inputLine = scanner.nextLine().trim();
				input = inputLine.split(" ");
				
				if (input.length == 0) {
					// keep waiting for input
					continue;
				}
				
				if (input[0].length() == 5) {
					Move promotion = new Promotion(input[0]);
					if (promotion.isValid()) {
						engine.registerMove(promotion);
						engine.makeMove();
						continue;
					}
				}

				if (input[0].length() == 4) {
					Move move = new Move(input[0]);
					if (move.isValid()) {
						engine.registerMove(move);
						engine.makeMove();
						continue;
					}
				}
				
				// find and run command
				Runnable func = funcMap.get(input[0]);
				
				if (func != null) {
					func.run();
				}
				
				if (input[0].equals("quit")) {
					// quit command received, terminate the engine
					break;
				}
			}
			
			termEngine();
		} catch (Exception e) {
			e.printStackTrace(log);
			log.flush();
		}
	}

}
