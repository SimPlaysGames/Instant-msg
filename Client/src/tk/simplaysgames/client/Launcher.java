package tk.simplaysgames.client;

import javax.swing.*;

/**
 * Created by SimPlaysGames on 18-Nov-16.
 */
public class Launcher {

    public static void main(String[] args) {

        Client client = new Client("127.0.0.1");
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Runtime.getRuntime().addShutdownHook(
                new Thread(){
                    public void run(){
                        client.closeConnection();
                    }
                }
        );
    }
}
