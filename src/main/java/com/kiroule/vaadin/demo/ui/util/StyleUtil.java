/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kiroule.vaadin.demo.ui.util;

import com.kiroule.vaadin.demo.backend.data.MySubscriptionsBean;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.data.entity.Subscriptions;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Rajkiran
 */
public class StyleUtil 
{
    public static String getRowStyle(MySubscriptionsBean order) {
		//String style = order.getState().name().toLowerCase();
                String style = "";

		long days = LocalDate.now().until(order.getFromDate(), ChronoUnit.DAYS);
		if (days == 0) {
			style += " today";
		} else if (days == 1) {
			style += " tomorrow";
		}

		return style;
	}
    
    public static String getRowStyle(Subscriptions order) {
		//String style = order.getState().name().toLowerCase();
                String style = "";

		long days = LocalDate.now().until(order.getFromDate(), ChronoUnit.DAYS);
		if (days == 0) {
			style += " today";
		} else if (days == 1) {
			style += " tomorrow";
		}

		return style;
	}
    
}
