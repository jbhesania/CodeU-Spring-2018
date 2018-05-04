// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.data;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.HashSet;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String password;
  private final Instant creation;
  private final HashMap<String, UUID> followingMap;
  private boolean admin;
  private final HashSet<GroupChat> groupChatSet;

  /**
   * Constructs a new User. Makes user follow themselves by default. 
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password the password of this User
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, String password, Instant creation) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.creation = creation;
    this.followingMap = new HashMap<String, UUID>();
    this.groupChatSet = new HashSet<GroupChat>();
    followingMap.put(name, id);

    List<String> admins = Arrays.asList("lloza", "cari", "joyaan", "linda");
    this.admin = admins.contains(name);
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the passsword of this User. */
  public String getPassword() {
    return password;
  }
 
  /** Returns the followingMap of this User. */
  public HashMap<String, UUID> getFollowingMap() {
    return followingMap;
  }
  
  /** Checks if userId exists in Hashmap using UUID. */
  public boolean follows(UUID id) {
    return followingMap.containsValue(id);  
  }

  /** Checks if userId exists in Hashmap using userName, valid because username
   *  is required to be unique. */
  public boolean follows(String name) {
    return followingMap.containsKey(name);  
  }

  /** Adds follower to HashMap. */
  public void follow(User toFollow) {
    followingMap.put(toFollow.getName(), toFollow.getId()); 
  }

  /** Unfollows user */
  public void unfollow(User toUnfollow) {
    followingMap.remove(toUnfollow.getName(), toUnfollow.getId());
  }

  /** Adds the name and UUID to to HashMap to follow */
  public void follow(String name, UUID id) {
    followingMap.put(name, id);
  }

  public boolean isAdmin() {return admin;}

  public void makeAdmin() {admin = true;}

  public void removeAdmin() {admin = false;}

  /** checks if user is in the group chat and returns true if user is **/
  public boolean inGroupChat(GroupChat groupChat) { return groupChatSet.contains(this); }

  /** adds a groupchat to the set of groupchats the user is in **/
  public void addGroupChat(GroupChat groupChat) {
    if (groupChat != null) {
      groupChatSet.add(groupChat);
    }
  }

  /** removes a groupchat from the set of groupchats the user is in**/
  public void removeGroupChat(GroupChat groupChat) {
    groupChatSet.remove(groupChat);
  }

  /** returns the list of group chats that the user is in **/
  public HashSet<GroupChat> getGroupChatSet() {
    return groupChatSet;
  }

}
