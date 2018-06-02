package ru.mobiskif.zdrav;

import android.content.Context;
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
import java.util.Observable;

import javax.net.ssl.HttpsURLConnection;

class HubService extends Observable {
    SharedPreferences settings;
    String TAG;
    String defpat="";

    public HubService(Context c) {
        SharedPreferences defsettings = PreferenceManager.getDefaultSharedPreferences(c);
        String currentPatient = defsettings.getString("currentUser", "0");
        defpat=currentPatient;
        settings = c.getSharedPreferences(currentPatient,0);
        TAG=this.getClass().getSimpleName()+" jop";
    }

    public HubService() {
        //settings = getDefaultSharedPreferences(c);
        TAG=this.getClass().getSimpleName()+" jop";
    }


    Cursor GetDistrictList(String action) {
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:GetDistrictList>\n" +
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
                "      </tem:GetDistrictList>\n" +
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
                            case "DistrictName":
                                row[1] = text;
                                row[2] = "район";
                                break;
                            case "IdDistrict":
                                row[0] = text;
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
        //SharedPreferences settings = context.getDefaultSharedPreferences(this);
        String districtID = settings.getString("GetDistrictList", "1");
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:GetOrgList>\n" +
                "         <tem:IdDistrict>" + districtID + "</tem:IdDistrict>\n" +
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
                "      </tem:GetOrgList>\n" +
                "   </soapenv:Body>\n" +
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
                            String hubid=null;
                            switch (name) {
                                case "Chief":
                                    row[3] = text;
                                    break;
                                case "Contact":
                                    row[0] = text;
                                    break;
                                case "Distric":
                                    break;
                                case "EMail":
                                    break;
                                case "Hub_ID":
                                    break;
                                case "ID":
                                    break;
                                case "Org_Address":
                                    row[2] = text;
                                    break;
                                case "Org_Name":
                                    row[1] = text;
                                    break;
                                case "Org_Type":
                                    hubid = text;
                                    row[3]+=" "+ hubid;
                                    if (row[2] != null && !hubid.contains("Аптеки")) mc.addRow(row);
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
        //Log.d("jop", "========="+action);
        //SharedPreferences settings = context.getDefaultSharedPreferences(this);
        String districtID = settings.getString("GetDistrictList", "17");
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <tem:GetLPUList>" +
                "         <tem:IdDistrict>" + districtID + "</tem:IdDistrict>" +
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
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
                                    row[1] = text;
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
        //SharedPreferences settings = context.getDefaultSharedPreferences(this);
        String orgID = settings.getString("GetLPUList", "27");
        String idPat = settings.getString("CheckPatient", "452528");

        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:GetSpesialityList>\n" +
                "         <tem:idLpu>"+orgID+"</tem:idLpu>\n" +
                "         <tem:idPat>"+idPat+"</tem:idPat>\n" +
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
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
                                row[2] = text;
                                break;
                            case "IdSpesiality":
                                row[0] = text;
                                break;
                            case "NameSpesiality":
                                row[1] = text;
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
        //SharedPreferences settings = context.getDefaultSharedPreferences(this);
        String specID = settings.getString("GetSpesialityList", "19");
        String orgID = settings.getString("GetLPUList", "27");
        String idPat = settings.getString("CheckPatient", "452528");

        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:GetDoctorList>\n" +
                "         <tem:idSpesiality>"+specID+"</tem:idSpesiality>\n" +
                "         <tem:idLpu>"+orgID+"</tem:idLpu>\n" +
                "         <tem:idPat>"+idPat+"</tem:idPat>\n" +
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
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
                                row[3] = text;
                                break;
                            case "CountFreeParticipantIE":
                                row[2] = text;
                                break;
                            case "IdDoc":
                                row[0] = text;
                                break;
                            case "Name":
                                row[1] = text;
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
        //SharedPreferences settings = context.getDefaultSharedPreferences(this);
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
                "         <tem:idPat>"+idPat+"</tem:idPat>\n" +
                "         <tem:visitStart>2018-01-01</tem:visitStart>\n" +
                "         <tem:visitEnd>2018-12-31</tem:visitEnd>\n" +
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
                "      </tem:GetAvaibleAppointments>\n" +
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
                                row[3] = text;
                                break;
                            case "VisitStart":
                                int loc = text.indexOf("T");
                                String date=text.substring(0,loc);
                                String time = text.substring(loc+1);
                                loc=time.lastIndexOf(":");
                                time=time.substring(0,loc);
                                row[1] = date;
                                row[2] = time;
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

    Cursor CreateClaimForRefusal(String action) {
        //SharedPreferences settings = context.getDefaultSharedPreferences(this);
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
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
                "      </tem:CreateClaimForRefusal>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        XmlPullParser myParser = readSOAP(query, action);

        String[] from = {"_ID", "column1", "column2", "column3"};
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
                                row[0] = ++i;
                                row[3] = text;
                                break;
                            case "VisitStart":
                                int loc = text.indexOf("T");
                                String date=text.substring(0,loc);
                                String time = text.substring(loc+1);
                                loc=time.lastIndexOf(":");
                                time=time.substring(0,loc);
                                row[1] = date;
                                row[2] = time;
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
        //SharedPreferences settings = context.getDefaultSharedPreferences(this);
        //String specID = settings.getString("GetSpesialityList", "0");
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
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
                "      </tem:SetAppointment>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        XmlPullParser myParser = readSOAP(query, action);

        String[] from = {"_ID", "column1", "column2", "column3"};
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
                                row[0] = ++i;
                                row[3] = text;
                                break;
                            case "VisitStart":
                                int loc = text.indexOf("T");
                                String date=text.substring(0,loc);
                                String time = text.substring(loc+1);
                                loc=time.lastIndexOf(":");
                                time=time.substring(0,loc);
                                row[1] = date;
                                row[2] = time;
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

    Cursor SearchTop10Patient(String action) {
        //SharedPreferences settings = context.getDefaultSharedPreferences(this);
        String orgID = settings.getString("GetLPUList", "174");
        //String pbirstdate = settings.getString("birstdate", "1966-09-03");
        String psurname = settings.getString("Surname", "Иванов");
        //String pname = settings.getString("name", "Дмитрий");
        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:hub=\"http://schemas.datacontract.org/2004/07/HubService2\">" +
                "<soapenv:Header/>\n<soapenv:Body><tem:SearchTop10Patient><tem:pat>\n" +
                //"<hub:Birthday>"+pbirstdate+"</hub:Birthday>\n" +
                //"<hub:Name>"+pname+"</hub:Name>\n" +
                "<hub:Surname>"+psurname+"</hub:Surname></tem:pat>\n" +
                "<tem:idLpu>"+orgID+"</tem:idLpu>\n" +
                "<tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
                "</tem:SearchTop10Patient></soapenv:Body></soapenv:Envelope>\n";

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
                                if(text==null) row[1] = "нет участка"; else row[1] = text;
                                //row[1] = text;
                                break;
                            case "Birthday":
                                if(text!=null) row[2] = text.split("T")[0];
                                break;
                            case "CellPhone":
                                row[3] = text;
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
                                row[1] += ", "+text;
                                break;
                            case "Name":
                                row[8] = text;
                                break;
                            case "Polis_N":
                                row[9] = text;
                                break;
                            case "Polis_S":
                                row[10] = text;
                                break;
                            case "SecondName":
                                row[11] = text;
                                break;
                            case "Surname":
                                row[12] = text;
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
        //SharedPreferences settings = context.getDefaultSharedPreferences(this);
        String sss = defpat;

        String orgID = settings.getString("GetLPUList", "175");
        //String idPat = settings.getString("SearchTop10Patient", "4326");
        //String idPat = settings.getString("idPat", "4326");
        String idPat = settings.getString("CheckPatient", "22");
        //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " история пациента " + idPat + " в поликлинике " + orgID);

        String query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <tem:GetPatientHistory>" +
                "         <tem:idLpu>"+orgID+"</tem:idLpu>" +
                "         <tem:idPat>"+idPat+"</tem:idPat>" +
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                "      </tem:GetPatientHistory>" +
                "   </soapenv:Body>" +
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
                            case "Name":
                                row[1] = text;
                                break;
                            case "IdAppointment":
                                row[0] = text;
                                break;
                            case "NameSpesiality":
                                //if(text!=null) row[1] = text +"\n"+ row[1];
                                if(text!=null) row[1] = text + "\n"+row[1];
                                break;
                            case "VisitStart":
                                if (text!=null) {
                                    int loc = text.indexOf("T");
                                    String date=text.substring(0,loc);
                                    String time = text.substring(loc+1);
                                    loc=time.lastIndexOf(":");
                                    time=time.substring(0,loc);
                                    //row[2] = text.split("T")[0] + "\n"+ text.split("T")[1];
                                    row[2] = date + " " + time;
                                    //row[3] = time;
                                }


                                //row[2]=text;
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
        //SharedPreferences settings = context.getDefaultSharedPreferences(this);
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
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
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
            //Log.e("jop","Запрос= " + body.getBytes().length + " bytes, " + body);

            //чтение ответа
            conn.connect();
            String line;
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) sb.append(line);
            isr.close();
            reader.close();
            Log.e("jop","Ответ= " + sb.length() + " bytes, " + sb);

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

    public Cursor defaultList() {
        String[] from = {"_ID", "1", "2", "3"};
        MatrixCursor mc = new MatrixCursor(from);
        Object[] from1 = {11, "11", "111", "1111"};
        Object[] from2 = {22, "qqq 2222222222222 2222222222 444444444 22222222222 www", "222", "2222"};
        Object[] from3 = {33, "33", "333", "3333"};
        mc.addRow(from1);
        mc.addRow(from2);
        mc.addRow(from3);
        return mc;
    }

    public Cursor CheckPatient(String action) {
        Log.d(TAG, "==========Это CheckPatient! =========");
        String idpat = "Указанный пациент не обнаружен в базе регистратуры. Проверьте правильность, или посетите регистратуру лично.";
        String birst = settings.getString("Birstdate", "1966-09-03");
        String lpu = settings.getString("GetLPUList", "17");
        String nam = settings.getString("Name", "Дмитрий");
        String sur = settings.getString("Surname", "Пархимович");
        String second = settings.getString("Secondname", "Леонидович");
        String query="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:hub=\"http://schemas.datacontract.org/2004/07/HubService2\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <tem:CheckPatient>" +
                "         <tem:pat>" +
                "           <hub:Birthday>"+birst+"</hub:Birthday>" +
                "            <hub:Name>"+nam+"</hub:Name>" +
                "            <hub:SecondName>"+second+"</hub:SecondName>" +
                "            <hub:Surname>"+sur+"</hub:Surname>" +
                "         </tem:pat>" +
                "         <tem:idLpu>"+lpu+"</tem:idLpu>" +
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                "     </tem:CheckPatient>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>";

        //Log.d(TAG, query);

        XmlPullParser myParser = readSOAP(query, "CheckPatient");

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
                            case "Success":
                                row[1] = text;
                                //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName()+" "+ name + " " + text);
                                break;
                            case "IdPat":
                                row[0] = text;
                                //mc.addRow(row);
                                //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName()+" "+ name + " " + text);
                                idpat = row[0].toString();
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
        row[1] = nam + " " + second + " " + sur;
        row[2] = birst;
        row[3] = idpat;
        mc.addRow(row);

        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName()+" lpu="+ lpu + " sur=" + sur + " id=" + idpat);

        return mc;
    }
}
