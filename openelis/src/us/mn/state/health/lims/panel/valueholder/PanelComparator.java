/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

//AIS Created for bugzilla 1776
package us.mn.state.health.lims.panel.valueholder;

import java.util.Comparator;

public class PanelComparator implements Comparable {
   String name;

   
   // You can put the default sorting capability here
   public int compareTo(Object obj) {
      Panel t = (Panel)obj;
      return this.name.compareTo(t.getDescription());
   }
   
 

 
   public static final Comparator NAME_COMPARATOR =
     new Comparator() {
      public int compare(Object a, Object b) {
    	  Panel t_a = (Panel)a;
    	  Panel t_b = (Panel)b;
 
         return ((t_a.getDescription().toLowerCase()).compareTo(t_b.getDescription().toLowerCase()));

      }
   };
   

}
