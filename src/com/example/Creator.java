package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by bsheen on 4/14/17.
 */
public class Creator {

    ObjectMapper mapper = new ObjectMapper();

    public void createMessages() {
        Scanner s = null;
        s = new Scanner(System.in);
        String description = null;
        String sender = null;
        Priority priority = null;
        System.out.print("input description: ");
        while (s.hasNext()) {
            String temp = s.nextLine();
            if (description == null) {
                description = temp;
                System.out.print("input sender: ");
            } else if (sender == null) {
                sender = temp;
                System.out.print("input priority: ");
            } else {
                switch (temp) {
                    case "Critical":
                        priority = Priority.CRITICAL;
                        break;
                    case "High":
                        priority = Priority.HIGH;
                        break;
                    case "Standard":
                        priority = Priority.STANDARD;
                        break;
                    case "Low":
                        priority = Priority.LOW;
                        break;
                    case "None":
                        priority = Priority.NONE;
                        break;
                    default:
                        System.out.println("Please input priority: Critical, High, Standard, Low, None");
                        System.out.print("input priority: ");
                }
                if (priority != null) {
                    Message msg = new Message(priority, description, sender);
                    try {
                        String json = mapper.writeValueAsString(msg);
                        try (PrintWriter out = new PrintWriter(msg.getId() + ".json")) {
                            out.println(json);
                            out.flush();
                        } catch (FileNotFoundException e) {
                            System.out.println("Error writing .json file " + e.getMessage());

                            System.out.println(json);
                        }
                    } catch (IOException e) {
                        System.out.println("Error writing msg as json " + e.getMessage());
                    }
                    System.out.println(msg);
                    description = null;
                    sender = null;
                    priority = null;
                    System.out.print("input description: ");
                }
            }
        }
    }

    public static void main(String[] args) {
        Creator creator = new Creator();
        creator.createMessages();
    }
}
