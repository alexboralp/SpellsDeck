/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.observerpattern;

/**
 *
 * @author alexander
 */
public interface IObservable {
    public boolean addObserver(IObserver pObserver);
    public boolean removeObserver(IObserver pObserver);
    public boolean update(IObserver pObserver, Object pMessage);
    public void updateAll(Object pMessage);
}
