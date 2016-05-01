/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import facebook4j.Account;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.auth.AccessToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import sun.net.www.http.HttpClient;

/**
 *
 * @author donatas
 */
@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {

    private static final String APP_ID = "1581543875491903";
    private static final String CLIENT_SECRET = "668d1f29c2257393382374610de8310c";

    @Pattern(regexp = "[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")
    private String email;
    @Size(min = 5)
    private String password;

    @ManagedProperty(value = "#{param.code}")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String value) {
        code = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    public void login() throws IOException {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute("authUserEmail", "email");
        
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .redirect("index.xhtml");
    }

    public String getFBReturnUrl() {
        String FBReturnUrl;
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        FBReturnUrl = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        FBReturnUrl += "fb-login.xhtml";
        return FBReturnUrl;
    }
    public String returnFromFb() throws FacebookException, UnsupportedEncodingException
    {
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(APP_ID, CLIENT_SECRET);
        facebook.setOAuthPermissions("email");
        facebook.setOAuthCallbackURL(URLEncoder.encode(getFBReturnUrl(), "UTF-8"));
        AccessToken token = facebook.getOAuthAccessToken(code);
        facebook.setOAuthAccessToken(token);
        User user = facebook.getUser(facebook.getId(), new Reading().fields("email"));
        String fbemail = user.getEmail();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute("authUserEmail", fbemail);
        return "index.xhtml";
    }
    
// IN case we drop fb4j api
//    public String returnFromFb() throws MalformedURLException, IOException {
//
//        String fburl = "https://graph.facebook.com/oauth/access_token?"
//					+ "client_id=" + APP_ID 
//                                        + "&amp;redirect_uri=" + URLEncoder.encode(getFBReturnUrl()+";", "UTF-8")
//					+ "&amp;client_secret=" + CLIENT_SECRET 
//                                        + "&amp;code=" + code;
//        //       HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
////        con.setRequestMethod("GET");
////        con.setRequestProperty("client_id",APP_ID);
////        con.setRequestProperty("redirect_uri",URLEncoder.encode(getFBReturnUrl(), "UTF-8"));
////        con.setRequestProperty("client_secret",CLIENT_SECRET);
////        con.setRequestProperty("code",code);
//        URL fbGraphURL;
//        try {
//            fbGraphURL = new URL(fburl);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Invalid code received " + e);
//        }
//        URLConnection fbConnection;
//        StringBuffer b = null;
//        try {
//            fbConnection = fbGraphURL.openConnection();
//            BufferedReader in;
//            in = new BufferedReader(new InputStreamReader(
//                    fbConnection.getInputStream()));
//            String inputLine;
//            b = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                b.append(inputLine + "\n");
//            }
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Unable to connect with Facebook "
//                    + e);
//        }
//        String accessToken = b.toString();
//        if (accessToken.startsWith("{")) {
//            throw new RuntimeException("ERROR: Access Token Invalid: "
//                    + accessToken);
//        }
//        // int responseCode = con.getResponseCode();
//
//        return "index.xhtml";
//    }

}