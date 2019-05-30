/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;

/**
 *
 * @author aborbon
 */
public class Observable {
    private final ArrayList<IObserver> observers = new ArrayList<>();
    
    public void addObserver(IObserver pObserver) {
        observers.add(pObserver);
    }
    
    public void notifyObservers(Object pData) {
        for (IObserver observer : observers) {
            observer.notify(pData);
        }
    }
}
