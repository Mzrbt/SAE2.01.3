package app.controller;


import app.model.entity.Entity;
import app.model.entity.Player;
import app.model.fight.Fight;
import app.model.fight.Spell;
import app.model.map.Place;
import app.model.map.World;

import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Game {
    private final World world;
    private final Player player;
    private Place currentPlace;

    private final Scanner scan;

    public Game(World world, Player player) {
        this.world = world;
        this.player = player;
        this.currentPlace = world.getPlaces().get(0);
        this.scan = new Scanner(System.in);
    }

    public void play() {
        boolean play = true;
        System.out.printf("Welcome in %s World !%n", world.getName());

        while (play) {
            System.out.printf("You are at %s.%n", currentPlace.getName());
            System.out.println(currentPlace.getDescription());

            Action a = getPlayerAction();
            switch (a) {
                case MOVE -> {
                    Place p = getNextPlace();
                    this.currentPlace = p;

                    if (p.isDefeat()) {
                        System.out.println("You lose. :(");
                        exit();
                    }

                    if (p.isEnd()) {
                        System.out.println("You win ! :D");
                        exit();
                    }

                    if (p.hasMonster() && p.getMonster().isAlive()) {
                        System.out.println("A fight begin !");
                        fight(new Fight(player, p.getMonster()));
                    }
                }

                case QUIT -> {
                    exit();
                    play = false;

                }

                default -> {
                }
            }
        }
    }

    private void fight(Fight fight) {
        Entity winner = null;

        while (winner == null) {
            if (fight.newTurn() != null) {
                break;
            }

            System.out.printf("%nPlayer : %s%n", player);
            System.out.printf("Monster : %s%n%n", fight.getMonster());

            int choice = getChoice(() -> {
                System.out.println("What do you want to do ?");
                System.out.println(" 0 - Basic attack");

                for (int i = 0; i < player.availableSpells().size(); i++) {
                    Spell spell = player.availableSpells().get(i);
                    System.out.printf("%2d - %s (%d mana)%n", (i + 1), spell.getClass().getSimpleName(), spell.getCost());
                }

            }, player.availableSpells().size() + 1);

            Spell spell = (choice == 0) ? null : player.availableSpells().get(choice - 1);

            winner = fight.turn(spell);
        }

        if (fight.playerWin()) {
            System.out.println("Player's win !");
        } else {
            System.out.println("Player's lose ! :(");
            exit();
        }
    }

    public Place getNextPlace() {
        List<Map.Entry<Place, Integer>> possibilities = currentPlace.getPaths().entrySet().stream().toList();

        int choice = getChoice(() -> {
            int index = 0;
            System.out.println("Where do you want to go ?");
            for (Map.Entry<Place, Integer> entry : possibilities) {
                System.out.printf("\t%2d - %s (%d km)%n", index, entry.getKey(), entry.getValue());
                index++;
            }
        }, currentPlace.getPaths().size());

        return possibilities.get(choice).getKey();
    }

    public Action getPlayerAction() {
        int choice = getChoice(() -> {
            System.out.println("What do you want to do ?");
            for (int i = 0; i < Action.values().length; i++) {
                System.out.printf("\t%2d - %s%n", i, Action.values()[i].getDescription());
            }
        }, Action.values().length);

        return Action.values()[choice];
    }

    public void exit() {
        System.out.println("Good bye!");
        System.exit(0);
    }

    private int getChoice(Runnable function, int nbChoice) {
        int choice = -1;
        while (choice < 0 || choice >= nbChoice) {
            function.run();

            try {
                choice = scan.nextInt();
            } catch (Exception ignored) {
                scan.next();
                System.out.printf("Please write a number between 0 & %d%n", nbChoice);
            }
        }

        return choice;
    }
}
