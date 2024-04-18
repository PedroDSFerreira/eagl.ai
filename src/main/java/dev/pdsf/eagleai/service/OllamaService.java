package dev.pdsf.eagleai.service;

import dev.pdsf.eagleai.exception.OllamaParseResponseException;
import dev.pdsf.eagleai.model.Description;
import dev.pdsf.eagleai.model.types.*;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

@Service
public class OllamaService {
    private final String promptMesage = """
             Describe the person (not real) in the image, according to the following parameters:
             - age: CHILD/ADOLESCENT/YOUNG_ADULT/ADULT/ELDER
              - sex: MAN/FEMALE
              - skinTone: DARK/MEDIUM/LIGHT
              - eyeColor: BROWN/GREEN/BLUE
              - hairType: LONG/SHORT/MEDIUM/NONE
              - hairColor: BLACK/BROWN/GRAY/WHITE/BLONDE/GINGER/PAINTED/NONE
              - facialHair: TRUE/FALSE

             The response should have all the fields mentioned, and only the corresponding types.
             Guess if are uncertain of the parameter type.
             If more than one person is detected in the image, throw an error.
             If something goes wrong, ONLY return 'error: ' plus the error message
            """;

    @Autowired
    private final OllamaChatClient ollamaClient;

    public OllamaService(OllamaChatClient ollamaClient) {
        this.ollamaClient = ollamaClient;
    }

    public String getResponse(byte[] imageData) {
        UserMessage userMessage = new UserMessage(promptMesage,
                List.of(new Media(MimeTypeUtils.IMAGE_JPEG, imageData)));
        System.out.println("Calling Ollama...");
        ChatResponse response = ollamaClient.call(new Prompt(userMessage));
        return response.getResult().getOutput().getContent();
    }

    public Description parseResponse(String response) {
        HashMap<String, String> params = new HashMap<String, String>();

        BufferedReader bufReader = new BufferedReader(new StringReader(response));
        String line = null;
        Description description;
        try {
            while ((line = bufReader.readLine()) != null) {
                String[] param = line.split(":");
                if (param.length == 2) {
                    String key = param[0].strip();
                    String value = param[1].strip().toUpperCase();
                    params.put(key, value);
                }
            }
            if (params.containsKey("error")) {
                throw new OllamaParseResponseException(params.get("error"));
            }

            description = new Description(
                    Age.valueOf(params.get("age")),
                    Sex.valueOf(params.get("sex")),
                    SkinTone.valueOf(params.get("skinTone")),
                    EyeColor.valueOf(params.get("eyeColor")),
                    HairType.valueOf(params.get("hairType")),
                    HairColor.valueOf(params.get("hairColor")),
                    Boolean.parseBoolean(params.get("facialHair")));

        } catch (Exception e) {
            throw new OllamaParseResponseException(response);
        }

        return description;
    }
}