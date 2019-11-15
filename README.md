# react-native-wallpaper
Set Wallpaper with react-native (Android only)
## Install

    yarn add https://github.com/inuyashaaa/react-native-wallpaper.git
## Link
### If version < 0.60
    react-native link react-native-walle
### If version >= 0.60
    Auto link
## Usage

    import RNWalle from "react-native-walle";
    const listOption = ['system', 'lock', 'both']
    
    RNWalle.setWallPaper("https://images.pexels.com/photos/799443/pexels-photo-799443.jpeg", listOption[0], function(res) {
      console.log(res);
      //res : 'success'
      //res : 'failed' or 'reason to fail while trying to set wallpaper'
    });
    
# Feel free to Contribute
This repo is update from [https://github.com/thecodrr/react-native-wallpaper.git](https://github.com/thecodrr/react-native-wallpaper.git)
