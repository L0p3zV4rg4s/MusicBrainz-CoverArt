# API - MusicBrainz-CoverArt
Search on the MusicBrainz Database the Cover Art.
Features:
* A free API to search Cover Art.

## Use
### CoverArt.java

| Modifier and Type | Method | Description |
| --- | --- | --- |
| --- | **Constructor** | --- |
| --- | CoverArt () | Create a new CoverArt obj |
| --- | CoverArt (boolean) | Create a new CoverArt obj with a *verbose option* |
| --- | **Method** | --- |
| *void* | aHref (String singer, String song, int page) | Search in MusicBrainz on page (page) for a singer and song |
| *PictureBigSmall* | coverBig () | Return a PictureBigSmall obj with a value from website as a Array |

### PictureBigSmall.java
| Modifier and Type | Method | Description |
| --- | --- | --- |
| --- | **Constructor** | --- |
| --- | PictureBigSmall (int) | Create a new PictureBigSmall with an capacity array as (int) |
| --- | **Method** | --- |
| *String* | getAllPictureHtml (String singer, String song) | output a HTML Document |


## Example

```java
String singer = "Michael Jackson";
String song = "Thriller";
//Search verbose mode - False silently mode
CoverArt myCover = new CoverArt(true);     

//Search on page 1, for more pages, please call method aHref again
myCover.aHref(singer, song, 1); 

PictureBigSmall myPic =  myCover.coverBig();

if (myPic != null) //To prevent a null myPic obj
    System.out.println(myPic.getAllPictureHtml(singer, song)); //Output optional
    
/**
* Another option: save the output getAllPicture(...) on a HTML file
*/
