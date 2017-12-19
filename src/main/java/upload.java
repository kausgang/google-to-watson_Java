

import com.ibm.watson.developer_cloud.discovery.v1.model.AddDocumentOptions;
import com.ibm.watson.developer_cloud.http.HttpMediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentAccepted;

public class upload {

    String environmentId = "ENVIRONMENT_ID";
    String collectionId = "COLLECTION_ID";

    Discovery discovery = new Discovery("2017-11-07");
    String username="USERNAME";
    String password="PASSWORD";



    AddDocumentOptions.Builder builder = new AddDocumentOptions.Builder(environmentId,collectionId);


    ////RECEIVE FILENAME FROM CALLING FUNCTION
    void process_document(File filename) {


       try{

           InputStream is;
           is = new FileInputStream(filename);

           builder=builder.file(is);

           builder=builder.filename(filename.getName());


           //// BELOW STEP IS OPTIONAL
           /*
           //IF HTML IS UPLOADED
           builder=builder.fileContentType( HttpMediaType.TEXT_HTML);
           //IF PDF IS UPLOADED
           builder=builder.fileContentType(HttpMediaType.APPLICATION_PDF);
           //IF DOC IS UPLOADED
           builder=builder.fileContentType(HttpMediaType.APPLICATION_MS_WORD);
            */
           AddDocumentOptions final_document_option = builder.build();

           discovery.setUsernameAndPassword(username, password);

           //  OPTIONAL STEP
           //discovery.setEndPoint("https://gateway.watsonplatform.net/discovery/api/");

           discovery.addDocument(final_document_option).execute();

           //IF YOU WANT TO COLLECT THE RESPONSE FROM EXECUTE OPERATION (TO CHECK STATUS),
           // USE THE BELOW STATEMENT
//           DocumentAccepted createDocumentResponse = discovery.addDocument(final_document_option).execute();
//           System.out.println(createDocumentResponse.getStatus());

       }
       catch (Exception e){
           e.printStackTrace();
       }
   }
}
