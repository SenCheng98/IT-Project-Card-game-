package demo;


import java.util.Random;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.OrderedCardLoader;
import utils.StaticConfFiles;

public class Run {

	public static Tile[][] tile = new Tile[9][5];
	public static Player humanPlayer;
	public static Player aiPlayer;
	
	
	public static void executeRun(ActorRef out) {
		
		// Draw tiles
		for(int x=0;x<9;x++) {
			for(int y=0;y<5;y++) {
				tile[x][y] = BasicObjectBuilders.loadTile(x, y);
				BasicCommands.drawTile(out, tile[x][y], 0);
			}
		}
		
		//set a human player
		humanPlayer = new Player(22,2);
		aiPlayer = new Player(22,2);
		
		
		//Draw health and mana
		BasicCommands.setPlayer1Health(out, humanPlayer);
		BasicCommands.setPlayer2Health(out, aiPlayer);
		humanPlayer.setMana(2);
		aiPlayer.setMana(2);
		BasicCommands.setPlayer1Mana(out, humanPlayer);
		BasicCommands.setPlayer2Mana(out, aiPlayer);
		
	
		// Draw a human player
		Unit unit1 = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
		unit1.setPositionByTile(tile[1][2]); 
		BasicCommands.drawUnit(out, unit1, tile[0][0]);
		
		
		// Draw a AI player
		Unit unit2 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 0, Unit.class);
		unit2.setPositionByTile(tile[7][2]); 
		BasicCommands.drawUnit(out, unit2, tile[7][2]);
		
		
		
		//Draw cards
		for(int i=0;i<6;i++) {
			BasicCommands.drawCard(out, OrderedCardLoader.getPlayer1Cards().get(i), i+1, 1);
		}	
		

		
	}
	

}

