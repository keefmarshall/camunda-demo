package org.hmcts.camunda.poc;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * NB for this to work, you have to send the document as a process instance variable, with the value set
 * to the Base64 encoded content of the document. You use a valueInfo field on the variable to
 * define the content-type ('mimeType' - note the case is wrong in the docs) and the filename.
 *
 * Sample JSON variable definition for the POST request that starts a process:
 * 
 * http://localhost:8080/engine-rest/process-definition/key/save-and-link-document/start
 * 
 * {
 *    "variables": {
 *       "ccdid": {
 *          "value": "12345ABCDE"
 *       },
 *       "document": {
 *        	"type": "File",
 *        	"valueInfo": {
 *        		"filename": "services.pdf",
 *        		"mimeType": "application/pdf"
 *        	},
 *          "value": "JVBERi....RU9GCg==" <-- this is the base64-encoded document content
 *       }
 *     }
 *  }
 *
 */
@Component
public class SaveDocumentAction extends AbstractBaseTaskHandler {

    private final static Logger LOGGER = Logger.getLogger(SaveDocumentAction.class.getName());

    @PostConstruct
    public void init() {
        LOGGER.info("Starting SaveDocumentAction...");
        super.init("save-document");
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        // Ideally all this validation would be handled in the business process mapped inside
        // Camunda, so we can guarantee we'll be receiving the right stuff. Not sure how to
        // do that at present though, or at least not easily.
        TypedValue typedDocument = externalTask.getVariableTyped("document");
        if (typedDocument != null) {

            LOGGER.info("Document type is: " + typedDocument.getType());

            if (typedDocument.getType() == ValueType.FILE) {

                String documentLink = saveDocument((FileValue) typedDocument);

                Map<String, Object> vars = new HashMap<>();
                vars.put("documentLink", documentLink);
                externalTaskService.complete(externalTask, vars);

            }
            else { // field is present, but not a file
                handleError(externalTask, externalTaskService, "Variable 'document' must of type 'File'");
            }
        }
        else { // field is absent
            handleError(externalTask, externalTaskService, "Value required for variable 'document'");
        }

    }

    /**
     * Returns document link
     *
     * @param document
     * @return
     */
    private String saveDocument(FileValue document) {
        LOGGER.info("Saving document...");
        String filename = document.getFilename();
        String mimetype = document.getMimeType();
        LOGGER.info("Filename is " + filename + " and mimetype is " + mimetype);

        // Would save document here, instead we just return a phoney document link

        return String.valueOf(document.hashCode());
    }
}
