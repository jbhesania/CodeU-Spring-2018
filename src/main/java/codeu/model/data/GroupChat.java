package codeu.model.data;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class GroupChat extends Conversation{
    private HashMap<String, UUID> members;
    private String ownerName;

    /** Constructor for GroupChat
     *
     * @param id UUID of this GroupChat
     * @param ownerId User of owner of GroupChat
     * @param title Title of this GroupChat
     * @param creation Instant creation time of this GroupChat
     * @param ownerName String username of owner of GroupChat
     *
     * Note: this constructor does not add this groupchat to the owner's groupchat map
     */
//    public GroupChat(UUID id, UUID ownerID, String title, Instant creation) {
//        super(id, ownerID, title, creation);
//        members = new HashMap<>();
//        members.put(title, ownerID);
//    }

    public GroupChat(UUID id, UUID ownerId, String title, Instant creation, String ownerName) {
        super(id, ownerId, title, creation);
        members = new HashMap<>();
        members.put(ownerName, ownerId);
        this.ownerName = ownerName;
    }

    public HashMap<String, UUID> getMembers() {
        return members;
    }

    public String getOwnerName() { return ownerName; }

    /** Adds a member to the group chat
     *  as for now anyone can add anyone
     **/
    public void addMember(User member) {
        if (member != null) {
            members.put(member.getName(), member.getId());
        }
    }

    /** Adds a member to the group chat
     *  as for now anyone can add anyone
     *  called from loading in persistent datastore so I do not add this groupchat to the added user's groupchat map
     **/
    public void addMember(String name, UUID userID) {
        if (name != null && userID != null) {
            members.put(name, userID);
        }
    }

    /** Removes a member from the group chat
     *  ideally checks for if a certain user is allowed to remove people is done before calling this
     **/
    public void removeMember(User member) {
        // do not allow leader to remove self from conversation as for now
        if (member != null && !(member.getName().equals(ownerName)) && members.containsKey(member.getName())) {
            members.remove(member.getName());
        }
    }

    public void removeMember(String memberName) {
        if (!memberName.equals(ownerName) && members.containsKey(memberName)) {
            members.remove(memberName);
        }
    }

    public boolean containsMember(User member) {
        return members.containsKey(member.getName());
    }

    public boolean containsMember(String memberName) { return members.containsKey(memberName); }
}
