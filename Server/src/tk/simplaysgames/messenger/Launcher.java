package tk.simplaysgames.messenger;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Created by SimPlaysGames on 18-Nov-16.
 */
public class Launcher {


    public static void main(String[] args) {
        Server server = new Server();
        server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        server.startRunning();
    }
}
