package tk.simplaysgames.client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by SimPlaysGames on 18-Nov-16.
 */
public class Client extends JFrame{

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message;
    private String serverIP;
    private Socket connection;

    public Client(String host){
        super("Sim's Instant Messenger");

        serverIP = host;

        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }
                }
        );

        add(userText, BorderLayout.SOUTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300,150);
        setVisible(true);
        startRunning();
    }

    //Start services
    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }
        catch (EOFException exception){
            showMessage("\n Client terminated the connection");
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    //Connect to server
    public void connectToServer() throws IOException {
        showMessage("\n Attempting to connection! \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("You are now connected to the server!\n");
    }

    //Setting up the streams
    public void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now connected \n");
    }

    //Things that happen while chatting
    private void whileChatting() throws IOException {
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }
            catch(ClassNotFoundException exception){
                showMessage("\n Oops, Something weird happened! Please try again");
            }
        }
        while(!message.equals("SERVER - END"));
        closeConnection();
}

    //Closing connections
    public void closeConnection() {
        showMessage("\n Closing connections... ");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
            showMessage("Connections closed");
        }
        catch(IOException exception){
            exception.printStackTrace();
        }
    }

    //Send messages
    private void sendMessage(String message){
        try{
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\nCLIENT - " + message);
        }
        catch(IOException exception){
            chatWindow.append("\n ERROR: Message could not be sent!");
        }
    }

    //Updates the chat window (Shows the message)
    private void showMessage(final String message){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(message);
                    }
                }

        );
    }

    private void ableToType (final boolean bool) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        userText.setEditable(bool);
                    }
                }
        );
    }
}
