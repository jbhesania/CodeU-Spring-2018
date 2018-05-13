<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!DOCTYPE html>
<html>
<head>
  <title>Byte Me's Chat App</title>
  <link rel="stylesheet" href="/css/main.css">
  <style>
    #container{
      background-color: rgba(129, 168, 37, 0.5);
      border-radius: 15px;
    }
    .photo{
      width: 100px;
      height: 100px;
    }
  </style>
</head>
<body>

  <jsp:include page="/WEB-INF/nav.jsp" />

    <div
      style="width:70%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>Team Byte Me</h1>    
      <ul>
        <img src="saveen.jpg" class = "photo"><li><strong>Saveen Sahni</strong></li>
        <p>Fearless Leader/Bollywood Champion</p>
        <img src="luis.jpg" class = "photo"><li><strong>Luis Loza</strong></li>
        <p>Luis is currently a sophomore at the University of Southern California majoring in Physics/Computer Science and minoring in Cinematic Arts. Aside from taking more naps than he should, Luis spends most of his spare time on Reddit, playing video games, attending concerts, or playing the violin. His favorite artists are Lana del Rey, Vampire Weekend, and Lorde, but he can listen to basically anything except country (don't @ him). Aside from his passion for STEM and the arts, Luis has an undying love for avocado (yes he will pay extra). Hit him up on chat if you ever want to discuss film, music, gaming, or anime.</p>
        <img src="cari.jpg" class = "photo"><li><strong>Cari Gan</strong></li>
        <p>Cari is a junior at UC Berkeley studying computer science. Her favorite artists include Mura Masa and Malaa, but she'll often mindlessly scroll through soundcloud without paying attention to the artists. When she's not sleeping 12/24 hours of the day, you can find her watching netflix, playing Catan with friends, and dancing (the only physical activity she can sustainably do). Avocados? more like avoca-no!!! but only because they're $1.50 for half of one at the poke place she works at (not worth, get the seaweed salad instead).</p>
        <img src="joyaan.jpg" class = "photo"><li><strong>Joyaan Bhesania</strong></li>
        <p>Joyaan studies Computer Science at UCSD when she's not busy sleeping or dancing. She loves to try new sports and activities, most recently aerial silks and gymnastics. While she's been on a bit of a Post Malone binge, she usually listens to acoustic music (if you haven't listened to Brooklyn Duo please do so). Cute animals and perfect avocados are life's true beauties.</p>
        <img src="linda.jpg" class = "photo"><li><strong>Linda Yang</strong></li>
        <p>Linda is currently a sophomore at UCSD studying Computer Science. In her spare time she likes to sleep, eat, and spend countless hours browsing memes online. She also loves to dance, draw and lose in League of Legends. Ice cream and boba also flow through her veins (not sure if that's healthy but ... ) and prefers udon to any other type of noodle (udon is the best noodle ChAnGE mY MinD). She also doesn't even like avocado that much so ya :^)  byE</p>
      </ul>
    </div>
</body>
</html>
