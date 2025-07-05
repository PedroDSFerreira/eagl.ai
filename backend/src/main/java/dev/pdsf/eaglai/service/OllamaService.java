package dev.pdsf.eaglai.service;

import dev.pdsf.eaglai.exception.OllamaParseResponseException;
import dev.pdsf.eaglai.model.Description;
import dev.pdsf.eaglai.model.types.*;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.ByteArrayResource;

@Service
public class OllamaService {
    private final String promptMesage = """
            Describe the person (not real) in the image, according to the following parameters:

            age: CHILD/ADOLESCENT/YOUNG_ADULT/ADULT/ELDER
            sex: MALE/FEMALE
            skinTone: DARK/MEDIUM/LIGHT
            eyeColor: BROWN/GREEN/BLUE
            hairType: LONG/SHORT/MEDIUM/NONE
            hairColor: BLACK/BROWN/GRAY/WHITE/BLONDE/GINGER/PAINTED/NONE
            facialHair: TRUE/FALSE

            The response should have all the fields with format "key: value" per line, and only the corresponding values available.
            Guess if are uncertain.
            If multiple people are detected in the image, throw error.
            If something goes wrong, only return 'error: ' plus error message.
            """;

    @Autowired
    private final OllamaChatModel ollamaClient;

    public OllamaService(OllamaChatModel ollamaClient) {
        this.ollamaClient = ollamaClient;
    }

    public String getResponse(byte[] imageData) {
        UserMessage userMessage = UserMessage.builder()
            .text(promptMesage)
            .media(List.of(new Media(MimeTypeUtils.IMAGE_PNG, new ByteArrayResource(imageData))))
            .build();

        ChatResponse response = ollamaClient.call(
            new Prompt(List.of(userMessage))
        );

        return response.getResult().getOutput().getText();
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

            Age age = Age.valueOf(params.get("age"));
            Sex sex = Sex.valueOf(params.get("sex"));
            SkinTone skinTone = SkinTone.valueOf(params.get("skinTone"));
            EyeColor eyeColor = EyeColor.valueOf(params.get("eyeColor"));
            HairType hairType = HairType.valueOf(params.get("hairType"));
            HairColor hairColor = HairColor.valueOf(params.get("hairColor"));
            boolean facialHair = Boolean.parseBoolean(params.get("facialHair"));

            description = new Description(
                    age,
                    sex,
                    skinTone,
                    eyeColor,
                    hairType,
                    hairColor,
                    facialHair);

        } catch (Exception e) {
            throw new OllamaParseResponseException(response);
        }

        return description;
    }
}
