/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Salts;

/**
 *
 * @author Karolis
 */
public class SaltsFacade extends GenericFacade<Salts>{
    
    public SaltsFacade() {
        super(Salts.class);
    }
    
    /*
   * Returns a random salt to be used to hash a password.
   *
   * @return a 16 bytes random salt

    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }
    
    * Returns a salted and hashed password using the provided hash.<br>
    * Note - side effect: the password is destroyed (the char[] is filled with zeros)
    *
    * @param password the password to be hashed
    * @param salt     a 16 bytes salt, ideally obtained with the getNextSalt method
    *
    * @return the hashed password with a pinch of salt
    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }*/
}
