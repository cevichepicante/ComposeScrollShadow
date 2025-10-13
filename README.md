<article id="28b56141-18f7-80fa-ae1a-d1b1d5bbd7f4" class="page sans">
  <header><h1 class="page-title"></h1><p class="page-description"></p></header>
  <div class="page-body">
    <h1 id="28b56141-18f7-8024-bdde-dabe9fed36f8" class="">Shadow-layered Scaffold implementation for jetpack compose</h1>
    <hr id="28b56141-18f7-80a8-90af-f6876a20b303">
    <h3 id="28b56141-18f7-80ac-8d7e-d8467b92f407" class="">Features</h3>
    <ul id="28b56141-18f7-806e-998a-ff7130068733" class="bulleted-list">
      <li style="list-style-type:disc">Shadow Indication type
        <ul id="28b56141-18f7-80f3-9a1a-db7bf029ac3b" class="bulleted-list">
          <li style="list-style-type:circle">ShadowIndicatedScaffold: always-visible (s<em>upport for: Any Composables)</em></li>
        </ul>
        <ul id="28b56141-18f7-801b-89b1-f9beceab5d12" class="bulleted-list">
          <li style="list-style-type:circle">ShadowIndicatedScrollScaffold: when scroll is processing. (s<em>upport for: LazyColumn, LazyRow)</em></li>
        </ul>
      </li>
    </ul>
    <ul id="28b56141-18f7-8078-b7f3-e5f064e8be6b" class="bulleted-list">
      <li style="list-style-type:disc"><strong>Customizable shadowed layer</strong>: color, shape, blur weight, direction</li>
    </ul>
    <h3 id="28b56141-18f7-8085-828d-d605c391125c" class="">Sample</h3>
    <ul id="28b56141-18f7-80a4-8813-ea401be129f9" class="block-color-default bulleted-list">
      <li style="list-style-type:disc">ShadowIndicatedScaffold <div id="28b56141-18f7-80bd-a0ac-f52c246ffa79" class="column-list">
        <div id="28b56141-18f7-8083-9dee-fc2f8ccea27f" style="width:50%" class="column">
          <p id="28b56141-18f7-80a5-902d-c199ca113afe" class="">Without</p>
          <figure id="28b56141-18f7-80ee-bd12-ffd46e8ab991" class="image">
            <a href="%EC%A0%9C%EB%AA%A9%20%EC%97%86%EC%9D%8C%2028b5614118f780faae1ad1b1d5bbd7f4/SwipingPhoto_BeforeShadow.jpg">
              <img style="width:288px" src="https://github.com/user-attachments/assets/872f54c2-e097-4feb-9c37-792c77a0b767">
            </a>
          </figure>
          <p id="28b56141-18f7-8024-93da-eabc02d5049e" class=""></p>
        </div>
        <div id="28b56141-18f7-8035-996b-d4ac1f6e1a59" style="width:50%" class="column">
          <p id="28b56141-18f7-80b0-884d-f2675d0df4dc" class="">With</p>
          <figure id="28b56141-18f7-8044-8daa-f374e11a080c" class="image">
            <a href="%EC%A0%9C%EB%AA%A9%20%EC%97%86%EC%9D%8C%2028b5614118f780faae1ad1b1d5bbd7f4/SwipingPhoto_AfterShadow.jpg">
              <img style="width:288px" src="https://github.com/user-attachments/assets/659eed51-13ba-443e-96c6-0a1cd53d5ce2"></a>
          </figure>
        </div>
      </div>
      </li>
    </ul>
    <ul id="28b56141-18f7-80f6-a5d8-d8b2add36e32" class="bulleted-list">
      <li style="list-style-type:disc">ShadowIndicatedScrollScaffold
        <div id="28b56141-18f7-8020-a150-f276d5596463" class="column-list">
          <div id="28b56141-18f7-8070-9692-f7015c70b66b" style="width:50%" class="column">
            <p id="28b56141-18f7-8087-9e80-e8f37bdb4c8d" class="">WIthout</p>
            <figure id="28b56141-18f7-806d-8df2-ec8945fa40a3" class="image">
              <a href="%EC%A0%9C%EB%AA%A9%20%EC%97%86%EC%9D%8C%2028b5614118f780faae1ad1b1d5bbd7f4/Chat_BeforShadow.jpg">
                <img style="width:288px" src="https://github.com/user-attachments/assets/85227655-1214-43c3-b526-bd18f9303e7e">
              </a>
            </figure>
          </div>
          <div id="28b56141-18f7-8078-b067-c7365aa7f41b" style="width:50.00000000000004%" class="column">
            <p id="28b56141-18f7-8032-b363-cbcd98deb7e1" class="">With</p>
            <figure id="28b56141-18f7-8003-aa8e-f043192e070f" class="image">
              <a href="%EC%A0%9C%EB%AA%A9%20%EC%97%86%EC%9D%8C%2028b5614118f780faae1ad1b1d5bbd7f4/ChatScreenScrollAction.gif">
                <img style="width:288px" src="https://github.com/user-attachments/assets/8bd820db-3b7e-4ca7-a491-9b74e8f2c99d">
              </a>
            </figure>
          </div>
        </div>
      </li>
    </ul>
    <h3 id="28b56141-18f7-80a1-8c57-f3a9dec0008c" class="">Installation</h3>
    <p id="28b56141-18f7-80bc-8106-e1536e42f7f7" class="">Add it in settings.gradle:</p>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism.min.css" integrity="sha512-tN7Ec6zAFaVSG3TpNAKtk4DOHNpSwKHxxrsiw4GHKESGPs5njn/0sMCUMl2svV4wo4BK/rCP7juYz+zx+l6oeQ==" crossorigin="anonymous" referrerpolicy="no-referrer">
    <pre id="28b56141-18f7-80bf-a469-d77260c4c267" class="code code-wrap language-plain" tabindex="0">
      <code class="Text language-plain" style="white-space:pre-wrap;word-break:break-all">
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
      </code>
    </pre>
    <p id="28b56141-18f7-809d-a7e2-e19ad44297c6" class="">Add it in app level build.gradle:</p>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism.min.css" integrity="sha512-tN7Ec6zAFaVSG3TpNAKtk4DOHNpSwKHxxrsiw4GHKESGPs5njn/0sMCUMl2svV4wo4BK/rCP7juYz+zx+l6oeQ==" crossorigin="anonymous" referrerpolicy="no-referrer">
    <pre id="28b56141-18f7-8016-b28b-c20cd3bcf3d6" class="code code-wrap language-plain" tabindex="0">
      <code class="Text language-plain" style="white-space:pre-wrap;word-break:break-all">
dependencies {
    implementation 'com.github.cevichepicante:ComposeScrollShadow:v1.0.0'
}
      </code>
    </pre>
  </div>
</article>
