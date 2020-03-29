package com.flash;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GameProcess extends FlashCard {
    private Map<String, String> cards = new HashMap<>();   //to store cards (term - definition)
    private Map<String, Integer> errors = new HashMap<>(); //to store cards (term - error)
    private ArrayList<String> log = new ArrayList<>();     //to store logs
    Scanner scan = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();

    //start menu
    public void inputAction(String importFile, String exportFile) {
        if (importFile != null) {
            importCard(importFile);
        }
        boolean ok = false;
        //проверяем, правильно ли пользователь ввел слово
        //если ввел не правильно, просим ввести заново
        while(!ok) {
            systemOutPrintln("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            switch (scanNextLine()) {
                case "add":
                    addCard();
                    System.out.println();
                    break;
                case "remove":
                    removeCard();
                    System.out.println();
                    break;
                case "import":
                    importCard();
                    System.out.println();
                    break;
                case "export":
                    exportCard();
                    System.out.println();
                    break;
                case "ask":
                    askCards();
                    System.out.println();
                    break;
                case "log":
                    logCard();
                    System.out.println();
                    break;
                case "hardest card":
                    printHardestCard();
                    System.out.println();
                    break;
                case "reset stats":
                    resetStats();
                    System.out.println();
                    break;
                case "exit":
                    if (exportFile != null) {
                        systemOutPrintln("Bye bye!");
                        exportCard(exportFile);
                    }else {
                        systemOutPrintln("Bye bye!");
                    }
                    System.out.println();
                    ok = true;
                    break;
                default:
                    systemOutPrintln("You inputted the wrong word. Please, input the action again.");
                    break;
            }
        }
    }
    //add a new card
    public void addCard() {
        systemOutPrintln("The card:");
        term = scanNextLine();
        if (cards.containsKey(term)) {
            systemOutPrintln("The card \"" + term + "\" already exists.");
        } else {
            systemOutPrintln("The definition of the card:");
            definition = scanNextLine();
            if (cards.containsValue(definition)) {
                systemOutPrintln("The definition \"" + definition + "\" already exists.");
            } else {
                cards.put(term, definition);
                systemOutPrintln("The pair (\"" + term + "\":\"" + definition + "\") has been added.");
            }
        }
    }
    //remove a card
    public void removeCard() {
        systemOutPrintln("The card:");
        term = scanNextLine();
        if (cards.containsKey(term)) {
            cards.remove(term);
            errors.remove(term);
            systemOutPrintln("The card has been removed.");
        } else {
            systemOutPrintln("Can't remove \"" + term + "\": there is no such card.");
        }
    }
    //import cards to the game
    public void importCard() {
        systemOutPrintln("File name:");
        String input = scanNextLine();
        int countOfCards = 0;
        File file = new File(input);
        try(Scanner scanFromFile = new Scanner(file)) {
            while(scanFromFile.hasNext()) {
                term = scanFromFile.nextLine();
                definition = scanFromFile.nextLine();
                error = Integer.parseInt(scanFromFile.nextLine());
                countOfCards++;
                cards.put(term, definition);
                errors.put(term, error);
            }
            systemOutPrintln(countOfCards + " cards have been loaded.");
        }catch(IOException e) {
            systemOutPrintln("File not found.");
        }
    }
    //overloading method, which imports cards at the beginning of the game
    public  void importCard(String importFile) {
        int countOfCards = 0;
        File file = new File(importFile);
        try(Scanner scanFromFile = new Scanner(file)) {
            while(scanFromFile.hasNext()) {
                term = scanFromFile.nextLine();
                definition = scanFromFile.nextLine();
                error =Integer.parseInt(scanFromFile.nextLine());
                countOfCards++;
                cards.put(term, definition);
                errors.put(term, error);
            }
            systemOutPrintln(countOfCards + " cards have been loaded.");
        }catch(IOException e) {
            systemOutPrintln("File not found.");
        }
    }
    //export cards to the file
    public void exportCard() {
        systemOutPrintln("File name:");
        String input = scanNextLine();
        int countOfcards = 0;
        try(FileWriter writer = new FileWriter(input)) {
            for (var elem: cards.entrySet()) {
                countOfcards++;
                writer.write(elem.getKey() + "\n");
                writer.write(elem.getValue() + "\n");
                writer.write(errors.get(elem.getKey()) + "\n");
            }
        }catch(IOException e){
            systemOutPrintln("It is impossible.");
        }
        systemOutPrintln(countOfcards + " cards have been saved.");
    }
    //overloading method, which export cards to the file at the end of the game
    public void exportCard(String exportFile) {
        int countOfcards = 0;
        try(FileWriter writer = new FileWriter(exportFile)) {
            for (var elem: cards.entrySet()) {
                countOfcards++;
                writer.write(elem.getKey() + "\n");
                writer.write(elem.getValue() + "\n");
                writer.write(errors.get(elem.getKey()) + "\n");
            }
        }catch(IOException e){
            systemOutPrintln("It is impossible.");
        }
        systemOutPrintln(countOfcards + " cards have been saved.");
    }
    //start game!!!
    public void askCards() {
        systemOutPrintln("How many times to ask?");
        String inputOfTimes = scanNextLine();
        String inputOfDefinition;
        ArrayList<String> listWithTerm = new ArrayList<>();
        for (var elem: cards.entrySet()) {
            listWithTerm.add(elem.getKey());
            if (!errors.containsKey(elem.getKey())) {
                errors.put(elem.getKey(), 0);
            }
        }
        Random random = new Random();
        String pointOfList;
        for (int i = 0; i < Integer.parseInt(inputOfTimes); i++) {
            pointOfList = listWithTerm.get(random.nextInt(listWithTerm.size()));
            systemOutPrintln("Print the definition of \"" + pointOfList + "\":");
            inputOfDefinition = scanNextLine();
            if (cards.get(pointOfList).equals(inputOfDefinition)) {
                systemOutPrintln("Correct answer");
            }else {
                errors.put(pointOfList, errors.get(pointOfList) + 1);//загоняем ошибки
                if (cards.containsValue(inputOfDefinition)) {
                    for (var el : cards.entrySet()) {
                        if (el.getValue().equals(inputOfDefinition)) {
                            systemOutPrintln("Wrong answer. The correct one is \"" + cards.get(pointOfList) + "\"," +
                                    " you've just written the definition of \"" + el.getKey() + "\".");
                            break;
                        }
                    }
                } else {
                    systemOutPrintln("Wrong answer. The correct one is \"" + cards.get(pointOfList) + "\".");
                }

            }
        }
    }
    //find the hardest card
    public void printHardestCard() {
        int maxNumber = 0;
        ArrayList<String> listWithMistakes = new ArrayList<>();
        for (var elem : errors.entrySet()) {
            if (elem.getValue() > maxNumber) {
                maxNumber = elem.getValue();
            }
        }
        for (var elem: errors.entrySet()) {
            if (elem.getValue().equals(maxNumber)) {
                listWithMistakes.add(elem.getKey());
            }
        }
        if (listWithMistakes.size() == 0) {
            systemOutPrintln("There are no cards with errors.");
        } else  if (listWithMistakes.size() == 1) {
            systemOutPrintln("The hardest card is \"" + listWithMistakes.get(0) +
                    "\". You have " + maxNumber + " errors answering it.");
        } else {
            for (int i = 0; i < listWithMistakes.size(); i++) {
                if (i < listWithMistakes.size() - 1) {
                    builder.append("\"").append(listWithMistakes.get(i)).append("\", ");
                } else {
                    builder.append("\"").append(listWithMistakes.get(i)).append("\" with ").append(maxNumber).append(" mistakes.");
                }
            }
            systemOutPrintln("The hardest cards " + builder);
        }
    }
    //remove the data about errors
    public void resetStats() {
        errors.clear();
        systemOutPrintln("Card statistics has been reset.");
    }
    //this method is logging input/output data
    public void logCard() {
        systemOutPrintln("File name:");
        String input = scanNextLine();
        try(FileWriter writer = new FileWriter(input)) {
            for (String log1: log) {
                writer.write(log1 + "\n");
            }
        }catch(IOException e){
            systemOutPrintln("It is impossible.");
        }
        systemOutPrintln("The log has been saved.");
    }
    //logging input data
    public String scanNextLine() {
        String inputC = scan.nextLine();
        log.add(inputC);
        return inputC;
    }
    //logging output data
    public void systemOutPrintln(String outputC) {
        System.out.println(outputC);
        log.add(outputC);
    }
}
