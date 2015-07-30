package com.pnicorp.android.users;

import java.util.ArrayList;

/**
 * Created by amiller on 6/3/2015.
 * For use with Pni logging library
 */
public class UserInfo {

    public UserInfo() {
        setActivity("none");
        setAge("none");
        setGender("none");
        setHeightFt("none");
        setName("none");
        setOrientation("none");
        setWeight("none");
        setHeightFt("none");
        setLogFiles("Downloads:/");
        setEmail("amiller@pnicorp.com");
    }
    public UserInfo(UserInfo UI){

        this.setActivity(UI.getActivity());
        this.setName(UI.getName());
        this.setOrientation(UI.getOrientation());
        this.setAge(UI.getAge());
        this.setHeightFt(UI.getHeightFt());
        this.setWeight(UI.getWeight());
        this.setGender(UI.getGender());
        this.setHeightIn(UI.getHeightIn());
        this.setLastLogFile(UI.getLastLogFile());
        this.setLogFiles(UI.getLogFiles());
        this.setEmail(UI.getEmail());
        if(extraItems!=null)
        {
            for(String s : UI.getExtras())
            {
                this.addExtra(s);
            }
        }
    }
    private String Activity;
    private String Name;
    private String Orientation;
    private String Age;
    private String HeightFt;
    private String HeightIn;
    private String LogFiles;
    private String LastLogFile;
    private String Email;

    public static final String GENDER_MALE_STRING_VALUE = "Male";
    public static final String GENDER_FEMALE_STRING_VALUE = "Female";

    public String getHeightIn() {
        return HeightIn;
    }

    public void setHeightIn(String heightIn) {
        HeightIn = heightIn;
    }

    private String Weight;
    private String Gender;

    private ArrayList<String> extraItems = null;

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOrientation() {
        return Orientation;
    }

    public void setOrientation(String orientation) {
        Orientation = orientation;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getHeightFt() {
        return HeightFt;
    }

    public void setHeightFt(String heightFt) {
        HeightFt = heightFt;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getLogFiles() {
        return LogFiles;
    }

    public void setLogFiles(String logFiles) {
        LogFiles = logFiles;
    }

    public String getLastLogFile() {
        return LastLogFile;
    }

    public void setLastLogFile(String lastLogFile) {

        if(!LogFiles.contains(lastLogFile))
        {
            appendLogFiles(lastLogFile);
        }

        LastLogFile = lastLogFile;
    }
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String appendLogFiles(String newFile)
    {
        if(LogFiles.isEmpty())
        {
            LogFiles=newFile+",";
        }else{
            LogFiles += newFile+",";
        }
        return LogFiles;
    }


    public ArrayList<String> toArrayList(){
        ArrayList<String> items = new ArrayList<String>();

        items.add ( getActivity());
        items.add ( getName());
        items.add ( getOrientation());
        items.add ( getAge());
        items.add ( getHeightFt());
        items.add ( getWeight());
        items.add ( getGender());
        items.add ( getHeightIn());
        items.add ( getLastLogFile());
        items.add ( getLogFiles());
        items.add ( getEmail());
        if(extraItems!=null)
        {

            items.addAll(extraItems);
        }

        return items;
    }

    public void addExtra(String newField) throws NullPointerException
    {

            if (extraItems == null) {
                extraItems = new ArrayList<String>();
            }

            extraItems.add(newField);
    }

    public ArrayList<String> getExtras()
    {
        if(extraItems==null)
        {
            return null;
        }
        return new ArrayList<>(extraItems);
    }

    public int size()
    {
        return toArrayList().size();
    }

    public boolean copyItems(ArrayList<String> input){

        if(input == null)
        {
            return false;
        }
        input.add ( getActivity());
        input.add ( getName());
        input.add ( getOrientation());
        input.add ( getAge());
        input.add ( getHeightFt());
        input.add ( getWeight());
        input.add ( getGender());
        input.add ( getHeightIn());
        input.add ( getLastLogFile());
        input.add ( getLogFiles());
        input.add ( getEmail());
        if(extraItems!=null)
        {

            input.addAll(extraItems);
        }

        return true;
    }
}
