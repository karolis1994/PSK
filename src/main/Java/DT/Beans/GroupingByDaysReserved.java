/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Entities.Reservations;
import DT.Facades.ReservationFacade;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Henrikas
 */
@Alternative
public class GroupingByDaysReserved implements Grouping {

    @Inject
    private ReservationFacade reservationFacade;
    private HashMap<Principals, Integer> principalsMap = new HashMap<>();
    private Calendar from;
    private Calendar to;
    
    @Override
    public List<Principals> splitIntoGroups(List<Principals> principals, int numberOfGroups) {
        setDates();
        
        List<Reservations> reservations = 
                reservationFacade.findByDatesCoveredNotCanceledExtraIdNull(from.getTime(), to.getTime());
        
        for (Principals p : principals) {
            int numOfDaysReserved = countNumberDaysReservedInPreviousYear(p, reservations);
            principalsMap.put(p, numOfDaysReserved);
        }
        
        principalsMap = sortMapByValues(principalsMap);
        
        int groupSize = (int) Math.ceil((double)principals.size() / (double)numberOfGroups);
        int leftToAssignInCurrentGroup = groupSize;
        int groupNumber = 1;
        
        for (Map.Entry pair : principalsMap.entrySet()) {
            ((Principals) pair.getKey()).setGroupno(groupNumber);
            leftToAssignInCurrentGroup--;
            if (leftToAssignInCurrentGroup == 0) {
                groupNumber++;
            }
        }
        
        return principals;
    }
    
    private void setDates() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.YEAR, -1);
        
        from = currentDate;
        to = currentDate;

        from.set(Calendar.MONTH, Calendar.JANUARY);
        from.set(Calendar.DAY_OF_MONTH, 1);
        
        from.set(Calendar.MONTH, Calendar.DECEMBER);
        from.set(Calendar.DAY_OF_MONTH, from.getActualMaximum(Calendar.DAY_OF_MONTH));
        
        System.out.println(from.getTime());
        System.out.println(to.getTime());
    }
    
    private int countNumberDaysReservedInPreviousYear(Principals principal, List<Reservations> reservations) {
        long diffDays = 0;
        for (Reservations r : reservations) {
            if (r.getPrincipalid().equals(principal)) {                               
                long startTime = r.getReservedfrom().getTime();
                long endTime = r.getReservedto().getTime();
                
                if (r.getReservedfrom().before(from.getTime()))
                    startTime = from.getTimeInMillis();
                            
                if (r.getReservedto().after(to.getTime()))
                    endTime = to.getTimeInMillis();
                
                long diffTime = endTime - startTime;
                diffDays += (diffTime / (1000 * 60 * 60 * 24));
            }
        }
        
        return (int) diffDays;
    }
    
    private HashMap<Principals, Integer> sortMapByValues(HashMap<Principals, Integer> unsortMap) {
        // Convert Map to List
        List<Map.Entry<Principals, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<Principals, Integer>>() {
                public int compare(Map.Entry<Principals, Integer> o1,
                                   Map.Entry<Principals, Integer> o2) {
                        return (o1.getValue()).compareTo(o2.getValue());
                }
        });

        // Convert sorted map back to a Map
        HashMap<Principals, Integer> sortedMap = new HashMap<>();
        for (Iterator<Map.Entry<Principals, Integer>> it = list.iterator(); it.hasNext();) {
                Map.Entry<Principals, Integer> entry = it.next();
                sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    
}
