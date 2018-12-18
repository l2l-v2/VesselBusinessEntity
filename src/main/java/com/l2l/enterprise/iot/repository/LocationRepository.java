package com.l2l.enterprise.iot.repository;

import com.l2l.enterprise.iot.domain.Location;
import com.l2l.enterprise.iot.util.CsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<Location> locations = new ArrayList<Location>();

    public LocationRepository(@Value("${vessel.locations}") String filePath) throws IOException {
        logger.debug("--"+filePath+"--");
        String path = this.getClass().getResource("/").getPath()+ "data/" +filePath;
        locations = CsvUtil.readLocations(path);
        logger.debug("locations : ");
    }

    public Location findLocation(String name){
        for(Location location : locations){
            if(name.equals(location.getName())){
                return location;
            }
        }
        return null;
    }
}
