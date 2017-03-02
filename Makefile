debug:
	./gradlew assembleDebug

debug-install:
	adb install -r app/build/outputs/apk/app-debug.apk

uninstall:
	adb uninstall com.example.helloworld

release: HelloWorld.keystore
	./gradlew clean assembleRelease

release-install:
	adb install -r app/build/outputs/apk/app-release.apk

clean:
	./gradlew clean

HelloWorld.keystore:
	@echo 'creating signing keystore...'
	keytool -genkey -v -storepass password -keypass password -keystore HelloWorld.keystore -alias helloworld -dname 'CN=helloworld,O=example.com' -keyalg RSA -keysize 2048 -validity 10000

retrace:
	cmd /c "%ANDROID_HOME%/tools/proguard/bin/retrace app/build/outputs/mapping/release/mapping.txt stacktrace.txt"
