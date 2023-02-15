package demo;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class CheckMoveLogic {

	public static void executeDemo(ActorRef out) {
		
//		// Draw two tiles to move between
//		Tile tile = BasicObjectBuilders.loadTile(0, 0);//loadTile(y,x)
//		BasicCommands.drawTile(out, tile, 0);	
//		Tile tile2 = BasicObjectBuilders.loadTile(1, 4);
//		BasicCommands.drawTile(out, tile2, 0);
		
		Tile[][] tile = new Tile[9][5];
		for(int x=0;x<5;x++) {
			for(int y=0;y<9;y++) {
				tile[y][x] = BasicObjectBuilders.loadTile(y, x);
				BasicCommands.drawTile(out, tile[y][x], 0);
			}
		}
	
		// Draw a unit
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
		unit.setPositionByTile(tile[1][1]); 
		BasicCommands.drawUnit(out, unit, tile[1][1]);
		
		// Move unit, default, horizontal then vertical
		BasicCommands.moveUnitToTile(out, unit, tile[3][3]);
		unit.setPositionByTile(tile[3][3]); 
		
		try {Thread.sleep(4000);} catch (InterruptedException e) {e.printStackTrace();}
		
		// Move unit, default, horizontal then vertical
		BasicCommands.moveUnitToTile(out, unit, tile[1][1]);
		unit.setPositionByTile(tile[1][1]); 
		
		try {Thread.sleep(4000);} catch (InterruptedException e) {e.printStackTrace();}
		
		// Move unit, alternative, vertical then horizontal
		BasicCommands.moveUnitToTile(out, unit, tile[3][3], true);
		unit.setPositionByTile(tile[3][3]); 
		
		try {Thread.sleep(4000);} catch (InterruptedException e) {e.printStackTrace();}
		
		// Move unit, alternative, vertical then horizontal
		BasicCommands.moveUnitToTile(out, unit, tile[1][1], true);
		unit.setPositionByTile(tile[1][1]); 
		
		BasicCommands.addPlayer1Notification(out, "drawCard [1u]", 1);
		Card hailstone_golem = BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem, 0, Card.class);
		BasicCommands.drawCard(out, hailstone_golem, 1, 0);
		BasicCommands.drawCard(out, hailstone_golem, 2, 0);
		BasicCommands.drawCard(out, hailstone_golem, 3, 0);
		BasicCommands.drawCard(out, hailstone_golem, 4, 0);
		BasicCommands.drawCard(out, hailstone_golem, 5, 0);
		BasicCommands.drawCard(out, hailstone_golem, 6, 0);
	}
	
}
