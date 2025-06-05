package app;

import app.controller.Game;
import app.model.parser.WorldIO;
import app.model.entity.Player;
import app.model.map.World;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        World world = WorldIO.loadWorld(Main.class.getResourceAsStream("Monde1.json"));
        Player player = new Player(100,100,20,20, 100);
        Game game = new Game(world, player);

        game.play();
    }
}