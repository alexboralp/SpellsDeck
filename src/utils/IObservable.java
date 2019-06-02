/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author aborbon
 */
public interface IObservable {
    public void addObserver(IObserver pObserver);
    public void notifyObservers(Object pData);
}
