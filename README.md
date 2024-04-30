# qlabHotkeys
A Java application to run in front of qlab to allow all keyboard keys to be used as a hotkey.

This app is available for all platforms and runs on Java. It allows the user to run in front of qlab and use all keys as hotkeys via OSC.

When a key is pressed an osc message is sent to the workspace, when a key is released a second message is then sent with an a after it.

EG
Users presses G, message sent to qlab to trigger the cue labelled G, when released a message is sent to trigger cue Ga.

On load up the users is offered options of which IP address to use and which port to send the messages. It also offered up the use of localhost and the default port of 53000 for ease. 
