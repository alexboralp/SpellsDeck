/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.observerpattern;

import java.util.LinkedList;

/**
 *
 * @author alexander
 */
public abstract class Observable implements IObservable{
    LinkedList<IObserver> observers;
    
    public Observable() {
        observers = new LinkedList<>();
    }
    
    @Override
    public boolean addObserver(IObserver pObserver) {
        return observers.add(pObserver);
    }
    
    @Override
    public boolean removeObserver(IObserver pObserver) {
        return observers.remove(pObserver);
    }

    @Override
    public boolean update(IObserver pObserver, Object pMessage) {
        int pos = observers.indexOf(pObserver);
        
        if (pos != -1) {
            observers.get(pos).update(pMessage);
            return true;
        }
        
        return false;
    }

    @Override
    public void updateAll(Object pMessage) {
        for (IObserver observer : observers) {
            observer.update(pMessage);
        }
    }
    
}
