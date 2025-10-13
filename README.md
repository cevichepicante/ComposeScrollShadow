# Shadow-layered Scaffold implementation for jetpack compose


### Features

- **Shadow Indication type**
    - ShadowIndicatedScaffold: always-visible *(support for: Any Composables)*
    - ShadowIndicatedScrollScaffold: when scroll is processing. *(support for: LazyColumn, LazyRow)*
- **Customizable shadowed layer**: color, shape, blur weight, direction
<br>

### Sample

- <a href="https://github.com/cevichepicante/ComposeScrollShadow/blob/c2e2f46533e1eca54cd93d46ca90db9c4f43fa0f/app/src/main/java/com/cevichepicante/composescrollshadow/ui/ReadMeSample.kt#L279">ShadowIndicatedScaffold</a>

<center>
<table>
<tr style="border: none;">
  <td style="text-align: center; border: none;">
    <p>Without</p>
    <img src="https://github.com/user-attachments/assets/5c540f2d-e3cd-443d-a047-d935db516ec7" width="300">
  </td>
  <td style="text-align: center; border: none;">
    <p>With</p>
    <img src="https://github.com/user-attachments/assets/767cc9d2-e831-48ed-963a-316903817dc0" width="300">
  </td>
</tr>
</table>
</center>
<br>

- <a href="https://github.com/cevichepicante/ComposeScrollShadow/blob/c2e2f46533e1eca54cd93d46ca90db9c4f43fa0f/app/src/main/java/com/cevichepicante/composescrollshadow/ui/ReadMeSample.kt#L60">ShadowIndicatedScrollScaffold</a>

<center>
<table>
<tr style="border: none;">
  <td style="text-align: center; border: none;">
    <p>Without</p>
    <img src="https://github.com/user-attachments/assets/97ec94e8-d78a-40cd-bd05-4af81e374950" width="300">
  </td>
  <td style="text-align: center; border: none;">
    <p>With</p>
    <img src="https://github.com/user-attachments/assets/71b78ca1-9142-43fc-849d-cec8f9b15fea" width="300">
  </td>
</tr>
</table>
</center>
<br>

### Installation

Add it in settings.gradle:

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add it in app level build.gradle:

```
dependencies {
    implementation 'com.github.cevichepicante:ComposeScrollShadow:1.0.1'
}
```


