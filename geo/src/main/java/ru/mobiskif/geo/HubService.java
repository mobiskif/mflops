package ru.mobiskif.geo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.preference.PreferenceManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import javax.net.ssl.HttpsURLConnection;

class HubService extends Observable {
    String GUID = "039E2126-0FCA-4E13-8AD6-AF303F7F0FC1";
    /*

gorzdrav 039E2126-0FCA-4E13-8AD6-AF303F7F0FC1

ЕПГУ 94AD0743-86BC-42D5-8EAA-DA80B6BC11C5

Мобильные приложения 6B2158A1-56E0-4C09-B70B-139B14FFEE14

     */
    SharedPreferences settings;
    String TAG;
    //String param;
    String[] from = {"_ID", "column1", "column2", "column3", "column4", "column5", "column6", "column7", "column8", "column9", "column10", "column11", "column12", "column13", "column14"};

    public HubService(Activity c) {
        SharedPreferences defsettings = PreferenceManager.getDefaultSharedPreferences(c);
        String currentPatient = defsettings.getString("currentUser", "0");
        settings = c.getSharedPreferences(currentPatient,0);
        TAG=this.getClass().getSimpleName()+" jop";
        //param=c.param;
    }

    void fill(String action, String name, MatrixCursor mc, Object[] row, String text) {
        if (action.equals("GetDistrictList")) {
            switch (name) {
                case "DistrictName":
                    row[2] = text + " район";
                    row[3] = "";
                    break;
                case "IdDistrict":
                    row[0] = text;
                    row[1] = "";
                    mc.addRow(row);
                    break;
                default:
                    break;
            }
        }
        else if (action.equals("GetLPUList")) {
            switch (name) {
                case "Chief":
                    break;
                case "Contact":
                    break;
                case "Distric":
                    break;
                case "EMail":
                    break;
                case "IdLPU":
                    if (settings.getString("show_all","1").equals("1")) {
                        row[0] = text;
                    }
                    else {
                        String visiblity = settings.getString("lpu_"+text, "1");
                        if (visiblity.equals("1")) row[0]=text;
                            else row[0]=null;
                    }
                    break;
                case "ID":
                    break;
                case "Org_Address":
                    break;
                case "LPUFullName":
                    row[1] = "";
                    row[3] = text;
                    break;
                case "LPUShortName":
                    row[2] = text;
                    if (row[0] != null) mc.addRow(row);
                    break;
                case "WWW":
                    break;
                default:
                    break;
            }
        }
        else if (action.equals("GetSpesialityList")) {
            switch (name) {
                case "CountFreeParticipantIE":
                    row[1] = "Талонов: " + text;
                    break;
                case "IdSpesiality":
                    row[0] = text;
                    break;
                case "NameSpesiality":
                    row[2] = text;
                    row[3] = "";
                    mc.addRow(row);
                    break;
                default:
                    break;
            }
        }
        else if (action.equals("GetDoctorList")) {
            switch (name) {
                case "AriaNumber":
                    //row[3] = text;
                    break;
                case "CountFreeParticipantIE":
                    row[1] = "Талонов: "+text;
                    break;
                case "IdDoc":
                    row[0] = text;
                    break;
                case "Name":
                    row[2] = text;
                    row[3] = "";
                    mc.addRow(row);
                    break;
                default:
                    break;
            }
        }
        else if (action.equals("GetAvaibleAppointments")) {
            switch (name) {
                case "IdAppointment":
                    //row[0] = ++i;
                    row[0] = text;
                    row[3] = text;
                    break;
                case "VisitEnd":
                    int loc = text.indexOf("T");
                    String date=text.substring(0,loc);

                    String time = text.substring(loc+1);
                    loc=time.lastIndexOf(":");
                    time=time.substring(0,loc);
                    row[1] = date;
                    //row[1] = d[2]+"."+d[1]+"."+d[0];
                    //row[3] = time;
                    //row[3] = "";
                    break;
                case "VisitStart":
                    loc = text.indexOf("T");
                    date=text.substring(0,loc);
                    time = text.substring(loc+1);
                    loc=time.lastIndexOf(":");
                    time=time.substring(0,loc);
                    //row[1] = date;
                    row[2] = time;

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dd = null;
                    try {
                        dd = sdf.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    sdf = new SimpleDateFormat("E, d MMM");
                    row[1]=sdf.format(dd);
                    if (new Date().before(dd)) mc.addRow(row);

                    break;
                default:
                    break;
            }

        }

        else if (action.equals("GetOrgList")) {
            switch (name) {
                case "Chief":
                    //row[1] = text;
                    break;
                case "Contact":
                    row[1] = text;
                    break;
                case "Distric":
                    break;
                case "EMail":
                    break;
                case "Hub_ID":
                    break;
                case "ID":
                    row[0] = text;
                    break;
                case "Org_Address":
                    row[2] = text;
                    break;
                case "Org_Name":
                    row[3] = text;
                    break;
                case "Org_Type":
                    String hubid = text;
                    row[3]+=" "+ hubid;
                    if (row[2] != null && !hubid.contains("Аптеки")) {
                        mc.addRow(row);
                    }
                    break;
                case "WWW":
                    break;
                default:
                    break;
            }

        }
        else if (action.equals("SearchTop10Patient")) {
            switch (name) {
                case "AriaNumber":
                    if(text==null) row[3] = "нет участка"; else row[3] = text;
                    //row[1] = text;
                    break;
                case "Birthday":
                    if(text!=null) row[1] = text.split("T")[0];
                    break;
                case "CellPhone":
                    //row[3] = text;
                    break;
                case "Document_N":
                    row[4] = text;
                    break;
                case "Document_S":
                    row[5] = text;
                    break;
                case "HomePhone":
                    row[6] = text;
                    break;
                case "IdPat":
                    row[0] = text;
                    //row[1] += ", "+text;

                    break;
                case "Name":
                    row[2] = text;
                    break;
                case "Polis_N":
                    row[9] = text;
                    break;
                case "Polis_S":
                    row[10] = text;
                    break;
                case "SecondName":
                    row[2] +=" "+text;
                    break;
                case "Surname":
                    row[2] +=" "+text;
                    Log.d(TAG, row[0]+" "+row[1]+" "+row[2]+" "+row[3] );
                    mc.addRow(row);
                    break;
                default:
                    break;
            }

        }

        else {}
    }

    String getQBody(String action) {
        String q1="";
        String districtid = settings.getString("GetDistrictList", "4");
        String lpuid = settings.getString("GetLPUList", "27");
        String specid = settings.getString("GetSpesialityList", "27");
        String docid = settings.getString("GetDoctorList", "27");
        String psurname = settings.getString("Surname", "Пархимович");
        String pname = settings.getString("Name", "Дмитрий");
        String pbirstdate = settings.getString("Birstdate", "1966-09-03");
        switch (action) {
            case "GetDistrictList":
                break;
            case "GetLPUList":
                q1="<tem:IdDistrict>" + districtid + "</tem:IdDistrict>";
                break;
            case "GetSpesialityList":
                q1="<tem:idLpu>"+lpuid+"</tem:idLpu>";
                break;
            case "GetDoctorList":
                q1="<tem:idSpesiality>"+specid+"</tem:idSpesiality><tem:idLpu>"+lpuid+"</tem:idLpu>";
                break;
            case "GetOrgList":
                q1="<tem:IdDistrict>" + districtid + "</tem:IdDistrict>";
                break;
            case "GetAvaibleAppointments":
                q1="<tem:idDoc>"+docid+"</tem:idDoc><tem:idLpu>"+lpuid+"</tem:idLpu><tem:visitStart>2018-01-01</tem:visitStart><tem:visitEnd>2018-12-31</tem:visitEnd>";
                break;
            case "SearchTop10Patient":
                q1="<tem:pat><hub:Surname>"+psurname+"</hub:Surname></tem:pat><tem:idLpu>"+lpuid+"</tem:idLpu>";
                break;
        }
        return q1;
    }

    Cursor querySOAP (String action) {
        String q0 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:hub=\"http://schemas.datacontract.org/2004/07/HubService\">" +
                "<soapenv:Header/>\n<soapenv:Body><tem:"+action+">";
        String q1 = getQBody(action);
        String q2 = "<tem:guid>"+GUID+"</tem:guid></tem:"+action+"></soapenv:Body></soapenv:Envelope>";
        XmlPullParser myParser = readSOAP(q0+q1+q2, action);
        int event;
        String text = null;
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        fill(action, name, mc,row,text);
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.e("jop","Ошибка парсинга SOAP " + e.toString());
            return defaultList();
        }
        return mc;
    }

    Cursor GetDistrictList(String action) {
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:GetDistrictList>\n" +
                "         <tem:guid>"+GUID+"</tem:guid>\n" +
                "      </tem:GetDistrictList>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        XmlPullParser myParser = readSOAP(query, action);
        int event;
        String text = null;
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        //mc.addRow(row);
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "DistrictName":
                                row[2] = text + " район";
                                row[3] = "";
                                break;
                            case "IdDistrict":
                                row[0] = text;
                                row[1] = "";
                                mc.addRow(row);
                                break;
                            default:
                                break;
                        }
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.e("jop","Ошибка парсинга SOAP " + e.toString());
            return defaultList();
        }
        return mc;
    }

    Cursor GetOrgList(String action) {
        //Log.d("jop", "========="+action);
        //SharedPreferences settings = activity.getDefaultSharedPreferences(this);
        String districtID = settings.getString("GetDistrictList", "1");
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:GetOrgList>\n" +
                "         <tem:IdDistrict>" + districtID + "</tem:IdDistrict>\n" +
                "         <tem:guid>"+GUID+"</tem:guid>\n" +
                "      </tem:GetOrgList>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        //Log.d(TAG,query);

        XmlPullParser myParser = readSOAP(query, action);
        if (myParser==null) return defaultList();
        else {

            //String[] from = {"_ID", "column1", "column2", "column3"};
            int event;
            String text = null;
            MatrixCursor mc = new MatrixCursor(from);
            Object[] row = new Object[from.length];
            row[0] = "";
            row[1] = "";
            row[2] = "";
            row[3] = "";
            try {
                event = myParser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    String name = myParser.getName();
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            break;

                        case XmlPullParser.TEXT:
                            text = myParser.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            String hubid=null;
                            //row[0]="";
                            row[1]="";
                            //row[2]="";
                            //row[3]="";
                            switch (name) {
                                case "Chief":
                                    row[1] = text;
                                    break;
                                case "Contact":
                                    row[1] = text;
                                    break;
                                case "Distric":
                                    row[1] = text;
                                    break;
                                case "EMail":
                                    break;
                                case "Hub_ID":
                                    row[1] = text;
                                    break;
                                case "ID":
                                    row[0] = text;
                                    break;
                                case "Org_Address":
                                    row[2] = text;
                                    break;
                                case "Org_Name":
                                    row[3] = text;
                                    break;
                                case "Org_Type":
                                    hubid = text;
                                    row[3]+=" "+ hubid;
                                    if (row[2] != null && !hubid.contains("Аптеки")) {
                                        //Log.d(TAG, row[3].toString());
                                        mc.addRow(row);
                                    }
                                    break;
                                case "WWW":
                                    break;
                                default:
                                    break;
                            }
                            text = null;
                    }
                    event = myParser.next();
                }
            } catch (Exception e) {
                Log.e("jop","Ошибка парсинга SOAP " + e.toString());
                return defaultList();
            }
            return mc;
        }
    }

    Cursor GetLPUList(String action) {
        //Log.d("jop", "========="+param);
        //SharedPreferences settings = activity.getDefaultSharedPreferences(this);
        String districtID = settings.getString("GetDistrictList", "17");
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <tem:GetLPUList>" +
                //"         <tem:IdDistrict>" + districtID + "</tem:IdDistrict>" +
                "         <tem:IdDistrict>" + districtID + "</tem:IdDistrict>" +
                "         <tem:guid>"+GUID+"</tem:guid>" +
                "      </tem:GetLPUList>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>";
        //Log.d(TAG,query);

        XmlPullParser myParser = readSOAP(query, action);
        if (myParser==null) return defaultList();
        else {

            String[] from = {"_ID", "column1", "column2", "column3"};
            int event;
            String text = null;
            MatrixCursor mc = new MatrixCursor(from);
            Object[] row = new Object[from.length];
            try {
                event = myParser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    String name = myParser.getName();
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            break;

                        case XmlPullParser.TEXT:
                            text = myParser.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            switch (name) {
                                case "Chief":
                                    break;
                                case "Contact":
                                    break;
                                case "Distric":
                                    break;
                                case "EMail":
                                    break;
                                case "IdLPU":
                                    row[0] = text;
                                    break;
                                case "ID":
                                    break;
                                case "Org_Address":
                                    break;
                                case "LPUFullName":
                                    row[1] = "";
                                    row[3] = text;
                                    break;
                                case "LPUShortName":
                                    row[2] = text;
                                    if (row[0] != null) mc.addRow(row);
                                    break;
                                case "WWW":
                                    break;
                                default:
                                    break;
                            }
                            text = null;
                    }
                    event = myParser.next();
                }
            } catch (Exception e) {
                Log.e("jop","Ошибка парсинга SOAP " + e.toString());
                return defaultList();
            }
            return mc;
        }
    }

    Cursor GetSpesialityList(String action) {
        //SharedPreferences settings = activity.getDefaultSharedPreferences(this);
        String orgID = settings.getString("GetLPUList", "27");
        String idPat = settings.getString("CheckPatient", "452528");

        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:GetSpesialityList>\n" +
                "         <tem:idLpu>"+orgID+"</tem:idLpu>\n" +
                //"         <tem:idPat>"+idPat+"</tem:idPat>\n" +
                "         <tem:guid>"+GUID+"</tem:guid>\n" +
                "      </tem:GetSpesialityList>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        XmlPullParser myParser = readSOAP(query, action);

        String[] from = {"_ID", "column1", "column2", "column3"};
        int event;
        String text = null;
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        //mc.addRow(row);
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "CountFreeParticipantIE":
                                row[1] = "Талонов: "+text;
                                break;
                            case "IdSpesiality":
                                row[0] = text;
                                break;
                            case "NameSpesiality":
                                row[2] = text;
                                row[3] = "";
                                mc.addRow(row);
                                break;
                            default:
                                break;
                        }
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.e("jop","Ошибка парсинга SOAP " + e.toString());
            return defaultList();
        }
        return mc;
    }

    Cursor GetDoctorList(String action) {
        //SharedPreferences settings = activity.getDefaultSharedPreferences(this);
        String specID = settings.getString("GetSpesialityList", "19");
        String orgID = settings.getString("GetLPUList", "27");
        String idPat = settings.getString("CheckPatient", "452528");

        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:GetDoctorList>\n" +
                "         <!--Optional:-->\n" +
                "         <tem:idSpesiality>"+specID+"</tem:idSpesiality>\n" +
                "         <!--Optional:-->\n" +
                "         <tem:idLpu>"+orgID+"</tem:idLpu>\n" +
                "         <!--Optional:-->\n" +
                "         <tem:idPat>"+idPat+"</tem:idPat>\n" +
                //"         <tem:idPat>"+idPat+"</tem:idPat>\n" +
                "         <!--Optional:-->\n" +
                "         <tem:guid>"+GUID+"</tem:guid>\n" +
                "         <!--Optional:-->\n" +
                "         <tem:idHistory>0</tem:idHistory>\n" +
                "      </tem:GetDoctorList>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        XmlPullParser myParser = readSOAP(query, action);

        String[] from = {"_ID", "column1", "column2", "column3"};
        int event;
        String text = null;
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        //mc.addRow(row);
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "AriaNumber":
                                //row[3] = text;
                                break;
                            case "CountFreeParticipantIE":
                                row[1] = "Талонов: "+text;
                                break;
                            case "IdDoc":
                                row[0] = text;
                                break;
                            case "Name":
                                row[2] = text;
                                row[3] = "";
                                mc.addRow(row);
                                break;
                            default:
                                break;
                        }
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.e("jop","Ошибка парсинга SOAP " + e.toString());
            return defaultList();
        }
        return mc;
    }

    Cursor GetAvaibleAppointments(String action) {
        //SharedPreferences settings = activity.getDefaultSharedPreferences(this);
        //String specID = settings.getString("GetSpesialityList", "0");
        String orgID = settings.getString("GetLPUList", "27");
        String idPat = settings.getString("CheckPatient", "452528");
        String idDoc = settings.getString("GetDoctorList", "9");
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:GetAvaibleAppointments>\n" +
                "         <tem:idDoc>"+idDoc+"</tem:idDoc>\n" +
                "         <tem:idLpu>"+orgID+"</tem:idLpu>\n" +
                //"         <tem:idPat>"+idPat+"</tem:idPat>\n" +
                "         <tem:visitStart>2018-01-01</tem:visitStart>\n" +
                "         <tem:visitEnd>2018-12-31</tem:visitEnd>\n" +
                "         <tem:guid>"+GUID+"</tem:guid>\n" +
                "      </tem:GetAvaibleAppointments>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
                //"         <tem:visitStart>2017-09-b01</tem:visitStart>\n" +
                //"         <tem:visitEnd>2017-12-31</tem:visitEnd>\n" +

        XmlPullParser myParser = readSOAP(query, action);

        int event;
        String text = "";
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        int i=0;
                        switch (name) {
                            case "IdAppointment":
                                //row[0] = ++i;
                                row[0] = text;
                                //row[3] = text;
                                break;
                            case "VisitEnd":
                                int loc = text.indexOf("T");
                                String date=text.substring(0,loc);

                                String time = text.substring(loc+1);
                                loc=time.lastIndexOf(":");
                                time=time.substring(0,loc);
                                row[1] = date;
                                //row[1] = d[2]+"."+d[1]+"."+d[0];
                                //row[3] = time;
                                row[3] = "";
                                break;
                            case "VisitStart":
                                loc = text.indexOf("T");
                                date=text.substring(0,loc);
                                time = text.substring(loc+1);
                                loc=time.lastIndexOf(":");
                                time=time.substring(0,loc);
                                //row[1] = date;
                                row[2] = time;

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date dd = sdf.parse(date);

                                sdf = new SimpleDateFormat("E, d MMM");
                                row[1]=sdf.format(dd);
                                if (new Date().before(dd)) mc.addRow(row);

                                break;
                            default:
                                break;
                        }
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.e("jop","Ошибка парсинга SOAP " + e.toString());
            return defaultList();
        }
        return mc;
    }

    Cursor CreateClaimForRefusal(String action) {
        //SharedPreferences settings = activity.getDefaultSharedPreferences(this);
        //String specID = settings.getString("GetSpesialityList", "0");
        String orgID = settings.getString("GetLPUList", "0");
        String idPat = settings.getString("CheckPatient", "4356");
        String idAppoint = settings.getString("GetPatientHistory", "0496e917-d422-42bc-a20c-3e58bb99e1ed");
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:CreateClaimForRefusal>\n" +
                "         <tem:idLpu>"+orgID+"</tem:idLpu>\n" +
                "         <tem:idPat>"+idPat+"</tem:idPat>\n" +
                "         <tem:idAppointment>"+idAppoint+"</tem:idAppointment>\n" +
                "         <tem:guid>"+GUID+"</tem:guid>\n" +
                "      </tem:CreateClaimForRefusal>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        XmlPullParser myParser = readSOAP(query, action);

        //String[] from = {"_ID", "column1", "column2", "column3"};
        int event;
        String text = "";
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        int i=0;
                        switch (name) {
                            case "ErrorList":
                                row[0] = "0";
                                row[1] = text;
                                break;
                            case "Success":
                                row[3] = idAppoint;
                                if (text.contains("true")) row[2] = "Талон отменен";
                                else row[2]="Не отменен";
                                mc.addRow(row);
                                break;
                            default:
                                break;
                        }
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.e("jop","Ошибка парсинга SOAP " + e.toString());
            return defaultList();
        }
        return mc;
    }

    Cursor SetAppointment(String action) {
        //SharedPreferences settings = activity.getDefaultSharedPreferences(this);
        //String specID = settings.getString("GetSpesialityList", "0");
        String error="";
        boolean success=false;
        String orgID = settings.getString("GetLPUList", "0");
        String idPat = settings.getString("CheckPatient", "4356");
        String idAppoint = settings.getString("GetAvaibleAppointments", "0496e917-d422-42bc-a20c-3e58bb99e1ed");
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:SetAppointment>\n" +
                "         <tem:idAppointment>"+idAppoint+"</tem:idAppointment>\n" +
                "         <tem:idLpu>"+orgID+"</tem:idLpu>\n" +
                "         <tem:idPat>"+idPat+"</tem:idPat>\n" +
                "         <tem:guid>"+GUID+"</tem:guid>\n" +
                "      </tem:SetAppointment>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        XmlPullParser myParser = readSOAP(query, action);

        //String[] from = {"_ID", "column1", "column2", "column3"};
        int event;
        String text = "";
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                row[0] = "";
                row[1] = "";
                row[2] = "";
                row[3] = "";
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        int i=0;
                        switch (name) {
                            case "ErrorDescription":
                                error=text;
                                break;
                            case "Success":
                                success = text.contains("true");
                                break;
                            case "Type":
                                break;
                            case "IdAppointment":
                                break;
                            case "VisitStart":
                                /*
                                int loc = text.indexOf("T");
                                String date=text.substring(0,loc);
                                String time = text.substring(loc+1);
                                loc=time.lastIndexOf(":");
                                time=time.substring(0,loc);
                                row[1] = date;
                                row[2] = time;
                                mc.addRow(row);
                                */
                                break;
                            default:
                                break;
                        }
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.e("jop","Ошибка парсинга SOAP " + e.toString());
            return defaultList();
        }
        row[0]=idAppoint;
        row[1]=idAppoint;
        row[2]= success ? "Талончик отложен" : "Ошибка";
        row[3]= success ? "" : error;
        mc.addRow(row);
        return mc;
    }

    Cursor SearchTop10Patient(String action) {
        //SharedPreferences settings = activity.getDefaultSharedPreferences(this);
        String lpuid = settings.getString("GetLPUList", "174");
        //String pbirstdate = settings.getString("Birstdate", "1966-09-03");
        String psurname = settings.getString("Surname", "Иванов");
        //String pname = settings.getString("Name", "Дмитрий");
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:hub=\"http://schemas.datacontract.org/2004/07/HubService2\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:SearchTop10Patient>\n" +
                "         <tem:pat>\n" +
                "            <hub:Surname>"+psurname+"</hub:Surname>\n" +
                "         </tem:pat>\n" +
                "         <tem:idLpu>"+lpuid+"</tem:idLpu>\n" +
                "         <tem:guid>"+GUID+"</tem:guid>\n" +
                "      </tem:SearchTop10Patient>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        XmlPullParser myParser = readSOAP(query, action);

        String[] from = {"_ID", "AriaNumber","Birthday", "CellPhone", "Document_N","Document_S","HomePhone","IdPat","Name","Polis_N","Polis_S","SecondName","Surname"};
        int event;
        String text = null;
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
/*
                  <a:AriaNumber>19УЧ</a:AriaNumber>
                  <a:Birthday>1988-07-31T00:00:00</a:Birthday>
                  <a:CellPhone>9119946526</a:CellPhone>
                  <a:Document_N>0243776</a:Document_N>
                  <a:Document_S>ВУ</a:Document_S>
                  <a:HomePhone i:nil="true"/>
                  <a:IdPat>449459</a:IdPat>
                  <a:Name>Александр</a:Name>
                  <a:Polis_N>7852110818004716</a:Polis_N>
                  <a:Polis_S>ЕП</a:Polis_S>
                  <a:SecondName>Дмитриевич</a:SecondName>
                  <a:Surname>Пархимович</a:Surname>
 */
                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "AriaNumber":
                                if(text==null) row[3] = "нет участка"; else row[3] = text;
                                //row[1] = text;
                                break;
                            case "Birthday":
                                if(text!=null) row[1] = text.split("T")[0];
                                break;
                            case "CellPhone":
                                //row[3] = text;
                                break;
                            case "Document_N":
                                row[4] = text;
                                break;
                            case "Document_S":
                                row[5] = text;
                                break;
                            case "HomePhone":
                                row[6] = text;
                                break;
                            case "IdPat":
                                row[0] = text;
                                //row[1] += ", "+text;

                                break;
                            case "Name":
                                row[2] = text;
                                break;
                            case "Polis_N":
                                row[9] = text;
                                break;
                            case "Polis_S":
                                row[10] = text;
                                break;
                            case "SecondName":
                                row[2] +=" "+text;
                                break;
                            case "Surname":
                                row[2] +=" "+text;
                                Log.d(TAG, row[0]+" "+row[1]+" "+row[2]+" "+row[3] );
                                mc.addRow(row);
                                break;
                            default:
                                break;
                        }
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.e("jop","Ошибка парсинга SOAP " + e.toString());
            return defaultList();
        }
        return mc;
    }

    Cursor GetPatientHistory(String action) {
        String orgID = settings.getString("GetLPUList", "175");
        String idPat = settings.getString("CheckPatient", "22");
        //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " история пациента " + idPat + " в поликлинике " + orgID);

        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <tem:GetPatientHistory>" +
                "         <tem:idLpu>"+orgID+"</tem:idLpu>" +
                "         <tem:idPat>"+idPat+"</tem:idPat>" +
                "         <tem:guid>"+GUID+"</tem:guid>" +
                "      </tem:GetPatientHistory>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>";

        XmlPullParser myParser = readSOAP(query, action);

        //String[] from = {"_ID", "column1", "column2", "column3"};
        int event;
        String text = null;
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        //mc.addRow(row);
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "Name":
                                row[1] = text;
                                break;
                            case "IdAppointment":
                                row[0] = text;
                                row[4] = "Ваш талон";
                                break;
                            case "NameSpesiality":
                                //if(text!=null) row[1] = text +"\n"+ row[1];
                                row[3] = (text!=null) ? text : " ";
                                break;
                            case "VisitStart":
                                if (text!=null) {
                                    int loc = text.indexOf("T");
                                    String date=text.substring(0,loc);
                                    String time = text.substring(loc+1);
                                    loc=time.lastIndexOf(":");
                                    time=time.substring(0,loc);
                                    //row[2] = text.split("T")[0] + "\n"+ text.split("T")[1];
                                    row[1] = date;
                                    row[2] = time;
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dd = null;
                                    try {
                                        dd = sdf.parse(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    sdf = new SimpleDateFormat("E, d MMM");
                                    row[1]=sdf.format(dd);
                                    //if (new Date().before(dd)) mc.addRow(row);
                                }

                                //row[4]="Предстоящий визит";
                                mc.addRow(row);
                                break;
                            default:
                                break;
                        }
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.d("jop","Ошибка парсинга SOAP " + e.toString());
            return defaultList();
        }
        return mc;
    }

    Cursor GetWorkingTime(String action) {
        //SharedPreferences settings = activity.getDefaultSharedPreferences(this);
        //String specID = settings.getString("GetSpesialityList", "0");
        String orgID = settings.getString("GetLPUList", "19");
        String idDoc = settings.getString("GetDoctorList", "10");
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:GetWorkingTime>\n" +
                "         <tem:idDoc>"+idDoc+"</tem:idDoc>\n" +
                "         <tem:idLpu>"+orgID+"</tem:idLpu>\n" +
                "         <tem:visitStart>2018-01-01</tem:visitStart>\n" +
                "         <tem:visitEnd>2018-12-31</tem:visitEnd>\n" +
                "         <tem:guid>"+GUID+"</tem:guid>\n" +
                "      </tem:GetWorkingTime>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        //"         <tem:visitStart>2017-09-b01</tem:visitStart>\n" +
        //"         <tem:visitEnd>2017-12-31</tem:visitEnd>\n" +

        XmlPullParser myParser = readSOAP(query, action);

        String[] from = {"_ID", "column1", "column2", "column3"};
        int event;
        String text = "";
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        try {
            String dat="", t0="", t11="", msg="";
            Boolean recday=false;
            event = myParser.getEventType();
            int i=0;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "DenyCause":
                                row[1] = text;
                                msg = text;
                                break;
                            case "RecordableDay":
                                row[0] = text;
                                recday=Boolean.valueOf(text);
                                break;
                            case "VisitEnd":
                                int loc = text.indexOf("T");
                                String date=text.substring(0,loc);
                                String time = text.substring(loc+1);
                                loc=time.lastIndexOf(":");
                                time=time.substring(0,loc);
                                row[0] = date;
                                row[3] = time;
                                dat = date;
                                t11=time;
                                break;
                            case "VisitStart":
                                int loc1 = text.indexOf("T");
                                String date1=text.substring(0,loc1);
                                String time1 = text.substring(loc1+1);
                                loc1=time1.lastIndexOf(":");
                                time1=time1.substring(0,loc1);
                                row[2] = time1;
                                //if (row[1].toString().length()==0)
                                t0=time1;

                                row[0]=dat;
                                row[1]=dat;
                                if (recday) row[2]=t0+".."+t11;
                                else row[2]=msg;
                                row[3]=msg;
                                mc.addRow(row);
                                break;
                            default:
                                break;
                        }
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.d("jop","Ошибка парсинга SOAP " + e.toString());
            return defaultList();
        }
        return mc;
    }

    private XmlPullParser readSOAP(String body, String action) {
        try {
            URL url = new URL("https://api.gorzdrav.spb.ru/Service/HubService.svc");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setRequestProperty("SOAPAction", "http://tempuri.org/IHubService/" + action);
            conn.setRequestProperty("Content-Length", String.valueOf(body.getBytes().length));
            conn.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
            conn.setDoOutput(true);

            //передача запроса
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            outputStream.write(body);
            outputStream.flush();
            outputStream.close();
            //Log.d("jop","Запрос= " + body.getBytes().length + " bytes, " + body);

            //чтение ответа
            conn.connect();
            String line;
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) sb.append(line);
            isr.close();
            reader.close();
            //Log.d("jop","Ответ= " + sb.length() + " bytes, " + sb);

            //препарсинг
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);// включаем поддержку namespace (по умолчанию выключена)
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(sb.toString()));
            return xpp;
        } catch (Exception e) {
            Log.d("jop","Ошибка чтения SOAP " + e.toString());
            return null;
        }
    }

    Cursor defaultList() {
        String[] from = {"_ID", "1", "2", "3"};
        MatrixCursor mc = new MatrixCursor(from);
        Object[] from1 = {1, "1", "1", "1"};
        Object[] from2 = {2, "2", "2", "2"};
        Object[] from3 = {3, "3", "3", "3"};
        mc.addRow(from1);
        mc.addRow(from2);
        mc.addRow(from3);
        return mc;
    }

    Cursor CheckPatient(String action) {
        //Log.d(TAG, "==========Это CheckPatient! =========");
        String idpat = "";
        String err = "Пациента нет в базе регистратуры. Проверьте \"Меню\" -> \"Пациент\", или посетите регистратуру лично.";
        String birst = settings.getString("Birthday", "1966-09-03");
        String lpu = settings.getString("GetLPUList", "174");
        String nam = settings.getString("Name", "Дмитрий");
        String sur = settings.getString("Surname", "Пархимович");
        String second = settings.getString("SecondName", "Леонидович");
        String query="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:hub=\"http://schemas.datacontract.org/2004/07/HubService2\">\n" +
                "<soapenv:Header/>\n" +
                "<soapenv:Body>\n" +
                "<tem:CheckPatient>\n" +
                "<tem:pat>\n" +
                "<hub:Birthday>"+birst+"</hub:Birthday>\n" +
                "<hub:Name>"+nam+"</hub:Name>\n" +
                "<hub:SecondName>"+second+"</hub:SecondName>\n" +
                "<hub:Surname>"+sur+"</hub:Surname>\n" +
                "</tem:pat>\n" +
                "<tem:idLpu>"+lpu+"</tem:idLpu>\n" +
                "<tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
                "</tem:CheckPatient>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>";

        //Log.d(TAG, query);

        XmlPullParser myParser = readSOAP(query, "CheckPatient");

        //String[] from = {"_ID", "column1", "column2", "column3", "column4"};
        int event;
        String text = null;
        MatrixCursor mc = new MatrixCursor(from);
        Object[] row = new Object[from.length];
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                row[0]="";
                row[1]="";
                row[2]="";
                row[3]="";

                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "ErrorList":
                                row[1] = text;
                                //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName()+" "+ name + " " + text);
                                break;
                            case "IdHistory":
                                row[1] = text;
                                //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName()+" "+ name + " " + text);
                                break;
                            case "Success":
                                row[1] = text;
                                //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName()+" "+ name + " " + text);
                                break;
                            case "IdPat":
                                row[0] = text;
                                idpat = text;
                                break;
                            default:
                                break;
                        }
                        text = null;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.d("jop","Ошибка парсинга SOAP " + e.toString());
        }
        //idpat=mc.getString(0);
        //String[] head = {"_ID", "1", "2", "3"};
        //MatrixCursor mc = new MatrixCursor(head);
        //Object[] row1 = {idpat, lpu + " " + sur + " " + birst, lpu, second};
        //row[1] = nam + " " + second + " " + sur;
        row[2] = nam + " " + second + " " + sur;;
        row[3] = idpat!=null ? idpat : err ;
        row[0] = idpat!=null ? idpat : "" ;
        mc.addRow(row);

        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName()+" lpu="+ lpu + " sur=" + sur + " id=" + idpat);

        return mc;
    }
}
