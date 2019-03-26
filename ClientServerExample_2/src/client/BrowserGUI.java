package ser321.http.client;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.net.URL;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

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
 * Purpose: Sample Java Swing view class. BrowserGUI constructs the view components
 * for a simple browser. This class is extended by the client controller which is
 * the BrowserStudent class. Browser defines the call-backs for the UI controls.
 * It contains sample control functions that respond to button clicks.
 *
 * This software is meant to run on Debian Wheezy Linux
 * <p/>
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Tim Lindquist (Tim.Lindquist@asu.edu) CIDSE - Software Engineering,
 *                       IAFSE, ASU at the Polytechnic campus
 * @file    BrowserGUI.java
 * @date    February, 2018
 **/
public class BrowserGUI extends JFrame  {

   private static final boolean debugOn = true;

// provide extending (controller) class access to the htmlpane and url
   protected JEditorPane htmlPane;
   protected String pathToRoot;
   protected JTextField urlTF;
   protected JButton displayButt;
   protected JButton backButt;
   protected JButton homeButt;
   
   public BrowserGUI(String path) {
      super("Ser321 Browser");
      JPanel jp = new JPanel(new FlowLayout());
      urlTF = new JTextField("http://localhost:8080/Ser321/index.html",40);
      jp.add(urlTF);
      displayButt = new JButton("Show/Refresh");
      jp.add(displayButt);
      backButt = new JButton("Back");
      jp.add(backButt);
      homeButt = new JButton("Home");
      jp.add(homeButt);
      getContentPane().add("North",jp);
      
      //Create the HTML viewing pane.
      htmlPane = new JEditorPane();
      htmlPane.setEditable(false);
      JScrollPane htmlView = new JScrollPane(htmlPane,
                                             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                             JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

      getContentPane().add("Center",htmlView);

      Dimension minimumSize = new Dimension(100, 100);
      htmlView.setMinimumSize(minimumSize);
      this.setPreferredSize(new Dimension(800, 650));
      pack();
      setVisible(true);
   }

   private void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }

}
