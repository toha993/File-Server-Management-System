## File Server Management System

This is an application which :
- uses TCP for file transfer.
- has a user interface built using Java AWT and Java Swing.
- is built using Java Socket Programming.
- allows a user to connect to a server at once. 
- allows server to connect with a user given port number
- allows client to see the files he/she can download or delete from server.
- allows client to upload files to the server from his local machine.



## Getting Started

1. Firstly,one has to open both project in Intellij IDEA in two different window.Now he has to run the file fileServer.java in a window and then run fileClient.java in another window.
2. In client gui, Client has to give server port number in the textfield and click the connect button. **

**For Uploading File:
1. For browsing files, click Choose File and select a file fmom the client specified directory and click open.
2. Now after clicking the send button it will be uploaded to the server.

**For Downloading File:
1. For browsing files, click Server Files and select a file fmom the server specified directory and click open.
2. Now after clicking the download button it will be downloaded from the server to the Downloadedfiles folder of the client's local machine.

**For Deleting File:
1. For browsing files, click Server Files and select a file fmom the server specified directory and click open.
2. Now after clicking the delete button it will be deleted from the server.

**Server GUI:
1. Server can see all the commands from the client
2. Server can detect at when the commands were executed.

N.B. If a file was selected and either send/delete/download was clicked, user has to start from browsing the files again.


## Prerequisites

If you do not have Intellij IDEA have installed, refer to this (https://www.jetbrains.com/idea/download/)

##Developers

1. Mutasim Billah Toha(2017831018)
2. Israt Jahan(2017831033)
