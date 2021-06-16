import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.Font;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class fileClient {

    private static boolean available(int port) {
        try (Socket ignored = new Socket("127.0.0.1", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }


    public static void main(String[] args) {

        final Socket[] connection = new Socket[1];        // Socket for communicating with that "127.0.0.1.
        final PrintWriter[] outgoing = new PrintWriter[1];     // Stream for sending a command to the server.
        final BufferedReader[] incoming = new BufferedReader[1];


        final int[] bytesRead = new int[1];
        final int[] current = {0};
        final FileOutputStream[] outStream = {null};
        final BufferedOutputStream[] outStreamBuffer = {null};
        final FileInputStream[] inpStream = {null};
        final BufferedInputStream[] inpStreamBuffer = {null};
        final OutputStream[] os = {null};



        Color send = new Color(0, 173, 181);
        Color choose = new Color(57, 62, 70);
        Color download = new Color(0, 200, 150);
        Color delete = new Color(34, 40, 49);
        Color connect = new Color(47, 79, 79);

        ImageIcon imageIcon = new ImageIcon("filemanagement.png");
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        JLabel images = new JLabel(imageIcon);
        images.setAlignmentX(Component.CENTER_ALIGNMENT);


        String commandIndex = "INDEX";
        String commandGet = "GET ";
        String commandSend = "SEND ";

        JFrame jFrame = new JFrame("Client");
        jFrame.setSize(680, 600);
        Color bg = new Color(238, 238, 238);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
         jFrame.getContentPane().setBackground(bg);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel clientTitle = new JLabel("File Management System for Client");
        clientTitle.setFont(new Font("Arial", Font.BOLD, 25));
        clientTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        clientTitle.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel serverPort = new JLabel("Server Port Number");
        serverPort.setFont(new Font("Arial", Font.BOLD, 15));
        serverPort.setBorder(new EmptyBorder(20, 0, 10, 0));
        serverPort.setAlignmentX(Component.CENTER_ALIGNMENT);

//        final JTextField portInput;
        JTextField portInput = new JTextField("");
        portInput.setFont(new Font("Arial",Font.PLAIN,18));
        portInput.setBorder(new EmptyBorder(10, 20, 10, 10));

//        connectionStatus= new JTextField("Not Connected");
//        connectionStatus.setBounds(50,100, 50,20);


        JLabel connectionStatus = new JLabel("Status: Not Connected");
        connectionStatus.setLocation(100, 100);
        connectionStatus.setForeground(Color.RED);
        connectionStatus.setFont(new Font("Monospace", Font.BOLD, 16));
        connectionStatus.setBorder(new EmptyBorder(20, 10, 10, 10));
        connectionStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel empty = new JLabel(" ");
        empty.setBounds(10,10,10,10);
        empty.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton connectButton = new JButton("Connect");
        connectButton.setBorder(new EmptyBorder(10, 10, 10, 10));
        connectButton.setBackground(connect);
        connectButton.setForeground(Color.white);
        connectButton.setOpaque(true);
        connectButton.setPreferredSize(new Dimension(35, 35));
        connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (available(parseInt(portInput.getText()))) {
                    connectionStatus.setText("Status: Port is not open");
                } else {
                    connectionStatus.setForeground(new Color(52, 103, 81));
                    connectionStatus.setText("Status: Connected to the port " + portInput.getText());

                }
            }
        });

        JLabel fileSelectedSend = new JLabel("Choose a file to send.");
        fileSelectedSend.setFont(new Font("Arial", Font.BOLD, 20));
        fileSelectedSend.setBorder(new EmptyBorder(25, 0, 0, 0));
        fileSelectedSend.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(20, 0, 60, 0));

        JButton sendButton = new JButton("Send File");
        sendButton.setBackground(send);
        sendButton.setForeground(Color.white);
        sendButton.setOpaque(true);
        sendButton.setPreferredSize(new Dimension(150, 40));
        sendButton.setFont(new Font("Arial", Font.BOLD, 20));

        JButton chooseButton = new JButton("Choose File");
        chooseButton.setPreferredSize(new Dimension(150, 40));
        chooseButton.setBackground(choose);
        chooseButton.setForeground(Color.white);
        chooseButton.setOpaque(true);
        chooseButton.setFont(new Font("Arial", Font.BOLD, 20));


        JLabel fileSelectedServer = new JLabel("Choose file to Download or Delete from server.");
        fileSelectedServer.setFont(new Font("Arial", Font.BOLD, 20));
        fileSelectedServer.setBorder(new EmptyBorder(0, 0, 0, 0));
        fileSelectedServer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jserverButton = new JPanel();
        jserverButton.setBorder(new EmptyBorder(20, 0, 100, 0));

        JButton showServerButton = new JButton("Server Files");
        showServerButton.setBackground(choose);
        showServerButton.setForeground(Color.white);
        showServerButton.setOpaque(true);
        showServerButton.setPreferredSize(new Dimension(180, 40));
        showServerButton.setFont(new Font("Arial", Font.BOLD, 20));

        JButton downloadButton = new JButton("Download");
        downloadButton.setBackground(send);
        downloadButton.setForeground(Color.white);
        downloadButton.setOpaque(true);
        downloadButton.setPreferredSize(new Dimension(180, 40));
        downloadButton.setFont(new Font("Arial", Font.BOLD, 20));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(delete);
        deleteButton.setForeground(Color.white);
        deleteButton.setOpaque(true);
        deleteButton.setPreferredSize(new Dimension(180, 40));
        deleteButton.setFont(new Font("Arial", Font.BOLD, 20));

        jpButton.add(sendButton);
        jpButton.add(chooseButton);
        jserverButton.add(showServerButton);
        jserverButton.add(downloadButton);
        jserverButton.add(deleteButton);

        final File[] fileToSend = new File[1];
        final File[] fileServer = new File[1];

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File("");
                String currentDirectoryPath = file.getAbsolutePath();
                JFileChooser jFileChooser = new JFileChooser(currentDirectoryPath);
                jFileChooser.setDialogTitle("Choose a file to send.");
                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    fileSelectedSend.setText("The file you want to send is " + fileToSend[0].getName());
                }
            }
        });


        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileToSend[0] == null) {
                    fileSelectedSend.setText("Please choose the file first.");
                } else {
                    try {
                        connection[0] = new Socket("127.0.0.1", parseInt(portInput.getText()));
                        incoming[0] = new BufferedReader(
                                new InputStreamReader(connection[0].getInputStream()));
                        outgoing[0] = new PrintWriter(connection[0].getOutputStream());
                        outgoing[0].println(commandSend);
                        outgoing[0].println(fileToSend[0].getName());
                        outgoing[0].flush();
                        File myFile = new File (fileToSend[0].getName());
                        System.out.println(myFile);
                        byte [] mybytearray  = new byte [(int)myFile.length()];
                        inpStream[0] = new FileInputStream(myFile);
                        inpStreamBuffer[0] = new BufferedInputStream(inpStream[0]);
                        inpStreamBuffer[0].read(mybytearray,0,mybytearray.length);
                        os[0] = connection[0].getOutputStream();
                        os[0].write(mybytearray,0,mybytearray.length);
                        os[0].flush();
                        outgoing[0].close();
                        inpStreamBuffer[0].close();
                        os[0].close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        showServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    connection[0] = new Socket("127.0.0.1", parseInt(portInput.getText()));
                    incoming[0] = new BufferedReader(
                            new InputStreamReader(connection[0].getInputStream()));
                    outgoing[0] = new PrintWriter(connection[0].getOutputStream());
                    outgoing[0].println(commandIndex);
                    outgoing[0].flush();
                    File filefordirectory = new File("ServerFiles");
                    filefordirectory.mkdir();
                    String directory = filefordirectory.getAbsolutePath();
                    while (true) {
                        String text = incoming[0].readLine();
                        if (text == null)
                            break;
                        File filelist = new File(directory, text);
                        filelist.createNewFile();
                    }
                    outgoing[0].close();
                    JFileChooser jFileChooser = new JFileChooser(directory);
                    jFileChooser.setDialogTitle("Choose a file to Download.");
                    if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        fileServer[0] = jFileChooser.getSelectedFile();
                        fileSelectedServer.setText("The file you choose from server is: " + fileServer[0].getName());
                    }
                    File[] files = filefordirectory.listFiles();
                    for (File file : files) {
                        file.delete();
                    }
                    filefordirectory.delete();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (fileServer[0] == null) {
                        fileSelectedServer.setText("Please choose the file first.");
                    } else {
                        connection[0] = new Socket("127.0.0.1", parseInt(portInput.getText()));
                        incoming[0] = new BufferedReader(
                                new InputStreamReader(connection[0].getInputStream()));
                        outgoing[0] = new PrintWriter(connection[0].getOutputStream());
                        outgoing[0].println(commandGet + fileServer[0].getName());
                        outgoing[0].flush();
                        String message = incoming[0].readLine();

                        if (!message.equalsIgnoreCase("OK")) {
                            System.out.println("File not found on server.");
                            System.out.println("Message from server: " + message);
                            return;
                        }

                        File fileto = new File("");
                        String path = fileto.getAbsolutePath() + "/DownloadedFiles";
                        PrintWriter fileOut;  // For writing the received data to a file.
                        String filename = incoming[0].readLine();
                        File downoladedfile = new File(path, filename);
                        byte [] mybytearray  = new byte [21474836];
                        InputStream is = connection[0].getInputStream();
                        outStream[0] = new FileOutputStream(downoladedfile);
                        outStreamBuffer[0] = new BufferedOutputStream(outStream[0]);
                        bytesRead[0] = is.read(mybytearray,0,mybytearray.length);
                        current[0] = bytesRead[0];
                        do {
                            bytesRead[0] =
                                    is.read(mybytearray, current[0], (mybytearray.length- current[0]));
                            if(bytesRead[0] >= 0) current[0] += bytesRead[0];
                        } while(bytesRead[0] > -1);
                        outStreamBuffer[0].write(mybytearray, 0 , current[0]);
                        outStreamBuffer[0].flush();
                        outStream[0].close();
                        outStreamBuffer[0].close();
                        outgoing[0].close();
                        fileSelectedServer.setText(fileServer[0].getName() + " has been downloaded from the server.");
                        fileServer[0] = null;
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (fileServer[0] == null) {
                        fileSelectedServer.setText("Please choose the file first.");
                    } else {
                        connection[0] = new Socket("127.0.0.1", parseInt(portInput.getText()));
                        incoming[0] = new BufferedReader(
                                new InputStreamReader(connection[0].getInputStream()));
                        outgoing[0] = new PrintWriter(connection[0].getOutputStream());
                        outgoing[0].println("DElETE " + fileServer[0].getName());
                        outgoing[0].flush();
                        outgoing[0].close();
                        fileSelectedServer.setText(fileServer[0].getName() + " file is deleted successfully.");
                        // fileSelectedServer.setText(fileServer[0].getName()+" has been downloaded from the server.");
                        fileServer[0] = null;
                    }


                } catch (Exception exception) {
                    exception.printStackTrace();
                }


            }
        });



        jFrame.add(clientTitle);
        jFrame.add(images);
        jFrame.add(serverPort);
        jFrame.add(portInput);
        jFrame.add(empty);
        jFrame.add(connectButton);
        jFrame.add(connectionStatus);
        jFrame.add(fileSelectedSend);
        jFrame.add(jpButton);
        jFrame.add(fileSelectedServer);
        jFrame.add(jserverButton);
        jFrame.setVisible(true);


    }


}
