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

		if (gameState.something) {

			this.turnCount++; // times of click
			ManaControl.manaCount(out);

		}

	}

}

class ManaControl {

	static int initMana = 2; // have 2 mana at the beginning
	static int setHumanMana = 2;
	static int setAiMana = 2;

	public static void manaCount(ActorRef out) { // ★★★ it should be clear when reload game!!!
		if (setHumanMana > 9) { // human mana limit
			setHumanMana = 9;
		}
		if (setAiMana > 9) { // ai mana limit
			setAiMana = 9;
		}

		if (EndTurnClicked.turnCount > 1) { // human first
			if (EndTurnClicked.turnCount % 2 == 0) {
				Run.aiPlayer.setMana(0);
				Run.humanPlayer.setMana(setHumanMana);
				setAiMana = setAiMana + 1;
				BasicCommands.setPlayer1Mana(out, Run.humanPlayer);
				BasicCommands.setPlayer2Mana(out, Run.aiPlayer);
			}
			if (EndTurnClicked.turnCount % 2 == 1) {
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
