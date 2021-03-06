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

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class GroupChatTest {

  @Test
  public void testCreate() {
    //creating user variables to make leader
    UUID idUser = UUID.randomUUID();
    String name = "leader";
    String password = "password";
    Instant created = Instant.now();

    User user = new User(idUser, name, password, created);

    //creating group chat variables
    UUID id = UUID.randomUUID();
    String title = "Test_Title_Group";
    Instant creation = Instant.now();

    //hashmap for testing
    HashMap<String, UUID> testMap = new HashMap<>();
    testMap.put(name, idUser);

     GroupChat groupChat = new GroupChat(id, idUser, title, creation, name);

     Assert.assertEquals(id, groupChat.getId());
     Assert.assertEquals(idUser, groupChat.getOwnerId());
     Assert.assertEquals(title, groupChat.getTitle());
     Assert.assertEquals(creation, groupChat.getCreationTime());
     Assert.assertEquals(testMap, groupChat.getMembers());

  }


}
