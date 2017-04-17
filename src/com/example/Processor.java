package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

/**
 * Created by bsheen on 4/14/17.
 */
public class Processor {
    private static ObjectMapper mapper = new ObjectMapper();

    private static Set<Message> msgSet = new HashSet<>();
    private static Map<Status, Set<Message>> msgMap = new HashMap<>();

    private static String message = null;
    private static Message msgTester;

    public void processMessages() {
        msgMap.put(Status.INITIAL, msgSet);

        msgSet = new HashSet<>();
        msgMap.put(Status.ASSIGNED, msgSet);

        msgSet = new HashSet<>();
        msgMap.put(Status.IN_PROGRESS, msgSet);

        msgSet = new HashSet<>();
        msgMap.put(Status.DONE, msgSet);

        while (true) {
            try {
                Thread.sleep(2000l);
            } catch (InterruptedException e) {
                System.out.println("error, could not sleep " + e.getMessage());
            }
            moveIt();
            try {
                Thread.sleep(2000l);
            } catch (InterruptedException e) {
                System.out.println("error, could not sleep " + e.getMessage());
            }
            readIt();
            try {
                Thread.sleep(2000l);
            } catch (InterruptedException e) {
                System.out.println("error, could not sleep " + e.getMessage());
            }
        }

    }

    private void moveIt() {
        System.out.println("before move: " + msgMap);
        if (!msgMap.get(Status.IN_PROGRESS).isEmpty()) {
            msgSet = msgMap.get(Status.IN_PROGRESS);
            for (Message m : msgSet) {
                msgMap.get(Status.DONE).add(m);
            }
            msgMap.get(Status.IN_PROGRESS).clear();
        }

        if (!msgMap.get(Status.ASSIGNED).isEmpty()) {
            msgSet = msgMap.get(Status.ASSIGNED);
            for (Message m : msgSet) {
                msgMap.get(Status.IN_PROGRESS).add(m);
            }
            msgMap.get(Status.ASSIGNED).clear();
        }

        if (!msgMap.get(Status.INITIAL).isEmpty()) {
            msgSet = msgMap.get(Status.INITIAL);
            for (Message m : msgSet) {
                msgMap.get(Status.ASSIGNED).add(m);
            }
            msgMap.get(Status.INITIAL).clear();
        }
        System.out.println("after move: " + msgMap);
    }

    private void readIt() {
        File file = new File(".");
        for (File f : file.listFiles()) {
            if (f.getName().endsWith(".json")) {
                try (BufferedReader in = new BufferedReader(new FileReader(f.getName()))) {
                    message = in.readLine();
                    f.delete();
                    msgTester = mapper.readValue(message, Message.class);
                    System.out.println(msgTester);
                    if (msgTester.getPriority() != Priority.NONE) {
                        msgMap.get(Status.INITIAL).add(msgTester);
                    } else {
                        msgMap.get(Status.DONE).add(msgTester);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error finding file " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("Error reading file " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        Processor processor = new Processor();
        processor.processMessages();
    }
}
