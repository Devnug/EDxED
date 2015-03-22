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
    private int img;

    public ViewModel() {
        name = "";
        title = "";
        desc = "";
        room = "";
        img = determineImg();
    }

    public ViewModel(String theName, String theTitle, String theDesc, String theImg, String theAttending) {
        name = theName;
        title = theTitle;
        desc = theDesc;
        room = "TBD";
        session = "Session 1";
        attending = theAttending;
        // Test with phil...for now...
        img = determineImg();
    }

    public int determineImg() {
        if(name.toLowerCase().indexOf("anne taliaferro") != -1)
            return R.drawable.anne;
        if(name.toLowerCase().indexOf("christopher purcell") != -1)
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
}
