package nraut.mapcheck;

/**
 * Created by naina on 5/20/2016.
 */
import android.content.Context;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoPoint;
import com.backendless.geo.Units;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DatabaseConnection {
    ProcessData pd = new ProcessData();

    public void saveDriverTable(final String email, final double origin_lat, final double origin_lng,
                                final double dest_lat, final double dest_lng, String passengerEmail,
                                String status, final Context context) {

        System.out.print("Origin" + origin_lat);
        System.out.print("Destination" + dest_lat);

        //new change
        BackendlessGeoQuery geoQuery = new BackendlessGeoQuery();
        geoQuery.addCategory("DriverOrigin");
        geoQuery.setIncludeMeta(true);
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("EmailAddress", email);
        geoQuery.setMetadata(meta);
        final List<String> categories1 = new ArrayList<String>();
        categories1.add("DriverOrigin");

        Backendless.Geo.getPoints(geoQuery, new AsyncCallback<BackendlessCollection<GeoPoint>>() {
            @Override
            public void handleResponse(BackendlessCollection<GeoPoint> geoPointCollection) {
                Iterator<GeoPoint> iterator = geoPointCollection.getCurrentPage().iterator();
                List<GeoPoint> GeoListPassengerOrigin = new ArrayList<GeoPoint>();

                //remove previous point
                if (iterator.hasNext()) {
                    GeoPoint geoPoint = iterator.next();
                    Backendless.Geo.removePoint(geoPoint, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void aVoid) {
                            System.out.println("Geo point deleted successfully");
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            System.out.println("Geo point not deleted successfully");
                        }
                    });
                }

                //add new point
                Backendless.Geo.savePoint(origin_lat, origin_lng, categories1, meta, new AsyncCallback<GeoPoint>() {
                    @Override
                    public void handleResponse(GeoPoint geoPoint) {
                        System.out.println(geoPoint.getObjectId());
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        System.out.println("error");
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.print("Error in Geo query");
            }

            ;
        });
        //end new change

        BackendlessGeoQuery geoQuery2 = new BackendlessGeoQuery();
        geoQuery2.addCategory("DriverDestination");
        geoQuery2.setIncludeMeta(true);
        geoQuery2.setMetadata(meta);
        final List<String> categories2 = new ArrayList<String>();
        categories2.add("DriverDestination");
        Backendless.Geo.getPoints(geoQuery2, new AsyncCallback<BackendlessCollection<GeoPoint>>() {
            @Override
            public void handleResponse(BackendlessCollection<GeoPoint> geoPointCollection) {
                Iterator<GeoPoint> iterator = geoPointCollection.getCurrentPage().iterator();
                List<GeoPoint> GeoListPassengerOrigin = new ArrayList<GeoPoint>();

                //remove previous point
                if (iterator.hasNext()) {
                    GeoPoint geoPoint = iterator.next();
                    Backendless.Geo.removePoint(geoPoint, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void aVoid) {
                            System.out.println("Geo point deleted successfully");
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            System.out.println("Geo point not deleted successfully");
                        }
                    });
                }

                //add new point
                Backendless.Geo.savePoint(dest_lat, dest_lng, categories2, meta, new AsyncCallback<GeoPoint>() {
                    @Override
                    public void handleResponse(GeoPoint geoPoint) {
                        System.out.println(geoPoint.getObjectId());
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        System.out.println("error");
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.print("Error in Geo query");
            }
            ;
        });


        //To fetch latitude and longitude by radius from PassengerOrigin
        BackendlessGeoQuery geoQuery3 = new BackendlessGeoQuery();
        geoQuery3.setLatitude(origin_lat);
        geoQuery3.setLongitude(origin_lng);
        geoQuery3.addCategory("PassengerOrigin");
        geoQuery3.setIncludeMeta(true);
        geoQuery3.setRadius(5d);
        geoQuery3.setUnits(Units.MILES);

        Backendless.Geo.getPoints(geoQuery3, new AsyncCallback<BackendlessCollection<GeoPoint>>() {
            @Override
            public void handleResponse(BackendlessCollection<GeoPoint> geoPointCollection) {
                Iterator<GeoPoint> iterator = geoPointCollection.getCurrentPage().iterator();
                List<GeoPoint> GeoListPassengerOrigin = new ArrayList<GeoPoint>();

                while (iterator.hasNext()) {
                    GeoPoint geoPoint = iterator.next();
                    System.out.println("GeoPoint - " + geoPoint);
                    System.out.println("GeoPoint - " + geoPoint.getMetadata("EmailAddress"));
                    GeoListPassengerOrigin.add(geoPoint);
                }

                pd.getOriginGeoData(GeoListPassengerOrigin);

                //get geo points from passengerDestination
                BackendlessGeoQuery geoQuery4 = new BackendlessGeoQuery();
                geoQuery4.setLatitude(dest_lat);
                geoQuery4.setLongitude(dest_lng);
                geoQuery4.addCategory("PassengerDestination");
                geoQuery4.setIncludeMeta(true);
                geoQuery4.setRadius(5d);
                geoQuery4.setUnits(Units.MILES);

                Backendless.Geo.getPoints(geoQuery4, new AsyncCallback<BackendlessCollection<GeoPoint>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<GeoPoint> geoPointCollection) {
                        Iterator<GeoPoint> iterator = geoPointCollection.getCurrentPage().iterator();
                        List<GeoPoint> GeoListPassengerDest = new ArrayList<GeoPoint>();

                        while (iterator.hasNext()) {
                            GeoPoint geoPoint = iterator.next();
                            System.out.println("GeoPoint - " + geoPoint);
                            System.out.println("GeoPoint - " + geoPoint.getMetadata("EmailAddress"));
                            GeoListPassengerDest.add(geoPoint);
                        }
                        pd.getDestGeoData(GeoListPassengerDest,context);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        System.out.println("Server reported an error - " + backendlessFault.getMessage());
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Server reported an error - " + backendlessFault.getMessage());
            }
        });
    }

    public void savePassengerTable(String email, final double origin_lat, final double origin_lng,
                                   final double dest_lat, final double dest_lng, String passengerEmail,
                                   String status,  final Context context) {
        //new change
        BackendlessGeoQuery geoQuery = new BackendlessGeoQuery();
        geoQuery.addCategory("PassengerOrigin");
        geoQuery.setIncludeMeta(true);
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("EmailAddress", email);
        geoQuery.setMetadata(meta);
        final List<String> categories1 = new ArrayList<String>();
        categories1.add("PassengerOrigin");

        Backendless.Geo.getPoints(geoQuery, new AsyncCallback<BackendlessCollection<GeoPoint>>() {
            @Override
            public void handleResponse(BackendlessCollection<GeoPoint> geoPointCollection) {
                Iterator<GeoPoint> iterator = geoPointCollection.getCurrentPage().iterator();
                List<GeoPoint> GeoListPassengerOrigin = new ArrayList<GeoPoint>();

                //remove previous point
                if (iterator.hasNext()) {
                    GeoPoint geoPoint = iterator.next();
                    Backendless.Geo.removePoint(geoPoint, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void aVoid) {
                            System.out.println("Geo point deleted successfully");
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            System.out.println("Geo point not deleted successfully");
                        }
                    });
                }

                //add new point
                Backendless.Geo.savePoint(origin_lat, origin_lng, categories1, meta, new AsyncCallback<GeoPoint>() {
                    @Override
                    public void handleResponse(GeoPoint geoPoint) {
                        System.out.println(geoPoint.getObjectId());
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        System.out.println("error");
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.print("Error in Geo query");
            }

            ;
        });
        //end new change

        BackendlessGeoQuery geoQuery2 = new BackendlessGeoQuery();
        geoQuery2.addCategory("PassengerDestination");
        geoQuery2.setIncludeMeta(true);
        geoQuery2.setMetadata(meta);
        final List<String> categories2 = new ArrayList<String>();
        categories2.add("PassengerDestination");
        Backendless.Geo.getPoints(geoQuery2, new AsyncCallback<BackendlessCollection<GeoPoint>>() {
            @Override
            public void handleResponse(BackendlessCollection<GeoPoint> geoPointCollection) {
                Iterator<GeoPoint> iterator = geoPointCollection.getCurrentPage().iterator();
                List<GeoPoint> GeoListPassengerOrigin = new ArrayList<GeoPoint>();

                //remove previous point
                if (iterator.hasNext()) {
                    GeoPoint geoPoint = iterator.next();
                    Backendless.Geo.removePoint(geoPoint, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void aVoid) {
                            System.out.println("Geo point deleted successfully");
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            System.out.println("Geo point not deleted successfully");
                        }
                    });
                }

                //add new point
                Backendless.Geo.savePoint(dest_lat, dest_lng, categories2, meta, new AsyncCallback<GeoPoint>() {
                    @Override
                    public void handleResponse(GeoPoint geoPoint) {
                        System.out.println(geoPoint.getObjectId());
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        System.out.println("error");
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.print("Error in Geo query");
            }

            ;
        });


        //To fetch latitude and longitude by radius from DriverOrigin
        BackendlessGeoQuery geoQuery3 = new BackendlessGeoQuery();
        geoQuery3.setLatitude(origin_lat);
        geoQuery3.setLongitude(origin_lng);
        geoQuery3.addCategory("DriverOrigin");
        geoQuery3.setIncludeMeta(true);
        geoQuery3.setRadius(5d);
        geoQuery3.setUnits(Units.MILES);

        Backendless.Geo.getPoints(geoQuery3, new AsyncCallback<BackendlessCollection<GeoPoint>>() {
            @Override
            public void handleResponse(BackendlessCollection<GeoPoint> geoPointCollection) {
                Iterator<GeoPoint> iterator = geoPointCollection.getCurrentPage().iterator();
                List<GeoPoint> GeoListDriverOrigin = new ArrayList<GeoPoint>();

                while (iterator.hasNext()) {
                    GeoPoint geoPoint = iterator.next();
                    System.out.println("GeoPoint - " + geoPoint);
                    System.out.println("GeoPoint - " + geoPoint.getMetadata("EmailAddress"));
                    GeoListDriverOrigin.add(geoPoint);
                }
                pd.getOriginGeoData(GeoListDriverOrigin);

                //get geo points from passengerDestination
                BackendlessGeoQuery geoQuery4 = new BackendlessGeoQuery();
                geoQuery4.setLatitude(dest_lat);
                geoQuery4.setLongitude(dest_lng);
                geoQuery4.addCategory("DriverDestination");
                geoQuery4.setIncludeMeta(true);
                geoQuery4.setRadius(5d);
                geoQuery4.setUnits(Units.MILES);

                Backendless.Geo.getPoints( geoQuery4, new AsyncCallback<BackendlessCollection<GeoPoint>>()
                {
                    @Override
                    public void handleResponse( BackendlessCollection<GeoPoint> geoPointCollection )
                    {
                        Iterator<GeoPoint> iterator = geoPointCollection.getCurrentPage().iterator();
                        List<GeoPoint> GeoListDriverDest = new ArrayList<GeoPoint>();

                        while( iterator.hasNext() ) {
                            GeoPoint geoPoint = iterator.next();
                            System.out.println("GeoPoint - " + geoPoint);
                            System.out.println("GeoPoint - " + geoPoint.getMetadata("EmailAddress"));
                            GeoListDriverDest.add(geoPoint);
                        }
                        pd.getDestGeoData(GeoListDriverDest,context);
                    }

                    @Override
                    public void handleFault( BackendlessFault backendlessFault )
                    {
                        System.out.println( "Server reported an error - " + backendlessFault.getMessage() );
                    }
                } );
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Server reported an error - " + backendlessFault.getMessage());
            }
        });
    }



    public void saveUsersTable(String email,String username,String pic,String pass,String mobile)
    {
        Users userTable = new Users();
        userTable.setEmail(email);
        userTable.setName(username);
        userTable.setProfilePic(pic);
        userTable.setPassword(pass);
        userTable.setMobilenumber(mobile);

        Backendless.Persistence.save(userTable, new AsyncCallback<Users>() {
            public void handleResponse(Users response) {
                System.out.println("Sucesss");
            }

            public void handleFault(BackendlessFault fault) {
                System.out.println("Errorrrr");
            }
        });
    }

    public void insertOriginDestination(String loginEmail, final String origin, final String dest){
        String whereClause = "email='"+loginEmail+"'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Backendless.Data.of(Users.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Users>>() {
            @Override
            public void handleResponse(BackendlessCollection<Users> usersBackendlessCollection) {
                Iterator<Users> iterater = usersBackendlessCollection.getCurrentPage().iterator();
                if (iterater.hasNext()) {
                    Users userTable = iterater.next();
                    userTable.setOrigin(origin);
                    userTable.setDestination(dest);
                    Backendless.Persistence.save(userTable, new AsyncCallback<Users>() {
                        @Override
                        public void handleResponse(Users users) {
                            System.out.println("Sucesss");
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            System.out.println("Error");
                        }
                    });
                }
                else {
                    System.out.println("Record doesnot exists");
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Errorrrr");
            }
        });
    }
}

