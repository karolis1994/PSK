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
public interface IPasswordHasher {
    public byte[] createSalt();
    public String createHash(String password, byte[] salt) throws Exception;
    public boolean verifyPassword(String password, String correctHash)  throws Exception;
}
