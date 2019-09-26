package com.tomclaw.bzz;

public class Main {

    public static void main(String[] args) {
        try {
            String command = args[0];
            String source = args[1];
            String destination = args[2];
            switch (command) {
                case "-c":
                    new FileCompress().start(source, destination);
                    break;
                case "-x":
                    new FileExtractor().start(source, destination);
                    break;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
