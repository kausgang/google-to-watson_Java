import com.ibm.watson.developer_cloud.discovery.v1.Discovery;

import java.io.File;

public class main {

    // GOOGLE CREDENTIALS
    private static String SEARCH_ENGINE_ID="SEARCH_ENGINE_ID";
    private static String API_KEY= "API_KEY";

    // WATSON CREDENTIALS
    private static String ENVIRONMENT_ID = "ENVIRONMENT_ID";
    private static String COLLECTION_ID = "COLLECTION_ID";
    private static String USERNAME="USERNAME";
    private static String PASSWORD="PASSWORD";


    private static String SEARCH_STRING = "SEARCH_STRING";
    private static String DOWNLOAD_LOCATION="DOWNLOADED_CONTENT_FOLDER";

    //change this if you want to keep the downloaded content
    private static boolean keep_files=true;




    public static void main(String args[]){

        File folder = new File(DOWNLOAD_LOCATION);

        if(!folder.exists()){

            try{
                folder.mkdir();
            }
            catch(SecurityException se){
                System.out.println("Security Exception");
                se.printStackTrace();
            }
        }

        // DOWNLOAD GOOGLE SEARCH PAGES IN THE "DOWNLOADED_LOCATION"
        download_from_google google = new download_from_google(SEARCH_ENGINE_ID,API_KEY);
        google.search_and_download(SEARCH_STRING,DOWNLOAD_LOCATION);


        //UPLOAD THE DOWNLOADED FILES INTO WATSON
        upload up = new upload(ENVIRONMENT_ID,COLLECTION_ID,USERNAME,PASSWORD);
        File[] list_of_document = folder.listFiles();
        for(int i=0;i<list_of_document.length;i++){
            up.process_document(folder.listFiles()[1]);
        }

        //DELETE FOLDER & FILES AFTER UPLOAD IS COMPETE
        if(!keep_files){
            for(int i=0;i<list_of_document.length;i++){
                list_of_document[i].delete();
            }
            folder.delete();
        }
    }
}
