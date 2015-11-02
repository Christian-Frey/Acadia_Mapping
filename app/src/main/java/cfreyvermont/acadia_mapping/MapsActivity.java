package cfreyvermont.acadia_mapping;

import android.app.Fragment;
import android.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapsActivity extends Fragment {
    private GoogleMap map;
    private final LatLngBounds ACADIA = new LatLngBounds(new LatLng(45.081360, -64.37),
            new LatLng(45.094025, -64.364259));
    private Boolean BUILDING_WINDOW_OPEN = false;
    private String BUILDING_NAME_WINDOW_OPEN;
    private final Map<String, Polygon> polyList = new LinkedHashMap<>();
    private Map<String, PolygonOptions> buildingOptions;
    private LatLng cameraPositionSaved;
    private String COLORED_BUILDING = null;

    /**
     * Called when the fragment is first created.
     *
     * @param savedInstanceState The saved state of the application.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            cameraPositionSaved = savedInstanceState.getParcelable("LatLng");
            BUILDING_WINDOW_OPEN = savedInstanceState.getBoolean("hasInfoOpen");
            COLORED_BUILDING = savedInstanceState.getString("coloredBuilding");
            if (BUILDING_WINDOW_OPEN) {
                BUILDING_NAME_WINDOW_OPEN = savedInstanceState.getString("BuildingName");
            }
        }
    }

    /**
     * Creates the view located in activity_maps.xml and land/activity_maps.xml
     *
     * @param inflater           The inflater for the view.
     * @param container          The container for the view.
     * @param savedInstanceState The savedData of the application.
     * @return the View that was created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    /**
     * @param view               the fully inflated view
     * @param savedInstanceState the saved data of the Application.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();

        final AutoCompleteTextView textView = (AutoCompleteTextView) getActivity()
                .findViewById(R.id.BuildingSearch);
        final String[] buildings = getResources().getStringArray(R.array.building_names);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, buildings);


        /*
         * Using an autocomplete TextView allows us to store all the building information, and
         * only show the relevant data to users. This removes the annoyingly long scroll
         * windows when searching through buildings for a particular one.
         */
        textView.setAdapter(adapter);
        Log.d("Adapter Set on TextView", "");
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String buildingWanted = adapter.getItem(position);
                String buildingCode = buildingWanted.substring(buildingWanted.length() - 3);

                if (map != null) {
                    PolygonOptions bldOption = buildingOptions.get(buildingCode);

                    if (bldOption == null) {
                        /* No building, lets leave before something breaks... */
                        return;
                    }
                    if (COLORED_BUILDING != null) {
                        polyList.get(COLORED_BUILDING).setFillColor(Color.TRANSPARENT);
                    }

                    /*
                     * Filling the building with red allows the user to quickly identify the
                     * building they are interested in. Red and black have great contrast, as
                     * well as red and tan. This means that the building jumps out at the user,
                     * quickly drawing their focus to the building.
                     */
                    polyList.get(buildingCode).setFillColor(Color.RED);
                    COLORED_BUILDING = buildingCode;
                    /*
                    * Moving to building that was selected. This, along with the coloring
                    * of the building means that the user does not need to look around for
                    * their building, and can quickly click on the building for more
                    * information.
                    */
                    CameraPosition cp = new CameraPosition.Builder()
                            .target(bldOption.getPoints().get(0))
                            .zoom(map.getCameraPosition().zoom)
                            .bearing(map.getCameraPosition().bearing)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                }
                //Hiding the keyboard so the whole building can be shown
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        if (map != null) {
            onMapReady();
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

        PolygonOptions AACOptions = new PolygonOptions().add(
                new LatLng(45.092708, -64.369103),
                new LatLng(45.091784, -64.368792),
                new LatLng(45.091754, -64.368931),
                new LatLng(45.091286, -64.368781),
                new LatLng(45.091290, -64.368714),
                new LatLng(45.091040, -64.368634),
                new LatLng(45.091087, -64.368409),
                new LatLng(45.091170, -64.368433),
                new LatLng(45.091320, -64.367677),
                new LatLng(45.091445, -64.367720),
                new LatLng(45.091426, -64.367838),
                new LatLng(45.091547, -64.367876),
                new LatLng(45.091560, -64.367830),
                new LatLng(45.092136, -64.368047),
                new LatLng(45.092109, -64.368205),
                new LatLng(45.092823, -64.368487));

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

        PolygonOptions BANOptions = new PolygonOptions().add(
                new LatLng(45.089018, -64.364889),
                new LatLng(45.088924, -64.364880),
                new LatLng(45.088930, -64.364716),
                new LatLng(45.089028, -64.364732));

        PolygonOptions BIOOptions = new PolygonOptions().add(
                new LatLng(45.088391, -64.369175),
                new LatLng(45.088448, -64.368901),
                new LatLng(45.088103, -64.368783),
                new LatLng(45.088054, -64.369051));

        PolygonOptions CAROptions = new PolygonOptions().add(
                new LatLng(45.088264, -64.367545),
                new LatLng(45.088321, -64.367153),
                new LatLng(45.088101, -64.367083),
                new LatLng(45.088090, -64.367147),
                new LatLng(45.087935, -64.367109),
                new LatLng(45.087886, -64.367366),
                new LatLng(45.088060, -64.367420),
                new LatLng(45.088056, -64.367474));

        PolygonOptions CHAOptions = new PolygonOptions().add(
                new LatLng(45.086434, -64.365761),
                new LatLng(45.086279, -64.365715),
                new LatLng(45.086313, -64.365495),
                new LatLng(45.086518, -64.365551),
                new LatLng(45.086539, -64.365438),
                new LatLng(45.086626, -64.365470),
                new LatLng(45.086662, -64.365229),
                new LatLng(45.086389, -64.365151),
                new LatLng(45.086370, -64.365269),
                new LatLng(45.086201, -64.365215),
                new LatLng(45.086231, -64.364998),
                new LatLng(45.086464, -64.365068),
                new LatLng(45.086481, -64.364966),
                new LatLng(45.086646, -64.365017),
                new LatLng(45.086629, -64.365148),
                new LatLng(45.086835, -64.365204),
                new LatLng(45.086799, -64.365433),
                new LatLng(45.086715, -64.365408),
                new LatLng(45.086661, -64.365738),
                new LatLng(45.086454, -64.365668));

        PolygonOptions CHIOptions = new PolygonOptions().add(
                new LatLng(45.087184, -64.367195),
                new LatLng(45.087327, -64.366440),
                new LatLng(45.087141, -64.366376),
                new LatLng(45.087008, -64.367122));

        PolygonOptions CLCOptions = new PolygonOptions().add(
                new LatLng(45.086926, -64.367144),
                new LatLng(45.086792, -64.367106),
                new LatLng(45.086837, -64.366811),
                new LatLng(45.086973, -64.366854));

        PolygonOptions CRFOptions = new PolygonOptions().add(
                new LatLng(45.086746, -64.367042),
                new LatLng(45.086774, -64.366844),
                new LatLng(45.086496, -64.366753),
                new LatLng(45.086466, -64.366946));

        PolygonOptions CROOptions = new PolygonOptions().add(
                new LatLng(45.085245, -64.364424),
                new LatLng(45.085485, -64.364519),
                new LatLng(45.085540, -64.3642921),
                new LatLng(45.085295, -64.364187));

        PolygonOptions CUTOptions = new PolygonOptions().add(
                new LatLng(45.085887, -64.366730),
                new LatLng(45.085934, -64.366306),
                new LatLng(45.085796, -64.366295),
                new LatLng(45.085768, -64.366531),
                new LatLng(45.085476, -64.366461),
                new LatLng(45.085495, -64.366163),
                new LatLng(45.085673, -64.366198),
                new LatLng(45.085688, -64.366024),
                new LatLng(45.085423, -64.365946),
                new LatLng(45.085334, -64.366533));

        PolygonOptions DENOptions = new PolygonOptions().add(
                new LatLng(45.087214, -64.365395),
                new LatLng(45.087034, -64.365325),
                new LatLng(45.087144, -64.364724),
                new LatLng(45.087324, -64.364786));

        PolygonOptions DIVOptions = new PolygonOptions().add(
                new LatLng(45.087509, -64.364088),
                new LatLng(45.087318, -64.364034),
                new LatLng(45.087379, -64.363624),
                new LatLng(45.087584, -64.363688),
                new LatLng(45.087576, -64.363739),
                new LatLng(45.087634, -64.363760),
                new LatLng(45.087587, -64.364053),
                new LatLng(45.087516, -64.364033));

        PolygonOptions EATOptions = new PolygonOptions().add(
                new LatLng(45.086479, -64.366722),
                new LatLng(45.086333, -64.366682),
                new LatLng(45.086405, -64.366205),
                new LatLng(45.086553, -64.366248));

        PolygonOptions ELLOptions = new PolygonOptions().add(
                new LatLng(45.090189, -64.369055),
                new LatLng(45.090229, -64.368820),
                new LatLng(45.089782, -64.368686),
                new LatLng(45.089718, -64.369040),
                new LatLng(45.089824, -64.369078),
                new LatLng(45.089847, -64.368965));

        PolygonOptions EMMOptions = new PolygonOptions().add(
                new LatLng(45.088041, -64.366948),
                new LatLng(45.088098, -64.366621),
                new LatLng(45.087981, -64.366583),
                new LatLng(45.087973, -64.366615),
                new LatLng(45.087935, -64.366615),
                new LatLng(45.087897, -64.366851),
                new LatLng(45.087954, -64.366867),
                new LatLng(45.087943, -64.366905));

        PolygonOptions FOUOptions = new PolygonOptions().add(
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

        PolygonOptions FTBOptions = new PolygonOptions().add(
                new LatLng(45.092587, -64.365607),
                new LatLng(45.092018, -64.365469),
                new LatLng(45.092056, -64.365099),
                new LatLng(45.092630, -64.365233));

        PolygonOptions GODOptions = new PolygonOptions().add(
                new LatLng(45.089437, -64.365000),
                new LatLng(45.089348, -64.365005),
                new LatLng(45.089344, -64.364868),
                new LatLng(45.089439, -64.364871));

        PolygonOptions H24Options = new PolygonOptions().add(
                new LatLng(45.089166, -64.364322),
                new LatLng(45.089111, -64.364319),
                new LatLng(45.089113, -64.364276),
                new LatLng(45.089069, -64.364265),
                new LatLng(45.089082, -64.364093),
                new LatLng(45.089213, -64.364117),
                new LatLng(45.089198, -64.364278));

        PolygonOptions HAYOptions = new PolygonOptions().add(
                new LatLng(45.089399, -64.364820),
                new LatLng(45.089405, -64.364589),
                new LatLng(45.089301, -64.364581),
                new LatLng(45.089299, -64.364611),
                new LatLng(45.089238, -64.364606),
                new LatLng(45.089232, -64.364735),
                new LatLng(45.089287, -64.364746),
                new LatLng(45.089289, -64.364810));

        PolygonOptions HDHOptions = new PolygonOptions().add(
                new LatLng(45.088693, -64.365745),
                new LatLng(45.088737, -64.365490),
                new LatLng(45.088979, -64.365493),
                new LatLng(45.088979, -64.365313),
                new LatLng(45.088784, -64.365286),
                new LatLng(45.088610, -64.365171),
                new LatLng(45.088526, -64.365373),
                new LatLng(45.088448, -64.365330),
                new LatLng(45.088395, -64.365443),
                new LatLng(45.088393, -64.365658),
                new LatLng(45.088476, -64.365645),
                new LatLng(45.088480, -64.365701),
                new LatLng(45.088582, -64.365696),
                new LatLng(45.088578, -64.365723));

        PolygonOptions HOROptions = new PolygonOptions().add(
                new LatLng(45.089012, -64.368634),
                new LatLng(45.089046, -64.368446),
                new LatLng(45.088690, -64.368333),
                new LatLng(45.088656, -64.368515));

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

        PolygonOptions KCIOptions = new PolygonOptions().add(
                new LatLng(45.087480, -64.369085),
                new LatLng(45.087696, -64.367924),
                new LatLng(45.087298, -64.367801),
                new LatLng(45.087268, -64.367983),
                new LatLng(45.087514, -64.368058),
                new LatLng(45.087397, -64.368793),
                new LatLng(45.087348, -64.368782),
                new LatLng(45.087303, -64.369023));

        PolygonOptions PATOptions = new PolygonOptions().add(
                new LatLng(45.088527, -64.368606),
                new LatLng(45.088588, -64.368252),
                new LatLng(45.088187, -64.368118),
                new LatLng(45.088149, -64.368327),
                new LatLng(45.088403, -64.368424),
                new LatLng(45.088384, -64.368553));

        PolygonOptions RAYOptions = new PolygonOptions().add(
                new LatLng(45.087546, -64.365695),
                new LatLng(45.087446, -64.365649),
                new LatLng(45.087478, -64.365491),
                new LatLng(45.087580, -64.365529));

        PolygonOptions RHOOptions = new PolygonOptions().add(
                new LatLng(45.088614, -64.367817),
                new LatLng(45.088652, -64.367613),
                new LatLng(45.088447, -64.367538),
                new LatLng(45.088439, -64.367592),
                new LatLng(45.088386, -64.367581),
                new LatLng(45.088375, -64.367672),
                new LatLng(45.088417, -64.367699),
                new LatLng(45.088413, -64.367747));

        PolygonOptions RJHOptions = new PolygonOptions().add(
                new LatLng(45.087000, -64.366441),
                new LatLng(45.087049, -64.366194),
                new LatLng(45.086935, -64.366162),
                new LatLng(45.086927, -64.366200),
                new LatLng(45.086712, -64.366130),
                new LatLng(45.086719, -64.366083),
                new LatLng(45.086613, -64.366051),
                new LatLng(45.086579, -64.366292),
                new LatLng(45.086687, -64.366322),
                new LatLng(45.086695, -64.366282),
                new LatLng(45.086905, -64.366354),
                new LatLng(45.086896, -64.366405));

        PolygonOptions SEMOptions = new PolygonOptions().add(
                new LatLng(45.088838, -64.366337),
                new LatLng(45.088561, -64.366259),
                new LatLng(45.088595, -64.366066),
                new LatLng(45.088754, -64.366109),
                new LatLng(45.088781, -64.365873),
                new LatLng(45.088842, -64.365884),
                new LatLng(45.088872, -64.365632),
                new LatLng(45.088914, -64.365637),
                new LatLng(45.088925, -64.365589),
                new LatLng(45.088987, -64.365621),
                new LatLng(45.088985, -64.365661),
                new LatLng(45.089034, -64.365699),
                new LatLng(45.088949, -64.366217),
                new LatLng(45.088856, -64.366196));

        PolygonOptions SUBOptions = new PolygonOptions().add(
                new LatLng(45.088608, -64.364786),
                new LatLng(45.088335, -64.364696),
                new LatLng(45.088352, -64.364618),
                new LatLng(45.088265, -64.364615),
                new LatLng(45.088246, -64.364687),
                new LatLng(45.088121, -64.364649),
                new LatLng(45.088121, -64.364590),
                new LatLng(45.087898, -64.364507),
                new LatLng(45.087915, -64.364397),
                new LatLng(45.088138, -64.364456),
                new LatLng(45.088185, -64.364207),
                new LatLng(45.088302, -64.364250),
                new LatLng(45.088323, -64.364132),
                new LatLng(45.088394, -64.364147),
                new LatLng(45.088395, -64.364205),
                new LatLng(45.088432, -64.364223),
                new LatLng(45.088448, -64.364123),
                new LatLng(45.088610, -64.364178),
                new LatLng(45.088602, -64.364269),
                new LatLng(45.088776, -64.364341),
                new LatLng(45.088715, -64.364698),
                new LatLng(45.088630, -64.364681));

        PolygonOptions UNHOptions = new PolygonOptions().add(
                new LatLng(45.089235, -64.367396),
                new LatLng(45.089337, -64.366691),
                new LatLng(45.089095, -64.366621),
                new LatLng(45.089065, -64.366857),
                new LatLng(45.089012, -64.366846),
                new LatLng(45.088974, -64.367039),
                new LatLng(45.089027, -64.367066),
                new LatLng(45.088989, -64.367318));

        PolygonOptions W17Options = new PolygonOptions().add(
                new LatLng(45.089337, -64.369538),
                new LatLng(45.089371, -64.369356),
                new LatLng(45.089231, -64.369313),
                new LatLng(45.089197, -64.369501));

        PolygonOptions WHEOptions = new PolygonOptions().add(
                new LatLng(45.086921, -64.364586),
                new LatLng(45.086752, -64.364532),
                new LatLng(45.086745, -64.364552),
                new LatLng(45.086539, -64.364490),
                new LatLng(45.086626, -64.363935),
                new LatLng(45.086851, -64.364007),
                new LatLng(45.086840, -64.364079),
                new LatLng(45.086993, -64.364130));

        PolygonOptions WHIOptions = new PolygonOptions().add(
                new LatLng(45.088012, -64.365645),
                new LatLng(45.088037, -64.365511),
                new LatLng(45.087851, -64.365441),
                new LatLng(45.087896, -64.365162),
                new LatLng(45.088076, -64.365218),
                new LatLng(45.088103, -64.365073),
                new LatLng(45.087830, -64.364976),
                new LatLng(45.087732, -64.365558));

        PolygonOptions WICOptions = new PolygonOptions().add(
                new LatLng(45.089438, -64.364325),
                new LatLng(45.089258, -64.364320),
                new LatLng(45.089260, -64.364178),
                new LatLng(45.089442, -64.364191));

        PolygonOptions WILOptions = new PolygonOptions().add(
                new LatLng(45.087682, -64.367232),
                new LatLng(45.087712, -64.367066),
                new LatLng(45.087458, -64.366975),
                new LatLng(45.087431, -64.367136));

        PolygonOptions WMHOptions = new PolygonOptions().add(
                new LatLng(45.088780, -64.369442),
                new LatLng(45.088814, -64.369286),
                new LatLng(45.088712, -64.369248),
                new LatLng(45.088748, -64.369042),
                new LatLng(45.088831, -64.369063),
                new LatLng(45.088865, -64.368902),
                new LatLng(45.088626, -64.368816),
                new LatLng(45.088535, -64.369358));

        //Standardizing building codes across the app.
        buildingOptions.put("AAC", AACOptions);
        buildingOptions.put("BAC", BACOptions);
        buildingOptions.put("BAN", BANOptions);
        buildingOptions.put("BIO", BIOOptions);
        buildingOptions.put("CAR", CAROptions);
        buildingOptions.put("CHA", CHAOptions);
        buildingOptions.put("CHI", CHIOptions);
        buildingOptions.put("CLC", CLCOptions);
        buildingOptions.put("CRF", CRFOptions);
        buildingOptions.put("CRO", CROOptions);
        buildingOptions.put("CUT", CUTOptions);
        buildingOptions.put("DEN", DENOptions);
        buildingOptions.put("DIV", DIVOptions);
        buildingOptions.put("EAT", EATOptions);
        buildingOptions.put("ELL", ELLOptions);
        buildingOptions.put("EMM", EMMOptions);
        buildingOptions.put("FOU", FOUOptions);
        buildingOptions.put("FTB", FTBOptions);
        buildingOptions.put("GOD", GODOptions);
        buildingOptions.put("H24", H24Options);
        buildingOptions.put("HAY", HAYOptions);
        buildingOptions.put("HDH", HDHOptions);
        buildingOptions.put("HOR", HOROptions);
        buildingOptions.put("HSH", HSHOptions);
        buildingOptions.put("KCI", KCIOptions);
        buildingOptions.put("PAT", PATOptions);
        buildingOptions.put("RAY", RAYOptions);
        buildingOptions.put("RHO", RHOOptions);
        buildingOptions.put("RJH", RJHOptions);
        buildingOptions.put("SEM", SEMOptions);
        buildingOptions.put("SUB", SUBOptions);
        buildingOptions.put("UNH", UNHOptions);
        buildingOptions.put("W17", W17Options);
        buildingOptions.put("WHE", WHEOptions);
        buildingOptions.put("WHI", WHIOptions);
        buildingOptions.put("WIC", WICOptions);
        buildingOptions.put("WIL", WILOptions);
        buildingOptions.put("WMH", WMHOptions);

        return buildingOptions;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private void onMapReady() {
        /* Start at University hall. */
        LatLng target = new LatLng(45.088845, -64.366850);
        /*
         * Creating a new camera position with the correct bearing, since south is the
         * top of campus, we want the map "upside down". This is more how the average user
         * will think about the campus, with the top of the hill at the top of the screen.
         */

        if (cameraPositionSaved != null) {
            target = cameraPositionSaved;
        }
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                //Centering on University hall to start
                .target(target)
                .bearing(165)
                .zoom(17)
                .build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        /*
         *We want people to only be able to look at Acadia University, so
         *lets set boundaries on the maps position.
         */

        /*
         * We chose these settings to reduce clutter on the map surface, while
         * still allowing the user to change key settings (Zoom and rotation).
         * This makes it easier for the user to find what they are looking for.
         */

        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setBuildingsEnabled(false);
        /*
        * Creating the points for our building outlines.
        * We chose to use a map because it allows us to tie the set of points to the
        * building code, will make it easier to identify each set of points.
        */
        buildingOptions = createPoints();

        /*
        * Iterates through the map to add the building outlines. Adds the polygons to
        * a map in order to keep track of them. We chose to keep the outlines black in order
        * to improve contrast between the outline lines and the background. Black and our tan
        * have a contrast rating of 15.5 from http://leaverou.github.io, which means it has
        * a lot of contrast. The outlines of the buildings afford clicking, they invite the
        * user to click on them.
        */
        for (Map.Entry<String, PolygonOptions> entry : buildingOptions.entrySet()) {
            Polygon i = map.addPolygon(entry.getValue());
            polyList.put(entry.getKey(), i);
        }

        if (BUILDING_WINDOW_OPEN) {
            Log.i("Opening From Saved:", BUILDING_NAME_WINDOW_OPEN);
            addFragment(BUILDING_NAME_WINDOW_OPEN);
        }
        if (COLORED_BUILDING != null) {
            Log.d("ColoredBuilding:", COLORED_BUILDING);
            polyList.get(COLORED_BUILDING).setFillColor(Color.RED);
        }

        /* Checking if the user has tapped on a building or not */
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                for (Map.Entry<String, Polygon> code : polyList.entrySet()) {
                    /* Checking if they clicked on a building */
                    if (PolyUtil.containsLocation(point, code.getValue().getPoints(), false)) {
                        /* Checking if a building is open right now... */
                        if (BUILDING_WINDOW_OPEN) {
                            removeFragment();
                        }

                        BUILDING_NAME_WINDOW_OPEN = code.getKey();
                        addFragment(code.getKey());
                        return;
                    }
                }
                /*
                 * Didn't tap on any building, so lets remove the current fragment,
                 * if there is one.
                 */
                removeFragment();
            }
        });

        /* Letting the map know what to do when it updates */
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            /*
             * We want to be able to restrict the user from leaving Acadia, so lets
             * (gently) let them know that they can not leave the area by snapping back
             * to the closest edge if they stray too far. Using this technique is
             * good design because it reminds the user to remain within the borders without being
             * aggressive.
             */

            private final int LAST_CALLED_THRESHOLD_MS = 250;
            private long lastCalled = Long.MIN_VALUE;

            @Override
            public void onCameraChange(CameraPosition position) {
                /* This attempts to prevent camera action when the camera is moving. */
                final long currentTime = System.currentTimeMillis();
                if (lastCalled + LAST_CALLED_THRESHOLD_MS > currentTime) {
                    return;
                }
                LatLng center = position.target;
                LatLng closeEdge = ACADIA.getCenter();

                if (!ACADIA.contains(center)) {
                    /* north, going north edge */
                    if (center.latitude > ACADIA.northeast.latitude) {
                        closeEdge = new LatLng(ACADIA.northeast.latitude, center.longitude);
                    }
                    /* south, going to south edge */
                    else if (center.latitude < ACADIA.southwest.latitude) {
                        closeEdge = new LatLng(ACADIA.southwest.latitude, center.longitude);
                    }
                    /* east, going to east edge */
                    else if (center.longitude > ACADIA.northeast.longitude) {
                        closeEdge = new LatLng(center.latitude, ACADIA.northeast.longitude);
                    }
                    /* west, going to west edge */
                    else if (center.longitude < ACADIA.southwest.longitude) {
                        closeEdge = new LatLng(center.latitude, ACADIA.southwest.longitude);
                    }

                    /* updating the camera position */
                    CameraPosition goingTo = new CameraPosition.Builder()
                            .target(closeEdge)
                            .zoom(position.zoom)
                            .bearing(position.bearing)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(goingTo));
                }
                lastCalled = currentTime;
            }
        });
    }

    /**
     * This function will add a fragment to the map activity, providing information about
     * the building that the user tapped on.
     *
     * @param bldName The name of the building that the fragment will be created about.
     */
    private void addFragment(String bldName) {
        BUILDING_WINDOW_OPEN = true;
        Fragment buildingInfo = new BuildingInfo();

        Bundle bundle = new Bundle();
        bundle.putString("buildingName", bldName);
        buildingInfo.setArguments(bundle);

        Log.i("Opening:", BUILDING_NAME_WINDOW_OPEN);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.building_info_placeholder, buildingInfo);
        transaction.addToBackStack("BuildingInfo").commit();
    }

    /**
     * This function removes the fragment that is currently on top of the map.
     */
    private void removeFragment() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack();
            Log.i("Removing:", BUILDING_NAME_WINDOW_OPEN);
            BUILDING_WINDOW_OPEN = false;
        }
    }

    /**
     * Saves the current state of the program in order to restore it at a later date,
     * such as during a rotation.
     *
     * @param outState The state of the methods above it.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("LatLng", map.getCameraPosition().target);
        outState.putBoolean("hasInfoOpen", (getChildFragmentManager().getBackStackEntryCount() > 0));
        outState.putString("coloredBuilding", COLORED_BUILDING);
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            outState.putString("BuildingName", BUILDING_NAME_WINDOW_OPEN);
        }
        Log.i("onSaveInstanceState:", "All saved, goodbye.");
    }
}


