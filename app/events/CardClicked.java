package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import demo.Run;
import structures.GameState;
import structures.basic.MiniCard;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		int handPosition = message.get("position").asInt();
		
		summonOrCast(handPosition);

		
	}
	
	public void summonOrCast (int handPos) {		//summon a unit or cast a spell
		
		for(int i=0;i<6;i++) {
			
			if(MiniCard.miniCard[i] != null) {

				if(MiniCard.miniCard[i].getPosition() == handPos) {
					
					System.out.println("xxxxxxxx" + MiniCard.miniCard[i].getId());
					if(MiniCard.miniCard[i].getId() == 4 || MiniCard.miniCard[i].getId() == 14
							|| MiniCard.miniCard[i].getId() == 8 || MiniCard.miniCard[i].getId() == 18) {
						System.out.println("This is a spell card!");
					}else {
						System.out.println("This is an unit card!");
						
						// draw the summon range
						//
						//
						// ??? 如何将手牌和unit的关系对应起来？？？
						//Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
						
					}
				}
			}

		}
	}
	

}
