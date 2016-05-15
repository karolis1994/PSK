/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import java.util.List;

/**
 *
 * @author Henrikas
 */
public interface Grouping {
    public List<Principals> splitIntoGroups(List<Principals> principals, int numberOfGroups);
}
