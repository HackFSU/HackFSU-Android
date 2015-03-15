# HackFSU-Android
###By TechNole

ToDo:

**General Problems**
  - App shutdown when screen is locked
  - splash is always called instead of only on the initial start
  - progressDialog messages are delayed by one screen when switching tabs
        due to it being run on onPreExecute()
            schedule's is seen when switching from count to update
            CountDown's is seen when switching from schedule to update


**Countdown:**
  - Force update every time it switches to Countdown screen. **was already
        doing this. *Read progressDialog prob in gen prob above
  - Make it prettier. need to check with Iosif
  - Check which parsed item will be used for the countdown timer & ring
  
  
**Updates:**
  - Change font style
  
  
**Map:**
  - Add Dirac Pictures by changing/adding src in activity_map.xml
  - maybe instead add webView
  
  
**Sponsors:**
  - Change to new Sponsors
  
  
**SplashScreen:**
  - Change to new design.
