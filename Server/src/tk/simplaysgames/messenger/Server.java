package tk.simplaysgames.messenger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by SimPlaysGames on 18.11.2016.
 */
public class Server extends JFrame{

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    //Constructor
    public Server() {
        super("Sim's Instant Messenger");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(300, 150);
        setVisible(true);
    }

    //Set up and run the server
    public void startRunning(){
        try{
            server = new ServerSocket(6789, 100);
            while(true){
                try{
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                }
                catch(EOFException eofExceptopn){
                    showMessage("\n Server ended the connection!");
                }
                finally {
                    closeConnection();
                }
            }
        }
        catch (IOException ioExceptione){
            ioExceptione.printStackTrace();
        }
    }

    //Wait for connection, then display connection information
    private void waitForConnection()throws IOException{
        showMessage("Waiting for someone to connect...\n");

        connection = server.accept();
        showMessage(" You are now connected!");
    }

    //Get stream to send and recieve data
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n The streams are setup! \n");
    }

    //During the conversation
    private void whileChatting() throws IOException {
        String message = " You are now connected! ";
        sendMessage(message);
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException classNotFoundException) {
                showMessage("\n Oops, Something weird happened! Please try again");
            }
        }
        while (!message.equals("CLIENT - END"));
        closeConnection();
    }

    //Close streams and sockets when done
    private void closeConnection(){
        showMessage("\n Closing connections... \n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    //Send a message to client
    private void sendMessage(String message){
        try{
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("\nSERVER - " + message);
        }
        catch (IOException ioException){
            chatWindow.append("\n ERROR: Message could not be sent!");
        }
    }

    //Updates the chat window (Shows the message)
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
                new Runnable(){
                    @Override
                    public void run() {
                        chatWindow.append(text);
                    }
                }
        );
    }

    //Change the permission to type (let them, or disallow them)
    private void ableToType (final boolean bool){
        SwingUtilities.invokeLater(
                new Runnable(){
                    @Override
                    public void run() {
                        userText.setEditable(bool);
                    }
                }
        );
    }


}

















