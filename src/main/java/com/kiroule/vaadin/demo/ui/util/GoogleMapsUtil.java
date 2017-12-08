/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kiroule.vaadin.demo.ui.util;

import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Rajkiran
 */
@SpringComponent
public class GoogleMapsUtil 
{
    
    private String directionsUrl;    
    private String placeUrl;   
    
    private static final String SPACE=" ";
    private static final String PLUS_SIGN="+";
    private static final String SRC_PLACEHOLDER="SRC_PLACEHOLDER";
    private static final String P1_PLACEHOLDER="P1_PLACEHOLDER";
    private static final String P2_PLACEHOLDER="P2_PLACEHOLDER";
    
    public String getDirectionsMap(String p1, String p2)
    {
        directionsUrl="https://www.google.com/maps/embed/v1/directions?key=AIzaSyBBFeOd6XZIXthP3jgWtEDBtjlSTcfA0ZM&origin=P1_PLACEHOLDER&destination=P2_PLACEHOLDER&avoid=tolls|highways";
        p1=p1.replaceAll(SPACE, PLUS_SIGN);
        p2=p2.replaceAll(SPACE, PLUS_SIGN);
        directionsUrl=directionsUrl.replaceAll(P1_PLACEHOLDER, p1);
        directionsUrl=directionsUrl.replaceAll(P2_PLACEHOLDER, p2);
        String directionsIframe= getPlainIframe().replaceAll(SRC_PLACEHOLDER, directionsUrl);        
        return directionsIframe;
    }
    
    public String getPlaceMap(String p1)
    {
        placeUrl="https://www.google.com/maps/embed/v1/place?key=AIzaSyBBFeOd6XZIXthP3jgWtEDBtjlSTcfA0ZM&q=P1_PLACEHOLDER";
        p1=p1.replaceAll(SPACE, PLUS_SIGN);
        
        placeUrl=placeUrl.replaceAll(P1_PLACEHOLDER, p1);
        
        String placeIframe= getPlainIframe().replaceAll(SRC_PLACEHOLDER, placeUrl);        
        return placeIframe;
    }
    
    private String getPlainIframe()
    {
        String iframe="<iframe width=\"650\" height=\"400\" frameborder=\"0\" style=\"border:0\" src=\"SRC_PLACEHOLDER\" allowfullscreen> </iframe>";
        
        return iframe;
        
    }
    
}
