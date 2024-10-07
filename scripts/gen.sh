#!/bin/bash

function execTool() {
  java -jar scripts/tools/gentool.jar "$1" "$2" "$srcDir" "$3"
}

if [ "$1" = "crud" ]; then
  srcDir="examples/crudexample"
  execTool "$1" "$2" "."
elif [ "$1" = "module" ]; then
  srcDir="examples/exampleApp"
  execTool "$1" "$2" "./"
  mv "./$2" "$2App"

  srcDir="examples/exampleIOS"
  execTool "$1" "$2" "./"
  mv "$2" "./$2IOS"

  srcDir="examples/shared/example"
  execTool "$1" "$2" "shared/src/commonMain/kotlin/modules"
  mv shared/src/commonMain/kotlin/modules/"$2" shared/src/commonMain/kotlin/modules/"$2"Module

  srcDir="examples/shared/exampleAndroid"
  execTool "$1" "$2" "shared/src/androidMain/kotlin/modules"
  mv shared/src/androidMain/kotlin/modules/"$2" shared/src/androidMain/kotlin/modules/"$2"Module

  srcDir="examples/shared/exampleIOS"
  execTool "$1" "$2" "shared/src/iosMain/kotlin/modules"
  mv shared/src/iosMain/kotlin/modules/"$2" shared/src/iosMain/kotlin/modules/"$2"Module
  cp examples/exampleIOS/iosApp/Assets.xcassets/AppIcon.appiconset/app-icon-1024.png "$2"IOS/iosApp/Assets.xcassets/AppIcon.appiconset/app-icon-1024.png

  echo -e "\ninclude(\":$2App\")" >> ./settings.gradle.kts

elif [ "$1" = "migration" ]; then
  echo "-- $(date "+%b %d, %Y")" >"app/src/main/resources/db/migration/V$(date +%s)__$2.sql"
  echo "Migration Created!"
else
  echo "Must specify type of generated asset. i.e. crud | module | migration"
fi
