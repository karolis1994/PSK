/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import DT.Services.IPasswordHasher;
import DT.Services.PasswordHasherPBKDF2;
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
import java.text.ParseException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author donatas
 */
@Named("loginBean")
@RequestScoped
public class LoginBean {

    private static final String APP_ID = "1581543875491903";
    private static final String CLIENT_SECRET = "668d1f29c2257393382374610de8310c";

    @Pattern(regexp = "[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]",
            message = "Neteisingas formatas, teisingo pavyzdys: Jonas@gmail.lt")
    private String email;

    @EJB
    private PrincipalsFacade principalsFacade;

    private String password;
    private String code;

    @Inject
    private UserSessionBean userSessionBean;

    @EJB
    private final IPasswordHasher passwordHasher = new PasswordHasherPBKDF2();

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

    @PostConstruct
    void init() {
        code = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
    }

    public void login() throws IOException, Exception {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Principals currentUser = principalsFacade.findByEmail(getEmail());

        if (currentUser == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Neteisingas slaptažodis arba elektroninis paštas.", ""));
            return;
        }
        if (currentUser.getPasswordhash() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reikalingas prisijungimas per facebook.", ""));
            return;
        }

        if (passwordHasher.verifyPassword(getPassword(), currentUser.getPasswordhash())) {
            userSessionBean.setUser(currentUser);
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("logged-in/index.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Neteisingas slaptažodis arba elektroninis paštas.", ""));
        }
    }

    public String getFBReturnUrl() {
        String FBReturnUrl;
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        FBReturnUrl = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        FBReturnUrl += "fb-login.xhtml";
        return FBReturnUrl;
    }

    public String returnFromFb() throws FacebookException, UnsupportedEncodingException, ParseException {
        
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(APP_ID, CLIENT_SECRET);
        facebook.setOAuthPermissions("email,public_profile,user_about_me,user_birthday");
        facebook.setOAuthCallbackURL(URLEncoder.encode(getFBReturnUrl(), "UTF-8"));
        AccessToken token = facebook.getOAuthAccessToken(code);
        facebook.setOAuthAccessToken(token);
        User user = facebook.getUser(facebook.getId(), new Reading().fields("email,bio,birthday,first_name,last_name"));
        if(userSessionBean.getUser() != null)
        {
            Principals logedInUser = userSessionBean.getUser();
            logedInUser.setFacebookid(user.getId());
            principalsFacade.edit(logedInUser);
            return"";
        }
        // Setting current user to UserSessionBean
        String facebookUserID = user.getId();
        Principals currentUser = principalsFacade.findByFacebookID(facebookUserID);
        if (currentUser == null) {
            //registrationBean.register(user, facebook);
            userSessionBean.setUserFB(user);
            return "user-registration.xhtml";
        }

        userSessionBean.setUser(currentUser);

        return "logged-in/index.xhtml";
    }

    public void RedirectToFacebook() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("https://www.facebook.com/dialog/oauth?client_id=1581543875491903&scope=email&redirect_uri=" + getFBReturnUrl());
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
//        return "logged-in/index.xhtml";
//    }
}
