package softcare.game.model;

import java.util.ArrayList;
import java.util.List;

import softcare.gui.PointXY;

public  class Location    {
    public Location( ) {
    }

    List<String> names= new ArrayList<>();
    List<PointXY> locations= new ArrayList<>();

    public List<String> getNames() {
        return names;
    }


    public List<PointXY> getLocations() {
        return locations;
    }
    public void   addPoint(String name, double x, double y ){
        locations.add(new PointXY(x,y));
        names.add(name);
    }

    public void undo() {
        if(locations.size()>0){
            names.remove(names.size()-1);
            locations.remove(locations.size()-1);
        }
    }


    public boolean editPoint(String name, double x, double y , int position){
        if(locations.size()>position) {
            locations.set(position,new PointXY(x, y));
            names.set(position,name);
            return  true;
        }
        return  false;
    }
}