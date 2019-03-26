
package ser321.http.server;
import java.io.* ;
import java.net.* ;
import java.util.* ;




/*
 * @author Danielle Panfili (dpanfili@asu.edu) CIDSE - Software Engineering,
 * @file    BrowserStudent.java
 * @date    February, 2018
*/
public final class webserver
{
    public static void main(String argv[]) throws Exception
    {
        //Setting up the port number that is passed in 
        if (argv.length != 1) {
            System.out.println("Usage: java ser321.sockets.ThreadedEchoServer"+
                    " [portNum]");
            System.exit(0);
        }
        int port = Integer.parseInt(argv[0]);
        if (port <= 1024) port=8888;
	System.out.println("Server running....");

        // Set up the socket for listening
        ServerSocket sock = new ServerSocket(port);

        //Http requests are in infinate loop until terminated
        while (true) {
            // listen for TCP request
            Socket connectionSocket = sock.accept();

            // process HTTP request
            HttpRequest request = new HttpRequest( connectionSocket );

            //Use Java thread to create new thread so you can handle multiple requests
            Thread thread = new Thread(request);

            // Start the thread.
            thread.start();
        }
        
    }
    
}

/*
* Class handles the http request so that the http page can display in the GUI
*/
final class HttpRequest implements Runnable
{
    final static String CRLF = "\r\n";//returning carriage return (CR) and a line feed (LF)
    //Create socket
    Socket socket;

    // Constructor method
    public HttpRequest(Socket socket) throws Exception
    {
        this.socket = socket;
    }

    // Implement the run method here. Just runs the process Request method
    public void run()
    {
        try {processRequest();
            
        }catch (Exception e) 
            {System.out.println(e);
                
            }
    }
    
    //Method will process the http request and display results on browswer
    private void processRequest() throws Exception
    {
        // Get socket input and output streams
        InputStream in = socket.getInputStream();
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        BufferedReader br = new BufferedReader(
                            new InputStreamReader(
                            socket.getInputStream()));//reads the input data
        
        //String that will set up the path for www/Ser321
        //String partOfrequest = "www/Ser321/";
        // Get the request line of the HTTP request message. Gets path/file.html usually
        String requestLine = br.readLine();
        //Set full request line to be the www/Ser321/file
        //String fullRequest = partOfrequest + requestLine;
       
        System.out.println(requestLine);
       

        //Getting the file name from the request line. Does not pull the www/Ser321. 
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken(); //removes the GET
        String fileName = tokens.nextToken();

        //Set the directory with www/Ser321 and file name that was tokenized
        fileName = "www/" + fileName;

        //Setting up file input stream to open and use for content length
        File file = null;
        FileInputStream fistream = null;
        boolean fileExists = true;
        try {
            file = new File(fileName);
            fistream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }

        //Construct the response message. needed for assignment.
        String stat = null;
        String contentTypeLine = null;
        String contentLength = null;
        String entityBody = null;

        if (fileExists) {
            stat = "HTTP/1.0 200 OK" + "\n"; //common success message
            contentTypeLine = "Content-type: " + contentType( fileName ) + "\n";//content info
            contentLength = "Content-Length: " + file.length() + "\n";
            }
        else {
            stat = "HTTP/1.0 404 Not Found" + CRLF;//common error message
            contentTypeLine = "Content-type: " + "text/html" + CRLF;//content info
            entityBody = "<HTML>" +
                    "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                    "<BODY>Not Found</BODY></HTML>";
        }


        //Send the status to output stream.
        os.writeBytes(stat);

        //Send the content type to output stream.
        os.writeBytes(contentTypeLine);

        //Send the content length to output stream.
        os.writeBytes(contentLength);

        //Blank line indicates end of headers
        os.writeBytes(CRLF);



        //Send the entity body.
        if (fileExists) {
            sendBytes(fistream, os);

            fistream.close();
        } else {
            os.writeBytes(stat);//Send the status.
            os.writeBytes(entityBody);//Send the an html error message info body.
            os.writeBytes(contentTypeLine);//Send the content type.
            os.writeBytes(contentLength);
        }

        
        
        //Prints on the command line
        System.out.println("*****");
        System.out.println("File: "+fileName);//print out file request to console
        System.out.println("*****");
        // Get and display the header lines.
        System.out.println("Header lines: ");
        String headerLine = null;
        while ((headerLine = br.readLine()).length() != 0) {
            System.out.println(headerLine);
        }
        //Print Response and Content
        System.out.println("*****");
        System.out.println("Content And Response: \n");
        System.out.println("HTTP Response: "+stat);
        System.out.println(contentTypeLine);
        System.out.println(contentLength);




        // Close streams and socket.
        os.close();
        br.close();
        socket.close();



    }
    //return the file types for the content type
    private static String contentType(String fileName)
    {
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {return "text/html";}
        if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {return "image/jpeg";}
        if(fileName.endsWith(".gif")) {return "image/gif";}
	if(fileName.endsWith(".png")) {return "image/png";}
	if(fileName.endsWith(".rtf")) {return "application/rtf";}
        return "application/octet-stream";
    }


    //set up input output streams
    private static void sendBytes(FileInputStream fistream, OutputStream os) throws Exception
    {
        // 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;

        // Copy requested file into the socket's output stream.
        while((bytes = fistream.read(buffer)) != -1 )// end of file with -1
        {
            os.write(buffer, 0, bytes);
        }}
}
