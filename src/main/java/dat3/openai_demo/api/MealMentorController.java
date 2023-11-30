package dat3.openai_demo.api;

import dat3.openai_demo.dtos.MyResponse;
import dat3.openai_demo.dtos.UserPromptResponse;
import dat3.openai_demo.dtos.UserResponse;
import dat3.openai_demo.entity.User;
import dat3.openai_demo.service.OpenAiService;
import dat3.openai_demo.service.UserService;
import dat3.security.service.UserDetailsServiceImp;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/mealPlanGenerator")
@CrossOrigin
public class MealMentorController {

  private OpenAiService openAiService;
  private UserService userService;
  String SYSTEM_MESSAGE = getSystemMessage();

  public MealMentorController(OpenAiService openAiService, UserService userService) {
    this.openAiService = openAiService;
    this.userService = userService;
  }



    @PostMapping()
      public MyResponse generatePrompt(@RequestBody UserPromptResponse userPromptResponse){
       /* try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error during delay");
        } */

            // Check if the user has credits
        if (userService.getUser(userPromptResponse.getUsername()).getCredits() <= 0) {

            return new MyResponse("{\n" +
                    "    \"Message\": \"USER HAS NO CREDITS\"\n" +
                    "}");

        } else {

            // Removing one credit from the user's credits
            String usernamePrompting = userPromptResponse.getUsername();
            UserResponse user = userService.getUser(usernamePrompting);
            userService.removeOneCreditFromUser(usernamePrompting);

            // Printing how many cr
            System.out.println("****** " + user.getUsername() + " is PROMPTING *******\n" +
                    "****** and he/she has got " +
                    userService.getUser(usernamePrompting).getCredits()
                    + " credits left *****");

      String userPrompt = "I am a " + user.getAge() + " old "
              + user.getSex()
              + " and" + " my activity level is: " + user.getActivityLevel()
              + ". The mealplan must not include: " + user.getAllergies()
              + ". My goals are: " + user.getGoals() + "i want the mealplan to only include" + userPromptResponse.getMealChecklist();


            return openAiService.makeRequest(userPrompt, SYSTEM_MESSAGE);
        }



  }

  private String getSystemMessage() {

      final String string =
              "You are an AI assistant whose sole function is to generate structured meal plans tailored to users' dietary requirements. " +
              "When prompted, you will produce a JSON object detailing a meal plan that includes meal titles, ingredients with quantities, " +
              "and approximate calorie count for the meal. "
              +
              "Ingredients should be presented as a simple string, for example 3 eggs or 2 tbsp salt or 200g flour"
              +
              "In the \"Description\" section, provide a short explanation for the nutritional value of the dish, " +
              "and why this specific meal have been picked for the user, " +
              "in relation the user's weight, activity level, preferences, allergies and the users goals."
              +
              "Be sure to mention that the dish do not contain what the user is allergic to."
              +
              "mealType is Breakfast, Lunch or Dinner, and is written with capital starting letter"
              +
              "......"
              +
              "BELOW IS AN EXAMPLE OF YOU SHOULD STRUCTURE THE RECIPE, AND NOT SOMETHING YOU SHOULD RETURN:"
              + "{"
              + "\"mealType"
              + "  {"
              + "    \"Title\": \"title of meal\","
              + "    \"Ingredients\": ["
              + "      \"50 g xxx\","
              + "      \"150 g xxx\","
              + "      \"1 cup xxx\","
              + "      \"1/2  xxx\","
              + "      \"xxx and xxx to taste\""
              + "    ],"
              + "    \"Calories\": \"xxx kcal\","
              + "    \"Protein\": \"xxx g\","
              + "    \"Carbohydrates\": \"xxx g\","
              + "    \"Fat\": \"xxx g\","
              + "    \"Time to make\": \"xxx min\","
              + "    \"Description\": \"This xxx is xxx.\","
              + "    \"Instructions\": \"1. xxx 2. xxx 3. xxx. 4. xxx. 5. xxx. 6. Serve.\""
              + "  }"
              + "}"
              +

              "Each ingredient's quantity should be listed in metric units or amount." +
              "Include meals for Breakfast, Lunch, and/or Dinner, depending on the user input. " +
              "If the user's request lacks details for meal plan creation, " +
              "the API should respond with a JSON object asking for the necessary information. " +
              "The API's responses should consistently follow this format " +
              "and should contain only the JSON-formatted meal plan, not any regular text, and no followup questions.\n" +
              "The response should only contain meals for 1 day\n";

      return string;
  }


}
