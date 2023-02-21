package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.Run;
import structures.GameState;

/**
 * Indicates that the user has clicked an object on the game canvas, in this
 * case the end-turn button.
 * 
 * { messageType = “endTurnClicked” }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor { // mana control & handcard number control & turn control??

	static int turnCount = 0;

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		if(Run.humanPlayer.getHealth() <= 0 || Run.aiPlayer.getHealth() <= 0) {
			gameState.something = false;
		}
		if (gameState.something) {

			this.turnCount++; // times of click
			ManaControl.manaCount(out);
			handCard.cardNumber(out);
			

		}

	}

}

class ManaControl {

	static int setHumanMana = 2;	// have 2 mana at the beginning
	static int setAiMana = 2;

	public static void manaCount(ActorRef out) { // ★★★ it should be clear when reload game!!!
		if (setHumanMana > 9) { // human mana limitation
			setHumanMana = 9;
		}
		if (setAiMana > 9) { // ai mana limitation
			setAiMana = 9;
		}

		if (EndTurnClicked.turnCount > 0) { // human first
			if (EndTurnClicked.turnCount % 2 == 0) {	// human turn
				Run.aiPlayer.setMana(0);
				Run.humanPlayer.setMana(setHumanMana);
				setAiMana = setAiMana + 1;
				BasicCommands.setPlayer1Mana(out, Run.humanPlayer);
				BasicCommands.setPlayer2Mana(out, Run.aiPlayer);
			}
			if (EndTurnClicked.turnCount % 2 == 1) {	//ai turn
				Run.humanPlayer.setMana(0);
				Run.aiPlayer.setMana(setAiMana);
				setHumanMana = setHumanMana + 1;
				BasicCommands.setPlayer1Mana(out, Run.humanPlayer);
				BasicCommands.setPlayer2Mana(out, Run.aiPlayer);
			}

		}
		System.out.println(Run.humanPlayer.getMana());
		System.out.println(Run.aiPlayer.getMana());
	}

}

class handCard{		
	
	static int setHumanCard = 3;	//have 3 cards at the beginning
	static int setAiCard = 3;
	public static void cardNumber(ActorRef out) {
		
		if(setHumanCard > 6) {		//handcards limitation
			setHumanCard = 6;
		}
		if(setAiCard > 6) {
			setAiCard = 6;
		}
		
		if(EndTurnClicked.turnCount > 0) {
			if(EndTurnClicked.turnCount % 2 == 0) {	//human turn
				
				//Draw cards
				for(int i=0;i<setHumanCard;i++) {
					if(Run.humanCard.get(0) == null)	break;
					BasicCommands.drawCard(out, Run.humanCard.get(0), i+1, 1);
					Run.humanCard.remove(0);
				}	
				setAiCard = setAiCard + 1;
			}
			if(EndTurnClicked.turnCount % 2 == 1) {	//ai turn
				
				setHumanCard = setHumanCard + 1; 
			}
		}

	}
}
