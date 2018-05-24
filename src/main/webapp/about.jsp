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
    h1{
      text-align: center;
    }

    #container{
      background-color: rgba(129, 168, 37, 0.5);
      border-radius: 15px;
    }

    .photo{
      width: 100px;
      height: 100px;
    }

    * {
      box-sizing: border-box;
    }

    /* Set a background color */
    body {
      background-color: #474e5d;
      font-family: Helvetica, sans-serif;
    }

    /* The actual timeline (the vertical ruler) */
    .timeline {
      position: relative;
      max-width: 1200px;
      margin: 0 auto;
      background-color: #474e5d00;
    }

    /* The actual timeline (the vertical ruler) */
    .timeline::after {
      content: '';
      position: absolute;
      width: 6px;
      background-color: white;
      top: 0;
      bottom: 0;
      left: 50%;
      margin-left: -3px;
      border-radius: 15%;
    }

    /* Container around content */
    .container {
      padding: 10px 20px;
      position: relative;
      background-color: inherit;
      width: 50%;
    }

    /* The circles on the timeline */
    .container::after {
      content: '';
      position: absolute;
      width: 25px;
      height: 25px;
      right: -17px;
      background-color: white;
      border: 4px solid rgb(55, 138, 0);
      top: 15px;
      border-radius: 50%;
      z-index: 1;
    }

    /* Place the container to the left */
    .left {
      left: 0;
    }

    /* Place the container to the right */
    .right {
      left: 50%;
    }

    /* Add arrows to the left container (pointing right) */
    .left::before {
      content: " ";
      height: 0;
      position: absolute;
      top: 22px;
      width: 0;
      z-index: 1;
      right: 30px;
      border: medium solid white;
      border-width: 10px 0 10px 10px;
      border-color: transparent transparent transparent white;
    }

    /* Add arrows to the right container (pointing left) */
    .right::before {
      content: " ";
      height: 0;
      position: absolute;
      top: 22px;
      width: 0;
      z-index: 1;
      left: 30px;
      border: medium solid white;
      border-width: 10px 10px 10px 0;
      border-color: transparent white transparent transparent;
    }

    /* Fix the circle for containers on the right side */
    .right::after {
      left: -16px;
    }

    /* The actual content */
    .content {
      padding: 20px 30px;
      background-color: white;
      position: relative;
      border-radius: 6px;
    }

    /* Media queries - Responsive timeline on screens less than 600px wide */
    @media screen and (max-width: 600px) {
    /* Place the timelime to the left */
      .timeline::after {
        left: 31px;
      }

    /* Full-width containers */
      .container {
        width: 100%;
        padding-left: 70px;
        padding-right: 25px;
      }

    /* Make sure that all arrows are pointing leftwards */
      .container::before {
        left: 60px;
        border: medium solid white;
        border-width: 10px 10px 10px 0;
        border-color: transparent white transparent transparent;
      }

    /* Make sure all circles are at the same spot */
      .left::after, .right::after {
        left: 15px;
      }

    /* Make all right containers behave like the left ones */
      .right {
        left: 0%;
      }
      
  }
  </style>
</head>
<body>

  <jsp:include page="/WEB-INF/nav.jsp" />

    <div style="width:70%; margin-left:auto; margin-right:auto; margin-top: 50px;">

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

      <h1>History of Avo-Chat-O</h1>
      <div class="timeline">
        <div class="container left">
          <div class="content">
            <h2>March 6, 2018</h2>
            <p>First merge to Github</p>
          </div>
        </div>
        <div class="container right">
          <div class="content">
            <h2>March 22, 2018</h2>
            <p>User registration now available</p>
          </div>
        </div>
        <div class="container left">
          <div class="content">
            <h2>March 24, 2018</h2>
            <p>Admin Page created</p>
          </div>
        </div>
        <div class="container right">
          <div class="content">
            <h2>April 15, 2018</h2>
            <p>Users now have their own profile pages</p>
          </div>
        </div>
        <div class="container left">
          <div class="content">
            <h2>April 18, 2018</h2>
            <p>Users with admin privileges can now view site statistics</p>
          </div>
        </div>
        <div class="container right">
          <div class="content">
            <h2>April 19, 2018</h2>
            <p>Activity feed is now available to all users</p>
          </div>
        </div>
        <div class="container left">
          <div class="content">
            <h2>April 20, 2018</h2>
            <p>Data can now be imported through the Admin Page</p>
          </div>
        </div>
        <div class="container right">
          <div class="content">
            <h2>April 23, 2018</h2>
            <p>The reign of the avocado begins</p>
          </div>
        </div>
        <div class="container left">
          <div class="content">
            <h2>May 13, 2018</h2>
            <p>Users can now create a private group chat accesible only to those in the group</p>
          </div>
        </div>
        <div class="container right">
          <div class="content">
            <h2>May 23, 2018</h2>
            <p>Users can now log out</p>
          </div>
        </div>
      </div>

  </div>
</body>
</html>
