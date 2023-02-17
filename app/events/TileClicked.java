package events;


import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CheckMoveLogic;
import structures.GameState;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * 
 * { 
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{

	Random r = new Random();
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		int tilex = message.get("tilex").asInt();	
		int tiley = message.get("tiley").asInt();
		
//		System.out.println(tilex + " " + tiley);
//		System.out.println("有无单位" + CheckMoveLogic.tile[tilex][tiley].getHasUnit());
		if (gameState.something == true) {
			// do some logic
			int step = 0;
			if(CheckMoveLogic.tile[tilex][tiley].getHasUnit()) {
				
				if(tilex == 7) {
					if(tiley == 2) {
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex + 1][tiley], 1);
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex - 1][tiley], 1);
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex - 2][tiley], 1);
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex][tiley + 1], 1);
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex][tiley + 2], 1);
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex][tiley - 1], 1);
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex][tiley - 2], 1);
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex + 1][tiley + 1], 1);
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex + 1][tiley - 1], 1);
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex - 1][tiley + 1], 1);
						BasicCommands.drawTile(out, CheckMoveLogic.tile[tilex - 1][tiley - 1], 1);
					}
					
				}
				
				
				step = 1;
			}
//			else if(step == 1 && ) {		//move
//				
//				int x = r.nextInt(8);
//				int y = r.nextInt(4);
//				BasicCommands.moveUnitToTile(out,CheckMoveLogic.tile[tilex][tiley].getUnit() , CheckMoveLogic.tile[x][y]);
//				CheckMoveLogic.tile[tilex][tiley].setUnit(null);
//				CheckMoveLogic.tile[tilex][tiley].setHasUnit(false);
//				
//			}
//			else if(step == 1 && ) {		//attack
//				
//				
//				
//			}
			
		}
		
	}

}
