package org.CyfrSheets.models;

import org.CyfrSheets.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Entity
public class SEvent {

    // Class name short for Scheduled Event

    @Id
    @GeneratedValue
    private int id;

    @Autowired
    private UserDao userDao;

    private static int nextTempId = -1;

    private int creatorId;
    private boolean hasUserCreator;

    private byte[] creatorKey;
    // mod keys. Don't worry about this for now and delete if you decide not to use it future me. Sincerely, past you.
    private ArrayList<byte[]> opKeys;

    // Possible elevated users field in future - save as ID
    //private ArrayList<Integer> opsIDs;

    private EventType type;
    private EventTime time;

    /**Ideally phasing out the participant interface.
     *
     * private ArrayList<Participant> participants = new ArrayList<>();
     *
     */

    // Refactoring TempUser as a series of hashmaps within this so I don't have to worry about private interfaces
        // Usernames to ID keys
    private HashMap<Integer, String> tempUserNames = new HashMap<>();
        // Passwords to ID keys
    private HashMap<Integer, byte[]> tempUserPasses = new HashMap<>();

    private HashMap<Integer, byte[]> tempUserSalts = new HashMap<>();

    private ArrayList<byte[]> possibleCreatorSalts = new ArrayList<>();

    @ManyToMany
    private List<User> regUsers;

    private HashMap<Integer, String> allUserNames = new HashMap<>();

    private HashMap<Integer, AttendingStatus> attendanceStatus = new HashMap<>();

    private String name;

    private String desc;

    private byte[] badByte = {-1, -1, -1};

    // Possible location field goes here

    // User creator constructor
    public SEvent(EventType type, EventTime time, String name, String desc, User creator) {
        this.name = name;
        this.type = type;
        this.time = time;
        this.desc = desc;
        hasUserCreator = true;
        creatorId = creator.getID();
        createKey();
    }

    // Nonuser creator constructor
    public SEvent(EventType type, EventTime time, String name, String desc, String creatorName, String creatorPass) {
        this.name = name;
        this.type = type;
        this.time = time;
        this.desc = desc;
        hasUserCreator = false;
        creatorId = nextTempId;
        // make new temp user to correspond with creator here
        nextTempId--;
    }

    public SEvent() { }

    public void addUser(User u) { regUsers.add(u); }

    public void addTempUser(String name, String pass) {
        tempUserNames.put(nextTempId, name);
        tempUserSecurePass(name, pass);
        nextTempId--;
    }

    public void addTempUser(String name) {

    }

    public void addParticipant(String name, String pass) {
        // Login verification boolean - Find some way to pass this in properly once sessions/cookies are figured out
        boolean loggedInUser = true;
        boolean isUser = false;
        int uID = -1;
        for (User u : userDao.findAll()) {
            if (u.checkName(name) && u.checkPassword(pass) && loggedInUser) {
                isUser = true;
                uID = u.getID();
            }
        }
        if (pass.equals("") || pass == null) addTempUser(name);
        else if(isUser) addUser(userDao.findOne(uID));
        else addTempUser(name, pass);
    }

    public boolean tempHasPass(int id) { return tempUserPasses.containsKey(id); }
    public boolean passCheck(int id, String pass) {
        if (id == creatorId) {
            // Use unique creator password hashing
            return checkKey(id, pass);
        } else {
            // Use standard tempUser hashing
            return checkPass(id, pass);
        }
    }

    // TODO: implement name and description changes. Reliant on ability to maintain session/pass participants along. Noncritical

    /**
    public boolean changeName(String name)  {
        Participant test = new Participant();
        try {
            userCheck(test);

            return true;
        } catch(UnregisteredUserException e){

            return false;
        }
    }

     public boolean changeDesc(String desc) {
        // Implement
        return false;
     }
     */

    // Essentially unnecessary until the above is implemented
    protected boolean creatorCheck(User u) { return u.isEqual(userDao.findOne(creatorId)); }

    protected boolean creatorCheck(String name) {
        if (tempUserNames.containsValue(name)) {
            return (tempUserNames.get(creatorId).equals(name));
        }
        return false;
    }

    protected boolean creatorCheck(Integer id) { return creatorId == id; }

    // Creating creator key/hash
    protected void createKey() { if (hasUserCreator) creatorKey = securePass("",true,false); }

    // creating tempUser creator key/hash
    protected void createKey(String cName, String cPass) {
        if(hasUserCreator) return;
        byte[] passByte = securePass(cPass, false, true);
        if (passByte == badByte) while (passByte == badByte) passByte = securePass(cPass, false, true);
        creatorKey = passByte;
        tempUserSecurePass(cName, cPass);
    }

    protected void tempUserSecurePass(String name, String pass) {
        byte[] passByte = securePass(pass, false, false);
        if (passByte == badByte) while (passByte == badByte) passByte = securePass(pass, false, false);
        tempUserPasses.put(nextTempId, passByte);
    }

    protected byte[] securePass(String pass, boolean uKey, boolean tKey) {
        byte[] salt = getSalt();
        if (salt == badByte) return badByte;
        User u = null;
        if (uKey) {
            u = userDao.findOne(creatorId);
            pass = this.name + u.getName() + creatorId;
            u.passTheSalt(salt);
        }
        if (tKey) {
            pass += this.name + this.creatorId + this.id;
            throwTheShaker(salt);
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            if (uKey || tKey) md.update(salt);
            md.update(pass.getBytes());
            if (!(uKey || tKey)) md.update(salt);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("How the hell did you botch this one?");
        }
        return badByte;
    }

    protected byte[] getSalt() {
        try {
            SecureRandom sR = SecureRandom.getInstance("SHA1PRNG");
            byte[] saltByte = new byte[32];
            sR.nextBytes(saltByte);
            return saltByte;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("How the hell did you botch this one?");
        }
        return new byte[] {-1, -1, -1};
    }

    // check user key
    protected boolean checkKey(User u) {
        try {
            String pass = this.name + u.getName() + creatorId;
            for (byte[] salt : u.giveTheShaker(this.id)) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(salt);
                byte [] checkIt = md.digest(pass.getBytes());
                if (byteCheck(checkIt, creatorKey)) return true;
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    // check tempuser key
    protected boolean checkKey(int id, String passKey) {
        try {
            String pass = passKey + this.name + this.creatorId + this.id;
            boolean keyCorrect = false;
            for (byte[] salt : this.possibleCreatorSalts) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(salt);
                byte[] checkIt = md.digest(pass.getBytes());
                if (byteCheck(checkIt, creatorKey)) keyCorrect = true;
            }
            if (!keyCorrect) return false;
            return checkPass(id, passKey);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    protected boolean checkPass(int id, String pass) {
        try {
            byte[] salt = tempUserSalts.get(id);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pass.getBytes());
            return byteCheck(md.digest(salt), tempUserPasses.get(id));
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    protected void throwTheShaker(byte[] salt) {
        try {
            Random r = new SecureRandom();
            SecureRandom sR = SecureRandom.getInstance("SHA1PRNG");
            int seed = r.nextInt(100);
            for (int i = 0; i < 100; i++) {
                if (seed == i) possibleCreatorSalts.add(salt);
                else {
                    byte[] nextByte = new byte[32];
                    sR.nextBytes(nextByte);
                    possibleCreatorSalts.add(nextByte);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Please stop finding ways to break this, past me. Sincerely, future you.");
        }
    }

    protected boolean byteCheck (byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        boolean noBreak = true;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                noBreak = false;
                break;
            }
        }
        if (noBreak) return true;
        return false;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDesc() { return desc; }
    public EventType getType() { return type; }
    public EventTime getTime() { return time; }
    public HashMap<Integer, String> getAttendees() { return allUserNames; }
    public HashMap<Integer, String> getTempUsers() { return tempUserNames; }
    public HashMap<Integer, AttendingStatus> getAttendance() { return attendanceStatus; }
    public List<User> getRegUsers() { return regUsers; }
}

enum AttendingStatus {

    YES (1),
    MAYBE (0),
    NO (-1);

    private final int state;

    private AttendingStatus(int state) { this.state = state; }

    public boolean hardYes() { return state == 1; }
    public boolean maybeAttending() { return state == 0; }
    public boolean notAttending() { return !(hardYes() || maybeAttending()); }
}