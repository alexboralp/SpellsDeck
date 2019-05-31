/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encrypter.methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import utils.Logger;

/**
 *
 * @author aborbon
 */
public class EncrypterFactory {

    public static IEncrypter getIntance(String name) {
        try {
            Class<?> myClass = Class.forName(name);
            Constructor<?> constructor = myClass.getConstructors()[0];
            Object object = constructor.newInstance(new Object[] { });
            return (IEncrypter)object;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.Log(ex.getMessage());
        }
        return null;
    }
    
}
