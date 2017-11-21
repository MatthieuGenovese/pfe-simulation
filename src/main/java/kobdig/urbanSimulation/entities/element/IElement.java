package kobdig.urbanSimulation.entities.element;

import org.postgis.PGgeometry;

/**
 * Created by Matthieu on 20/11/2017.
 */
public interface IElement {

    //Administrative Division
    public String getId();
    public void setId(String id);
    public double getLatitude();
    public double getLongitude();
    public AdministrativeDivision getDivision();
    public void setDivision(AdministrativeDivision division);
    public double getUtility();
    public void setUtility(double utility);
    public PGgeometry getGeom();
    public boolean isUpdated();
    public void setUpdated(boolean updated);
   /* public int getCode();
    public double getOnSaleProperties();
    public void setOnSaleProperties(double onSaleProperties);
    public double getRentedProperties();
    public void setRentedProperties(double rentedProperties);
    public ArrayList<Land> getLands();
    public void addLand(Land land);*/


    //LAND
    /*public String getId();
    public double getPrice();
    public double getLatitude();
    public double getLongitude();
    public AdministrativeDivision getDivision();
    public void setDivision(AdministrativeDivision division);
    public double getUtility();
    public void setUtility(Double utility);
    public PGgeometry getGeom();
    public boolean isUpdated();
    public void setUpdated(boolean updated);
    public void step(int time);

    //PROPERTY
    public String getId();
    public void setId(String id);
    public String getState();
    public void setState(String state);
    public AdministrativeDivision getDivision();
    public void setDivision(AdministrativeDivision division);
    public double getCurrentPrice();
    public double getCurrentCapitalizedRent();
    public double getCurrentPotentialRent();
    public double getCurrentValue();
    public double getLatitude();
    public double getLongitude();
    public double getUtility();
    public void setUtility(double utility);
    public Land getLand();
    public void setLand(Land land);
    public PGgeometry getGeom();
    public boolean isUpdated();
    public void setUpdated(boolean updated);
    public void step(int time);
    public String toString();

    //EQUIPMENT
    public AdministrativeDivision getDivision();
    public void setDivision(AdministrativeDivision division);
    public String getId();
    public String getType();
    public PGgeometry getGeom();

    //TRANSPORT NETWORK

    public String getId();
    public AdministrativeDivision getDivision();
    public void setDivision(AdministrativeDivision division);*/
}
