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


        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(ACADIA, 0));

        //Creating a new camera position with the correct bearing, since south is the
        //top of campus, we want the map "upside down"
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                //Centering on University hall to start
                .target(new LatLng(45.088845, 64.366850))
                .bearing(165)
                .zoom(17)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        map.setIndoorEnabled(false);
        map.setBuildingsEnabled(true);
        //Letting the map know what to do when it updates
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            //We want to be able to restrict the user from leaving Acadia, so lets
            //(gently) let them know that they can not leave the area by snapping back
            //to the closest edge if they stray too far.
            //NOTE: This method is inefficient because it is called every time the camera
            //is updated, even if it is moved by the program. Not ideal, but should be
            //acceptable for our uses.
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLng center = position.target;
                LatLng closeEdge = ACADIA.getCenter();

                if (!ACADIA.contains(center)) {
                    //I'm north, goto north edge
                    if (center.latitude > ACADIA.northeast.latitude)
                        closeEdge = new LatLng(ACADIA.northeast.latitude, center.longitude);
                        //south, going to south edge
                    else if (center.latitude < ACADIA.southwest.latitude)
                        closeEdge = new LatLng(ACADIA.southwest.latitude, center.longitude);
                        //Too far east, going to east edge
                    else if (center.longitude > ACADIA.northeast.longitude)
                        closeEdge = new LatLng(center.latitude, ACADIA.northeast.longitude);
                        //Too far west, going to west edge
                    else if (center.longitude < ACADIA.southwest.longitude)
                        closeEdge = new LatLng(center.latitude, ACADIA.southwest.longitude);

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
