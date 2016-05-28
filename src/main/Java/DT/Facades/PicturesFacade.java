/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Pictures;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Karolis
 */
@Stateless
public class PicturesFacade extends GenericFacade<Pictures>{
    
    public PicturesFacade() {
        super(Pictures.class);
    }
    
    public static byte[] getBytesFromInputStream(InputStream is) throws IOException
    {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();)
        {
            byte[] buffer = new byte[0xFFFF];

            for (int len; (len = is.read(buffer)) != -1;)
                os.write(buffer, 0, len);

            os.flush();

            return os.toByteArray();
        }
    }
    
        //Method to upload a picture
    public boolean uploadPicture(Pictures picture, UploadedFile uploadedPicture) {
        try {
            //Converting picture to byte array
            byte[] pictureBytes = new byte[(int)uploadedPicture.getSize()];
            pictureBytes = getBytesFromInputStream(uploadedPicture.getInputstream());
            picture.setImagename(uploadedPicture.getFileName());
            picture.setImage(pictureBytes);
            create(picture);
        } catch(Exception e) {
            return false;
        }
        return true;
    }
    
}
