/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Services;

/**
 *
 * @author Karolis
 */
public interface IMail {
    public int sendMail(String recipient, String subject, String message);
}
