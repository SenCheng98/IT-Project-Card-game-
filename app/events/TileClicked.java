package events;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.Run;
import structures.GameState;
import structures.basic.Unit;

public class TileClicked implements EventProcessor {

	
	boolean unitSelected = false;	//if player has selected a unit
	int[][] moveTiles;				//store move range to a 2-dimension array
	int[][] attackTiles;
	Unit lastUnit; 					//store the latest selected unit
	boolean lock = false;
	
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		int x = message.get("tilex").asInt(); // coordinate x of clicked tile
		int y = message.get("tiley").asInt(); // coordinate y of clicked tile
		
		if(Run.humanPlayer.getHealth() <= 0 || Run.aiPlayer.getHealth() <= 0) {	//game over
			gameState.something = false;
		}

		if (gameState.something == true) {
			// do some logic
			
			this.lock = false;		// a lock to make sure the order must be step1 to step2
			 
			if(this.unitSelected) {	// ★★★ step2, move, or attack
				
				for(int[] tile : moveTiles) {
					if(tile != null) {
						BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 0);		// delete the highlight tiles when click tile
					}
				}
				int lock2 = 0;
				for(int[] tile : moveTiles) {
					if(tile != null) {
						if(tile[0] == x && tile[1] == y) {	// the clicked position is in the move range
							
							if(Run.tile[x][y].getHasUnit()) {	//there is an unit on the position
								
								for(int[] tile2 : attackTiles) {
									if(tile2[0] == x && tile2[1] == y) {	// ★★ 1st situation ---> the clicked unit is in attack range
										attack();				// ★ just attack
										lock2 = 1;
										break;
									}
								}
								if(lock2 == 1)	break;
								
								// ★★ 2nd situation ---> the clicked unit is in move range but not attack range
								int[] pos = new int[2];
								pos = preAttackMove(lastUnit, x, y);		// get the new position
								move(out, lastUnit, pos[0], pos[1]);		// ★ move first then attack
								attack();
								break;
							}else {
								move(out, lastUnit, x, y);		// ★ just move
								break;
							}
						}
					}
				}
				this.unitSelected = false;
				this.lock = true;
//				if() {}
//				if() {}
			}
			
			if (Run.tile[x][y].getHasUnit() && this.lock == false) {		//★★★ step 1, select an unit

				this.unitSelected = true;
				this.lastUnit = Run.tile[x][y].getUnit();
				this.moveTiles = null;		//even if we get new data then we click an unit, but it's not bad to clear old data
				this.moveTiles = Board.getMoveRangeTiles(x, y);
				this.attackTiles = Board.getAttackRangeTiles(x, y);
				for (int[] tile : moveTiles) {
					if (tile != null) {
						if(Run.tile[tile[0]][tile[1]].getHasUnit()) {
							BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 2);		// highlight the move range 
						}else {
							BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 1);		// do not highlight if there is an unit in a tile
						}
						
					}
				}
				
			}

		}

	}
	
	public void move (ActorRef out, Unit unit, int x, int y) {	// (x, y) is the new position of the unit
		BasicCommands.moveUnitToTile(out, unit, Run.tile[x][y], true);	// move 
		
		Run.tile[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setUnit(null); //delete the information in the old tile.
		Run.tile[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setHasUnit(false); 
		lastUnit.setPositionByTile(Run.tile[x][y]);	//set the new position of the unit
	}
	
	public void attack() {
		
		//input: ActorRef out, Unit unit, int x, int y
	}
	
	public int[] preAttackMove(Unit unit, int x, int y) {
		int [] pos = new int[2];
		int x1  = unit.getPosition().getTilex() - x;
		int y1  = unit.getPosition().getTiley() - y;
		
		System.out.println("xd" + x1 + "xd" + y1);
		
		if(x1 == 2) {
			pos[0] = unit.getPosition().getTilex() - 1;
			pos[1] = unit.getPosition().getTiley();
		}
		if(x1 == -2) {
			pos[0] = unit.getPosition().getTilex() + 1;
			pos[1] = unit.getPosition().getTiley();
		}
		if(y1 == 2) {
			pos[0] = unit.getPosition().getTilex();
			pos[1] = unit.getPosition().getTiley() - 1;
		}
		if(y1 == -2) {
			pos[0] = unit.getPosition().getTilex();
			pos[1] = unit.getPosition().getTiley() + 1;
		}
		return pos;
	}

}

class Board {		//calculate attack / move range of a unit
	// 9 * 5;
	static int maxX = 8; // the number of tiles in one row is 9
	static int maxY = 4; // the number of tiles in one colum is 5;

	static int[][] getMoveRangeTiles(int x, int y) { // input the coordinate of the unit, show the move range

		//     8
		//   0 1 2
		// 9 3   4 10
		//   5 6 7
		//     11

		int[][] tiles = Arrays.copyOf(getAttackRangeTiles(x, y), 12); // max=12


			if (y > 1) {
				tiles[8] = new int[2]; // tile8
				tiles[8][0] = x;
				tiles[8][1] = y - 2;
			}

			if (y < maxY - 1) {
				tiles[11] = new int[2]; // tile11
				tiles[11][0] = x;
				tiles[11][1] = y + 2;
			}

			if (x > 1) {
				tiles[9] = new int[2]; // tile9
				tiles[9][0] = x - 2;
				tiles[9][1] = y;
			}


			if (x < maxX - 1) {
				tiles[10] = new int[2]; // tile10
				tiles[10][0] = x + 2;
				tiles[10][1] = y;
			}

		return tiles;
	}
	
	static int[][] getAttackRangeTiles(int x, int y) { // input the coordinate of the unit, show the attack range

		//     
		//   0 1 2
		//   3   4 
		//   5 6 7
		//     

		int[][] tiles = new int[8][]; // max=8

		if (y > 0) {
			tiles[1] = new int[2]; // tile1
			tiles[1][0] = x;
			tiles[1][1] = y - 1;
			if (x > 0) {
				tiles[0] = new int[2]; // tile0
				tiles[0][0] = x - 1;
				tiles[0][1] = y - 1;
			}
			if (x < maxX) {
				tiles[2] = new int[2]; // tile2
				tiles[2][0] = x + 1;
				tiles[2][1] = y - 1;
			}
		}

		if (y < maxY) {
			tiles[6] = new int[2]; // tile2
			tiles[6][0] = x;
			tiles[6][1] = y + 1;
			if (x > 0) {
				tiles[5] = new int[2]; // tile5
				tiles[5][0] = x - 1;
				tiles[5][1] = y + 1;
			}
			if (x < maxX) {
				tiles[7] = new int[2]; // tile7
				tiles[7][0] = x + 1;
				tiles[7][1] = y + 1;
			}
		}

		if (x > 0) {
			tiles[3] = new int[2]; // tile3
			tiles[3][0] = x - 1;
			tiles[3][1] = y;
		}
		if (x < maxX) {
			tiles[4] = new int[2]; // tile4
			tiles[4][0] = x + 1;
			tiles[4][1] = y;
		}

		return tiles;
	}

}
