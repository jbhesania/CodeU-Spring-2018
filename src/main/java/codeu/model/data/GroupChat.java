package codeu.model.data;

import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GroupChat extends Conversation{
    public HashMap<String, UUID> members;

    /** Constructor for GroupChat
     *
     * @param id UUID of this GroupChat
     * @param ownerID UUID of owner of GroupChat
     * @param title Title of this GroupChat
     * @param creation Instant creation time of this GroupChat
     *
     * Note: this constructor does not add this groupchat to the owner's groupchat map
     */
    public GroupChat(UUID id, UUID ownerID, String title, Instant creation) {
        super(id, ownerID, title, creation);
        members = new HashMap<>();
        members.put(title, ownerID);
    }

    public HashMap<String, UUID> getMembers() {
        return members;
    }

    /** Adds a member to the group chat
     *  as for now anyone can add anyone
     **/
    public void addMember(User member) {
        if (member != null) {
            members.put(getTitle(), member.getId());
            member.addGroupChat(this);
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
        if (member != null && member.getId() != this.owner) {
            members.remove(member.getName());
            member.removeGroupChat(this);
        }
    }

}
