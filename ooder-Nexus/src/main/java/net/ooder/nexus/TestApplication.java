package net.ooder.nexus;

public class TestApplication {
    public static void main(String[] args) {
        try {
            System.out.println("Starting application...");
            NexusSpringApplication.main(args);
            System.out.println("Application started successfully!");
        } catch (Exception e) {
            System.err.println("Error starting application:");
            e.printStackTrace();
        }
    }
}