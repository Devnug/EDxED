package edxed.nug.devnug.edxed;

import android.media.Image;

/**
 * Created by Nug on 3/18/2015.
 */
public class ViewModel {

    private String name;
    private String title;
    private String desc;
    private String room;
    private String session;
    private String attending;
    private String twitterHandle;
    private String jobTitle;
    private String email;
    private String strand;
    private String imgString;
    private String imgString2;
    private int id;
    private int img;
    private int img2 = -1;
    private int lastUpdate = 1;

    public ViewModel() {
        name = "";
        title = "";
        desc = "";
        room = "";
        img = determineImg();
    }

    public ViewModel(String theName, String theTitle, String theDesc, String theRoom, String theAttending, String theSession) {
        name = theName;
        title = theTitle;
        desc = theDesc;
        room = theRoom;
        session = theSession;
        attending = theAttending;
        // Test with phil...for now...
        img = determineImg();
        if(name.indexOf("&") != -1) {
            img2 = determineImg(name.substring(name.indexOf("&") + 1).trim());
        }
    }

    public ViewModel(String theName, String theJobTitle, String theTwitterHandle, String theEmail) {
        name = theName;
        jobTitle = theJobTitle;
        twitterHandle = theTwitterHandle;
        email = theEmail;
        // Test with phil...for now...
        img = determineImg();

    }

    public ViewModel(String theName, String newTitle, String newDesc, String newRoom, String newSession, int update, int theId, String theImgString, String theImgString2) {
        name = theName;
        title = newTitle;
        desc = newDesc;
        room = newRoom;
        session = newSession;
        lastUpdate = update;
        id = theId;
        attending = "false";
        imgString = theImgString;
        imgString2 = theImgString;
        // Test with phil...for now...
        img = determineImg();
        if(name.indexOf("&") != -1 && name.toLowerCase().indexOf("goulding") == -1) {
            img2 = determineImg(name.substring(name.indexOf("&") + 1).trim());
        }
    }

    public ViewModel(String theName, String newTitle, String newDesc, String newRoom, String theAttending, String newSession, int update, int theId, String theImgString, String theImgString2) {
        name = theName;
        title = newTitle;
        desc = newDesc;
        room = newRoom;
        session = newSession;
        lastUpdate = update;
        id = theId;
        attending = theAttending;
        imgString = theImgString;
        imgString2 = theImgString2;
        // Test with phil...for now...
        img = determineImg();
        if(name.indexOf("&") != -1 && name.toLowerCase().indexOf("goulding") == -1) {
            img2 = determineImg(name.substring(name.indexOf("&") + 1).trim());
        }
    }

    public int determineImg() {
        if(name.toLowerCase().indexOf("anne taliaferro") != -1)
            return R.drawable.anne;
        if(name.toLowerCase().indexOf("christopher purcell") != -1 || name.toLowerCase().indexOf("chris purcell") != -1)
            return R.drawable.christopher;
        if(name.toLowerCase().indexOf("gina angelillo") != -1)
            return R.drawable.gina;
        if(name.toLowerCase().indexOf("halley anne curtis") != -1)
            return R.drawable.halley;
        if(name.toLowerCase().indexOf("jennifer gunn") != -1)
            return R.drawable.jennifer;
        if(name.toLowerCase().indexOf("kate salute") != -1)
            return R.drawable.kate;
        if(name.toLowerCase().indexOf("phil linder") != -1)
            return R.drawable.phil;
        if(name.toLowerCase().indexOf("sarah katz") != -1)
            return R.drawable.sarah;
        if(name.toLowerCase().indexOf("thomas rodney") != -1)
            return R.drawable.thomas;
        if(name.toLowerCase().indexOf("tim comer") != -1)
            return R.drawable.tim;
        if(name.toLowerCase().indexOf("walter brown") != -1)
            return R.drawable.walter;
        if(name.toLowerCase().indexOf("michael ponella") != -1)
            return R.drawable.michael;
        if(name.toLowerCase().indexOf("nancy amling") != -1)
            return R.drawable.nancy;
        if(name.toLowerCase().indexOf("dr. gary haber") != -1)
            return R.drawable.haber;
        if(name.toLowerCase().indexOf("vanessa letourneau") != -1)
            return R.drawable.vanessa;
        if(name.toLowerCase().indexOf("ross burman") != -1)
            return R.drawable.ross;
        if(name.toLowerCase().indexOf("lori-stahl-van brackle") != -1)
            return R.drawable.lori_stahl;
        if(name.toLowerCase().indexOf("myrlene michel") != -1)
            return R.drawable.myrlene;
        if(name.toLowerCase().indexOf("jason levy") != -1)
            return R.drawable.jason;
        if(name.toLowerCase().indexOf("gerard aroito") != -1)
            return R.drawable.gerard;
        if(name.toLowerCase().indexOf("amy demarco") != -1)
            return R.drawable.amy;
        if(name.toLowerCase().indexOf("ruth groebner") != -1)
            return R.drawable.ruth;
        if(name.toLowerCase().indexOf("dr. denver j. fowler") != -1)
            return R.drawable.denver;
        if(name.toLowerCase().indexOf("melda n. yildiz") != -1)
            return R.drawable.melda;
        if(name.toLowerCase().indexOf("jesse spevack") != -1)
            return R.drawable.jesse;
        if(name.toLowerCase().indexOf("adam ward") != -1)
            return R.drawable.adam;
        if(name.toLowerCase().indexOf("kyeatta garrett-bey") != -1)
            return R.drawable.kyeatta;
        return R.drawable.nopic;
    }

    public int determineImg(String name) {
        if(name.toLowerCase().indexOf("robert kane") != -1)
               return R.drawable.robert; //replace with robert when picture is added
        return R.drawable.nopic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getRoom() {
        if(room.equals("") || room.equals(null))
            return "TBD";
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getAttending() {
        return attending;
    }

    public void setAttending(String attending) {
        this.attending = attending;
    }

    public String getSession() {
        return session;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLastUpdate() {
        return lastUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public String getImgString() {
        return imgString;
    }

    public void setImgString(String imgString) {
        this.imgString = imgString;
    }

    public int getImg2() {
        return img2;
    }

    public void setImg2(int img2) {
        this.img2 = img2;
    }

    public String getImgString2() {
        return imgString2;
    }

    public void setImgString2(String imgString2) {
        this.imgString2 = imgString2;
    }
}
