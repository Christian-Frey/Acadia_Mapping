package cfreyvermont.acadia_mapping;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
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
import java.util.Set;

public class MapsActivity extends Fragment {
    GoogleMap map;
    LatLngBounds ACADIA = new LatLngBounds(new LatLng(45.081360, -64.37),
            new LatLng(45.094025, -64.364259));
    GoogleMapOptions mapOptions;
    List<Polygon> polyList = new ArrayList<>();
    Map<String, PolygonOptions> buildingOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_maps, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            ACADIA = savedInstanceState.getParcelable("LatLngBounds");
            mapOptions = savedInstanceState.getParcelable("mapOptions");
            CameraPosition position = savedInstanceState.getParcelable("LatLng");
            MapFragment mFragment = MapFragment.newInstance(mapOptions);
            map = mFragment.getMap();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
        } else {
            final MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            map = mapFragment.getMap();
            buildMap();
        }
    }

    /**
     * Creates a Map data-type containing the outlining points and name of buildings at Acadia.
     *
     * @return Map <String building name, PolygonOptions points outlining the building>.
     */
    private static Map<String, PolygonOptions> createPoints() {
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

        PolygonOptions elliotHallOptions = new PolygonOptions().add(
                new LatLng(45.090189, -64.369055),
                new LatLng(45.090229, -64.368820),
                new LatLng(45.089782, -64.368686),
                new LatLng(45.089718, -64.369040),
                new LatLng(45.089824, -64.369078),
                new LatLng(45.089847, -64.368965));

        PolygonOptions HSHOptions = new PolygonOptions().add(
                new LatLng(45.089491, -64.369287),
                new LatLng(45.089605, -64.368590),
                new LatLng(45.089249, -64.368477),
                new LatLng(45.089226, -64.368627),
                new LatLng(45.089128, -64.368541),
                new LatLng(45.089064, -64.368648),
                new LatLng(45.089041, -64.368804),
                new LatLng(45.089064, -64.368927),
                new LatLng(45.089170, -64.368868),
                new LatLng(45.089125, -64.369179));

        PolygonOptions uClubOptions = new PolygonOptions().add(
                new LatLng(45.089337, -64.369538),
                new LatLng(45.089371, -64.369356),
                new LatLng(45.089231, -64.369313),
                new LatLng(45.089197, -64.369501));

        PolygonOptions barraxOptions = new PolygonOptions().add(
                new LatLng(45.088780, -64.369442),
                new LatLng(45.088814, -64.369286),
                new LatLng(45.088712, -64.369248),
                new LatLng(45.088748, -64.369042),
                new LatLng(45.088831, -64.369063),
                new LatLng(45.088865, -64.368902),
                new LatLng(45.088626, -64.368816),
                new LatLng(45.088535, -64.369358));

        PolygonOptions hortonHallOptions = new PolygonOptions().add(
                new LatLng(45.089012, -64.368634),
                new LatLng(45.089046, -64.368446),
                new LatLng(45.088690, -64.368333),
                new LatLng(45.088656, -64.368515));

        PolygonOptions biologyOptions = new PolygonOptions().add(
                new LatLng(45.088391, -64.369175),
                new LatLng(45.088448, -64.368901),
                new LatLng(45.088103, -64.368783),
                new LatLng(45.088054, -64.369051));

        PolygonOptions pattersonOptions = new PolygonOptions().add(
                new LatLng(45.088527, -64.368606),
                new LatLng(45.088588, -64.368252),
                new LatLng(45.088187, -64.368118),
                new LatLng(45.088149, -64.368327),
                new LatLng(45.088403, -64.368424),
                new LatLng(45.088384, -64.368553));

        PolygonOptions kcicOptions = new PolygonOptions().add(
                new LatLng(45.087480, -64.369085),
                new LatLng(45.087696, -64.367924),
                new LatLng(45.087298, -64.367801),
                new LatLng(45.087268, -64.367983),
                new LatLng(45.087514, -64.368058),
                new LatLng(45.087397, -64.368793),
                new LatLng(45.087348, -64.368782),
                new LatLng(45.087303, -64.369023));

        PolygonOptions uhallOptions = new PolygonOptions().add(
                new LatLng(45.089235, -64.367396),
                new LatLng(45.089337, -64.366691),
                new LatLng(45.089095, -64.366621),
                new LatLng(45.089065, -64.366857),
                new LatLng(45.089012, -64.366846),
                new LatLng(45.088974, -64.367039),
                new LatLng(45.089027, -64.367066),
                new LatLng(45.088989, -64.367318));

        PolygonOptions rhodesHallOptions = new PolygonOptions().add(
                new LatLng(45.088614, -64.367817),
                new LatLng(45.088652, -64.367613),
                new LatLng(45.088447, -64.367538),
                new LatLng(45.088439, -64.367592),
                new LatLng(45.088386, -64.367581),
                new LatLng(45.088375, -64.367672),
                new LatLng(45.088417, -64.367699),
                new LatLng(45.088413, -64.367747));

        PolygonOptions carnegieOptions = new PolygonOptions().add(
                new LatLng(45.088264, -64.367545),
                new LatLng(45.088321, -64.367153),
                new LatLng(45.088101, -64.367083),
                new LatLng(45.088090, -64.367147),
                new LatLng(45.087935, -64.367109),
                new LatLng(45.087886, -64.367366),
                new LatLng(45.088060, -64.367420),
                new LatLng(45.088056, -64.367474));

        PolygonOptions emmersonOptions = new PolygonOptions().add(
                new LatLng(45.088041, -64.366948),
                new LatLng(45.088098, -64.366621),
                new LatLng(45.087981, -64.366583),
                new LatLng(45.087973, -64.366615),
                new LatLng(45.087935, -64.366615),
                new LatLng(45.087897, -64.366851),
                new LatLng(45.087954, -64.366867),
                new LatLng(45.087943, -64.366905));

        PolygonOptions willetOptions = new PolygonOptions().add(
                new LatLng(45.087682, -64.367232),
                new LatLng(45.087712, -64.367066),
                new LatLng(45.087458, -64.366975),
                new LatLng(45.087431, -64.367136));

        PolygonOptions fountainOptions = new PolygonOptions().add(
                new LatLng(45.087859, -64.366423),
                new LatLng(45.087961, -64.365849),
                new LatLng(45.087802, -64.365795),
                new LatLng(45.087783, -64.365897),
                new LatLng(45.087703, -64.365870),
                new LatLng(45.087658, -64.365908),
                new LatLng(45.087613, -64.366187),
                new LatLng(45.087640, -64.366241),
                new LatLng(45.087723, -64.366273),
                new LatLng(45.087715, -64.366359));

        buildingOptions.put("BAC", BACOptions);
        buildingOptions.put("Tower", towerOptions);
        buildingOptions.put("Elliot", elliotHallOptions);
        buildingOptions.put("HSH", HSHOptions);
        buildingOptions.put("UniClub", uClubOptions);
        buildingOptions.put("Barrax", barraxOptions);
        buildingOptions.put("Horton", hortonHallOptions);
        buildingOptions.put("Biology", biologyOptions);
        buildingOptions.put("Patterson", pattersonOptions);
        buildingOptions.put("KCIC", kcicOptions);
        buildingOptions.put("UHall", uhallOptions);
        buildingOptions.put("Rhodes", rhodesHallOptions);
        buildingOptions.put("Carnegie", carnegieOptions);
        buildingOptions.put("Emmerson", emmersonOptions);
        buildingOptions.put("Willet", willetOptions);
        buildingOptions.put("FountainCommons", fountainOptions);

        return buildingOptions;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void buildMap() {
        //We want people to only be able to look at Acadia University, so
        //lets set boundaries on the maps position
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setBuildingsEnabled(false);

        // TODO: Fix zooming in and out on startup
        /* Creating the points for our building outlines.
        We chose to use a map because it allows us to tie the set of points to a human
        readable name, will make it easier to identify each set of points. */
        buildingOptions = createPoints();

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
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


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
                        Set<String> keySet = buildingOptions.keySet();
                        Object keyArray[] = keySet.toArray();
                        addFragment(keyArray[pos].toString());
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

    public void addFragment(String bldName) {
        Fragment buildingInfo = new BuildingInfo();

        Bundle bundle = new Bundle();
        bundle.putString("buildingName", bldName);
        buildingInfo.setArguments(bundle);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.building_info_placeholder, buildingInfo)
                .addToBackStack(null).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("LatLngBounds", ACADIA);
        outState.putParcelable("mapOptions", mapOptions);
        outState.putParcelable("LatLng", map.getCameraPosition());
    }
}
