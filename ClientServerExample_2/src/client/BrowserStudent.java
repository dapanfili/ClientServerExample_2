
package ser321.http.client;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JOptionPane;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import javax.swing.JEditorPane;
import javax.swing.text.EditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.tree.TreePath;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Stack;
import java.io.*;
import java.util.*;
import java.io.File;
import java.io.ByteArrayInputStream;
//import java.io.StringBufferInputStream;
/**
 * Copyright (c) 2018 Tim Lindquist,
 * Software Engineering,
 * Arizona State University at the Polytechnic campus
 * <p/>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation version 2
 * of the License.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but without any warranty or fitness for a particular purpose.
 * <p/>
 * Please review the GNU General Public License at:
 * http://www.gnu.org/licenses/gpl-2.0.html
 * see also: https://www.gnu.org/licenses/gpl-faq.html
 * so you are aware of the terms and your rights with regard to this software.
 * Or, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,USA
 * <p/>
 * Purpose: Sample Java Swing controller class. BrowserGUI constructs the view components
 * for a simple broswer GUI. This class is extends the GUI to provide the controller.
 * It contains sample control functions that respond to button clicks and hotline clicks
 *
 * This software is meant to run on Debian Wheezy Linux
 * <p/>
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Tim Lindquist (Tim.Lindquist@asu.edu) CIDSE - Software Engineering,
 *                       IAFSE, ASU at the Polytechnic campus
 * @file    BrowserStudent.java
 * @date    February, 2018
 * 
 * 
 * 
 * 
 * 
 * 
 * @author Danielle Panfili (dpanfili@asu.edu) CIDSE - Software Engineering,
 * @file    BrowserStudent.java
 * @date    February, 2018
 **/
 
 
public class BrowserStudent extends BrowserGUI
                            implements ActionListener,HyperlinkListener {

   private static final boolean debugOn = true;

   private URL helpURL;
   Stack stack = new Stack();

   public BrowserStudent(String url) {
      super(".");
      WindowListener wl = new WindowAdapter() {
         public void windowClosing(WindowEvent e) {System.exit(0);}
      };
      this.addWindowListener(wl);
      displayButt.addActionListener(this);
      homeButt.addActionListener(this);
      backButt.addActionListener(this);
      initHelp(url);
      htmlPane.setEditable(false);
      htmlPane.addHyperlinkListener(this);
   }
    
    //Used for link clicks from page to page
   public void hyperlinkUpdate(HyperlinkEvent e) {
      if(HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
         debug("activated a hyperlink.");
         try {
             //Set page url 
            htmlPane.setPage(e.getURL());
            urlTF.setText(e.getURL().toString());
	    stack.push(e.getURL());
         } catch (IOException e1) {
            e1.printStackTrace();
         }
      }else if(HyperlinkEvent.EventType.ENTERED.equals(e.getEventType())) {
         //debug("entered a hyperlink.");
         // Do something?
      }else if(HyperlinkEvent.EventType.EXITED.equals(e.getEventType())) {
         //debug("exited a hyperlink.");
         // Do something?
      }
   }

   /**
    * actionPerformed is defined by the ActionListener interface.
    * An object of Browser registers itself to hear about action events
    * caused by the <b>Button Clicks</b> and <b>Menu selecions (none here)</b>.
    * @param ActionEvent the event object created by the source of the
    * button push (the JButton object.)
    */
   public void actionPerformed(ActionEvent e) {
      try{
         if (e.getActionCommand().equals("Show/Refresh")){
            debug("Show/Refresh Page "+urlTF.getText());
            String urlStr = urlTF.getText();
            urlTF.setText(urlStr);
            this.displayURL(new URL(urlStr));
            stack.push(urlStr);
         }else if(e.getActionCommand().equals("Home")){
            debug("Go to home page clicked");
            urlTF.setText("http://pooh.poly.asu.edu/Ser321/index.html");
            String urlStr = urlTF.getText();
            this.displayURL(new URL(urlStr));
            stack.push(urlStr);

         }
         else if(e.getActionCommand().equals("Back")){
            debug("Go to back page clicked");
	    stack.pop();
            Object newURl = stack.pop();
            System.out.print("URL is "+ newURl);
            String strURL = newURl.toString();
            System.out.println("STR url is: "+ strURL);
            urlTF.setText(strURL);
            String urlStr = urlTF.getText();
            this.displayURL(new URL(urlStr));
            stack.push(urlStr);


         }
      }catch (Exception ex) {
         JOptionPane.showMessageDialog(this, "Exception: "+ex.getMessage());
         ex.printStackTrace();
      }
   }

      private void initHelp(String url) {
      String s = null;
      try {
         s = "file://" 
            + System.getProperty("user.dir")
            + System.getProperty("file.separator")
            + "TreeDemoHelp.html";
         debug("Help URL is " + s);
	 String urlNew = url;
         helpURL = new URL(urlNew);
         displayURL(helpURL);
         stack.push(helpURL);
      } catch (Exception e) {
         System.err.println("Couldn't create help URL: " + s + " exception: "
                            +e.getMessage());
      }
   }

   private void displayURL(URL url) {
      try {
         htmlPane.setPage(url);
      } catch (IOException e) {
         System.err.println("Attempted to read a bad URL: " + url);
      }
   }
   private void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }



   public static void main(String[] args) {
      System.out.println("syntax: java -cp classes ser321.http.client.BrowserStudent");
      new BrowserStudent(args[0]);
   }
}

