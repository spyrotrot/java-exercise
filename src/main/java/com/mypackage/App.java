package com.mypackage;

import java.io.Console;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;

public class App {
    public static void main( String[] args ) {
        try {
            new Menu().show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
