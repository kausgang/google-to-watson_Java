import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import java.io.*;
import java.util.ArrayList;


public class download_from_google {

    private String SEARCH_ENGINE_ID , API_KEY;


    public download_from_google(String SEARCH_ENGINE_ID, String API_KEY) {

        this.API_KEY = API_KEY;
        this.SEARCH_ENGINE_ID = SEARCH_ENGINE_ID;
    }

    public void search_and_download(String SEARCH_STRING, String DOWNLOAD_LOCATION){



        //REMOVE SPACES FROM SEARCH STRING, REPLACE WITH + .. SO AS TO ALLOW GOOGLE TO SEARCH
        SEARCH_STRING=SEARCH_STRING.replace(' ','+');

        String SEARCH_URL =
                "https://www.googleapis.com/customsearch/v1?" +
                        "key="+API_KEY +
                        "&cx="+SEARCH_ENGINE_ID +
                        "&q="+SEARCH_STRING ;


        ///////check the content from google

//      BufferedReader in1 = new BufferedReader(
//              new InputStreamReader(url.openStream()));
//
//      String inputLine;
//      while ((inputLine = in1.readLine()) != null)
//          System.out.println(inputLine);
//      in1.close();




        //DEFINE ARARYLIST { URL , FILETYPE (html/pdf/doc) }
        //to identify different file types from google search result
        List<String[]> searchurl=new ArrayList<String[]>();


        //DEFIONE JSONFACTORY TO IDENTIFY URL AND FILE TYPE
        //https://www.tutorialspoint.com/jackson/jackson_streaming_api.htm
        JsonFactory jasonFactory = new JsonFactory();

        //SURROUND THE ENTIRE THING IN TRY-CATCH IN CASE THE GOOGLE URL ITSELF COULD NOT BE OPENED
        try{

            URL url = new URL(SEARCH_URL);

            //COLLECT THE CONTENT OF GOOGLE SEARCH (JSON OUTPUT) INTO A JSONPARSER
            JsonParser jsonParser =jasonFactory.createParser (new InputStreamReader((url.openStream())));


            String searchresult="";

            //WHILE THE JSONPARSER HAS TOKEN
            while(jsonParser.nextToken()!= null)
            {

                String fieldname = jsonParser.getCurrentName();

                //GOOGLE SEARCH RESULT URLS ARE SENT IN "link" TAG
                if("link".equals(fieldname)){

                    jsonParser.nextToken();
                    searchresult= jsonParser.getText();

                    //ADD ALL THE URL AS HTML FIRST AND CHANGE LATER
                    searchurl.add(new String[]{searchresult,"html"});

                }

                //CHECK FOR FILEFORMAT AND CHANGE ARRAYLIST CONTENT
                if("fileFormat".equals(fieldname))
                {

                    jsonParser.nextToken();
                    String a=jsonParser.getText();
                    if("PDF/Adobe Acrobat".equals(a))
                    {

                        //CHECK IF CONTENT IS PDF ; IF YES THEN REPLACE THE LAST ITEM WITH PDF
                        searchurl.set((searchurl.size()-1),(new String[]{searchresult,"pdf"}));
                    }
                    if("Microsoft Word".equals(a))
                    {

                        //CHECK IF CONTENT IS doc ; IF YES THEN REPLACE THE LAST ITEM WITH doc
                        searchurl.set((searchurl.size()-1),(new String[]{searchresult,"doc"}));
                    }

                }

            }


            List<String> filedelete=new ArrayList<String>();




            //FOR EACH ITEMS IN THE GOOGLE SEARCH RESULT (FIRST 10 URL)
            for (int i=0;i<searchurl.size();i++)
            {

                //DETERMINE PROPER FILENAME AND FILETYPE
                String filetype=searchurl.get(i)[1];
                String file_separator=System.getProperty("file.separator");

                String filename = DOWNLOAD_LOCATION +
                                    file_separator +
                                    SEARCH_STRING +
                                    "_" +
                                    i +
                                    "." +
                                    filetype;



                try
                {

                    //BROWSE THE URL CONTENT
                    InputStream in =(new URL(searchurl.get(i)[0].toString())).openStream();

                    //WRITE TO FILE
                    OutputStream fos = new FileOutputStream(filename);
                    int length = -1;
                    byte[] buffer = new byte[1024];// buffer for portion of data from connection
                    while ((length = in.read(buffer)) > -1)
                    {
                        fos.write(buffer, 0, length);
                    }
                    fos.close();
                    in.close();
                }
                catch(Exception e)
                {
                    //IF THERE IS EXCEPTION READING FROM THE URL, SAVE THE FILE NAME FOR DELETION
                    System.out.println(filename+" marked for deletion");
                    filedelete.add(filename);
                }

            }



            //delete errored out files
            for(int j=0;j<filedelete.size();j++){
                File file=new File(filedelete.get(j).toString());
                file.delete();
            }


        }
        catch(Exception e){

        }

    }


}
