package demo;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.MiniCard;
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
	public static List<Card> humanCard = OrderedCardLoader.getPlayer1Cards(); // only load once
	public static List<Card> aiCard = OrderedCardLoader.getPlayer1Cards();
	
	public static ArrayList<Unit> unitOnTile = new ArrayList<Unit>();	// store all my unit on the tile, which is used in summon method in CardClicked
	
	public static void executeRun(ActorRef out) {
		
		// Draw tiles
		for(int x=0;x<9;x++) {
			for(int y=0;y<5;y++) {
				tile[x][y] = BasicObjectBuilders.loadTile(x, y);
				BasicCommands.drawTile(out, tile[x][y], 0);
			}
		}
		
		//set health and mana
		humanPlayer = new Player(20,2);
		aiPlayer = new Player(20,2);
		
		
		//Draw health and mana
		BasicCommands.setPlayer1Health(out, humanPlayer);
		BasicCommands.setPlayer2Health(out, aiPlayer);

		BasicCommands.setPlayer1Mana(out, humanPlayer);
		BasicCommands.setPlayer2Mana(out, aiPlayer);
		
	
		// Draw a human player
		Unit unit1 = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
		unit1.setPositionByTile(tile[1][2]); 
		BasicCommands.drawUnit(out, unit1, tile[1][2]);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit1, 5);
		BasicCommands.setUnitHealth(out, unit1, 20);
		
		unitOnTile.add(unit1);
		
		
		// Draw a AI player
		Unit unit2 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 11, Unit.class);
		unit2.setPositionByTile(tile[7][2]); 
		BasicCommands.drawUnit(out, unit2, tile[7][2]);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit2, 4);
		BasicCommands.setUnitHealth(out, unit2, 20);
		
		unitOnTile.add(unit2);
		
		String[] deck1Cards = {
				StaticConfFiles.c_azure_herald,
				StaticConfFiles.c_azurite_lion,
				StaticConfFiles.c_comodo_charger,
				StaticConfFiles.c_fire_spitter,
				StaticConfFiles.c_hailstone_golem,
				StaticConfFiles.c_ironcliff_guardian,
				StaticConfFiles.c_pureblade_enforcer,
				StaticConfFiles.c_silverguard_knight,
				StaticConfFiles.c_sundrop_elixir,
				StaticConfFiles.c_truestrike
		};
		
		String[] deck1Units = {
				StaticConfFiles.u_comodo_charger,
				StaticConfFiles.u_hailstone_golem,
				StaticConfFiles.u_azure_herald,
				StaticConfFiles.u_azurite_lion,
				StaticConfFiles.u_pureblade_enforcer,
				StaticConfFiles.u_ironcliff_guardian,
				StaticConfFiles.u_silverguard_knight,
				StaticConfFiles.u_fire_spitter
		};
		
		String[] deck2Cards = {
				StaticConfFiles.c_blaze_hound,
				StaticConfFiles.c_bloodshard_golem,
				StaticConfFiles.c_entropic_decay,
				StaticConfFiles.c_hailstone_golem,
				StaticConfFiles.c_planar_scout,
				StaticConfFiles.c_pyromancer,
				StaticConfFiles.c_serpenti,
				StaticConfFiles.c_rock_pulveriser,
				StaticConfFiles.c_staff_of_ykir,
				StaticConfFiles.c_windshrike,
		};
		
		String[] deck2Units = {
				StaticConfFiles.u_blaze_hound,
				StaticConfFiles.u_bloodshard_golem,
				StaticConfFiles.u_hailstone_golemR,
				StaticConfFiles.u_planar_scout,
				StaticConfFiles.u_pyromancer,
				StaticConfFiles.u_rock_pulveriser,
				StaticConfFiles.u_serpenti,
				StaticConfFiles.u_windshrike
		};
		
		
		Unit unit3 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_comodo_charger, 3, Unit.class);
		unit3.setPositionByTile(tile[2][4]); 
		BasicCommands.drawUnit(out, unit3, tile[2][4]);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit3, 5);
		BasicCommands.setUnitHealth(out, unit3, 3);
		
		unitOnTile.add(unit3);
		

		Unit unit4 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azure_herald, 4, Unit.class);
		unit4.setPositionByTile(tile[4][3]); 
		BasicCommands.drawUnit(out, unit4, tile[4][3]);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit4, 4);
		BasicCommands.setUnitHealth(out, unit4, 4);

		unitOnTile.add(unit4);
		

		// 5 steps in setting hand cards (sequential extraction, not randomly)
		// draw minicards
		// store cards to minicard[] + set a position of minicard + set minicard id
		// delete deck card
		for(int i=0;i<3;i++) {
			BasicCommands.drawCard(out, Run.humanCard.get(0), i+1, 1);
			Run.humanCard.get(0).getMiniCard().setPosition(i+1);;
			MiniCard.miniCard[i] = Run.humanCard.get(0).getMiniCard();
			MiniCard.miniCard[i].setId(Run.humanCard.get(0).getId());
			Run.humanCard.remove(0);
			
		}	
		
		
		
		

		
	}
	

}

