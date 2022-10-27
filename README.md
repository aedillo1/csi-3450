# csi-3450-project
 very quick tutorial

 1. install mariadb, with the username being "root" and the password being "alpine"
 2. install heidisql
    - it's an 'under-the-hood' program that lets you see what's happening within the database
    - after installing it, connect to the database with it
    - import the 'library.sql' file into heidisql by doing the following:
        1. File -> Run SQL -> [get the sql file from where you cloned the repo]
        2. right click left window and refresh. the inventory database should be in there.

 3. if you are using visual studio code (and im really hoping you are), go into the launch.json that is in .vscode
    - edit the "vmArgs" module path so that it links to the jdk that is within the repo.
    - honestly, it should be hooked already, but let me know if it isn't
 4. if all goes well, you should be able to run it by hitting the play button from the App.java file

 NOTE: any changes to the database will only affect on your computer. i do not plan on hosting the entire db for a small project like this.
