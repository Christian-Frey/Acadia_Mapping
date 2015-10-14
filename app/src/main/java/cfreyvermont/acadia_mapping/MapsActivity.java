package cfreyvermont.acadia_mapping;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //The boundary of our Map
    LatLngBounds ACADIA = new LatLngBounds(new LatLng(45.081360, -64.37),
            new LatLng(45.094025, -64.364259));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Creates a Map containing the outlining points and name of buildings at Acadia.
     *
     * @return Map String building name, PolygonOptions points outlining the building.
     */
    private Map<String, PolygonOptions> createPoints() {
        //We are using a LinkedHashMap to maintain order.
        Map<String, PolygonOptions> buildingOptions = new LinkedHashMap<>();

        PolygonOptions BACOptions = new PolygonOptions().add(
                new LatLng(45.089740, -64.365454),
                new LatLng(45.089820, -64.364944),
                new LatLng(45.089865, -64.364955),
                new LatLng(45.089884, -64.364832),
                new LatLng(45.089710, -64.364784),
                new LatLng(45.089748, -64.364553),
                new LatLng(45.089862, -64.364574),
                new LatLng(45.089900, -64.364333),
                new LatLng(45.090574, -64.364526),
                new LatLng(45.090676, -64.364757),
                new LatLng(45.090551, -64.365170),
                new LatLng(45.090377, -64.365175),
                new LatLng(45.090415, -64.364918),
                new LatLng(45.090248, -64.364891),
                new LatLng(45.090062, -64.365556));

        PolygonOptions towerOptions = new PolygonOptions().add(
                new LatLng(45.085245, -64.364424),
                new LatLng(45.085485, -64.364519),
                new LatLng(45.085540, -64.3642921),
                new LatLng(45.085295, -64.364187));

        buildingOptions.put("BAC", BACOptions);
        buildingOptions.put("Tower", towerOptions);
        return buildingOptions;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap map) {
        //We want people to only be able to look at Acadia University, so
        //lets set boundaries on the maps position
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);

        /* Creating the points for our building outlines.
        We chose to use a map because it allows us to tie the set of points to a human
        readable name, will make it easier to identify each set of points. */
        final Map<String, PolygonOptions> buildingOptions = createPoints();
        final List<Polygon> polyList = new ArrayList<>();

       /*
        * Iterates through the map to add the building outlines. Adds the polygons to
        * a list in order to keep track of them.
        */
        for (Map.Entry<String, PolygonOptions> entry : buildingOptions.entrySet()) {
            Polygon i = map.addPolygon(entry.getValue());
            polyList.add(i);
        }


        //Creating a new camera position with the correct bearing, since south is the
        //top of campus, we want the map "upside down"
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                //Centering on University hall to start
                .target(new LatLng(45.088845, 64.366850))
                .bearing(165)
                .zoom(17)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        /*
         * Below are callback functions for Map clicks and camera changes
         */

        //Checking if the user has tapped on a building or not
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                int pos;
                for (pos = 0; pos < polyList.size(); pos++) {
                    //Checking if they clicked on a building
                    if (PolyUtil.containsLocation(point, polyList.get(pos).getPoints(), false)) {
                        //Create Building Info Window
                    }
                }
            }
        });

        //Letting the map know what to do when it updates
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            /* We want to be able to restrict the user from leaving Acadia, so lets
             * (gently) let them know that they can not leave the area by snapping back
             * to the closest edge if they stray too far.
             * NOTE: This method is inefficient because it is called every time the camera
             * is updated, even if it is moved by the program. Not ideal, but should be
             * acceptable for our uses.
            */
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLng center = position.target;
                LatLng closeEdge = ACADIA.getCenter();

                if (!ACADIA.contains(center)) {
                    //north, going north edge
                    if (center.latitude > ACADIA.northeast.latitude) {
                        closeEdge = new LatLng(ACADIA.northeast.latitude, center.longitude);
                    }
                    //south, going to south edge
                    else if (center.latitude < ACADIA.southwest.latitude) {
                        closeEdge = new LatLng(ACADIA.southwest.latitude, center.longitude);
                    }
                    //east, going to east edge
                    else if (center.longitude > ACADIA.northeast.longitude) {
                        closeEdge = new LatLng(center.latitude, ACADIA.northeast.longitude);
                    }
                    //west, going to west edge
                    else if (center.longitude < ACADIA.southwest.longitude) {
                        closeEdge = new LatLng(center.latitude, ACADIA.southwest.longitude);
                    }

                    //updating the camera position
                    CameraPosition goingTo = new CameraPosition.Builder()
                            .target(closeEdge)
                            .zoom(position.zoom)
                            .bearing(position.bearing)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(goingTo));
                }
            }
        });
    }
}
