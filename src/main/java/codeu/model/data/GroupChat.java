package codeu.model.data;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class GroupChat extends Conversation{
    public HashSet<User> members;
    public User leader;

    public GroupChat(UUID id, UUID ownerID, User owner, String title, Instant creation) {
        super(id, ownerID, title, creation);
        members = new HashSet<>();
        members.add(owner);
        leader = owner;
        owner.addGroupChat(this);
    }

    /** Returns leader of group chat **/
    public User getLeader() {
        return leader;
    }

    public HashSet<User> getMembers() {
        return members;
    }

    /** Adds a member to the group chat
     *  as for now anyone can add anyone
     **/
    public void addMember(User member) {
        if (member == null) {
            return;
        }
        members.add(member);
        member.addGroupChat(this);
    }

    /** Removes a member from the group chat
     *  ideally checks for if a certain user is allowed to remove people is done before calling this
     **/
    public  void removeMember(User member) {
        // do not allow leader to remove self from conversation as for now
        if (member != null && member != this.leader) {
            members.remove(member);
            member.removeGroupChat(this);
        }
    }

}
