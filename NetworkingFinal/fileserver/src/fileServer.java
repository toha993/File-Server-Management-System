import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class fileServer {


    static final int port = 1833;


    public static void main(String[] args) {

        File directory;

        ServerSocket listener;

        Socket connection;


        File forDirectory = new File("");
        directory = new File(forDirectory.getAbsolutePath());


        JFrame jFrame = new JFrame("Server");
        jFrame.setSize(500, 600);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color panel = new Color(250, 243, 243);
        Color send = new Color(70,130,180);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jPanel.setBackground(panel);

        JLabel frameTitle = new JLabel("File Management System for Server");
        frameTitle.setFont(new Font("Arial", Font.BOLD, 25));
        frameTitle.setBorder(new EmptyBorder(20,0,20,0));
        frameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel listeningPortText = new JLabel("");
        listeningPortText.setFont(new Font("Arial", Font.BOLD, 17));
        listeningPortText.setForeground(send);
        listeningPortText.setOpaque(true);
        listeningPortText.setBorder(new EmptyBorder(50,0,20,0));
        listeningPortText.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon imageIcon = new ImageIcon("filemanagement.png");
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        JLabel images = new JLabel(imageIcon);
        images.setAlignmentX(Component.CENTER_ALIGNMENT);

        jFrame.add(frameTitle);
        jFrame.add(images);
        jFrame.add(listeningPortText);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);

        try {
            listener = new ServerSocket(port);

            listeningPortText.setText("Server is listening on port " + port);
            while (true) {
                connection = listener.accept();
                PrintWriter outgoing;
                BufferedReader incoming;
                String command = "";
                JPanel panelRow = new JPanel();
                panelRow.setBackground(panel);
                panelRow.setLayout(new BoxLayout(panelRow, BoxLayout.X_AXIS));
                JLabel commandText = new JLabel("");
                commandText.setFont(new Font("Arial", Font.BOLD, 15));
                commandText.setBorder(new EmptyBorder(10,0, 10,0));
                panelRow.add(commandText);
                jPanel.add(panelRow);
                jFrame.validate();
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy KK:mm a");
                String formattedDate = myDateObj.format(myFormatObj);
                try {
                    incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    outgoing = new PrintWriter( connection.getOutputStream() );
                    command = incoming.readLine();

                    OutputStream output = null;
                    FileInputStream inpStreamFile = null;
                    BufferedInputStream inpStreamBuffer = null;
                    FileOutputStream outStreamFile = null;
                    BufferedOutputStream outStreamBuffer = null;

                    if (command.equalsIgnoreCase("index")) {
                        commandText.setForeground(Color.black);
                        commandText.setText("Sending Index to "+connection.getInetAddress() + "  at " +formattedDate);
                        String[] fileList = directory.list();
                        for (int i = 0; i < fileList.length; i++)
                        {
                            if(fileList[i].contains("."))
                                outgoing.println(fileList[i]);
                        }
                        outgoing.flush();
                        outgoing.close();
                        if (outgoing.checkError())
                            throw new Exception("Error while transmitting data.");
                    }
                    else if (command.toLowerCase().startsWith("get")){
                        String fileName = command.substring(3).trim();
                        commandText.setForeground(new Color(0, 173, 181));
                        commandText.setText("Sending " + fileName+ " to "+connection.getInetAddress()+ "  at " +formattedDate);
                        File file = new File(directory,fileName);
                        if ( (! file.exists()) || file.isDirectory() ) {
                            outgoing.println("ERROR");
                            outgoing.flush();
                            outgoing.close();
                        }
                        else {
                            outgoing.println("OK");
                            outgoing.println(fileName);
                            outgoing.flush();
                            byte [] mybytearray  = new byte [(int)file.length()];
                            inpStreamFile = new FileInputStream(file);
                            inpStreamBuffer = new BufferedInputStream(inpStreamFile);
                            inpStreamBuffer.read(mybytearray,0,mybytearray.length);
                            output = connection.getOutputStream();
                            output.write(mybytearray,0,mybytearray.length);
                            output.flush();
                            inpStreamBuffer.close();
                            output.close();
                            outgoing.close();
                        }

                        if (outgoing.checkError())
                            throw new Exception("Error while transmitting data.");

                    }
                    else if(command.toLowerCase().startsWith("send")){
                        String filename = incoming.readLine();
                        File file = new File(filename);
                        int current = 0;
                        int bytesRead;  
                        byte [] mybytearray  = new byte [214748364];
                        InputStream is = connection.getInputStream();
                        outStreamFile = new FileOutputStream(file);
                        outStreamBuffer = new BufferedOutputStream(outStreamFile);
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        current = bytesRead;
                        do {
                            bytesRead =
                                    is.read(mybytearray, current, (mybytearray.length-current));
                            if(bytesRead >= 0) current += bytesRead;
                        } while(bytesRead > -1);
                        outStreamBuffer.write(mybytearray, 0 , current);
                        outStreamBuffer.flush();
                        outStreamFile.close();
                        outStreamBuffer.close();
                        commandText.setForeground(new Color(0, 200, 150));
                        commandText.setText("Receiving "+filename    + " From "+ connection.getInetAddress()+ "  at " +formattedDate);
                    }
                    else if (command.toLowerCase().startsWith("delete")) {
                        String filename = command.substring(6).trim();
                        if (filename == "fileserver.iml") {
                            //cant delete the file.
                        } else {
                            File file = new File(filename);
                            file.delete();
                            commandText.setForeground(new Color(132, 84, 96));
                            commandText.setText("Deleting File " + filename + " from " + connection.getInetAddress()+ "  at " +formattedDate);
                        }
                    }
                    else {
                        outgoing.println("ERROR unsupported command");
                        outgoing.flush();
                    }
                }
                catch (Exception e) {
                    commandText.setText("Connected to "+ connection.getInetAddress()+ "  at " +formattedDate );
                }

                    try {
                        connection.close();
                    }
                    catch (IOException e) {

                    }
            }
        }
        catch (Exception e) {
            System.out.println("Error:  " + e);
        }

    }



}
