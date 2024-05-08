/* autogenerated by Processing revision 1293 on 2024-05-08 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import javax.swing.JOptionPane;
import oscP5.*;
import netP5.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class QlabHotkeys extends PApplet {





OscP5 oscP5;
NetAddress qlabAddress;

boolean[] keyStates = new boolean[256]; // Array to store key states
boolean[] messageSent = new boolean[256]; // Array to track whether message has been sent
PFont font;

boolean escapePressed = false;
long escapePressTime = 0;

public void setup() {
  // Ask if the user wants to use localhost IP
  int option1 = JOptionPane.showConfirmDialog(null, "Would you like to use the localhost IP?", "IP Configuration", JOptionPane.YES_NO_OPTION);
  String ipAddress;
  if (option1 == JOptionPane.YES_OPTION) {
    ipAddress = "localhost";
  } else {
    ipAddress = JOptionPane.showInputDialog(null, "Enter the IP address:", "IP Configuration", JOptionPane.QUESTION_MESSAGE);
  }
  
  // Ask if the user wants to use the default port
  int option2 = JOptionPane.showConfirmDialog(null, "Would you like to use the default port (53000)?", "Port Configuration", JOptionPane.YES_NO_OPTION);
  int port;
  if (option2 == JOptionPane.YES_OPTION) {
    port = 53000;
  } else {
    String portString = JOptionPane.showInputDialog(null, "Enter the port number:", "Port Configuration", JOptionPane.QUESTION_MESSAGE);
    port = Integer.parseInt(portString);
  }
  
  /* size commented out by preprocessor */;
  font = createFont("Arial", 12); // Specify a font that includes a wide range of characters
  textFont(font);
  
  oscP5 = new OscP5(this, port); // Adjust port number if needed
  qlabAddress = new NetAddress(ipAddress, port); // Adjust port and IP for QLab

  textAlign(CENTER, CENTER);
}

public void draw() {
  background(0);
  displayKeyStates();
}

public void displayKeyStates() {
  int cols = 10; // Number of columns
  int rows = 5; // Number of rows
  
  int cellWidth = width / cols;
  int cellHeight = height / rows;
  
  String keys = "1234567890QWERTYUIOPASDFGHJKL ZXCVBNM[]-=,./;'#\\";
  
  for (int i = 0; i < keys.length(); i++) {
    char keyChar = keys.charAt(i);
    int index = (int) keyChar;
    if (index < 256) {
      if (keyStates[index]) {
        fill(0, 255, 0);
      } else {
        fill(255);
      }
      int x = i % cols;
      int y = i / cols;
      rect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
      fill(0);
      text(keyChar, x * cellWidth + cellWidth / 2, y * cellHeight + cellHeight / 2);
    }
  }
}

public void keyPressed() {
  int keyIndex = keyCode;
  if (keyIndex == 32) { // Space bar
    sendOSC("/go");
  } else if (keyIndex == 38) {
    sendOSC("/playhead/previous");
  } else if (keyIndex == 40) {
    sendOSC("playhead/next");
  } else if (keyIndex == 49) { // Escape key
    long now = millis();
    if (escapePressed && now - escapePressTime < 500) {
      sendOSC("/hardstop");
      escapePressed = false;
    } else {
      escapePressed = true;
      escapePressTime = now;
      sendOSC("/panic");
    }
  } else if (keyIndex >= 32 && keyIndex <= 126) { // Monitor specified keys
    if (!messageSent[keyIndex]) {
      keyStates[keyIndex] = true; // Set the key state to true
      char keyChar = (char) keyIndex;
      sendOSC("/cue/" + keyChar + "/go"); // Send OSC command for key press
      messageSent[keyIndex] = true; // Mark the message as sent
    }
  }
}

public void keyReleased() {
  int keyIndex = keyCode;
  if (keyIndex >= 32 && keyIndex <= 126) { // Monitor specified keys
    keyStates[keyIndex] = false; // Set the key state to false
    char keyChar = (char) keyIndex;
    sendOSC("/cue/" + keyChar + "a/go"); // Send OSC command for key release
    messageSent[keyIndex] = false; // Reset the message sent flag
  }
}

public void sendOSC(String address) {
  OscMessage msg = new OscMessage(address);
  oscP5.send(msg, qlabAddress);
}


  public void settings() { size(400, 400); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "QlabHotkeys" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
