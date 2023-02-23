package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.Run;
import structures.GameState;
import structures.basic.MiniCard;

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
	static int addCard = 0;		//completing hand card
	
	public static void cardNumber(ActorRef out) {
	
		
		if(setHumanCard > 6) {		//handcards limitation
			setHumanCard = 6;
		}
		if(setAiCard > 6) {
			setAiCard = 6;
		}
		
		if(EndTurnClicked.turnCount > 0) {
			if(EndTurnClicked.turnCount % 2 == 0) {	//human turn
				
				for(int i=0;i<setHumanCard;i++) {
					
					if(MiniCard.miniCard[i] == null) {		// if there is no card on one position, insert a card to it
						
						
						if(Run.humanCard.get(0) == null)	break;
						
						// 5 steps in setting hand cards (sequential extraction, not randomly)
						// draw minicards
						// store cards to minicard[] + set a position of minicard + set minicard id
						// delete deck card
						BasicCommands.drawCard(out, Run.humanCard.get(0), i+1, 1);
						MiniCard.miniCard[i] = Run.humanCard.get(0).getMiniCard();
						MiniCard.miniCard[i].setPosition(i+1);
						MiniCard.miniCard[i].setId(Run.humanCard.get(0).getId());
						Run.humanCard.remove(0);
					}
					
				}

				System.out.println("card remain: " + Run.humanCard.size());
				System.out.println(Run.humanCard.get(0));
				setAiCard = setAiCard + 1;
			}
			
			
			if(EndTurnClicked.turnCount % 2 == 1) {	//ai turn
				
				setHumanCard = setHumanCard + 1; 
				for(int i=0;i<6;i++) {
					if(MiniCard.miniCard[i] != null) {
						BasicCommands.deleteCard(out, i+1);
						MiniCard.miniCard[i] = null;
					}
				}
				
			}
		}

	}
}
