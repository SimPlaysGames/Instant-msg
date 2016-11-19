package tk.simplaysgames.messenger;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Created by SimPlaysGames on 18-Nov-16.
 */
public class Launcher {
    static Server server = new Server();

    public static void main(String[] args) {
        //server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Runtime.getRuntime().addShutdownHook(
                new Thread(){
                    public void run(){
                        server.closeConnection();
                    }
                }
        );
    }
}
