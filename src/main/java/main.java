import java.io.File;

public class main {
    public static void main(String args[]){

        String SEARCH_STRING = "RAVANA PDF";
        String DOWNLOAD_LOCATION="DOWNLOADED_CONTENT";

        //change this if you want to keep the downloaded content
        boolean keep_files=false;



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
        download_from_google google = new download_from_google();
        google.search_and_download(SEARCH_STRING,DOWNLOAD_LOCATION);

        //UPLOAD THE DOWNLOADED FILES INTO WATSON
        upload up = new upload();
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
