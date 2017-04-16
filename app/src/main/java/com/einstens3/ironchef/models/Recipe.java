package com.einstens3.ironchef.models;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;


@ParseClassName("Recipe")
public class Recipe extends ParseObject {
    /**
     * Fields
     * - Name: String            <<-- unique Recipe name if user wants
     * - Standard Name: String   <<-- Standard name to grouping receipe
     * - createdAt: Date         <<-- ParseObject has createdAt and updatedAt
     * - updatedAt: Date         <<-- ParseObject has createdAt and updatedAt
     * - Author: ParseUser
     * - Description: String
     * - Ingredients: List<String>
     * - Photo: ParseFile
     * - Category - Set<String>
     * - Serving: Int
     * - Cooking time: Int (min)  - PrepTime
     * - ChallengeTo:  Recipe
     * - draft: boolean
     * - private boolean
     */
    // ------------------------------------
    //  Constants
    // ------------------------------------
    public static final String TAG = Recipe.class.getSimpleName();

    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CHALLENGES = "challenges";
    public static final String KEY_LIKES = "likes";

    // ------------------------------------
    //  Static convenient methods
    // ------------------------------------

    // ------------------------------------
    //  Constructors
    // ------------------------------------
    public Recipe() {
    }

    // ------------------------------------
    //  Getters/Setters
    // ------------------------------------

    // name: String - Recipe Name  Name could be unique

    public String getName() {
        return getString("name");
    }

    public void setName(String value) {
        put("name", value);
    }

    //  standardName: String - Standard Recipe Name for grouping same foods

    public String getStandardName() {
        return getString("standardName");
    }

    public void setStandardName(String value) {
        put("standardName", value);
    }

    // author: ParseUser - Recipe Author (owner)

    public ParseUser getAuthor() {
        return (ParseUser) getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser value) {
        put(KEY_AUTHOR, value);
    }

    // description: String - Recipe description

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String value) {
        put("description", value);
    }

    // ingredients: List<String> - list of ingredients

    public List<String> getIngredients() {
        return getList("ingredients");
    }

    public void setIngredients(List<String> value) {
        put("ingredients", value);
    }

    // photo: ParseFile - Recipe's main photo

    public ParseFile getPhoto() {
        return getParseFile("photo");
    }

    public void setPhoto(ParseFile value) {
        put("photo", value);
    }

    // categories: List<String> - List of categories that the recipe belongs to

    public List<String> getCategories() {
        return getList("categories");
    }

    public void setCategories(List<String> value) {
        put("categories", value);
    }


    // serving: long - a quantity of food suitable for how many person.

    public long getServing() {
        return getLong("serving");
    }

    public void setServing(long value) {
        put("serving", value);
    }

    // cookingTime: long - total cooking time

    public long getCookingTime() {
        return getLong("cookingTime");
    }

    public void setCookingTime(long value) {
        put("cookingTime", value);
    }

    // challengeTo: Recipe - null in case this is original recipe
    //                     - reference to original recipe

    public Recipe getChallengeTo() {
        return (Recipe) getParseObject("challengeTo");
    }

    public void setChallengeTo(Recipe value) {
        put("challengeTo", value);
    }

    // NOTE - For Sprint 1, Steps are fixed number of steps

//    class Step extends ParseObject{
//        String text;
//        ParseFile photo;
//
//    }
//    public List<Step> getSteps();

    public String getStep1Text() {
        return getString("step1text");
    }

    public void setStep1Text(String value) {
        put("step1text", value);
    }

    public ParseFile getStep1Photo() {
        return getParseFile("step1photo");
    }

    public void setStep1Photo(ParseFile value) {
        put("step1photo", value);
    }

    public String getStep2Text() {
        return getString("step2text");
    }

    public void setStep2Text(String value) {
        put("step2text", value);
    }

    public ParseFile getStep2Photo() {
        return getParseFile("step2photo");
    }

    public void setStep2Photo(ParseFile value) {
        put("step2photo", value);
    }

    public String getStep3Text() {
        return getString("step3text");
    }

    public void setStep3Text(String value) {
        put("step3text", value);
    }

    public ParseFile getStep3Photo() {
        return getParseFile("step3photo");
    }

    public void setStep3Photo(ParseFile value) {
        put("step3photo", value);
    }

    public boolean isDraft() {
        return getBoolean("draft");
    }

    public void setDraft(boolean value) {
        put("draft", value);
    }


    public boolean isPublic() {
        return getBoolean("public");
    }

    public void setPublic(boolean value) {
        put("public", value);
    }

    // ----------------------------------------------------------------
    // Like related features
    // ----------------------------------------------------------------

    public void like() {
        addLike(ParseUser.getCurrentUser());
    }

    public void unlike() {
        addLike(ParseUser.getCurrentUser());
    }

    public void countLikes(CountCallback callback) {
        getLikesRelation().getQuery().countInBackground(callback);
    }

    public ParseRelation<ParseUser> getLikesRelation() {
        return getRelation(KEY_LIKES);
    }

    public void addLike(ParseUser value) {
        getLikesRelation().add(value);
        saveInBackground();
    }

    public void removeLike(ParseUser value) {
        getLikesRelation().remove(value);
        saveInBackground();
    }

    // ----------------------------------------------------------------
    // Challenge related features
    // ----------------------------------------------------------------

    public ParseRelation<Challenge> getChallengesRelation(){
        return getRelation(KEY_CHALLENGES);
    }

    public void addChallenge(Challenge value){
        getChallengesRelation().add(value);
        saveInBackground();
    }

    public void removeChallenge(Challenge value){
        getChallengesRelation().remove(value);
        saveInBackground();
    }

    public void getOwnChallenge(GetCallback<Challenge> callback) {
        getChallengesRelation()
                .getQuery()
                .whereEqualTo(Challenge.KEY_USER, ParseUser.getCurrentUser())
                .getFirstInBackground(callback);

    }

}