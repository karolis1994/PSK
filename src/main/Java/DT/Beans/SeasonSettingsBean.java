/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Entities.Settings;
import DT.Facades.PrincipalsFacade;
import DT.Facades.SettingsFacade;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Karolis
 */
@Named("seasonSettingsBean")
@RequestScoped
public class SeasonSettingsBean {
    
    private final static int DEFAULT_GROUP_NUMBER = 6;
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String FORM_UPDATED = "Forma atnaujinta.";
    
    private Settings startDateSettings;
    private Settings endDateSettings;
    private Settings groupNumberSettings;
    
    @Inject
    private SettingsFacade settingsFacade;
    
    private Date startDate;
    private Date endDate;
    private int groupNumber;
    
    private Date thisYear;
    private Date nextYear;
    private SimpleDateFormat sdf;
    
    @Inject Grouping grouping;
    
    @Inject
    PrincipalsFacade principalsFacade;
    
    @PostConstruct
    public void init() {
        groupNumberSettings = settingsFacade.getSettingByName("GroupNumber");
        startDateSettings = settingsFacade.getSettingByName("SeasonStartDate");
        endDateSettings = settingsFacade.getSettingByName("SeasonEndDate");
        
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(today.get(1), 0, 1, 0, 0, 0);
        thisYear = today.getTime();
        today.add(Calendar.YEAR, 1);
        today.add(Calendar.DATE, -1);
        nextYear = today.getTime(); 
        
        //Get data from settings, if invalid data get default
        sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            startDate = sdf.parse(startDateSettings.getSettingvalue());
            endDate = sdf.parse(endDateSettings.getSettingvalue());
            groupNumber = Integer.parseInt(groupNumberSettings.getSettingvalue());
        } catch(NumberFormatException e) {
            groupNumber = DEFAULT_GROUP_NUMBER;
        } catch (ParseException e1) {
            startDate = new Date();
            endDate = new Date();
        }
        
    }
    
    //Method to update changes in database
    public void change() {
        startDateSettings.setSettingvalue(sdf.format(startDate));
        endDateSettings.setSettingvalue(sdf.format(endDate));
        groupNumberSettings.setSettingvalue(Integer.toString(groupNumber));
        
        settingsFacade.edit(startDateSettings);
        settingsFacade.edit(endDateSettings);
        settingsFacade.edit(groupNumberSettings);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, FORM_UPDATED, ""));
    }
 
    public void regroup() {
        
        List<Principals> principals = principalsFacade.findAllApproved();        
        List<Principals> groupedPrincipals = grouping.splitIntoGroups(principals, groupNumber);
        
        for(Principals p : groupedPrincipals) {
            principalsFacade.edit(p);
        }
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Date getThisYear() {
        return thisYear;
    }

    public Date getNextYear() {
        return nextYear;
    }
    
    
    
}
