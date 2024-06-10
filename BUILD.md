<pre>
  <b>   BUILDING THE SERVER</b>
  -----(
  Prerequirements:
  IntelliJ IDEA    | <a href="https://www.jetbrains.com/idea/">https://www.jetbrains.com/idea/</a>
  for Windows 7      <a href="https://www.jetbrains.com/idea/download/other.html#2022.3">https://www.jetbrains.com/idea/download/other.html#2022.3</a>
  --
  JDK 17           | <a href="https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html">https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html</a>
  --
  XAMPP            | <a href="https://www.apachefriends.org/">https://www.apachefriends.org/</a>
  --
  Database file    | <a href="https://github.com/bjuonday/gtanks_server_remake/blob/master/db_gtanks.sql">https://github.com/bjuonday/gtanks_server_remake/blob/master/db_gtanks.sql</a>
  -----)

<b>0. Gradle (apart of JDK 17)</b>
  0.1. Run start.bat and wait for it to install correctly
       If it doesn't install or did not do it correctly, please make an issue and grab an log file
       (or copy and paste the log, i'm unsure that i haven't even checked for that) TO THE ORIGINAL
       REPOSITORY, not the fork!

<b>1. XAMPP</b>
  1.1. Start Apache service
  1.2. Start MySQL service

<b>2. Through phpmyadmin (apart of MySQL service)</b>
  2.1. Home > Database > Create database named 'db_gtanks' and set collation to utf8_general_ci
  2.2. db_gtanks (The database you have currently created) > Import:
  Browse... > this file you have downloaded > Scroll Down > Click 'Import' button

<b>3. IntelliJ IDEA</b>
  3.1. Open > (( [path/to/where/you/have/placed/it/from] > gtanks_server_remake > server ))
       The square brackets ( [, ] ) isn't for what you have to go to
       the exact directory, this is clearly an explanation!
  3.2. In (( gtanks_server_remake > server )) folder, go to src > main > java > amalgama
  and find the 'Main.java' file
  3.3. Run this file and hope for the best it'll work when in terminal displays:
       <code>[Netty] Server available on (5000) port.</code>


 <b>   RUNNING THE CLIENT</b>
  -----(
  Prerequirements:
  The client itself      | <a href="https://github.com/bjuonday/gtanks_server_remake/tree/master/client">https://github.com/bjuonday/gtanks_server_remake/tree/master/client</a>
  Fixed battles.swf file | had lost it
  -----)

  1. In (( gtanks_2024 > client )) folder, drag 'gt_client' folder into %appdata%.
  2. In (( %appdata% > gt_client )) folder, click GlobalSettings.exe > Advanced > Trusted
  Location Settings, then add domain named 'C:\Users\[Your profile username]\AppData
  \Roaming\gt_client' (without columns; incase '%appdata%\gt_client' doesn't work)
  3. When your server is still running, you can drag the 'GTloader.swf' file into
  'flashplayer_32.exe' executable.
  About the fixed battles.swf file, I cannot find it because I can't seemingly find myself as
  the main operating system and going to lose some files each install. So, yeah...
</pre>
