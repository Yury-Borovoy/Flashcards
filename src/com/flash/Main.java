package com.flash;

public class Main {

    public static void main(String[] args) {
	String importFile = null;
	String exportFile = null;
	//check the arguments for import and export files
        for (int i = 0; i < args.length; i++) {
            if (args[i].matches("-import")) {
                importFile = args[i + 1];
            } else if (args[i].matches("-export")) {
                exportFile = args[i + 1];
            }
        }

        GameProcess gameProcess = new GameProcess();
        gameProcess.inputAction(importFile, exportFile);
    }
}
