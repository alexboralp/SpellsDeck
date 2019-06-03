/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista.player.drag;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author alexander
 */
public class DragListener extends MouseInputAdapter {
    
    public enum TIPO_CARTA {
        CARD_NORMAL, CARD_SELECTED
    }
    
    public enum TIPO {
        PRESSED, RELEASED
    }
    
    private JLabel lblSource;
    private TIPO tipo;
    private TIPO_CARTA tipoCartaSource;
    private TIPO_CARTA tipoCartaTarget;

    public DragListener() {
        super();
        lblSource = null;
        tipo = null;
        tipoCartaSource = null;
        tipoCartaTarget = null;
    }
    
    @Override
    public void mousePressed(MouseEvent arg0) {
        
        System.out.println("Mouse pressed");
        
        tipo = TIPO.PRESSED;
        
        JLabel childComp = getComponentAt(arg0);
        if (childComp != null) {
            lblSource = childComp;
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent arg0) {
        System.out.println("Mouse dragged");
        if (lblSource != null) {
            ((JFrame)arg0.getSource()).setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent arg0) {
        System.out.println("Mouse released");
        
        tipo = TIPO.RELEASED;
        
        JLabel lblTarget = getComponentAt(arg0);
        if (lblTarget != null && lblSource != null && lblTarget != lblSource) {
            if ( tipoCartaSource == TIPO_CARTA.CARD_NORMAL && tipoCartaTarget == TIPO_CARTA.CARD_SELECTED) {
                System.out.println("Mouse released 1");
                lblTarget.setText(lblSource.getText());
                lblTarget.setIcon(lblSource.getIcon());
            } else if ( tipoCartaSource == TIPO_CARTA.CARD_SELECTED && tipoCartaTarget == TIPO_CARTA.CARD_SELECTED) {
                System.out.println("Mouse released 2");
                String strtemp = lblSource.getText();
                lblSource.setText(lblTarget.getText());
                lblTarget.setText(strtemp);
                Icon icontemp = lblSource.getIcon();
                lblSource.setIcon(lblTarget.getIcon());
                lblTarget.setIcon(icontemp);
            }
        }
        
        tipoCartaSource = null;
        tipoCartaTarget = null;
        lblSource = null;
        ((JFrame)arg0.getSource()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    private void ponerTipoCarta (TIPO_CARTA tipoCarta) {
        if (tipo == TIPO.PRESSED) {
            tipoCartaSource = tipoCarta;
        } else {
            tipoCartaTarget = tipoCarta;
        }
    }

    private JLabel getComponentAt(MouseEvent e) {
        Point p = e.getPoint();
        JFrame comp = (JFrame) e.getSource();
        Component childComp = getComponentAt(comp, p);
        if (childComp != null && childComp instanceof JRootPane) {
            childComp = getComponentAt((JRootPane)childComp, p);
            if (childComp != null && childComp instanceof JLayeredPane) {
                childComp = getComponentAt((JLayeredPane)childComp, p);
                if (childComp != null && childComp instanceof JPanel) {
                    childComp = getComponentAt((JPanel)childComp, p);
                    if (childComp != null) {
                        if (childComp instanceof JLabel) {
                            ponerTipoCarta(TIPO_CARTA.CARD_NORMAL);
                            return (JLabel)childComp;
                        } else if (childComp instanceof JPanel) {
                            childComp = getComponentAt((JPanel)childComp, p);
                            if (childComp != null && childComp instanceof JLabel) {
                                if (p.x < 3 * comp.getWidth() / 7) {
                                    ponerTipoCarta(TIPO_CARTA.CARD_SELECTED);
                                    return (JLabel)childComp;
                                } else {
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public Component getComponentAt(Container parent, Point p) {
        Component comp = null;
        for (Component child : parent.getComponents()) {
            if (child.getBounds().contains(p)) {
                comp = child;
            }
        }
        return comp;
    }
    
}
